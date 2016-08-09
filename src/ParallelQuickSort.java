

public class ParallelQuickSort{
	public static void quickSort(int[] list){
		quickSort(list, 0, list.length-1);
	}

	public static void quickSort(int[] list, int first, int last){
		if(last > first){
			int pivotIndex = partition(list, first, last);
			quickSort(list, first, pivotIndex -1);	//This is a candidate for a task
			quickSort(list, pivotIndex+1, last);
		}
	}//End quickSort with three arguments

	public static int partition(int[] list, int first, int last){
		int pivot = list[first]; //Choose the first element as the pivot
		int low = first + 1;	//Index for forward search
		int high = last;	//Index for backward search

		while(high > low){
			//Search forward from left
			while(low <= high && list[low] <= pivot)
				low++;

			//Search backward from right
			while(high >= low && list[high] > pivot)
				high--;

			//Swap two elements in the list
			if(high > low){
				int temp = list[high];
				list[high] = list[low];
				list[low] = temp;
			}
		}// End while

		list[first] = list[high];
		list[high] = pivot;
		return high;

		//The following commented code is not needed
		//Search for the first number in list
		//that is less than the pivot
		/*while(high > first && list[high] >= pivot)
			high--;*/

		//Return the position of the pivot
		//  If the pivot is greater than the value at list[high],
		//	swap the positions of the pivot and the value at list[high]
		/*if(pivot > list[high]){
			list[first] = list[high];
			list[high] = pivot;
			return high;
		}
		else{
			return first;
		}*/
	}

	//QuickSort task
	private static class QuickSortTask extends RecursiveAction{

		private final int THRESHOLD = 500;
		

		//Constuctor


		@Override
		protected voide computer(){

		}
	}

	//Test method
	public static void main(String[] args){
		//int[] list = {2, 3, 2, 5, 6, 1, -2, 3, 14, 12};
		//int[] list = {2, 22, 55, 100, 99, 4, 5, 32, 0, 1};
		int[] list = new int[50];
		for(int i = 0; i < 50; i++){
			java.util.Random rand = new java.util.Random();
			list[i] = rand.nextInt(50) + 1;
		}
		quickSort(list);
		for(int i = 0; i < list.length; i++)
			System.out.print(list[i] + " ");
	}
}