package ie.gmit.sw.reflection.metrics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
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
		if(isValidClass(superClass)){
			inheritedMethods = getInherited(superClass, Method.class);
			inheritedFields = getInherited(superClass, Field.class);
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
