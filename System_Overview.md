# HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM - TÀI LIỆU CHỨC NĂNG

---

## 0. MÀN HÌNH ĐĂNG NHẬP (LoginFrame)

1.  **System Login (Đăng nhập hệ thống):**
    *   Mục đích: Cổng bảo mật đầu tiên, yêu cầu xác thực danh tính trước khi truy cập.
    *   **Giao diện đăng nhập:**
        *   Ô nhập **Username** và **Password** (ẩn ký tự).
        *   Nút Login kích hoạt quá trình kiểm tra thông tin.
        *   Dòng trạng thái kết nối Database (Connecting...) hiển thị realtime.
    *   Cơ chế phân quyền tự động: Admin thấy toàn bộ menu, Staff chỉ thấy menu bán hàng.

2.  **Advanced Connection (Kết nối nâng cao):**
    *   **Dual-Database:** Hỗ trợ song song Localhost và Cloud Server.
    *   **Hot-swap (F12):** Cho phép Quản trị viên nhấn phím tắt F12 để chuyển đổi nguồn dữ liệu tức thì khi gặp sự cố mạng.

**Tóm lại chức năng:** Đảm bảo an ninh hệ thống và tính sẵn sàng cao, cho phép hoạt động liên tục ngay cả khi mất mạng internet (nhờ chế độ Offline/Local).

---

## 1. TRANG CHỦ (Home - StatsPanel)

1.  **Business Dashboard (Bảng điều khiển kinh doanh):**
    *   Hiển thị các chỉ số KPI quan trọng nhất trong ngày theo thời gian thực.
    *   **Các thẻ chỉ số (Metric Cards):**
        *   **Revenue (Doanh thu):** Tổng tiền bán Vé + Bắp nước.
        *   **Tickets (Số vé):** Tổng số lượng vé đã xuất.
        *   **Occupancy (Tỷ lệ lấp đầy):** Phần trăm ghế có khách ngồi.

2.  **Traffic Chart (Biểu đồ lưu lượng):**
    *   **Mục đích:** Trực quan hóa khung giờ cao điểm và thấp điểm.
    *   **Hiển thị:**
        *   Trục ngang: Các giờ trong ngày (8:00 - 23:00).
        *   Trục dọc: Số lượng khách hàng.

**Tóm lại chức năng:** Giúp người quản lý nắm bắt nhanh "sức khỏe" của rạp phim ngay khi đăng nhập mà không cần thao tác tra cứu phức tạp.

---

## 2. TRANG BÁN VÉ (Booking - BanVePanel)

1.  **Movie Selection (Khu vực chọn phim):**
    *   **Tìm kiếm thông minh:** Thanh search cho phép lọc phim theo tên.
    *   **Bộ lọc ngày (Date Spinner):** Cho phép bán vé cho các suất chiếu tương lai.
    *   Danh sách suất chiếu hiển thị tóm tắt: Tên phim - Giờ chiếu - Tên rạp.

2.  **Seat Map (Sơ đồ ghế trực quan):**
    *   **Hiển thị:** Lưới ghế mô phỏng chính xác sơ đồ thực tế của phòng chiếu.
    *   **Mã màu trạng thái (Legend):**
        *   **Standard (Xám):** Ghế thường giá tiêu chuẩn.
        *   **VIP (Vàng):** Ghế vị trí đẹp, phụ thu thêm tiền.
        *   **Double (Hồng):** Ghế đôi dành cho cặp đôi.
        *   **Booked (Đỏ):** Ghế đã bán, bị khóa không thể chọn.
        *   **Selected (Xanh lá):** Ghế đang được chọn.

3.  **Checkout & Payment (Thanh toán):**
    *   **Giỏ hàng:** Tự động cộng dồn tiền Vé và Bắp nước.
    *   **Khuyến mãi tự động:**
        *   Hệ thống tự quét ngày chiếu để áp dụng giảm giá (VD: Thứ 2 giảm 30%).
        *   Hiển thị dòng thông báo: `Active Promo: [Tên chương trình]`.
    *   **Tích điểm (Loyalty):** Tìm khách theo SĐT và hỗ trợ trừ điểm thưởng.

**Tóm lại chức năng:** Module quan trọng nhất, nơi nhân viên thực hiện toàn bộ quy trình bán hàng: Tư vấn chỗ ngồi, bán kèm đồ ăn và in vé cho khách.

