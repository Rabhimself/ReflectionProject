package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetricsBuilder {
	
	public static List<ClassMetric> getMetrics(Map<String, Set<Class<?>>> efferentMap,Map<String, Set<Class<?>>> afferentMap){
		List<ClassMetric> metrics = new ArrayList<ClassMetric>();
		
		for(String k : efferentMap.keySet()){
				
				Set<Class<?>> efferentClasses = efferentMap.get(k);
				Set<Class<?>> afferentClasses = afferentMap.get(k);
				double ce;
				double ca;
				
				if (efferentClasses != null)
					ce = efferentClasses.size();
				else
					ce = 0;
				if(afferentClasses != null)
					ca = afferentClasses.size();
				else
					ca = 0;
					
				double stab = getStability(ce,ca);
				double cf = getCouplingFactor(efferentMap.get(k).size(), efferentMap.size()-1);
				ClassMetric cm = new ClassMetric(stab, cf, k);
				metrics.add(cm);
		}
		
		
		return metrics;
	}

	public static double getStability(double efferentCouplings, double afferentCouplings) {	
		if (efferentCouplings + afferentCouplings != 0) {
			return efferentCouplings / (afferentCouplings + efferentCouplings);
		} else
			return 0;
	}
	
	public static double getCouplingFactor(double couplings, double potentialCouplings) {
			return couplings/potentialCouplings;
	}
}
