# HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM - TÀI LIỆU ĐẶC TẢ CHỨC NĂNG

---

## 0. MÀN HÌNH ĐĂNG NHẬP (LoginFrame)

### Đăng nhập hệ thống (System Login)
*   **Mục đích:** Cổng bảo mật đầu tiên, yêu cầu xác thực danh tính trước khi truy cập.
*   **Giao diện:**
    *   Ô nhập **Username** và **Password** (ẩn ký tự).
    *   Nút **Login** kích hoạt quá trình kiểm tra thông tin.
    *   **DB Mode:** Tùy chọn database ở **Cloud** hoặc **Local** (khuyên dùng).
*   **Cơ chế phân quyền:**
    *   **Phân quyền tự động:** Admin thấy toàn bộ menu, Staff chỉ thấy menu Home, Booking, Concessions.

### Kết nối nâng cao (Advanced Connection)
*   **Dual-Database:** Hỗ trợ song song Localhost và Cloud Server.
*   **Hot-swap:** Click để chuyển đổi nguồn dữ liệu tức thì khi gặp sự cố mạng.

> **Tóm tắt chức năng:** Đảm bảo an ninh hệ thống và tính sẵn sàng cao, cho phép hoạt động liên tục ngay cả khi mất mạng internet (nhờ chế độ Offline/Local).

---

## 1. TRANG CHỦ (Home - StatsPanel)
*   Hiển thị tổng quan các chỉ số kinh doanh (Doanh thu, số vé bán ra...).

---

## 2. TRANG BÁN VÉ (Booking - BanVePanel)

### Khu vực chọn phim (Movie Selection)
*   **Tìm kiếm thông minh:** Thanh search cho phép lọc phim theo tên.
*   **Bộ lọc ngày (Date Spinner):** Cho phép bán vé cho các suất chiếu tương lai.
*   **Danh sách suất chiếu:** Hiển thị tóm tắt (Tên phim - Giờ chiếu - Tên rạp).

### Sơ đồ ghế trực quan (Seat Map)
*   **Hiển thị:** Lưới ghế mô phỏng chính xác sơ đồ thực tế của phòng chiếu.
*   **Mã màu trạng thái (Legend):**
    *   ⚪ **Standard (Xám):** Ghế thường giá tiêu chuẩn.
    *   🟡 **VIP (Vàng):** Ghế vị trí đẹp, phụ thu thêm tiền.
    *   🌸 **Double (Hồng):** Ghế đôi dành cho cặp đôi.
    *   🔴 **Booked (Đỏ):** Ghế đã bán, bị khóa không thể chọn.
    *   🟢 **Selected (Xanh lá):** Ghế đang được chọn.

### Thanh toán (Checkout & Payment)
*   **Giỏ hàng:** Tự động cộng dồn tiền Vé và Bắp nước.
*   **Khuyến mãi tự động:**
    *   Hệ thống tự quét ngày chiếu để áp dụng giảm giá (VD: Thứ 2 giảm 30%).
    *   Hiển thị dòng thông báo: `Active Promo: [Tên chương trình]`.
*   **Tích điểm (Loyalty):** Tìm khách theo SĐT và hỗ trợ trừ điểm thưởng.

> **Tóm tắt chức năng:** Module quan trọng nhất, nơi nhân viên thực hiện toàn bộ quy trình bán hàng: Tư vấn chỗ ngồi, bán kèm đồ ăn và in vé cho khách.

---

## 3. QUẢN LÝ BẮP NƯỚC (Concessions - ConcessionPanel)

### Quản lý thực đơn (Menu Management)
*   Quản lý danh sách các món ăn uống: Bắp rang, Nước ngọt, Combo.
*   Cho phép cập nhật giá bán (Selling Price) và hình ảnh minh họa.

### Cấu hình Combo (Combo Configuration)
*   Tạo các gói Combo (VD: 1 Bắp + 2 Nước) để kích cầu.
*   Hệ thống tự động trừ kho các món lẻ khi bán 1 Combo.

> **Tóm tắt chức năng:** Quản lý mảng F&B (Food & Beverage), giúp tối ưu hóa nguồn thu phụ trợ bên cạnh doanh thu bán vé.

---

## 4. QUẢN LÝ PHIM (Movies - MoviePanel)

### Cơ sở dữ liệu phim (Movie Database)
*   Nơi nhập liệu thông tin phim: Tên, Thể loại, Thời lượng, Đạo diễn.
*   Quản lý trạng thái phim: Đang chiếu (Now Showing) hoặc Sắp chiếu.

### Quản lý hình ảnh (Media Assets)
*   **Upload Poster:** Tính năng chọn ảnh từ máy tính để làm ảnh bìa đẹp mắt cho phim.
*   Hình ảnh này sẽ được đồng bộ hiển thị ở trang Bán vé và Xếp lịch.

