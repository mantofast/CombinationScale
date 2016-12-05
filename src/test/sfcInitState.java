package test;

import java.util.concurrent.locks.ReentrantLock;

public class sfcInitState implements sfcState {

	public Sfc sfc;

	public sfcInitState(Sfc sfc) {
		this.sfc = sfc;
	}

	@Override
	public void come() {
		// TODO Auto-generated method stub
		System.out.println("state transform erro");
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
		this.sfc.setState(this.sfc.leftState);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ReentrantLock lock = this.sfc.lock;
		lock.lock();
		int flag = 0;
		for (Vnf f : this.sfc.VnfList) {
			if (!f.SfcList.contains(this.sfc)) {
				if (!f.SfcWaitList.contains(this.sfc)) {
					f.SfcWaitList.add(this.sfc);
					System.out.println("add sfc" + this.sfc.type + " into VNF"
							+ f.type);

				} else
					System.out.println("sfc" + this.sfc.type
							+ " is already in the VNF" + f.type);
			} else
				flag = 1;
		}
		if (flag == 0)
			this.sfc.setState(this.sfc.runState);
		lock.unlock();

	}

}
