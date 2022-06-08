package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import data.PriorityQueue;
import data.Vector2;
import gui.Grid;
import javafx.application.Platform;

public class Dijkstra 
{	
	
	public static ArrayList<Vector2<Integer>> dijkstra(Grid grid)
	{
		try 
		{
			int width = grid.getGridWidth();
			int height = grid.getGridHeight();
			Vector2<Integer> start = grid.getStart();
			Vector2<Integer> end = grid.getEnd();
			

			double[][] distances = new double[width][height];
			HashMap<Vector2<Integer>, Vector2<Integer>> childToParent = new HashMap<Vector2<Integer>, Vector2<Integer>>(width * height);
			PriorityQueue<Vector2<Integer>> queue = new PriorityQueue<Vector2<Integer>>(width * height);
			queue.add(start, 0);
			distances[start.x][start.y] = 0;  
			childToParent.put(start, null);
			
			for (int x = 0; x < width; x ++)
				for (int y = 0; y < height; y ++)
				{
					if (grid.hasObstacle(x, y) || start.equals(x, y))
						continue; 
					distances[x][y] = Double.MAX_VALUE;
				}
			
			while (!queue.isEmpty())
			{
				Vector2<Integer> curr = queue.next();
				
				if (curr.equals(end))
					break;
				
				Platform.runLater(()->{
					grid.setVisitedTile(curr);
				});
				Thread.sleep(10);

				ArrayList<Vector2<Integer>> neighbors = grid.getNeighbors(curr);
				for (Vector2<Integer> neighbor : neighbors)
				{
					double newDistance = distances[curr.x][curr.y] + Util.dist(neighbor, curr);
					if (distances[neighbor.x][neighbor.y] > newDistance)
					{
						distances[neighbor.x][neighbor.y] = newDistance; 
						childToParent.put(neighbor, curr);
						queue.add(neighbor, newDistance);
					}
				}
			}
			
			ArrayList<Vector2<Integer>> path = new ArrayList<Vector2<Integer>>();
			if (!childToParent.containsKey(end))
				return path;
			
			path.add(end);
			while (!childToParent.get(path.get(0)).equals(start))
				path.add(0, childToParent.get(path.get(0)));
			return path;
		}
		catch (InterruptedException e)
		{
			Platform.runLater(()->{
				grid.clearVisited();
			});
			return null;
		}

	}
}
