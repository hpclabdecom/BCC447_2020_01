package parallel.phase_one;

import java.util.List;

import parallel.model.Node;
import parallel.model.PhaseOnePOJO;

public class InsertTuplePipe extends Thread{
	
	private ResourceList<PhaseOnePOJO> resourceC, resourceP;
	
	public InsertTuplePipe(ResourceList<PhaseOnePOJO> resourceC, ResourceList<PhaseOnePOJO> resourceP) {
		this.resourceC = resourceC;
		this.resourceP = resourceP;
	}

	@Override
	public void run() {
		try {
            PhaseOnePOJO pojo;
            while ((!resourceC.isFinished()) ||
                    (resourceC.getNumOfRegisters() != 0)) {
                if ((pojo = resourceC.getRegister()) != null) {
                    doSomething(pojo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	private void doSomething(PhaseOnePOJO pojo){
		
		
		if(resourceC.getLabel().equals(pojo.getTuple().get(pojo.getLevel()))){
			
			List<String> tuple = pojo.getTuple();
			int level = pojo.getLevel();
			Node root = pojo.getTrees().get(tuple.get(level));
			insert(root, tuple.subList(level, tuple.size()));
					
			level++;
			
			if(level<tuple.size() && resourceP!=null){
				pojo.setLevel(level);
				resourceP.putRegister(pojo);			
			}
		}else if(resourceP!=null) resourceP.putRegister(pojo);
		
	}
	
	
	private void insert(Node n, List<String> tuple){
		n.incrementCount();
		for(String label:tuple){
			if(n.containsNode(label)){
				Node exist = n.getNode(label);
				exist.incrementCount();
				n = exist;
			}else{
				Node notExist = new Node();
				notExist.setLabel(label);
				notExist.incrementCount();
				n.addNode(label, notExist);
				n = notExist;
			}
		}
	}

}
