package gui;
import data.Vector2;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Grid
{
	private static Image defaultTile;
	private static Image startTile;
	private static Image endTile;
	private static Image pathTile;
	private static Image obstacleTile;
	private static Image visitedTile;
	private static boolean imagesLoaded = false;


	private int width, height;
	private Vector2<Integer> hover = new Vector2<Integer>(0, 0);
	private Vector2<Integer> start = new Vector2<Integer>(0, 0);
	private Vector2<Integer> end = new Vector2<Integer>(0, 0);
	private Tile[] tiles;
	private GridPane gp;
	private Tile.Status selectionType = Tile.Status.DEFAULT;
	private boolean createObstacle = true;
	private boolean showVisited = false;
	private boolean locked = false;

	private  EventHandler<MouseEvent> handleHover = (e) -> {
		if (locked)
			return;
		int x = (int)e.getX() / Tile.WIDTH;
		int y = (int)e.getY() / Tile.WIDTH;
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		
		getTile(hover.x, hover.y).setHovered(false);
		((ImageView)gp.getChildren().get(hover.y * width + hover.x)).setEffect(null);
		hover.x = x;
		hover.y = y;
		getTile(hover.x, hover.y).setHovered(true);
		if (getTileStatus(hover.x, hover.y) != Tile.Status.OBSTACLE)
			((ImageView)gp.getChildren().get(hover.y * width + hover.x)).setEffect(new ColorAdjust(0, 0, 0.5, 0));
		update();
	};
	
	private EventHandler<MouseEvent> handleClick = (e) -> {
		if (locked)
			return;
		if (showVisited)
		{
			clearVisited();
			clearPath();
			showVisited = false;
		}
		switch (selectionType)
		{
			case START:
				setStart(hover.x, hover.y);
				break;
			case END:
				setEnd(hover.x, hover.y);
				break;
			case OBSTACLE:
				if (e.getEventType() == MouseEvent.MOUSE_PRESSED)
					createObstacle = getTileStatus(hover.x, hover.y) != Tile.Status.OBSTACLE;
				if (createObstacle)
					setObstacle(hover.x, hover.y);
				else
					deleteObstacle(hover.x, hover.y);
				break;
			case DEFAULT:
			default:
				break;
		}
		update();
	};
	
	public Grid(int width, int height) throws FileNotFoundException
	{
		this.width = width;
		this.height = height;
		gp = new GridPane();
		gp.setOnMouseMoved(handleHover);
		gp.setOnMouseDragged((e) -> {
			handleHover.handle(e);
			handleClick.handle(e);
		});
		gp.setOnMousePressed(handleClick);
		tiles = new Tile[width * height];
		
		if (!imagesLoaded)
		{
			defaultTile = new Image(new FileInputStream("src/gui/res/Tile.png"));
			startTile = new Image(new FileInputStream("src/gui/res/StartTile.png"));
			endTile = new Image(new FileInputStream("src/gui/res/EndTile.png"));
			pathTile = new Image(new FileInputStream("src/gui/res/PathTile.png"));
			obstacleTile = new Image(new FileInputStream("src/gui/res/ObstacleTile.png"));
			visitedTile = new Image(new FileInputStream("src/gui/res/VisitedTile.png"));
			imagesLoaded = true;
		}
		
		for (int y = 0; y < height; y ++)
			for (int x = 0; x < width; x ++)
			{
				tiles[y * width + x] = new Tile();
				ImageView tileImg = new ImageView(defaultTile);
				tileImg.setFitWidth(Tile.WIDTH);
				tileImg.setFitHeight(Tile.WIDTH);
				gp.add(tileImg, x, y);
			}
		
		setEnd(width - 1, height - 1);
		setStart(0, 0);
		update();
	}
	
	public void update()
	{
		for (int x = 0; x < width; x ++)
		{
			for (int y = 0; y < height; y ++)
			{
				ImageView imgV = (ImageView)(gp.getChildren().get(y * width + x));
				imgV.setImage(getTileImage(x, y));
			}
		}
	}
	
	private void setStart(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.END || getTileStatus(x, y) == Tile.Status.OBSTACLE)
			return;
		setTileStatus(start.x, start.y, Tile.Status.DEFAULT);
		start.x = x;
		start.y = y;
		setTileStatus(start.x, start.y, Tile.Status.START);
	}
	
	private void setEnd(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.START || getTileStatus(x, y) == Tile.Status.OBSTACLE)
			return;
		setTileStatus(end.x, end.y, Tile.Status.DEFAULT);
		end.x = x;
		end.y = y;
		setTileStatus(end.x, end.y, Tile.Status.END);
	}
	
	private void setObstacle(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.START || getTileStatus(x, y) == Tile.Status.END)
			return;
		setTileStatus(x, y, Tile.Status.OBSTACLE);
	}
	
	private void deleteObstacle(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.OBSTACLE)
			setTileStatus(x, y, Tile.Status.DEFAULT);
	}
	
	public int getGridWidth()
	{
		return width;
	}
	
	public int getGridHeight()
	{
		return height;
	}
	
	public Vector2<Integer> getStart()
	{
		return start;
	}
	
	public Vector2<Integer> getEnd()
	{
		return end;
	}
	
	private Tile getTile(int x, int y)
	{
		assert(!(x < 0 || x >= width || y < 0 || y >= height));
		return tiles[y * width + x];
	}
	
	private Image getTileImage(int x, int y)
	{
		switch (getTile(x, y).getStatus())
		{
			case START:
				return startTile;
			case END:
				return endTile;
			case PATH:
				return pathTile;
			case OBSTACLE:
				return obstacleTile;
			case VISITED:
				return visitedTile;
			case DEFAULT:
			default:
				if (getTile(x,y).isHovered())
				{
					switch(selectionType)
					{
						case START:
							return startTile;
						case END:
							return endTile;
						case OBSTACLE:
							return obstacleTile;
						default:
					}
				}
				return defaultTile;
			
		}
	}
	
	private Tile.Status getTileStatus(int x, int y)
	{
		return getTile(x, y).getStatus();
	}
	
	private void setTileStatus(int x, int y, Tile.Status status)
	{
		getTile(x, y).setStatus(status);
	}
	
	public GridPane getGridPane()
	{
		return gp;
	}
	
	public void setSelectionType(Tile.Status selectionType)
	{
		this.selectionType = selectionType;
	}
	
	public Tile.Status getSelectionType()
	{
		return selectionType;
	}
	
	public boolean hasObstacle(int x, int y)
	{
		if (!validTile(x, y))
			return true;
		return getTileStatus(x, y) == Tile.Status.OBSTACLE;
	}
	
	public boolean hasObstacle(Vector2<Integer> tile)
	{
		return hasObstacle(tile.x, tile.y);
	}
	
	public ArrayList<Vector2<Integer>> getNeighbors(Vector2<Integer> tile)
	{
		ArrayList<Vector2<Integer>> neighbors = new ArrayList<Vector2<Integer>>(8);
		for (int x = -1; x <= 1; x ++)
			for (int y = -1; y <= 1; y ++)
			{
				Vector2<Integer> neighbor = new Vector2<Integer>(tile.x + x, tile.y + y);
				if (neighbor.x < 0 || neighbor.x >= width || neighbor.y < 0 || neighbor.y >= height)
					continue;
				if (hasObstacle(neighbor.x, neighbor.y) || tile.equals(neighbor))
					continue;
				if (x != 0 && y != 0 && hasObstacle(neighbor.x, tile.y) && hasObstacle(tile.x, neighbor.y))
					continue;
				neighbors.add(neighbor);
			}
		return neighbors;
	}
	
	public void clearPath()
	{
		for (Tile t : tiles)
			if (t.getStatus() == Tile.Status.PATH)
				t.setStatus(Tile.Status.DEFAULT);
		update();
	}
	
	public void clearVisited()
	{
		for (Tile t : tiles)
			if (t.getStatus() == Tile.Status.VISITED)
				t.setStatus(Tile.Status.DEFAULT);
		update();
	}
	
	public void clearObstacles()
	{
		for (Tile t : tiles)
			if (t.getStatus() == Tile.Status.OBSTACLE)
				t.setStatus(Tile.Status.DEFAULT);
		update();
	}
	
	public void setVisitedTile(Vector2<Integer> visited)
	{
		showVisited = true;
		if (getTileStatus(visited.x, visited.y) != Tile.Status.DEFAULT)
			return;
		setTileStatus(visited.x, visited.y, Tile.Status.VISITED);
		update();
	}
	
	public boolean setPath(ArrayList<Vector2<Integer>> path)
	{
		clearPath();
		if (path == null)
			return true;
		if (path.isEmpty())
			return false;
		
		for (Vector2<Integer> coord : path)
		{
			if (getTileStatus(coord.x, coord.y) != Tile.Status.DEFAULT && getTileStatus(coord.x, coord.y) != Tile.Status.VISITED )
				continue;
			setTileStatus(coord.x, coord.y, Tile.Status.PATH);
		}
		update();
		return true;
	}
	
	public void lock()
	{
		locked = true;
	}
	
	public void unlock()
	{
		locked = false;
	}
	
	public boolean validTile(int x, int y)
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}
}
