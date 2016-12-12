package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class testMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println("Begin to test");
		Vnf vnf1 = new Vnf(1, 100);
		Thread t1 = new Thread(vnf1);
		t1.start();

		Vnf vnf2 = new Vnf(2, 100);
		Thread t2 = new Thread(vnf2);
		t2.start();

		Vnf vnf3 = new Vnf(3, 80);
		Thread t3 = new Thread(vnf3);
		t3.start();

		Vnf vnf4 = new Vnf(4, 100);
		Thread t4 = new Thread(vnf4);
		t4.start();

		Vnf vnf5 = new Vnf(5, 90);
		Thread t5 = new Thread(vnf5);
		t5.start();

		ArrayList<Vnf> VnfList1 = new ArrayList<Vnf>();
		VnfList1.add(vnf1);
		VnfList1.add(vnf2);
		VnfList1.add(vnf3);

		Queue<Vnf> VnfQueue1 = new LinkedList<Vnf>();
		VnfQueue1.add(vnf1);
		VnfQueue1.add(vnf2);
		VnfQueue1.add(vnf3);
		Sfc sfc1 = new Sfc(VnfQueue1, VnfList1, 1, 80);

		ArrayList<Vnf> VnfList2 = new ArrayList<Vnf>();
		VnfList2.add(vnf4);
		VnfList2.add(vnf2);
		VnfList2.add(vnf5);

		Queue<Vnf> VnfQueue2 = new LinkedList<Vnf>();
		VnfQueue2.add(vnf4);
		VnfQueue2.add(vnf2);
		VnfQueue2.add(vnf5);
		Sfc sfc2 = new Sfc(VnfQueue2, VnfList2, 2, 80);

		Thread sfcT1 = new Thread(sfc1);
		sfcT1.start();

		Thread sfcT2 = new Thread(sfc2);
		sfcT2.start();

	}

}
