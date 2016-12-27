package test;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Vnf implements Runnable {
	public int type;
	public int id;
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
	public Condition con;
	public ArrayList<Future<Integer>> results = new ArrayList<>();

	public Vnf(int id, int type, int totalCpu) {
		this.id = id;
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
		this.con = lock.newCondition();
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

	public int changePackNum(Sfc s) {
		double gama = ((this.type * s.type) / 3.0) * (s.packetLen / 60.0);

		return (int) gama * s.packetNum;
	}

	public int changePackLen(Sfc s) {
		double gama = (60.0 / s.packetLen);

		return (int) gama * s.packetLen;
	}

	public int computeCpuCost(Sfc s) {
		int cost = this.type * s.type * 12 * (int) (s.packetLen / 60.0);
		return cost;
	}

	public int computeDelay(Sfc s) {
		int delay = (int) (s.packetLen / 60.0) * 1000;
		return delay;
	}

	class Result {
		public int id;
		public int count;

		public Result(int id, int count) {
			this.id = id;
			this.count = count;
		}
	}

	class SfcHandler implements Callable<Integer> {
		public int id;
		public int InPackNum;
		public int r;// 因子
		public int delay;// 每个包的处理时间

		public SfcHandler(int id, int packetNum, int delay) {
			this.id = id;
			this.InPackNum = packetNum;
			this.delay = delay;
		}

		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			int count = 0;
			while (count < InPackNum) {
				Thread.sleep(this.delay);
				count++;
			}
			System.out.println("vnf" + type + "handle sfc" + this.id + "over");
			return this.id;
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// while (true) {
		int flagP = 0; // 普通扩容标志
		int flagL = 0;// 联动扩容标志，》=2时需要

		while (true) {
			this.lock.lock();
			while (this.state != this.norState)
				try {
					con.await();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			// step1:遍历vnfllist，看有没有sfc是处于left状态的，释放资源
			// System.out.println("vnf info: " + this.type + " n: " + n);

			for (Sfc s : this.SfcList) {
				// if (s.State == s.leftState) {
				for (Future<Integer> result : this.results) {
					if (result.isDone()) {
						try {
							if (result.get() == s.id) {

								this.cosumption -= computeCpuCost(s);
								// s.packetNum =
								s.VnfQueue.remove();
								s.packetLen = changePackLen(s);
								s.packetNum = changePackNum(s);
								this.SfcList.remove(s);
								System.out.println("vnf" + this.type + " cost:"
										+ this.cosumption);
								System.out.println("delete sfc" + s.type
										+ "from vnf" + this.type);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}
			// lock.unlock();
			// step2:遍历waitlist,看能否接入其中的一部分
			// lock.lock();
			for (Sfc s : this.SfcWaitList) {
				if (s.cost <= this.totalCpu - this.cosumption) {
					this.cosumption += computeCpuCost(s);
					this.SfcList.add(s);
					this.SfcWaitList.remove(s);

					// 增加处理线程

					SfcHandler h = new SfcHandler(s.id, s.packetNum,
							computeDelay(s));
					FutureTask<Integer> task = new FutureTask<>(h);
					this.results.add(task);
					Thread t = new Thread(task);
					t.start();
					System.out.println("vnf" + this.type + ": sfc" + s.type
							+ " access success");
					// s.setState(s.runState);
					// System.out.println("change state sfc:" + s.type);

				}

			}
			// lock.unlock();

			// step3:判断是否需要普通扩容

			// if (flagP >= 2) {
			// System.out.println("vnf" + this.type + " normal scale up");
			// this.state.scaleUp(10);
			// flagP = 0;
			// } else if (this.cosumption >= this.totalCpu * 0.8)
			// flagP++;
			//
			// // step4: 判断是否需要联动扩容
			//
			// if (flagL >= 3) {
			// System.out.println("vnf" + this.type + " combina scale up");
			// this.state.scaleUp(Math.min(flagL * 3, 10));
			// flagL = 0;
			// } else
			// for (Sfc s : this.SfcList) {
			// for (Vnf f : s.VnfList) {
			// if (f.cosumption >= f.totalCpu * 0.65)
			// flagL++;
			// }
			// }
			// STEP3:判断是否需要扩容，以及扩容的逻辑：
			if (this.cosumption >= this.totalCpu * 0.8)// H_L thredhold
			{
				if (flagP >= 2) {
					System.out.println("vnf" + this.type + " normal scale up");
					this.state.scaleUp(10);
					flagP = 0;
				} else
					flagP++;
			} else if (this.cosumption >= this.totalCpu * 0.6) {
				if (flagL != 0) {
					System.out.println("vnf" + this.type + " combina scale up");
					this.state.scaleUp(Math.min(flagL * 2, 7));
					flagL = 0;
				} else
					for (Sfc s : this.SfcList) {
						int flag = 0;
						for (Vnf f : s.VnfList) {
							if (f.type != this.type
									&& f.cosumption >= f.totalCpu * 0.8)
								flag = 1;
						}
						if (flag == 1)
							flagL++;
					}

			} else {
				// do nothing
				System.out.print("");
			}

			con.signalAll();
			lock.unlock();
			try {
				// 每隔一段时间check状态
				Thread.sleep(10);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch blockrun()
				e.printStackTrace();
			}
		}

	}
	// }
}
