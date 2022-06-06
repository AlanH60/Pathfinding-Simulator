package gui;
import data.Vector2;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
	private static boolean imagesLoaded = false;
	
	enum SelectionType
	{
		START,
		END,
		OBSTACLE,
		NONE
	}
	
	private int width, height;
	private Vector2<Integer> hover = new Vector2<Integer>(0, 0);
	private Vector2<Integer> start = new Vector2<Integer>(0, 0);
	private Vector2<Integer> end = new Vector2<Integer>(0, 0);
	private Tile[] tiles;
	private GridPane gp;
	private SelectionType selectionType = SelectionType.NONE;
	private boolean createObstacle = true;
	
	public Grid(int width, int height) throws FileNotFoundException
	{
		this.width = width;
		this.height = height;
		gp = new GridPane();
		EventHandler<MouseEvent> handleHover = (e) -> {
			int x = (int)e.getX() / Tile.WIDTH;
			int y = (int)e.getY() / Tile.WIDTH;
			if (x < 0 || x >= width || y < 0 || y >= height)
				return;
			
			getTile(hover.x, hover.y).setHovered(false);
			((ImageView)gp.getChildren().get(hover.y * width + hover.x)).setEffect(null);
			hover.x = x;
			hover.y = y;
			getTile(hover.x, hover.y).setHovered(true);
			if (getTileStatus(hover.x, hover.y) == Tile.Status.DEFAULT)
				((ImageView)gp.getChildren().get(hover.y * width + hover.x)).setEffect(new ColorAdjust(0, 0, 0.5, 0));
			update();
		};
		EventHandler<MouseEvent> handleClick = (e) -> {
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
					createObstacle = getTileStatus(hover.x, hover.y) != Tile.Status.OBSTACTLE;
				if (createObstacle)
					setObstacle(hover.x, hover.y);
				else
					deleteObstacle(hover.x, hover.y);
				break;
			case NONE:
			default:
				break;
			}
			update();
		};
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
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void setStart(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.END || getTileStatus(x, y) == Tile.Status.OBSTACTLE)
			return;
		setStatus(start.x, start.y, Tile.Status.DEFAULT);
		start.x = x;
		start.y = y;
		setStatus(start.x, start.y, Tile.Status.START);
	}
	
	public void setEnd(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.START || getTileStatus(x, y) == Tile.Status.OBSTACTLE)
			return;
		setStatus(end.x, end.y, Tile.Status.DEFAULT);
		end.x = x;
		end.y = y;
		setStatus(end.x, end.y, Tile.Status.END);
	}
	
	public void setObstacle(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.START || getTileStatus(x, y) == Tile.Status.END)
			return;
		setStatus(x, y, Tile.Status.OBSTACTLE);
	}
	
	public void deleteObstacle(int x, int y)
	{
		if (getTileStatus(x, y) == Tile.Status.OBSTACTLE)
			setStatus(x, y, Tile.Status.DEFAULT);
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
	
	public Tile getTile(int x, int y)
	{
		assert(!(x < 0 || x >= width || y < 0 || y >= height));
		return tiles[y * width + x];
	}
	
	public Image getTileImage(int x, int y)
	{
		switch (getTile(x, y).getStatus())
		{
			case START:
				return startTile;
			case END:
				return endTile;
			case PATH:
				return pathTile;
			case OBSTACTLE:
				return obstacleTile;
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
	
	public Tile.Status getTileStatus(int x, int y)
	{
		return getTile(x, y).getStatus();
	}
	
	private void setStatus(int x, int y, Tile.Status status)
	{
		getTile(x, y).setStatus(status);
	}
	
	public GridPane getGridPane()
	{
		return gp;
	}
	
	public void setSelectionType(SelectionType selectionType)
	{
		this.selectionType = selectionType;
	}
	
	public SelectionType getSelectionType()
	{
		return selectionType;
	}
	
}
