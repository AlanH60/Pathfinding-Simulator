package data;

public class Vector2<T>
{
	public T x, y;
	public Vector2(T x, T y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(T x, T y)
	{
		return this.x == x && this.y == y;
	}
}
