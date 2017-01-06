package ie.gmit.sw;

import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SceneBuilder {

	public static Scene buildStabilitiesTableScene(Map<String, String> stabilities, Stage stage){
		Scene prev = stage.getScene();
		Button processButton = new Button();
        // use fully detailed type for Map.Entry<String, String> 
        TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Key");
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });

        TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Value");
        column2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });

        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(stabilities.entrySet());
        final TableView<Map.Entry<String,String>> table = new TableView<>(items);
        table.getColumns().setAll(column1, column2);

		processButton.setText("Back");
		processButton.setOnAction((ActionEvent e) -> {
        	stage.setScene(prev);
            
		});
        
		BorderPane bp = new BorderPane();
		HBox layout = new HBox();
		layout.getChildren().add(processButton);
		bp.setTop(layout);
		StackPane sp = new StackPane();
		sp.getChildren().add(table);
		bp.setCenter(sp);
		
		return new Scene(bp, 800, 600);
		
	}
}
