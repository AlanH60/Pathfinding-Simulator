package gui;

import java.util.HashMap;

import algorithms.Dijkstra;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GridController 
{
	@FXML private VBox controller;
	@FXML private ImageView startIcon;
	@FXML private ImageView endIcon;
	@FXML private ImageView obstacleIcon;
	private ImageView selectedIcon;
	private HashMap<ImageView, Grid.SelectionType> iconToType;
	private Grid grid;
	
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
		long start = System.currentTimeMillis();
		grid.setPath(Dijkstra.dijkstra(grid));
		System.out.println("Time Elapsed: " + (System.currentTimeMillis() - start) + " ms");
	}
	
	@FXML
	private void clearPath(MouseEvent e)
	{
		grid.clearPath();
	}
	
	@FXML
	private void clearObstacles(MouseEvent e)
	{
		grid.clearObstacles();
	}

}
