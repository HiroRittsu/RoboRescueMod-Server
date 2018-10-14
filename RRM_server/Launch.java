public class Launch {

	public static void main(String[] args) {

		SocketServer server = new SocketServer(6591, "localhost");

		server.pubGML("/home/migly/git/rcrs-server/maps/gml/test/map/");

	}

}
