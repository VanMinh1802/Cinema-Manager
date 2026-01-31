ĐẠI HỌC QUỐC GIA TP. HỒ CHÍ MINH
TRƯỜNG ĐẠI HỌC CÔNG NGHỆ THÔNG TIN
TRUNG TÂM PHÁT TRIỂN CNTT

BÁO CÁO ĐỒ ÁN MÔN HỌC
HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM (CINEMA MANAGER)

MÔN HỌC: LẬP TRÌNH ỨNG DỤNG JAVA (JAVA WINDOWS FORM)
GIẢNG VIÊN: [Tên Giảng Viên]

Nhóm [Số Nhóm]
Thành viên:
- Họ tên: [Tên Thành Viên 1]      MSSV: [MSSV]
- Họ tên: [Tên Thành Viên 2]      MSSV: [MSSV]
- Họ tên: [Tên Thành Viên 3]      MSSV: [MSSV]

---

## MỤC LỤC
1. DANH MỤC HÌNH ẢNH
2. LIVE DEMO
3. BẢNG PHÂN CÔNG CÔNG VIỆC
4. GIỚI THIỆU DỰ ÁN
5. PHÂN TÍCH YÊU CẦU CHỨC NĂNG
6. THIẾT KẾ HỆ THỐNG
7. CÔNG NGHỆ SỬ DỤNG
8. CHỨC NĂNG CHI TIẾT
9. CƠ SỞ DỮ LIỆU
10. BẢO MẬT
11. KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN
12. HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY CHƯƠNG TRÌNH
13. TÀI LIỆU THAM KHẢO

---

## 1. DANH MỤC HÌNH ẢNH
(Sinh viên tự chụp màn hình ứng dụng và thêm vào đây)
*   Hình 8.1: Màn hình Đăng nhập
*   Hình 8.2: Trang Bán vé và Sơ đồ ghế
*   Hình 8.3: Quản lý Bắp nước
*   Hình 8.4: Giao diện Xếp lịch
*   Hình 8.5: Báo cáo Doanh thu

## 2. LIVE DEMO
*   **Video Demo:** [Link Video]
*   **Source Code:** [Link GitHub]

## 3. BẢNG PHÂN CÔNG CÔNG VIỆC
| STT | Tên sinh viên | MSSV | Các công việc | Tỉ lệ (%) |
| :--- | :--- | :--- | :--- | :--- |
| 1 | [Nguyễn Văn A] | [...] | - Thiết kế CSDL<br>- Code phần Quản lý Phim, Lịch chiếu | 25% |
| 2 | [Trần Thị B] | [...] | - Thiết kế giao diện (GUI)<br>- Code phần Bán vé, Sơ đồ ghế | 25% |
| 3 | [Lê Văn C] | [...] | - Viết báo cáo<br>- Code phần Thống kê, Báo cáo | 25% |
| 4 | [Phạm Văn D] | [...] | - Tổng hợp source code<br>- Code phần Đăng nhập, Nhân viên | 25% |

## 4. GIỚI THIỆU DỰ ÁN

### 4.1. Tổng quan
Ứng dụng **Cinema Manager** là một hệ thống phần mềm Desktop được xây dựng trên nền tảng **Java Swing**, nhằm mục đích số hóa và tự động hóa toàn bộ quy trình vận hành của một cụm rạp chiếu phim. Hệ thống thay thế các phương thức quản lý thủ công bằng quy trình bán vé, xếp lịch và báo cáo tự động, chính xác.

### 4.2. Mục tiêu
*   Xây dựng giao diện đồ họa (GUI) trực quan.
*   Tối ưu hóa quy trình bán vé và chọn ghế.
*   Quản lý chặt chẽ doanh thu và tồn kho.
*   Cung cấp báo cáo thống kê trực quan.

### 4.3. Đặc điểm nổi bật
*   **Sơ đồ ghế động:** Hiển thị trực quan trạng thái ghế.
*   **Xếp lịch kéo thả:** Dễ dàng thao tác và quản lý suất chiếu.
*   **Chế độ Dual-Database:** Hỗ trợ hoạt động Offline và Online.

