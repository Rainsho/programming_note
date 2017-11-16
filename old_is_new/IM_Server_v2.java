package com.rainsho.course.thread.imv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class IM_Server_v2 {
	private static final int SERVER_PORT = 9001;

	public IM_Server_v2() {
		try {
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(SERVER_PORT);
			System.out.println("服务端已启动。");

			S.s[S.n++] = ss.accept(); // 第一个连接对象
			new Thread(new Send()).start();
			new Thread(new Rec(S.s[S.n - 1], S.n)).start();

			while (true) { // 后续连接对象只接收
				S.s[S.n++] = ss.accept();
				new Thread(new Rec(S.s[S.n - 1], S.n)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new IM_Server_v2();
	}
}

class Rec implements Runnable {
	Socket s;
	int n;

	public Rec(Socket s, int n) {
		this.s = s;
		this.n = n;
	}

	@Override
	public void run() {
		while (s.isConnected()) {
			try {
				InputStream in = s.getInputStream();
				byte[] buf = new byte[1024];
				int len = in.read(buf);
				String str_rec = new String(buf, 0, len);
				System.out.print("来自" + n + "号客户端的信息>>");
				System.out.println(str_rec);
				if (str_rec.equals("exit")) {
					for (Socket x : S.s) {
						if (x != null) {
							OutputStream out = x.getOutputStream();
							out.write("exit".getBytes());
						}
					}
					System.exit(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class Send implements Runnable {
	@Override
	public void run() {
		while (true) {
			try {
				//System.out.print("说点什么: ");
				@SuppressWarnings("resource")
				String str_send = new Scanner(System.in).nextLine();
				for (Socket x : S.s) {
					if (x != null) {
						OutputStream out = x.getOutputStream();
						out.write(str_send.getBytes());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class S {
	static Socket[] s = new Socket[20];
	static int n = 0;
}
