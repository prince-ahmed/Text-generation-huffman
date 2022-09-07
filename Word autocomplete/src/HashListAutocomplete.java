import java.util.*;

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
        if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
		initialize(terms,weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if(!myMap.containsKey(prefix)||k==0){
            return new ArrayList<>();
        }
        // TODO Auto-generated method stub
        String actualprefix=prefix;
        if(prefix.length()>MAX_PREFIX){
            actualprefix=prefix.substring(0, MAX_PREFIX);
        }
        if(myMap.containsKey(actualprefix)){
            List<Term> all = myMap.get(prefix);
            List<Term> list = all.subList(0, Math.min(k, all.size()));
            return list;
        }
        List<Term> s=new ArrayList<>();
        return s;
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        // TODO Auto-generated method stub
        myMap=new HashMap<>();
        // mySize=0;
        // for(int j=0;j<terms.length;j++){
        //     // char[] cs=s.toCharArray();
        //     Term r= new Term(terms[j],weights[j]);
        //     int up=MAX_PREFIX;
        //     if(terms[j].length()<MAX_PREFIX){
        //         up=terms[j].length();
        //     }
        //     // mySize+=BYTES_PER_DOUBLE;
        //     for(int i=0;i<up;i++){
        //         String c=terms[j].substring(0,i);
        //         myMap.putIfAbsent(c, new ArrayList<>());
        //         myMap.get(c).add(r);
        //         // mySize+=BYTES_PER_CHAR*terms[j].length()+BYTES_PER_DOUBLE+BYTES_PER_CHAR*c.length();
        //     }
        // }


        // for(int j=0;j<terms.length;j++){
        //     int up=Math.min(terms[j].length(),MAX_PREFIX);
        //     for(int i=0;i<up;i++){
        //         myMap.putIfAbsent(terms[j].substring(0,i), new ArrayList<>());
        //         myMap.get(terms[j].substring(0,i)).add(new Term(terms[j],weights[j]));
        //     }
        // }
        int count = 0;
		for(String term : terms) {
			int max = Math.min(term.length(), MAX_PREFIX);
			for(int i = 0; i <= max; i++) {
				myMap.putIfAbsent(term.substring(0,i), new ArrayList<Term>());
				myMap.get(term.substring(0,i)).add(new Term(term, weights[count]));
			}
			count++;
		}
        for(String key:myMap.keySet()){
            Collections.sort(myMap.get(key), Comparator.comparing(Term::getWeight).reversed());
        }
    }

    @Override
    public int sizeInBytes() {
        // TODO Auto-generated method stub
        // mySize=0;
        if (mySize == 0) {
            for(String key:myMap.keySet()){
                mySize+=key.length()*BYTES_PER_CHAR;
                // for(Term s:myMap.get(key)){
                //     mySize+=s.getWord().length()*BYTES_PER_CHAR+BYTES_PER_DOUBLE;
                // }


                // List<Term> terms=myMap.get(key);
                // for(int i=0;i<terms.size();i++){
                //     Term s=terms.get(i);
                //     mySize+=s.getWord().length()*BYTES_PER_CHAR+ BYTES_PER_DOUBLE;
                // }



			// for(Term t : myTerms) {
			//     mySize += BYTES_PER_DOUBLE + BYTES_PER_CHAR*t.getWord().length();	
			}
            
            for(List<Term> terms:myMap.values()){
                for(Term term:terms){
                    mySize+=term.getWord().length()*BYTES_PER_CHAR+ BYTES_PER_DOUBLE;
                
                }
            }
		}
		return mySize;

        // return mySize;
    }
    
}





