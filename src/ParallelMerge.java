//import java.util.concurrent.RecursiveTask; //For when a value is returned
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelMerge{
	public static void main(String[] args){
		final int SIZE = 7000000;
		int[] list1 = new int[SIZE];
		int[] list2 = new int[SIZE];

		for(int i = 0; i < list1.length; i++){
			list1[i] = list2[i] = (int)(Math.random() * 10000000);
		}

		long startTime = System.currentTimeMillis();
		parallelMergeSort(list1);	//INvoke parallel merge sort
		long endTime = System.currentTimeMillis();
		System.out.println("\nParallel time with " 
			+ Runtime.getRuntime().availableProcessors()+
			" processors is " + (endTime - startTime) + " milliseconds");

		startTime = System.currentTimeMillis();
		MergeSort.mergeSort(list2);	//MergeSort in Listing 23.5
		endTime = System.currentTimeMillis();
		System.out.println("\nSequential time is " +
			(endTime - startTime) + " milliseconds");
	}

	public static void parallelMergeSort(int[] list){
		RecursiveAction mainTask = new SortTask(list);
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(mainTask);
	}

	private static class SortTask extends RecursiveAction{
		private final int THRESHOLD = 500;
		private int[] list;

		SortTask(int[] list){
			this.list = list;
		}

		@Override
		protected void compute(){
			if(list.length < THRESHOLD) //If problem is small enough
				java.util.Arrays.sort(list);
			else{	//If the problem is substantially large
				
				//DIVIDE THE PROBELM INTO nonoverlapping PARTS//

				//Obtain the first half of array
				int[] firstHalf = new int[list.length /2];
				System.arraycopy(list, 0, firstHalf, 0, list.length / 2);

				//Obtain the second half of the array
				int secondHalfLen = list.length - list.length /2;
				int[] secondHalf = new int[secondHalfLen];
				System.arraycopy(list, list.length / 2,
					secondHalf, 0, secondHalfLen);

				//SOLVE SUBPROBLEMS CONCURRENTLY//

				//Recursively sort the two halves
				invokeAll(new SortTask(firstHalf), new SortTask(secondHalf));

				//COMBINE THE RESULTS OF THE SUBPROBLEMS//
				
				//Merge firstHalf with secondHalf 
				MergeSort.merge(firstHalf, secondHalf, list);
			}
		}
	}
}