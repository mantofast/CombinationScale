package test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
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
	public Condition con;

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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// while (true) {
		int flagP = 0; // ��ͨ���ݱ�־
		int flagL = 0;// �������ݱ�־����=2ʱ��Ҫ
		while (true) {
			this.lock.lock();
			while (this.state != this.norState)
				try {
					con.await();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			// step1:����vnfllist������û��sfc�Ǵ���left״̬�ģ��ͷ���Դ
			// System.out.println("vnf info: " + this.type + " n: " + n);

			for (Sfc s : this.SfcList) {
				if (s.State == s.leftState) {
					// System.out.println("vnf"+this.type+" cost:"+this.cosumption);
					this.cosumption -= s.cost;
					this.SfcList.remove(s);
					System.out.println("vnf" + this.type + " cost:"
							+ this.cosumption);
					System.out.println("delete sfc" + s.type + "from vnf"
							+ this.type);

				}
			}
			// lock.unlock();
			// step2:����waitlist,���ܷ�������е�һ����
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

			// step3:�ж��Ƿ���Ҫ��ͨ����

			// if (flagP >= 2) {
			// System.out.println("vnf" + this.type + " normal scale up");
			// this.state.scaleUp(10);
			// flagP = 0;
			// } else if (this.cosumption >= this.totalCpu * 0.8)
			// flagP++;
			//
			// // step4: �ж��Ƿ���Ҫ��������
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
			// STEP3:�ж��Ƿ���Ҫ���ݣ��Լ����ݵ��߼���
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
				// ÿ��һ��ʱ��check״̬
				Thread.sleep(10);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch blockrun()
				e.printStackTrace();
			}
		}

	}
	// }
}
