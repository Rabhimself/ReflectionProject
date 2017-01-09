package ie.gmit.sw.reflection.metrics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassMetricFactory {

	public ClassMetric getNewClassMetric(Class<?> cls) {
		if (!isValidClass(cls))
			return null;

		String clsName = trimName(cls.getName());

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
		int inheritedFields = 0;
		for (int i = 0; i < f.length; i++) {
			dependencies.add(f[i].getType());
			if (Modifier.isPrivate(f[i].getModifiers()))
				privateFields++;
			if(isInherited(f[i].getDeclaringClass(), cls))
				inheritedFields++;
			fieldCount++;
		}
		cm.setFieldCount(fieldCount);
		cm.setPrivateFieldCount(privateFields);
		cm.setInheritedFieldCount(inheritedFields);
		
		Method[] meths = cls.getDeclaredMethods();
		
		int privateMethods = 0;
		int methodsCount = 0;
		int inheritedMethods = 0;
		for (int i = 0; i < meths.length; i++) {
			dependencies.add(meths[i].getReturnType());
			dependencies.addAll(Arrays.asList(meths[i].getParameterTypes()));
			if (Modifier.isPrivate(meths[i].getModifiers())) {

				privateMethods++;
			}
			Class<?> declaring = meths[i].getDeclaringClass();
			if(isInherited(declaring, cls)){
				inheritedMethods++;			
			}

			methodsCount++;

		}
		cm.setMethodCount(methodsCount);
		cm.setPrivateMethodCount(privateMethods);
		cm.setInheritedMethodCount(inheritedMethods);
		
		dependencies.forEach((c) -> {
			if (isValidClass(c)) {
				cm.addEfferentDependency(trimName(c.getName()));
			}
		});
		return cm;
	}

	private boolean isValidClass(Class<?> clss) {
		if (clss.isPrimitive())
			return false;
		if (clss.isArray())
			return false;

		String n = clss.getName();

		return !n.startsWith("java.") ? true : false;
	}

	private String trimName(String name) {
		if (name.indexOf('$') != -1)
			return name.substring(0, name.indexOf('$'));
		else
			return name;
	}

	private boolean isInherited(Class<?> declaring, Class<?> cls) {
		if (isValidClass(declaring)) {
			if (!declaring.equals(cls)) {
				if (declaring.isAssignableFrom(cls))
					return true;
			}
		}
		return false;
	}
}