---

## 3. QUẢN LÝ BẮP NƯỚC (Concessions - ConcessionPanel)

1.  **Menu Management (Quản lý thực đơn):**
    *   Quản lý danh sách các món ăn uống: Bắp rang, Nước ngọt, Combo.
    *   Cho phép cập nhật giá bán (Selling Price) và hình ảnh minh họa.

2.  **Combo Configuration (Cấu hình Combo):**
    *   Tạo các gói Combo (VD: 1 Bắp + 2 Nước) để kích cầu.
    *   Hệ thống tự động trừ kho các món lẻ khi bán 1 Combo.

**Tóm lại chức năng:** Quản lý mảng F&B (Food & Beverage), giúp tối ưu hóa nguồn thu phụ trợ bên cạnh doanh thu bán vé.

---

## 4. QUẢN LÝ PHIM (Movies - MoviePanel)

1.  **Movie Database (Cơ sở dữ liệu phim):**
    *   Nơi nhập liệu thông tin phim: Tên, Thể loại, Thời lượng, Đạo diễn.
    *   Quản lý trạng thái phim: Đang chiếu (Now Showing) hoặc Sắp chiếu.

2.  **Media Assets (Quản lý hình ảnh):**
    *   **Upload Poster:** Tính năng chọn ảnh từ máy tính để làm ảnh bìa đẹp mắt cho phim.
    *   Hình ảnh này sẽ được đồng bộ hiển thị ở trang Bán vé và Xếp lịch.

**Tóm lại chức năng:** Đây là kho dữ liệu trung tâm. Phim phải được khai báo tại đây trước thì mới có thể tiến hành xếp lịch chiếu.

---

## 5. XẾP LỊCH CHIẾU (Schedule - SchedulePanel)

1.  **Drag & Drop Interface (Giao diện kéo thả):**
    *   Thao tác trực quan: Kéo Poster phim từ danh sách thả vào dòng thời gian (Timeline).
    *   Trục thời gian ngang giúp dễ dàng quan sát lịch trình của từng phòng.

2.  **Schedule Logic (Logic xếp lịch):**
    *   **Tự động tính giờ:** Tự cộng thêm thời lượng phim + 15 phút dọn dẹp để ra giờ kết thúc.
    *   **Chặn trùng lịch:** Hệ thống tự động báo lỗi nếu xếp đè lên suất chiếu khác.

**Tóm lại chức năng:** Công cụ giúp người quản lý sắp xếp hàng trăm suất chiếu mỗi tuần một cách chính xác, loại bỏ hoàn toàn sai sót trùng giờ.

---

## 6. QUẢN LÝ PHÒNG (Rooms - RoomPanel)

1.  **Room Layout (Thiết kế sơ đồ phòng):**
    *   Cho phép định nghĩa kích thước phòng: Số hàng x Số cột (VD: 10x15).
    *   Đặt tên phòng chiếu (Cinema 1, IMAX...).

2.  **Seat Configuration (Cấu hình ghế):**
    *   Click vào từng ô trên lưới để gán loại ghế: Thường, VIP, Đôi hoặc Hỏng.
    *   Thiết lập giá vé cơ bản (Base Price) cho từng phòng.

**Tóm lại chức năng:** Số hóa bản đồ vật lý của rạp vào phần mềm, làm cơ sở cho tính năng bán vé chọn chỗ.

---

## 7. QUẢN LÝ NHÂN VIÊN (Staff - EmployeePanel)

1.  **Staff Profiles (Hồ sơ nhân sự):**
    *   Lưu trữ thông tin nhân viên: Họ tên, Ngày sinh, Số điện thoại.
    *   Danh sách hiển thị dạng bảng dễ tra cứu.

2.  **Account Management (Quản lý tài khoản):**
    *   Cấp Tên đăng nhập và Mật khẩu truy cập hệ thống.
    *   **Phân quyền (Roles):** Chọn vai trò Quản lý hoặc Nhân viên bán vé.

**Tóm lại chức năng:** Giúp kiểm soát an ninh hệ thống và quản lý danh sách nhân sự đang làm việc tại rạp.

---

## 8. CA LÀM VIỆC (Shifts - ShiftPanel)

