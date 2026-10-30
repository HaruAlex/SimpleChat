package simplechat.client;

import java.io.IOException;

import com.lloseng.ocsf.client.AbstractClient;

import simplechat.ChatIF;

/**
 * Lớp ChatClient quản lý kết nối từ Client đến Server.
 *
 * Sử dụng AbstractClient của OCSF để thực hiện
 * giao tiếp Client-Server thông qua TCP.
 */
public class ChatClient extends AbstractClient {

    private final ChatIF clientUI;
    private final String loginId;

    private volatile boolean manualDisconnect;
    private volatile boolean shuttingDown;

    /**
     * Khởi tạo ChatClient.
     *
     * @param clientUI giao diện hiển thị thông báo
     * @param loginId tên người dùng
     * @param host địa chỉ Server
     * @param port cổng Server
     */
    public ChatClient(ChatIF clientUI, String loginId,
                      String host, int port) {

        super(host, port);

        this.clientUI = clientUI;
        this.loginId = loginId;

        this.manualDisconnect = false;
        this.shuttingDown = false;
    }

    /**
     * Xử lý thông điệp nhận được từ Server.
     *
     * @param msg thông điệp từ Server
     */
    @Override
    protected void handleMessageFromServer(Object msg) {

        clientUI.display(String.valueOf(msg));
    }

    /**
     * Được OCSF gọi khi kết nối với Server thành công.
     */
    @Override
    protected void connectionEstablished() {

        manualDisconnect = false;

        clientUI.display("Connected to server "
                + getHost() + ":" + getPort());

        try {
            sendToServer("#login " + loginId);
        } catch (IOException e) {
            clientUI.display("Cannot send login information: "
                    + e.getMessage());
        }
    }

    /**
     * Được OCSF gọi khi kết nối bị đóng.
     */
    @Override
    protected void connectionClosed() {

        clientUI.display("Connection to server has been closed.");

        if (!manualDisconnect && !shuttingDown) {
            terminateClient();
        }
    }

    /**
     * Được OCSF gọi khi xảy ra lỗi trong quá trình giao tiếp.
     *
     * @param exception lỗi kết nối
     */
    @Override
    protected void connectionException(Exception exception) {

        clientUI.display("Connection error: "
                + exception.getMessage());

        if (!manualDisconnect && !shuttingDown) {
            terminateClient();
        }
    }

    /**
     * Đăng nhập hoặc kết nối lại Server.
     *
     * @throws IOException nếu không thể kết nối
     */
    public void login() throws IOException {

        if (isConnected()) {
            clientUI.display("You are already connected.");
            return;
        }

        manualDisconnect = false;
        shuttingDown = false;

        openConnection();
    }

    /**
     * Đăng xuất khỏi Server nhưng không kết thúc chương trình.
     */
    public void logoff() {

        if (!isConnected()) {
            clientUI.display("You are not connected.");
            return;
        }

        manualDisconnect = true;

        try {
            closeConnection();
            clientUI.display("You have logged off.");
        } catch (IOException e) {
            clientUI.display("Cannot close connection: "
                    + e.getMessage());
        }
    }

    /**
     * Thoát ứng dụng Client.
     */
    public void quit() {

        shuttingDown = true;
        manualDisconnect = true;

        if (isConnected()) {
            try {
                closeConnection();
            } catch (IOException e) {
                clientUI.display("Error closing connection: "
                        + e.getMessage());
            }
        }

        clientUI.display("Client application terminated.");
        System.exit(0);
    }

    /**
     * Kết thúc Client khi Server đóng kết nối bất ngờ.
     */
    private void terminateClient() {

        if (!shuttingDown) {
            shuttingDown = true;

            clientUI.display(
                    "The server is unavailable. Client will terminate."
            );

            System.exit(0);
        }
    }
}
