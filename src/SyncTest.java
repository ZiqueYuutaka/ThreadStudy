import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class SyncTest{
	private static Account account = new Account();

	public static void main(String[] args){
		ExecutorService executor = Executors.newCachedThreadPool();

		//Create and launch 100 threads
		for(int i = 0; i < 100; i++){
			executor.execute(new AddAPennyTask());
		}

		executor.shutdown();

		//Wait until all tasks are finished
		while(!executor.isTerminated()){}

		System.out.println("What is balance? " + account.getBalance());
	}

	private static class AddAPennyTask implements Runnable{
		public void run(){
			account.deposit(1);
			//Another way to address race condition
			//Allows ANY object's method to become thread-safe
			/*synchronized(account){
				account.deposit(1);
			}*/
		}
	}

	//Use of explicit lock
	private static class Account{
		private int balance = 0;
		private static Lock lock = new ReentrantLock();//lock shared by all account objects

		public int getBalance(){
			return balance;
		}
		
		public void deposit(int amount){
			//Pass lock to method accessed by multiple threads
			lock.lock();

			//Delay is deliberate to magnify data-corruption
			try{
				
				int newBalance = balance + amount;

				Thread.sleep(5);

				balance = newBalance;
			}
			catch(InterruptedException ex){}
			finally{	//Always release the lock
				lock.unlock();
			}
		}
	}

	/******************************************************
	//Use of implicit lock
	private static class Account{
		private int balance = 0;

		public int getBalance(){
			return balance;
		}

		/******************************************************
		//One way to address race condition
		//Implicit lock passed
		public synchronized void deposit(int amount){
			int newBalance = balance + amount;

			//Delay is deliberate to magnify data-corruption
			try{
				Thread.sleep(5);
			}
			catch(InterruptedException ex){}

			balance = newBalance;
		}
		*********************************************************
		public void deposit(int amount){
			int newBalance = balance + amount;

			//Delay is deliberate to magnify data-corruption
			try{
				Thread.sleep(5);
			}
			catch(InterruptedException ex){}

			balance = newBalance;
		}
	}
	*********************************************************/
}