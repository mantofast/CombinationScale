package test;

import java.util.ArrayList;

public class testMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vnf vnf1 = new Vnf(1, 100);
		Thread t1 = new Thread(vnf1);
		t1.start();

		Vnf vnf2 = new Vnf(2, 100);
		Thread t2 = new Thread(vnf2);
		t2.start();

		Vnf vnf3 = new Vnf(3, 100);
		Thread t3 = new Thread(vnf3);
		t3.start();

		Vnf vnf4 = new Vnf(4, 100);
		Thread t4 = new Thread(vnf4);
		t4.start();

		Vnf vnf5 = new Vnf(5, 100);
		Thread t5 = new Thread(vnf5);
		t5.start();

		ArrayList<Vnf> vnflist1 = new ArrayList<Vnf>();
		vnflist1.add(vnf1);
		vnflist1.add(vnf2);
		vnflist1.add(vnf3);
		Sfc sfc1 = new Sfc(vnflist1, 1, 10);

		ArrayList<Vnf> vnflist2 = new ArrayList<Vnf>();
		vnflist2.add(vnf4);
		vnflist2.add(vnf2);
		vnflist2.add(vnf5);
		Sfc sfc2 = new Sfc(vnflist2, 2, 100);

		Thread sfcT1 = new Thread(sfc1);
		sfcT1.start();

		Thread sfcT2 = new Thread(sfc2);
		sfcT2.start();

	}

}