## 5. PHÂN TÍCH YÊU CẦU CHỨC NĂNG

### 5.1. Quản lý người dùng
*   Đăng nhập hệ thống bảo mật.
*   Phân quyền Admin/Staff.

### 5.2. Quản lý nghiệp vụ rạp
*   Quản lý Phim, Suất chiếu, Phòng vé.
*   Bán vé, bán bắp nước (Combo).
*   Quản lý nhân viên và ca làm việc.
*   Chăm sóc khách hàng thành viên (Loyalty).

### 5.3. Thống kê & Báo cáo
*   Báo cáo doanh thu theo ngày/tháng.
*   Xuất dữ liệu Excel.

## 6. THIẾT KẾ HỆ THỐNG

### 6.1. Kiến trúc MVC
*   **Models:** `Phim`, `NhanVien`, `Ve`, `Ghe`...
*   **Controllers:** Xử lý sự kiện trong View hoặc Controller riêng.
*   **Views:** Các `JPanel` giao diện người dùng.

### 6.2. Luồng xử lý
User Action -> Controller -> Service/DAO -> Database -> View Update.

## 7. CÔNG NGHỆ SỬ DỤNG
*   **Backend:** Java Core (JDK 17).
*   **Database:** MySQL 8.0, JDBC.
*   **Frontend:** Java Swing, Graphics2D.
*   **Tools:** Git, NetBeans/IntelliJ/VSCode, Maven.

## 8. CHỨC NĂNG CHI TIẾT

### 8.0. MÀN HÌNH ĐĂNG NHẬP (LoginFrame)
**Đăng nhập hệ thống (System Login)**
*   **Mục đích:** Cổng bảo mật đầu tiên, yêu cầu xác thực danh tính trước khi truy cập.
*   **Giao diện:**
    *   Ô nhập **Username** và **Password** (ẩn ký tự).
    *   Nút **Login** kích hoạt quá trình kiểm tra thông tin.
    *   **DB Mode:** Tùy chọn database ở Cloud hoặc Local (khuyên dùng).
*   **Cơ chế phân quyền:**
    *   Phân quyền tự động: Admin thấy toàn bộ menu, Staff chỉ thấy menu Home, Booking, Concessions.

**Kết nối nâng cao (Advanced Connection)**
*   **Dual-Database:** Hỗ trợ song song Localhost và Cloud Server.
*   **Hot-swap:** Click để chuyển đổi nguồn dữ liệu tức thì khi gặp sự cố mạng.
*   **Tóm tắt:** Đảm bảo an ninh hệ thống và tính sẵn sàng cao (Offline/Local).

### 8.1. TRANG CHỦ (Home - StatsPanel)
**Tổng quan**
*   Trang chủ được thiết kế theo phong cách Futuristic (Tương lai) với hiệu ứng thị giác mạnh mẽ, tạo ấn tượng hiện đại cho người dùng ngay khi đăng nhập.

**Giao diện & Hiệu ứng (Visual & Effects)**
*   **Hình nền động (Live Background):**
    *   Sử dụng thuật toán **Procedural Generation** để tạo ra một vũ trụ thu nhỏ với các hành tinh 3D.
    *   Hiệu ứng **Parallax Scrolling**: Các ngôi sao và hành tinh di chuyển với tốc độ khác nhau dựa trên độ sâu (Z-depth) khi di chuột.
    *   **Particle System:** Hiệu ứng sao băng (Shooting Star) và vệt chuột (Mouse Trail) lấp lánh.
*   **Đồng hồ thời gian thực (Real-time Clock):**
    *   Hiển thị giờ phút giây với font chữ lớn, hiệu ứng đổ bóng (Glow Effect).
    *   Tự động cập nhật mỗi giây (Timer tick).

**Logic Nghiệp vụ**
*   **Lời chào thông minh (Smart Greeting):**
    *   Logic: `IF (Hour < 12) "Good Morning" ELSE IF (Hour < 17) "Good Afternoon" ELSE "Good Evening"`.
    *   Hiển thị kèm tên người dùng đang đăng nhập (VD: "Good Evening, Nguyễn Quản Lý").