> **Tóm tắt chức năng:** Đây là kho dữ liệu trung tâm. Phim phải được khai báo tại đây trước thì mới có thể tiến hành xếp lịch chiếu.

---

## 5. XẾP LỊCH CHIẾU (Schedule - SchedulePanel)

### Giao diện kéo thả (Drag & Drop Interface)
*   **Thao tác trực quan:** Kéo Poster phim từ danh sách thả vào dòng thời gian (Timeline).
*   Trục thời gian ngang giúp dễ dàng quan sát lịch trình của từng phòng.

### Logic xếp lịch (Schedule Logic)
*   **Tự động tính giờ:** Tự cộng thêm thời lượng phim + 15 phút dọn dẹp để ra giờ kết thúc.
*   **Chặn trùng lịch:** Hệ thống tự động báo lỗi nếu xếp đè lên suất chiếu khác.

> **Tóm tắt chức năng:** Công cụ giúp người quản lý sắp xếp hàng trăm suất chiếu mỗi tuần một cách chính xác, loại bỏ hoàn toàn sai sót trùng giờ.

---

## 6. QUẢN LÝ PHÒNG (Rooms - RoomPanel)

### Thiết kế sơ đồ phòng (Room Layout)
*   Cho phép định nghĩa kích thước phòng: Số hàng x Số cột (VD: 10x15).
*   Đặt tên phòng chiếu (Cinema 1, IMAX...).

### Cấu hình ghế (Seat Configuration)
*   Click vào từng ô trên lưới để gán loại ghế: Thường, VIP, Đôi.
*   Thiết lập giá vé cơ bản (Base Price) cho từng phòng.

> **Tóm tắt chức năng:** Số hóa bản đồ vật lý của rạp vào phần mềm, làm cơ sở cho tính năng bán vé chọn chỗ.

---

## 7. QUẢN LÝ NHÂN VIÊN (Staff - EmployeePanel)

### Hồ sơ nhân sự (Staff Profiles)
*   Lưu trữ thông tin nhân viên: Họ tên, Ngày sinh, Số điện thoại.
*   Danh sách hiển thị dạng bảng dễ tra cứu.

### Quản lý tài khoản (Account Management)
*   Cấp Tên đăng nhập và Mật khẩu truy cập hệ thống.
*   **Phân quyền (Roles):** Chọn vai trò Quản lý hoặc Nhân viên bán vé.

> **Tóm tắt chức năng:** Giúp kiểm soát an ninh hệ thống và quản lý danh sách nhân sự đang làm việc tại rạp.

---

## 8. CA LÀM VIỆC (Shifts - ShiftPanel)

### Bảng phân ca (Shift Planner)
*   Giao diện lịch tuần trực quan (Thứ 2 - Chủ Nhật).
*   **Thao tác:** Gán nhân viên vào các ca làm việc (Sáng/Chiều/Tối).

### Dự tính lương (Payroll Estimate)
*   Hệ thống ước tính chi phí lương dựa trên số ca làm việc đã phân công.

> **Tóm tắt chức năng:** Thay thế quy trình phân ca thủ công, giúp theo dõi lịch làm việc của nhân viên dễ dàng.

---

## 9. KHÁCH HÀNG THÂN THIẾT (Loyalty - LoyaltyPanel)

### Cơ sở dữ liệu khách hàng (Member Database)
*   Lưu trữ thông tin liên lạc và điểm đã tích để chăm sóc khách hàng (CRM).

### Xếp hạng tự động (Auto-Tiering)
*   Hệ thống tự động nâng hạng thẻ (Bronze -> Gold -> Platinum).
*   Căn cứ vào tổng chi tiêu tích lũy của khách hàng.

> **Tóm tắt chức năng:** Công cụ giúp giữ chân khách hàng (Retention), khuyến khích họ quay lại rạp thông qua các ưu đãi hạng thẻ.

---

## 10. CHÍNH SÁCH GIÁ (Policies - DiscountPanel)

### Chính sách nhân khẩu học (Demographic Policies)
*   Quản lý các chính sách giảm giá dựa trên đối tượng khách hàng (ví dụ: Học sinh, Sinh viên, Người cao tuổi...).
*   Cho phép thêm, sửa, xóa các loại giảm giá theo % hoặc số tiền cố định.

### Khuyến mãi hàng tuần (Weekly Promotions)
*   **Mục đích:** Quản lý lịch khuyến mãi tự động theo các ngày trong tuần và các ngày lễ đặc biệt.
*   **Giao diện:**
    *   Mỗi ngày được hiển thị dưới dạng một thẻ ("MON", "TUE", "WED"...).
    *   Màu đỏ đậm thể hiện ngày đó đang có khuyến mãi kích hoạt (`Active`).
    *   Nút gạt (Switch) trên thẻ cho phép bật/tắt nhanh khuyến mãi của ngày đó.
