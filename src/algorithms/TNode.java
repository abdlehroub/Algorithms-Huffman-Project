package algorithms;

public class TNode implements Comparable<TNode>{
	private TNode left;
	private TNode right;
	private char character;
	private int freq;

	public TNode(char character, int freq) {
		this.character = character;
		this.freq = freq;
		this.left = null;
		this.right = null;
	}
	public TNode(char character, int freq, TNode left, TNode right) {
		this.character = character;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}
	
	public TNode (char character) {
		this.character = character;
	}
	
	public TNode(TNode left , TNode right) {
		this.left = left;
		this.right = right;
	}


	public TNode getLeft() {
		return left;
	}

	public void setLeft(TNode left) {
		this.left = left;
	}

	public TNode getRight() {
		return right;
	}

	public void setRight(TNode right) {
		this.right = right;
	}

	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	@Override
	public int compareTo(TNode o) {
		// TODO Auto-generated method stub
		return this.freq - o.freq;
	}

}
