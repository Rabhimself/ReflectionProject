package ie.gmit.sw;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StabilityArrayBuilder {

	public static String[][] init(Map<String, Set<Class>> e, Map<String, Set<Class>> a) {
		Iterator it = e.entrySet().iterator();
		int counter = 0;
		String[][] arr = new String[e.size()][2];

		while (it.hasNext()) {

			Map.Entry<String, Set<Class>> pair = (Map.Entry) it.next();
			double ca = pair.getValue().size();
			Set<Class> asdf = a.get(pair.getKey());
			double ce;
			double stab;
			if (asdf != null)
				ce = asdf.size();
			else
				ce = 0;

			if (ce + ca != 0) {
				stab = ce / (ca + ce);
			} else
				stab = 0;
			arr[counter][0] = pair.getKey();
			arr[counter][1] = Double.toString(stab);
			counter++;
			it.remove();
		}
		return arr;
	}
}
