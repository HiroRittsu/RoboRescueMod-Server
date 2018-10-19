public class SocketServer {

	OriginalSocket originalSocket = new OriginalSocket();

	public SocketServer(int port, String ip) {
		originalSocket.joinServer(port, ip);
	}

	public void pubGML(String Path) {

		if (Path != null)
			originalSocket.publishFile(Path + "map.gml");
	}

	public void publicText(String text) {
		originalSocket.publishMsgs(text);
	}

}
