package heap_package;
import java.util.ArrayList;
 
public class Heap{

	protected Node root;								// root of the heap
	protected Node[] nodes_array;                    // Stores the address of node corresponding to the keys
	private int max_size;                           // Maximum number of nodes heap can have 
	private static final String NullKeyException = "NullKey";      // Null key exception
	private static final String NullRootException = "NullRoot";    // Null root exception
	private static final String KeyAlreadyExistsException = "KeyAlreadyExists";   // Key already exists exception

	/* 
	   1. Can use helper methods but they have to be kept private. 
	   2. Not allowed to use any data structure. 
	*/

	public Heap(int max_size, int[] keys_array, int[] values_array) throws Exception{

		/* 
		   1. Create Max Heap for elements present in values_array.
		   2. keys_array.length == values_array.length and keys_array.length number of nodes should be created. 
		   3. Store the address of node created for keys_array[i] in nodes_array[keys_array[i]].
		   4. Heap should be stored based on the value i.e. root element of heap should 
		      have value which is maximum value in values_array.
		   5. max_size denotes maximum number of nodes that could be inserted in the heap. 
		   6. keys will be in range 0 to max_size-1.
		   7. There could be duplicate keys in keys_array and in that case throw KeyAlreadyExistsException. 
		*/

		/* 
		   For eg. keys_array = [1,5,4,50,22] and values_array = [4,10,5,23,15] : 
		   => So, here (key,value) pair is { (1,4), (5,10), (4,5), (50,23), (22,15) }.
		   => Now, when a node is created for element indexed 1 i.e. key = 5 and value = 10, 
		   	  that created node address should be saved in nodes_array[5]. 
		*/ 

		/*
		   n = keys_array.length
		   Expected Time Complexity : O(n).
		*/

		this.max_size = max_size;
		this.nodes_array = new Node[this.max_size];

		// To be filled in by the student
		int n = keys_array.length;
		nodes_array[keys_array[0]] = new Node(keys_array[0], values_array[0], null);
		root = nodes_array[keys_array[0]];
		for (int i = 0; i < n; i++) {
			if (2*i+1 < n) {
				if (nodes_array[keys_array[2*i+1]] != null) {
					throw new Exception(KeyAlreadyExistsException);
				}
				nodes_array[keys_array[2*i+1]] = new Node(keys_array[2*i+1], values_array[2*i+1], nodes_array[keys_array[i]]);
				nodes_array[keys_array[i]].left = nodes_array[keys_array[2*i+1]];
			}
			if (2*i+2 < n) {
				if (nodes_array[keys_array[2*i+2]] != null) {
					throw new Exception(KeyAlreadyExistsException);
				}
				nodes_array[keys_array[2*i+2]] = new Node(keys_array[2*i+2], values_array[2*i+2], nodes_array[keys_array[i]]);
				nodes_array[keys_array[i]].right = nodes_array[keys_array[2*i+2]];
			}
		}
		for (int i = n-1; i>=0 ; i--) {
			nodeHeight(nodes_array[keys_array[i]]);
			completeCheck(nodes_array[keys_array[i]]);
			swapDown(nodes_array[keys_array[i]]);
		}
	}

	public ArrayList<Integer> getMax() throws Exception{

		/* 
		   1. Returns the keys with maximum value in the heap.
		   2. There could be multiple keys having same maximum value. You have
		      to return all such keys in ArrayList (order doesn't matter).
		   3. If heap is empty, throw NullRootException.

		   Expected Time Complexity : O(1).
		*/

		ArrayList<Integer> max_keys = new ArrayList<Integer>();    // Keys with maximum values in heap.

		// To be filled in by the student
		if (root == null) {
			throw new Exception(NullRootException);
		}

		max_keys.add(root.key);
		for (int i = 0; i < max_keys.size(); i++) {
			if (nodes_array[max_keys.get(i)].left != null && nodes_array[max_keys.get(i)].value == nodes_array[max_keys.get(i)].left.value) {
				max_keys.add(nodes_array[max_keys.get(i)].left.key);
			}
			if (nodes_array[max_keys.get(i)].right != null && nodes_array[max_keys.get(i)].value == nodes_array[max_keys.get(i)].right.value) {
				max_keys.add(nodes_array[max_keys.get(i)].right.key);
			}
		}
		return max_keys;
	}

