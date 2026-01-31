ĐẠI HỌC QUỐC GIA TP. HỒ CHÍ MINH
TRƯỜNG ĐẠI HỌC CÔNG NGHỆ THÔNG TIN
TRUNG TÂM PHÁT TRIỂN CNTT

BÁO CÁO ĐỒ ÁN MÔN HỌC
**HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM (CINEMA MANAGER)**

**MÔN HỌC:** LẬP TRÌNH ỨNG DỤNG JAVA (JAVA WINDOWS FORM)
**GIẢNG VIÊN:** [Tên Giảng Viên]

**Nhóm [Số Nhóm]**
**Thành viên:**
*   Họ tên: [Tên Thành Viên 1]      MSSV: [MSSV]
*   Họ tên: [Tên Thành Viên 2]      MSSV: [MSSV]
*   Họ tên: [Tên Thành Viên 3]      MSSV: [MSSV]

---

# TÀI LIỆU ĐẶC TẢ & HƯỚNG DẪN SỬ DỤNG

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

## 12. KIẾN TRÚC HỆ THỐNG (SYSTEM ARCHITECTURE)

### 12.1. Kiến trúc MVC
*   **Models:** `Phim`, `NhanVien`, `Ve`, `Ghe`...
*   **Controllers:** Xử lý sự kiện trong View hoặc Controller riêng.
*   **Views:** Các `JPanel` giao diện người dùng.

### 12.2. Luồng xử lý
User Action -> Controller -> Service/DAO -> Database -> View Update.

### 12.3. Công nghệ sử dụng
*   **Backend:** Java Core (JDK 17).
*   **Database:** MySQL 8.0, JDBC.
*   **Frontend:** Java Swing, Graphics2D.
*   **Tools:** Git, NetBeans/IntelliJ/VSCode, Maven.

### 12.4. Tính năng kỹ thuật nổi bật
*   **Dual-Database:** Hỗ trợ hoạt động Offline và Online (Localhost/Cloud).
*   **Async Loading:** Sử dụng `CompletableFuture` để tải dữ liệu lớn (Sơ đồ ghế, Danh sách phim) mà không làm treo giao diện.
*   **Custom Painting:** Tự vẽ các component phức tạp (Sơ đồ ghế, Timeline) bằng `Graphics2D`.

---

## 13. HƯỚNG DẪN CÀI ĐẶT (INSTALLATION)

### 13.1. Yêu cầu hệ thống
*   **Java JDK:** Phiên bản 17 hoặc mới hơn.
*   **MySQL Server:** Phiên bản 8.0+.

### 13.2. Cài đặt Cơ sở dữ liệu
**Cách 1: Sử dụng Cloud (Khuyên dùng)**
*   Phần mềm đã cấu hình sẵn kết nối đám mây. Bạn chỉ cần chạy ứng dụng và chọn chế độ mạng nếu cần.

**Cách 2: Chạy Offline (Localhost)**
1.  Mở MySQL Workbench hoặc phpMyAdmin.
2.  Tạo database tên `cinema_db`.
3.  Import file `cinema_db.sql` từ thư mục `DB` của project.

### 13.3. Cách chạy chương trình
1.  Tìm file `RunApp.bat` trong thư mục dự án.
2.  Double-click để khởi chạy.
3.  **Lưu ý:** Giữ nguyên cửa sổ Command Prompt (màn hình đen) chạy nền.

---

## 14. HƯỚNG DẪN SỬ DỤNG CHI TIẾT (USER MANUAL)

# SỔ TAY HƯỚNG DẪN SỬ DỤNG CHI TIẾT
**Phần mềm quản lý rạp chiếu phim (Cinema Manager)**

---

## PHẦN 1: KHỞI ĐỘNG & ĐĂNG NHẬP

### Bước 1: Khởi động chương trình
1.  Tìm thư mục chứa phần mềm (thường ở `D:\CinemaManager` hoặc ngoài Desktop).
2.  Tìm file có tên **`RunApp`** (biểu tượng bánh răng hoặc file .bat).
3.  **Click đúp chuột** vào file này.
4.  *Lưu ý quan trọng:* Một màn hình màu đen (Command Prompt) sẽ hiện lên trước. **TUYỆT ĐỐI KHÔNG TẮT MÀN HÌNH ĐEN NÀY**, nếu tắt thì phần mềm sẽ đóng theo. Hãy để nó chạy ẩn bên dưới.

