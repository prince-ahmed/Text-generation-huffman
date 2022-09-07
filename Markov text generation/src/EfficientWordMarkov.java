import java.util.*;
public class EfficientWordMarkov extends BaseWordMarkov{
    private  HashMap<WordGram, ArrayList<String>> myMap;
    public EfficientWordMarkov(){
		this(3);
        // super(myOrder);
	}
    public EfficientWordMarkov(int order) {
		super(order);
		myMap = new HashMap<WordGram, ArrayList<String>>();
	}
    @Override
	public void setTraining(String text) {
        super.setTraining(text);
		myMap.clear();
        String[] ref=text.split("\\s+");
        for (int i=0;i<ref.length-myOrder+1;i++){
            WordGram wg = new WordGram(ref, i, myOrder);
            if(!myMap.containsKey(wg)){
                myMap.put(wg, new ArrayList<>());
            }
            if(i==ref.length-myOrder){
                // String maybe=wg.shiftAdd(PSEUDO_EOS).toString();
                myMap.get(wg).add(PSEUDO_EOS);
            }
            // String maybe=wg.shiftAdd(ref[i+myOrder]).toString();
			else myMap.get(wg).add(ref[i+myOrder]);
        }
        

    }
    @Override
	public ArrayList<String> getFollows(WordGram kGram) {
		// TODO: Implement getFollows
		if(!myMap.containsKey(kGram)){
			throw new NoSuchElementException(kGram+" not in map");
		}
		// String sb=String.join(" ", myMap.get(kGram));
        //String.join(" ", sb);
        return myMap.get(kGram);
	}
}
