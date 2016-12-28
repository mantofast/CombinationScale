package test;

public class vnfNormalState implements vnfState {
	public Vnf vnf;

	public vnfNormalState(Vnf vnf) {
		this.vnf = vnf;

	}

	@Override
	public int scaleUp(int amount) {
		// TODO Auto-generated method stub
		this.vnf.setState(this.vnf.upState);
		return this.vnf.state.scaleUp(amount);

	}

	@Override
	public int scaleDown(int amount) {
		// TODO Auto-generated method stub
		if (amount < this.vnf.cosumption) {
			this.vnf.setState(this.vnf.downState);
			return 1;// Ñ¹Ëõ³É¹¦£¬¿ÉÒÔÀ©ÈÝ
		} else
			return -1;// Ñ¹ËõÊ§°Ü

	}

}
