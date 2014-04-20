package mavenProject;

import java.math.BigInteger;

public class ComputingThread implements Runnable {
	private int id;
	private BigInteger start;
	private BigInteger end;
	private BigInteger[] result;
	
	private BigInteger computeSum(){
		BigInteger localSum = new BigInteger("0");
		
		for(BigInteger i = start; i.compareTo(end) == -1; i = i.add(new BigInteger("1"))){
			localSum = localSum.add(i);
		}
		
		return localSum;
	}
	
	ComputingThread(BigInteger start, BigInteger end, BigInteger[] result, int id){
		this.start = start;
		this.end = end;
		this.result = result;
		this.id = id;
	}
	
	public void run() {
		System.out.println("[ Entering Thread " + this.id + " ]");
		this.result[this.id] = computeSum();
		System.out.println("[ Leaving Thread " + this.id + " ]");
	}

}
