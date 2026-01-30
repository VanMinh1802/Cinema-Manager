# SỔ TAY HƯỚNG DẪN TEST & SỬ DỤNG (FULL VERSION)

Tài liệu này hướng dẫn kiểm thử toàn diện 100% chức năng của hệ thống CineMe Manager. Tài liệu được chia theo đúng thứ tự menu trên phần mềm.

---

## 1. ĐĂNG NHẬP (Login & Authentication)
*   **Tài khoản Admin:** `admin` / `123456` (Full quyền).
*   **Tài khoản Staff:** `staff` / `123456` (Chỉ bán vé/bắp nước/xem lịch).
*   **Chức năng cần test:**
    *   [ ] Nhập sai mật khẩu -> Báo lỗi.
    *   [ ] Chuyển đổi **DB Mode** (Cloud/Local) -> Đăng nhập lại để kiểm tra dữ liệu khác nhau.

---

## 2. CHỨC NĂNG CHÍNH (Front Desk)

### 2.1. Overview (Tổng quan)
*   Màn hình đầu tiên sau khi đăng nhập.
*   **Test:**
    *   [ ] Kiểm tra xem Biểu đồ doanh thu 7 ngày gần nhất có hiển thị không.
    *   [ ] Kiểm tra các thẻ số liệu (Total Sales, Total Tickets) có con số không (không bị 0 hoặc lỗi).

### 2.2. Booking (Bán vé - Quan trọng nhất)
*   **Quy trình Test:**
    1.  Chọn Phim -> Chọn Suất Chiếu.
    2.  Check sơ đồ ghế:
        *   Ghế Đỏ: Đã bán (Không chọn được).
        *   Ghế Trắng: Trống (Chọn được).
        *   Ghế Xanh: Đang chọn.
    3.  Bấm nut **Payment**:
        *   [ ] Chọn thêm Combo Bắp/Nước (Concessions) trong dialog thanh toán.
        *   [ ] Nhập SĐT Khách hàng -> Bấm Enter.
            *   Khách cũ: Tự hiện tên + Điểm tích lũy.
            *   Khách mới: Hiện form đăng ký nhanh -> Nhập tên -> Lưu.
        *   [ ] Chọn phương thức thanh toán (Cash/Card).
        *   [ ] Áp dụng khuyến mãi (Voucher) nếu có.
    4.  Bấm **Print Ticket**.
    5.  **Kết quả:** Dialog Vé hiện ra + Dữ liệu được lưu vào Database (Doanh thu tăng).

### 2.3. Concessions (Bán Bắp Nước lẻ)
*   Menu dành cho quầy Canteen (khách không xem phim nhưng mua nước).
*   **Test:**
    *   [ ] Chọn món (Pepsi, Popcorn...).
    *   [ ] Tăng giảm số lượng.
    *   [ ] Thanh toán & In hóa đơn.

---

## 3. QUẢN LÝ (Management)

### 3.1. Movies (Phim)
*   **Test:**
    *   [ ] **Thêm Phim:** Bấm dấu `+`. Nhập tên, thời lượng, thể loại. URL Poster nhập link ảnh trên mạng (ví dụ google image) để test load ảnh.
    *   [ ] **Sửa:** Click vào phim -> Sửa thông tin -> Lưu.
    *   [ ] **Thay đổi trạng thái:** Đang chiếu / Sắp chiếu / Ngừng chiếu.

### 3.2. Schedule (Lịch chiếu)
*   **Test:**
    *   [ ] Chọn Phòng (Cinema 1, 2...).
    *   [ ] Chọn Ngày (Lịch ngày mai, ngày kia...).
    *   [ ] **Xếp lịch:** Click vào ô trống trên timeline -> Chọn Phim -> Chọn Giờ -> Lưu.
    *   [ ] **Check lỗi:** Thử xếp 2 phim chồng giờ nhau -> Hệ thống phải báo lỗi trùng lịch.

### 3.3. Rooms (Phòng chiếu)
*   **Test:**
    *   [ ] Thêm phòng chiếu mới (Ví dụ: Cinema 10).
    *   [ ] Cấu hình số ghế (Ví dụ 10 hàng x 15 cột = 150 ghế).

### 3.4. Staff (Nhân viên)
*   **Test:**
    *   [ ] Thêm nhân viên mới (Tạo tài khoản đăng nhập).
    *   [ ] Đặt mật khẩu + Quyền hạn (Manager/Staff).
    *   [ ] Thử dùng tài khoản mới này để đăng nhập.

### 3.5. Shifts (Ca làm việc)
*   **Test:**
    *   [ ] Phân ca cho nhân viên.
    *   [ ] Check chấm công (nếu có tính năng chấm công).

### 3.6. Loyalty (Khách hàng thân thiết)
*   **Test:**
    *   [ ] Xem danh sách khách hàng.
    *   [ ] Lịch sử mua vé của khách hàng.
    *   [ ] Cấu hình tỉ lệ tích điểm (Ví dụ: 10% giá trị hóa đơn).

### 3.7. Policies (Chính sách & Giá vé)
*   **Test:**
    *   [ ] **Cấu hình giá vé:** Giá vé thường, giá vé VIP, giá vé cuối tuần.
    *   [ ] **Tạo mã giảm giá (Voucher):**
        *   Tạo mã `TEST50` giảm 50%.
        *   Quay lại màn hình Booking, thử áp dụng mã này xem giá tiền có giảm không.

---

## 4. BÁO CÁO (Reports)
*   Tab cuối cùng.
*   **Test:**
    *   [ ] Lọc theo khoảng thời gian (Hôm nay, Tuần này, Tháng này).
    *   [ ] **Doanh thu:** Tổng tiền vé + Tiền bắp nước.
    *   [ ] **Top Phim:** Phim nào bán chạy nhất.
    *   [ ] Xuất báo cáo (nếu có nút Export Excel/PDF).

---
**Ghi chú:**
*   Nếu quá trình test bị lỗi, hãy chụp ảnh màn hình và gửi lại cho đội Dev.
*   Đảm bảo bật đúng chế độ **DB Mode** trước khi test để dữ liệu không bị lệch.
