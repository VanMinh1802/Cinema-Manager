package com.cinema.gui;

import com.cinema.dao.PhongChieuDAO;
import com.cinema.dto.PhongChieu;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class RoomPanel extends JPanel {

  private PhongChieuDAO phongDAO = new PhongChieuDAO();
  private JPanel gridPanel;
  private JButton btnTotal;

  // Colors
  private static final Color BG_MAIN = Color.decode("#121212"); // Darker for high contrast
  private static final Color BG_CARD = Color.decode("#1E1E1E");

  private static final Color TXT_PRIMARY = Color.WHITE;
  private static final Color TXT_SECONDARY = Color.GRAY;

  // --- Sidebar Components ---
  private JPanel sidebarPanel;
  private PhongChieu selectedRoom;
  private JTextField txtPriceStandard, txtPriceVIP, txtPriceDouble;
  private JLabel lblSelectedRoom;

  public RoomPanel() {
    setLayout(new BorderLayout());
    setBackground(BG_MAIN);

    // Header
    add(createHeader(), BorderLayout.NORTH);

    // Split Pane for Content (Left) and Pricing (Right)
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setResizeWeight(0.85); // 85% for content, 15% for sidebar
    splitPane.setDividerSize(5);
    splitPane.setBorder(null);
    splitPane.setOpaque(false);

    // Left: Grid
    JScrollPane scroll = new JScrollPane(createContent());
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);

    // Right: Sidebar
    sidebarPanel = createSidebar();

    splitPane.setLeftComponent(scroll);
    splitPane.setRightComponent(sidebarPanel);

    add(splitPane, BorderLayout.CENTER);
  }

  // Same Header
  private JPanel createHeader() {
    JPanel p = new JPanel(new BorderLayout());
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(30, 40, 20, 40));

    // Title
    JPanel pText = new JPanel(new GridLayout(2, 1));
    pText.setOpaque(false);
    JLabel lblTitle = new JLabel("Room Management");
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
    lblTitle.setForeground(TXT_PRIMARY);

    JLabel lblSub = new JLabel("Configure theater halls, seating layouts, and projection technology.");
    lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
    lblSub.setForeground(TXT_SECONDARY);

    pText.add(lblTitle);
    pText.add(lblSub);
    p.add(pText, BorderLayout.WEST);

    // Filter / Search (Visual Only)
    JPanel pAction = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pAction.setOpaque(false);
    JButton btnFilter = new JButton("Filter");
    styleButton(btnFilter, new Color(40, 40, 40));
    btnFilter.addActionListener(e -> showFilterDialog());
    pAction.add(btnFilter);

    btnTotal = new JButton("Total Rooms: " + phongDAO.getAllPhong().size());
    styleButton(btnTotal, new Color(40, 40, 40));
    btnTotal.setEnabled(false);
    pAction.add(btnTotal);

    p.add(pAction, BorderLayout.EAST);

    return p;
  }

  private JPanel createSidebar() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(new Color(25, 25, 25));
    p.setBorder(new EmptyBorder(20, 20, 20, 20));
    p.setPreferredSize(new Dimension(340, 0)); // Fixed width for Sidebar

    // Sidebar Header
    JPanel pHead = new JPanel(new GridLayout(2, 1, 0, 5));
    pHead.setOpaque(false);

    JLabel lblTitle = new JLabel("PRICING CONFIGURATION");
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
    lblTitle.setForeground(Color.GRAY);

    lblSelectedRoom = new JLabel("Select a Room");
    lblSelectedRoom.setFont(new Font("SansSerif", Font.BOLD, 18));
    lblSelectedRoom.setForeground(Color.WHITE);

    pHead.add(lblTitle);
    pHead.add(lblSelectedRoom);
    p.add(pHead, BorderLayout.NORTH);

    // Form
    JPanel pForm = new JPanel(new GridBagLayout());
    pForm.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 0, 5, 0);
    gbc.weightx = 1.0;
    gbc.gridx = 0;

    // Standard
    gbc.gridy = 0;
    pForm.add(createPriceLabel("STANDARD"), gbc);
    gbc.gridy = 1;
    txtPriceStandard = createPriceInput();
    pForm.add(txtPriceStandard, gbc);

    // VIP
    gbc.gridy = 2;
    pForm.add(createPriceLabel("VIP TIER"), gbc);
    gbc.gridy = 3;
    txtPriceVIP = createPriceInput();
    pForm.add(txtPriceVIP, gbc);

    // Double
    gbc.gridy = 4;
    pForm.add(createPriceLabel("DOUBLE/COUPLE"), gbc);
    gbc.gridy = 5;
    txtPriceDouble = createPriceInput();
    pForm.add(txtPriceDouble, gbc);

    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setOpaque(false);
    wrapper.add(pForm, BorderLayout.NORTH);
    p.add(wrapper, BorderLayout.CENTER);

    // Save Button
    JButton btnSave = new JButton("Save Configuration");
    styleButton(btnSave, Color.decode("#D32F2F")); // ACCENT_RED
    btnSave.setPreferredSize(new Dimension(0, 40));
    btnSave.addActionListener(e -> savePricing());
    p.add(btnSave, BorderLayout.SOUTH);

    // Edit Layout Button (Top of form or below title)
    JButton btnLayout = new JButton("Open Seat Layout Editor");
    styleButton(btnLayout, new Color(40, 60, 80));
    btnLayout.addActionListener(e -> {
      if (selectedRoom != null)
        showSeatLayoutDialog(selectedRoom);
    });

    // Wrapper for buttons
    JPanel pBtns = new JPanel(new GridLayout(2, 1, 0, 10));
    pBtns.setOpaque(false);
    pBtns.add(btnLayout);
    pBtns.add(btnSave);

    p.add(pBtns, BorderLayout.SOUTH);

    // Initially disabled
    toggleSidebar(false);

    return p;
  }

  private JLabel createPriceLabel(String text) {
    JLabel l = new JLabel(text);
    l.setFont(new Font("SansSerif", Font.BOLD, 10));
    l.setForeground(Color.GRAY);
    return l;
  }

  private JTextField createPriceInput() {
    JTextField t = new JTextField("0.0");
    t.setBackground(new Color(40, 40, 40));
    t.setForeground(Color.WHITE);
    t.setBorder(new EmptyBorder(10, 10, 10, 10));
    t.setCaretColor(Color.WHITE);
    return t;
  }

  private void toggleSidebar(boolean enable) {
    txtPriceStandard.setEnabled(enable);
    txtPriceVIP.setEnabled(enable);
    txtPriceDouble.setEnabled(enable);
    if (!enable) {
      lblSelectedRoom.setText("Select a Room");
      txtPriceStandard.setText("");
      txtPriceVIP.setText("");
      txtPriceDouble.setText("");
    }
  }

  private JPanel createContent() {
    // Grid Layout matching screenshot (3 columns)
    gridPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // Keep 3 cols or reduce to 2 if sidebar takes space?
    // Let's try 3 for now, ScrollPane handles it
    gridPanel.setOpaque(false);
    gridPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

    loadRooms();

    // Wrap in Flow/Border to prevent Grid from stretching massive cells if valid
    // items are few
    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setOpaque(false);
    wrapper.add(gridPanel, BorderLayout.NORTH);
    return wrapper;
  }

  private void loadRooms() {
    loadRooms("", -1); // Default: No name filter, All statuses
  }

  private void loadRooms(String nameSearch, int statusFilter) { // statusFilter: -1=All, 1=Active, 0=Maintenance
    gridPanel.removeAll();
    List<PhongChieu> list = phongDAO.getAllPhong();

    int count = 0;
    for (PhongChieu pc : list) {
      // Filter Logic
      boolean matchName = nameSearch.isEmpty() || pc.getTenPhong().toLowerCase().contains(nameSearch.toLowerCase());
      boolean matchStatus = (statusFilter == -1) || (pc.getTinhTrang() == statusFilter);

      if (matchName && matchStatus) {
        gridPanel.add(createRoomCard(pc));
        count++;
      }
    }

    if (nameSearch.isEmpty() && statusFilter == -1) {
      gridPanel.add(createAddRoomCard());
    }

    gridPanel.revalidate();
    gridPanel.repaint();

    if (btnTotal != null) {
      btnTotal.setText("Total Rooms: " + count);
    }
  }

  private JPanel createRoomCard(PhongChieu pc) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(BG_CARD);
    card.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));
    card.setPreferredSize(new Dimension(300, 250)); // Fixed height

    // Make card clickable to select room
    card.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        selectRoom(pc);
      }
    });

    // 1. Mini Layout Visual (Top)
    JPanel visual = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(30, 30, 30));
        g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);

        // Draw mini seats
        g.setColor(new Color(60, 60, 60));
        int rows = 4;
        int cols = 8;
        int size = 6;
        int gap = 4;
        int startX = (getWidth() - (cols * (size + gap))) / 2;
        int startY = (getHeight() - (rows * (size + gap))) / 2;

        for (int r = 0; r < rows; r++) {
          for (int c = 0; c < cols; c++) {
            g.fillRect(startX + c * (size + gap), startY + r * (size + gap), size, size);
          }
        }
      }
    };
    visual.setPreferredSize(new Dimension(0, 100));
    visual.setOpaque(false);
    card.add(visual, BorderLayout.NORTH);

    // 2. Info (Center)
    JPanel info = new JPanel(new GridLayout(3, 1, 0, 5));
    info.setOpaque(false);
    info.setBorder(new EmptyBorder(10, 15, 10, 15));

    JLabel lblName = new JLabel(pc.getTenPhong());
    lblName.setFont(new Font("SansSerif", Font.BOLD, 16));
    lblName.setForeground(TXT_PRIMARY);

    JLabel lblSeats = new JLabel("\uD83D\uDCBA " + pc.getSoLuongGhe() + " Seats"); // Seat Icon
    lblSeats.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lblSeats.setForeground(TXT_SECONDARY);

    // Mock Tags
    info.add(lblName);
    info.add(lblSeats);
    card.add(info, BorderLayout.CENTER);

    // 3. Actions (Bottom)
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    actions.setOpaque(false);
    actions.setBorder(new EmptyBorder(0, 10, 10, 10));

    JButton btnEdit = new JButton("EDIT");
    styleButton(btnEdit, new Color(50, 40, 40));
    btnEdit.setForeground(new Color(255, 100, 100));
    btnEdit.setPreferredSize(new Dimension(60, 30)); // Smaller
    btnEdit.addActionListener(e -> editRoom(pc));

    JButton btnDel = new JButton(pc.getTinhTrang() == 1 ? "LOCK" : "UNLOCK");
    styleButton(btnDel, pc.getTinhTrang() == 1 ? new Color(50, 40, 40) : new Color(40, 60, 40));
    btnDel.setForeground(pc.getTinhTrang() == 1 ? new Color(255, 100, 100) : new Color(100, 255, 100));
    btnDel.setPreferredSize(new Dimension(70, 30));
    btnDel.addActionListener(e -> toggleStatus(pc));

    actions.add(btnEdit);
    actions.add(btnDel);

    // Status Indicator
    JLabel lblStatus = new JLabel(pc.getTinhTrang() == 1 ? "●" : "●");
    lblStatus.setForeground(pc.getTinhTrang() == 1 ? new Color(100, 255, 100) : new Color(255, 165, 0));
    actions.add(Box.createHorizontalStrut(5));
    actions.add(lblStatus);

    card.add(actions, BorderLayout.SOUTH);

    return card;
  }

  private JPanel createAddRoomCard() {
    JPanel card = new JPanel(new GridBagLayout());
    card.setBackground(new Color(25, 20, 20));
    card.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));

    JLabel lblPlus = new JLabel("+");
    lblPlus.setFont(new Font("SansSerif", Font.PLAIN, 40));
    lblPlus.setForeground(new Color(100, 100, 100));

    JLabel lblText = new JLabel("Add New Cinema Room");
    lblText.setFont(new Font("SansSerif", Font.BOLD, 14));
    lblText.setForeground(TXT_PRIMARY);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    card.add(lblPlus, gbc);
    gbc.gridy = 1;
    gbc.insets = new Insets(10, 0, 0, 0);
    card.add(lblText, gbc);

    // Hover Effect
    card.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        card.setBackground(new Color(35, 30, 30));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }

      public void mouseExited(MouseEvent e) {
        card.setBackground(new Color(25, 20, 20));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }

      public void mouseClicked(MouseEvent e) {
        showAddDialog();
      }
    });

    return card;
  }

  // --- Interactions ---
  private void selectRoom(PhongChieu pc) {
    this.selectedRoom = pc;
    lblSelectedRoom.setText(pc.getTenPhong());
    toggleSidebar(true);

    // Load Prices
    java.util.Map<String, Double> prices = phongDAO.getRoomPricing(pc.getMaPhong());
    txtPriceStandard.setText(String.valueOf(prices.getOrDefault("Standard", 0.0)));
    txtPriceVIP.setText(String.valueOf(prices.getOrDefault("VIP", 0.0)));
    txtPriceDouble.setText(String.valueOf(prices.getOrDefault("Double", 0.0)));
  }

  private void savePricing() {
    if (selectedRoom == null)
      return;
    try {
      double std = Double.parseDouble(txtPriceStandard.getText().trim());
      double vip = Double.parseDouble(txtPriceVIP.getText().trim());
      double dbl = Double.parseDouble(txtPriceDouble.getText().trim());

      phongDAO.updateRoomPricing(selectedRoom.getMaPhong(), "Standard", std);
      phongDAO.updateRoomPricing(selectedRoom.getMaPhong(), "VIP", vip);
      phongDAO.updateRoomPricing(selectedRoom.getMaPhong(), "Double", dbl);

      JOptionPane.showMessageDialog(this, "Pricing Saved Successfully!");
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Invalid Price Format! Use numbers only.");
    }
  }

  // --- Actions ---
  private void toggleStatus(PhongChieu pc) {
    int current = pc.getTinhTrang();
    int newItem = (current == 1) ? 0 : 1;
    String action = (newItem == 1) ? "ACTIVATE" : "DEACTIVATE";

    int cf = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to " + action + " room " + pc.getTenPhong()
            + "?\n(Maintenance mode disables scheduling)",
        "Confirm Status Change", JOptionPane.YES_NO_OPTION);

    if (cf == JOptionPane.YES_OPTION) {
      phongDAO.updateStatus(pc.getMaPhong(), newItem);
      loadRooms();
    }
  }

  private void editRoom(PhongChieu pc) {
    JTextField txtName = new JTextField(pc.getTenPhong());
    JSpinner spnSeats = new JSpinner(new SpinnerNumberModel(pc.getSoLuongGhe(), 1, 1000, 10));
    JCheckBox chkReset = new JCheckBox("Regenerate Seat Layout (Warning: Resets all seats)");

    Object[] msg = {
        "Room Name:", txtName,
        "Capacity:", spnSeats,
        chkReset
    };

    int res = JOptionPane.showConfirmDialog(this, msg, "Edit Room Details", JOptionPane.OK_CANCEL_OPTION);
    if (res == JOptionPane.OK_OPTION) {
      String name = txtName.getText().trim();
      int caps = (int) spnSeats.getValue();

      if (!name.isEmpty()) {
        phongDAO.updatePhong(pc.getMaPhong(), name, caps);

        if (chkReset.isSelected()) {
          new com.cinema.dao.SeatGenerator().generateSeats(pc.getMaPhong(), caps);
        }
        loadRooms();
      }
    }
  }

  private void showAddDialog() {
    JTextField txtName = new JTextField();
    JSpinner spnSeats = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 10));

    Object[] msg = {
        "Room Name:", txtName,
        "Capacity:", spnSeats
    };

    int res = JOptionPane.showConfirmDialog(this, msg, "Add New Room", JOptionPane.OK_CANCEL_OPTION);
    if (res == JOptionPane.OK_OPTION) {
      String name = txtName.getText().trim();
      int caps = (int) spnSeats.getValue();

      if (!name.isEmpty()) {
        phongDAO.addPhong(name, caps);
        // Fetch room by name to get ID
        for (PhongChieu p : phongDAO.getAllPhong()) {
          if (p.getTenPhong().equals(name)) {
            new com.cinema.dao.SeatGenerator().generateSeats(p.getMaPhong(), caps);
            break;
          }
        }

        loadRooms();
      }
    }
  }

  // --- Filter Dialog ---
  private void showFilterDialog() {
    JTextField txtSearch = new JTextField(15);
    String[] statuses = { "All", "Active", "Maintenance" };
    JComboBox<String> cboStatus = new JComboBox<>(statuses);

    JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
    panel.add(new JLabel("Search Name:"));
    panel.add(txtSearch);
    panel.add(new JLabel("Status:"));
    panel.add(cboStatus);

    int res = JOptionPane.showConfirmDialog(this, panel, "Filter Rooms", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);

    if (res == JOptionPane.OK_OPTION) {
      String search = txtSearch.getText().trim();
      int statusIdx = cboStatus.getSelectedIndex();
      int statusCode = -1;
      if (statusIdx == 1)
        statusCode = 1; // Active
      if (statusIdx == 2)
        statusCode = 0; // Maintenance

      loadRooms(search, statusCode);
    }
  }

  // --- Seat Layout Editor Dialog ---
  private void showSeatLayoutDialog(PhongChieu pc) {
    JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), "Seat Layout Editor: " + pc.getTenPhong(),
        Dialog.ModalityType.APPLICATION_MODAL);
    d.setSize(900, 600);
    d.setLocationRelativeTo(this);
    d.setLayout(new BorderLayout());
    d.getContentPane().setBackground(new Color(20, 20, 20));

    // Header
    JPanel pHeader = new JPanel(new BorderLayout());
    pHeader.setBackground(new Color(30, 30, 30));
    pHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
    JLabel lblInst = new JLabel("<html><b>INSTRUCTIONS:</b> Click a seat to toggle its type.<br>" +
        "<font color='gray'>Standard</font> → <font color='#FFC107'>VIP</font> → <font color='#E91E63'>Double</font></html>");
    lblInst.setForeground(Color.WHITE);
    lblInst.setFont(new Font("SansSerif", Font.PLAIN, 14));
    pHeader.add(lblInst, BorderLayout.CENTER);
    d.add(pHeader, BorderLayout.NORTH);

    // Seat Grid
    JPanel pGrid = new JPanel(); // Layout set dynamically
    pGrid.setBackground(new Color(20, 20, 20));

    JScrollPane scroll = new JScrollPane(pGrid);
    scroll.setBorder(null);
    d.add(scroll, BorderLayout.CENTER);

    // Load Seats
    com.cinema.dao.GheDAO gheDAO = new com.cinema.dao.GheDAO();
    List<com.cinema.dao.GheDAO.SeatInfo> seats = gheDAO.getSeatsByRoom(pc.getMaPhong());

    // Calculate logic (same as BanVePanel map logic roughly)
    java.util.Map<String, java.util.List<com.cinema.dao.GheDAO.SeatInfo>> rowMap = new java.util.TreeMap<>();
    int maxCol = 0;
    for (com.cinema.dao.GheDAO.SeatInfo s : seats) {
      String row = s.tenGhe.substring(0, 1);
      try {
        int col = Integer.parseInt(s.tenGhe.substring(1));
        maxCol = Math.max(maxCol, col);
      } catch (Exception e) {
      }
      rowMap.computeIfAbsent(row, k -> new java.util.ArrayList<>()).add(s);
    }

    if (maxCol == 0)
      maxCol = 10;
    pGrid.setLayout(new GridLayout(rowMap.size(), maxCol + 1, 5, 5)); // +1 for label
    pGrid.setBorder(new EmptyBorder(20, 50, 20, 50));

    for (java.util.Map.Entry<String, java.util.List<com.cinema.dao.GheDAO.SeatInfo>> entry : rowMap.entrySet()) {
      // Label
      JLabel l = new JLabel(entry.getKey(), SwingConstants.CENTER);
      l.setForeground(Color.WHITE);
      pGrid.add(l);

      // Sort
      entry.getValue().sort((s1, s2) -> {
        int c1 = Integer.parseInt(s1.tenGhe.substring(1));
        int c2 = Integer.parseInt(s2.tenGhe.substring(1));
        return Integer.compare(c1, c2);
      });

      int currentPos = 1;
      for (com.cinema.dao.GheDAO.SeatInfo s : entry.getValue()) {
        int col = Integer.parseInt(s.tenGhe.substring(1));
        while (currentPos < col) {
          pGrid.add(Box.createGlue());
          currentPos++;
        }

        JButton btn = new JButton(s.tenGhe);
        styleSeatButton(btn, s.loaiGhe);
        btn.addActionListener(e -> {
          String newType = getNextType(s.loaiGhe);
          if (gheDAO.updateSeatType(s.maGhe, newType)) {
            s.loaiGhe = newType;
            styleSeatButton(btn, newType);
          }
        });
        pGrid.add(btn);
        currentPos++;
      }
      while (currentPos <= maxCol) {
        pGrid.add(Box.createGlue());
        currentPos++;
      }
    }

    d.setVisible(true);
  }

  private String getNextType(String current) {
    if (current == null)
      return "VIP";
    if (current.equalsIgnoreCase("Thuong") || current.equalsIgnoreCase("Standard"))
      return "VIP";
    if (current.equalsIgnoreCase("VIP"))
      return "Double";
    return "Standard";
  }

  private void styleSeatButton(JButton b, String type) {
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    b.setBorder(new LineBorder(Color.DARK_GRAY));
    if (type == null || type.equalsIgnoreCase("Thuong") || type.equalsIgnoreCase("Standard")) {
      b.setBackground(new Color(60, 60, 60)); // Gray
    } else if (type.equalsIgnoreCase("VIP")) {
      b.setBackground(Color.decode("#FFC107")); // Amber
      b.setForeground(Color.BLACK);
    } else if (type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("Doi")) {
      b.setBackground(Color.decode("#E91E63")); // Pink
    }
  }

  // Styles
  private void styleButton(JButton b, Color bg) {
    b.setBackground(bg);
    b.setForeground(TXT_PRIMARY);
    b.setFocusPainted(false);
    b.setBorderPainted(false);
    b.setFont(new Font("SansSerif", Font.BOLD, 11));
  }
}
