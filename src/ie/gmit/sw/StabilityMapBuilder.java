package ie.gmit.sw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StabilityMapBuilder {

	public static Map<String,String> init(Map<String, Set<Class>> e, Map<String, Set<Class>> a) {
		Iterator it = e.entrySet().iterator();
		Map<String, String>arr =new HashMap<String, String>();

		for(String k : e.keySet()){
			Set<Class> efferentClasses = e.get(k);
			Set<Class> afferentClasses = a.get(k);
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
			
			
			arr.put(k, Double.toString(stab));
		}
		return arr;
	}
}
