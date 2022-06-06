package gui;

public class Tile 
{	
	public static final int WIDTH = 30;
	enum Status
	{
		DEFAULT,
		START,
		END,
		OBSTACTLE,
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
