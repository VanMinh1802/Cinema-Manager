package com.cinema.gui;

import com.cinema.dao.KhachHangDAO;
import com.cinema.dao.LoyaltyDAO;
import com.cinema.dto.KhachHang;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;

public class LoyaltyPanel extends JPanel {

  private KhachHangDAO memberDAO = new KhachHangDAO();
  private LoyaltyDAO loyaltyDAO = new LoyaltyDAO();

  // Components
  private JTable tblMembers;
  private DefaultTableModel memberModel;

  private JTextField txtPtsPerDollar; // Actually per unit (e.g., 10k VND)
  private JTextField txtRedeemValue; // Value of 1 point (e.g. 1000 VND)

  public LoyaltyPanel() {
    setLayout(new BorderLayout());
    setBackground(Color.decode("#121212"));

    add(createHeader(), BorderLayout.NORTH);

    // Split: Left (Members) vs Right (Rules)
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    split.setResizeWeight(0.7);
    split.setOpaque(false);
    split.setBorder(null);
    split.setDividerSize(5);

    split.setLeftComponent(createMemberPanel());
    split.setRightComponent(createRulesPanel());

    add(split, BorderLayout.CENTER);
  }

  private JPanel createHeader() {
    JPanel p = new JPanel(new BorderLayout());
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(30, 40, 20, 40));

    JLabel lblTitle = new JLabel("Loyalty Program");
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
    lblTitle.setForeground(Color.WHITE);

