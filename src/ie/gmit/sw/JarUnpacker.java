package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarUnpacker {

	@SuppressWarnings("rawtypes")
	public Map<String, Set<Class>> unpack(String jar) throws IOException  {

		File f = new File(jar);
		InputStream inStream = new FileInputStream(f);
		JarInputStream in = new JarInputStream(inStream);
		JarEntry next = in.getNextJarEntry();
		Map<String, Set<Class>> bigMap = new HashMap<String, Set<Class>>();
		

		while (next != null) {
			if (next.getName().endsWith(".class")) {
				String name = next.getName().replaceAll("/", "\\.");
				name = name.replaceAll(".class", "");
				if (!name.contains("$"))
					name.substring(0, name.length() - ".class".length());

				URLClassLoader child = new URLClassLoader(new URL[] { f.toURI().toURL() },
						this.getClass().getClassLoader());
				Class cls;
				
				
				try {
					cls = Class.forName(name, true, child);

					Package pack = cls.getPackage(); // Get the package
					boolean iface = cls.isInterface(); // Is it an interface?
					Class[] interfaces = cls.getInterfaces(); // Get the set of
																// interface it
																// implements
					Constructor[] cons = cls.getConstructors(); // Get the set
																// of
																// constructors
					Field[] fields = cls.getFields(); // Get the fields
														// attributes
					Method[] methods = cls.getMethods(); // Get the set of
															// methods
					Map<String, Class[]> paramsMap = new HashMap<String, Class[]>();

					Set<Class> dependencies = new HashSet<Class>();
					for (int i = 0; i < cons.length; i++) {
						Class[] params = cons[i].getParameterTypes(); // Get the
																		// parameters
						// System.out.println("Constructors:" +cons[i]);
						for (int j = 0; j < params.length; j++) {
							String n = params[j].toString();
							if (n.startsWith("class"))
								if (!(n.startsWith("class java"))) {
									dependencies.add(params[j]);
								}

						}

					}

					for (int i = 0; i < methods.length; i++) {
						Class c = methods[i].getReturnType(); // Get a method
																// return
																// type
						Class[] params = methods[i].getParameterTypes(); // Get
																			// method
																			// parameters

						for (int j = 0; j < params.length; j++) {
							String s = methods[i].getDeclaringClass().toString();
//							System.out.println(s);
							if (!s.startsWith("class java.")) {
//								System.out.println("Method: " + methods[i].toString());
								String n = params[j].toString();
								if (n.startsWith("class"))
									if (!(n.startsWith("class java"))) {
										dependencies.add(params[j]);
									}
							}
						}
					}

					bigMap.put(name, dependencies);
				} catch (NoClassDefFoundError e) {
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			
			
			next = in.getNextJarEntry();
		}

		in.close();
		return bigMap;
	}
}
