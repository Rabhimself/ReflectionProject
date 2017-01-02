package ie.gmit.sw;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarAnalyzer {
	private Map<String, Set<Class>> bigAfferentMap = new HashMap<String, Set<Class>>();
	private Map<String, Set<Class>> bigEfferentMap = new HashMap<String, Set<Class>>();
	private CouplingsAnalyzer ca = new CouplingsAnalyzer(bigAfferentMap, bigEfferentMap);

	
	
	public Map<String, Set<Class>> getBigAfferentMap() {
		return bigAfferentMap;
	}



	public Map<String, Set<Class>> getBigEfferentMap() {
		return bigEfferentMap;
	}



	public void analyzeJar(JarInputStream in, URLClassLoader loader) throws IOException {
		JarEntry next = in.getNextJarEntry();
		while (next != null) {
			if (next.getName().endsWith(".class")) {
				String name = next.getName().replaceAll("/", "\\.");
				name = name.replaceAll(".class", "");
				if (!name.contains("$"))
					name.substring(0, name.length() - ".class".length());

				

				try {
					Class cls = Class.forName(name, true, loader);
					ca.buildCouplings(cls);
				}
				// These are thrown when trying to load some classes that arent
				// necessarily needed by the library, but can be used
				// loggers and (sometimes) test suites it seems, since they CAN
				// be used, but arent by default. Usually in that case the
				// logger
				// libraries are provided by the developer using the library.
				catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				} catch (NoClassDefFoundError e) {
					// TODO: handle exception
				}

			}

			next = in.getNextJarEntry();
		}

		in.close();
	}
}
