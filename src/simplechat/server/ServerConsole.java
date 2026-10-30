package simplechat.server;

import java.io.IOException;
import java.util.Scanner;

import simplechat.ChatIF;

/**
 * Giao diện dòng lệnh của SimpleChat Server.
 *
 * Cho phép người quản trị điều khiển Server
 * và gửi thông điệp đến các Client.
 */
public class ServerConsole implements ChatIF {

    private static final int DEFAULT_PORT = 5555;

    private final EchoServer server;

    // Trạng thái Server đã đóng hoàn toàn hay chưa.
    private boolean serverClosed = true;

    // Trạng thái chương trình console.
    private boolean running = true;

    /**
     * Khởi tạo ServerConsole.
     *
     * @param port cổng Server
     */
    public ServerConsole(int port) {

        server = new EchoServer(port);
    }

    /**
     * Hiển thị thông báo lên console.
     *
     * @param message nội dung cần hiển thị
     */
    @Override
    public void display(String message) {

        System.out.println(message);
    }

    /**
     * Xử lý dữ liệu do người quản trị nhập.
     *
     * @param message nội dung nhập
     */
    public void handleMessageFromServerUI(String message) {

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        message = message.trim();

        if (message.startsWith("#")) {
            handleCommand(message);
            return;
        }

        if (serverClosed) {

            display("Server is closed. Use #start first.");
            return;
        }

        String serverMessage = "[SERVER] " + message;

        display(serverMessage);

        server.sendToAllClients(serverMessage);
    }

    /**
     * Xử lý các lệnh điều khiển Server.
     *
     * @param command lệnh quản trị
     */
    private void handleCommand(String command) {

        String[] parts = command.trim().split("\\s+", 2);

        String action = parts[0].toLowerCase();

        try {

            switch (action) {

                case "#quit":

                    quitServer();
                    break;

                case "#stop":

                    if (serverClosed) {

                        display("Server is already closed.");

                    } else if (!server.isListening()) {

                        display("Server is already stopped.");

                    } else {

                        server.stopListening();

                        display(
                                "Server stopped accepting new clients."
                        );
                    }

                    break;

                case "#close":

                    closeServer();
                    break;

                case "#start":

                    startServer();
                    break;

                case "#setport":

                    changePort(parts);
                    break;

                case "#getport":

                    display(
                            "Current port: " + server.getPort()
                    );

                    break;

                default:

                    display("Unknown command: " + action);

                    display("Available commands:");
                    display("#start, #stop, #close, #quit");
                    display("#setport <port>, #getport");

                    break;
            }

        } catch (IOException e) {

            display("Server error: " + e.getMessage());

        } catch (NumberFormatException e) {

            display("Invalid port number.");
        }
    }

    /**
     * Bắt đầu cho Server lắng nghe kết nối.
     */
    private void startServer() throws IOException {

        if (!serverClosed && server.isListening()) {

            display("Server is already running.");
            return;
        }

        server.listen();

        serverClosed = false;

        display("Starting server on port " + server.getPort());
    }

    /**
     * Ngừng Server và đóng tất cả kết nối Client.
     */
    private void closeServer() throws IOException {

        if (serverClosed) {

            display("Server is already closed.");
            return;
        }

        server.close();

        serverClosed = true;

        display("Server and all client connections are closed.");
    }

    /**
     * Thay đổi Port của Server.
     *
     * Chỉ cho phép thay đổi khi Server đã đóng
     * và không còn Client kết nối.
     *
     * @param parts các thành phần của lệnh
     */
    private void changePort(String[] parts) {

        if (parts.length < 2) {

            display("Usage: #setport <port>");
            return;
        }

        if (!serverClosed) {

            display("Please use #close before changing the port.");
            return;
        }

        if (server.getNumberOfClients() > 0) {

            display("Cannot change port while clients are connected.");
            return;
        }

        int port = Integer.parseInt(parts[1].trim());

        if (port < 1 || port > 65535) {

            display("Port must be between 1 and 65535.");
            return;
        }

        server.setPort(port);

        display("Server port changed to: " + server.getPort());
    }

    /**
     * Đóng Server và kết thúc chương trình.
     */
    private void quitServer() throws IOException {

        if (!serverClosed) {
            server.close();
            serverClosed = true;
        }

        display("Server application terminated.");

        running = false;
    }

    /**
     * Khởi chạy chương trình Server.
     *
     * Cách sử dụng:
     * ServerConsole [port]
     *
     * @param args tham số dòng lệnh
     */
    public static void main(String[] args) {

        int port = DEFAULT_PORT;

        try {

            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            if (port < 1 || port > 65535) {

                System.out.println("Invalid port number.");
                return;
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid port number.");
            return;
        }

        ServerConsole console = new ServerConsole(port);

        try {

            console.startServer();

        } catch (IOException e) {

            console.display(
                    "Cannot start server: " + e.getMessage()
            );

            return;
        }

        console.display("SimpleChat Server Console started.");
        console.display("Enter #quit to terminate Server.");

        Scanner scanner = new Scanner(System.in);

        while (console.running) {

            String message = scanner.nextLine();

            console.handleMessageFromServerUI(message);
        }
    }
}