*   **Chỉnh sửa chi tiết (Edit Promotion):**
    *   Khi bấm vào một thẻ ngày, cột bên phải sẽ hiển thị chi tiết để chỉnh sửa.
    *   **Promotion Name:** Tên chương trình (ví dụ: "Monday Special").
    *   **Discount Value:** Giá trị giảm (ví dụ: 30).
    *   **Type:** Loại giảm (Percent % hoặc Fixed Amount).
    *   Nút **Save Changes** để lưu lại cấu hình vào cơ sở dữ liệu.

> **Tóm tắt chức năng:** Trang này giúp người quản lý (Admin) thiết lập hệ thống khuyến mãi linh hoạt. Hệ thống sẽ tự động áp dụng các khuyến mãi này dựa trên ngày hiện tại khi nhân viên bán vé.

---

## 11. BÁO CÁO (Reports - ReportPanel)

### Phân tích doanh thu (Revenue Analytics)
*   **Chế độ xem:** Tùy chọn xem chi tiết theo Ngày (Daily) hoặc tổng hợp Tháng (Monthly).
*   **Biểu đồ:** So sánh tỷ trọng doanh thu Vé vs Bắp nước.

### Xuất dữ liệu (Data Export)
*   **Tính năng:** Xuất toàn bộ báo cáo ra file Excel (.xls).
*   **Nội dung:** Bao gồm bảng tổng hợp metrics và log chi tiết từng giao dịch.

> **Tóm tắt chức năng:** Công cụ hỗ trợ kế toán, minh bạch hóa dòng tiền và cung cấp số liệu chính xác để đánh giá hiệu quả kinh doanh.

---

# HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY HỆ THỐNG CINEMA MANAGER

Tài liệu này hướng dẫn chi tiết các bước để thiết lập môi trường và chạy ứng dụng Cinema Manager trên máy tính Windows.

### 1. YÊU CẦU HỆ THỐNG (PREREQUISITES)
Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các phần mềm sau:
*   **Java Development Kit (JDK):**
    *   Phiên bản yêu cầu: JDK 17 hoặc mới hơn.
    *   Kiểm tra: Mở CMD và gõ `java -version`. Nếu hiện java version "17..." là đạt yêu cầu.
*   **MySQL Server:**
    *   Phiên bản: MySQL 8.0+.
    *   Công cụ quản lý: MySQL Workbench (khuyến nghị).

### 2. CÀI ĐẶT CƠ SỞ DỮ LIỆU (DATABASE SETUP)
Hệ thống cần có dữ liệu để hoạt động. Bạn có 2 cách để kết nối:

#### Cách 1: Sử dụng Cloud Database (Nhanh nhất)
*   Hệ thống đã được cấu hình sẵn để kết nối tới Cloud Server (Aiven).
*   **Ưu điểm:** Không cần cài MySQL trên máy.
*   **Lưu ý:** Cần có kết nối Internet ổn định.
*   **Thao tác:** Ở màn hình đăng nhập, khuyến nghị chọn DB Mode là **Local**, nếu local lỗi có thể click để đổi qua **Cloud**.

#### Cách 2: Cài đặt Local Database (Chạy Offline)
Nếu bạn dùng XAMPP/WAMP hoặc Hosting có sẵn phpMyAdmin:
1.  Truy cập phpMyAdmin (thường là `http://localhost/phpmyadmin`).
2.  Nhấn **New (Mới)** ở thanh bên trái để tạo Database.
3.  Đặt tên là `cinema_db` và chọn mã hóa `utf8mb4_general_ci`, nhấn **Create**.
4.  Chọn Database `cinema_db` vừa tạo.
5.  Vào tab **Import (Nhập)**.
6.  Nhấn **Choose File** và chọn file `cinema_db.sql` trong thư mục `CinemaManager_Release/DB`.
7.  Nhấn **Import (hoặc Go)** ở cuối trang.

### 3. CÁCH CHẠY CHƯƠNG TRÌNH
Chúng tôi đã tạo sẵn script tự động để bạn không cần dùng IDE phức tạp.

#### Bước 1: Tìm file chạy
Truy cập vào thư mục: `CinemaManager_Release` hoặc thư mục gốc `CinemaManager`.

#### Bước 2: Khởi động
*   Tìm file có tên: `RunApp.bat` (hoặc `RunApp`).
*   **Double-click (Nhấn đúp chuột)** vào file này.
*   Một cửa sổ đen (Console) sẽ hiện lên để biên dịch code. **ĐỪNG TẮT NÓ**.
*   Sau vài giây, màn hình Đăng nhập (Login) sẽ xuất hiện.

#### Bước 3: Đăng nhập
Sử dụng các tài khoản mặc định sau để test:

| Vai Trò | Tài Khoản | Mật Khẩu | Quyền Hạn |
| :--- | :--- | :--- | :--- |
| **Quản Lý** | `admin` | `123456` | Toàn quyền hệ thống |
| **Nhân Viên** | `nv01` | `123456` | Chỉ bán vé và quản lý concessions |
