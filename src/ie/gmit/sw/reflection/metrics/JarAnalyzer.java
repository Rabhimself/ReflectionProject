package ie.gmit.sw.reflection.metrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.jar.JarInputStream;

public class JarAnalyzer {

	
	public static JarMetric analyze(File f){
		JarMetric jarMetrics = null;

			try {
				InputStream inStream = new FileInputStream(f);
				JarInputStream in = new JarInputStream(inStream);

				URLClassLoader loader = new URLClassLoader(new URL[] { f.toURI().toURL() },
						f.getClass().getClassLoader());
				
				ClassesAnalyzer ca = new ClassesAnalyzer();				
				ca.analyzeJar(in, loader);

				List<ClassMetric> metrics = MetricsBuilder.getMetrics(ca.getEfferentMap(), ca.getAfferentMap());
				
				jarMetrics = new JarMetric(metrics, ca.getPrivateFieldCount()/ca.getFieldCount(), ca.getPrivateMethodCount()/ca.getMethodCount());
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
				
		return jarMetrics;
	}
}
