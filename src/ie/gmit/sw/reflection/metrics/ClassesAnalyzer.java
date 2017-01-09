package ie.gmit.sw.reflection.metrics;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassesAnalyzer {
	private Map<String, Set<Class<?>>> afferentMap;
	private Map<String, Set<Class<?>>> efferentMap;
	private int privateFields, fieldCount;
	private int privateMethods, methodsCount;
	private int interfaceCount, abstractCount;
	
	public void analyzeJar(JarInputStream in, URLClassLoader loader) throws IOException {
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
		if(cls.isInterface())
			interfaceCount++;
		if(Modifier.isAbstract(cls.getModifiers()))
			abstractCount++;
		

		Set<Class<?>> dependencies = new HashSet<Class<?>>();

		efferentMap.put(clsName, dependencies);

		Class<?>[] interfaces = cls.getInterfaces();
		dependencies.addAll(Arrays.asList(interfaces));

		Constructor<?>[] cons = cls.getConstructors();

		Arrays.asList(cons).forEach((c) -> {
			dependencies.addAll(Arrays.asList(c.getParameterTypes()));
		});

		Field[] f = cls.getDeclaredFields();
		for (int i = 0; i < f.length; i++) {
			dependencies.add(f[i].getType());
			if (Modifier.isPrivate(f[i].getModifiers()))
				privateFields++;
			fieldCount++;
		}

		Method[] meths = cls.getMethods();

//		Arrays.asList(meths).forEach((m) -> {
//			dependencies.add(m.getReturnType());
//			dependencies.addAll(Arrays.asList(m.getParameterTypes()));
//		});
		for (int i = 0; i < meths.length; i++) {
			dependencies.add(meths[i].getReturnType());
			dependencies.addAll(Arrays.asList(meths[i].getParameterTypes()));

			if (Modifier.isPrivate(meths[i].getModifiers())){
				privateMethods++;
				System.out.println(meths[i] +" is private");
			}
			methodsCount++;
		}

		Set<Class<?>> dump = new HashSet<Class<?>>();
		dependencies.forEach((c) -> {
			String n = c.getCanonicalName();
			if (n != null) {
				int indx = n.indexOf('$');
				if (indx != -1) {
					n = n.substring(0, indx);
				}
				if (n.contains(".") && !n.startsWith("java.")) {
					Set<Class<?>> set = afferentMap.get(n);
					if (set == null) {
						set = new HashSet<Class<?>>();
						afferentMap.put(n, set);
					}
					set.add(cls);
				} else {
					dump.add(c);
				}
			}
		});
		dependencies.removeAll(dump);
	}
	
	private void init() {
		afferentMap = new HashMap<String, Set<Class<?>>>();
		efferentMap = new HashMap<String, Set<Class<?>>>();
		fieldCount = 0;
		privateFields = 0;
		privateMethods = 0;
		methodsCount = 0;
		interfaceCount = 0;
	}
	public int getInterfaceCount() {
		return interfaceCount;
	}
	public int getPrivateFieldCount() {
		return privateFields;
	}
	public int getFieldCount() {
		return fieldCount;
	}
	public int getPrivateMethodCount() {
		return privateMethods;
	}
	public int getMethodCount() {
		return methodsCount;
	}
	public int getAbstractCount() {
		return abstractCount;
	}
	public Map<String, Set<Class<?>>> getAfferentMap() {
		return afferentMap;
	}
	public Map<String, Set<Class<?>>> getEfferentMap() {
		return efferentMap;
	}
}
