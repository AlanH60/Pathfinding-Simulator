package data;
import java.util.ArrayList;
import java.util.HashMap;

public class PriorityQueue<T>
{
	private class Node
	{
		private T value;
		private double priority;

		public Node(T value, double priority)
		{
			this.value = value;
			this.priority = priority;
		}
		
		public boolean equals(Object other)
		{
			if (other instanceof PriorityQueue<?>.Node)
				return ((PriorityQueue<?>.Node) other).value.equals(value);
			return false;
		}
	}
	
	private ArrayList<Node> tree;
	private HashMap<T, Integer> indices;

	public PriorityQueue(int maxSize)
	{
		tree = new ArrayList<Node>(maxSize);
		indices = new HashMap<T, Integer>(maxSize);
	}
	
	public void add(T value, double priority)
	{
		Node newNode = new Node(value, priority);
		if (indices.containsKey(value))
		{
			int idx = indices.get(value);
			tree.set(idx, newNode);
			sortUp(newNode);
			sortDown(newNode);
			return;
		}
		indices.put(value, tree.size());
		tree.add(newNode);
		sortUp(newNode);
	}
	
	public T next()
	{
		if (isEmpty())
			return null;
		Node temp = tree.get(0);
		swap(temp, tree.get(tree.size() - 1));
		tree.remove(temp);
		indices.remove(temp.value);
		if (!isEmpty())
			sortDown(tree.get(0));
		return temp.value;
	}
	
	public boolean isEmpty()
	{
		return tree.isEmpty();
	}

	private void sortUp(Node node)
	{
		int idx = indices.get(node.value);
		if (idx == 0)
			return;
		Node parent = tree.get((idx - 1) / 2);
		if (parent.priority > node.priority)
		{
			swap(node, parent);
			sortUp(node);
		}
	}
	
	private void sortDown(Node node)
	{
		int idx = indices.get(node.value);
		int leftIdx = idx * 2 + 1;
		int rightIdx = idx * 2 + 2;
		if (leftIdx < tree.size())
		{
			Node min = tree.get(leftIdx);
			if (rightIdx < tree.size() && min.priority > tree.get(rightIdx).priority)
				min = tree.get(rightIdx);
			if (node.priority > min.priority)
			{
				swap(node, min);
				sortDown(node);
			}
		}
	}
	
	private void swap(Node one, Node two)
	{
		int idxOne = indices.get(one.value);
		int idxTwo = indices.get(two.value);
		
		tree.set(idxTwo, tree.set(idxOne, two));
		indices.put(one.value, idxTwo);
		indices.put(two.value, idxOne);
		
	}
	
}
