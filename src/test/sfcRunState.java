package test;

import java.util.Date;

public class sfcRunState implements sfcState {

	public Sfc sfc;

	public sfcRunState(Sfc sfc) {
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
		// do something
		int i = 0;
		System.out.println("start running demand logical" + this.sfc.type);
		System.out.println(new Date(System.currentTimeMillis()));
		for (i = 0; i < 2; i++) {
			System.out.println("running demand logical" + this.sfc.type);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.sfc.setState(this.sfc.leftState);
	}

}
