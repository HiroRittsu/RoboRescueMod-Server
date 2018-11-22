package roborescuemod.kernel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class OriginalSocket {

	private ServerSocket server = null;
	private Socket socket = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;

	private void buffer() { // buffer変数作成
		try {
			// socket送信
			this.writer = new PrintWriter(socket.getOutputStream(), true);
			// socket受け取り
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error CreateBuffer");
		}
	}

	public void delaitinon(int milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void joinServer(int port, String ip) {

		try {
			// serverの設定
			server = new ServerSocket();
			server.bind(new InetSocketAddress(ip, port));

			System.out.println("Waiting Client...");
			// 応答待機、応答後接続
			socket = server.accept();

			buffer();

		} catch (IOException e) {
			System.out.println("un-connection");
		}

		System.out.println("connected");

	}

	public void joinClient(int port, String ip) {
		System.out.println("Waiting Server...");

		while (socket == null) {
			try {
				socket = new Socket(ip, port);
				buffer();
			} catch (IOException e) {
				// System.out.print(".");
			}
		}

		System.out.println("connected");
	}

	public void publishMsgs(String s) {
		// 送信
		// (new BufferedWriter(this.output)).write(s);
		writer.println(s);
		System.out.println("send");
	}

	public String subscribeMsgs() {

		try {
			// 受信
			return this.reader.readLine();
		} catch (IOException e) {
			System.out.println("Error Subsclibe");
		}

		return null;
	}
}