	public void insert(int key, int value) throws Exception{

		/* 
		   1. Insert a node whose key is "key" and value is "value" in heap 
		      and store the address of new node in nodes_array[key]. 
		   2. If key is already present in heap, throw KeyAlreadyExistsException.

		   Expected Time Complexity : O(logn).
		*/

		// To be filled in by the student
		System.out.println("insert"+key+","+value);
		if (nodes_array[key] != null) {
			throw new Exception(KeyAlreadyExistsException);
		}
		if (root == null) {
			root = new Node(key, value, null);
			nodes_array[key] = root;
		}
		else {
			lastNodeinsertion(key, value, root);	
			swapUp(nodes_array[key]);		
		}
	}

	public ArrayList<Integer> deleteMax() throws Exception{

		/* 
		   1. Remove nodes with the maximum value in the heap and returns their keys.
		   2. There could be multiple nodes having same maximum value. You have
		      to delete all such nodes and return all such keys in ArrayList (order doesn't matter).
		   3. If heap is empty, throw NullRootException.

		   Expected Average Time Complexity : O(logn).
		*/

		ArrayList<Integer> max_keys = new ArrayList<Integer>();   // Keys with maximum values in heap that will be deleted.

		// To be filled in by the student
		System.out.println("deleteMax");
		if (root == null) {
			System.out.println("delete exgtf");
			throw new Exception(NullRootException);
		}

		int maxValue = root.value;
		while (root != null && root.value == maxValue) {
			max_keys.add(root.key);
			lastNode(root);
			swapDown(root);
		}
		return max_keys;
	}

	public void update(int key, int diffvalue) throws Exception{

		/* 
		   1. Update the heap by changing the value of the node whose key is "key" to value+diffvalue.
		   2. If key doesn't exists in heap, throw NullKeyException.

		   Expected Time Complexity : O(logn).
		*/

		// To be filled in by the student
		if (nodes_array[key] == null) {
			System.out.println("null key exbgvv");
			throw new Exception(NullKeyException);
		}
		nodes_array[key].value += diffvalue;
		if (diffvalue >= 0) {
			swapUp(nodes_array[key]);
		}
		else {
			swapDown(nodes_array[key]);
		}
	}

	public int getMaxValue() throws Exception{

		/* 
		   1. Returns maximum value in the heap.
		   2. If heap is empty, throw NullRootException.

		   Expected Time Complexity : O(1).
		*/

		// To be filled in by the student
		if (root == null) {
			System.out.println("get max vmsldfkj ah");
			throw new Exception(NullRootException);
		}
		return root.value;
	}

	public ArrayList<Integer> getKeys() throws Exception{

		/*
		   1. Returns keys of the nodes stored in heap.
		   2. If heap is empty, throw NullRootException.
		 
		   Expected Time Complexity : O(n).
		*/

		ArrayList<Integer> keys = new ArrayList<Integer>();   // Stores keys of nodes in heap

		// To be filled in by the student
		if (root == null) {
			throw new Exception(NullRootException);
		}

		keys.add(root.key);
		for (int i = 0; i < keys.size(); i++) {
			if (nodes_array[keys.get(i)].left != null) {
				keys.add(nodes_array[keys.get(i)].left.key);
			}
			if (nodes_array[keys.get(i)].right != null) {
				keys.add(nodes_array[keys.get(i)].right.key);
			}
		}
		return keys;
	}

