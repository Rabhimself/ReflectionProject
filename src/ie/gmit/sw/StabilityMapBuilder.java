package ie.gmit.sw;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StabilityMapBuilder {

	public static Map<String,String> build(Map<String, Set<Class<?>>> map2, Map<String, Set<Class<?>>> map3) {

		Map<String, String>map = new HashMap<String, String>();

		for(String k : map2.keySet()){
			Set<Class<?>> efferentClasses = map2.get(k);
			Set<Class<?>> afferentClasses = map3.get(k);
			double ce;
			double ca;
			double stab;
			
			if (efferentClasses != null)
				ce = efferentClasses.size();
			else
				ce = 0;
			if(afferentClasses != null)
				ca = afferentClasses.size();
			else
				ca = 0;
				
			if (ce + ca != 0) {
				stab = ce / (ca + ce);
			} else
				stab = 0;
			
			
			map.put(k, Double.toString(stab));
		}
		return map;
	}
}
