package test;

public class vnfNormalState implements vnfState {
	public Vnf vnf;

	public vnfNormalState(Vnf vnf) {
		this.vnf = vnf;

	}

	@Override
	public int scaleUp(int amount) {
		// TODO Auto-generated method stub
		if (amount < this.vnf.cosumption) {
			this.vnf.setState(this.vnf.upState);
			this.vnf.state.scaleUp(amount);
			return 1;// ���ݳɹ�
		} else
			return -1;// ����ʧ��

	}

	@Override
	public int scaleDown(int amount) {
		// TODO Auto-generated method stub
		if (amount < this.vnf.cosumption) {
			this.vnf.setState(this.vnf.downState);
			return 1;// ѹ���ɹ�����������
		} else
			return -1;// ѹ��ʧ��

	}

}
