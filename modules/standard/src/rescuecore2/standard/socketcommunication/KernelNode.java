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

	public void addAgentData(int time, String agent_name, int id, int[] histly) {

		if (histly != null) {
			String input = null;
			input = String.valueOf(time) + "," + agent_name + "," + String.valueOf(id);
			for (int point : histly) {
				input = input + "," + point;
			}

			agent_data.add(input);
		}
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
