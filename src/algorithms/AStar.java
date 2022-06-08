package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import data.PriorityQueue;
import data.Vector2;
import gui.Grid;
import javafx.application.Platform;

public class AStar 
{
	public static ArrayList<Vector2<Integer>> aStar(Grid grid)
	{
		try 
		{
			Vector2<Integer> start = grid.getStart();
			Vector2<Integer> end = grid.getEnd();
			int width = grid.getGridWidth();
			int height = grid.getGridHeight();
			
			HashMap<Vector2<Integer>, Double> gScores = new HashMap<Vector2<Integer>, Double>(width * height);
			HashMap<Vector2<Integer>, Double> hScores = new HashMap<Vector2<Integer>, Double>(width * height);
			HashMap<Vector2<Integer>, Vector2<Integer>> childToParent = new HashMap<Vector2<Integer>, Vector2<Integer>>(width * height);
			HashMap<Vector2<Integer>, Boolean> visited = new HashMap<Vector2<Integer>, Boolean>(width * height);
			PriorityQueue<Vector2<Integer>> queue = new PriorityQueue<Vector2<Integer>>(width * height);
			
			
			double dist = Util.dist(start, end);
			queue.add(start, dist);
			gScores.put(start, 0.0);
			hScores.put(start, dist);
			childToParent.put(start, null);
			
			while (!queue.isEmpty())
			{
				Vector2<Integer> curr = queue.next();
				
				visited.put(curr, true);
				if (curr.equals(end))
					break;
				
				Platform.runLater(()->{
					grid.setVisitedTile(curr);
				});
				Thread.sleep(10);

				ArrayList<Vector2<Integer>> neighbors = grid.getNeighbors(curr);
				for (Vector2<Integer> neighbor : neighbors)
				{
					if (!visited.containsKey(neighbor))
					{
						if (!hScores.containsKey(neighbor))
							hScores.put(neighbor, Util.dist(neighbor, end));
						
						double newGScore = Util.dist(neighbor, curr) + gScores.get(curr);
						
						if (!gScores.containsKey(neighbor) || newGScore < gScores.get(neighbor))
						{
							gScores.put(neighbor, newGScore);
							childToParent.put(neighbor, curr);
							queue.add(neighbor, hScores.get(neighbor) + gScores.get(neighbor));
						}
						
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
