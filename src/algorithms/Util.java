package algorithms;

import data.Vector2;

public class Util 
{
	public static double dist(Vector2<Integer> one, Vector2<Integer> two)
	{
		int xDiff = one.x - two.x;
		int yDiff = one.y - two.y;
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}
}
