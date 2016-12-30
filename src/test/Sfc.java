package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

public class Sfc implements Runnable {
	public int id;
	public int type;
	public sfcState initState;
	public sfcState runState;
	public sfcState leftState;
	public sfcState State;
	public ArrayList<Vnf> VnfList;
	public Queue<Vnf> VnfQueue;
	public int cost;
	public int ResponseTime;
	public int runTime;
	public int packetNum;
	public int packetLen;

	// public ReentrantLock lock;

	public Sfc(int id, int packetNum, int packetLen, Queue<Vnf> VnfQueue,
			ArrayList<Vnf> VnfList, int type, int cost, int runTime) {
		this.id = id;
		this.packetNum = packetNum;
		this.packetLen = packetLen;
		this.VnfList = VnfList;
		this.VnfQueue = VnfQueue;
		this.initState = new sfcInitState(this);
		this.runState = new sfcRunState(this);
		this.leftState = new sfcLeftState(this);
		this.State = this.initState;
		this.type = type;
		this.cost = cost;
		this.runTime = runTime;
		// this.lock = new ReentrantLock();
	}

	public void run() {
		for (Vnf f : this.VnfQueue) {
			f.lock.lock();
			f.SfcWaitList.add(this);
			f.lock.unlock();
		}
		while (!this.VnfQueue.isEmpty()) {
			Vnf f = this.VnfQueue.element();
			f.lock.lock();
			if (!f.SfcList.contains(this)) {
				if (!f.SfcReadyList.contains(this)) {
					f.SfcReadyList.add(this);
					System.out
							.println("add sfc" + this.id + " into VNF" + f.id);

				} // else
					// System.out.println("sfc" + this.sfc.type
					// + " is already in the VNF" + f.type);
			}
			f.lock.unlock();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// lock.unlock();
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println("sfc" + this.id + " end");

	}

	public void run1() {
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
		System.out.println("sfc" + this.id + " end");
	}

	public void setState(sfcState s) {
		this.State = s;
	}

}
