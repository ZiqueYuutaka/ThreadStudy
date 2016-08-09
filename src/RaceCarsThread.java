/************************************************************
(Racing car) Write a program that simulates car racing, as shown in
Figure 15.34a. The car moves from left to right. When it hits the right end, it
restarts from the left and continues the same process. You can use a timer to
control animation. Redraw the car with a new base coordinates (x, y), as shown
in Figure 15.34b. Also let the user pause/resume the animation with a button
press/release and increase/decrease the car speed by pressing the UP and
DOWN arrow keys.

(Racing cars) Rewrite Programming Exercise 15.29 using a thread to control
car racing. Compare the program with Programming Exercise 15.29 by setting
the delay time to 10 in both programs. Which one runs the animation faster?

Answer: The Racecar using the JavaFX without Threads is traveling quicker.
	This is due to the overhead cost of Platform.runLater().  It sends
	the Runnable to an event queue and then returns to the caller.
	The event queue is managed by the Event Dispatch Thread which in the Java
	architecture.  
	Too many calls to the event queue can delay the GUI update.
	The animated RaceCar utilizing Timeline and KeyFrame displays
	already processed events at specific intervals along a time set
	by cycleCount.  The Runnables passed by runLater() have yet to
	be processed.
*************************************************************/
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class RaceCarsThread extends Application{
	/*public static void main(String[] args){

	}*/
	private int screenWidth = 800;
	private int screenHeight = 400;
	/*private double moveRate = 20;
	private double position = 0;*/

	@Override
	public void start(Stage primaryStage){
		Pane pane = new Pane();

		Car raceCar = new Car();

		//raceCar.reposition(moveRate);

		pane.getChildren().add(raceCar.getBody());
		pane.getChildren().add(raceCar.getWheel1());
		pane.getChildren().add(raceCar.getWheel2());
		pane.getChildren().add(raceCar.getCabin());

		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					while(true){

						raceCar.reposition();

						Platform.runLater(new Runnable(){ //Update GUI
							@Override
							public void run(){
								pane.getChildren().clear();
								pane.getChildren().add(raceCar.getBody());
								pane.getChildren().add(raceCar.getWheel1());
								pane.getChildren().add(raceCar.getWheel2());
								pane.getChildren().add(raceCar.getCabin());
							}
						});

						Thread.sleep(10);
					}
				}
				catch(InterruptedException ex){}
			}
		}).start();

		/*EventHandler<ActionEvent> eventHandler = e->{
			pane.getChildren().clear();
			//position += moveRate;
			raceCar.reposition();
			pane.getChildren().add(raceCar.getBody());
			pane.getChildren().add(raceCar.getWheel1());
			pane.getChildren().add(raceCar.getWheel2());
			pane.getChildren().add(raceCar.getCabin());
		};

		Timeline animation = new Timeline(
			new KeyFrame(Duration.millis(500), eventHandler));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();*/

		Scene scene = new Scene(pane, screenWidth, screenHeight);
		primaryStage.setTitle("RaceCarsThread");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	class Car{
		private Polygon cabin = null;
		
		private Rectangle body = null;
		private double bodyHeight = 10;
		private double bodyWidth = 50;

		private Circle wheel1 = null;
		private Circle wheel2 = null;
		private double wheelRadius = 5;

		private ObservableList<Double> list = null;

		private double moveRate = 20;
		private double position = 0;

		//Create the car at the starting point
		public Car(){
			this.reset();
		}

		public Rectangle getBody(){
			return body;
		}

		public Polygon getCabin(){
			return cabin;
		}

		public Circle getWheel1(){return wheel1;}

		public Circle getWheel2(){return wheel2;}

		public void reposition(){
			position += moveRate;

			if(position + bodyWidth >= screenWidth){
				this.reset();
			}
			else{
				//Create the cabin
				cabin = new Polygon();
				list = cabin.getPoints();
				double centerX = (bodyWidth / 2) + position, centerY = (screenHeight - 20);
				double radius = Math.min(bodyWidth, bodyHeight) * 0.90;
				for(int i = 0; i < 4; i++){
					list.add(centerX + radius * Math.cos(2* i * Math.PI / 6));
					list.add(centerY - radius * Math.sin(2 * i * Math.PI/6));
				}

				//Create the body
				body = new Rectangle(position, screenHeight - 20, bodyWidth, bodyHeight);

				//Create the wheels
				wheel1 = new Circle((bodyWidth/4) + position, screenHeight - 5, 5);
				wheel2 = new Circle((3*bodyWidth/4) + position, screenHeight - 5, 5);
			}
		}

		//Reset the position of the car to the start
		public void reset(){
			position = 0;
			this.reposition();
		}
	}
}