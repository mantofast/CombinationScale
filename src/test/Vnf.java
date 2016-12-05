package test;

import java.util.concurrent.CopyOnWriteArrayList;

public class Vnf implements Runnable {
	public int type;
	public vnfState state;
	public vnfState norState;
	public vnfState upState;
	public vnfState downState;

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
	public int MaxCpu;
	public int MinCpu;

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

	}

	public int accept(Sfc sfc) {
		if (sfc.cost <= MaxCpu - cosumption) {
			if (sfc.cost <= totalCpu - cosumption) {
				if (this.SfcList == null)
					this.SfcList = new CopyOnWriteArrayList<Sfc>();

				this.SfcList.add(sfc);
				sfc.State.run();
				return 1;
			} else {
				if (this.SfcWaitList == null)
					this.SfcWaitList = new CopyOnWriteArrayList<Sfc>();
				this.SfcWaitList.add(sfc);
				return 1;
			}
		} else
			return -1;
	}

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
			if (this.state == this.norState) {
				// step1:����vnfllist������û��sfc�Ǵ���left״̬�ģ��ͷ���Դ
				// System.out.println("vnf info: " + this.type + " n: " + n);

				for (Sfc s : this.SfcList) {
					if (s.State == s.leftState)
						this.cosumption -= s.cost;
					this.SfcList.remove(s);
				}
				// step2:����waitlist,���ܷ�������е�һ����
				for (Sfc s : this.SfcWaitList) {
					if (s.cost <= this.totalCpu - this.cosumption) {
						this.cosumption += s.cost;
						this.SfcList.add(s);
						this.SfcWaitList.remove(s);
						s.setState(s.runState);

					}

				}

				// step3: �ж��Ƿ���Ҫ��������
				if (flagL >= 3) {
					this.state.scaleUp(Math.max(flagL * 3, 10));
					flagL = 0;
				} else
					for (Sfc s : this.SfcList) {
						for (Vnf f : s.VnfList) {
							if (f.cosumption >= f.totalCpu * 0.65)
								flagL++;
						}
					}

				// step4:�ж��Ƿ���Ҫ��ͨ����
				if (flagP >= 2) {
					this.state.scaleUp(10);
					flagP = 0;
				} else if (this.cosumption >= this.totalCpu * 0.75)
					flagP++;

				try {
					// ÿ��һ��ʱ��check״̬
					Thread.sleep(10);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// }
	}

}
