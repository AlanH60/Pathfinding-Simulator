package data;

public class PriorityQueue<T>
{
	private class Node
	{
		private T value;
		private double priority;
		private Node next;
		
		public Node(T value, double priority, Node next)
		{
			this.value = value;
			this.priority = priority;
			this.next = next;
		}
	}
	
	private Node root;
	private int size;
	
	public PriorityQueue()
	{
		root = null;
		size = 0;
	}
	
	public void add(T value, double priority)
	{
		Node newNode = new Node(value, priority, null);
		if (get(value) != null)
		{
			newNode = get(value);
			remove(value);
		}
		size ++;
		if (root == null)
		{
			root = newNode;
			return;
		}
		if (priority < root.priority)
		{
			newNode.next = root;
			root = newNode;
			return;
		}
		else if (root.next == null)
		{
			root.next = newNode;
			return;
		}
		
		Node curr = root;
		while (priority > curr.next.priority)
		{
			if (curr.next.next == null)
			{
				curr.next.next = newNode;
				return;
			}
			else
				curr = curr.next;
		}
		newNode.next = curr.next;
		curr.next = newNode;
	}
	
	private Node get(T value)
	{
		Node curr = root;
		while (curr != null)
		{
			if (curr.value.equals(value))
				return curr;
			curr = curr.next;
		}
		return null;
	}
	
	private void remove(T value)
	{
		if (root == null)
			return;
		Node curr = root;
		while (curr.next != null)
		{
			if (curr.next.value.equals(value))
			{
				Node temp = curr.next;
				curr.next = curr.next.next;
				temp.next = null;
				size--;
				return;
			}
			curr = curr.next;
		}
	}
	
	public T next()
	{
		if (isEmpty())
			return null;
		Node temp = root;
		root = root.next;
		size--;
		return temp.value;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}
	
}
