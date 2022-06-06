package data;

public class Vector2<T>
{
	public T x, y;
	
	public Vector2()
	{
		
	}
	
	public Vector2(T x, T y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(T x, T y)
	{
		return this.x == x && this.y == y;
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof Vector2<?>)
			return ((Vector2<?>)other).x == x && ((Vector2<?>)other).y == y;
		return false;
	}
	
	public String toString()
	{
		return "(" + x.toString() + ", " + y.toString() + ")";
	}
	
	public int hashCode()
	{
		return x.hashCode() + y.hashCode();
	}
}
