package ie.gmit.sw.reflection.gui;

import java.lang.invoke.MethodHandleInfo;

import ie.gmit.sw.reflection.metrics.ClassMetric;
import ie.gmit.sw.reflection.metrics.JarMetric;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class JarMetricsPane extends Pane {

	private final StringProperty classes = new SimpleStringProperty();
	private final StringProperty couplingFactor = new SimpleStringProperty();
	private final StringProperty methodHidingFactor = new SimpleStringProperty();
	private final StringProperty methodInheritanceFactor = new SimpleStringProperty();
	private final StringProperty ahfString = new SimpleStringProperty();
	private final StringProperty aifString = new SimpleStringProperty();
	private final StringProperty ihfString = new SimpleStringProperty();
	private final StringProperty ihmString = new SimpleStringProperty();
	private final StringProperty interfaceCountString = new SimpleStringProperty();
	public JarMetricsPane() {

		Label jmets = new Label("Jar Metrics");
		VBox right = new VBox();
		right.setPadding(new Insets(10));
		right.setAlignment(Pos.TOP_CENTER);
		right.getChildren().add(jmets);
		jmets.setFont(new Font(16));
		jmets.setPadding(new Insets(0, 0, 10, 0));

		Label cls = new Label("Classes: ");
		HBox clsBox = new HBox();
		TextField clstf = new TextField();
		clsBox.setAlignment(Pos.CENTER_RIGHT);
		clstf.setMaxWidth(50);
		clstf.setEditable(false);
		clstf.textProperty().bind(classes);
		clsBox.getChildren().addAll(cls, clstf);
		
		Label interfaces = new Label("Interfaces: ");
		HBox intBox = new HBox();
		TextField inttf = new TextField();
		intBox.setAlignment(Pos.CENTER_RIGHT);
		inttf.setMaxWidth(50);
		inttf.setEditable(false);
		inttf.textProperty().bind(interfaceCountString);
		intBox.getChildren().addAll(interfaces, inttf);

		Label cf = new Label("Coupling Factor: ");
		HBox cfBox = new HBox();
		TextField tf = new TextField();
		cfBox.setAlignment(Pos.CENTER_RIGHT);
		cfBox.getChildren().addAll(cf, tf);
		tf.setMaxWidth(50);
		tf.setEditable(false);
		tf.textProperty().bind(couplingFactor);

		Label mhfl = new Label("Method Hiding Factor: ");
		HBox mhfBox = new HBox();
		TextField mhftf = new TextField();
		mhfBox.setAlignment(Pos.CENTER_RIGHT);
		mhfBox.getChildren().addAll(mhfl, mhftf);
		mhftf.setMaxWidth(50);
		mhftf.setEditable(false);
		mhftf.textProperty().bind(methodHidingFactor);
		
		Label mifl = new Label("Method Inheritance Factor: ");
		HBox mifBox = new HBox();
		TextField miftf = new TextField();
		mifBox.setAlignment(Pos.CENTER_RIGHT);
		mifBox.getChildren().addAll(mifl, miftf);
		miftf.setMaxWidth(50);
		miftf.setEditable(false);
		miftf.textProperty().bind(methodInheritanceFactor);

		Label ahfl = new Label("Attribute Hiding Factor: ");
		HBox ahfBox = new HBox();
		TextField ahftf = new TextField();
		ahfBox.setAlignment(Pos.CENTER_RIGHT);
		ahfBox.getChildren().addAll(ahfl, ahftf);
		ahftf.setMaxWidth(50);
		ahftf.setEditable(false);
		ahftf.textProperty().bind(ahfString);

		Label aifl = new Label("Attribute Inheritance Factor: ");
		HBox aifBox = new HBox();
		TextField aiftf = new TextField();
		aifBox.setAlignment(Pos.CENTER_RIGHT);
		aifBox.getChildren().addAll(aifl, aiftf);
		aiftf.setMaxWidth(50);
		aiftf.setEditable(false);
		aiftf.textProperty().bind(aifString);
		
		right.getChildren().addAll(clsBox, intBox, cfBox,mhfBox, mifBox,ahfBox, aifBox);
		this.getChildren().add(right);
	}

	public void displayJar(JarMetric jm) {
		
		classes.set(String.valueOf(jm.getClassCount()));
		int inheritedMethods = 0;
		int inheritedFields = 0;
		double c = 0;
		for(ClassMetric cm : jm.getClassMetrics()){
			c+=cm.getEfferentCount();
			inheritedMethods+=cm.getInheritedMethodCount();
			inheritedFields+=cm.getInheritedFieldCount();
		}
		double cf = c / (jm.getClassCount() * (jm.getClassCount() - 1));
		double mhf = jm.getMethodHidingFactor();
		interfaceCountString.set(String.valueOf(jm.getInterfaceCount()));
		couplingFactor.set(String.valueOf(cf));
		methodHidingFactor.set(String.valueOf(mhf));
		methodInheritanceFactor.set(String.valueOf(jm.getMethodInheritanceFactor()));
		aifString.set(String.valueOf(jm.getAttributeInheritanceFactor()));
		ahfString.set(Double.toString(jm.getAttributeHidingFactor()));
		ihfString.set(String.valueOf(inheritedFields));
		ihmString.set(String.valueOf(inheritedMethods));
	}
}
