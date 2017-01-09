package ie.gmit.sw.reflection.metrics;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClassMetric {
	private final SimpleDoubleProperty stability;
	private final SimpleDoubleProperty efferent;
	private final SimpleDoubleProperty afferent;
	private final SimpleStringProperty className;
	
	
	public ClassMetric(String name, double efferent, double afferent) {
		double stab;

		if(efferent+afferent != 0)
			 stab = efferent/(afferent+efferent);
		else
			stab = 0;
		this.stability = new SimpleDoubleProperty(stab);
		this.className = new SimpleStringProperty(name);
		this.efferent = new SimpleDoubleProperty(efferent);
		this.afferent = new SimpleDoubleProperty(afferent);
		
	}	
	public double getStability() {
		return stability.get();
	}
	public String getClassName() {
		return className.get();
	}
	public double getEfferent() {
		return this.efferent.get();
	}
	public double getAfferent() {
		return this.afferent.get();
	}

	
}
