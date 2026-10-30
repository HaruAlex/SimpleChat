package simplechat.client;

import java.io.IOException;
import java.util.Scanner;

import simplechat.ChatIF;

/**
 * Giao diện console của ứng dụng SimpleChat Client.
 *
 * Tiếp nhận dữ liệu từ người dùng và chuyển yêu cầu
 * đến ChatClient xử lý.
 */
public class ClientConsole implements ChatIF {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5555;

    private final ChatClient client;

    /**
     * Khởi tạo ClientConsole.
     *
     * @param loginId tên người dùng
     * @param host địa chỉ Server
     * @param port cổng Server
     */
    public ClientConsole(String loginId, String host, int port) {

        client = new ChatClient(this, loginId, host, port);
    }

    /**
     * Hiển thị thông báo ra console.
     *
     * @param message nội dung cần hiển thị
     */
    @Override
    public void display(String message) {

        System.out.println(message);
    }

    /**
     * Xử lý dữ liệu do người dùng nhập.
     *
     * @param message nội dung tin nhắn
     */
    public void handleMessageFromClientUI(String message) {

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        message = message.trim();

        if (message.startsWith("#")) {
            handleCommand(message);
            return;
        }

        if (!client.isConnected()) {
            display("You are not connected to the server.");
            return;
        }

        try {
            client.sendToServer(message);
        } catch (IOException e) {
            display("Cannot send message: " + e.getMessage());
        }
    }

    /**
     * Xử lý các lệnh điều khiển Client.
     *
     * @param command lệnh người dùng nhập
     */
    private void handleCommand(String command) {

        String[] parts = command.trim().split("\\s+", 2);
        String action = parts[0].toLowerCase();

        try {

            switch (action) {

                case "#quit":

                    client.quit();
                    break;

                case "#logoff":

                    client.logoff();
                    break;

                case "#login":

                    client.login();
                    break;

                case "#sethost":

                    if (client.isConnected()) {
                        display("Cannot change host while connected.");
                    } else if (parts.length < 2
                            || parts[1].trim().isEmpty()) {
                        display("Usage: #sethost <host>");
                    } else {

                        client.setHost(parts[1].trim());

                        display("Host changed to: "
                                + client.getHost());
                    }

                    break;

                case "#setport":

                    if (client.isConnected()) {
                        display("Cannot change port while connected.");
                    } else if (parts.length < 2) {
                        display("Usage: #setport <port>");
                    } else {

                        int port = Integer.parseInt(parts[1].trim());

                        if (port < 1 || port > 65535) {
                            display("Port must be between 1 and 65535.");
                            break;
                        }

                        client.setPort(port);

                        display("Port changed to: "
                                + client.getPort());
                    }

                    break;

                case "#gethost":

                    display("Current host: " + client.getHost());
                    break;

                case "#getport":

                    display("Current port: " + client.getPort());
                    break;

                default:

                    display("Unknown command: " + action);
                    display("Available commands:");
                    display("#login, #logoff, #quit");
                    display("#sethost, #setport");
                    display("#gethost, #getport");
                    break;
            }

        } catch (IOException e) {

            display("Connection error: " + e.getMessage());

        } catch (NumberFormatException e) {

            display("Invalid port number.");
        }
    }

    /**
     * Khởi chạy chương trình Client.
     *
     * Cách sử dụng:
     * ClientConsole <loginId> [host] [port]
     *
     * @param args tham số dòng lệnh
     */
    public static void main(String[] args) {

        if (args.length < 1 || args[0].trim().isEmpty()) {

            System.out.println(
                    "Usage: ClientConsole <loginId> [host] [port]"
            );

            return;
        }

        String loginId = args[0].trim();
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        try {

            if (args.length >= 2) {
                host = args[1].trim();
            }

            if (args.length >= 3) {
                port = Integer.parseInt(args[2]);
            }

            if (port < 1 || port > 65535) {
                System.out.println("Invalid port number.");
                return;
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid port number.");
            return;
        }

        ClientConsole console =
                new ClientConsole(loginId, host, port);

        try {

            console.client.login();

        } catch (IOException e) {

            console.display(
                    "Cannot connect to server: " + e.getMessage()
            );
        }

        console.display("SimpleChat Client started.");
        console.display("Enter messages to chat.");
        console.display("Enter #quit to exit.");

        Scanner scanner = new Scanner(System.in);

        while (true) {

            String message = scanner.nextLine();

            console.handleMessageFromClientUI(message);
        }
    }
}
