package utils;

// Represents a pair (like in C++)
public class Pair<K, V> {
	
	private K first;
	private V second;
	
	public Pair() {
		this.first = null;
		this.second = null;
	}
	
	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(K first) {
		this.first = first;
	}
	
	public K getFirst() {
		return first;
	}
	
	public void setSecond(V second) {
		this.second = second;
	}
	
	public V getSecond() {
		return second;
	}
}
