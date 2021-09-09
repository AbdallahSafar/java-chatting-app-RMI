package Test1;

import java.util.HashMap;

public class Tests {

	public static void main(String[] args) {
		HashMap<String,String> hash = new HashMap();
		System.out.println(hash);
		hash.put("Abdallah", "0000");
		hash.put("Oussama", "1234");
		hash.put("Yahya", "0.1565");
		System.out.println(hash);
		System.out.println(hash.get("Abdallah"));
		System.out.println(hash.containsKey("Ahmad"));
		System.out.println(hash.keySet().toString());
		for (String key: hash.keySet()) {
		    System.out.println(key + "=" + hash.get(key));
		}
		System.out.println(hash.keySet().toString().split(",")[0]);
		
		System.out.println(hash.get("Abdallah") == "0000");
	}

}
