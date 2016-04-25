package com.fs121.monitor;

import java.util.Date;

public class TestDate {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Date d1 = new Date();
		d1.setSeconds(0);
		Date d2 = new Date();

		System.out.println(d1.getTime());
		System.out.println(d2.getTime());
		System.out.println(d2.getTime() - d1.getTime());
	}

}
