package ie.gmit.sw.reflection.metrics;

import java.util.List;

public class JarMetric {
	private List<ClassMetric> classMetrics;
	private double attributeHidingFactor;
	private double methodHidingFactor;
	private int interfaceCount;
	private int abstractCount;
	private double attributeInheritanceFactor;
	private double methodInheritanceFactor;
	
	public JarMetric(List<ClassMetric> classMetrics, double attributeHidingFactor,double attributeInheritanceFactor, double methodHidingFactor, double methodInheritanceFactor, int interfaceCount, int abstractCount) {
		this.classMetrics = classMetrics;
		this.attributeHidingFactor = attributeHidingFactor;
		this.methodHidingFactor = methodHidingFactor;
		this.interfaceCount = interfaceCount;
		this.abstractCount = abstractCount;
		this.attributeInheritanceFactor = attributeInheritanceFactor;
		this.methodInheritanceFactor = methodInheritanceFactor;
	}
	
	public List<ClassMetric> getClassMetrics(){
		return classMetrics;
	}
	public double getAttributeHidingFactor(){
		return attributeHidingFactor;
	}
	public double getMethodHidingFactor(){
		return methodHidingFactor;
	}
	public int getClassCount(){
		return classMetrics.size();
	}

	public int getInterfaceCount() {
		return interfaceCount;
	}

	public int getAbstractCount() {
		return abstractCount;
	}

	public double getAttributeInheritanceFactor() {
		return attributeInheritanceFactor;
	}

	public double getMethodInheritanceFactor() {
		return methodInheritanceFactor;
	}
	
}
