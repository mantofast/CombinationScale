package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

public class Sfc implements Runnable {
	public int type;
	public sfcState initState;
	public sfcState runState;
	public sfcState leftState;
	public sfcState State;
	public ArrayList<Vnf> VnfList;
	public Queue<Vnf> VnfQueue;
	public int cost;
	public int ResponseTime;

	// public ReentrantLock lock;

	public Sfc(Queue<Vnf> VnfQueue, ArrayList<Vnf> VnfList, int type, int cost) {
		this.VnfList = VnfList;
		this.VnfQueue = VnfQueue;
		this.initState = new sfcInitState(this);
		this.runState = new sfcRunState(this);
		this.leftState = new sfcLeftState(this);
		this.State = this.initState;
		this.type = type;
		this.cost = cost;
		// this.lock = new ReentrantLock();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// int n = 0;

		while (this.State != this.leftState) {

			// n++;
			// System.out.println("sfc info: " + this.type);
			// System.out.println(this.State == this.initState);
			this.State.run();
			try {
				Thread.sleep(15);// 每隔一段时间check一下状态
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println("sfc" + this.type + " end");
	}

	public void setState(sfcState s) {
		this.State = s;
	}

}
