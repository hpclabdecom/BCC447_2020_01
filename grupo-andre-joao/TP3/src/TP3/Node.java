package TP3;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Node {

	private int count;
	private final Map<String, Node> descendants;
	private String label;
	private boolean aggregated;

	@Override
	public int hashCode() {
		return Objects.hash(descendants, label);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		final Node other = (Node) obj;
		return Objects.equals(descendants, other.descendants) && Objects.equals(label, other.label);
	}

	public Node() {
		count = 0;
		descendants = new ConcurrentHashMap<>();
		aggregated = false;
	}

	public void setAggregated() {
		this.aggregated = true;
	}

	public boolean isAggregated() {
		return this.aggregated;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public Node getNode(final String label) {
		return descendants.get(label);
	}

	public int getCount() {
		return count;
	}

	public void incrementCount() {
		this.count++;
	}

	public void incrementCount(final int value) {
		this.count += value;
	}

	public Node addNode(final String label, final Node n) {
		return descendants.put(label, n);
	}

	public boolean containsNode(final String label) {
		return descendants.containsKey(label);
	}

	public Node removeNode(final String label) {
		return descendants.remove(label);
	}

	public Set<String> getDescendants() {
		final SortedSet<String> sorted = new TreeSet<>();
		sorted.addAll(descendants.keySet());
		return sorted;
	}

	@Override
	public String toString() {
		return "Pai - " + label + " Descendentes - " + this.getDescendants();
	}

}
