package com.cinema.gui;

import com.cinema.dto.LichChieu;
import java.awt.*;
import java.awt.print.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TicketDialog extends JDialog {

    private JPanel ticketPanel;

    public TicketDialog(JFrame parent, LichChieu lc, List<String> gheChon, double tienVe, double tienSnack, int maHD,
            Map<String, Integer> snacks) {
        super(parent, "Vé Điện Tử", true);
        setSize(400, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240)); // Nền xám nhạt bên ngoài

        // 1. TẠO PANEL VÉ (MÀU TRẮNG Ở GIỮA)
        ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBackground(Color.WHITE);
        ticketPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding xung quanh

        // --- A. HEADER (Logo & Mã GD) ---
        addCenteredLabel(ticketPanel, "<html><h2 style='color:#cc0000'>CINEMA PRO</h2></html>");
        addCenteredLabel(ticketPanel, "TRANSACTION #" + maHD);
        addDashedLine(ticketPanel);

        // --- B. THÔNG TIN PHIM ---
        addCenteredLabel(ticketPanel, "<html><h1 style='margin-bottom:0'>" + lc.getTenPhim() + "</h1></html>");
        addCenteredLabel(ticketPanel, "Movie Ticket");
        ticketPanel.add(Box.createVerticalStrut(15)); // Khoảng cách

        // --- C. GRID THÔNG TIN (Ngày/Giờ/Phòng/Ghế) ---
        // Dùng HTML Table để căn chỉnh đẹp như mẫu
        String gheStr = String.join(", ", gheChon);
        String infoHtml = "<html><table style='width:100%'>"
                + "<tr><td style='color:gray'>DATE</td><td style='color:gray; text-align:right'>TIME</td></tr>"
                + "<tr><td><b>" + lc.getNgayChieu() + "</b></td><td style='text-align:right'><b>" + lc.getGioChieu()
                + "</b></td></tr>"
                + "<tr><td><br></td><td></td></tr>" // Dòng trống
                + "<tr><td style='color:gray'>HALL</td><td style='color:gray; text-align:right'>SEATS</td></tr>"
                + "<tr><td><b>" + lc.getTenPhong() + "</b></td><td style='text-align:right'><b>" + gheStr
                + "</b></td></tr>"
                + "</table></html>";

        JLabel lblInfo = new JLabel(infoHtml);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(lblInfo);
        ticketPanel.add(Box.createVerticalStrut(20));

        // --- D. MÃ QR (Giả lập bằng icon hoặc panel màu) ---
        JPanel qrPanel = new JPanel();
        qrPanel.setPreferredSize(new Dimension(120, 120));
        qrPanel.setMaximumSize(new Dimension(120, 120));
        qrPanel.setBackground(Color.BLACK); // Giả lập QR màu đen
        qrPanel.setToolTipText("QR Code giả lập");

        // Chèn chữ QR vào giữa
        JLabel lblQR = new JLabel("QR CODE", SwingConstants.CENTER);
        lblQR.setForeground(Color.WHITE);
        lblQR.setPreferredSize(new Dimension(120, 120));
        qrPanel.add(lblQR);

        ticketPanel.add(qrPanel);
        addCenteredLabel(ticketPanel, "<html><small>Scan at the entrance</small></html>");
        addDashedLine(ticketPanel);

        // --- E. TỔNG TIỀN (CHI TIẾT) ---
        StringBuilder snackHtml = new StringBuilder();
        if (snacks != null && !snacks.isEmpty()) {
            snackHtml.append("<tr><td colspan='2'><hr></td></tr>");
            snackHtml.append("<tr><td colspan='2'><b>Snacks:</b></td></tr>");
            for (Map.Entry<String, Integer> entry : snacks.entrySet()) {
                snackHtml.append("<tr><td>- ").append(entry.getKey()).append(" (x").append(entry.getValue())
                        .append(")</td><td style='text-align:right'></td></tr>");
            }
            snackHtml.append("<tr><td>Tổng Snack</td><td style='text-align:right'>")
                    .append(String.format("%,.0f", tienSnack)).append("</td></tr>");
        }

        double tongTien = tienVe + tienSnack;

        String priceHtml = "<html><table style='width:280px'>"
                + "<tr><td>Vé (" + gheChon.size() + "x)</td><td style='text-align:right'>"
                + String.format("%,.0f", tienVe) + "</td></tr>"
                + "<tr><td>Phí tiện ích</td><td style='text-align:right'>0</td></tr>"
                + snackHtml.toString()
                + "<tr><td><hr></td><td><hr></td></tr>"
                + "<tr><td><b>TOTAL</b></td><td style='text-align:right; color:red; font-size:16px'><b>"
                + String.format("%,.0f VNĐ", tongTien) + "</b></td></tr>"
                + "</table></html>";

        JLabel lblPrice = new JLabel(priceHtml);
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(lblPrice);

        // --- CUỐI CÙNG: THÊM VÀO SCROLL PANE ---
        // Để nhỡ vé dài quá thì cuộn được
        JScrollPane scroll = new JScrollPane(ticketPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // 2. NÚT CHỨC NĂNG (IN / ĐÓNG)
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnPrint = new JButton("Print Receipt");
        JButton btnClose = new JButton("Close");

        btnPrint.setBackground(new Color(220, 53, 69)); // Red color similar to template
        btnPrint.setForeground(Color.WHITE);

        // Sự kiện IN VÉ (Sử dụng PrinterJob của Java)
        btnPrint.addActionListener(e -> printTicket());
        btnClose.addActionListener(e -> dispose());

        btnPanel.add(btnPrint);
        btnPanel.add(btnClose);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // --- CÁC HÀM PHỤ TRỢ ---
    private void addCenteredLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
    }

    private void addDashedLine(JPanel panel) {
        JLabel line = new JLabel("------------------------------------------------------");
        line.setForeground(Color.LIGHT_GRAY);
        line.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(line);
        panel.add(Box.createVerticalStrut(10));
    }

    // Hàm thực hiện lệnh IN ra máy in
    private void printTicket() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Ticket Ticket");

        job.setPrintable((Graphics pg, PageFormat pf, int pageNum) -> {
            if (pageNum > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) pg;
            g2.translate(pf.getImageableX(), pf.getImageableY());

            // Scale lại cho vừa khổ giấy in nếu cần
            g2.scale(0.8, 0.8);

            // Vẽ toàn bộ cái panel vé ra máy in
            ticketPanel.printAll(g2);
            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
}
