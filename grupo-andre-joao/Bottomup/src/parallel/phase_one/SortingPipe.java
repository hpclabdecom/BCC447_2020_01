package parallel.phase_one;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import parallel.model.Node;
import parallel.model.PhaseOnePOJO;

public class SortingPipe implements Runnable{
	
	private ResourceList<PhaseOnePOJO> resource;
	private String[] tuple;
	private Map<String, Node> trees;
	
	public SortingPipe(ResourceList<PhaseOnePOJO> resource, Map<String, Node> trees, String[] tuple) {
		this.resource = resource;
		this.tuple= tuple;
		this.trees = trees;
	}

	@Override
	public void run() {
		try {
            doSomething();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	
	private void doSomething(){
		List<String> aux = new LinkedList<String>();
		for(String s:tuple) aux.add(s);
		
		Collections.sort(aux);
		
		PhaseOnePOJO pojo = new PhaseOnePOJO();
		pojo.setTuple(aux);
		pojo.setLevel(0);
		pojo.setTrees(trees);
		
		resource.putRegister(pojo);
	}
	

}
