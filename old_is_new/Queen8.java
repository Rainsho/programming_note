package com.rainsho.project;

import java.util.Date;

public class Queen8 {

	int[] a = new int[8];
	int[] b = new int[8];
	int x = a.length;
	int s = 0;
	int t = 0;

	boolean ck(int... a) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (a[i] == a[j] || Math.abs(i - j) == Math.abs(a[i] - a[j])) {
					return false;
				}
			}
		}
		return true;
	}

	boolean ck(int n) {// 前n位
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				if (a[i] == a[j] || Math.abs(i - j) == Math.abs(a[i] - a[j])) {
					return false;
				}
			}
		}
		return true;
	}

	void f1() {
		long timer1 = new Date().getTime();
		int t = 0, s = 0;
		for (int i = 0; i < Math.pow(x, x); i++) {
			t = i;
			for (int j = 0; j < a.length; j++) {
				a[j] = (int) (t / Math.pow(x, x - j - 1));
				t %= Math.pow(x, x - j - 1);
				if (!this.ck(j + 1)) {
					break;
				}
			}
			if (this.ck(a)) {
				s++;
			}
		}
		long timer2 = new Date().getTime();
		System.out.println(s + "\ttimer: " + (timer2 - timer1) / 1000.0 + "s");
	}

	void f2_0(int n) {
		t++;
		if (n == x) {
			s++;
			return;
		} else {
			for (int i = 0; i < x; i++) {
				a[n] = i;
				if (this.ck(n + 1)) {
					this.f2_0(n + 1);
				}
			}
		}
	}

	void f2() {
		long timer1 = new Date().getTime();
		this.f2_0(0);
		long timer2 = new Date().getTime();
		System.out.println(s + "\ttimer: " + (timer2 - timer1) / 1000.0 + "s");
	}

	public static void main(String[] args) {

		Queen8 q = new Queen8();
		q.f1(); // about 15s --> (add filter) 7.2s
		q.f2(); // about 0.005s
		System.out.println(q.t); // t=2057

	}
}
