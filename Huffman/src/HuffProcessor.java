import java.util.PriorityQueue;

/**
 * Although this class has a history of several years,
 * it is starting from a blank-slate, new and clean implementation
 * as of Fall 2018.
 * <P>
 * Changes include relying solely on a tree for header information
 * and including debug and bits read/written information
 * 
 * @author Ow	en Astrachan
 *
 * Revise
 */

public class HuffProcessor {

	private class HuffNode implements Comparable<HuffNode> {
		HuffNode left;
		HuffNode right;
		int value;
		int weight;

		public HuffNode(int val, int count) {
			value = val;
			weight = count;
		}
		public HuffNode(int val, int count, HuffNode ltree, HuffNode rtree) {
			value = val;
			weight = count;
			left = ltree;
			right = rtree;
		}

		public int compareTo(HuffNode o) {
			return weight - o.weight;
		}
	}

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); 
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;

	private boolean myDebugging = false;
	
	public HuffProcessor() {
		this(false);
	}
	
	public HuffProcessor(boolean debug) {
		myDebugging = debug;
	}

	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void compress(BitInputStream in, BitOutputStream out){


		// remove all this code when implementing compress
		// while (true){
		// 	int val = in.readBits(BITS_PER_WORD);
		// 	if (val == -1) break;
		// 	out.writeBits(BITS_PER_WORD, val);
		// }
		// out.close();
		int[] counts=getcounts(in);
		System.out.println(counts[0]); 
		HuffNode root=makeTree(counts);
		in.reset();
		out.writeBits(BITS_PER_INT, HUFF_TREE);
		writeTree(root,out);
		String[] encodings=new String[ALPH_SIZE+1];
		makeEncoding(root, " ", encodings);
		while(true){
			//reading one bit of the input file at a time g
			int bits = in.readBits(BITS_PER_WORD);
			//if doesn't run check down here, how to go from ASCII to the letter itself in encodings
			String code=encodings[bits];
			out.writeBits(code.length(), Integer.parseInt(code, 2));
			if(bits==-1){
				String finals=encodings[PSEUDO_EOF];
				out.writeBits(finals.length(), Integer.parseInt(code, 2));
				break;
			}
		}
		out.close();
	}

	private void makeEncoding(HuffProcessor.HuffNode root, String path, String[] encodings) {
		if(root.left==null && root.right==null){
			encodings[root.value]=path;
			return;
		}
		else{
			makeEncoding(root.left, path+"0", encodings);
			makeEncoding(root.right, path+"1", encodings);
		}
	}

	private void writeTree(HuffProcessor.HuffNode root, BitOutputStream out) {
		if(root.left!=null || root.right!=null){
			//internal nodes be like
			out.writeBits(1, 0);;
			writeTree(root.left, out);
			writeTree(root.right, out);
		}
		else{
			out.writeBits(BITS_PER_WORD+1, 1);
		}
	}

	private HuffProcessor.HuffNode makeTree(int[] counts) {
		PriorityQueue<HuffNode> pq=new PriorityQueue<>();
		for(int i=0;i<counts.length;i++){
			if(counts[i]>0){
				pq.add(new HuffNode(i, counts[i],null,null));
			}
		}
		pq.add(new HuffNode(PSEUDO_EOF, 1,null,null));
		while(pq.size()>1){
			HuffNode left=pq.remove();
			HuffNode right=pq.remove();
			HuffNode bigger= new HuffNode(0, left.weight+right.weight,left,right);
			pq.add(bigger);
		}
		return pq.remove();
	}

	private int[] getcounts(BitInputStream in) {
		int[] counts = new int[ALPH_SIZE];
		int values = in.readBits(BITS_PER_WORD);
		while (values != -1) {
			counts[values]++;
			values = in.readBits(BITS_PER_WORD);
		}
		return counts;
	}





























	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void decompress(BitInputStream in, BitOutputStream out){

		// remove all code when implementing decompress

		// while (true){
		// 	int val = in.readBits(BITS_PER_WORD);
		// 	if (val == -1) break;
		// 	out.writeBits(BITS_PER_WORD, val);
		// }
		// 
		int bits = in.readBits(BITS_PER_INT);
		if(bits!= HUFF_TREE){
			throw new HuffException("invalid magic number "+bits);
		}
		HuffNode root = readTree(in);
		//readtree now creates a tree and root points at it
		HuffNode current=root;
		while(true){
			int bits2=in.readBits(1);
			if(bits2==-1){
				throw new HuffException("bad input, no PSEUDO_EOF");
			}
			if(bits2==0){
				//we are at a leaf so we need to check the value to see where to go
				//if(current.value==0){
					current=current.left;
			}
				//}
				
			//if (current.value==1){
			else{
				current=current.right;
			}
				//}
			
			if(current.left== null && current.right==null){
				//we found a leaf
				if(current.value==PSEUDO_EOF){
					System.out.println("you got to the end ladies and gentlemen");
					break;
				}
				else{
					out.writeBits(BITS_PER_WORD, current.value);
					//print out the value//
					// System.out.println(current.value);
					current=root;
				}
			}
		}
		out.close();
	}


	private HuffNode readTree(BitInputStream in) {
		//readbits updates the cursor to the next bit
		int bit = in.readBits(1);
		if (bit == -1) throw new HuffException("no more bits ");
		
		if (bit == 0) {
			//if 0 that means you are at an internal node ygm, recursive call to keep going
			HuffNode left = readTree(in);
			HuffNode right = readTree(in);
			return new HuffNode(0,0,left,right);
		}
		else {
			//if bit==1 then we know that we are at a leaf and we need to return +9 after it.
			int value = in.readBits(BITS_PER_WORD+1);
			// print tree was successful//
			// System.out.println("tree was successful");
			return new HuffNode(value,0,null,null);
		}
	}
	

		//expand readTreeHeader//
	// private HuffNode readTreeHeader(BitInputStream in){
	// 	int bit = in.readBits(1);
	// 	if(bit==-1){
	// 		throw new HuffException("bad input, no tree");
	// 	}
	// 	if(bit==0){
	// 		int val = in.readBits(BITS_PER_WORD);
	// 		if(val==-1){
	// 			throw new HuffException("bad input, no tree");
	// 		}
	// 		return new HuffNode(val,0);
	// 	}
	// 	else{
	// 		HuffNode left = readTreeHeader(in);
	// 		HuffNode right = readTreeHeader(in);
	// 		return new HuffNode(0,0,left,right);
	// 	}
	// }
		

}