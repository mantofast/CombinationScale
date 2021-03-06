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

		Vnf vnf1 = new Vnf(1, 1, 60);
		Thread t1 = new Thread(vnf1);
		t1.start();

		Vnf vnf2 = new Vnf(2, 1, 60);
		Thread t2 = new Thread(vnf2);
		t2.start();

		Vnf vnf3 = new Vnf(3, 1, 38);
		Thread t3 = new Thread(vnf3);
		t3.start();

		Vnf vnf4 = new Vnf(4, 1, 60);
		Thread t4 = new Thread(vnf4);
		t4.start();

		Vnf vnf5 = new Vnf(5, 1, 38);
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
		Sfc sfc1 = new Sfc(1, 20, 60, VnfQueue1, VnfList1, 4, 16, 2000);

		ArrayList<Vnf> VnfList2 = new ArrayList<Vnf>();
		VnfList2.add(vnf4);
		VnfList2.add(vnf2);
		VnfList2.add(vnf5);

		Queue<Vnf> VnfQueue2 = new LinkedList<Vnf>();
		VnfQueue2.add(vnf4);
		VnfQueue2.add(vnf2);
		VnfQueue2.add(vnf5);
		Sfc sfc2 = new Sfc(2, 20, 60, VnfQueue2, VnfList2, 4, 16, 2000);

		ArrayList<Vnf> VnfList3 = new ArrayList<Vnf>();
		VnfList3.add(vnf3);
		VnfList3.add(vnf2);

		Queue<Vnf> VnfQueue3 = new LinkedList<Vnf>();
		VnfQueue3.add(vnf3);
		VnfQueue3.add(vnf2);
		Sfc sfc3 = new Sfc(3, 40, 60, VnfQueue3, VnfList3, 6, 24, 5000);

		ArrayList<Vnf> VnfList4 = new ArrayList<Vnf>();
		VnfList4.add(vnf5);
		VnfList4.add(vnf2);

		Queue<Vnf> VnfQueue4 = new LinkedList<Vnf>();
		VnfQueue4.add(vnf5);
		VnfQueue4.add(vnf2);
		Sfc sfc4 = new Sfc(4, 40, 60, VnfQueue4, VnfList4, 6, 24, 5000);

		Thread sfcT3 = new Thread(sfc3);
		sfcT3.start();
		Thread sfcT4 = new Thread(sfc4);
		sfcT4.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread sfcT1 = new Thread(sfc1);
		sfcT1.start();

		Thread sfcT2 = new Thread(sfc2);
		sfcT2.start();

	}

}
