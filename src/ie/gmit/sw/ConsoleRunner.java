package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarInputStream;

public class ConsoleRunner {
	public static void main(String[] args) throws IOException {
		Map<String, Set<Class<?>>> bigAMap = null;
		Map<String, Set<Class<?>>> bigEMap = null;
		File f = new File(args[0]);

		InputStream inStream = new FileInputStream(f);
		JarInputStream in = new JarInputStream(inStream);
		JarAnalyzer ja = new JarAnalyzer();
		URLClassLoader loader = new URLClassLoader(new URL[] { f.toURI().toURL() },
				f.getClass().getClassLoader());
		ja.analyzeJar(in, loader);

		bigAMap = ja.getBigAfferentMap();
		bigEMap = ja.getBigEfferentMap();

		System.out.printf("\n\n/////////////////STABILITIES/////////////\n");
		for(String k : bigEMap.keySet()){

			double ce = bigEMap.get(k).size();
			Set<Class<?>> asdf = bigAMap.get(k);
			double ca;
			if (asdf != null)
				ca = asdf.size();
			else
				ca = 0;
			System.out.println("CLASS "+k);
			System.out.println("     "+ce +"/" +ca + " + " + ce);
			if (ce + ca != 0) {
				
				System.out.println("     Stability = " + (ce / (ca + ce)));
				
			}
			else
				System.out.println("     Stability = " + 0);
				
		}
	}
}
