package asrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssociationRule {

	private static final int minSup = 2; 
	private static final float minConf = 0.6f; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Integer, List<Integer>> items = new HashMap<>();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		items.put(1, list1);
		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(1);
		list2.add(2);
		items.put(2, list2);
		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(1);
		list3.add(2);
		items.put(3, list3);
		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(1);
		list4.add(2);
		list4.add(3);
		items.put(4, list4);
		long startTime = System.nanoTime();
		Apriori(items);
		long endTime = System.nanoTime();
		long duration = endTime - startTime; 
		System.out.println("Run time: " + (double)(duration / 1000000000));
	}
	public static void Apriori (Map<Integer, List<Integer>> items) {
		Map<Integer, Integer> init = new HashMap<>();
		List<Integer> keyList = new ArrayList<>();
		for (int i = 1; i <= items.size(); i++) {
			for (int j = 0; j < items.get(i).size(); j++) {
				int key = items.get(i).get(j);
				System.out.println("Number of key init: "+j+ "/" + (items.get(i).size() - 1));
				init.put(key, init.getOrDefault(key, 0) + 1);
			}
		}
		for (Map.Entry<Integer, Integer> entry : init.entrySet()) {
	        if (entry.getValue() < minSup) {
	        	init.remove(entry.getKey());
	        }else {
	    		keyList.add(entry.getKey());        	
	        }
	    }
		Map<Integer, Map<Integer, Integer>> saveRes = new HashMap<>();
		saveRes.put(1, new HashMap<>(init)); 
		int k = 2;
		while (true) {
			List<List<Integer>> listOfCombination = new ArrayList<>();
			backTrack(k,keyList,listOfCombination, new ArrayList<>(), 0);
			init.clear();
			for (int i = 0; i < listOfCombination.size(); i++) {
				String valueCheck = "";
				for (int j = 0; j < listOfCombination.get(i).size(); j++) {
					valueCheck += listOfCombination.get(i).get(j);
				}
				for (Map.Entry<Integer, List<Integer>> entry : items.entrySet()) {
					String res = entry.getValue().stream()
							.map(s -> String.valueOf(s))
							.collect(Collectors.joining());
					if (res.contains(valueCheck)){
						init.put(Integer.parseInt(valueCheck), init.getOrDefault(Integer.parseInt(valueCheck), 0) + 1);
					}
				}	
			}	
			keyList.clear();
			for (Map.Entry<Integer, Integer> entry : init.entrySet()) {
				if (entry.getValue() < minSup) {
					init.remove(entry.getKey());
				}else {
					keyList.add(entry.getKey());
				}
			}
			k++;
			saveRes.put(k, new HashMap<>(init));
			if (k > keyList.size()) break;
		}
		for (Map.Entry<Integer, Map<Integer, Integer>> entry : saveRes.entrySet()) {
			for (Map.Entry<Integer, Integer> en : entry.getValue().entrySet()) {
				System.out.println(en.getKey() + " Sup: " + en.getValue());
			}
		}
	}
	public static void backTrack (int k, List<Integer> keyList, List<List<Integer>> res, List<Integer> temp, int start) {
		if (k == 0) {
			res.add(new ArrayList<Integer>(temp));
			return; 
		}
		for (int i = start; i < keyList.size(); i++) {
			temp.add(keyList.get(i));
			backTrack(k - 1, keyList, res, temp, i + 1);
			temp.remove(temp.size()-1);
		}
	}
}
