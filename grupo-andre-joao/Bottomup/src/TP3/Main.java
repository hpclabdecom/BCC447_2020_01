package TP3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

//import javafx.util.Pair;

public class Main {
	static final Integer minSuport = 4;

	public Main() {
		final Node root = new Node();
		root.setLabel("root");
		phaseOne(root);

		// phaseTwo(null, "root", root, 2);

		final ArrayList<ConcurrentLinkedQueue<Pair<Node, String>>> matrix = PhaseOneAndAHalf(root);

		printMatrix(matrix);

		newPhaseTwo(matrix);

		System.out.println("end phase two");

		traverse("root", root);
	}

	private void printMatrix(final ArrayList<ConcurrentLinkedQueue<Pair<Node, String>>> matrix) {
		Integer i = 0;
		for (final ConcurrentLinkedQueue<Pair<Node, String>> concurrentLinkedQueue : matrix) {
			System.out.println("Nivel: " + i);

			concurrentLinkedQueue.forEach(par -> {
				System.out.println("pai: " + par.getKey().getLabel() + " filho: " + par.getValue());
			});

			i += 1;
		}
	}

	/*
	 * private void phaseOne(Node root){
	 *
	 * String[] tuple1 = {"A","C","E","G","T"}; String[] tuple2 = {"C","D","F","T"};
	 * String[] tuple3 = {"G","T"}; String[] tuple4 = {"C","E","G","T"}; String[]
	 * tuple5 = {"A","C","D","E","G"}; String[] tuple6 = {"A","G","T"}; String[]
	 * tuple7 = {"A","C","E","F","G","T"}; String[] tuple8 = {"D","F"}; String[]
	 * tuple9 = {"A","C","G","T"}; String[] tuple10 = {"A","D","E"};
	 *
	 * Node current = root; insert(current, tuple1); current = root; insert(current,
	 * tuple2); current = root; insert(current, tuple3); current = root;
	 * insert(current, tuple4); current = root; insert(current, tuple5); current =
	 * root; insert(current, tuple6); current = root; insert(current, tuple7);
	 * current = root; insert(current, tuple8); current = root; insert(current,
	 * tuple9); current = root; insert(current, tuple10); current = root; }
	 */

	private void phaseOne(final Node root) {

//		final String[] tuple1 = { "A", "C", "E", "G", "T" };
//		final String[] tuple2 = { "A", "C", "E", "T" };
//		final String[] tuple3 = { "A", "C", "G" };

		String[][] lst = {
				
			{"A","C","E","G","T"}, 
			{"C","D","F","T"},
			{"G","T"}, 
			{"C","E","G","T"}, 
			{"A","C","D","E","G"}, 
			{"A","G","T"}, 
			{"A","C","E","F","G","T"}, 
			{"D","F"}, 
			{"A","C","G","T"}, 
			{"A","D","E"},
		};
		
		Node current = root;
		
		for(String[] tuple : lst) {
			insert(current, tuple);
		}
	}

	private void insert(Node n, final String[] tuple) {
		n.incrementCount();
		for (final String label : tuple) {
			if (n.containsNode(label)) {
				final Node exist = n.getNode(label);
				exist.incrementCount();
				n = exist;
			} else {
				final Node notExist = new Node();
				notExist.setLabel(label);
				notExist.incrementCount();
				n.addNode(label, notExist);
				n = notExist;
			}
		}
	}

