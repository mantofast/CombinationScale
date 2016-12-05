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
		for (Vnf f : this.sfc.VnfList) {
			if (!f.SfcWaitList.contains(this.sfc)
					&& !f.SfcList.contains(this.sfc)) {
				f.SfcWaitList.add(this.sfc);
				System.out.println("add sfc into sfcWaitList");
			} else
				System.out.println("this sfc is already in the list");
		}
		lock.unlock();
		// this.sfc.setState(this.sfc.runState);
	}

}
