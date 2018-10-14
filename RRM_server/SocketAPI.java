import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAPI {

	private ServerSocket server = null;
	private Socket socket = null;
	private OutputStreamWriter output;
	private InputStreamReader input;

	private void buffer() { // buffer変数作成
		try {
			// socket送信
			output = new OutputStreamWriter(socket.getOutputStream());
			// socket受け取り
			input = new InputStreamReader(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Error CreateBuffer");
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
				System.out.print(".");
			}
		}

		System.out.println("connected");
	}

	public void pub_msgs(String s) {
		try {
			// 送信
			(new BufferedWriter(output)).write(s);
			System.out.println("send");
		} catch (IOException e) {
			System.out.println("Error Publisher");
		}
	}

	public String get_msgs() {

		try {
			// 受信
			return (new BufferedReader(input)).readLine();
		} catch (IOException e) {
			System.out.println("Error Subsclibe");
		}

		return null;
	}

	public void publishFile(String Path) {

		byte[] buffer = new byte[51200]; // ファイル送信時のバッファ

		try {
			@SuppressWarnings("resource")
			InputStream inputStream = new FileInputStream(Path);
			OutputStream outputStream = socket.getOutputStream();

			// ファイルをストリームで送信
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}

		} catch (FileNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	public InputStream subscribeFileData() {

		try {

			return socket.getInputStream();

		} catch (FileNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return null;
	}

}
