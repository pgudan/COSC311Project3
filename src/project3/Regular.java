package project3;

import java.util.ArrayList;

public class Regular<E> {

	private ArrayList<Integer> regularEvents = new ArrayList<Integer>();
	int size, front, rear;
	
	Regular(int size)
	{
		this.size = size;
		this.front = -1;
		this.rear = -1;
	}
	
	public void add(int callTime)
	{
		if((front == 0 && rear == size - 1) || (rear == (front - 1) % (size - 1))) // Check for full array
		{
			System.out.println("Call dropped, queue full");
		}
		else if (front == -1) // Check for empty queue
		{
			front = 0;
			rear = 0;
			regularEvents.add(0, callTime);
		}
		else if(rear == size - 1 && front != 0) // Check for need to set over old value
		{
			rear = 0;
			regularEvents.set(0, callTime);
		}
		else // Array starts at index 0
		{
			if (front <= rear) // Add new element if array has not looped
			{
				regularEvents.add(rear, callTime);
			}
			else // Set to replace old element if array has looped
			{
				regularEvents.set(rear, callTime);
			}
		}
	}
	
	public int remove()
	{
		int temp;
		
		if (front == -1)
		{
			return -1; // Empty array
		}
		
		temp = regularEvents.get(front);
		
		if (front == rear) // If only one element, set to empty array
		{
			front = -1;
			rear = -1;
		}
		else if (front == size - 1) // If front is at index size
		{
			front = 0;
		}
		else // Else, move front node 1 back
		{
			front = front + 1;
		}
		
		return temp; // Return value of removed element
	}
	
	public int getFirst()
	{
		int temp;
		
		if (front == -1) // Empty array
		{
			return -1;
		}
		
		return regularEvents.get(front);
	}
}
