package ie.gmit.sw.reflection.metrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarAnalyzer {
	private Map<String, ClassMetric> classMetrics;
	private double privateFields, fieldCount;
	private double privateMethods, methodsCount;
	private double inheritedMethods, inheritedFields;
	private int interfaceCount, abstractCount;
	private ClassMetricFactory ca = new ClassMetricFactory();

	public JarMetric analyze(File f) {
		JarMetric jarMetrics = null;

		try {
			InputStream inStream = new FileInputStream(f);
			JarInputStream in = new JarInputStream(inStream);

			URLClassLoader loader = new URLClassLoader(new URL[] { f.toURI().toURL() }, f.getClass().getClassLoader());

			analyzeJar(in, loader);
			double mhf = methodsCount != 0 ? privateMethods / methodsCount : 0;
			double ahf = fieldCount != 0 ? privateFields / fieldCount : 0;
			double aif = inheritedFields + fieldCount != 0 ? inheritedFields / (fieldCount + inheritedFields) : 0;
			double mif = inheritedMethods + methodsCount != 0 ? inheritedMethods / (methodsCount + inheritedMethods)
					: 0;
			jarMetrics = new JarMetric(new ArrayList<ClassMetric>(classMetrics.values()), ahf, aif, mhf, mif,
					interfaceCount, abstractCount);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return jarMetrics;
	}

	private void analyzeJar(JarInputStream in, URLClassLoader loader) throws IOException {
		init();

		JarEntry next = in.getNextJarEntry();

		while (next != null) {
			if (next.getName().endsWith(".class")) {
				String name = next.getName().replaceAll("/", "\\.");
				name = name.replaceAll(".class", "");
				if (!name.contains("$"))
					name.substring(0, name.length() - ".class".length());

				try {
					Class<?> cls = Class.forName(name, true, loader);
					ClassMetric cm = ca.getNewClassMetric(cls);
					if (cm != null) {
						if (classMetrics.containsKey(cm.getClassName()))
							classMetrics.get(cm.getClassName()).merge(cm);
						else
							classMetrics.put(cm.getClassName(), cm);
					}
				}
				// These are thrown when trying to load some classes that aren't
				// necessarily needed by the library, but can be used.
				// Loggers and (sometimes) test suites it seems, since they CAN
				// be used, but aren't by default.
				catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("Class loader tried loading an external class: " + name);
				} catch (NoClassDefFoundError e) {
					// TODO: handle exception
				}

			}

			next = in.getNextJarEntry();
		}

		in.close();
		getJarMetrics();
	}

	private void getJarMetrics() {

		for (String s : classMetrics.keySet()) {
			ClassMetric cm = classMetrics.get(s);
			determineAfferentDependencies(cm);
			fieldCount += cm.getFieldCount();
			privateFields += cm.getPrivateFieldCount();
			privateMethods += cm.getPrivateMethodCount();
			methodsCount += cm.getMethodCount();
			interfaceCount += cm.getIsInterface() == true ? 1 : 0;
			abstractCount += cm.getIsAbstract() == true ? 1 : 0;
			inheritedMethods += cm.getInheritedMethodCount();
			inheritedFields += cm.getInheritedFieldCount();
		}
	}

	private void determineAfferentDependencies(ClassMetric cm) {
		cm.getEfferentDependencies().forEach(dep -> {
			// just in case a class is dependent on a nonjdk class not in the
			// current jar
			if (classMetrics.get(dep) != null)
				classMetrics.get(dep).addAfferentDependecy(cm.getClassName());
		});
	}

	private void init() {
		classMetrics = new HashMap<String, ClassMetric>();
		fieldCount = 0;
		privateFields = 0;
		privateMethods = 0;
		methodsCount = 0;
		interfaceCount = 0;
	}

}
