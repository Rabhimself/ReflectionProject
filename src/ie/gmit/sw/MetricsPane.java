package ie.gmit.sw;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MetricsPane extends BorderPane {

	public MetricsPane() {
		HBox layout = new HBox();

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JAR", "*.jar"));
		fileChooser.setTitle("Open Jar File");

		MetricsTable table = new MetricsTable();

		Button processButton;
		processButton = new Button();
		processButton.setText("Select Jar");

		processButton.setOnAction((ActionEvent e) -> {

			File f = fileChooser.showOpenDialog(null);
			List<ClassMetric> metrics = null;
			if (f != null)
				metrics = JarAnalyzer.unpack(f);
			metrics.forEach((cm) ->{
				table.addEntry(cm);
			});
			
		});

		layout.getChildren().add(processButton);
		this.setTop(layout);
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		this.setCenter(sp);
	}

}