	// Write helper functions(if any) here (They have to be private).
	private void lastNodeinsertion(int key, int value, Node checkpoint) { //O(logn)
		if (checkpoint.left == null) {
			checkpoint.left = new Node(key, value, checkpoint);
			nodes_array[key] = checkpoint.left;
		}
		else if (checkpoint.right == null) {
			checkpoint.right = new Node(key, value, checkpoint);
			nodes_array[key] = checkpoint.right;
		}
		else if (checkpoint.left.is_complete == true) {
			if (checkpoint.right.is_complete == false || (checkpoint.right.is_complete == true && checkpoint.left.height != checkpoint.right.height)) {
				lastNodeinsertion(key, value, checkpoint.right);
			}
		}
		else {
			lastNodeinsertion(key, value, checkpoint.left);
		}
		nodeHeight(checkpoint);
		completeCheck(checkpoint);
	}

	private void lastNode(Node checkpoint) { //O(logn)
		if (root.height == 1) {
			root = null;
		}
		if (checkpoint.left == null) {
			root.key = checkpoint.key;
			root.value = checkpoint.value;
			checkpoint.parent.right = null;
			nodeHeight(checkpoint.parent);
			completeCheck(checkpoint.parent);
		}
		else if (checkpoint.right == null) {
			root.key = checkpoint.left.key;
			root.value = checkpoint.left.value;
			checkpoint.left = null;
		}
		else if (checkpoint.left.is_complete == false) {
			lastNode(checkpoint.left);	
		}
		else if (checkpoint.left.is_complete == true) {
			if (checkpoint.right.is_complete == false) {
				lastNode(checkpoint.right);
			}
			else {
				if (checkpoint.left.height == checkpoint.right.height) {
					lastNode(checkpoint.right);
				}
				else {
					lastNode(checkpoint.left);
				}
			}
		}
		nodeHeight(checkpoint);
		completeCheck(checkpoint);
	}

	private void swapUp(Node checkpoint) { //O(logn)
		while (checkpoint.parent != null && checkpoint.parent.value < checkpoint.value) {
			swap(checkpoint, checkpoint.parent);
			checkpoint = checkpoint.parent;
		}
	}

	private void swapDown(Node checkpoint) { //O(logn)
		if (checkpoint.left == null) {}
		else if (checkpoint.right == null) {
			if (checkpoint.value < checkpoint.left.value) {
				swap(checkpoint, checkpoint.left);
			}
		}
		else if (checkpoint.value < checkpoint.left.value || checkpoint.value < checkpoint.right.value) {
			if (checkpoint.left.value > checkpoint.right.value) {
				swap(checkpoint, checkpoint.left);
				swapDown(checkpoint.left);
			}
			else {
				swap(checkpoint, checkpoint.right);
				swapDown(checkpoint.right);
			}
		}
	}

	private void swap(Node pointer1, Node pointer2) { //O(1)
		int tempKey = pointer1.key;
		int tempValue = pointer1.value;
		pointer1.key = pointer2.key;
		pointer1.value = pointer2.value;
		pointer2.key = tempKey;
		pointer2.value = tempValue;
		nodes_array[pointer1.key] = pointer1;
		nodes_array[pointer2.key] = pointer2;
	}

	private void completeCheck(Node checkpoint) {
		if (checkpoint == null) {}
		else if (checkpoint.left == null) {
			checkpoint.is_complete = true;
		}
		else if (checkpoint.right == null) {
			checkpoint.is_complete = false;
		}
		else {
			if (checkpoint.left.is_complete == true && checkpoint.right.is_complete == true) {
				if (checkpoint.left.height == checkpoint.right.height) {
					checkpoint.is_complete = true;
				}
				else {
					checkpoint.is_complete = false;
				}
			}
			else {
				checkpoint.is_complete = false;
			}
		}
	}

	private void nodeHeight(Node checkpoint) {
		if (checkpoint == null) {}
		else if (checkpoint.left == null) {
			checkpoint.height = 1;
		}
		else if (checkpoint.right == null) {
			checkpoint.height = 2;
		}
		else {
			checkpoint.height = max(checkpoint.left.height, checkpoint.right.height)+1;
		}
	}

	private int max(int a, int b) {
		if (a > b) {
			return a;
		}
		else {
			return b;
		}
	}
}