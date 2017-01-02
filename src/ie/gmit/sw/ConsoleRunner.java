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
		// System.out.println(bigAMap);
		// System.out.println(bigEMap);

		// System.out.println("///////////////////////////AFFERENT");
		// Iterator it = bigAMap.entrySet().iterator();
		// while (it.hasNext()) {
		// Map.Entry <String, Set<Class>> pair = (Map.Entry)it.next();
		// int ca = pair.getValue().size();
		// Set<Class> asdf = bigEMap.get(pair.getKey());
		// int ce;
		// if(asdf != null)
		// ce = asdf.size();
		// else
		// ce = 0;
		//
		// System.out.println(ce +" "+ca);
		// it.remove();
		// }

		Iterator it = bigEMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Set<Class>> pair = (Map.Entry) it.next();
			double ca = pair.getValue().size();
			Set<Class> asdf = bigAMap.get(pair.getKey());
			double ce;
			if (asdf != null)
				ce = asdf.size();
			else
				ce = 0;

			if(ce+ca != 0){
				
			
			System.out.println(ce/(ca+ce));

			System.out.println("////////////////");
			}
			else			
				System.out.println("shhhiiiiitttt");
			it.remove();
		}
	}
}
