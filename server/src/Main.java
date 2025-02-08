public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(5000);
        server.start();
        System.out.println("Server running on port 5000");
    }
}