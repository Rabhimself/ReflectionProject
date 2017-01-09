package ie.gmit.sw.reflection.metrics;

import java.util.HashSet;
import java.util.Set;

//make interface with only the metric methods, ignore all get/set
public class ClassMetric  {
	private double efferentCount;
	private double afferentCount;
	private String className;
	private boolean isInterface;
	private boolean isAbstract;
	private int methodCount;
	private int inheritedMethodCount;
	private int privateMethodCount;
	private int fieldCount;
	private int privateFieldCount;
	private int inheritedFieldCount;
	private Set<String> efferentDependencies;
	private Set<String> afferentDependencies;

	public ClassMetric(String name) {
		this.className = name;
		efferentCount = afferentCount = methodCount = privateFieldCount = fieldCount = privateFieldCount = 0;
		isInterface = isAbstract = false;
		efferentDependencies = new HashSet<String>();
		afferentDependencies = new HashSet<String>();
	}


	public double getEfferentCount() {
		return efferentCount;
	}

	public void setEfferentCount(double efferent) {
		this.efferentCount = efferent;
	}


	public double getAfferentCount() {
		return afferentCount;
	}

	public void setAfferentCount(double afferent) {
		this.afferentCount = afferent;
	}

	public boolean getIsInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	
	public boolean getIsAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public int getMethodCount() {
		return methodCount;
	}

	public void setMethodCount(int methodCount) {
		this.methodCount = methodCount;
	}

	public int getPrivateMethodCount() {
		return privateMethodCount;
	}

	public void setPrivateMethodCount(int privateMethodCount) {
		this.privateMethodCount = privateMethodCount;
	}

	public int getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public int getPrivateFieldCount() {
		return privateFieldCount;
	}

	public void setPrivateFieldCount(int privateFieldCount) {
		this.privateFieldCount = privateFieldCount;
	}

	public String getClassName() {
		return className;
	}

	public Set<String> getEfferentDependencies() {
		return efferentDependencies;
	}

	public Set<String> getAfferentDependencies() {
		return afferentDependencies;
	}

	public int getInheritedMethodCount() {
		return inheritedMethodCount;
	}

	public void setInheritedMethodCount(int inheritedMethodCount) {
		this.inheritedMethodCount = inheritedMethodCount;
	}


	public int getInheritedFieldCount() {
		return inheritedFieldCount;
	}

	public void setInheritedFieldCount(int inheritedFieldCount) {
		this.inheritedFieldCount = inheritedFieldCount;
	}


	public double getStability() {
		return efferentCount + afferentCount != 0 ? (efferentCount / (efferentCount + afferentCount)) : 0;
	}

	public boolean isDependentOn(Class<?> clss) {
		return efferentDependencies.contains(trimClassName(clss.getName()));
	}

	public boolean isDependentOf(Class<?> clss) {
		return afferentDependencies.contains(trimClassName(clss.getName()));
	}

	public void addAfferentDependecy(String clss) {
		afferentDependencies.add(clss);
		afferentCount++;
	}

	public void addEfferentDependency(String clss) {
		efferentDependencies.add(clss);
		efferentCount++;
	}

	private String trimClassName(String name) {
		int i = name.indexOf('$');
		if (i != -1)
			return name.substring(0, i);
		return name;
	}
}
