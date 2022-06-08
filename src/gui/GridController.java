package gui;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.AStar;
import algorithms.BreadthFirstSearch;
import algorithms.DepthFirstSearch;
import algorithms.Dijkstra;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GridController 
{
	private static final int DIJKSTRA = 0;
	private static final int ASTAR = 1;
	private static final int BFS = 2;
	private static final int DFS = 3;
	
	@FXML private VBox controller;
	@FXML private ImageView startIcon;
	@FXML private ImageView endIcon;
	@FXML private ImageView obstacleIcon;
	@FXML private ComboBox<String> algorithmSelectionBox;
	private ImageView selectedIcon;
	private HashMap<ImageView, Grid.SelectionType> iconToType;
	private Grid grid;
	private boolean running = false;
	private ExecutorService threadPool;
	
	public GridController(Grid grid)
	{
		this.grid = grid;
		threadPool = Executors.newFixedThreadPool(1);
	}
	
	public VBox get()
	{
		iconToType = new HashMap<ImageView, Grid.SelectionType>();
		iconToType.put(startIcon, Grid.SelectionType.START);
		iconToType.put(endIcon, Grid.SelectionType.END);
		iconToType.put(obstacleIcon, Grid.SelectionType.OBSTACLE);

		return controller;
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
			grid.setSelectionType(Grid.SelectionType.NONE);
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
		if (icon == selectedIcon)
			return;
		ColorAdjust ca = new ColorAdjust();
		ca.setBrightness((e.getEventType() == MouseEvent.MOUSE_ENTERED) ? 0.5 : 0.0);
		icon.setEffect(ca);
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
		grid.setSelectionType(Grid.SelectionType.NONE);
		grid.clearVisited();
		grid.clearPath();
		grid.lock();
		int selectedAlgorithm = algorithmSelectionBox.getSelectionModel().getSelectedIndex();
		
		switch (selectedAlgorithm)
		{
			case DIJKSTRA:
				threadPool.execute(() -> {
					grid.setPath(Dijkstra.dijkstra(grid));
					running = false;
					grid.unlock();
				});
				break;
			case ASTAR:
				threadPool.execute(() -> {
					grid.setPath(AStar.aStar(grid));
					running = false;
					grid.unlock();
				});
				break;
			case BFS:
				threadPool.execute(() -> {
					grid.setPath(BreadthFirstSearch.BFS(grid));
					running = false;
					grid.unlock();
				});
				break;
			case DFS:
				threadPool.execute(() -> {
					grid.setPath(DepthFirstSearch.DFS(grid));
					running = false;
					grid.unlock();
				});
				break;
		}
		
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
