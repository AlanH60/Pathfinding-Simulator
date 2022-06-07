package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import data.PriorityQueue;
import data.Vector2;
import gui.Grid;

public class AStarJPS 
{
	public static ArrayList<Vector2<Integer>> aStarJPS(Grid grid)
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
			ArrayList<Vector2<Integer>> neighbors = grid.getNeighbors(curr);
			for (int x = -1; x <= 1; x ++)
				for (int y = -1; y <= 1; y ++)
				{
					Vector2<Integer> neighbor = new Vector2<Integer>(curr.x + x, curr.y + y);
					if (neighbors.contains(neighbor) && !visited.containsKey(neighbor))
					{
						neighbor = jump(grid, curr, x, y, start, end, true);
						if (neighbor == null)
							continue;
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
		
		if (!childToParent.containsKey(end))
			return new ArrayList<Vector2<Integer>>(visited.keySet());;
		ArrayList<Vector2<Integer>> path = new ArrayList<Vector2<Integer>>();
		path.add(end);
		while (!childToParent.get(path.get(0)).equals(start))
			path.add(0, childToParent.get(path.get(0)));
		return path;
		
	}
	
	private static Vector2<Integer> jump(Grid grid, Vector2<Integer> curr, int dx, int dy, Vector2<Integer> start, Vector2<Integer> end, boolean first)
	{
		Vector2<Integer> next = new Vector2<Integer>(curr.x + dx, curr.y + dy);
		if (grid.hasObstacle(next))
			return null;
		if (next.equals(end))
			return next;
		if (dx != 0 && dy != 0)
		{
			Vector2<Integer> adjacent1 = new Vector2<Integer>(next.x - dx, next.y);
			Vector2<Integer> adjacent2 = new Vector2<Integer>(next.x, next.y - dy);
			if (grid.hasObstacle(adjacent1) && grid.hasObstacle(adjacent2))
				return null;
			
			Vector2<Integer> nextAdjacent1 = new Vector2<Integer>(next.x - dx, next.y + dy);
			Vector2<Integer> nextAdjacent2 = new Vector2<Integer>(next.x + dx, next.y - dy);
			
			if (grid.hasObstacle(adjacent1) && !grid.hasObstacle(nextAdjacent1) || 
				grid.hasObstacle(adjacent2) && !grid.hasObstacle(nextAdjacent2))
				return next;
			
			if (jump(grid, next, dx, 0, start, end, false) != null || 
				jump(grid, next, 0, dy, start, end, false) != null)
				return next;
		}
		else if (dx != 0)
		{
			Vector2<Integer> bottom = new Vector2<Integer>(next.x, next.y + 1);
			Vector2<Integer> nextBottom = new Vector2<Integer>(next.x + dx, next.y + 1);
			Vector2<Integer> top = new Vector2<Integer>(next.x, next.y - 1);
			Vector2<Integer> nextTop = new Vector2<Integer>(next.x + dx, next.y - 1);
			
			if (grid.hasObstacle(bottom) && !grid.hasObstacle(nextBottom) || grid.hasObstacle(top) && !grid.hasObstacle(nextTop))
				return next;
			
			if (first &&
				(jump(grid, next, 0, 1, start, end, false) != null || 
				jump(grid, next, 0, -1, start, end, false) != null))
					return next;
		
		}
		else
		{
			Vector2<Integer> right = new Vector2<Integer>(next.x + 1, next.y);
			Vector2<Integer> nextRight = new Vector2<Integer>(next.x + 1, next.y );
			Vector2<Integer> left = new Vector2<Integer>(next.x - 1, next.y);
			Vector2<Integer> nextLeft = new Vector2<Integer>(next.x - 1, next.y + dy);
			
			if (grid.hasObstacle(right) && !grid.hasObstacle(nextRight) || grid.hasObstacle(left) && !grid.hasObstacle(nextLeft))
				return next;
			if (first &&
				(jump(grid, next, 1, 0, start, end, false) != null || 
				jump(grid, next, -1, 0, start, end, false) != null))
						return next;
		}
		return jump(grid, next, dx, dy, start, end, first);
	}
	
}
