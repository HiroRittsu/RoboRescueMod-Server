import java.util.Scanner;

public class Launch {

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		SocketServer server = new SocketServer(6591, "localhost");
		String string;

		// server.pubGML("/home/migly/git/rcrs-server/maps/gml/test/map/");

		while (true) {
			System.out.println("listen client");
			server.publicText(scanner.nextLine());
			//string = scanner.next();
			//System.out.println(string);
			//server.pubGML(string);

		}

	}

}
