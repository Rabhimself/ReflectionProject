package ie.gmit.sw.reflection.gui;

import java.io.File;
import java.util.List;

import ie.gmit.sw.reflection.metrics.ClassMetric;
import ie.gmit.sw.reflection.metrics.JarAnalyzer;
import ie.gmit.sw.reflection.metrics.JarMetric;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class MetricsPane extends BorderPane {

	public MetricsPane() {
		
		final DoubleProperty classes = new SimpleDoubleProperty();
		final DoubleProperty couplings = new SimpleDoubleProperty();
		final StringProperty couplingFactor = new SimpleStringProperty();
		final StringProperty methodHidingFactor = new SimpleStringProperty();
		final StringProperty classesString = new SimpleStringProperty();
		final StringProperty ahfString = new SimpleStringProperty();
		StringConverter<Number> converter = new NumberStringConverter();
		Bindings.bindBidirectional(classesString, classes, converter);
		
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JAR", "*.jar"));
		fileChooser.setTitle("Open Jar File");

		MetricsTable table = new MetricsTable();

		Button processButton;
		processButton = new Button();
		processButton.setText("Select Jar");

		processButton.setOnAction((ActionEvent e) -> {

			File f = fileChooser.showOpenDialog(null);
			JarMetric jarMetrics = null;
			List<ClassMetric> metrics = null;
			if (f != null) {
				jarMetrics = JarAnalyzer.analyze(f);
				metrics = jarMetrics.getClassMetrics();
				metrics.forEach((cm) -> {
					table.addEntry(cm);
					couplings.set(couplings.get() + cm.getEfferent());
					System.out.println(couplings);
				});
				classes.set(metrics.size());
				double cf = couplings.get() /(classes.get() * (classes.get() -1));
				double mhf = jarMetrics.getMethodHidingFactor();
				couplingFactor.set(String.valueOf(cf));
				methodHidingFactor.set(String.valueOf(mhf));
				ahfString.set(Double.toString(jarMetrics.getAttributeHidingFactor()));
			}
		});
		
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		
		HBox top = new HBox();
		top.setAlignment(Pos.CENTER_LEFT);
		top.setPadding((new Insets(10)));
		top.getChildren().add(processButton);
				
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
		clstf.textProperty().bind(classesString);
		clsBox.getChildren().addAll(cls, clstf);
		
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
		
		right.getChildren().add(clsBox);
		right.getChildren().add(cfBox);
		right.getChildren().add(mhfBox);
		
		this.setTop(top);
		this.setCenter(sp);
		this.setRight(right);
	}

}
