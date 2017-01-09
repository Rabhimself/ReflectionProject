package ie.gmit.sw.reflection.gui;

import java.util.ArrayList;
import java.util.List;

import ie.gmit.sw.reflection.metrics.ClassMetric;
import ie.gmit.sw.reflection.metrics.ClassMetric;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("rawtypes")
public class MetricsTable extends TableView {
	private static List<ClassMetric> metrics = new ArrayList<ClassMetric>();
	private static ObservableList<ClassMetric> items = FXCollections
			.observableArrayList(metrics);

	@SuppressWarnings("unchecked")
	public MetricsTable() {
		super(items);

		TableColumn classColumn = new TableColumn("Class");
		classColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, String>("className"));
		classColumn.setMinWidth(300);
		TableColumn intColumn = new TableColumn("Interface");
		intColumn.setMinWidth(50);
		intColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, String>("isInterface"));
		TableColumn absColumn = new TableColumn("Abstract");
		absColumn.setMinWidth(50);
		absColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, String>("isAbstract"));
		TableColumn ceColumn = new TableColumn("CE");
		ceColumn.setMinWidth(25);
		ceColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, Double>("efferentCount"));
		TableColumn caColumn = new TableColumn("CA");
		caColumn.setMinWidth(25);
		caColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, Double>("afferentCount"));
		TableColumn stabilityColumn = new TableColumn("Stability");
		TableColumn decColumn = new TableColumn<>("Value");
		decColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, Double>("stability"));
		TableColumn meterColumn = new TableColumn("Meter");
		meterColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, Double>("stability"));
		
		TableColumn fieldColumn = new TableColumn("Field Count");
		fieldColumn.setMinWidth(100);
		fieldColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, Double>("fieldCount"));
		
		TableColumn mthdColumn = new TableColumn("Method Count");
		mthdColumn.setMinWidth(100);
		mthdColumn.setCellValueFactory(new PropertyValueFactory<ClassMetric, Double>("methodCount"));
		
		
		meterColumn.setCellFactory(cb -> new TableCell<String, Double>() {
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
		cols.add(intColumn);
		cols.add(absColumn);
		cols.add(ceColumn);
		cols.add(caColumn);
		cols.add(stabilityColumn);
		cols.add(fieldColumn);
		cols.add(mthdColumn);

		this.getColumns().setAll(cols);
	}

	public void addEntry(ClassMetric e) {
		items.add(e);

	}

}
