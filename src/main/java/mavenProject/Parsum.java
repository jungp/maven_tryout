package mavenProject;

import java.math.BigInteger;

public class Parsum {

	private BigInteger parsum(BigInteger n){
		int cores = Runtime.getRuntime().availableProcessors();
		BigInteger chunkSize = n.divide(BigInteger.valueOf((long) cores));
		BigInteger rest = n.subtract( chunkSize.multiply(BigInteger.valueOf((long) cores)) );
		
		Thread[] threads = new Thread[cores];
		BigInteger[] results = new BigInteger[cores];
		
		BigInteger start = new BigInteger("0");
		BigInteger end = new BigInteger("0");
		
		for(int i = 0; i < cores; i++){
			end = end.add(chunkSize);
			
			if (i == cores - 1){ // last core might get additional lines from potentialRest
				threads[i] = new Thread(new ComputingThread(start, end.add(rest.add(new BigInteger("1"))), results, i)); // since end is exclusive +1 necessary
				threads[i].start();
			} else {
				threads[i] = new Thread(new ComputingThread(start, end, results, i));
				start = start.add(chunkSize);
				threads[i].start();
			}		
		}
		
		BigInteger overallSum = new BigInteger("0");
		for(int i = 0; i < cores; i++){
			try {
				threads[i].join();
				overallSum = overallSum.add(results[i]);
				System.out.println("Thread #" + i + " result: " + results[i]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return overallSum;
	}
	
	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		
		Parsum calculation = new Parsum();
		System.out.println(calculation.parsum(new BigInteger("10000000")));
		
		long time = System.currentTimeMillis() - start;
		System.out.println("Running time: " + time + "ms");

	}

}
