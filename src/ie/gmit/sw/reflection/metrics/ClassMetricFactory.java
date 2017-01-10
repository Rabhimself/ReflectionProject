package ie.gmit.sw.reflection.metrics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//A psuedo factory class that takes any class as a parameter
//and return all the metrics for it, the only problem is the afferent dependencies set does not get
//instantiated in that factory, the jar analyzer has to do that once it has all the class metrics built(specifically the list of efferent couplings)
public class ClassMetricFactory {

	public ClassMetric getNewClassMetric(Class<?> cls) {
		if (!isValidClass(cls))
			return null;

		String clsName = trimName(cls.getName());
		Class<?> superClass = cls.getSuperclass();
		ClassMetric cm = new ClassMetric(clsName);
		Set<Class<?>> dependencies = new HashSet<Class<?>>();

		cm.setInterface(cls.isInterface());
		cm.setAbstract(Modifier.isAbstract(cls.getModifiers()));

		Class<?>[] interfaces = cls.getInterfaces();

		dependencies.addAll(Arrays.asList(interfaces));

		Constructor<?>[] cons = cls.getConstructors();

		Arrays.asList(cons).forEach((c) -> {
			dependencies.addAll(Arrays.asList(c.getParameterTypes()));
		});

		Field[] f = cls.getDeclaredFields();

		int privateFields = 0;
		int fieldCount = 0;

		for (int i = 0; i < f.length; i++) {
			dependencies.add(f[i].getType());
			if (Modifier.isPrivate(f[i].getModifiers()))
				privateFields++;
			fieldCount++;
		}
		cm.setFieldCount(fieldCount);
		cm.setPrivateFieldCount(privateFields);

		Method[] meths = cls.getDeclaredMethods();

		int privateMethods = 0;
		int methodsCount = 0;

		for (int i = 0; i < meths.length; i++) {
			dependencies.add(meths[i].getReturnType());
			dependencies.addAll(Arrays.asList(meths[i].getParameterTypes()));
			if (Modifier.isPrivate(meths[i].getModifiers())) {

				privateMethods++;
			}
			methodsCount++;

		}
		cm.setMethodCount(methodsCount);
		cm.setPrivateMethodCount(privateMethods);

		int inheritedMethods = 0;
		int inheritedFields = 0;
		if (superClass != null) {
			if (isValidClass(superClass)) {
				inheritedMethods = getInherited(superClass, Method.class);
				inheritedFields = getInherited(superClass, Field.class);
			}
		}
		cm.setInheritedMethodCount(inheritedMethods);
		cm.setInheritedFieldCount(inheritedFields);

		dependencies.forEach((c) -> {
			if (isValidClass(c)) {
				cm.addEfferentDependency(trimName(c.getName()));
			}
		});
		return cm;
	}

	// determines if the class passed to it is not a standard jdk class,
	// and isnt a primitive or array
	private boolean isValidClass(Class<?> clss) {
		if (clss.isPrimitive())
			return false;
		if (clss.isArray())
			return false;

		String n = clss.getName();

		return !n.startsWith("java.") ? true : false;
	}

	// Trims off any of the anonymous classnames from it's parent
	// so that the anonymous class's dependencies are bundled with
	// it's parents metrics
	private String trimName(String name) {
		if (name.indexOf('$') != -1)
			return name.substring(0, name.indexOf('$'));
		else
			return name;
	}

	private int getInherited(Class<?> cls, Class<? extends Member> m) {
		int i = 0;
		if (isValidClass(cls.getSuperclass()))
			i += getInherited(cls.getSuperclass(), m);

		if (m.equals(Method.class))
			i += cls.getDeclaredMethods().length;
		if (m.equals(Field.class))
			i += cls.getDeclaredFields().length;
		if (m.equals(Constructor.class))
			i += cls.getDeclaredConstructors().length;

		return i;
	}
}
