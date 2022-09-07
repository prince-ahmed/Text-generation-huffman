import java.util.*;

public class EfficientMarkov extends BaseMarkov {
	private Map<String,ArrayList<String>> myMap;
	
	public EfficientMarkov(){
		this(3);
	}

	public EfficientMarkov(int order) {
		super(order);
		myMap = new HashMap<>();
	}

	@Override
	public void setTraining(String text) {
		super.setTraining(text);
		myMap.clear();
		// TODO: Finish implementing setTraining
		// char[] t= text.toCharArray();
		for (int i=0;i<text.length()-myOrder+1;i++){
			String key = text.substring(i,i+myOrder);
			if(!myMap.containsKey(key)){
				myMap.put(key, new ArrayList<>());
				// myMap.get(key).add(text.substring(i+getOrder()+1, i+getOrder()+1));
			}
			if(i==text.length()-myOrder){
				myMap.get(key).add(PSEUDO_EOS);
			}
			else myMap.get(key).add(text.substring(i+myOrder, i+myOrder+1));
		}
		// String key2=text.substring(text.length()-getOrder(), text.length());
		// if(!myMap.containsKey(key2)){
		// 	myMap.put(key2, new ArrayList<>());
		// 	myMap.get(key2).add(PSEUDO_EOS);
		// }
	}
	@Override
	public ArrayList<String> getFollows(String key) {
		// TODO: Implement getFollows
		if(!myMap.containsKey(key)){
			throw new NoSuchElementException(key+" not in map");
		}
		return myMap.get(key);
	}
}	




// for (int i=0;i<text.length()-myOrder;i++){
// 	String key = text.substring(i,i+myOrder);
// 	if(!myMap.containsKey(key)){
// 		myMap.put(key, new ArrayList<>());
// 		// myMap.get(key).add(text.substring(i+getOrder()+1, i+getOrder()+1));
// 	}
// 	if(i==text.length()-myOrder){
// 		myMap.get(key).add(PSEUDO_EOS);
// 	}
// 	else myMap.get(key).add(text.substring(i+myOrder+1, i+myOrder+1));
// }