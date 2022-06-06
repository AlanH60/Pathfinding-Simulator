package gui;

public class Tile 
{	
	public static final int WIDTH = 25;
	enum Status
	{
		DEFAULT,
		START,
		END,
		OBSTACLE,
		PATH
	}
	
	private Status status = Status.DEFAULT;
	private boolean hovered = false;
	
	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	public void setHovered(boolean hovered)
	{
		this.hovered = hovered;
	}
	
	public boolean isHovered()
	{
		return hovered;
	}
	
	public Status getStatus()
	{
		return this.status;
	}
}