*   **Cập nhật Trích dẫn (Random Quotes):**
    *   Hệ thống lưu trữ một danh sách các câu nói nổi tiếng về điện ảnh (VD: "May the Force be with you", "I am Iron Man").
    *   Hiệu ứng **Typewriter**: Chữ chạy từng ký tự giống như đang gõ máy.
    *   Tự động đổi câu trích dẫn sau mỗi 20 giây.

**Tóm tắt:** Không chỉ là trang Dashboard hiển thị thông tin, trang Chủ còn là nơi thể hiện kỹ thuật đồ họa nâng cao trong Java Swing (Graphics2D, AffineTransform, RadialGradientPaint).

### 8.2. TRANG BÁN VÉ (Booking - BanVePanel)
**Mô tả chung**
Module bán vé là trái tim của hệ thống, nơi nhân viên thực hiện các nghiệp vụ phức tạp nhất: Tư vấn chọn chỗ, bán kèm dịch vụ (Cross-selling) và thanh toán đa phương thức.

**Quy trình nghiệp vụ (Business Flow)**
1.  **Chọn suất chiếu:** Lọc phim theo Ngày & Tên -> Hệ thống load sơ đồ ghế thời gian thực (Real-time Seat Map).
2.  **Chọn ghế:**
    *   Click ghế trống (Xám) -> Chuyển sang màu Xanh (Đang chọn).
    *   Click ghế đã bán (Đỏ) -> Hệ thống cảnh báo hoặc kích hoạt quy trình **Hoàn vé (Refund)** nếu có quyền hạn.
3.  **Chọn dịch vụ đi kèm:** Thêm Bắp/Nước trực tiếp từ thanh công cụ nhanh (Quick Add Bar).
4.  **Áp dụng giảm giá (Discount & Loyalty):**
    *   Chọn đối tượng khách hàng (HSSV, Người cao tuổi) để giảm giá.
    *   Tìm thành viên bằng SĐT -> Hệ thống tự động hiển thị Hạng thẻ (Gold/Platinum) và giảm giá thêm (5-10%) nếu có.
    *   Sử dụng điểm tích lũy để thanh toán (Redeem Points).
5.  **Thanh toán:** Hệ thống lưu vé, trừ kho bắp nước, cộng điểm thưởng và xuất vé (E-Ticket hoặc Hóa đơn in).

**Cơ chế tính giá (Pricing Logic)**
Giá vé cuối cùng = (Giá gốc theo Loại ghế + Phụ thu VIP/Couple) - Khuyến mãi tuần - Giảm giá đối tượng - Ưu đãi thành viên.
*   **Giá động:** Cấu hình giá khác nhau cho từng phòng chiếu hoặc giờ chiếu.
*   **Thuế:** Tự động tính VAT 5% vào tổng hóa đơn.

**Tính năng kỹ thuật (Technical Features)**
*   **Async Loading:** Sử dụng `CompletableFuture` để tải sơ đồ ghế bất đồng bộ, giúp giao diện không bị đơ khi mạng chậm.
*   **State Management:** Quản lý trạng thái Giỏ hàng (Tạm tính, Số lượng) và đồng bộ tức thì với UI.
*   **Visual Seat Map:** Vẽ tùy chỉnh (Custom Painting) với Java Swing, tạo hình ghế ngồi 3D (Standard/VIP/Couple) thay vì nút bấm tiêu chuẩn.

### 8.3. QUẢN LÝ BẮP NƯỚC (Concessions - ConcessionPanel)
**Tổng quan**
Module quản lý kho hàng F&B (Food & Beverages), hỗ trợ theo dõi hàng tồn kho và cấu hình các gói Combo kích cầu.

**Chức năng chính**
1.  **Quản lý danh mục (Inventory Management):**
    *   **Phân loại:** Lọc nhanh theo nhóm Popcorn, Drinks, Candies, Combos.
    *   **Tìm kiếm:** Hỗ trợ tìm theo Tên sản phẩm hoặc mã SKU (Ví dụ: `POP-022`).
    *   **Trạng thái kho:** Hiển thị thanh trạng thái màu (Xanh/Đỏ) báo hiệu mức tồn kho.
