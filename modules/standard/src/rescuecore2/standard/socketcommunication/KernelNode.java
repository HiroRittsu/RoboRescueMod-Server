package rescuecore2.standard.socketcommunication;

import java.util.ArrayList;

public class KernelNode {

	public ArrayList<String> agent_data = null;
	private OriginalSocket originalSocket = null;

	public KernelNode() {
		originalSocket = new OriginalSocket();
		originalSocket.joinServer(12345, "localhost");
		agent_data = new ArrayList<>();
	}

	private int getTime() {
		return 0;
	}

	public void addAgentData(String agent_name, int id, int x, int y) {
		agent_data.add(agent_name + "," + String.valueOf(id) + "," + String.valueOf(x) + "," + String.valueOf(y));
	}

	public void socketThread() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					if (agent_data != null && !agent_data.isEmpty()) {
						originalSocket.publishMsgs(agent_data.remove(0));
					}
					originalSocket.delaitinon(1);
				}
			}
		}).start();
	}
}
