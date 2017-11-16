package com.rainsho.project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class FiveCard {

	public static void main(String[] args) {

		/*
		 * Card[] all_card = new AllCard().shuffle();// 洗牌 Player[] p = new
		 * Player[10];// 创建玩家数组 double[] rd0 = new double[52];// 二次洗牌 int[] rd =
		 * new int[52]; for (int i = 0; i < rd0.length; i++) { rd0[i] =
		 * Math.random(); } for (int i = 0; i < rd0.length; i++) { for (int j =
		 * 0; j < rd0.length; j++) { if (rd0[i] > rd0[j]) { rd[i]++; } } }
		 * 
		 * for (int i = 0; i < p.length; i++) { p[i] = new Player(); // 实例化玩家
		 * for (int j = 0; j < 5; j++) p[i].get_one_card(all_card[rd[i * 5 +
		 * j]]); // 发牌 }
		 * 
		 * Player pp = new Player(); // 根据牌面大小排序，进行测试 for (int i = 0; i <
		 * p.length - 1; i++) { for (int j = 0; j < p.length - 1 - i; j++) { if
		 * (p[j].card_value() < p[j + 1].card_value()) { pp = p[j]; p[j] = p[j +
		 * 1]; p[j + 1] = pp; } } }
		 * 
		 * for (int i = 0; i < p.length; i++) { // 输出结果，测试对比 for (int j = 0; j <
		 * 5; j++) { System.out.print(p[i].my_card[j].show() + "\t"); }
		 * System.out.println(p[i].card_value0() + "\t" + p[i].card_value()); }
		 */

		FiveCard fc = new FiveCard();
		fc.welcome();

	}

	void welcome() {
		Player ai = new Player();
		Player usr = new Player();
		System.out.println("Welcome to Rainsho's Five Card Draw Game!");
		System.out.println("Now you have $" + usr.money + "!");
		System.out.println("Have fun!");
		while (sc("\nLet's play?(y/n): ").equals("y")) {
			play(ai, usr);
		}
	}

	void play(Player ai, Player usr) {
		Card[] all_card = new AllCard().shuffle();
		ai.init();
		usr.init();
		Random r = new Random();
		int i = r.nextInt(30);
		String t, str_ai, str_usr;
		ai.get_one_card(all_card[i++]);
		ai.get_one_card(all_card[i++]);
		usr.get_one_card(all_card[i++]);
		usr.get_one_card(all_card[i++]);
		str_ai = "Computer have:\t***\t" + ai.my_card[1].show();
		str_usr = "You have:\t" + usr.my_card[0].show() + "\t"
				+ usr.my_card[1].show();
		System.out.println(str_ai);
		System.out.println(str_usr);
		t = sc("Continue?(y/n): ");
		if (!t.equals("y")) {
			return;
		}
		ai.get_one_card(all_card[i++]);
		usr.get_one_card(all_card[i++]);
		str_ai += "\t" + ai.my_card[2].show();
		str_usr += "\t" + usr.my_card[2].show();
		System.out.println(str_ai);
		System.out.println(str_usr);
		t = sc("Continue?(y/n): ");
		if (!t.equals("y")) {
			return;
		}
		ai.get_one_card(all_card[i++]);
		usr.get_one_card(all_card[i++]);
		str_ai += "\t" + ai.my_card[3].show();
		str_usr += "\t" + usr.my_card[3].show();
		System.out.println(str_ai);
		System.out.println(str_usr);
		t = sc("Continue?(y/n): ");
		if (!t.equals("y")) {
			return;
		}
		ai.get_one_card(all_card[i++]);
		usr.get_one_card(all_card[i++]);
		str_ai += "\t" + ai.my_card[4].show();
		str_usr += "\t" + usr.my_card[4].show();
		System.out.println(str_ai);
		System.out.println(str_usr);
		t = sc("Continue?(y/n): ");
		if (!t.equals("y")) {
			return;
		}
		str_ai = str_ai.replaceFirst("\\*{3}", ai.my_card[0].show());
		System.out.println(str_ai);
		System.out.println(str_usr);
		if (ai.card_value() > usr.card_value()) {
			System.out.println("You lose $1000!");
			usr.money -= 1000;
		} else {
			System.out.println("You win $1000!");
			usr.money += 1000;
		}
		System.out.println("Now you have $" + usr.money + "!");
	}

	String sc(String x) {
		System.out.print(x);
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		return s.next();
	}

}

class Card {

	int t, s;

	public Card(int t, int s) {
		this.t = t;
		this.s = s;
	}

	int get_t() {
		return this.t;
	}

	int get_s() {
		return this.s;
	}

	String show() {
		String str = "";
		switch (this.t) {
		case 1:
			str += "♠";
			break;
		case 2:
			str += "♥";
			break;
		case 3:
			str += "♣";
			break;
		case 4:
			str += "♦";
			break;
		}
		switch (this.s) {
		case 11:
			str += "J";
			break;
		case 12:
			str += "Q";
			break;
		case 13:
			str += "K";
			break;
		case 14:
			str += "A";
			break;
		default:
			str += this.s;
			break;
		}
		return str;
	}

}

class AllCard {

	Card[] shuffle() {
		int[] arr_t = { 1, 2, 3, 4 };
		int[] arr_s = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
		HashSet<Card> s = new HashSet<Card>();
		for (int i = 0; i < arr_t.length; i++) {
			for (int j = 0; j < arr_s.length; j++) {
				s.add(new Card(arr_t[i], arr_s[j]));
			}
		}
		Card[] arr = new Card[52];
		int i = 0;
		for (Card obj : s) {
			arr[i++] = obj;
		}
		return arr;
	}

}

class Player {

	int money = 10000;
	int i = 0;
	Card[] my_card = new Card[5];