2.  **Cấu hình Combo (Dynamic Combos):**
    *   Cho phép tạo gói Combo mới (Ví dụ: Combo đôi = 2 Bắp + 2 Nước).
    *   **Logic tồn kho thông minh:** Số lượng tồn kho của Combo không cố định, mà được **tính toán động** dựa trên số lượng của các món lẻ thành phần.
        *   *Ví dụ:* Nếu kho còn 100 bắp và 50 nước, Combo (1 Bắp + 1 Nước) sẽ chỉ còn 50 suất.
3.  **CRUD Sản phẩm:**
    *   Thêm mới/Cập nhật hình ảnh, giá bán và giá vốn.
    *   Xóa sản phẩm (Soft delete hoặc cảnh báo nếu đã có giao dịch).

**UX/UI Elements**
*   **Giao diện thẻ (Card Layout):** Hiển thị hình ảnh sản phẩm trực quan.
*   **Responsive Grid:** Lưới sản phẩm tự động co giãn theo kích thước cửa sổ.
*   **Pill Filters:** Các nút lọc hình viên thuốc hiện đại, dễ thao tác.

### 8.4. QUẢN LÝ PHIM (Movies - MoviePanel)
**Tổng quan**
Giao diện quản lý phim với thiết kế Dark Mode hiện đại, tối ưu cho việc tra cứu nhanh và cập nhật thông tin chính xác.

**Tính năng chính**
1.  **Danh sách phim (Rich List View):**
    *   Sử dụng `JTable` tùy biến hoàn toàn (Custom Renderers).
    *   **Poster Column:** Hiển thị thumbnail ảnh bìa phim ngay trong danh sách.
    *   **Badge System:** Hiển thị Thể loại (Genre) và Trạng thái (Active/Coming Soon) dưới dạng thẻ màu (Sci-Fi: Xanh, Action: Đỏ, Active: Xanh lá).
2.  **Bộ lọc & Tìm kiếm (Async Search):**
    *   Thanh tìm kiếm phản hồi tức thì (Real-time Filtering) theo Tên phim, Thể loại hoặc Mã Phim (Movie ID).
    *   Load dữ liệu bất đồng bộ (Async) để đảm bảo UI không bao giờ bị treo khi cơ sở dữ liệu phim lớn.
3.  **Chi tiết & Chỉnh sửa (Detail Editor):**
    *   Bố cục 3 cột (3-Column Layout): Poster - Thông tin chính (Form) - Tóm tắt (Synopsis).
    *   **Poster Upload:** Cho phép tải ảnh bìa từ máy tính, có Preview trước khi lưu.
    *   **Trạng thái:** Chuyển đổi linh hoạt giữa Active (Đang chiếu), Coming Soon (Sắp chiếu) và Archived (Lưu trữ).

**UX/UI Elements**
*   **Gradient Placeholders:** Khi chưa có poster, hệ thống tự tạo hình ảnh Gradient đẹp mắt thay thế.
*   **Rounded Panels:** Các khung chứa thông tin được bo góc mềm mại, tạo cảm giác cao cấp.

### 8.5. XẾP LỊCH CHIẾU (Schedule - SchedulePanel)
**Giải thuật Render Timeline (Timeline Canvas Algorithm)**
*   Hệ thống không sử dụng component có sẵn mà tự vẽ toàn bộ Canvas trình diễn lịch chiếu (`TimelineCanvas`).
*   **Logic vẽ khối lượng hình học (Rendering Logic):**
    *   `X (Vị trí)`: Tính toán dựa trên giờ bắt đầu (Start Hour). Phép tính: `Pixels = (Hour - 8) * SlotWidth`.
    *   `Width (Chiều dài)`: Tương ứng với thời lượng phim (`Duration`). Phép tính: `Width = (Duration / 60) * SlotWidth`.
    *   `Y (Hàng)`: Xác định dựa trên chỉ số phòng chiếu (`Room Index`).
*   **Progress Bar Occupancy:**
    *   Trên mỗi block lịch chiếu, một thanh tiến trình (màu vàng/đỏ) được vẽ đè lên để hiển thị trực quan tỷ lệ vé đã bán (`Sold / Capacity`).

