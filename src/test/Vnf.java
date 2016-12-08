package test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Vnf implements Runnable {
	public int type;
	public vnfState state;
	public vnfState norState;
	public vnfState upState;
	public vnfState downState;
	// public ArrayList<Sfc> SfcList;
	// public ArrayList<Sfc> SfcWaitList;
	public CopyOnWriteArrayList<Sfc> SfcList;
	public CopyOnWriteArrayList<Sfc> SfcWaitList;
	// public int num;
	public int totalCpu;
	public int cosumption;
	public int coolDownPeriod;
	public int scaleUpInterval;
	public int scaleUpPeriod;
	public int scaleDownInterval;
	public int scaleDownPeriod;
	public int MaxCpu = 150;
	public int MinCpu;
	public ReentrantLock lock;

	public Vnf(int type, int totalCpu) {
		this.type = type;
		this.totalCpu = totalCpu;
		this.norState = new vnfNormalState(this);
		this.upState = new vnfScaleUpState(this);
		this.downState = new vnfScaleDownState(this);
		this.state = norState;
		// this.SfcList = new ArrayList<Sfc>();
		// this.SfcWaitList = new ArrayList<Sfc>();
		this.SfcList = new CopyOnWriteArrayList<Sfc>();
		this.SfcWaitList = new CopyOnWriteArrayList<Sfc>();
		this.lock = new ReentrantLock();
	}

	// public int accept(Sfc sfc) {
	// if (sfc.cost <= MaxCpu - cosumption) {
	// if (sfc.cost <= totalCpu - cosumption) {
	// if (this.SfcList == null)
	// this.SfcList = new CopyOnWriteArrayList<Sfc>();
	//
	// this.SfcList.add(sfc);
	// sfc.State.run();
	// return 1;
	// } else {
	// if (this.SfcWaitList == null)
	// this.SfcWaitList = new CopyOnWriteArrayList<Sfc>();
	// this.SfcWaitList.add(sfc);
	// return 1;
	// }
	// } else
	// return -1;
	// }

	public void setState(vnfState state) {
		this.state = state;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// while (true) {
		int flagP = 0; // 普通扩容标志
		int flagL = 0;// 联动扩容标志，》=2时需要
		while (true) {
			this.lock.lock();
			if (this.state == this.norState) {
				// step1:遍历vnfllist，看有没有sfc是处于left状态的，释放资源
				// System.out.println("vnf info: " + this.type + " n: " + n);

				for (Sfc s : this.SfcList) {
					if (s.State == s.leftState) {
						this.cosumption -= s.cost;
						this.SfcList.remove(s);
						System.out.println("delete sfc" + s.type + "from vnf"
								+ this.type);

					}
				}
				// lock.unlock();
				// step2:遍历waitlist,看能否接入其中的一部分
				// lock.lock();
				for (Sfc s : this.SfcWaitList) {
					if (s.cost <= this.totalCpu - this.cosumption) {
						this.cosumption += s.cost;
						this.SfcList.add(s);
						this.SfcWaitList.remove(s);
						System.out.println("vnf" + this.type + ": sfc" + s.type
								+ " access success");
						// s.setState(s.runState);
						// System.out.println("change state sfc:" + s.type);

					}

				}
				// lock.unlock();

				// step3: 判断是否需要联动扩容
				// lock.lock();
				// if (flagL >= 3) {
				// this.state.scaleUp(Math.min(flagL * 3, 10));
				// flagL = 0;
				// } else
				// for (Sfc s : this.SfcList) {
				// for (Vnf f : s.VnfList) {
				// if (f.cosumption >= f.totalCpu * 0.65)
				// flagL++;
				// }
				// }
				// lock.unlock();
				// step4:判断是否需要普通扩容
				// lock.lock();
				if (flagP >= 1) {
					this.state.scaleUp(10);
					flagP = 0;
				} else if (this.cosumption >= this.totalCpu * 0.75)
					flagP++;
				// lock.unlock();
				// try {
				// // 每隔一段时间check状态
				// Thread.sleep(10);
				//
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch blockrun()
				// e.printStackTrace();
				// }
			} else {
				// System.out.println("vnf" + this.type
				// + " is not int the normal state");
				// this.state.run();

			}
			try {
				// 每隔一段时间check状态
				Thread.sleep(10);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch blockrun()
				e.printStackTrace();
			}
			lock.unlock();
		}

		// }
	}
}
