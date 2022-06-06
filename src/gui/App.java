package gui;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
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
		
		GridPane gridPane = new GridPane();

		Grid grid = new Grid(20, 20);
		
		GridController gc = new GridController(grid);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("res/GridController.fxml"));
		loader.setController(gc);
		loader.load();
		
		gridPane.add(grid.getGridPane(), 0, 0);
		gridPane.add(gc.get(), 1, 0);

		root.getChildren().add(gridPane);
		stage.show();
		
	}

}
