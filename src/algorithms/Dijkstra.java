package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import data.PriorityQueue;
import data.Vector2;
import gui.Grid;

public class Dijkstra 
{	
	
	public static ArrayList<Vector2<Integer>> dijkstra(Grid grid)
	{
		int width = grid.getGridWidth();
		int height = grid.getGridHeight();
		Vector2<Integer> start = grid.getStart();
		Vector2<Integer> end = grid.getEnd();
		

		double[][] distances = new double[width][height];
		HashMap<Vector2<Integer>, Vector2<Integer>> childToParent = new HashMap<Vector2<Integer>, Vector2<Integer>>();
		PriorityQueue<Vector2<Integer>> queue = new PriorityQueue<Vector2<Integer>>();
		queue.add(start, 0);
		distances[start.x][start.y] = 0;  
		childToParent.put(start, null);
		
		for (int x = 0; x < width; x ++)
			for (int y = 0; y < height; y ++)
			{
				if (grid.hasObstacle(x, y) || start.equals(x, y))
					continue;
				int xDiff = Math.abs(start.x - x);
				int yDiff = Math.abs(start.y - y);
				if (grid.viableNeighbor(start, new Vector2<Integer>(x, y)))
				{
					distances[x][y] = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
					queue.add(new Vector2<Integer>(x, y), distances[x][y]);
				}
				else
					distances[x][y] = Double.MAX_VALUE;
				childToParent.put(new Vector2<Integer>(x, y), start);
			}
		
		while (!queue.isEmpty())
		{
			Vector2<Integer> curr = queue.next();
			if (curr.equals(end))
				break;
			for (int x = -1; x <= 1; x ++)
				for (int y = -1; y <= 1; y ++)
				{
					Vector2<Integer> neighbor = new Vector2<Integer>(curr.x + x, curr.y + y);
					if (grid.viableNeighbor(curr, neighbor))
					{
						double newDistance = distances[curr.x][curr.y] + Math.sqrt(x * x + y * y);
						if (distances[neighbor.x][neighbor.y] > newDistance)
						{
							distances[neighbor.x][neighbor.y] = newDistance; 
							childToParent.put(neighbor, curr);
							queue.add(neighbor, newDistance);
						}
					}
				}
		}
		if (distances[end.x][end.y] == Double.MAX_VALUE)
			return null;
		ArrayList<Vector2<Integer>> path = new ArrayList<Vector2<Integer>>();
		path.add(end);
		while (!childToParent.get(path.get(0)).equals(start))
			path.add(0, childToParent.get(path.get(0)));
		return path;
		
	}
}