	void init() {
		i = 0;
		this.my_card = new Card[5];
	}

	void get_one_card(Card obj) {
		if (this.i > 4) {
			System.out
					.println("you've already got 5 cards, can't get any more!");
			return;
		}
		this.my_card[this.i] = obj;
		this.i++;
	}

	String card_value0() {
		if (i != 5) {
			System.out.println("doesn't have 5 cards can't compare!");
			return null;
		}
		CardValue cv = new CardValue(this.my_card);
		return cv.value_of_type() + cv.value_of_score();
	}

	long card_value() {
		if (i != 5) {
			System.out.println("doesn't have 5 cards can't compare!");
			return -1;
		}
		return Long.valueOf(card_value0(), 16);
	}

}

class CardValue {

	Card[] my_card;
	int[] arr_t = new int[5];
	int[] arr_s = new int[5];
	int[][] arr_4 = { { 1, 1, 1, 1, 1 }, { 2, 2, 2, 2, 2 }, { 3, 3, 3, 3, 3 },
			{ 4, 4, 4, 4, 4 } };

	CardValue(Card[] my_card) {
		this.my_card = my_card;
		for (int i = 0; i < my_card.length; i++) {
			arr_t[i] = my_card[i].get_t();
			arr_s[i] = my_card[i].get_s();
		}
		Arrays.sort(arr_t);
		Arrays.sort(arr_s);
	}

	String value_of_type() {
		String str = hex(ck1_straight_flush(), ck2_four_of_a_kind(),
				ck3_fullhouse(), ck4_flush(), ck5_straight(), ck6_3_of_a_kind())
				+ hex(ck7_pair(), 2);
		return str;
	}

	String value_of_score() {
		String str = "";
		for (int i = 0; i < 5; i++) {
			str += Integer.toHexString(arr_s[4 - i]);
		}
		return str;
	}

	boolean ck0_straight() {
		boolean t = true;
		for (int i = 0; i < 5; i++) {
			t = t & (arr_s[i] == arr_s[0] + i);
		}
		int[] b = { 2, 3, 4, 5, 14 };
		t = t || Arrays.equals(arr_s, b);
		return t;
	}

	String hex(int... x) {
		String str = "";
		for (int i : x) {
			str += Integer.toHexString(i);
		}
		return str;
	}

	String hex(int x, int i) {
		String str = Integer.toHexString(x);
		return str.length() < i ? "0" + str : str;
	}

	int ck1_straight_flush() {// 同花顺
		boolean t = false;
		for (int i = 0; i < 4; i++) {
			t = t || Arrays.equals(arr_t, arr_4[i]);
		}
		if (t) {
			if (ck0_straight()) {
				return (arr_s[3] == 5 && arr_s[4] == 14) ? 5 : arr_s[4];
			}
		}
		return 0;
	}

	int ck2_four_of_a_kind() {// 四条
		boolean t = true;
		t = (arr_s[1] == arr_s[2] && arr_s[1] == arr_s[3])
				& (arr_s[1] == arr_s[0] || arr_s[1] == arr_s[4]);
		return t ? arr_s[1] : 0;
	}

	int ck3_fullhouse() {// 三张一对
		boolean t = true;
		t = (arr_s[0] == arr_s[1] && arr_s[0] == arr_s[2] && arr_s[3] == arr_s[4])
				|| (arr_s[0] == arr_s[1] && arr_s[2] == arr_s[3] && arr_s[2] == arr_s[4]);
		return t ? arr_s[2] : 0;// 不可能出现三张部分相同，比较三张部分即可
	}

	int ck4_flush() {// 同花
		boolean t = false;
		for (int i = 0; i < 4; i++) {
			t = t || Arrays.equals(arr_t, arr_4[i]);
		}
		return t ? arr_s[4] : 0;
	}

	int ck5_straight() {// 顺子
		if (ck0_straight()) {
			return (arr_s[3] == 5 && arr_s[4] == 14) ? 5 : arr_s[4];
		}
		return 0;
	}

	int ck6_3_of_a_kind() {// 三条
		boolean t = true;
		t = (arr_s[2] == arr_s[0] && arr_s[2] == arr_s[1])
				|| (arr_s[2] == arr_s[3] && arr_s[2] == arr_s[4])
				|| (arr_s[2] == arr_s[1] && arr_s[2] == arr_s[3]);
		return t ? arr_s[2] : 0;
	}

	int ck7_pair() { // 两对与一对合并
		int k = 1, k2 = 0;
		for (int i = 0; i < 5 - 1; i++) {
			if (arr_s[i] != arr_s[i + 1]) {
				k++;
			} else {
				k2 = arr_s[i + 1];
			}
		}
		if (k == 4) {
			return k2;
		}
		if (k == 3) {
			return arr_s[3] * 16 + arr_s[1];
		}
		return 0;
	}

	/*
	 * int ck7_two_pair() {// 两对 11223 12233 11233 || 11234 12234 12334 12344 if
	 * ((arr_s[0] == arr_s[1] && arr_s[2] == arr_s[3]) || (arr_s[1] == arr_s[2]
	 * && arr_s[3] == arr_s[4]) || (arr_s[0] == arr_s[1] && arr_s[3] ==
	 * arr_s[4])
	 * 
	 * ) { return arr_s[3] * 100 + arr_s[1]; } return 0; }
	 * 
	 * int ck8_one_pair() {// 一对 int k = 0, k2 = 0; for (int i = 0; i <
	 * arr_s.length - 1; i++) { if (arr_s[i] == arr_s[i + 1]) { k++; k2 =
	 * arr_s[i + 1]; } } return k == 1 ? k2 : 0; } int ck9_zilch() {// 无对 return
	 * 1; }
	 */

}