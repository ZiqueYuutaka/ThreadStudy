public class MergeSort{
	/** The method for sorting the numbers*/
	public static void mergeSort(int[] list){
		if(list.length > 1){
			//Merge sort the first half
			int[] firstHalf = new int[list.length /2];
			System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
			mergeSort(firstHalf);

			//Merge second half
			int secondHalfLen = list.length - list.length /2;
			int[] secondHalf = new int[secondHalfLen];
			System.arraycopy(list, list.length / 2,
				secondHalf, 0, secondHalfLen);
			mergeSort(secondHalf);

			//Merge firstHalf and secondHalf
			merge(firstHalf, secondHalf, list);
		}
	}

	/** Merge two sorted lists in ascending order*/
	public static void merge(int[] list1, int[] list2, int[] temp){
		int current1 = 0;	//list1 index
		int current2 = 0;	//list2 index
		int current3 = 0;	//temp index

		while(current1< list1.length && current2 < list2.length){
			if(list1[current1] < list2[current2]){
				temp[current3++] = list1[current1++];
			}
			else
				temp[current3++] = list2[current2++];
		}

		while(current1<list1.length)//There are still elements in list1
			temp[current3++] = list1[current1++];

		while(current2 < list2.length) //There are still elements in list2
			temp[current3++] = list2[current2++];
	}
}