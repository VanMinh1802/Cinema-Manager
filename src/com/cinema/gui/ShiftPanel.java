package com.cinema.gui;

import com.cinema.dao.CaLamViecDAO;
import com.cinema.dao.ChamCongDAO;
import com.cinema.dao.NhanVienDAO;
import com.cinema.dto.CaLamViec;
import com.cinema.dto.ChamCong;
import com.cinema.dto.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShiftPanel extends JPanel {

  private CaLamViecDAO shiftDAO = new CaLamViecDAO();
  private ChamCongDAO chamCongDAO = new ChamCongDAO();
  private NhanVienDAO nhanVienDAO = new NhanVienDAO();

  // UI Components
  private CardLayout cardLayout;
  private JPanel mainContentPanel;
  private JToggleButton btnTabShifts;
  private JToggleButton btnTabHistory;

  // Shift Table
  private JTable shiftTable;
  private DefaultTableModel shiftModel;

  // History Table
  private JTable historyTable;
  private DefaultTableModel historyModel;
  private JSpinner dateSpinner;
  private JTextField txtSearchHistory;
  private java.util.List<ChamCong> currentLogs; // Cache for filtering

  // Colors
  private static final Color BG_DARK = new Color(18, 18, 18);
  private static final Color TXT_PRIMARY = new Color(240, 240, 240);
  private static final Color TXT_SECONDARY = new Color(150, 150, 150);
  private static final Color ACCENT_RED = new Color(229, 9, 20);
  private static final Color BORDER_COLOR = new Color(60, 60, 60);

  public ShiftPanel() {
    setLayout(new BorderLayout());
    setBackground(BG_DARK);

    add(createHeader(), BorderLayout.NORTH);

    cardLayout = new CardLayout();
    mainContentPanel = new JPanel(cardLayout);
    mainContentPanel.setBackground(BG_DARK);

    mainContentPanel.add(createShiftManagementPanel(), "TAB_SHIFTS");
    mainContentPanel.add(createHistoryPanel(), "TAB_HISTORY");

    add(mainContentPanel, BorderLayout.CENTER);

    switchTab("TAB_SHIFTS");
  }

  private JPanel createHeader() {
    JPanel container = new JPanel(new BorderLayout());
    container.setBackground(BG_DARK);
    container.setBorder(new EmptyBorder(30, 40, 20, 40));

    // Title Row
    JPanel pTop = new JPanel(new BorderLayout());
    pTop.setOpaque(false);

    JLabel lbl = new JLabel("Work Schedule & Timekeeping");
    lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
    lbl.setForeground(TXT_PRIMARY);
    pTop.add(lbl, BorderLayout.WEST);

    // Tab Row
    JPanel pTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20)); // Spacing
    pTabs.setOpaque(false);

    btnTabShifts = createTabButton("Shift Definitions");
    btnTabHistory = createTabButton("Timekeeping History");

    btnTabShifts.addActionListener(e -> switchTab("TAB_SHIFTS"));
    btnTabHistory.addActionListener(e -> {
      switchTab("TAB_HISTORY");
      loadHistoryData();
    });

    pTabs.add(btnTabShifts);
    pTabs.add(Box.createHorizontalStrut(10));
    pTabs.add(btnTabHistory);

    container.add(pTop, BorderLayout.NORTH);
    container.add(pTabs, BorderLayout.SOUTH);

    return container;
  }

  private void switchTab(String tabName) {
    cardLayout.show(mainContentPanel, tabName);
    boolean isShifts = "TAB_SHIFTS".equals(tabName);
    btnTabShifts.setSelected(isShifts);
    btnTabHistory.setSelected(!isShifts);
  }

  private JToggleButton createTabButton(String text) {
    JToggleButton b = new JToggleButton(text);
    b.setPreferredSize(new Dimension(180, 40));
    b.setFont(new Font("SansSerif", Font.BOLD, 14));
    b.setFocusPainted(false);
    b.setBorderPainted(false);
    b.setContentAreaFilled(false);
    b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
      @Override
      public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        JToggleButton btn = (JToggleButton) c;
        if (btn.isSelected()) {
          g2.setColor(ACCENT_RED);
          btn.setForeground(Color.WHITE);
        } else {
          g2.setColor(new Color(40, 40, 40));
          btn.setForeground(TXT_SECONDARY);
        }
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
        super.paint(g, c);
      }
    });
    return b;
  }

  // --- TAB 1: SHIFT MANAGEMENT ---
  private JPanel createShiftManagementPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(0, 40, 40, 40));

    // Add Button
    JPanel pTools = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pTools.setOpaque(false);
    JButton btnAdd = new JButton("+ Add New Shift");
    btnAdd.setBackground(ACCENT_RED);
    btnAdd.setForeground(Color.WHITE);
    btnAdd.setFocusPainted(false);
    btnAdd.setFont(new Font("SansSerif", Font.BOLD, 12));
    btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnAdd.addActionListener(e -> showShiftDialog(null));
    btnAdd.setBorder(new EmptyBorder(8, 15, 8, 15));
    pTools.add(btnAdd);
    p.add(pTools, BorderLayout.NORTH);

    String[] cols = { "ID", "Shift Name", "Start Time", "End Time", "Actions" };
    shiftModel = new DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int row, int col) {
        return col == 4;
      }
    };

    shiftTable = createStyledTable(shiftModel);

    // Actions
    shiftTable.getColumnModel().getColumn(4).setCellRenderer(new ActionIconsRenderer());
    shiftTable.getColumnModel().getColumn(4).setCellEditor(new ActionIconsEditor());
    shiftTable.getColumnModel().getColumn(4).setMinWidth(140);
    shiftTable.getColumnModel().getColumn(4).setMaxWidth(160);

    JScrollPane sp = new JScrollPane(shiftTable);
    sp.setBorder(new LineBorder(BORDER_COLOR));
    sp.getViewport().setBackground(BG_DARK);

    p.add(sp, BorderLayout.CENTER);

    loadShiftData();
    return p;
  }

  // --- TAB 2: HISTORY ---
  private JPanel createHistoryPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(0, 40, 40, 40));

    // Date Selection Tools
    // Toolbar Container
    JPanel pTools = new JPanel(new BorderLayout());
    pTools.setOpaque(false);

    // LEFT: Search Bar
    JPanel pSearch = new JPanel(new BorderLayout(5, 0));
    pSearch.setOpaque(false);
    pSearch.setBorder(new EmptyBorder(5, 0, 5, 20)); // Margin right

    JLabel lblSearch = new JLabel("🔍");
    lblSearch.setForeground(TXT_SECONDARY);

    txtSearchHistory = new JTextField();
    txtSearchHistory.setPreferredSize(new Dimension(200, 30));
    txtSearchHistory.setFont(new Font("SansSerif", Font.PLAIN, 12));
    txtSearchHistory.setBackground(new Color(40, 40, 40));
    txtSearchHistory.setForeground(TXT_PRIMARY);
    txtSearchHistory.setCaretColor(TXT_PRIMARY);
    txtSearchHistory.setBorder(new LineBorder(BORDER_COLOR));

    // Add KeyListener for filtering
    txtSearchHistory.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent e) {
        filterHistory(txtSearchHistory.getText());
      }
    });

    pSearch.add(lblSearch, BorderLayout.WEST);
    pSearch.add(txtSearchHistory, BorderLayout.CENTER);

    // RIGHT: Date Picker & Button
    JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    pRight.setOpaque(false);

    JLabel lblDate = new JLabel("Filter by Day: ");
    lblDate.setForeground(TXT_SECONDARY);
    lblDate.setFont(new Font("SansSerif", Font.PLAIN, 12));
    pRight.add(lblDate);

    SpinnerDateModel model = new SpinnerDateModel();
    dateSpinner = new JSpinner(model);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
    dateSpinner.setEditor(editor);
    dateSpinner.setFont(new Font("SansSerif", Font.PLAIN, 12));
    dateSpinner.setPreferredSize(new Dimension(120, 30));
    pRight.add(dateSpinner);

    JButton btnFilter = new JButton("View History");
    btnFilter.setBackground(new Color(40, 40, 40));
    btnFilter.setForeground(Color.WHITE);
    btnFilter.setFocusPainted(false);
    btnFilter.setFont(new Font("SansSerif", Font.BOLD, 12));
    btnFilter.setBorder(new EmptyBorder(5, 15, 5, 15));
    btnFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnFilter.addActionListener(e -> loadHistoryData());
    pRight.add(btnFilter);

    pTools.add(pSearch, BorderLayout.WEST);
    pTools.add(pRight, BorderLayout.EAST);

    p.add(pTools, BorderLayout.NORTH);

    String[] cols = { "Log ID", "Employee", "Shift", "Check In", "Check Out", "Status", "Total Hour" };
    historyModel = new DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };

    historyTable = createStyledTable(historyModel);

    // Width adjustments
    historyTable.getColumnModel().getColumn(0).setMaxWidth(60); // ID
    historyTable.getColumnModel().getColumn(5).setMinWidth(100); // Status
    historyTable.getColumnModel().getColumn(6).setMinWidth(120); // Hours

    JScrollPane sp = new JScrollPane(historyTable);
    sp.setBorder(new LineBorder(BORDER_COLOR));
    sp.getViewport().setBackground(BG_DARK);

    p.add(sp, BorderLayout.CENTER);

    return p;
  }

  private JTable createStyledTable(DefaultTableModel m) {
    JTable t = new JTable(m);
    t.setRowHeight(50);
    t.setBackground(new Color(30, 30, 30));
    t.setForeground(TXT_PRIMARY);
    t.setShowVerticalLines(false);
    t.setSelectionBackground(new Color(50, 50, 50));
    t.setSelectionForeground(Color.WHITE);
    t.setFont(new Font("SansSerif", Font.PLAIN, 14));

    t.getTableHeader().setBackground(new Color(25, 25, 25));
    t.getTableHeader().setForeground(TXT_PRIMARY);
    t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
    t.getTableHeader().setPreferredSize(new Dimension(0, 45));

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);

    t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        l.setBorder(new EmptyBorder(0, 10, 0, 0));
        if (!isSelected)
          l.setBackground(row % 2 == 0 ? new Color(30, 30, 30) : new Color(35, 35, 35));

        // Specific logic for History Status column
        if (table == historyTable && column == 5) {
          String status = (String) value;
          if ("Working".equals(status))
            l.setForeground(new Color(0, 200, 100)); // Green
          else
            l.setForeground(TXT_SECONDARY);
        }
        // Specific logic for Hours column
        else if (table == historyTable && column == 6) {
          l.setForeground(new Color(255, 215, 0)); // Gold
          l.setFont(new Font("SansSerif", Font.BOLD, 14));
        } else {
          l.setForeground(TXT_PRIMARY);
        }

        return l;
      }
    });

    return t;
  }

  private void loadShiftData() {
    shiftModel.setRowCount(0);
    List<CaLamViec> list = shiftDAO.getAllCaLamViec();
    for (CaLamViec s : list) {
      shiftModel.addRow(new Object[] {
          s.getMaCa(),
          s.getTenCa(),
          s.getGioBatDau(),
          s.getGioKetThuc(),
          s
      });
    }
  }

  private void loadHistoryData() {
    // Get date from spinner
    java.util.Date selectedDate = new java.util.Date(); // Default today
    if (dateSpinner != null) {
      selectedDate = (java.util.Date) dateSpinner.getValue();
    }

    java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
    currentLogs = chamCongDAO.getHistoryByDate(sqlDate); // CACHE IT

    // Filter immediately after loading (usually empty search at first)
    filterHistory(txtSearchHistory != null ? txtSearchHistory.getText() : "");
  }

  private void filterHistory(String query) {
    if (currentLogs == null)
      return;

    historyModel.setRowCount(0);
    List<NhanVien> employees = nhanVienDAO.getAllNhanVien();
    List<CaLamViec> shifts = shiftDAO.getAllCaLamViec();

    // Map<Integer, Double> salaryMap =
    // employees.stream().collect(Collectors.toMap(NhanVien::getMaNV,
    // NhanVien::getLuongTheoGio)); // Unused
    Map<Integer, String> empMap = employees.stream().collect(Collectors.toMap(NhanVien::getMaNV, NhanVien::getHoTen));
    Map<Integer, String> shiftMap = shifts.stream().collect(Collectors.toMap(CaLamViec::getMaCa, CaLamViec::getTenCa));

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // No longer need currency format if showing hours

    String q = query.toLowerCase();

    for (ChamCong c : currentLogs) {
      String empName = empMap.getOrDefault(c.getMaNV(), "Unknown #" + c.getMaNV());
      String shiftName = shiftMap.getOrDefault(c.getMaCa(), "Shift #" + c.getMaCa());

      // Filter Logic
      String normQ = removeAccent(q);
      String normEmp = removeAccent(empName.toLowerCase());
      String normShift = removeAccent(shiftName.toLowerCase());

      boolean match = String.valueOf(c.getMaNV()).contains(q) ||
          normEmp.contains(normQ) ||
          normShift.contains(normQ);

      if (!match)
        continue;

      String checkIn = c.getThoiGianCheckIn() != null ? sdf.format(c.getThoiGianCheckIn()) : "-";
      String checkOut = c.getThoiGianCheckOut() != null ? sdf.format(c.getThoiGianCheckOut()) : "-";
      String status = (c.getThoiGianCheckOut() == null) ? "Working" : "Finished";

      String hourStr = "-";
      if (c.getThoiGianCheckIn() != null && c.getThoiGianCheckOut() != null) {
        long durationMillis = c.getThoiGianCheckOut().getTime() - c.getThoiGianCheckIn().getTime();
        double hours = durationMillis / (1000.0 * 60 * 60);
        // double rate = salaryMap.getOrDefault(c.getMaNV(), 20000.0);
        // double salary = hours * rate;
        // Format to 2 decimal places e.g. "4.50 hrs"
        hourStr = String.format("%.2f hrs", hours);
      }

      historyModel.addRow(new Object[] {
          c.getMaChamCong(),
          empName,
          shiftName,
          checkIn,
          checkOut,
          status,
          hourStr
      });
    }
  }

  // Helper to remove accents
  private String removeAccent(String s) {
    if (s == null)
      return "";
    String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
  }

  private void showShiftDialog(CaLamViec existing) {
    JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
        existing == null ? "Add Shift" : "Edit Shift", true);
    dlg.setSize(400, 350);
    dlg.setLocationRelativeTo(this);

    JPanel p = new JPanel(null);
    p.setBackground(BG_DARK);

    JLabel lName = new JLabel("Shift Name");
    lName.setForeground(TXT_SECONDARY);
    lName.setBounds(30, 20, 100, 20);
    p.add(lName);

    JTextField txtName = new JTextField();
    if (existing != null)
      txtName.setText(existing.getTenCa());
    styleInput(txtName);
    txtName.setBounds(30, 45, 320, 35);
    p.add(txtName);

    JLabel lStart = new JLabel("Start (HH:mm:ss)");
    lStart.setForeground(TXT_SECONDARY);
    lStart.setBounds(30, 90, 150, 20);
    p.add(lStart);

    JTextField txtStart = new JTextField("08:00:00");
    if (existing != null)
      txtStart.setText(existing.getGioBatDau().toString());
    styleInput(txtStart);
    txtStart.setBounds(30, 115, 320, 35);
    p.add(txtStart);

    JLabel lEnd = new JLabel("End (HH:mm:ss)");
    lEnd.setForeground(TXT_SECONDARY);
    lEnd.setBounds(30, 160, 150, 20);
    p.add(lEnd);

    JTextField txtEnd = new JTextField("16:00:00");
    if (existing != null)
      txtEnd.setText(existing.getGioKetThuc().toString());
    styleInput(txtEnd);
    txtEnd.setBounds(30, 185, 320, 35);
    p.add(txtEnd);

    JButton btnSave = new JButton("Save");
    btnSave.setBackground(ACCENT_RED);
    btnSave.setForeground(Color.WHITE);
    btnSave.setBounds(30, 250, 320, 40);
    btnSave.setFocusPainted(false);
    btnSave.addActionListener(e -> {
      try {
        String name = txtName.getText();
        Time start = Time.valueOf(txtStart.getText());
        Time end = Time.valueOf(txtEnd.getText());

        if (existing == null) {
          CaLamViec clv = new CaLamViec(0, name, start, end);
          shiftDAO.addCaLamViec(clv);
        } else {
          existing.setTenCa(name);
          existing.setGioBatDau(start);
          existing.setGioKetThuc(end);
          shiftDAO.updateCaLamViec(existing);
        }
        loadShiftData();
        dlg.dispose();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dlg, "Invalid Time Format (HH:mm:ss)");
      }
    });
    p.add(btnSave);

    dlg.setContentPane(p);
    dlg.setVisible(true);
  }

  private void styleInput(JTextField t) {
    t.setBackground(new Color(40, 40, 40));
    t.setForeground(Color.WHITE);
    t.setCaretColor(Color.WHITE);
    t.setBorder(new LineBorder(new Color(60, 60, 60)));
  }

  // Actions Renderer
  class ActionIconsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    public ActionIconsRenderer() {
      setLayout(new GridLayout(1, 2, 10, 0));
      setOpaque(true);
      add(createIconLabel("✎"));
      add(createIconLabel("🗑"));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int col) {
      setBackground(
          isSelected ? new Color(50, 50, 50) : (row % 2 == 0 ? new Color(30, 30, 30) : new Color(35, 35, 35)));
      return this;
    }

    private JLabel createIconLabel(String s) {
      JLabel l = new JLabel(s, SwingConstants.CENTER);
      l.setForeground(TXT_SECONDARY);
      l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
      return l;
    }
  }

  class ActionIconsEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
    JPanel p;
    CaLamViec current;

    public ActionIconsEditor() {
      p = new JPanel(new GridLayout(1, 2, 10, 0));
      p.setOpaque(true);
      JButton btnEdit = createButton("✎");
      JButton btnDel = createButton("🗑");

      btnEdit.addActionListener(e -> {
        fireEditingStopped();
        if (current != null)
          showShiftDialog(current);
      });
      btnDel.addActionListener(e -> {
        fireEditingStopped();
        if (current != null) {
          if (JOptionPane.showConfirmDialog(null,
              "Delete shift " + current.getTenCa() + "?") == JOptionPane.YES_OPTION) {
            shiftDAO.deleteCaLamViec(current.getMaCa());
            loadShiftData();
          }
        }
      });
      p.add(btnEdit);
      p.add(btnDel);
    }

    private JButton createButton(String s) {
      JButton b = new JButton(s);
      b.setContentAreaFilled(false);
      b.setBorderPainted(false);
      b.setForeground(Color.WHITE);
      b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
      return b;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      current = (CaLamViec) value;
      p.setBackground(new Color(45, 45, 45));
      return p;
    }

    public Object getCellEditorValue() {
      return current;
    }
  }
}
