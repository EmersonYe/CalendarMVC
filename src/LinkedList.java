import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
   An implementation of a doubly linked list.
 */
public class LinkedList
{  
	ArrayList<ChangeListener> listeners;
	Node first;
	Node last;
	/**
	 * 
	 * @author emersonye
	 *
	 */
	public void addChangeListener(ChangeListener listener)
	{
		listeners.add(listener);
	}
	class Pointer
	{  
		Node position;

		/**
         Constructs a pointer that points to the front
         of the linked list.
		 */
		public Pointer()
		{  
			position = first;
		}

		/**
         Moves the pointer to the next element.
         @throws NoSuchElementException when the pointer is at
         the past-the-end position.
		 */
		public void next() throws NoSuchElementException
		{  
			if(position.previous == last)
				throw new NoSuchElementException();
			position = position.next;
		}

		/**
         Tests if one can call next on the pointer.
         @return true if there is an element or the past-the-end position
         after the pointer position
		 */
		public boolean hasNext()
		{  
			if (position == null)
				return false;
			if(position.next != null || position == last)
				return true;
			return false;
		}

		/**
         Gets the object at the pointer.
         @throws NoSuchElementException when the pointer is at
         the past-the-end position.
		 */
		public Object get() throws NoSuchElementException
		{
			if (position.previous == last)
				throw new NoSuchElementException();
			return (position.data);
		}

		/**
         Sets the pointer element to a different value. 
         @param element the element to set
         @throws NoSuchElementException when the pointer is at
         the past-the-end position.
		 */
		public void set(Object element) throws NoSuchElementException
		{
			if (!hasNext())
				throw new NoSuchElementException();
			position.data = element;
		}

		/**
         Moves the pointer before the previous element.
         @throws NoSuchElementException when trying to move before the
         first element
		 */
		public void previous()
		{  
			if (hasPrevious())
			{
				if(position == null)
					position = last;
				else
					position = position.previous;
			}
		}

		/**
         Tests if there is an element before the pointer position.
         @return true if there is an element before the pointer position
		 */
		public boolean hasPrevious()
		{  
			if (position == first) return false;
			return true;
		}

		/**
         Adds an element before the pointer position
         @param element the element to add
		 */
		public void add(Object element)
		{  
			Node previous = new Node();
			Node addMe = new Node();
			addMe.data = element;
			addMe.next = position;
			if (position == null)
			{
				previous = last;
				last = addMe;
			}
			else
			{
				previous = position.previous;
				position.previous = addMe;
			}
			if(position == first)
			{
				first = addMe;
			}
			else
			{
				previous.next = addMe;
				addMe.previous = previous;
			}
			ChangeEvent event = new ChangeEvent(this);
			for (ChangeListener listener : listeners)
				listener.stateChanged(event); 
		}

		/**
         Removes the element at the pointer and moves the pointer
         to the next element.
         @throws NoSuchElementException when the pointer is at
         the past-the-end position.
		 */
		public void remove()
		{  
			if (position == first)
			{
				first = position.next;
				position.next.previous = null;	
				position = first;
			}
			else if (position == last)
			{
				last = last.previous;
				last.next = null;
			}
			else
			{
				Node previous = position.previous;
				previous.next = position.next;
				position.next.previous = previous;
				position = position.next;
			}
			ChangeEvent event = new ChangeEvent(this);
			for (ChangeListener listener : listeners)
				listener.stateChanged(event); 
		}
	}
	/**
	 * 
	 * @author emersonye
	 *
	 */
	class Node
	{  
		Object data;
		Node next;
		Node previous;
	}

	/** 
      Constructs an empty linked list.
	 */
	public LinkedList()
	{  
		first = null;
		last = null;
	}

	/**
      Returns a pointer for iterating through this list.
      @return a pointer for iterating through this list
	 */
	public Pointer listPointer()
	{  
		return new Pointer();
	}

	/**
      Returns the first element in the linked list.
      @return the first element in the linked list
	 */
	public Object getFirst()
	{  
		if (first == null) { throw new NoSuchElementException(); }
		return first.data;
	}

	/**
      Removes the first element in the linked list.
      @return the removed element
	 */
	public Object removeFirst()
	{  
		if (first == null) { throw new NoSuchElementException(); }
		Object element = first.data;
		first = first.next;
		if (first == null) { last = null; } // List is now empty
		else { first.previous = null; }
		return element;
	}

	/**
      Adds an element to the front of the linked list.
      @param element the element to add
	 */
	public void addFirst(Object element)
	{  
		Node newNode = new Node();
		newNode.data = element;
		newNode.next = first;
		newNode.previous = null;
		if (first == null) { last = newNode; }
		else { first.previous = newNode; }
		first = newNode;
	}

	/**
      Returns the last element in the linked list.
      @return the last element in the linked list
	 */
	public Object getLast()
	{  
		if (last == null) { throw new NoSuchElementException(); }
		return last.data;
	}

	/**
      Removes the last element in the linked list.
      @return the removed element
	 */
	public Object removeLast()
	{  
		if (last == null) { throw new NoSuchElementException(); }
		Object element = last.data;
		last = last.previous;
		if (last == null) { first = null; } // List is now empty
		else { last.next = null; }
		return element;
	}

	/**
      Adds an element to the back of the linked list.
      @param element the element to add
	 */
	public void addLast(Object element)
	{  
		Node newNode = new Node();
		newNode.data = element;
		newNode.next = null;
		newNode.previous = last;
		if (last == null) { first = newNode; }
		else { last.next = newNode; }
		last = newNode;
	}
}
