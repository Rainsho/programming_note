package com.rainsho.project;

public class Init_RMB {

	String no[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	String un1[] = { "仟", "佰", "拾" };
	String un2[] = { "元", "万", "亿", "万亿", "亿亿" };
	String un3[] = { "角", "分" };

	public static void main(String[] args) {
		Init_RMB rmb = new Init_RMB();
		rmb.up(1010101);
		rmb.up(1011010.10);
		rmb.up(1011001.00);
		rmb.up(100010000.01);
		rmb.up(10010010000010010.01);
		rmb.up(1001010100101000010.01);

	}

	void up(double x) {
		if (x > 1E20) {
			System.out.println("go and fuck yourself!");
			return;
		}
		long yuan = (long) x;
		int cent = (int) (x * 100 % 100);
		if (cent < 10) {
			System.out.println(yuan + ".0" + cent);
		} else {
			System.out.println(yuan + "." + cent);
		}
		String up_rmb = up_yuan(yuan) + up_cent(cent);
		if (up_rmb.startsWith("零")) {
			up_rmb = up_rmb.substring(1);
		}
		System.out.println(up_rmb);
	}

	String up_cent(int x) {
		if (x == 0) {
			return "整";
		} else if (x < 10) {
			return no[0] + no[x] + "分整";
		} else if (x % 10 == 0) {
			return no[x / 10] + "角" + "整";
		} else {
			return no[x / 10] + "角" + no[x % 10] + "分整";
		}
	}

	String up_yuan(long x) {
		if (x == 0) {
			return no[0] + un2[0];
		}
		int l = (x + "").length();
		int t = (int) Math.ceil((double) l / 4);
		int[] arr = new int[t];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (x / Math.pow(10, 4 * (t - i - 1)));
			x %= Math.pow(10, 4 * (t - i - 1));
		}
		// 4位拆分，调用bt4处理
		String up = "";
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 0 && i < arr.length - 1) {
				if (arr[i + 1] >= 1000) {
					up += "零";
				}
				continue;
			}
			if (arr[i] == 0) {
				if (t - i - 1 == 0) {
					up += un2[t - i - 1];
					continue;
				} else {
					continue;
				}

			}
			up += bt4(arr[i]) + un2[t - i - 1];
		}

		return up;
	}

	String bt4(int x) {
		if (x == 0) {
			return "";
		}
		if (x % 1000 == 0) {
			return no[x / 1000] + un1[0];
		}
		int[] arr = new int[4];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (x / Math.pow(10, 3 - i));
			x %= Math.pow(10, 3 - i);
		}

		String up = "";
		for (int i = 0; i < arr.length; i++) {
			if (i == 0 && arr[i] == 0) {
				up += no[arr[i]];
				continue;
			}
			if (up.length() == 2 && arr[i] == 0) {
				up += no[arr[i]];
				continue;
			}

			if (i > 0 && arr[i] == 0 && up != "") {
				continue;
			}
			if (i == 3 && arr[i] == 0) {
				continue;
			} else if (i == 3 && arr[i - 1] == 0 && !up.endsWith("零")) {
				up += "零" + no[arr[i]];
			} else if (i == 3) {
				up += no[arr[i]];
			} else {
				up += no[arr[i]] + un1[i];
			}
		}
		return up;
	}

}