    p.add(lblTitle, BorderLayout.WEST);
    return p;
  }

  private javax.swing.table.TableRowSorter<DefaultTableModel> sorter;

  private JPanel createMemberPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(20, 40, 40, 20));

    // Top Bar: Label + Search + Add Button
    JPanel pTop = new JPanel(new BorderLayout(15, 0));
    pTop.setOpaque(false);
    pTop.setBorder(new EmptyBorder(0, 0, 10, 0));

    JLabel lbl = new JLabel("MEMBER DIRECTORY");
    lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
    lbl.setForeground(Color.LIGHT_GRAY);
    pTop.add(lbl, BorderLayout.WEST);

    // Search Bar (Real-time)
    JTextField txtSearch = new JTextField(" Search members...");
    styleInput(txtSearch);
    txtSearch.setPreferredSize(new Dimension(250, 35));
    txtSearch.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        if (txtSearch.getText().equals(" Search members...")) {
          txtSearch.setText("");
        }
      }

      public void focusLost(FocusEvent e) {
        if (txtSearch.getText().isEmpty()) {
          txtSearch.setText(" Search members...");
        }
      }
    });
    txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
      public void insertUpdate(javax.swing.event.DocumentEvent e) {
        filter();
      }

      public void removeUpdate(javax.swing.event.DocumentEvent e) {
        filter();
      }

      public void changedUpdate(javax.swing.event.DocumentEvent e) {
        filter();
      }

      private void filter() {
        String text = txtSearch.getText();
        if (text.equals(" Search members...") || text.isEmpty()) {
          if (sorter != null)
            sorter.setRowFilter(null);
        } else {
          if (sorter != null)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
      }
    });

    JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    pRight.setOpaque(false);
    pRight.add(txtSearch);

    JButton btnAdd = new JButton("+ Add New Member");
    styleButton(btnAdd, new Color(46, 125, 50));
    btnAdd.setFont(new Font("SansSerif", Font.BOLD, 12));
    btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btnAdd.addActionListener(e -> showMemberDialog(null));
    pRight.add(btnAdd);

    pTop.add(pRight, BorderLayout.EAST);
    p.add(pTop, BorderLayout.NORTH);

    // Table
    String[] cols = { "ID", "Name", "Phone", "Tier", "Points", "Total Spent", "ACTIONS" };
    memberModel = new DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int row, int col) {
        return col == 6; // Only Actions column
      }
    };
    tblMembers = new JTable(memberModel);
    tblMembers.setRowHeight(45);
    tblMembers.setShowVerticalLines(false);
    tblMembers.setIntercellSpacing(new Dimension(0, 0));
    tblMembers.setBackground(new Color(30, 30, 30));
    tblMembers.setForeground(new Color(220, 220, 220));
    tblMembers.setSelectionBackground(new Color(50, 50, 50));
    tblMembers.setSelectionForeground(Color.WHITE);

    // Custom Header Renderer
    javax.swing.table.JTableHeader header = tblMembers.getTableHeader();
    header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        l.setBackground(new Color(20, 20, 20));
        l.setForeground(new Color(180, 180, 180));
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setBorder(new EmptyBorder(10, 10, 10, 10)); // Top/Bottom padding
        l.setHorizontalAlignment(SwingConstants.LEFT);
        if (column == 6)
          l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
      }
    });

    // Custom Cell Renderer for alignment & padding
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
          c.setBackground(row % 2 == 0 ? new Color(30, 30, 30) : new Color(35, 35, 38)); // Zebra stripe
        }
        setBorder(new EmptyBorder(0, 10, 0, 10));
        return c;
      }
    };
    for (int i = 0; i < 6; i++)
      tblMembers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

    // Actions Renderer (No Editor needed anymore)
    tblMembers.getColumnModel().getColumn(6).setCellRenderer(new ActionIconsRenderer());
    tblMembers.getColumnModel().getColumn(6).setMinWidth(140);
    tblMembers.getColumnModel().getColumn(6).setMaxWidth(160);

    // Mouse Listener for Actions
    tblMembers.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = tblMembers.rowAtPoint(e.getPoint());
        int col = tblMembers.columnAtPoint(e.getPoint());

        if (row >= 0 && col == 6) {
          // Get the object at this row
          // Convert view row to model row (sorting!)
          int modelRow = tblMembers.convertRowIndexToModel(row);
          KhachHang k = (KhachHang) memberModel.getValueAt(modelRow, 6);

          // Determine Edit vs Delete based on X position
          Rectangle cellRect = tblMembers.getCellRect(row, col, false);
          int x = e.getX() - cellRect.x;
          int w = cellRect.width;

          if (x < w / 2) {
            // Edit (Left side)
            if (k != null)
              showMemberDialog(k);
          } else {
            // Delete (Right side)
            if (k != null) {
              int cf = JOptionPane.showConfirmDialog(LoyaltyPanel.this,
                  "Delete member " + k.getHoTen() + "?", "Confirm",
                  JOptionPane.YES_NO_OPTION);
              if (cf == JOptionPane.YES_OPTION) {
                if (memberDAO.deleteKhachHang(k.getMaKH())) {
                  loadMembers();
                } else {
                  JOptionPane.showMessageDialog(LoyaltyPanel.this, "Could not delete (maybe dependent data exists).");
                }
              }
            }
          }
        }
      }
    });

    loadMembers();

    sorter = new javax.swing.table.TableRowSorter<>(memberModel);
    tblMembers.setRowSorter(sorter);

    JScrollPane scroll = new JScrollPane(tblMembers);
    scroll.getViewport().setBackground(new Color(20, 20, 20)); // Match Header
    scroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(50, 50, 50)));
    p.add(scroll, BorderLayout.CENTER);

    return p;
  }

  // Note: Removed ActionIconsEditor class as it is replaced by MouseListener

  private void loadMembers() {
    memberModel.setRowCount(0);
    java.util.List<KhachHang> list = memberDAO.getAllKhachHang();
    for (KhachHang k : list) {
      memberModel.addRow(new Object[] {
          k.getMaKH(), k.getHoTen(), k.getSdt(),
          k.getHangThanhVien(), k.getDiemTichLuy(),
          String.format("%,.0f", k.getTongChiTieu()),
          k // Pass object for actions
      });
    }
  }

  // --- Dialog for Add/Edit ---
  private void showMemberDialog(KhachHang existingMember) {
    JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
        existingMember == null ? "Add New Member" : "Edit Member", Dialog.ModalityType.APPLICATION_MODAL);
    dlg.setSize(400, 300);
    dlg.setLocationRelativeTo(this);

    JPanel p = new JPanel(null);
    p.setBackground(new Color(30, 30, 30));

    JLabel lName = new JLabel("Full Name");
    lName.setForeground(Color.GRAY);
    lName.setBounds(30, 20, 100, 20);
    p.add(lName);

    JTextField txtName = new JTextField();
    if (existingMember != null)
      txtName.setText(existingMember.getHoTen());
    styleInput(txtName);
    txtName.setBounds(30, 45, 320, 35);
    p.add(txtName);

    JLabel lPhone = new JLabel("Phone Number");
    lPhone.setForeground(Color.GRAY);
    lPhone.setBounds(30, 90, 100, 20);
    p.add(lPhone);

    JTextField txtPhone = new JTextField();
    if (existingMember != null)
      txtPhone.setText(existingMember.getSdt());
    styleInput(txtPhone);
    txtPhone.setBounds(30, 115, 320, 35);
    p.add(txtPhone);

    JButton btnSave = new JButton("Save");
    styleButton(btnSave, new Color(46, 125, 50));
    btnSave.setBounds(30, 180, 320, 40);
    btnSave.addActionListener(e -> {
      String n = txtName.getText().trim();
      String ph = txtPhone.getText().trim();
      if (n.isEmpty() || ph.isEmpty()) {
        JOptionPane.showMessageDialog(dlg, "Name and Phone are required.");
        return;
      }

      if (existingMember == null) {
        KhachHang newMem = new KhachHang(0, n, ph, "", null, 0, "Bronze", 0);
        memberDAO.insertKhachHang(newMem);
      } else {
        existingMember.setHoTen(n);
        existingMember.setSdt(ph);
        memberDAO.updateKhachHang(existingMember);
      }
      loadMembers();
      dlg.dispose();
    });
    p.add(btnSave);

    dlg.setContentPane(p);
    dlg.setVisible(true);
  }

  private JPanel createRulesPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(new Color(25, 25, 25)); // Sidebar bg
    p.setBorder(new EmptyBorder(20, 20, 20, 20));

    JPanel pContent = new JPanel(new GridBagLayout());
    pContent.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 0, 5, 0);
    gbc.weightx = 1.0;
    gbc.gridx = 0;

    // Title
    JLabel lblTitle = new JLabel("GLOBAL RULES");
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
    lblTitle.setForeground(Color.LIGHT_GRAY);

    // 1. Accrual
    JLabel lblAccrual = new JLabel("Points per 10,000 VND Spent");
    lblAccrual.setForeground(Color.GRAY);
    txtPtsPerDollar = new JTextField("1");
    styleInput(txtPtsPerDollar);

    // 2. Redemption
    JLabel lblRedeem = new JLabel("VND Value per Point");
    lblRedeem.setForeground(Color.GRAY);
    txtRedeemValue = new JTextField("1000"); // 1 point = 1000vnd
    styleInput(txtRedeemValue);

    // Layout
    gbc.gridy = 0;
    pContent.add(lblTitle, gbc);
    gbc.gridy = 1;
    pContent.add(Box.createVerticalStrut(20), gbc);
    gbc.gridy = 2;
    pContent.add(lblAccrual, gbc);
    gbc.gridy = 3;
    pContent.add(txtPtsPerDollar, gbc);
    gbc.gridy = 4;
    pContent.add(Box.createVerticalStrut(10), gbc);
    gbc.gridy = 5;
    pContent.add(lblRedeem, gbc);
    gbc.gridy = 6;
    pContent.add(txtRedeemValue, gbc);

    p.add(pContent, BorderLayout.NORTH);

    JButton btnSave = new JButton("Save Configuration");
    styleButton(btnSave, Color.decode("#D32F2F"));
    btnSave.setPreferredSize(new Dimension(0, 40));
    btnSave.addActionListener(e -> saveRules());
    p.add(btnSave, BorderLayout.SOUTH);

    loadRules();

    return p;
  }

  // --- Logic ---
  // Searching is now handled by TableRowSorter in createMemberPanel

  private void loadRules() {
    Map<String, String> rules = loyaltyDAO.getRules();
    if (rules.containsKey("POINTS_PER_10K"))
      txtPtsPerDollar.setText(rules.get("POINTS_PER_10K"));
    if (rules.containsKey("POINT_VALUE_VND"))
      txtRedeemValue.setText(rules.get("POINT_VALUE_VND"));
  }

  private void saveRules() {
    loyaltyDAO.updateRule("POINTS_PER_10K", txtPtsPerDollar.getText());
    loyaltyDAO.updateRule("POINT_VALUE_VND", txtRedeemValue.getText());
    JOptionPane.showMessageDialog(this, "Rules updated successfully!");
  }

  // Styles
  private void styleButton(JButton b, Color bg) {
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    b.setBorderPainted(false);
  }

  private void styleInput(JTextField t) {
    t.setBackground(new Color(40, 40, 40));
    t.setForeground(Color.WHITE);
    t.setBorder(new EmptyBorder(10, 10, 10, 10));
    t.setCaretColor(Color.WHITE);
  }

  // --- Renderers & Editors for Actions ---
  class ActionIconsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    public ActionIconsRenderer() {
      setLayout(new GridLayout(1, 2, 10, 0)); // Grid layout for stability
      setOpaque(true);
      add(createIconLabel("✎"));
      add(createIconLabel("🗑"));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      setBackground(isSelected ? new Color(50, 50, 50) : new Color(30, 30, 30));
      return this;
    }

    private JLabel createIconLabel(String s) {
      JLabel l = new JLabel(s, SwingConstants.CENTER);
      l.setForeground(Color.LIGHT_GRAY);
      l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
      return l;
    }
  }

}
