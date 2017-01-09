package ie.gmit.sw.reflection.metrics;

import java.util.List;

public class JarMetric {
	private List<ClassMetric> classMetrics;
	private double attributeHidingFactor;
	private double methodHidingFactor;
	
	public JarMetric(List<ClassMetric> classMetrics, double attributeHidingFactor, double methodHidingFactor) {
		this.classMetrics = classMetrics;
		this.attributeHidingFactor = attributeHidingFactor;
		this.methodHidingFactor = methodHidingFactor;
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
}
