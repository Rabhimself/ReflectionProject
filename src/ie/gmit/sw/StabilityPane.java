package ie.gmit.sw;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarInputStream;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class StabilityPane extends BorderPane{

	public StabilityPane(){
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
			
			if (f != null) {
				try {
					InputStream inStream = new FileInputStream(f);
					JarInputStream in = new JarInputStream(inStream);
					JarAnalyzer ja = new JarAnalyzer();
					URLClassLoader loader = new URLClassLoader(new URL[] { f.toURI().toURL() },
							this.getClass().getClassLoader());
					ja.analyzeJar(in, loader);

					Map<String, String> m = StabilityMapBuilder.build(ja.getBigEfferentMap(), ja.getBigAfferentMap());
					for (Entry<String, String> entry : m.entrySet()) {
						table.addEntry(entry);
					}
				} catch (MalformedURLException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		});

		
		layout.getChildren().add(processButton);
		this.setTop(layout);
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		this.setCenter(sp);
	}
	
	
}
