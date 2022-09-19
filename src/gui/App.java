package gui;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class App extends Application
{
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception
	{
		Pane root = new Pane();
		stage.setTitle("Pathfinding Simulator");
		stage.setScene(new Scene(root, 920, 620));
		
		GridPane gridPane = new GridPane();
		Grid grid;
		grid = new Grid(600 / Tile.WIDTH, 600 / Tile.WIDTH);
		GridController gc = new GridController(grid, this.getHostServices());

		FXMLLoader loader = new FXMLLoader(getClass().getResource("res/GridController.fxml"));
		loader.setController(gc);
		loader.load();
		gc.setImages();
	
		gridPane.add(grid.getGridPane(), 0, 0);
		gridPane.add(gc.get(), 1, 0);

		root.getChildren().add(gridPane);
		
		stage.setOnCloseRequest((e) -> {
			gc.cleanUp();
		});
		stage.show();		
	}

}
