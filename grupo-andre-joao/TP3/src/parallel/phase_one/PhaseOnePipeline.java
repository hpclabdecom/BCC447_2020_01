package parallel.phase_one;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import parallel.model.Node;
import parallel.model.PhaseOnePOJO;

public class PhaseOnePipeline {
	
	private String[] itemset;
	private Map<String, ResourceList<PhaseOnePOJO>> resources;
	private Map<String, InsertTuplePipe> insertThreads;
	private ExecutorService sortingThreads;
	
	public PhaseOnePipeline(String[] itemset){
		this.itemset = itemset;
		resources = new TreeMap<String, ResourceList<PhaseOnePOJO>>();
		insertThreads = new TreeMap<String, InsertTuplePipe>();
		
	}
	
	public void beginPipeline(){
		for(String aux:itemset){
			ResourceList<PhaseOnePOJO> re = new ResourceList<PhaseOnePOJO>();
			re.setLabel(aux);
			resources.put(aux, re);		
		}
		
		sortingThreads = Executors.newCachedThreadPool();
		
		for(int i=0; i<itemset.length; i++){
			InsertTuplePipe insertPipe = null;
			if(i+1<itemset.length)
				insertPipe= new InsertTuplePipe(resources.get(itemset[i]), resources.get(itemset[i+1]));
			else insertPipe= new InsertTuplePipe(resources.get(itemset[i]), null);
			
			insertPipe.start();
			insertThreads.put(itemset[i], insertPipe);
		}
		
		
	}
	
	public void insertIntoPipeline(Map<String, Node> trees, String[] tuple){
		SortingPipe sort = new SortingPipe(resources.get(itemset[0]), trees, tuple);
		sortingThreads.submit(sort);
	}
	
	public void finishPipeline(String[]itemset){
		try{
			sortingThreads.shutdown();
			sortingThreads.awaitTermination(1, TimeUnit.HOURS);
			
			for(String label:itemset){
				resources.get(label).setFinished();
				
				insertThreads.get(label).join();
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

}