### Bước 2: Đăng nhập hệ thống
Sau khoảng 5-10 giây, màn hình Đăng nhập màu xanh đen sẽ hiện ra.
*   **Nhập thông tin:**
    *   **Quản lý (Manager):**
        *   Tài khoản: `admin`
        *   Mật khẩu: `123456`
    *   **Nhân viên (Staff):**
        *   Tài khoản: `nv01`
        *   Mật khẩu: `123456`
*   **Chọn Chế độ (DB Mode):**
    *   Luôn ưu tiên chọn **LOCAL** (Mặc định) để phần mềm chạy nhanh nhất và không phụ thuộc vào mạng Internet.
    *   Chỉ chọn **CLOUD** khi Quản lý yêu cầu đồng bộ dữ liệu.
*   Nhấn nút **LOGIN** hoặc phím **Enter** để vào.

---

## PHẦN 2: QUY TRÌNH BÁN VÉ (Dành cho Nhân viên Thu ngân)
*Đây là quy trình lặp lại hằng ngày.*

### 2.1. Chấm công (Timekeeping)
*Bắt buộc phải check-in khi bắt đầu ca và check-out khi ra về.*
1.  Đăng nhập bằng tài khoản cá nhân.
2.  Hệ thống tự động ghi nhận giờ **Check-in** ngay khi bạn đăng nhập thành công.
3.  Khi hết ca, nhấn nút **Logout** hoặc tắt phần mềm. Hệ thống sẽ ghi nhận giờ **Check-out**.
    *   *Lưu ý:* Nếu quên Logout, hệ thống sẽ tính giờ làm đến tận nửa đêm hoặc lần đăng nhập sau, dẫn đến sai lương.

### 2.2. Đăng ký thành viên mới (Loyalty Registration)
*Khi khách hàng muốn làm thẻ thành viên.*
1.  Vào menu **Loyalty**.
2.  Nhập thông tin khách:
    *   **Họ tên:** (Bắt buộc)
    *   **SĐT:** (Bắt buộc - dùng làm mã số thẻ)
    *   **Email:** (Nếu có)
3.  Nhấn nút **Register New Member**.
4.  Thông báo "Success" hiện ra. Khách hàng bắt đầu là hạng `Standard` (0 điểm).

### 2.3. Bán vé xem phim (Booking)
**Bước 1: Chọn Phim & Suất chiếu**
1.  Nhìn vào cột bên trái màn hình **Booking**.
2.  Click chọn **Ngày chiếu** (Hôm nay, Ngày mai...).
3.  Danh sách Poster phim sẽ hiện ra bên dưới. Click chọn hình **Poster phim** khách muốn xem.
4.  Bên cạnh poster sẽ hiện ra các khung giờ (Ví dụ: 09:00, 14:30). Click chọn **Giờ chiếu** phù hợp.

**Bước 2: Chọn Ghế ngồi**
Sơ đồ ghế sẽ hiện ra ở giữa màn hình.
*   **Cách đọc màu:**
    *   ⬜ **Màu Xám:** Ghế trống (Có thể bán).
    *   🟥 **Màu Đỏ:** Ghế đã bán (Không thể chọn).
    *   🟦 **Màu Xanh:** Ghế bạn đang chọn cho khách này.
*   **Thao tác:** Click chuột trái vào ghế khách chọn (Ví dụ: E5, E6). Ghế sẽ chuyển sang màu Xanh.
*   *Nếu chọn nhầm:* Click lại vào ghế đó một lần nữa để bỏ chọn.

**Bước 3: Chọn Bắp nước (Upsell)**
Nhìn xuống thanh công cụ **Quick Add** nằm ngang dưới cùng.
*   Click nút **🍿 Popcorn**: Để thêm 1 Bắp. Click nhiều lần để tăng số lượng.
*   Click nút **🥤 Coke**: Để thêm 1 Nước ngọt.
*   *Mẹo:* Nếu khách muốn mua Combo lớn, hãy bấm vào tab **Concessions** ở menu trên cùng để có nhiều lựa chọn hơn.

**Bước 4: Thanh toán & In vé**
1.  Nhìn sang bảng **Order Summary** bên phải cùng.
2.  Kiểm tra lại kỹ với khách: *"Anh/Chị xem phim Đào, Phở và Piano suất 14:30, 2 ghế E5 E6, 1 Bắp đúng không ạ?"*
3.  **Tích điểm (Nếu khách yêu cầu):**
    *   Hỏi: *"Anh/Chị có thẻ thành viên không ạ?"*
    *   Nhập **Số điện thoại** của khách vào ô `Member Search` và nhấn Enter.
    *   Nếu đúng số, Tên khách và hạng thẻ (Gold/Platinum) sẽ hiện ra. Hệ thống tự động giảm giá nếu khách là VIP.
