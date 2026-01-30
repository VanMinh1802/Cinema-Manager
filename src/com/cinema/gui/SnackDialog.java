package com.cinema.gui;

import com.cinema.dao.SanPhamDAO;
import com.cinema.dto.SanPham;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SnackDialog extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private SanPhamDAO spDAO = new SanPhamDAO();
    private JButton btnXacNhan;

    // Lưu kết quả chọn: Tên Món -> Số Lượng
    private Map<String, Integer> selectedSnacks = new HashMap<>(); // Khởi tạo để tránh null nếu cần
    private double tongTienSnack = 0;
    private Map<String, Integer> initialSelection; // Để lưu dữ liệu đầu vào

    public boolean isConfirmed = false; // Kiểm tra xem user có bấm Xác nhận không

    public SnackDialog(JFrame parent, Map<String, Integer> currentSelection) {
        super(parent, "Chọn Bắp & Nước", true);
        this.initialSelection = currentSelection; // Lưu lại reference hoặc copy

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 1. Bảng danh sách món
        String[] cols = { "Mã", "Tên Món", "Đơn Giá", "Số Lượng Mua" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override // Chỉ cho sửa cột Số Lượng (cột 3)
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        table = new JTable(tableModel);
        loadData();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. Hướng dẫn & Nút
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.add(new JLabel("Nhập số lượng vào cột cuối cùng rồi bấm Xác Nhận ->"));

        btnXacNhan = new JButton("Xác Nhận & Đóng");
        btnXacNhan.setBackground(new Color(0, 153, 51));
        btnXacNhan.setForeground(Color.WHITE);

        btnXacNhan.addActionListener(e -> tinhTienVaDong());

        pnlBottom.add(btnXacNhan);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        List<SanPham> list = spDAO.getAllSanPham();
        for (SanPham sp : list) {
            // Lấy số lượng đã chọn trước đó (nếu có)
            int soLuong = 0;
            if (initialSelection != null && initialSelection.containsKey(sp.getTenSP())) {
                soLuong = initialSelection.get(sp.getTenSP());
            }

            tableModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), sp.getGiaBan(), soLuong });
        }
    }

    private void tinhTienVaDong() {
        tongTienSnack = 0;
        selectedSnacks.clear();

        // Duyệt qua bảng để xem user nhập số lượng bao nhiêu
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object val = tableModel.getValueAt(i, 3);
            if (val == null)
                continue;

            try {
                String valStr = val.toString().trim();
                if (valStr.isEmpty())
                    continue;

                int sl = Integer.parseInt(valStr);
                if (sl > 0) {
                    String ten = tableModel.getValueAt(i, 1).toString();
                    double gia = Double.parseDouble(tableModel.getValueAt(i, 2).toString());

                    selectedSnacks.put(ten, sl); // Lưu tên và số lượng
                    tongTienSnack += (sl * gia); // Cộng tiền

                }
            } catch (NumberFormatException e) {
                String tenItem = tableModel.getValueAt(i, 1).toString();
                JOptionPane.showMessageDialog(this,
                        "Số lượng không hợp lệ cho món: " + tenItem + "\nVui lòng nhập số nguyên (VD: 1, 2).");
                return; // Dừng lại để user sửa
            }
        }

        isConfirmed = true;
        dispose(); // Đóng cửa sổ
    }

    // Getter để lấy dữ liệu ra ngoài
    public Map<String, Integer> getSelectedSnacks() {
        return selectedSnacks;
    }

    public double getTongTienSnack() {
        return tongTienSnack;
    }
}
