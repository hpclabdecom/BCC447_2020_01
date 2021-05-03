package parallel.model;

import java.util.List;
import java.util.Map;

public class PhaseOnePOJO {
	
	private int level;
	
	private List<String> tuple;
	
	private Map<String, Node> trees;
	
	public PhaseOnePOJO(){
		level = -1;
	}
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<String> getTuple() {
		return tuple;
	}

	public void setTuple(List<String> tuple) {
		this.tuple = tuple;
	}

	public Map<String, Node> getTrees() {
		return trees;
	}

	public void setTrees(Map<String, Node> trees) {
		this.trees = trees;
	}

}
