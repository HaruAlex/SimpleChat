package simplechat.server;

import com.lloseng.ocsf.server.AbstractServer;
import com.lloseng.ocsf.server.ConnectionToClient;

/**
 * Server của ứng dụng SimpleChat.
 *
 * Kế thừa AbstractServer của OCSF để quản lý
 * kết nối và trao đổi thông điệp với nhiều Client.
 */
public class EchoServer extends AbstractServer {

    private static final String LOGIN_KEY = "loginId";

    /**
     * Khởi tạo EchoServer.
     *
     * @param port cổng Server
     */
    public EchoServer(int port) {

        super(port);
    }

    /**
     * Xử lý thông điệp nhận từ Client.
     *
     * @param msg thông điệp nhận được
     * @param client kết nối gửi thông điệp
     */
    @Override
    protected void handleMessageFromClient(
            Object msg, ConnectionToClient client) {

        String message = String.valueOf(msg).trim();

        if (message.isEmpty()) {
            return;
        }

        // Xử lý đăng nhập.
        if (message.startsWith("#login ")) {

            String loginId = message.substring(7).trim();

            if (loginId.isEmpty()) {
                sendToClient(client, "[SERVER] Invalid login ID.");
                return;
            }

            // Lưu Login ID vào kết nối của Client.
            client.setInfo(LOGIN_KEY, loginId);

            System.out.println(loginId + " has logged in.");

            sendToAllClients(
                    "[SERVER] " + loginId + " has joined the chat."
            );

            return;
        }

        // Lấy Login ID của Client.
        Object info = client.getInfo(LOGIN_KEY);

        if (info == null) {

            sendToClient(
                    client,
                    "[SERVER] Please login before sending messages."
            );

            return;
        }

        String loginId = info.toString();

        // Xử lý lệnh đăng xuất nếu Client gửi Logout.
        if (message.equalsIgnoreCase("#logout")) {

            try {

                client.sendToClient(
                        "[SERVER] You have logged out."
                );

                client.close();

            } catch (Exception e) {

                System.out.println(
                        "Error logging out client: " + e.getMessage()
                );
            }

            return;
        }

        // Định dạng và phân phối tin nhắn.
        String formattedMessage = loginId + ": " + message;

        System.out.println(formattedMessage);

        sendToAllClients(formattedMessage);
    }

    /**
     * Gửi thông điệp đến một Client cụ thể.
     *
     * @param client Client nhận thông điệp
     * @param message nội dung thông điệp
     */
    private void sendToClient(
            ConnectionToClient client, Object message) {

        try {

            client.sendToClient(message);

        } catch (Exception e) {

            System.out.println(
                    "Cannot send message to client: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Được OCSF gọi khi có Client kết nối.
     *
     * @param client kết nối vừa được thiết lập
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {

        System.out.println(
                "Client connected: " + client.getInetAddress()
        );
    }

    /**
     * Được OCSF gọi khi Client ngắt kết nối.
     *
     * @param client kết nối vừa đóng
     */
    @Override
    protected synchronized void clientDisconnected(
            ConnectionToClient client) {

        Object info = client.getInfo(LOGIN_KEY);

        String loginId = info == null
                ? "Unknown client"
                : info.toString();

        System.out.println(loginId + " disconnected.");

        sendToAllClients(
                "[SERVER] " + loginId + " has left the chat."
        );
    }

    /**
     * Xử lý lỗi xảy ra trong kết nối Client.
     *
     * @param client Client phát sinh lỗi
     * @param exception lỗi phát sinh
     */
    @Override
    protected synchronized void clientException(
            ConnectionToClient client, Throwable exception) {

        System.out.println(
                "Client communication error: "
                        + exception.getMessage()
        );
    }

    /**
     * Được OCSF gọi khi Server bắt đầu lắng nghe.
     */
    @Override
    protected void serverStarted() {

        System.out.println(
                "Server started on port " + getPort()
        );
    }

    /**
     * Được OCSF gọi khi Server ngừng lắng nghe.
     */
    @Override
    protected void serverStopped() {

        System.out.println("Server stopped listening.");
    }

    /**
     * Được OCSF gọi khi Server đóng hoàn toàn.
     */
    @Override
    protected void serverClosed() {

        System.out.println("Server closed.");
    }

    /**
     * Được OCSF gọi khi xảy ra lỗi trong quá trình lắng nghe.
     *
     * @param exception lỗi phát sinh
     */
    @Override
    protected void listeningException(Throwable exception) {

        System.out.println(
                "Server listening error: " + exception.getMessage()
        );
    }
}
