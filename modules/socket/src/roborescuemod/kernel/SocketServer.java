package roborescuemod.kernel;

import java.util.ArrayList;

public class SocketServer {

	public OriginalSocket originalSocket;
	public int port;
	public String ip;

	public SocketServer(int port, String ip) {
		this.port = port;
		this.ip = ip;
		originalSocket = new OriginalSocket();
		originalSocket.joinServer(port, ip);
	}

	public void publishConfig(ArrayList<String> msgs) {
		for (String msg : msgs) {
			originalSocket.publishMsgs(msg);
		}
	}

	public void publishScenario(String msg) {
		originalSocket.publishMsgs(msg);
	}

	public void publishServer(ArrayList<String> msgs) {
		for (String msg : msgs) {
			originalSocket.publishMsgs(msg);
		}
	}

	public void publishCommand(String command) {
		switch (command) {
		case "registry_map":
		case "orient_scenario":
			originalSocket.publishMsgs("command," + command);
			break;

		default:
			System.out.println("command例外送信");
			break;
		}
	}

	public void waitCommand(String command) {
		while (originalSocket.subscribeMsgs() == "command," + command) {
			System.out.println("待機");
		}
	}

}