	private void traverse(final String label, final Node n) {
		System.out.println(label + " count: " + n.getCount());
		for (final String descendat : n.getDescendants()) {
			traverse(descendat, n.getNode(descendat));
		}
		if (n.getDescendants().isEmpty())
			System.out.println("reached a leaf");
	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

	private void phaseTwo(final Node ancestor, final String descendantLabel, final Node descendant,
			final int minSupport) {
		final Iterator<String> it = descendant.getDescendants().iterator();
		while (it.hasNext()) {
			final String secondDescendantLevelLabel = it.next();
			phaseTwo(descendant, secondDescendantLevelLabel, descendant.getNode(secondDescendantLevelLabel),
					minSupport);
		}

		if (!descendant.isAggregated())
			if (ancestor != null)
				copyNodes(ancestor, descendant);
		// sync
		prune(descendant, minSupport);
		// sync

	}

	private void prune(final Node n, final int minSupport) {
		n.getDescendants().parallelStream().forEach(node -> {
			if (n.getNode(node).getCount() < minSupport)
				n.removeNode(node);
		});

//		Iterator<String> it = n.getDescendants().iterator();
//		while (it.hasNext()){
//			String descendantLabel = it.next();
//			if(n.getNode(descendantLabel).getCount()<minSupport)
//				n.removeNode(descendantLabel);
//		}
	}

	private void copyNodes(final Node ancestor, final Node descendant) {
		final Iterator<String> it = descendant.getDescendants().iterator();
		while (it.hasNext()) {
			final String secondDescendantLevelLabel = it.next();

			if (ancestor.containsNode(secondDescendantLevelLabel)) {
				ancestor.getNode(secondDescendantLevelLabel)
						.incrementCount(descendant.getNode(secondDescendantLevelLabel).getCount());
				ancestor.getNode(secondDescendantLevelLabel).setAggregated();
			} else {
				final Node newNode = new Node();
				newNode.setLabel(secondDescendantLevelLabel);
				newNode.incrementCount(descendant.getNode(secondDescendantLevelLabel).getCount());
				newNode.setAggregated();
				ancestor.addNode(secondDescendantLevelLabel, newNode);
			}
			copyNodes(ancestor.getNode(secondDescendantLevelLabel), descendant.getNode(secondDescendantLevelLabel));
		}
	}

	private ArrayList<ConcurrentLinkedQueue<Pair<Node, String>>> PhaseOneAndAHalf(final Node root) {
		// Matrix [nivel da arvore][todos os n�s que podem ser executados em paralelo]
		// Matrix[n][m] (Node, Node) [(Node,Node)]
		// Matrix.append()
		final ArrayList<ConcurrentLinkedQueue<Pair<Node, String>>> Matrix = new ArrayList<>();
		final ConcurrentLinkedQueue<Pair<Node, String>> array = new ConcurrentLinkedQueue<>();
		array.add(new Pair<Node, String>(root, ""));
		Matrix.add(array);
		recursiveAdd(Matrix, root, 1);
		return Matrix;
	}

	private void recursiveAdd(final ArrayList<ConcurrentLinkedQueue<Pair<Node, String>>> matrix, final Node no,
			final Integer level) {

		ConcurrentLinkedQueue<Pair<Node, String>> array = null;
		try {
			array = matrix.get(level);
		} catch (final IndexOutOfBoundsException e) {
			array = new ConcurrentLinkedQueue<>();
			if (!no.getDescendants().isEmpty()) {
				matrix.add(array);
			}
		}

		for (final String descendent : no.getDescendants()) {
			array.add(new Pair<Node, String>(no, descendent));
			recursiveAdd(matrix, no.getNode(descendent), level + 1);
		}

	}

	private void newPhaseTwo(final ArrayList<ConcurrentLinkedQueue<Pair<Node, String>>> matrix) {
		final Integer firstIndex = matrix.size() - 1;

		for (int i = firstIndex; i > 0; i--) {
			System.out.println("Nivel " + i + " elementos: " + matrix.get(i));

			ConcurrentLinkedQueue<Pair<Node,String>> treeLevel = getValuesUnique(matrix.get(i));
			
			treeLevel.parallelStream()
					.forEach(pair -> copyNodes(pair.getKey(), pair.getKey().getNode(pair.getValue())));
																										

			System.out.println("agora prune:");

			matrix.get(i).parallelStream().map(pair -> pair.getKey()).collect(Collectors.toSet()).forEach(pai -> {
				System.out.println(pai);
				prune(pai, minSuport);
			});
		}
	}
	
	private <S,T> ConcurrentLinkedQueue<Pair<S,T>> getValuesUnique(List<Pair<S,T>> list)  {
		List<T> unique = new ArrayList<T>();
		ConcurrentLinkedQueue<Pair<S,T>> result = new ConcurrentLinkedQueue<Pair<S,T>>();
		
		Iterator<Pair<S,T>> it = list.iterator();
		while(it.hasNext()) {
			Pair<S,T> value = it.next();
			if(!unique.contains(value.getValue())) {
				unique.add(value.getValue());
				result.add(value);
			}
		}
		
		return result;
	}
	
	private <S,T> ConcurrentLinkedQueue<Pair<S,T>> getValuesUnique(ConcurrentLinkedQueue<Pair<S,T>> list)  {
		
		List<Pair<S,T>> arrlst = new ArrayList<Pair<S,T>>(list);
		
		return getValuesUnique(arrlst);
	}

//Pior caso
//	/\
//		\
//			\
//				\
//					\
//						\
}
