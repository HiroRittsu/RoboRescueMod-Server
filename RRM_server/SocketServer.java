public class SocketServer {

	SocketAPI socketAPI_map = new SocketAPI();

	private int port;
	private String ip;

	public SocketServer(int port, String ip) {
		this.port = port;
		this.ip = ip;
	}

	public void pubGML(String Path) {

		socketAPI_map.joinServer(port, ip);

		if (Path != null)
			socketAPI_map.publishFile(Path + "map.gml");
	}

}
