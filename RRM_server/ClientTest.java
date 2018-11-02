public class ClientTest {

	public static void main(String[] args) {

		OriginalSocket originalSocket = new OriginalSocket();

		originalSocket.joinClient(12345, "localhost");

		while (true) {
			System.out.println(originalSocket.subscribeMsgs());
		}
	}
}
