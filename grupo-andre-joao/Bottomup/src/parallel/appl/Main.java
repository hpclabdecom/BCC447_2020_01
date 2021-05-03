package parallel.appl;

import java.util.Map;
import java.util.TreeMap;

import parallel.model.Node;
import parallel.phase_one.PhaseOnePipeline;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}
	
	public Main(){
		
		String[][] tuples = {{"T","E","C","G","A"},
							 {"C","D","T","F"},
							 {"T","G"},
							 {"C","T","G","E"},
							 {"E","C","G","A","D"},
							 {"T","G","A"},
							 {"F","G","E","A","C","T"},
							 {"F","D"},
							 {"T","G","C","A"},
							 {"A","E","D"}};
		
		String[] itemset = {"A", "C", "D", "E", "F", "G", "T"};
		
		Map<String, Node> trees = new TreeMap<String, Node>();
		for(String label:itemset){
			Node root = new Node();
			trees.put(label, root);
		}
		
		PhaseOnePipeline phaseOnePipeline = new PhaseOnePipeline(itemset);
		phaseOnePipeline.beginPipeline();
		
		for(String[] tuple: tuples)
			phaseOnePipeline.insertIntoPipeline(trees, tuple);
		
		phaseOnePipeline.finishPipeline(itemset);
		
		System.out.println("Phase one finished");
		
		
		for(String label:trees.keySet()){
			System.out.println("Tree label: " + label);
			
			traverse("root", trees.get(label));
			
			System.out.println();
		}
		
		//phase 2
		
	}
	
	private void traverse(String label, Node n){
		System.out.println(label + " count: " + n.getCount());
		for(String descendat: n.getDescendants()){
			traverse(descendat, n.getNode(descendat));
		}
		if(n.getDescendants().isEmpty()) System.out.println("reached a leaf");
	}

}
