package test;

import java.util.ArrayList;

public class Sfc implements Runnable {
	public int type;
	public sfcState initState;
	public sfcState runState;
	public sfcState leftState;
	public sfcState State;
	public ArrayList<Vnf> VnfList;
	public int cost;
	public int ResponseTime;

	public Sfc(ArrayList<Vnf> VnfList, int type, int cost) {
		this.VnfList = VnfList;
		this.initState = new sfcInitState(this);
		this.runState = new sfcInitState(this);
		this.leftState = new sfcInitState(this);
		this.State = this.initState;
		this.type = type;
		this.cost = cost;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// int n = 0;

		while (this.State != this.leftState) {

			// n++;
			// System.out.println("sfc info: " + this.type);
			this.State.run();
			try {
				Thread.sleep(5);// 每隔一段时间check一下状态
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setState(sfcState s) {
		this.State = s;
	}

}
