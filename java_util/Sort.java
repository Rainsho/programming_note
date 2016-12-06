package com.rainsho.util;

import java.util.Random;

public class Sort {
	public static void main(String[] args) {
		Sort sort = new Sort();
		int[] arr = sort.gen_arr(10);
		sort.dip_arr(arr);
		sort.dip_arr(sort.bubble(arr));
		sort.dip_arr(sort.insertion(arr));
		sort.dip_arr(sort.selection(arr));
		int[] arg = arr.clone();
		sort.quick(arg, 0, arg.length - 1);
		sort.dip_arr(arg);
		sort.dip_arr(arr);
	}

	public int[] gen_arr(int n) {
		int[] arr = new int[n];
		Random r = new Random();
		for (int i = 0; i < arr.length; i++) {
			arr[i] = r.nextInt(100);
		}
		return arr;
	}

	public void dip_arr(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + "\t");
		}
		System.out.println();
	}

	// 冒泡排序a[n]<>a[n+1]
	public int[] bubble(int[] arg) {
		int[] arr = arg.clone();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length - i - 1; j++) {
				if (arr[j] > arr[j + 1]) {
					int t = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = t;
				}
			}
		}
		return arr;
	}

	// 插入排序a[0,n]<--a[n+1]
	public int[] insertion(int[] arg) {
		int[] arr = arg.clone();
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = i + 1; j > 0; j--) {
				if (arr[j - 1] < arr[j]) {
					break;
				}
				int t = arr[j - 1];
				arr[j - 1] = arr[j];
				arr[j] = t;
			}
		}
		return arr;
	}

	// 选择排序a[n]=min
	public int[] selection(int[] arg) {
		int[] arr = arg.clone();
		for (int i = 0; i < arr.length - 1; i++) {
			int min = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[min] > arr[j]) {
					min = j;
				}
			}
			int t = arr[i];
			arr[i] = arr[min];
			arr[min] = t;
		}
		return arr;
	}

	// 快速排序a[n]-->a[0,x)+a(x,n]
	public void quick(int[] arg, int s, int t) {
		if (s >= t) {
			return;
		}
		int m = arg[t];
		int l = s, r = t - 1;
		while (l < r) {
			while (arg[l] < m && l < r) {
				l++;
			}
			while (arg[r] > m && l < r) {
				r--;
			}
			int temp = arg[l];
			arg[l] = arg[r];
			arg[r] = temp;
		}
		if (arg[l] >= arg[t]) {
			int temp = arg[t];
			arg[t] = arg[l];
			arg[l] = temp;
		} else {
			l++;
		}
		quick(arg, s, l - 1);
		quick(arg, l + 1, t);
	}
}