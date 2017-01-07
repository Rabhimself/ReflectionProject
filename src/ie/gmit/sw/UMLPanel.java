package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class UMLPanel extends ScrollPane {

	private final int WIDTH = 780;
	private final int HEIGHT = 580;
	ObservableList<Node> children;

	public UMLPanel() {
		Pane p = new Pane();
		children = p.getChildren();

		Map<String, Set<String>> map = testLoop();

		p.setMinWidth(WIDTH);
		p.setMinHeight(HEIGHT);

		this.setContent(p);
		this.setPannable(true);
		System.out.println(Math.floor(Math.random() * 6));
	}

	private Map<String, Set<String>> testLoop() {

		Map<String, Set<String>> m = new HashMap<String, Set<String>>();

		for (int i = 1; i <= 15; i++) {
			Set<String> stringset = new HashSet<String>();
			String s = "java.lang.String " + i;
			m.put(Integer.toString(i), new HashSet<String>());
			double max = Math.floor(Math.random() * 5);
			for (double j = 0; j < max; j++) {

				String s2 = "java.lang.String " + i + "" + j;
				stringset.add(s2);

			}

		}
		return m;

	}

	class RelNode  extends Label{
		private Class cls;
	
		public RelNode(String s, Class c) {
			super(s);
			cls = c;
			setPadding(new Insets(5));
			setLayoutX(WIDTH/2);
			setLayoutY(HEIGHT/2);
			setBorder(new Border(new BorderStroke(Color.BLACK,new BorderStrokeStyle(null, null, null,10,0,null), null,null)));
		}

		public Class getCls() {
			return cls;
		}
	}
}
