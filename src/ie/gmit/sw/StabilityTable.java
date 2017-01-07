package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
public class StabilityTable extends TableView {
	private static Map<String, String> stabilities = new HashMap<String, String>();
	private static ObservableList<Map.Entry<String, String>> items = FXCollections
			.observableArrayList(stabilities.entrySet());

	@SuppressWarnings("unchecked")
	public StabilityTable() {
		super(items);

		TableColumn<Map.Entry<String, String>, String> classColumn = new TableColumn<>("Class");
		classColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
						return new SimpleStringProperty(p.getValue().getKey());
					}
				});
		classColumn.setMinWidth(300);
		TableColumn stabilityColumn = new TableColumn("Stability");

		TableColumn<Map.Entry<String, String>, String> decColumn = new TableColumn<>("Value");
		decColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
						return new SimpleStringProperty(p.getValue().getValue());
					}
				});
		TableColumn meterColumn = new TableColumn("");
		meterColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<Number>>() {

					@Override
					public ObservableValue<Number> call(
							TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
						return new SimpleDoubleProperty(Double.parseDouble(p.getValue().getValue()));
					}
				});
		meterColumn.setCellFactory(col -> new TableCell<String, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null && !empty) {				
					double percent =item-1;					
					percent *=-100;
					percent+= .01;
					
					String color = "169f16";
					if (percent > 80) {
						color = "008000";
					} else if (percent > 66) {
						color = "9acd32";
					}
					else if(percent > 50){
						color ="ffff00";
					}
					else if(percent > 25){
						color = "ff4500";
					}
					else
						color = "ff0000";

					setStyle("-fx-background-color: linear-gradient(from 0% 100% to " + (percent) + "% 100%, #" + color	+ ", #" + color + " 99.99%, transparent);");
				}
			}
		}

		);

		List<TableColumn> cols = new ArrayList<TableColumn>();
		stabilityColumn.getColumns().addAll(decColumn, meterColumn);

		cols.add(classColumn);
		cols.add(stabilityColumn);

		this.getColumns().setAll(cols);
	}

	public void addEntry(Entry<String, String> e) {
		items.add(e);

	}

}