**Cơ chế phát hiện xung đột (Conflict Detection)**
*   **Input Validation:** Kiểm tra định dạng giờ (Regex: `HH:mm:ss`) ngay khi nhập liệu để đảm bảo chuẩn `java.sql.Time`.
*   **Database Constraints:** Việc kiểm tra trùng lịch (Overlapping) được ủy nhiệm xuống Database Engine (hoặc Query Check) để đảm bảo tính toàn vẹn. Nếu Insert thất bại, hệ thống sẽ báo lỗi.
*   **Maintenance Blocking:** Logic kiểm tra `tình trạng phòng` (Maintenance Mode) trước khi cho phép thả (Drop) phim vào timeline.
    *   *Visual Warning:* Hiển thị thông báo "Room is under maintenance" nếu cố tình xếp lịch.

**Tương tác người dùng (UX Interactions)**
*   **Drag & Drop:** Sử dụng `DropTargetAdapter` để bắt sự kiện thả Poster phim vào Canvas.
*   **Context Menu:** Cho phép xóa hoặc sửa giờ chiếu nhanh bằng Menu ngữ cảnh.

### 8.6. QUẢN LÝ PHÒNG (Rooms - RoomPanel)
**Mô hình trực quan (Grid Layout Visualization)**
*   Hiển thị danh sách tất cả phòng chiếu dưới dạng lưới thẻ (Grid Cards).
*   **Mini-map:** Mỗi thẻ hiển thị sơ đồ thu nhỏ của phòng để dễ dàng nhận diện cấu trúc (4x8, 10x15...).
*   **Trạng thái:**
    *   🟢 **Active:** Phòng đang hoạt động bình thường.
    *   🔴 **Locked (Maintenance):** Phòng đang bảo trì. Chức năng xếp lịch (Schedule) sẽ tự động chặn mọi thao tác thêm suất chiếu vào phòng này.

**Cấu hình ghế & Giá (Seat & Pricing Config)**
*   **Sidebar Configurator:** Bảng cấu hình giá vé chi tiết nằm bên phải, cho phép chỉnh sửa nhanh giá vé cơ bản cho 3 loại ghế (Standard, VIP, Double) của phòng đang chọn.
*   **Seat Layout Editor:** Công cụ đồ họa hóa giúp vẽ sơ đồ ghế.
    *   *Click chuột* để chuyển đổi loại ghế theo vòng lặp: `Standard -> VIP -> Double -> Standard`.
    *   Hệ thống tự động lưu tọa độ và loại ghế vào cơ sở dữ liệu.

**Tóm tắt:** Không chỉ đặt tên phòng, module này định nghĩa "luật chơi" (Giá, Sơ đồ) cho toàn bộ hệ thống bán vé.

### 8.7. QUẢN LÝ NHÂN VIÊN (Staff - EmployeePanel)
**Hồ sơ nhân sự & Phân quyền (RBAC)**
*   **Quản lý vai trò (Role-based Access Control):**
    *   👑 **MANAGER (Màu tím):** Toàn quyền truy cập hệ thống (Setting, Report, Staff...).
    *   🔵 **STAFF (Màu xanh):** Chỉ truy cập được module Bán vé và F&B.
*   **Trạng thái tài khoản:**
    *   🟢 **Active:** Đăng nhập bình thường.
    *   🔴 **Inactive:** Tài khoản bị khóa (không xóa vĩnh viễn), ngăn chặn đăng nhập trái phép sau khi nghỉ việc.

**Quản lý lương & Thông tin (Payroll Info)**
*   **Lương theo giờ (Hourly Wage):** Thiết lập mức lương cơ bản (VD: 20,000 VND/giờ) làm cơ sở để tính toán chi phí nhân sự hằng tháng.
*   **Bảo mật:** Mật khẩu khi khởi tạo phải có độ dài tối thiểu 6 ký tự.

**Tóm tắt:** Hệ thống phân quyền chặt chẽ giúp bảo mật dữ liệu nhạy cảm (Doanh thu, Cấu hình) nhưng vẫn đảm bảo nhân viên bán vé thao tác nhanh chóng.

