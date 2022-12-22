package _8_Hashing;

import java.util.HashMap;
import java.util.Iterator;

public class _0_HashMap {
    public static void main(String[] args) {
        HashMap<String,Integer> map=new HashMap<>();
        map.put("SB",0);
        map.put("SUKU",2);
        map.put("MG",9);
        map.put("Alvin",1);
        Iterator<String> i =map.keySet().iterator();
        while (i.hasNext())
        {
            String key=i.next();
            System.out.println(key+" "+map.get(key));
        }

    }
}
