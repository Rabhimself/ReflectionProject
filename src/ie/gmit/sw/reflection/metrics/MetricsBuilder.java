package ie.gmit.sw.reflection.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetricsBuilder {

	public static List<ClassMetric> getMetrics(Map<String, Set<Class<?>>> efferentMap,
			Map<String, Set<Class<?>>> afferentMap) {
		List<ClassMetric> metrics = new ArrayList<ClassMetric>();

		for (String k : efferentMap.keySet()) {

			Set<Class<?>> efferentClasses = efferentMap.get(k);
			Set<Class<?>> afferentClasses = afferentMap.get(k);

			int ce = efferentClasses != null ? efferentClasses.size() : 0;
			int ca = afferentClasses != null ? afferentClasses.size() : 0;

			metrics.add(new ClassMetric(k, ce, ca));
		}

		return metrics;
	}
}
