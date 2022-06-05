package gui;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class App extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Pane root = new Pane();
		stage.setTitle("Pathfinding Simulator");
		stage.setScene(new Scene(root, 900, 600));
		
		SplitPane splitPane = new SplitPane();

		
		
		Grid grid = new Grid(60, 60);
		
		splitPane.getItems().addAll(grid, new Pane());
		splitPane.setDividerPositions(0.5f);
		root.getChildren().add(splitPane);
		stage.show();
		
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}

}
