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
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JAR", "*.jar"));
		fileChooser.setTitle("Open Jar File");
		JarMetricsPane jmp = new JarMetricsPane();
		MetricsTable table = new MetricsTable();

		Button processButton;
		processButton = new Button();
		processButton.setText("Select Jar");

		processButton.setOnAction((ActionEvent e) -> {

			File f = fileChooser.showOpenDialog(null);
			JarMetric jarMetrics = null;
			
			if (f != null) {
				jarMetrics = new JarAnalyzer().analyze(f);
				List<ClassMetric> metrics = jarMetrics.getClassMetrics();
				metrics.forEach((cm) -> {
					table.addEntry(cm);					
				});
				jmp.displayJar(jarMetrics);
			}
		});
		
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		
		HBox top = new HBox();
		top.setAlignment(Pos.CENTER_LEFT);
		top.setPadding((new Insets(10)));
		top.getChildren().add(processButton);

		

		this.setTop(top);
		this.setCenter(sp);
		this.setRight(jmp);
	}

}
