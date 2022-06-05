package gui;
import data.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Grid extends GridPane
{
	enum Status
	{
		DEFAULT,
		START,
		END,
		PATH
	}
	private int width, height;
	private Status[] cellStatus;
	private Vector2<Integer> start = new Vector2<Integer>(0,0);
	private Vector2<Integer> end = new Vector2<Integer>(0,0);
	private Tile[] tiles;
	private GridPane gp;
	
	public Grid(int width, int height)
	{
		this.width = width;
		this.height = height;
		cellStatus = new Status[width * height];
		gp = new GridPane();
		for (int x = 0; x < width; x ++)
			for (int y = 0; y < height; y ++)
			{
				tiles[y * width + x] = new Tile();
				ImageView tileImg = new ImageView(new Image("res\\Tile.png"));
				gp.add(tileImg, x, y);
			}
		
		update();
	}
	
	public void update()
	{
		for (int x = 0; x < width; x ++)
		{
			for (int y = 0; y < height; y ++)
			{
				if (start.equals(x, y))
					tiles[y * width + x].setStatus(Tile.Status.START);
				else if (end.equals(x, y))
					tiles[y * width + x].setStatus(Tile.Status.END);
				else
					tiles[y * width + x].setStatus(Tile.Status.DEFAULT);
				ImageView imgV = (ImageView)(getChildren().get(y * width + x));
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
		start.x = x;
		start.y = y;
	}
	
	public void setEnd(int x, int y)
	{
		end.x = x;
		end.y = y;
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
	
	public Image getTileImage(int x, int y)
	{
		switch (tiles[y * width  + x].getStatus())
		{
			case START:
				return new Image("res\\Tile.png");
			case END:
				return new Image("res\\Tile.png");
			case PATH:
				return new Image("res\\Tile.png");
			case DEFAULT:
			default:
				return new Image("res\\Tile.png");
			
		}
	}
	
	public Status getStatus(int x, int y)
	{
		return cellStatus[y * width + x];
	}
	
	
}
