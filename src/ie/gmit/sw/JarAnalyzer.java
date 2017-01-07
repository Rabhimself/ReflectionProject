package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.jar.JarInputStream;

public class JarAnalyzer {

	
	public static Map<String, String> unpack(File f){
		Map<String, String> m = null;		
			try {
				InputStream inStream = new FileInputStream(f);
				JarInputStream in = new JarInputStream(inStream);

				URLClassLoader loader = new URLClassLoader(new URL[] { f.toURI().toURL() },
						f.getClass().getClassLoader());
				
				CouplingsAnalyzer ca = new CouplingsAnalyzer();				
				ca.analyzeJar(in, loader);

				m = StabilityMapBuilder.build(ca.getBigEfferentMap(), ca.getBigAfferentMap());

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
				
		return m;
	}
}
