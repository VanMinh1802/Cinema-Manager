package com.cinema.gui;

import com.cinema.dao.NhanVienDAO;
import com.cinema.dto.NhanVien;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class EmployeePanel extends JPanel {

    // Colors
    private static final Color BG_DARK = Color.decode("#0A0A0A");
    private static final Color BG_CARD = Color.decode("#1A1A1A");
    private static final Color BG_INPUT = Color.decode("#2A2A2A");
    private static final Color BORDER_COLOR = Color.decode("#3A3A3A");
    private static final Color TEXT_PRIMARY = Color.WHITE;
    private static final Color TEXT_SECONDARY = Color.decode("#A0A0A0");
    private static final Color ACCENT_RED = Color.decode("#EF4444");

    private JTextField txtHoTen, txtTaiKhoan, txtSearch;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboChucVu;
    private JTextField txtLuong; // NEW FIELD
    private JTable table;
    private DefaultTableModel tableModel;
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private int selectedID = -1;
    private NhanVien currentEditingEmployee = null; // Add field

    public EmployeePanel() {
        setLayout(new BorderLayout());
        setBackground(BG_DARK);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header Section
        mainPanel.add(createHeaderSection(), BorderLayout.NORTH);

        // Content Section (Table + Form)
        mainPanel.add(createContentSection(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                loadData();
                removeAncestorListener(this);
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });
    }

    private JPanel createHeaderSection() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_DARK);

        // Title
        JLabel title = new JLabel("Staff & Employee Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(5));

        // Subtitle
        JLabel subtitle = new JLabel("Manage access and permissions for cinema personnel.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(subtitle);

        return header;
    }

    private JPanel createContentSection() {
        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setBackground(BG_DARK);

        // Left: Table Section
        JPanel tableSection = new JPanel(new BorderLayout(0, 15));
        tableSection.setBackground(BG_DARK);
        tableSection.add(createSearchBar(), BorderLayout.NORTH);
        tableSection.add(createTable(), BorderLayout.CENTER);

        // Right: Form Section
        // Right: Form Section
        JPanel formContent = createFormPanel();
        JScrollPane formScroll = new JScrollPane(formContent);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        formScroll.setPreferredSize(new Dimension(380, 0));
        formScroll.setMinimumSize(new Dimension(380, 600));

        content.add(tableSection, BorderLayout.CENTER);
        content.add(formScroll, BorderLayout.EAST);

        return content;
    }

    private JPanel createSearchBar() {
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setBackground(BG_DARK);

        // Search Input
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBackground(BG_INPUT);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 12, 10, 12)));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setForeground(TEXT_SECONDARY);

        txtSearch = new JTextField();
        txtSearch.setText("Search by name, role, or ID...");
        txtSearch.setBackground(BG_INPUT);
        txtSearch.setForeground(TEXT_SECONDARY);
        txtSearch.setBorder(null);
        txtSearch.setCaretColor(TEXT_PRIMARY);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(BG_DARK);

        JButton btnFilter = createButton("Filter", false);

        // Add search functionality
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterTable(txtSearch.getText());
            }
        });

        // Clear placeholder on focus
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals("Search by name, role, or ID...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Search by name, role, or ID...");
                    txtSearch.setForeground(TEXT_SECONDARY);
                }
            }
        });

        buttonsPanel.add(btnFilter);

        // Add Filter button functionality
        btnFilter.addActionListener(e -> {
            JPopupMenu popup = new JPopupMenu();
            popup.setBackground(BG_CARD);
            popup.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

            String[] options = { "All", "Manager", "Staff", "Active", "Inactive" };
            for (String option : options) {
                JMenuItem item = new JMenuItem(option);
                item.setFont(new Font("SansSerif", Font.PLAIN, 13));
                item.setForeground(TEXT_PRIMARY);
                item.setBackground(BG_CARD);
                item.setBorder(new EmptyBorder(5, 10, 5, 10));

                item.addActionListener(ev -> filterByRole(option));
                popup.add(item);
            }
            popup.show(btnFilter, 0, btnFilter.getHeight());
        });

        searchBar.add(searchPanel, BorderLayout.CENTER);
        searchBar.add(buttonsPanel, BorderLayout.EAST);

        return searchBar;
    }

    private JButton createButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            btn.setForeground(Color.WHITE);
            btn.setBackground(ACCENT_RED);
            btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        } else {
            btn.setForeground(TEXT_PRIMARY);
            btn.setBackground(BG_CARD);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(9, 16, 9, 16)));
        }

        return btn;
    }

    private JScrollPane createTable() {
        // Table Model
        String[] columns = { "ID", "FULL NAME", "USERNAME", "ROLE", "STATUS" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(50);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(30, 30, 30));
        table.setForeground(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(50, 50, 50));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Header
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                l.setBackground(new Color(20, 20, 20));
                l.setForeground(new Color(180, 180, 180));
                l.setFont(new Font("SansSerif", Font.BOLD, 11));
                l.setBorder(new EmptyBorder(10, 10, 10, 10));

                // Alignments - ALL LEFT to match LoyaltyPanel
                l.setHorizontalAlignment(SwingConstants.LEFT);
                return l;
            }
        });

        // Column Widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Username
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Role
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Status

        // Custom Renderer (for all columns)
        for (int i = 0; i < 5; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new EmployeeCellRenderer());
        }

        // Mouse listener (Optional, if user clicks row)
        // Mouse listener (Click row to edit)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    // Get ID from column 0 (Format: "#123")
                    String idStr = tableModel.getValueAt(row, 0).toString().replace("#", "");
                    try {
                        int id = Integer.parseInt(idStr);
                        NhanVien nv = nhanVienDAO.getNhanVienById(id);
                        if (nv != null) {
                            populateForm(nv);
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(50, 50, 50)));
        scroll.getViewport().setBackground(new Color(20, 20, 20));

        return scroll;
    }

    private JPanel createFormPanel() {
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(BG_CARD);
        formCard.setOpaque(true);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(25, 20, 25, 20)));

        // Header
        JLabel formTitle = new JLabel("Add New Employee");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        formTitle.setForeground(TEXT_PRIMARY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);

        JLabel formSubtitle = new JLabel("Create account for new staff");
        formSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        formSubtitle.setForeground(TEXT_SECONDARY);
        formSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formSubtitle);
        formCard.add(Box.createVerticalStrut(20));

        // Full Name
        formCard.add(createLabel("Full Name"));
        formCard.add(Box.createVerticalStrut(6));
        txtHoTen = createTextField("e.g., John Wick");
        formCard.add(txtHoTen);
        formCard.add(Box.createVerticalStrut(15));

        // Username
        formCard.add(createLabel("Username"));
        formCard.add(Box.createVerticalStrut(6));
        txtTaiKhoan = createTextField("e.g., john.w");
        formCard.add(txtTaiKhoan);
        formCard.add(Box.createVerticalStrut(15));

        // Role
        formCard.add(createLabel("Role"));
        formCard.add(Box.createVerticalStrut(6));
        cboChucVu = new JComboBox<>(new String[] { "BanVe", "QuanLy" });
        cboChucVu.setBackground(BG_INPUT);
        cboChucVu.setForeground(TEXT_PRIMARY);
        cboChucVu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cboChucVu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)));
        cboChucVu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cboChucVu.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(cboChucVu);
        formCard.add(Box.createVerticalStrut(15));

        // Salary (Hourly Rate)
        formCard.add(createLabel("Hourly Wage (VND)"));
        formCard.add(Box.createVerticalStrut(6));
        txtLuong = createTextField("20000");
        formCard.add(txtLuong);
        formCard.add(Box.createVerticalStrut(15));

        // Password
        formCard.add(createLabel("Initial Password"));
        formCard.add(Box.createVerticalStrut(6));

        txtMatKhau = new JPasswordField();
        txtMatKhau.setEchoChar('•');
        txtMatKhau.setBackground(BG_INPUT);
        txtMatKhau.setForeground(TEXT_PRIMARY);
        txtMatKhau.setCaretColor(TEXT_PRIMARY);
        txtMatKhau.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(txtMatKhau);

        JLabel hint = new JLabel("Must be at least 6 characters long");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 10));
        hint.setForeground(TEXT_SECONDARY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(hint);
        formCard.add(Box.createVerticalStrut(20));

        // Actions Panel (Grid Layout for Compactness)
        JPanel pActions = new JPanel(new GridLayout(3, 1, 5, 5)); // 3 Rows
        pActions.setLayout(new BoxLayout(pActions, BoxLayout.Y_AXIS)); // Actually, Grid is better
        // Fix: Use standard JPanel with GridBag or Grid for actionable area
        JPanel pGridActions = new JPanel(new GridLayout(3, 1, 5, 5));
        pGridActions.setBackground(BG_CARD);
        pGridActions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        pGridActions.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Row 1: Save (Primary)
        JButton btnSave = createButton("Save Employee", true);
        btnSave.addActionListener(e -> handleSaveEmployee());

        // Row 2: Update & Delete
        JPanel pRow2 = new JPanel(new GridLayout(1, 2, 10, 0));
        pRow2.setBackground(BG_CARD);

        JButton btnUpdate = createButton("Update", false);
        btnUpdate.addActionListener(e -> handleUpdateEmployee());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnDelete.setForeground(ACCENT_RED);
        btnDelete.setBackground(BG_CARD);
        btnDelete.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> handleDeleteEmployee());

        pRow2.add(btnUpdate);
        pRow2.add(btnDelete);

        // Row 3: Toggle & Clear
        JPanel pRow3 = new JPanel(new GridLayout(1, 2, 10, 0));
        pRow3.setBackground(BG_CARD);

        JButton btnToggle = new JButton("Toggle Status");
        btnToggle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnToggle.setForeground(new Color(255, 165, 0)); // Orange
        btnToggle.setBackground(BG_CARD);
        btnToggle.setBorder(BorderFactory.createLineBorder(new Color(255, 165, 0), 1));
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> handleToggleStatus());

        JButton btnClear = createButton("Clear", false);
        btnClear.addActionListener(e -> clearForm());

        pRow3.add(btnToggle);
        pRow3.add(btnClear);

        // Add to Grid
        pGridActions.add(btnSave);
        pGridActions.add(pRow2);
        pGridActions.add(pRow3);

        formCard.add(pGridActions);
        formCard.add(Box.createVerticalGlue());

        return formCard;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.setText(placeholder);
        txt.setBackground(BG_INPUT);
        txt.setForeground(TEXT_SECONDARY);
        txt.setCaretColor(TEXT_PRIMARY);
        txt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }

    private void handleSaveEmployee() {
        String password = String.valueOf(txtMatKhau.getPassword());

        if (txtHoTen.getText().isEmpty() || txtTaiKhoan.getText().isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields (Name, Username, Password)!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!");
            return;
        }

        double luong = 20000;
        try {
            luong = Double.parseDouble(txtLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Salary Format!");
            return;
        }

        NhanVien nv = new NhanVien(0, txtHoTen.getText(), txtTaiKhoan.getText(),
                password, cboChucVu.getSelectedItem().toString(), 1, luong);

        if (nhanVienDAO.addNhanVien(nv)) {
            JOptionPane.showMessageDialog(this, "Employee added successfully!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error! Username may already exist.");
        }
    }

    private void handleUpdateEmployee() {
        if (currentEditingEmployee == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update!");
            return;
        }

        String newPassword = String.valueOf(txtMatKhau.getPassword());

        // Validate password length if changing
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!");
            return;
        }

        // If password field is empty, keep the old one. Otherwise use the new one.
        String passwordToSave = newPassword.isEmpty() ? currentEditingEmployee.getMatKhau() : newPassword;

        double luong = 20000;
        try {
            luong = Double.parseDouble(txtLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Salary Format!");
            return;
        }

        NhanVien nv = new NhanVien(
                currentEditingEmployee.getMaNV(),
                txtHoTen.getText(),
                txtTaiKhoan.getText(),
                passwordToSave,
                cboChucVu.getSelectedItem().toString(),
                currentEditingEmployee.getTrangThai(),
                luong);

        if (nhanVienDAO.updateNhanVien(nv)) {
            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating employee!");
        }
    }

    private void handleDeleteEmployee() {
        if (selectedID == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (nhanVienDAO.deleteNhanVien(selectedID)) {
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting employee!");
            }
        }
    }

    private void handleToggleStatus() {
        if (selectedID == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first!");
            return;
        }

        // Get current employee info
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        NhanVien currentEmployee = null;
        for (NhanVien nv : list) {
            if (nv.getMaNV() == selectedID) {
                currentEmployee = nv;
                break;
            }
        }

        if (currentEmployee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found!");
            return;
        }

        // Toggle status
        int newStatus = (currentEmployee.getTrangThai() == 1) ? 0 : 1;
        String action = (newStatus == 1) ? "activate" : "deactivate";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + action + " this account?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (nhanVienDAO.toggleStatus(selectedID, newStatus)) {
                String statusText = (newStatus == 1) ? "activated" : "deactivated";
                JOptionPane.showMessageDialog(this, "Account " + statusText + " successfully!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error changing account status!");
            }
        }
    }

    private void loadData() {
        com.cinema.util.UIUtils.runAsync(this, () -> {
            return nhanVienDAO.getAllNhanVien();
        }, (list) -> {
            tableModel.setRowCount(0);
            for (NhanVien nv : list) {
                String status = (nv.getTrangThai() == 1) ? "Active" : "Inactive";
                tableModel.addRow(new Object[] {
                        "#" + nv.getMaNV(),
                        nv.getHoTen(),
                        "@" + nv.getTaiKhoan(),
                        nv.getChucVu(),
                        status
                });
            }
        });
    }

    private void loadDataLegacy() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        for (NhanVien nv : list) {
            String status = (nv.getTrangThai() == 1) ? "Active" : "Inactive";
            tableModel.addRow(new Object[] {
                    "#" + nv.getMaNV(),
                    nv.getHoTen(),
                    "@" + nv.getTaiKhoan(),
                    nv.getChucVu(),
                    status
            });
        }
    }

    private void clearForm() {
        txtHoTen.setText("");
        txtTaiKhoan.setText("");
        txtMatKhau.setText("");
        txtLuong.setText("20000");
        cboChucVu.setSelectedIndex(0);
        txtTaiKhoan.setEditable(true);
        selectedID = -1;
        currentEditingEmployee = null;
    }

    private void filterTable(String searchText) {
        // Skip if it's the placeholder text
        if (searchText.equals("Search by name, role, or ID...") || searchText.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        String lowerSearch = searchText.toLowerCase();

        for (NhanVien nv : list) {
            boolean matches = false;

            // Search by ID
            if (String.valueOf(nv.getMaNV()).contains(lowerSearch)) {
                matches = true;
            }
            // Search by name
            else if (nv.getHoTen().toLowerCase().contains(lowerSearch)) {
                matches = true;
            }
            // Search by username
            else if (nv.getTaiKhoan().toLowerCase().contains(lowerSearch)) {
                matches = true;
            }
            // Search by role
            else if (nv.getChucVu().toLowerCase().contains(lowerSearch)) {
                matches = true;
            }

            if (matches) {
                String status = (nv.getTrangThai() == 1) ? "Active" : "Inactive";
                tableModel.addRow(new Object[] {
                        "#" + nv.getMaNV(),
                        nv.getHoTen(),
                        "@" + nv.getTaiKhoan(),
                        nv.getChucVu(),
                        status
                });
            }
        }
    }

    private void filterByRole(String filter) {
        if (filter.equals("All")) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();

        for (NhanVien nv : list) {
            boolean include = false;

            if (filter.equals("Manager") && nv.getChucVu().equalsIgnoreCase("QuanLy")) {
                include = true;
            } else if (filter.equals("Staff") && nv.getChucVu().equalsIgnoreCase("BanVe")) {
                include = true;
            } else if (filter.equals("Active") && nv.getTrangThai() == 1) {
                include = true;
            } else if (filter.equals("Inactive") && nv.getTrangThai() == 0) {
                include = true;
            }

            if (include) {
                String status = (nv.getTrangThai() == 1) ? "Active" : "Inactive";
                tableModel.addRow(new Object[] {
                        "#" + nv.getMaNV(),
                        nv.getHoTen(),
                        "@" + nv.getTaiKhoan(),
                        nv.getChucVu(),
                        status
                });
            }
        }
    }

    // Custom Cell Renderer
    class EmployeeCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // 1. GLOBAL ALIGNMENT (LEFT)
            c.setHorizontalAlignment(SwingConstants.LEFT);
            c.setVerticalAlignment(SwingConstants.CENTER);
            c.setBorder(new EmptyBorder(0, 10, 0, 10));

            // 2. ZEBRA STRIPING & BACKGROUND
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(30, 30, 30) : new Color(35, 35, 38));
            } else {
                c.setBackground(new Color(50, 50, 50));
            }
            c.setForeground(isSelected ? Color.WHITE : new Color(220, 220, 220));

            // 3. CUSTOM CONTENT
            if (column == 3) { // Role
                c.setText(value.toString());
                c.setFont(new Font("SansSerif", Font.BOLD, 12));
                if (value.toString().equalsIgnoreCase("QuanLy")) {
                    c.setForeground(new Color(139, 92, 246)); // Violet
                    c.setText("MANAGER");
                } else {
                    c.setForeground(new Color(59, 130, 246)); // Blue
                    c.setText("STAFF");
                }
            } else if (column == 4) { // Status
                c.setText(value.toString()); // Active/Inactive
                c.setFont(new Font("SansSerif", Font.BOLD, 12));
                // Center alignment REMOVED. Now inherits Left.

                if (value.toString().equals("Active")) {
                    c.setForeground(new Color(16, 185, 129)); // Green
                } else {
                    c.setForeground(new Color(239, 68, 68)); // Red
                }
            }

            // Normal Columns
            c.setFont(new Font("SansSerif", Font.PLAIN, 13));
            if (column == 0)
                c.setForeground(Color.GRAY); // ID
            if (column == 1)
                c.setFont(new Font("SansSerif", Font.BOLD, 13)); // Name

            return c;
        }
    }

    // --- Helper methods ---
    private void populateForm(NhanVien nv) {
        currentEditingEmployee = nv;
        selectedID = nv.getMaNV();

        txtHoTen.setText(nv.getHoTen());
        txtTaiKhoan.setText(nv.getTaiKhoan());
        txtMatKhau.setText("");
        txtLuong.setText(String.valueOf(nv.getLuongTheoGio()));
        cboChucVu.setSelectedItem(nv.getChucVu());
        txtTaiKhoan.setEditable(false);
    }
}
