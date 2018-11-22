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

	public void publishCommand(String command) {
		switch (command) {
		case "registry_map":
			originalSocket.publishMsgs("command,registry_map");
			break;

		default:
			break;
		}
	}

}
