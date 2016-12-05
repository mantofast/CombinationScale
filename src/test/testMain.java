package test;

import java.util.ArrayList;

public class testMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vnf vnf1 = new Vnf(1, 100);
		Thread t1 = new Thread(vnf1);
		t1.start();

		ArrayList<Vnf> vnflist = new ArrayList<Vnf>();
		vnflist.add(vnf1);
		Sfc sfc1 = new Sfc(vnflist, 11, 10);

		Thread t2 = new Thread(sfc1);
		t2.start();

	}

}