### 8.8. CA LÀM VIỆC (Shifts - ShiftPanel)
**Quản lý ca làm việc (Shift Definitions)**
*   Định nghĩa các khung giờ chuẩn (Sáng: 08:00-16:00, Chiều...) giúp chuẩn hóa quy trình chấm công.
*   **Timekeeping History:** Bảng theo dõi lịch sử chấm công chi tiết.
    *   Tự động tính **Total Hours** (Số giờ làm việc) chênh lệch giữa Check-in và Check-out.
    *   Trạng thái hiển thị màu sắc trực quan: 🟢 **Working** (Đang làm), ⚫ **Finished** (Đã về).
    *   Bộ lọc theo Ngày (Date Picker) và Tìm kiếm nhân viên.

**Tóm tắt:** Công cụ số hóa thay thế máy chấm công thẻ giấy, giúp tính lương chính xác đến từng phút.

### 8.9. KHÁCH HÀNG THÂN THIẾT (Loyalty - LoyaltyPanel)
**Cấu hình & Quy đổi điểm (Global Rules Config)**
*   **Logic quy đổi điểm:** Hệ thống tách biệt giá trị quy đổi để Admin có thể cấu hình linh động (Soft-coding).
    *   *Ví dụ:* `1 Point = 1,000 VND`.
    *   Khi thanh toán, nếu khách hàng chọn dùng điểm, hệ thống sẽ thực hiện phép tính: `Discount = PointsToRedeem * PointValue`.
*   **Module quản lý thành viên(Member Directory):**
    *   Tìm kiếm **Real-time** (ngay khi gõ phím) theo Tên hoặc Số điện thoại.
    *   **Tier Logic (Xếp hạng):**
        *   **Standard:** Mặc định.
        *   **Gold:** Tổng chi tiêu > 5,000,000 VND. (Hiển thị thẻ màu vàng trên UI).
        *   **Platinum:** Tổng chi tiêu > 15,000,000 VND. (Hiển thị thẻ màu bạc ánh kim).
    *   Việc nâng hạng được kiểm tra tự động mỗi khi cập nhật chi tiêu (`checkTierUpdate()`).

### 8.10. CHÍNH SÁCH GIÁ & KHUYẾN MÃI (DiscountPanel)
**Bộ máy định giá (Pricing Engine Algorithm)**
Hệ thống áp dụng mô hình "Waterfall Discount" (Giảm giá theo tầng) trong hàm `updateCart()`:
1.  **Base Price:** Lấy giá vé gốc theo Lịch chiếu (hoặc cấu hình Phòng).
2.  **Weekly Promo:** Kiểm tra ngày hiện tại có trong danh sách khuyến mãi không?
    *   Nếu có: Áp dụng giảm `Fixed Amount` hoặc `Percentage`.
3.  **Policy Warning:** Chỉ một Policy (HSSV/Người cao tuổi) được áp dụng trên Subtotal sau khi đã trừ Promo.
4.  **Tier Benefit:** Tự động giảm thêm **5%** (Gold) hoặc **10%** (Platinum) trên tổng hóa đơn vé.
5.  **Loyalty Redemption:** Trừ trực tiếp tiền từ điểm tích lũy.

**Công thức tổng quát:**
`Final = (BasePrice - WeeklyPromo - Policy) * (1 - TierDiscount) - LoyaltyPoints + Tax(5%)`

### 8.11. BÁO CÁO (Reports - ReportPanel)
**Kiến trúc thống kê (Aggregation Architecture)**
*   Sử dụng trực tiếp các câu lệnh **SQL Aggregate Functions** (`SUM`, `COUNT`, `GROUP BY`) để tối ưu hiệu năng thay vì xử lý trên Java.
*   **Phân tích doanh thu theo giờ (Hourly Heatmap):**
    *   Query: `GROUP BY HOUR(NgayLap)` -> Trả về mảng 24 phần tử (0-23h).
    *   Biểu đồ: Vẽ Stacked Bar Chart, tách biệt `Ticket Revenue` (Màu đỏ) và `Concession Revenue` (Màu xanh) để thấy rõ nguồn thu.
