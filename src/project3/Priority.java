package project3;

public class Priority<T> {

	Node head; // First node of list
	
	static class Node
	{
		int time; // Time of the event
		EventType type = EventType.DialIn;
		
		Node next;
		
		Node(int t, EventType y)
		{
			time = t;
			type = y;
			next = null;
		}
	}
	
	public static Priority<Node> append(Priority<Node> list, int t, EventType y)
	{
		Node newNode = new Node(t, y);
		newNode.next = null;
		
		if (list.head == null)
		{
			list.head = newNode;
		}
		
		else
		{
			Node lastNode = list.head;
			while (lastNode.next != null)
			{
				lastNode = lastNode.next;
			}
			
			lastNode.next = newNode;
		}
		
		return list;
	}
	
	public static int getFirstTime(Priority<Node> list)
	{
		return list.head.time;
	}
	
	public static EventType getFirstType(Priority<Node> list)
	{
		return list.head.type;
	}
	
	public static void removeFirst(Priority<Node> list)
	{
		Node temp = list.head;
		temp = temp.next;
		list.head = temp;
	}
	
	public static Priority<Node> add(Priority<Node> list, int t, EventType y)
	{
		Node newNode = new Node(t, y);
		newNode.next = null;
		Node previousNode = list.head;
		Node currentNode = list.head;
		
		if (t < currentNode.time) // Insert at start of list
		{
			newNode.next = list.head;
			list.head = newNode;
			
			return list;
		}
		else
		{
			currentNode = currentNode.next;
		}
		
		while ((currentNode.next != null) && (t > currentNode.time))
		{
			previousNode = currentNode;
			currentNode = currentNode.next;
		}
		
		previousNode.next = newNode;
		newNode.next = currentNode;
		
		return list;
	}
}
