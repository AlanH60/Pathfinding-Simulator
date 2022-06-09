package gui;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
		
		try
		{
			GridPane gridPane = new GridPane();
			Grid grid;
			grid = new Grid(600 / Tile.WIDTH, 600 / Tile.WIDTH);
			GridController gc = new GridController(grid, this.getHostServices());

			FXMLLoader loader = new FXMLLoader(getClass().getResource("res/GridController.fxml"));
			loader.setController(gc);
			loader.load();
		
			gridPane.add(grid.getGridPane(), 0, 0);
			gridPane.add(gc.get(), 1, 0);

			root.getChildren().add(gridPane);
			
			stage.setOnCloseRequest((e) -> {
				gc.cleanUp();
			});
			stage.show();
		}
		catch (IOException e)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.toString());
			stage.show();
			alert.setOnCloseRequest((a) ->{
				stage.close();
			});
			alert.show();
		}
		
	}

}
