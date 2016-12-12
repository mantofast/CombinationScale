package test;

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

		int flag = 0;
		while (!this.sfc.VnfQueue.isEmpty()) {
			Vnf f = this.sfc.VnfQueue.element();
			f.lock.lock();
			if (!f.SfcList.contains(this.sfc)) {
				flag = 1;
				if (!f.SfcWaitList.contains(this.sfc)) {
					f.SfcWaitList.add(this.sfc);
					System.out.println("add sfc" + this.sfc.type + " into VNF"
							+ f.type);

				} // else
				// System.out.println("sfc" + this.sfc.type
				// + " is already in the VNF" + f.type);
			} else
				this.sfc.VnfQueue.remove();
			f.lock.unlock();
		}
		if (flag == 0)
			this.sfc.setState(this.sfc.runState);
		// lock.unlock();

	}

}
