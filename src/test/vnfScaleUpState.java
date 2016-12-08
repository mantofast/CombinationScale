package test;

public class vnfScaleUpState implements vnfState {
	public Vnf vnf;

	public vnfScaleUpState(Vnf vnf) {
		this.vnf = vnf;

	}

	@Override
	public int scaleUp(int amount) {
		// TODO Auto-generated method stub
		System.out.println("try to scale up");
		run(amount);
		// System.out.println("scale up end");
		return 1;
	}

	@Override
	public int scaleDown(int amount) {
		// TODO Auto-generated method stub
		System.out
				.println("can't change state to scaling dowm during the scaling up process !!!");
		return -1;
	}

	public int run(int amount) {
		if (this.vnf.totalCpu + amount <= this.vnf.MaxCpu) {

			// try {
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			for (int i = 0; i < 100; i++)
				;
			this.vnf.totalCpu += amount;
			this.vnf.setState(this.vnf.norState);
			System.out.println("vnf" + this.vnf.type + "scale up success");
			return 1;

		} else {
			System.out.println("can't scale up");
			return -1;
		}
	}

}
