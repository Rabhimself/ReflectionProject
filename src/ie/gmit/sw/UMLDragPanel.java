package ie.gmit.sw;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

//Shamefully lifted the drag functionality from http://stackoverflow.com/questions/19748744/javafx-how-to-connect-two-nodes-by-a-line
//Done some minor tweaks to work with labels, moving on and leaving this here in case I get around to making it work better than it does right now.
public class UMLDragPanel extends ScrollPane {
	
	private final int WIDTH = 780;
	private final int HEIGHT = 580;

	public UMLDragPanel() {
		Pane p = new Pane();
		p.setMinWidth(WIDTH);
		p.setMinHeight(HEIGHT);
		
		Label l1 = new Label();
		l1.setText("java.lang.String");
		l1.setPadding(new Insets(5));
		l1.setLayoutX(WIDTH/2);
		l1.setLayoutY(HEIGHT/2);
		l1.setBorder(new Border(new BorderStroke(Color.BLACK,new BorderStrokeStyle(null, null, null,10,0,null), null,null)));
		
		DoubleProperty startX =  new SimpleDoubleProperty(WIDTH/2-50);
		DoubleProperty startY =  new SimpleDoubleProperty(HEIGHT/2);
		DoubleProperty endX =  new SimpleDoubleProperty(WIDTH/2+50);
		DoubleProperty endY =  new SimpleDoubleProperty(HEIGHT/2);

		
		Anchor a1 = new Anchor("java.lang.String", startX, startY);
		Anchor a2 = new Anchor("java.lang.String", endX, endY);
		
		
		Center startCenter = new Center(a1);
		Center endCenter = new Center(a2);
		
		BoundLine l = new BoundLine(startCenter.centerXProperty(), startCenter.centerYProperty(), endCenter.centerXProperty(), endCenter.centerYProperty());
//		l.setText();
//		l.setPadding(new Insets(5));
//		l.setLayoutX(WIDTH/2);
//		l.setLayoutY(HEIGHT/2 - 50);
//		l.setBorder(new Border(new BorderStroke(Color.BLACK,new BorderStrokeStyle(null, null, null,10,0,null), null,null)));
		
		
		ObservableList<Node> children = p.getChildren();
		children.add(a1);
		children.add(a2);
		children.add(l);
		this.setContent(p);
		this.setPannable(true);
	}
	
	 class BoundLine extends Line {
		    BoundLine(ReadOnlyDoubleProperty readOnlyDoubleProperty, ReadOnlyDoubleProperty readOnlyDoubleProperty2, ReadOnlyDoubleProperty readOnlyDoubleProperty3, ReadOnlyDoubleProperty readOnlyDoubleProperty4) {
		      startXProperty().bind(readOnlyDoubleProperty);
		      startYProperty().bind(readOnlyDoubleProperty2);
		      endXProperty().bind(readOnlyDoubleProperty3);
		      endYProperty().bind(readOnlyDoubleProperty4);
		      setStrokeWidth(2);
		      setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
		      setStrokeLineCap(StrokeLineCap.BUTT);
		      getStrokeDashArray().setAll(10.0, 5.0);
		      setMouseTransparent(true);
		    }
		  }

		  // a draggable anchor displayed around a point.
		  class Anchor extends Label { 
		    Anchor(String label, DoubleProperty x, DoubleProperty y) {
		      super(label);
		      setLayoutX(x.get());
		      setLayoutY(y.get());
		      setBorder(new Border(new BorderStroke(Color.BLACK,new BorderStrokeStyle(null, null, null,10,0,null), null,null)));

		      x.bind(this.layoutXProperty());
		      y.bind(this.layoutYProperty());
		      enableDrag();
		    }

		    // make a node movable by dragging it around with the mouse.
		    private void enableDrag() {
		      final Delta dragDelta = new Delta();
		      setOnMousePressed(new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent mouseEvent) {
		          // record a delta distance for the drag and drop operation.
		          dragDelta.x = layoutXProperty().get() - mouseEvent.getX();
		          dragDelta.y = layoutYProperty().get() - mouseEvent.getY();
		          getScene().setCursor(Cursor.MOVE);
		        }
		      });
		      setOnMouseReleased(new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent mouseEvent) {
		          getScene().setCursor(Cursor.HAND);
		        }
		      });
		      setOnMouseDragged(new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent mouseEvent) {
		          double newX = mouseEvent.getX() + dragDelta.x;
		          if (newX > 0 && newX < getScene().getWidth()) {
		        	  setLayoutX(newX);
		          }  
		          double newY = mouseEvent.getY() + dragDelta.y;
		          if (newY > 0 && newY < getScene().getHeight()) {
		        	  setLayoutY(newY);
		          }  
		        }
		      });
		      setOnMouseEntered(new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent mouseEvent) {
		          if (!mouseEvent.isPrimaryButtonDown()) {
		            getScene().setCursor(Cursor.HAND);
		          }
		        }
		      });
		      setOnMouseExited(new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent mouseEvent) {
		          if (!mouseEvent.isPrimaryButtonDown()) {
		            getScene().setCursor(Cursor.DEFAULT);
		          }
		        }
		      });
		    }

		    // records relative x and y co-ordinates.
		    private class Delta { double x, y; }
		  }
		  
		  class Center {
			    private ReadOnlyDoubleWrapper centerX = new ReadOnlyDoubleWrapper();
			    private ReadOnlyDoubleWrapper centerY = new ReadOnlyDoubleWrapper();

			    public Center(Node node) {
			        calcCenter(node.getBoundsInParent());
			        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			            @Override public void changed(
			                   ObservableValue<? extends Bounds> observableValue, 
			                   Bounds oldBounds, 
			                   Bounds bounds
			            ) {
			                calcCenter(bounds);
			            }
			        });
			    }

			    private void calcCenter(Bounds bounds) {
			        centerX.set(bounds.getMinX() + bounds.getWidth()  / 2);
			        centerY.set(bounds.getMinY() + bounds.getHeight() / 2);
			    }

			    ReadOnlyDoubleProperty centerXProperty() {
			        return centerX.getReadOnlyProperty();
			    }

			    ReadOnlyDoubleProperty centerYProperty() {
			        return centerY.getReadOnlyProperty();
			    }
			}
}