4.  Nhấn nút **CHECKOUT** (Màu đỏ to nhất).
5.  Thông báo "Payment Successful" hiện ra -> Hoàn tất.

### 2.4. Xử lý các tình huống thường gặp
*   **Khách muốn đổi ghế:** Bỏ chọn ghế cũ (Click lại thành màu xám) và chọn ghế mới.
*   **Khách đổi ý không mua nữa:** Nhấn nút **Clear** (Hình thùng rác) để xóa trắng đơn hàng.
*   **Ghế màu đỏ nhưng thực tế trong rạp trống:** Có thể do ca trước bán nhầm. Hãy gọi Quản lý check lại.

---

## PHẦN 3: QUY TRÌNH QUẢN LÝ (Dành cho Manager)
*Các công việc cấu hình hệ thống.*

### 3.1. Quản lý Nhân sự (Menu: STAFF)
*Nơi thêm, sửa, xóa tài khoản nhân viên.*
1.  Vào menu **Staff**.
2.  **Thêm nhân viên mới:**
    *   Nhập các thông tin: Tên, Email, SĐT.
    *   **Role (Vai trò):** Chọn `Staff` (Bán hàng) hoặc `Manager` (Quản lý).
    *   **Hourly Wage:** Nhập lương theo giờ (VD: 20000).
    *   Nhấn **Add**. Mật khẩu mặc định sẽ là `123456`.
3.  **Khóa tài khoản (Lock Account):**
    *   Khi nhân viên nghỉ việc, tìm tên họ trong danh sách.
    *   Click cột **Status** để chuyển từ `Active` (Xanh) sang `Inactive` (Đỏ). Nhân viên đó sẽ không đăng nhập được nữa.

### 3.2. Quản lý Phim (Menu: MOVIES)
*Tạo mới và cập nhật trạng thái phim.*
1.  Vào menu **Movies**.
2.  Nhấn nút hình dấu cộng **(+)** hoặc nút **Add**.
3.  Điền thông tin:
    *   **Title:** Tên phim tiếng Việt.
    *   **Duration:** Thời lượng (phút).
    *   **Status:** Chọn `Active` (Nếu đang chiếu) hoặc `Coming Soon` (Sắp chiếu).
4.  **Tải ảnh bìa:** Nhấn nút `Upload Poster` -> Chọn file ảnh `.jpg` hoặc `.png` từ máy tính.
5.  Nhấn **Save**.

### 3.3. Xếp lịch chiếu (Menu: SCHEDULE)
*Rất quan trọng - Cần làm hàng tuần.*
1.  Vào menu **Schedule**.
2.  Xác định **Phòng** (Hàng ngang) và **Giờ** (Cột dọc) muốn chiếu.
3.  Ở danh sách phim bên trái, giữ chuột trái vào hình Poster phim.
4.  **Kéo và Thả** (Drag & Drop) tấm hình đó vào đúng vị trí giờ trên dòng thời gian của phòng.
5.  Một hộp thoại nhỏ hiện ra để xác nhận giờ chính xác (Ví dụ: Kéo vào khoảng 10h nhưng muốn chiếu lúc 10:15 thì gõ lại `10:15`).
6.  Nhấn **OK**.
    *   *Lưu ý:* Nếu hệ thống báo lỗi đỏ, có nghĩa là bạn đang xếp chồng lên phim khác hoặc phòng đang bảo trì. Hãy chọn giờ khác.

### 3.4. Thiết lập Phòng & Sơ đồ ghế (Menu: ROOMS)
*Cấu hình bố trí ghế và giá vé.*
1.  Vào menu **Rooms**.
2.  Chọn phòng từ danh sách (hoặc nhấn Add Room).
3.  **Vẽ ghế:**
    *   Màn hình hiện ra lưới ô vuông (Grid).
    *   **Click lần 1:** Tạo ghế Thường (Màu xám - Standard).
    *   **Click lần 2:** Chuyển thành ghế VIP (Màu đỏ).
    *   **Click lần 3:** Chuyển thành ghế Đôi (Màu hồng - Couple).
    *   **Click lần 4:** Xóa ghế (Thành lối đi).
4.  **Đặt giá:** Nhập giá vé Standard/VIP vào bảng bên phải.
5.  Nhấn **Save Room**.
    *   *Lưu ý:* Việc sửa sơ đồ ghế sẽ xóa hết lịch chiếu cũ của phòng đó, hãy cẩn thận.