1.  **Shift Planner (Bảng phân ca):**
    *   Giao diện lịch tuần trực quan (Thứ 2 - Chủ Nhật).
    *   **Thao tác:** Gán nhân viên vào các ca làm việc (Sáng/Chiều/Tối).

2.  **Payroll Estimate (Dự tính lương):**
    *   Hệ thống ước tính chi phí lương dựa trên số ca làm việc đã phân công.

**Tóm lại chức năng:** Thay thế quy trình phân ca thủ công, giúp theo dõi lịch làm việc của nhân viên dễ dàng.

---

## 9. KHÁCH HÀNG THÂN THIẾT (Loyalty - LoyaltyPanel)

1.  **Member Database (Cơ sở dữ liệu khách hàng):**
    *   Lưu trữ thông tin liên lạc để chăm sóc khách hàng (CRM).
    *   Tra cứu lịch sử mua hàng và tổng tiền đã chi tiêu.

2.  **Auto-Tiering (Xếp hạng tự động):**
    *   Hệ thống tự động nâng hạng thẻ (Bạc -> Vàng -> Kim Cương).
    *   Căn cứ vào tổng chi tiêu tích lũy của khách hàng.

**Tóm lại chức năng:** Công cụ giúp giữ chân khách hàng (Retention), khuyến khích họ quay lại rạp thông qua các ưu đãi hạng thẻ.

---

## 10. CHÍNH SÁCH GIÁ (Policies - DiscountPanel)

1.  **Demographic Policies (Chính sách nhân khẩu học):**
    *   Quản lý các chính sách giảm giá dựa trên đối tượng khách hàng (ví dụ: Học sinh, Sinh viên, Người cao tuổi...).
    *   Cho phép thêm, sửa, xóa các loại giảm giá theo % hoặc số tiền cố định.

2.  **Weekly Promotions (Khuyến mãi hàng tuần - Đang hiển thị trong ảnh):**
    *   **Mục đích:** Quản lý lịch khuyến mãi tự động theo các ngày trong tuần (Thứ 2 đến Chủ Nhật) và các ngày lễ đặc biệt (Tết, Giỗ Tổ, 30/4, 2/9...).
    *   **Giao diện thẻ (Cards):**
        *   Mỗi ngày được hiển thị dưới dạng một thẻ ("MON", "TUE", "WED"...).
        *   Màu đỏ đậm thể hiện ngày đó đang có khuyến mãi kích hoạt (`Active`).
        *   Nút gạt (Switch) trên thẻ cho phép bật/tắt nhanh khuyến mãi của ngày đó.
    *   **Chỉnh sửa chi tiết (Edit Promotion - Cột bên phải):**
        *   Khi bấm vào một thẻ ngày, cột bên phải sẽ hiển thị chi tiết để chỉnh sửa.
        *   Promotion Name: Tên chương trình (ví dụ: "Monday Special").
        *   Discount Value: Giá trị giảm (ví dụ: 30).
        *   Type: Loại giảm (Percent % hoặc Fixed Amount).
        *   Nút Save Changes để lưu lại cấu hình vào cơ sở dữ liệu.

**Tóm lại chức năng:** Trang này giúp người quản lý (Admin) thiết lập hệ thống khuyến mãi linh hoạt. Ví dụ: Cài đặt để "Thứ 2 giảm 30%" hoặc "Ngày lễ 30/4 giảm giá vé đồng loạt". Khi nhân viên bán vé, hệ thống sẽ tự động áp dụng các khuyến mãi này dựa trên ngày hiện tại (như đã thấy ở trang Bán Vé trước đó).

---

## 11. BÁO CÁO (Reports - ReportPanel)

1.  **Revenue Analytics (Phân tích doanh thu):**
    *   **Chế độ xem:** Tùy chọn xem chi tiết theo Ngày (Daily) hoặc tổng hợp Tháng (Monthly).
    *   **Biểu đồ:** So sánh tỷ trọng doanh thu Vé vs Bắp nước.

2.  **Data Export (Xuất dữ liệu):**
    *   **Tính năng:** Xuất toàn bộ báo cáo ra file Excel (.xls).
    *   **Nội dung:** Bao gồm bảng tổng hợp metrics và log chi tiết từng giao dịch.

**Tóm lại chức năng:** Công cụ hỗ trợ kế toán, minh bạch hóa dòng tiền và cung cấp số liệu chính xác để đánh giá hiệu quả kinh doanh.
