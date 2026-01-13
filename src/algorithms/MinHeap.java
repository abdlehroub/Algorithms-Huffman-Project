package algorithms;

import java.util.Arrays;

public class MinHeap<T extends Comparable<T>> {
	private T[] heap;
	private int size;

	
	@SuppressWarnings("unchecked")
	public MinHeap(int capacity) {
		heap = (T[]) new Comparable[capacity + 1]; // 1-based index
		size = 0;
	}

	// Build heap from array
	public void heapify(T[] array) {
		size = array.length;
		heap = (T[]) new Comparable[size + 1]; // 1-based
		System.arraycopy(array, 0, heap, 1, size);

		for (int i = size / 2; i >= 1; i--) {
			sink(i);
		}
	}

	// Insert element into heap
	public void insert(T val) {
		if (size + 1 == heap.length)
			resize(2 * heap.length);
		heap[++size] = val;
		swim(size);
	}

	private void swim(int k) {
		while (k > 1 && greater(k / 2, k)) {
			swap(k, k / 2);
			k = k / 2;
		}
	}

	// Sink down
	private void sink(int k) {
		while (2 * k <= size) {
			int j = 2 * k; // left child
			if (j < size && greater(j, j + 1))
				j++; // right child is larger
			if (!greater(k, j))
				break;
			swap(k, j);
			k = j;
		}
	}

	private void swap(int i, int j) {
		T tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	private boolean greater(int i, int j) {
		return heap[i].compareTo(heap[j]) > 0;
	}

	// Extract min
	public T extractMin() {
		if (size == 0)
			throw new RuntimeException("Heap is empty");
		T min = heap[1];
		swap(1, size--);
		sink(1);
		heap[size + 1] = null;
		return min;
	}

	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		heap = Arrays.copyOf(heap, newSize);
	}

	// Print the heap
	public void printHeap() {
		for (int i = 1; i <= size; i++) {
			System.out.print(heap[i] + " ");
		}
		System.out.println();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	

}