### 3.5. Chính sách Giá & Khuyến mãi (Menu: POLICIES)
*Cấu hình các chương trình giảm giá tự động.*
1.  Vào menu **Policies** (hoặc Discounts).
2.  **Weekly Promotions (Khuyến mãi tuần):**
    *   Tích chọn vào các ngày trong tuần (Ví dụ: Thứ 3, Thứ 4).
    *   Nhấn **Save**. Hệ thống sẽ tự động giảm giá vé vào những ngày này.
3.  **Category Discounts (Giảm giá đối tượng):**
    *   **Student (HSSV):** Bật/Tắt công tắc giảm giá cho học sinh.
    *   **Senior (Người cao tuổi):** Bật/Tắt công tắc tương ứng.

### 3.6. Quản lý Ca làm việc (Menu: SHIFTS)
*Xem ai đang làm việc và lịch sử chấm công.*
1.  Vào menu **Shifts**.
2.  **Xem lịch sử:** Danh sách hiển thị toàn bộ lịch sử check-in/out của nhân viên.
3.  **Total Hours:** Cột này hiển thị tổng số giờ làm việc của từng ca.
4.  **Lọc dữ liệu:**
    *   Sử dụng ô tìm kiếm để lọc theo Tên nhân viên.
    *   Sử dụng bộ chọn Ngày (Date Picker) để xem chấm công của các ngày trước.

### 3.7. Xem Báo cáo & Xuất lương (Menu: REPORTS)
1.  Vào menu **Reports**.
2.  Mặc định hệ thống sẽ hiện doanh thu của tháng hiện tại.
3.  **Các chỉ số cần quan tâm:**
    *   `Total Revenue`: Tổng tiền thực thu (dùng để đếm tiền trong két).
    *   `Ticket Sold`: Số vé bán ra.
4.  **Xuất file:** Nhấn nút **Export Excel** góc trên phải để tải file báo cáo về gửi cho kế toán.
    *   File báo cáo sẽ chứa đầy đủ thông tin: Doanh thu vé, Bắp nước, và Giờ làm việc của nhân viên.
5.  **Bảng lương (Payroll):**
    *   Vào tab **Staff Stats**.
    *   Hệ thống tự động tính: `Tổng giờ làm x Lương theo giờ = Lương ước tính`.

### 3.8. Quản lý Khách hàng thân thiết (Menu: LOYALTY)
*Quản lý danh sách và hạng thành viên.*
1.  Vào menu **Loyalty**.
2.  **Danh sách thành viên:** Hiển thị Tên, SĐT, Điểm tích lũy và Tổng chi tiêu.
3.  **Hạng thành viên (Tự động):**
    *   `Standard`: Khách mới.
    *   `Gold`: Chi tiêu > 5 triệu.
    *   `Platinum`: Chi tiêu > 15 triệu.
4.  Bạn có thể sửa thông tin SĐT hoặc tên khách nếu họ yêu cầu.

---

## PHẦN 4: KHẮC PHỤC SỰ CỐ (TROUBLESHOOTING)

### 4.1. Sự cố Mất mạng Internet / Không kết nối được Server
*   **Biểu hiện:** Đăng nhập xoay vòng mãi không vào, hoặc báo lỗi "Connection Timed Out".
*   **Cách xử lý:**
    1.  Tắt hẳn chương trình.
    2.  Mở lại.
    3.  Tại màn hình Đăng nhập, click vào nút gạt **DB Mode** để chuyển sang **LOCAL**.
    4.  Đăng nhập lại bình thường. Dữ liệu sẽ lưu tạm trên máy này.

### 4.2. Sự cố Không in được vé
*   **Nguyên nhân:** Thường do máy in hết giấy hoặc lỏng dây cáp.
*   **Cách xử lý:** Kiểm tra đèn xanh trên máy in. Nếu đèn đỏ nhấp nháy, hãy thay giấy. Nếu vẫn không được, hãy chụp ảnh màn hình "Payment Successful" lại để đối soát sau.

### 4.3. Sự cố Nhân viên quên mật khẩu
*   Chỉ có Quản lý (`admin`) mới có quyền reset mật khẩu.
*   **Cách xử lý:** Vào menu **Staff**, chọn nhân viên đó và cập nhật lại mật khẩu mới (Mặc định nên đặt là `123456` rồi yêu cầu nhân viên đổi sau).
