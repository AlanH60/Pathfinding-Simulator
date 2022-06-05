package gui;

public class Tile 
{
	enum Status
	{
		DEFAULT,
		START,
		END,
		PATH
	}
	
	private Status status = Status.DEFAULT;
	
	public Tile()
	{
		
	}
	
	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	public Status getStatus()
	{
		return this.status;
	}
}
