package ie.gmit.sw;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CouplingsAnalyzer {

	Map<String, Set<Class>> bigAfferentMap;
	Map<String, Set<Class>> bigEfferentMap;

	// I don't like this; instantiating with the class and then setting it with
	// new classes.
	// Break this whole class up more later
	public CouplingsAnalyzer(Map<String, Set<Class>> bigAfferentMap, Map<String, Set<Class>> bigEfferentMap) {
		this.bigAfferentMap = bigAfferentMap;
		this.bigEfferentMap = bigEfferentMap;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	public void buildCouplings(Class cls) {
		Package pack = cls.getPackage();// Get the package
		boolean iface = cls.isInterface(); // Is it an interface?
		Class[] interfaces = cls.getInterfaces(); // Get the set of
													// interface it
													// implements
		String clsName = cls.getName();
		int index = clsName.indexOf('$');
		if (index != -1) {
			clsName = clsName.substring(0, index);
		}

		Constructor[] cons = null;
		try {


			Set<Class> effDependencies = new HashSet<Class>();
			bigEfferentMap.put(clsName, effDependencies);
			
			///////////Fields////////////////
			Field[] fields = cls.getDeclaredFields();
			Class[] variableTypes = new Class[fields.length];
			for (int i = 0; i < fields.length; i++) {
				 variableTypes[i] = fields[i].getType();
			}
			doParamsLoop(variableTypes, clsName, cls);
			
			////////////////////////////////
			
			/////////// Get all dependencies from constructors//////////
			cons = cls.getConstructors();
			for (int i = 0; i < cons.length; i++) {
				Class[] params = cons[i].getParameterTypes(); // Get the
				doParamsLoop(params, clsName, cls);
			}
			/////////////////////////////////////////////////////////

			////////////// Get all dependencies from methods//////////////
			Method[] methods = cls.getMethods(); // Get the set of
			for (int i = 0; i < methods.length; i++) {
				Class[] params = methods[i].getParameterTypes(); // Get
				doParamsLoop(params, clsName, cls);

				Class c = methods[i].getReturnType();
				String n = c.getName();
				if ((n.contains(".") && !n.startsWith("java."))) {
					effDependencies.add(c);
					if (!bigAfferentMap.containsKey(c.getName())) {
						// create the set of afferent dependencies for the
						// parameter
						Set<Class> affDependencies = new HashSet<Class>();
						affDependencies.add(cls);
						// add it and the big map of afferent dependencies
						bigAfferentMap.put(c.getName(), affDependencies);
					} else {
						// get the set of afferent dependencies for the
						// parameter and add the current class to it
						bigAfferentMap.get(c.getName()).add(cls);
					}
				}
			}
		} catch (NoClassDefFoundError e) {
			// These are thrown when trying to load some classes that arent
			// necessarily needed by the library, but can be used
			// loggers and (sometimes) test suites it seems, since they CAN be
			// used, but arent by default. Usually in that case the logger
			// libraries are provided by the developer using the library.
		}

	}

	private void doParamsLoop(Class[] params, String className, Class cls) {
		for (int j = 0; j < params.length; j++) {
			// use toString() instead of getName() to make filtering out
			// primitives easier
			String n = params[j].getName();
			int indx = n.indexOf('$');
			if (indx != -1) {
				n = n.substring(0, indx);
			}
			// ignore any primitives and base java classes
			if (n.contains(".") && !n.startsWith("java.")) {
//				System.out.println(n);
				// add the efferent dependency (param[j]) to the set
				// since this class is dependent on it
				bigEfferentMap.get(className).add(params[j]);
//				System.out.println(className + " dependent on " + params[j]);
				if (!bigAfferentMap.containsKey(params[j].getName())) {
					// create the set of afferent dependencies for the
					// parameter
					Set<Class> affDependencies = new HashSet<Class>();
					affDependencies.add(cls);
					// add it and the big map of afferent dependencies
					bigAfferentMap.put(params[j].getName(), affDependencies);
//					System.out.println(params[j].getName() + " dependent on " + className);
				} else {
					// get the set of afferent dependencies for the
					// parameter and add the current class to it
					bigAfferentMap.get(params[j].getName()).add(cls);
//					System.out.println(params[j].getName() + " dependent on " + className);
				}
				// System.out.println(cls.getName() +" is dependent on "
				// +params[j].getName());

			}
		}
	}

}
