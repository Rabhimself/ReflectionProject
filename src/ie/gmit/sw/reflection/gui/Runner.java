package ie.gmit.sw.reflection.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Runner extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Reflection");
		Scene scene = new Scene(new MetricsPane(), 1200, 800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}