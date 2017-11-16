package com.rainsho.course.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class IM_Client {
	private static final int SERVER_PORT = 9001;

	public IM_Client() {
		try {
			InetAddress address = InetAddress.getByName("192.168.5.55");
			System.out.println("客户端已启动。");
			S2.s = new Socket(address, SERVER_PORT);
			new Thread(new Rec2(S2.s)).start();
			new Thread(new Send2(S2.s)).start();
		} catch (IOException nhe) {
			System.out.println("未找到指定主机...");
		}
	}

	public static void main(String[] args) {
		new IM_Client();
	}
}

class Rec2 implements Runnable {
	Socket s;

	public Rec2(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {

		try {
			while (S2.s.isConnected()) {
				InputStream in = s.getInputStream();
				byte[] buf = new byte[1024];
				int len = in.read(buf);
				String str_rec = new String(buf, 0, len);
				System.out.print("来自服务端的回答>>");
				System.out.println(str_rec);
				if (str_rec.equals("exit")) {
					OutputStream out = s.getOutputStream();
					out.write("exit".getBytes());
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Send2 implements Runnable {
	Socket s;

	public Send2(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			while (S2.s.isConnected()) {
				OutputStream out = s.getOutputStream();
				System.out.print("说点什么: ");
				@SuppressWarnings("resource")
				String str_send = new Scanner(System.in).nextLine();
				out.write(str_send.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class S2 {
	static Socket s;
}