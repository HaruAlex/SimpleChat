 SimpleChat – OCSF
 1. Giới thiệu
SimpleChat là ứng dụng trò chuyện đơn giản được xây dựng bằng Java, sử dụng  OCSF 
(Object Client-Server Framework) để triển khai mô hình giao tiếp Client–Server.
Ứng dụng cho phép nhiều Client kết nối đến một Server, gửi và nhận tin nhắn trong thời gian thực.
Server đóng vai trò trung tâm tiếp nhận tin nhắn và phân phối nội dung đến các Client đang kết nối.
 2. Công nghệ sử dụng
-  Ngôn ngữ:  Java
-  IDE:  Eclipse
-  Framework:  OCSF
-  Mô hình:  Client–Server
-  Giao thức:  TCP/IP
 3. Cấu trúc dự án
SimpleChat/
│
└── src/
    ├── com/
    │   └── lloseng/
    │       └── ocsf/
    │           ├── client/
    │           └── server/
    │
    └── simplechat/
        ├── ChatIF.java
        │
        ├── client/
        │   ├── ChatClient.java
        │   └── ClientConsole.java
        │
        └── server/
            ├── EchoServer.java
            └── ServerConsole.java
 4. Chức năng chính
 4.1. Server
- Khởi động và lắng nghe kết nối từ Client.
- Tiếp nhận tin nhắn từ các Client.
- Phân phối tin nhắn đến các Client đang kết nối.
- Theo dõi sự kiện Client kết nối và ngắt kết nối.
- Hỗ trợ quản lý trạng thái hoạt động của Server.
 4.2. Client
- Kết nối đến Server thông qua địa chỉ IP và cổng.
- Gửi tin nhắn đến Server.
- Nhận và hiển thị tin nhắn từ Server.
- Đăng nhập bằng tên người dùng.
- Ngắt kết nối và kết nối lại với Server.
 5. Hướng dẫn cài đặt và chạy chương trình
 Bước 1: Chuẩn bị
- Cài đặt JDK.
- Cài đặt Eclipse IDE.
- Chuẩn bị mã nguồn OCSF.
- Import project SimpleChat vào Eclipse.
 Bước 2: Chạy Server
1. Mở file "ServerConsole.java".
2. Chọn  Run As → Java Application .
3. Server mặc định sử dụng cổng "5555".
 Bước 3: Chạy Client
1. Mở file "ClientConsole.java".
2. Chọn  Run Configurations → Arguments .
3. Nhập các tham số:

Client 1:
user1 localhost 5555

Client 2:
user2 localhost 5555

4. Nhấn  Apply → Run  để khởi chạy từng Client.

Lưu ý: Cần chạy Server trước khi khởi chạy Client. Mỗi Client nên được chạy trong một tiến trình Java riêng.

 6. Các lệnh hỗ trợ
 6.1. Client
| Lệnh | Chức năng |
|---|---|
| `\#quit` | Thoát chương trình Client |
| `\#logoff` | Ngắt kết nối với Server |
| `\#login` | Kết nối lại với Server |
| `\#sethost` | Thay đổi địa chỉ Server |
| `\#setport` | Thay đổi cổng kết nối |
| `\#gethost` | Xem địa chỉ Server hiện tại |
| `\#getport` | Xem cổng kết nối hiện tại |
 6.2. Server
| Lệnh | Chức năng |
|---|---|
| `\#quit` | Thoát chương trình Server |
| `\#stop` | Dừng lắng nghe kết nối mới |
| `\#close` | Đóng Server |
| `\#start` | Khởi động lại Server |
| `\#setport` | Thay đổi cổng Server |
| `\#getport` | Xem cổng Server hiện tại |
Các tin nhắn thông thường được gửi từ Server sẽ được phân phối đến những Client đang kết nối.
 7. Kịch bản kiểm thử
 Test 1: Kết nối Client với Server
- Khởi chạy Server trên cổng 5555.
- Khởi chạy Client `user1`.
- Khởi chạy Client `user2`.
- Kiểm tra thông báo kết nối trên Console.
 Kết quả mong đợi:  Cả hai Client kết nối thành công đến Server.
 Test 2: Gửi và nhận tin nhắn
 user1 gửi: 
Hello user2!

 user2 nhận: 
user1: Hello user2!

 user2 gửi: 
Hi user1! How are you?

 user1 nhận: 
user2: Hi user1! How are you?

 Kết quả mong đợi:  Tin nhắn được gửi đến Server và phân phối đến các Client đang kết nối.

 Test 3: Kiểm tra nội dung tin nhắn

Thử gửi các nội dung:
123456789
Hello!!!
Good morning
Xin chào, bạn khỏe không?
This is a test message.

 Kết quả mong đợi:  Client nhận và hiển thị đúng nội dung tin nhắn.

 Test 4: Ngắt kết nối Client
- Cho "user2" thực hiện lệnh #logoff.
- "user1" tiếp tục gửi tin nhắn.
- Kiểm tra "user2" không còn nhận tin nhắn khi đã ngắt kết nối.

 Kết quả mong đợi:  Client ngắt kết nối không nhận được tin nhắn mới.

 Test 5: Kết nối lại Client
- Cho "user2" kết nối lại với Server.
- Gửi tin nhắn từ "user2" đến "user1".

 Kết quả mong đợi:  "user2" kết nối thành công và tiếp tục trao đổi tin nhắn.
 8. Kết luận
Thông qua bài tập, nhóm hiểu rõ hơn về mô hình Client–Server và cách sử dụng OCSF để xây dựng
ứng dụng giao tiếp bằng Java. Nhóm đã thực hành triển khai Server, Client, xử lý kết nối, gửi và nhận
tin nhắn, đồng thời kiểm thử hoạt động của nhiều Client trên cùng một Server.
Qua đó, nhóm củng cố kiến thức về lập trình mạng, xử lý kết nối và trao đổi dữ liệu giữa các tiến trình.