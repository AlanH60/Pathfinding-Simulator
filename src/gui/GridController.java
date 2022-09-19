package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.AStar;
import algorithms.BreadthFirstSearch;
import algorithms.DepthFirstSearch;
import algorithms.Dijkstra;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class GridController 
{
	private static final int DIJKSTRA = 1;
	private static final String DIJKSTRA_LINK = "https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/";
	
	private static final int ASTAR = 2;
	private static final String ASTAR_LINK = "https://www.geeksforgeeks.org/a-search-algorithm/";
	
	private static final int BFS = 3;
	private static final String BFS_LINK = "https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/";
	
	private static final int DFS = 4;
	private static final String DFS_LINK = "https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/";
	
	@FXML private VBox controller;
	@FXML private ImageView startIcon;
	@FXML private ImageView endIcon;
	@FXML private ImageView obstacleIcon;
	@FXML private ImageView visitedIcon;
	@FXML private ImageView pathIcon;
	@FXML private ComboBox<String> algorithmSelectionBox;
	@FXML private Label warningText;
	@FXML private ImageView infoButton;
	private Alert infoAlertBox;
	private HostServices hostServices;
	private Popup iconHoverPopup;
	private Pane iconPopupPane;
	private Label iconPopupText;
	private ImageView selectedIcon;
	private HashMap<ImageView, Tile.Status> iconToType;
	private Grid grid;
	private boolean running = false;
	private ExecutorService threadPool;
	
	public GridController(Grid grid, HostServices hostServices)
	{
		this.grid = grid;
		this.hostServices = hostServices;
		threadPool = Executors.newFixedThreadPool(1);
		iconHoverPopup = new Popup();
		iconPopupPane = new Pane();
		iconPopupPane.setStyle("-fx-background-color:white; -fx-border-radius: 10; -fx-background-radius: 10;");
		iconPopupText = new Label();
		iconPopupText.setStyle("-fx-font: 12px Georgia");
		iconPopupText.setPadding(new Insets(10, 10, 10, 10));
		iconPopupPane.getChildren().add(iconPopupText);
		iconHoverPopup.getContent().add(iconPopupPane);
		infoAlertBox = new Alert(AlertType.CONFIRMATION);
	}
	
	public void setImages()
	{
		startIcon.setImage(Grid.startTile);
		endIcon.setImage(Grid.endTile);
		obstacleIcon.setImage(Grid.obstacleTile);
		visitedIcon.setImage(Grid.visitedTile);
		pathIcon.setImage(Grid.pathTile);
		infoButton.setImage(new Image(getClass().getResourceAsStream("/gui/res/InfoIcon.png")));
	}
	
	public VBox get()
	{
		iconToType = new HashMap<ImageView, Tile.Status>();
		iconToType.put(startIcon, Tile.Status.START);
		iconToType.put(endIcon, Tile.Status.END);
		iconToType.put(obstacleIcon, Tile.Status.OBSTACLE);
		iconToType.put(visitedIcon, Tile.Status.VISITED);
		iconToType.put(pathIcon, Tile.Status.PATH);
		return controller;
	}
	
	@FXML
	private void handleInfoButton(MouseEvent e)
	{
		if (e.getEventType() == MouseEvent.MOUSE_ENTERED)
			controller.setCursor(Cursor.HAND);
		else if (e.getEventType() == MouseEvent.MOUSE_EXITED)
			controller.setCursor(Cursor.DEFAULT);
		else
		{
			Optional<ButtonType> result = Optional.empty();
			String link = "";
			switch (getSelectedIcon())
			{
				case DIJKSTRA:
					infoAlertBox.setContentText("You're about to open an article on Dijkstra's Algorithm.");
					result = infoAlertBox.showAndWait();
					link = DIJKSTRA_LINK;
					break;
				case ASTAR:
					infoAlertBox.setContentText("You're about to open an article on the A* Algorithm.");
					result = infoAlertBox.showAndWait();
					link = ASTAR_LINK;
					break;
				case BFS:
					infoAlertBox.setContentText("You're about to open an article on Breadth First Search.");
					result = infoAlertBox.showAndWait();
					link = BFS_LINK;
					break;
				case DFS:
					infoAlertBox.setContentText("You're about to open an article on Depth First Search.");
					result = infoAlertBox.showAndWait();
					link = DFS_LINK;
					break;
				default:
					break;
			}
			if (result.isPresent() && result.get() == ButtonType.OK)
				hostServices.showDocument(link);
		}
	}
	
	@FXML
	private void handleIconClick(MouseEvent e)
	{
		if (running)
			return;
		if (selectedIcon != null)
			selectedIcon.setEffect(null);
		
		ImageView clickedIcon = (ImageView)e.getSource();
		if (clickedIcon == selectedIcon)
		{
			grid.setSelectionType(Tile.Status.DEFAULT);
			selectedIcon = null;
		}
		else
		{
			grid.setSelectionType(iconToType.get(clickedIcon));
			selectedIcon = clickedIcon;
			ColorAdjust ca = new ColorAdjust();
			ca.setBrightness(-0.5);
			selectedIcon.setEffect(ca);
		}
	}
	
	@FXML 
	private void handleIconHover(MouseEvent e)
	{
		if (running)
			return;
		ImageView icon = (ImageView)e.getSource();
		if (icon != selectedIcon && (e.getEventType() == MouseEvent.MOUSE_ENTERED || e.getEventType() == MouseEvent.MOUSE_EXITED))
		{
			ColorAdjust ca = new ColorAdjust();
			ca.setBrightness((e.getEventType() == MouseEvent.MOUSE_ENTERED) ? 0.5 : 0.0);
			icon.setEffect(ca);
		}		
		
		if (e.getEventType() == MouseEvent.MOUSE_MOVED || e.getEventType() == MouseEvent.MOUSE_ENTERED)
		{
			if (e.getEventType() == MouseEvent.MOUSE_ENTERED)
			{
				switch (iconToType.get(icon))
				{
					case END:
						controller.setCursor(Cursor.HAND);
						iconPopupText.setText("Determines the final destination of the path.");
						break;
					case OBSTACLE:
						controller.setCursor(Cursor.HAND);
						iconPopupText.setText("Prevents the path from traversing this tile. ");
						break;
					case START:
						controller.setCursor(Cursor.HAND);
						iconPopupText.setText("Determines the starting location of the path.");
						break;
					case VISITED:
						iconPopupText.setText("Tile that the algorithm has evaluated.");
						break;
					case PATH:
						iconPopupText.setText("Tiles of the final path.");
						break;
					default:
						iconPopupText.setText("");
						break;
				}
			}
			Point2D screenCoord = icon.localToScreen(e.getX() + 10, e.getY() + 10);

			iconHoverPopup.show(icon, screenCoord.getX(), screenCoord.getY());
		}
		else if (e.getEventType() == MouseEvent.MOUSE_EXITED)
		{
			iconHoverPopup.hide();
			controller.setCursor(Cursor.DEFAULT);
		}
	}
	
	
	@FXML
	private void start(MouseEvent e)
	{
		
		if (running)
		{
			cleanUp();
			threadPool = Executors.newFixedThreadPool(1);
		}
		running = true;
		endIcon.setEffect(null);
		startIcon.setEffect(null);
		obstacleIcon.setEffect(null);
		selectedIcon = null;
		grid.setSelectionType(Tile.Status.DEFAULT);
		grid.clearVisited();
		grid.clearPath();
		grid.lock();
		int selectedAlgorithm = getSelectedIcon();
		warningText.setText("");
		switch (selectedAlgorithm)
		{
			case DIJKSTRA:
				threadPool.execute(() -> {
					if (!grid.setPath(Dijkstra.dijkstra(grid)))
						Platform.runLater(()->{ warningText.setText("* Unable to find path"); });
					running = false;
					grid.unlock();
				});
				break;
			case ASTAR:
				threadPool.execute(() -> {
					if (!grid.setPath(AStar.aStar(grid)))
						Platform.runLater(()->{ warningText.setText("* Unable to find path"); });
					running = false;
					grid.unlock();
				});
				break;
			case BFS:
				threadPool.execute(() -> {
					if (!grid.setPath(BreadthFirstSearch.BFS(grid)))
						Platform.runLater(()->{ warningText.setText("* Unable to find path"); });
					running = false;
					grid.unlock();
				});
				break;
			case DFS:
				threadPool.execute(() -> {
					if (!grid.setPath(DepthFirstSearch.DFS(grid)))
						Platform.runLater(()->{ warningText.setText("* Unable to find path"); });
					running = false;
					grid.unlock();
				});
				break;
			default:
				warningText.setText("* Please select an algorithm");
				break;
		}
		
	}
	
	private int getSelectedIcon()
	{
		return algorithmSelectionBox.getSelectionModel().getSelectedIndex();
	}
	
	@FXML
	private void stop(MouseEvent e)
	{
		if (running)
		{
			cleanUp();
			threadPool = Executors.newFixedThreadPool(1);
		}
	}
	
	@FXML
	private void clearObstacles(MouseEvent e)
	{
		if (!running)
			grid.clearObstacles();
	}
	
	
	public void cleanUp()
	{
		while (!threadPool.isTerminated())
			threadPool.shutdownNow();
	}

}
