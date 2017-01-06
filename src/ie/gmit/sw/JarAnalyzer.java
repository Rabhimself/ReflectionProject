package ie.gmit.sw;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarAnalyzer {
	private Map<String, Set<Class<?>>> bigAfferentMap = new HashMap<String, Set<Class<?>>>();
	private Map<String, Set<Class<?>>> bigEfferentMap = new HashMap<String, Set<Class<?>>>();

	public Map<String, Set<Class<?>>> getBigAfferentMap() {
		return bigAfferentMap;
	}

	public Map<String, Set<Class<?>>> getBigEfferentMap() {
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
					Class<?> cls = Class.forName(name, true, loader);
					buildCouplings(cls);
				}
				// These are thrown when trying to load some classes that arent
				// necessarily needed by the library, but can be used
				// loggers and (sometimes) test suites it seems, since they CAN
				// be used, but arent by default. Usually in that case the
				// logger libraries are provided by the developer using the
				// library.
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

	private void buildCouplings(Class<?> cls) {
		String clsName = cls.getName();
		int index = clsName.indexOf('$');
		if (index != -1) {
			clsName = clsName.substring(0, index);
		}

		// Package pack = cls.getPackage();
		// boolean iface = cls.isInterface();

		Set<Class<?>> dependencies = new HashSet<Class<?>>();

		bigEfferentMap.put(clsName, dependencies);

		Class<?>[] interfaces = cls.getInterfaces();
		dependencies.addAll(Arrays.asList(interfaces));

		Constructor<?>[] cons = cls.getConstructors();
		;
		Arrays.asList(cons).forEach((c) -> {
			dependencies.addAll(Arrays.asList(c.getParameterTypes()));
		});

		Field[] f = cls.getDeclaredFields();
		Arrays.asList(f).forEach((field) -> {
			dependencies.add(field.getType());
		});

		Method[] meths = cls.getMethods();
		Arrays.asList(meths).forEach((m) -> {
			dependencies.add(m.getReturnType());
			dependencies.addAll(Arrays.asList(m.getParameterTypes()));
		});

		Set<Class<?>> dump = new HashSet<Class<?>>();
		dependencies.forEach((c) -> {
			String n = c.getCanonicalName();
			int indx = n.indexOf('$');
			if (indx != -1) {
				n = n.substring(0, indx);
			}
			if (n.contains(".") && !n.startsWith("java.")) {
				Set<Class<?>> set = bigAfferentMap.get(n);
				if (set == null) {
					set = new HashSet<Class<?>>();
					bigAfferentMap.put(n, set);
				}
				set.add(cls);
			} else {
				dump.add(c);
			}
		});
		dependencies.removeAll(dump);
	}
}
