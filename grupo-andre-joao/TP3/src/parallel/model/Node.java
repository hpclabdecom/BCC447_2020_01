package parallel.model;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


public class Node{
	
	private int count;
	private Map<String,Node> descendants;
	private String label;
	private boolean aggregated;
			
	public Node(){
		count =0;
		descendants = new ConcurrentHashMap<String,Node>();
		aggregated = false;
	}
	
	public void setAggregated(){
		this.aggregated=true;
	}
	
	public boolean isAggregated(){
		return this.aggregated;
	}
	
	public void setLabel(String label){
		this.label= label;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	
	public Node getNode(String label) {
		return descendants.get(label);
	}

	public int getCount() {
		return count;
	}

	public void incrementCount() {
		this.count++;
	}
	
	public void incrementCount(int value) {
		this.count += value;
	}
	
	
	public Node addNode(String label, Node n){
		return descendants.put(label, n);
	}
	
	
	public boolean containsNode(String label){
		return descendants.containsKey(label);
	}
	
	public Node removeNode(String label){
		return descendants.remove(label);
	}
	
	public Set<String> getDescendants(){
		SortedSet<String> sorted = new TreeSet<String>();
		sorted.addAll(descendants.keySet());
		return sorted;
	}

}
