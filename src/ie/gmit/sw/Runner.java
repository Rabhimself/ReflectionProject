package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Runner extends Application {
	private static final String TITLE = "Reflection";
	private Button processButton;
	private File f;
	private final TableView table = new TableView<Row>();
	private ObservableList<Row> data;
	private Map<String, String> stabilities;
	private Scene tableScene;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(TITLE);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JAR", "*.jar"));
		fileChooser.setTitle("Open Jar File");

		processButton = new Button();
		processButton.setText("Select Jar");
		processButton.setOnAction((ActionEvent e) -> {
			f = fileChooser.showOpenDialog(primaryStage);
			
        	try {
        		InputStream inStream = new FileInputStream(f);
        		JarInputStream in = new JarInputStream(inStream);	
				JarAnalyzer ja = new JarAnalyzer();
				URLClassLoader loader =  new URLClassLoader(new URL[] { f.toURI().toURL() }, this.getClass().getClassLoader());
				ja.analyzeJar(in, loader);
				
				stabilities = StabilityMapBuilder.init(ja.getBigEfferentMap(), ja.getBigAfferentMap());
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        	
        	tableScene = SceneBuilder.buildStabilitiesTableScene(stabilities, primaryStage);
            primaryStage.setScene(tableScene);
		});
		
		BorderPane bp = new BorderPane();
		HBox layout = new HBox();
		layout.getChildren().add(processButton);
		bp.setTop(layout);
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		bp.setCenter(sp);
		
		Scene scene = new Scene(bp, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}