*   **Top Phim (Best Sellers):**
    *   Logic: `ORDER BY SUM(GiaTien) DESC`.
    *   Dữ liệu được lấy theo bộ lọc thời gian thực: `WHERE MONTH(NgayChieu) = ?`.

**Xuất dữ liệu Excel (Advanced Export)**
*   Hệ thống không dùng thư viện POI nặng nề mà sử dụng thuật toán xuất **Native HTML Table** với đuôi `.xls`.
*   **Ưu điểm:** Tương thích tuyệt đối với Excel, hỗ trợ Unicode (Tiếng Việt) chuẩn xác, giữ nguyên định dạng màu sắc báo cáo web.
*   **Nội dung:** Bao gồm Metrics tổng quan và Log giao dịch chi tiết (Transaction History).

## 9. CƠ SỞ DỮ LIỆU
(Xem file Cinema_db.sql đính kèm hoặc hình ảnh ERD trong báo cáo chi tiết)
*   **Bảng chính:** NhanVien, Phim, PhongChieu, Ghe, LichChieu, Ve, HoaDon...

## 10. BẢO MẬT
*   **Authentication:** Mã hóa mật khẩu (nếu có, hoặc so sánh trực tiếp trong phạm vi đồ án).
*   **Authorization:** Phân quyền chức năng theo vai trò.
*   **Validation:** Kiểm tra dữ liệu đầu vào (số điện thoại, tiền...).

## 11. KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN
### 11.1. Kết luận
*   Ứng dụng đáp ứng tốt các yêu cầu quản lý rạp chiếu phim cơ bản.
*   Giao diện trực quan, dễ sử dụng cho nhân viên.

### 11.2. Hướng phát triển
*   Phát triển Mobile App đặt vé.
*   Tích hợp thanh toán QR Code.

---

## 12. HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY CHƯƠNG TRÌNH

### 12.1. YÊU CẦU HỆ THỐNG (PREREQUISITES)
Trước khi bắt đầu, hãy đảm bảo máy tính đã cài đặt:
*   **Java Development Kit (JDK):**
    *   Phiên bản: JDK 17+.
    *   Kiểm tra: Mở CMD gõ `java -version`.
*   **MySQL Server:**
    *   Phiên bản: MySQL 8.0+.
    *   Công cụ: MySQL Workbench.

### 12.2. CÀI ĐẶT CƠ SỞ DỮ LIỆU (DATABASE SETUP)
**Cách 1: Sử dụng Cloud Database (Nhanh nhất)**
*   Hệ thống đã cấu hình sẵn kết nối Cloud (Aiven).
*   **Thao tác:** Ở màn hình đăng nhập, nên chọn **DB Mode: Local**. Nếu lỗi, click để chuyển sang **Cloud**.

**Cách 2: Cài đặt Local Database (Chạy Offline)**
1.  Truy cập phpMyAdmin (`http://localhost/phpmyadmin`) hoặc MySQL Workbench.
2.  Tạo Database mới tên `cinema_db` (Encoding: `utf8mb4_general_ci`).
3.  Chọn Database vừa tạo -> Import.
4.  Chọn file `cinema_db.sql` trong thư mục `CinemaManager_Release/DB`.
5.  Nhấn **Import**.

### 12.3. CÁCH CHẠY CHƯƠNG TRÌNH
Chúng tôi đã tạo sẵn script tự động.
**Bước 1:** Vào thư mục `CinemaManager_Release` hoặc thư mục gốc.
**Bước 2:** Chạy file `RunApp.bat`.
*   Cửa sổ Console sẽ hiện lên, **ĐỪNG TẮT NÓ**.
*   Màn hình Login sẽ xuất hiện sau vài giây.

**Bước 3: Đăng nhập**
| Vai Trò | Tài Khoản | Mật Khẩu | Quyền Hạn |
| :--- | :--- | :--- | :--- |
| **Quản Lý** | `admin` | `123456` | Toàn quyền |
| **Nhân Viên** | `nv01` | `123456` | Bán vé & F&B |

---

## 13. TÀI LIỆU THAM KHẢO
1.  Oracle Java Documentation.
2.  MySQL Reference Manual.
