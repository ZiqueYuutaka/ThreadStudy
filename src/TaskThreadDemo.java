/**********************************************************************
Demonstration of how threads work.  The completion of each task is
unpredictable, but it seems that the first two threads to start have
a higher priority than the third.
***********************************************************************/
import java.util.concurrent.*;
public class TaskThreadDemo{
	/************TEST ONE***************************
	//Demonstration of concurrent programming
	public static void main(String[] args){
		//Create tasks
		Runnable printA = new PrintChar('a', 100);
		Runnable printB = new PrintChar('b', 100);
		Runnable print100= new PrintNum(100);

		//Create threads
		Thread thread1 = new Thread(printA);
		Thread thread2 = new Thread(printB);
		Thread thread3 = new Thread(print100);

		//Start threads
		thread1.start();
		thread2.start();
		thread3.start();
	}
	************************************************/

	//Demonstration of thread management through a thread pool
	//Order of which thread executes first matters.
	public static void main(String[] args){
		//Control how many threads can run concurrently
		//ExecutorService executor = Executors.newFixedThreadPool(1);

		//Cache each waiting thread and launch all of them
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new PrintChar('a', 1000));

		//If using the newFixedThreadPool(), threads started
		//from inside other threads are not affected.
		Runnable print100 = new PrintNum(1000);
		Thread thread3 = new Thread(print100);
		thread3.setPriority(Thread.MAX_PRIORITY);
		executor.execute(thread3);

		executor.execute(new PrintChar('b', 1000));

		executor.shutdown();
	}
}//End TaskThreadDemo

class PrintChar implements Runnable{
	private char charToPrint;	//The character to print
	private int times;	//The number of times to repeat

	/**Construct a task with a specified character and number of
	 *  times to print the character
	 */
	public PrintChar(char c, int t){
		charToPrint = c;
		times = t;
	}

	@Override //Must override the run() if implemeting Runnable
	public void run(){
		for(int i = 0; i < times; i++){
			System.out.print(charToPrint);
		}
	}
}//End PrintChar

class PrintNum implements Runnable{
	private int lastNum;

	/**Construct a task for printing numbers*/
	public PrintNum(int n){
		lastNum = n;
	}

	@Override
	public void run(){
		//Can call to execute another thread
		//in an already existing thread.
		Thread thread4 = new Thread(
			new PrintChar('c',200)); //not affected by fixedThreadPool()
		thread4.start();
		try{
			for(int i = 0; i < lastNum; i++){
				System.out.print(" " + i);
				if(i == 5) thread4.join(); //Wait for thread4 to finish job
			}	
		}
		catch(InterruptedException ex){
		}
		
	}
}