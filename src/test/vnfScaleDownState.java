package test;

public class vnfScaleDownState implements vnfState {

	public Vnf vnf;

	public vnfScaleDownState(Vnf vnf) {
		this.vnf = vnf;

	}

	@Override
	public int scaleUp(int amount) {
		// TODO Auto-generated method stub
		System.out
				.println("can't change state to the scaling up during the scaling down process");
		return -1;
	}

	@Override
	public int scaleDown(int amount) {
		// TODO Auto-generated method stub
		System.out.println("the vnf is already in the scaling down process");
		return -1;
	}

	public int run() {
		this.vnf.totalCpu -= 10;
		return 1;
	}

}
