import shared.Constants;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(Constants.SERVER_PORT);
        server.start();
        System.out.println("Server running on port " + Constants.SERVER_PORT);
    }
}