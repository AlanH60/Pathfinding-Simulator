package gui;

import java.util.HashMap;

import algorithms.AStar;
import algorithms.AStarJPS;
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
	@FXML private ComboBox algorithmSelectionBox;
	private ImageView selectedIcon;
	private HashMap<ImageView, Grid.SelectionType> iconToType;
	private Grid grid;
	private boolean running = false;
	Thread t;
	
	public GridController(Grid grid)
	{
		this.grid = grid;
	}
	
	public VBox get()
	{
		iconToType = new HashMap<ImageView, Grid.SelectionType>()
		{{
				put(startIcon, Grid.SelectionType.START);
				put(endIcon, Grid.SelectionType.END);
				put(obstacleIcon, Grid.SelectionType.OBSTACLE);
		}};
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
		running = true;
		endIcon.setEffect(null);
		startIcon.setEffect(null);
		obstacleIcon.setEffect(null);
		selectedIcon = null;
		grid.setSelectionType(Grid.SelectionType.NONE);
		grid.clearVisited();
		grid.clearPath();
		long start = System.currentTimeMillis();
		int selectedAlgorithm = algorithmSelectionBox.getSelectionModel().getSelectedIndex();
		
		switch (selectedAlgorithm)
		{
		case DIJKSTRA:
			System.out.print("Dijkstra: ");
			t = new Thread(
			() ->
			{
				grid.setPath(Dijkstra.dijkstra(grid));
				running = false;
			});
			t.start();
			break;
		case ASTAR:
			System.out.print("A*: ");
			t = new Thread(
			() ->
			{
				grid.setPath(AStar.aStar(grid));
				running = false;
			});
			t.start();
			break;
		case BFS:
			System.out.print("BFS: ");
			t = new Thread(
			() ->
			{
				grid.setPath(BreadthFirstSearch.BFS(grid));
				running = false;
			});
			t.start();
			break;
		case DFS:
			System.out.print("DFS: ");
			t = new Thread(
			() ->
			{
				grid.setPath(DepthFirstSearch.DFS(grid));
				running = false;
			});
			t.start();
			
			break;
		}
		
		System.out.println("Time Elapsed: " + (System.currentTimeMillis() - start) + " ms");
	}
	
	@FXML
	private void clearObstacles(MouseEvent e)
	{
		grid.clearObstacles();
	}
	
	@FXML
	private void clear(MouseEvent e)
	{
		grid.clear();
	}

}
