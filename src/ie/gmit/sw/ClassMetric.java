package ie.gmit.sw;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClassMetric {
	private final SimpleDoubleProperty stability;
	private final SimpleDoubleProperty couplingFactor;
	private final SimpleStringProperty className;
	
	public ClassMetric(double stability, double couplingFactor, String name) {
		this.stability = new SimpleDoubleProperty(stability);
		this.couplingFactor = new SimpleDoubleProperty(couplingFactor);
		this.className = new SimpleStringProperty(name);
	}	
	public double getStability() {
		return stability.get();
	}

	public void setStability(double stabiliy) {
		this.stability.set(stabiliy);
	}

	public double getCouplingFactor() {
		return couplingFactor.get();
	}

	public void setCouplingFactor(double couplingFactor) {
		this.couplingFactor.set(couplingFactor);
	}

	public String getClassName() {
		return className.get();
	}

	public void setClassName(String name) {
		className.set(name);
	}
}
