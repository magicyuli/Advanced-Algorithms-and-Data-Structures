/**
 ******************************************************************************
 *                    HOMEWORK, 15-351/650
 ******************************************************************************
 *                    Amortized Dictionary
 ******************************************************************************
 *
 * Implementation of an Amortized Array-Based Dictionary data structure.
 *
 * This data structure supports duplicates and does *NOT* support storage of
 * null references.
 *
 * Notes:
 * 		-It is *highly* recommended that you begin by reading over all the methods,
 *       all the comments, and all the code that has already been written for you.
 *
 * 		-the specifications provided are to help you understand what the methods
 *       are supposed to accomplish.
 * 		-See the lab documentation & recitation notes for implementation details.
 *
 *
 * User ID(s):
 * Liruoyang Yu
 * liruoyay
 *
 *****************************************************************************/


import static java.util.Arrays.binarySearch;
import java.util.Arrays;


public class Dictionary<E extends Comparable<E>>  implements DictionaryInterface<E>
{
	/*
	 * Keeps track of the number of elements in the dictionary.
	 * Take a look at the implementation of size()
	 */
	private int size;
	/*
	 * The head reference to the linked list of Nodes.
	 * Take a look at the Node class.
	 */
	private Node head;

	/**
	 * Creates an empty dictionary.
	 */
	public Dictionary()
	{
		size = 0;
		head = null;
	}

	/**
	 * Adds item to the dictionary, thus making contains(item) true.
	 * Increments size to ensure size() is correct.
	 */
	public void add(E item)
	{
		if(item == null)
		{
			throw new NullPointerException("Error passing null object to add");
		}

		/* new a list */
		Node newNode = new Node(new Comparable[] {item}, head);
		head = newNode;

		while (head.next != null && head.size == head.next.size) {
			mergeDown();
		}
		size++;
	}

	/**
	 * Starting with the smallest array, mergeDown() merges arrays of the same size together until
	 * all the arrays have different size.
	 *
	 * This is very useful for implementing add(item)!
	 */
	private void mergeDown()
	{
		if (head.size != head.next.size) {
			throw new IllegalStateException("Two lists to be merged don't match in sizes.");
		}
		Comparable[] tmp = new Comparable[head.size * 2];
		Comparable[] a1 = head.array;
		Comparable[] a2 = head.next.array;
		int i = 0;
		int j = 0;
		int k = 0;
		while (i < head.size && j < head.size) {
			if (a1[i].compareTo(a2[j]) <= 0) {
				tmp[k++] = a1[i++];
			}
			else {
				tmp[k++] = a2[j++];
			}
		}
		while (i < head.size) {
			tmp[k++] = a1[i++];
		}
		while (i < head.size) {
			tmp[k++] = a2[j++];
		}

		Node newHead = new Node(tmp, head.next.next);
		head = newHead;
	}


	/**
	 * Returns true if the dictionary contains an element equal to item, otherwise- false.
	 * Use the method contains() in the Node class.
	 */
	public boolean contains(E item)
	{
		if(item == null)
		{
			throw new NullPointerException("Error passing null object to contain");
		}

		/* Search through all the lists */
		Node cur = head;
		while (cur != null) {
			if (cur.contains(item)) {
				return true;
			}
			cur = cur.next;
		}
		return false;
	}


	/**
	 * Returns the size of the dictionary.
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns a helpful string representation of the dictionary.
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		Node tmp = head;
		while(tmp != null)
		{
			sb.append( tmp.array.length + ": ");
			sb.append(tmp.toString());
			sb.append("\n");
			tmp = tmp.next;
		}
		return sb.toString();
	}



	/**
	 * Implementation of the underlying array-based data structure.
	 *
	 * You may add additional methods.
	 */
	@SuppressWarnings("unchecked")
	private static class Node
	{
		private Comparable[] array;
		private Node next;
		private int size;

		/**
		 * Creates an Node with the specified parameters.
		 */
		public Node(Comparable[] array, Node next)
		{
			this.array = array;
			this.next = next;
			this.size = array.length;
		}


		/**
		 * Returns	true, if there is an element in the array equal to item
		 * 			false, otherwise
		 */
		public boolean contains(Comparable item)
		{
			/* Do binary search */
			int h = 0;
			int t = size - 1;
			int mid = (h + t) / 2;
			while (h <= t) {
				if (array[mid].compareTo(item) < 0) {
					h = mid + 1;
				}
				else if (array[mid].compareTo(item) > 0) {
					t = mid - 1;
				}
				else {
					return true;
				}
				mid = (h + t) / 2;
			}

			return false;
		}

		/**
		 * Returns a useful representation of this Node.  (Note how this is used by Dictionary's toString()).
		 */
		public String toString()
		{
			return java.util.Arrays.toString(array);
		}
	}

}


