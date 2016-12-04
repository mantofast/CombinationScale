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
		for (Vnf f : this.sfc.VnfList)
			f.SfcWaitList.add(this.sfc);
		this.sfc.setState(this.sfc.runState);
	}

}
