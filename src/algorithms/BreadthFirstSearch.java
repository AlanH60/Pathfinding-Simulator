package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import data.PriorityQueue;
import data.Vector2;
import gui.Grid;
import javafx.application.Platform;

public class BreadthFirstSearch 
{
	public static ArrayList<Vector2<Integer>> BFS(Grid grid)
	{
		int width = grid.getGridWidth();
		int height = grid.getGridHeight();
		Vector2<Integer> start = grid.getStart();
		Vector2<Integer> end = grid.getEnd();
		

		HashMap<Vector2<Integer>, Vector2<Integer>> childToParent = new HashMap<Vector2<Integer>, Vector2<Integer>>(width * height);
		ArrayList<Vector2<Integer>> next = new ArrayList<Vector2<Integer>>(width * height);
		childToParent.put(start, null);
		next.add(start);
		while (!next.isEmpty())
		{
			Vector2<Integer> curr = next.get(0);
			
			if (curr.equals(end))
				break;
			Platform.runLater(()->{
				grid.update();
				grid.setVisited(curr);
			});
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e) {}
			ArrayList<Vector2<Integer>> neighbors = grid.getNeighbors(curr);
			for (Vector2<Integer> neighbor : neighbors)
			{
				if (neighbor.x != curr.x && neighbor.y != curr.y)
					continue;
				if (!childToParent.containsKey(neighbor))
				{
					next.add(neighbor);
					childToParent.put(neighbor, curr);
				}
			}
			next.remove(0);
		}
		if (!childToParent.containsKey(end))
			return null;
		ArrayList<Vector2<Integer>> path = new ArrayList<Vector2<Integer>>();
		path.add(end);
		while (!childToParent.get(path.get(0)).equals(start))
			path.add(0, childToParent.get(path.get(0)));
		return path;
		
	}
}
