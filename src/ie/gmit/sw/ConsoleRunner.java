package ie.gmit.sw;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConsoleRunner {
	public static void main(String[] args) throws MalformedURLException {
		Map<String, Set<Class>> bigAMap = null;
		Map<String, Set<Class>> bigEMap = null;
		File f = new File(args[0]);
		JarUnpacker ju = new JarUnpacker(f);

		JarAnalyzer ja = new JarAnalyzer();

		try {
			ja.analyzeJar(ju.unpack(), ju.getLoader());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bigAMap = ja.getBigAfferentMap();
		bigEMap = ja.getBigEfferentMap();

		Iterator it = bigEMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Set<Class>> pair = (Map.Entry) it.next();
			double ce = pair.getValue().size();
			Set<Class> asdf = bigAMap.get(pair.getKey());
			double ca;
			if (asdf != null)
				ca = asdf.size();
			else
				ca = 0;
			System.out.println("CLASS "+pair.getKey());
			System.out.println("     "+ce +"/" +ca + " + " + ce);
			if (ce + ca != 0) {
				
				System.out.println("     Stability = " + (ce / (ca + ce)));
				
			}
			else
				System.out.println("     Stability = " + 0);
				
			it.remove();
		}
	}
}
