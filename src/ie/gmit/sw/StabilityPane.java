package ie.gmit.sw;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class StabilityPane extends BorderPane {

	public StabilityPane() {
		HBox layout = new HBox();

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JAR", "*.jar"));
		fileChooser.setTitle("Open Jar File");

		StabilityTable table = new StabilityTable();

		Button processButton;
		processButton = new Button();
		processButton.setText("Select Jar");

		processButton.setOnAction((ActionEvent e) -> {

			File f = fileChooser.showOpenDialog(null);
			Map<String, String> stabilitiesMap = null;
			if (f != null)
				 stabilitiesMap=JarAnalyzer.unpack(f);
				for (Entry<String, String> entry : stabilitiesMap.entrySet()) {
					table.addEntry(entry);
				}
		});

		layout.getChildren().add(processButton);
		this.setTop(layout);
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		this.setCenter(sp);
	}

}
