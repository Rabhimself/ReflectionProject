package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class StabilityTable extends TableView<Map.Entry<String, String>>{
	private static Map<String, String> stabilities = new HashMap<String, String>();
	private static ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(stabilities.entrySet());

	public StabilityTable() {
		super(items);
		
		TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Key");
		column1.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
						return new SimpleStringProperty(p.getValue().getKey());
					}
				});

		TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Value");
		column2.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
						return new SimpleStringProperty(p.getValue().getValue());
					}
				});
		List<TableColumn<Map.Entry<String, String>, String>> cols = new ArrayList<TableColumn<Map.Entry<String, String>, String>>();
		cols.add(column1);
		cols.add(column2);
		this.getColumns().setAll(cols);
	}
	
	public void addEntry(Entry<String, String> e){
		items.add(e);
	}

	
}
