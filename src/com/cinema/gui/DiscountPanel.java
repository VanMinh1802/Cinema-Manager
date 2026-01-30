package com.cinema.gui;

import com.cinema.dao.DiscountPolicyDAO;
import com.cinema.dao.WeeklyPromotionDAO;
import com.cinema.dto.DiscountPolicy;
import com.cinema.dto.WeeklyPromotion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscountPanel extends JPanel {

  // Colors
  private static final Color BG_DARK = new Color(18, 18, 18);
  private static final Color ACCENT_RED = new Color(229, 9, 20);
  private static final Color TXT_PRIMARY = new Color(240, 240, 240);
  private static final Color TXT_SECONDARY = new Color(150, 150, 150);
  private static final Color BORDER_COLOR = new Color(60, 60, 60);

  // Toggle Colors

  // DAOs
  private DiscountPolicyDAO policyDAO;
  private WeeklyPromotionDAO promoDAO;

  // UI Components
  private CardLayout cardLayout;
  private JPanel mainContentPanel;
  private JPanel pCardList;
  private PromoEditorPanel editorPanel;
  private JTable policyTable;
  private DefaultTableModel policyModel;

  // Tab Buttons
  private JToggleButton btnTabPolicy;
  private JToggleButton btnTabWeekly;

  // Data
  private List<WeeklyPromotion> weeklyPromos;
  private WeeklyPromotion selectedPromo;
  private javax.swing.table.TableRowSorter<DefaultTableModel> sorter;

  public DiscountPanel() {
    policyDAO = new DiscountPolicyDAO();
    promoDAO = new WeeklyPromotionDAO();

    setLayout(new BorderLayout());
    setBackground(BG_DARK);

    // Header
    add(createHeader(), BorderLayout.NORTH);

    // Main Content (Card Layout)
    cardLayout = new CardLayout();
    mainContentPanel = new JPanel(cardLayout);
    mainContentPanel.setBackground(BG_DARK);

    // Tab 1: Demographic Policies
    mainContentPanel.add(createPolicyPanel(), "TAB_POLICY");

    // Tab 2: Weekly Promotions
    mainContentPanel.add(createWeeklyPromoLayout(), "TAB_WEEKLY");

    add(mainContentPanel, BorderLayout.CENTER);

    // Init state
    switchTab("TAB_POLICY");
  }

  private JPanel createHeader() {
    JPanel container = new JPanel(new BorderLayout());
    container.setBackground(BG_DARK);

    // Top Header Row (Title only)
    JPanel pTop = new JPanel(new BorderLayout());
    pTop.setBackground(BG_DARK);
    pTop.setBorder(new EmptyBorder(20, 30, 20, 30));

    JLabel lblTitle = new JLabel("Discounts & Promotions Admin");
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
    lblTitle.setForeground(TXT_PRIMARY);
    lblTitle.setIcon(new Icon() {
      public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(ACCENT_RED);
        g.fillRoundRect(x, y, 6, 24, 2, 2);
        g.fillRoundRect(x + 8, y + 4, 16, 16, 4, 4);
        g.setColor(Color.WHITE);
        g.fillOval(x + 13, y + 9, 6, 6);
      }

      public int getIconWidth() {
        return 24;
      }

      public int getIconHeight() {
        return 24;
      }
    });
    lblTitle.setIconTextGap(15);
    pTop.add(lblTitle, BorderLayout.WEST);

    // Bottom Row: Custom Tabs
    JPanel pTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    pTabs.setBackground(BG_DARK);
    pTabs.setBorder(new EmptyBorder(0, 30, 0, 30));

    btnTabPolicy = createTabButton("Demographic Policies");
    btnTabWeekly = createTabButton("Weekly Promotions");

    btnTabPolicy.addActionListener(e -> switchTab("TAB_POLICY"));
    btnTabWeekly.addActionListener(e -> switchTab("TAB_WEEKLY"));

    pTabs.add(btnTabPolicy);
    pTabs.add(Box.createHorizontalStrut(10));
    pTabs.add(btnTabWeekly);

    container.add(pTop, BorderLayout.NORTH);
    container.add(pTabs, BorderLayout.SOUTH);

    return container;
  }

  private void switchTab(String tabName) {
    cardLayout.show(mainContentPanel, tabName);

    boolean isPolicy = "TAB_POLICY".equals(tabName);
    btnTabPolicy.setSelected(isPolicy);
    btnTabWeekly.setSelected(!isPolicy);
  }

  private JToggleButton createTabButton(String text) {
    JToggleButton b = new JToggleButton(text);
    b.setPreferredSize(new Dimension(200, 40)); // Increased width to fit text
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

        // Background
        if (btn.isSelected()) {
          // Gradient or Solid Red
          g2.setColor(new Color(229, 9, 20)); // Netflix Red
          g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
          btn.setForeground(Color.WHITE);
        } else {
          g2.setColor(new Color(40, 40, 40)); // Dark Grey
          g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
          btn.setForeground(new Color(150, 150, 150));
        }

        super.paint(g, c);
      }
    });

    return b;
  }

  // =================================================================================
  // TAB 1: DEMOGRAPHIC POLICIES
  // =================================================================================
  private JPanel createPolicyPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BG_DARK);
    // Tool Bar (Search + Add)
    JPanel pTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    pTools.setBackground(BG_DARK);
    pTools.setBorder(new EmptyBorder(0, 0, 15, 0));

    JTextField txtSearch = new JTextField(" Search policies...");
    txtSearch.setPreferredSize(new Dimension(250, 35));
    txtSearch.setBackground(new Color(40, 40, 40));
    txtSearch.setForeground(TXT_SECONDARY);
    txtSearch.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(BORDER_COLOR),
        new EmptyBorder(0, 10, 0, 10)));

    txtSearch.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        if (txtSearch.getText().equals(" Search policies...")) {
          txtSearch.setText("");
          txtSearch.setForeground(TXT_PRIMARY);
        }
      }

      public void focusLost(FocusEvent e) {
        if (txtSearch.getText().isEmpty()) {
          txtSearch.setText(" Search policies...");
          txtSearch.setForeground(TXT_SECONDARY);
        }
      }
    });

    txtSearch.getDocument().addDocumentListener(new DocumentListener() {
      public void insertUpdate(DocumentEvent e) {
        filter();
      }

      public void removeUpdate(DocumentEvent e) {
        filter();
      }

      public void changedUpdate(DocumentEvent e) {
        filter();
      }

      private void filter() {
        String text = txtSearch.getText();
        if (text.equals(" Search policies...") || text.isEmpty()) {
          if (sorter != null)
            sorter.setRowFilter(null);
        } else {
          if (sorter != null)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
      }
    });
    pTools.add(txtSearch);

    JButton btnAdd = new JButton("+ Add New Policy");
    btnAdd.setBackground(ACCENT_RED);
    btnAdd.setForeground(Color.WHITE);
    btnAdd.setFocusPainted(false);
    btnAdd.setFont(new Font("SansSerif", Font.BOLD, 12));
    btnAdd.setBorder(new EmptyBorder(8, 15, 8, 15));
    btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnAdd.addActionListener(e -> showPolicyDialog(null));
    pTools.add(btnAdd);

    panel.add(pTools, BorderLayout.NORTH);

    // Define Table Columns
    String[] columns = { "ID", "POLICY NAME", "TYPE", "VALUE", "STATUS", "ACTIONS" };

    // Custom Model
    policyModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 4 || column == 5;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 4)
          return Boolean.class;
        return Object.class;
      }
    };

    policyTable = new JTable(policyModel);
    policyTable.setBackground(new Color(25, 25, 25));
    policyTable.setForeground(TXT_PRIMARY);
    policyTable.setRowHeight(60);
    policyTable.setShowVerticalLines(false);
    policyTable.setShowHorizontalLines(true);
    policyTable.setGridColor(new Color(45, 45, 45));
    policyTable.setSelectionBackground(new Color(40, 40, 40));
    policyTable.setSelectionForeground(Color.WHITE);
    policyTable.setFont(new Font("SansSerif", Font.PLAIN, 14));

    // Header Style
    policyTable.getTableHeader().setBackground(new Color(30, 30, 30));
    policyTable.getTableHeader().setForeground(TXT_PRIMARY); // White text
    policyTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
    policyTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
    policyTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

    // Renderers
    policyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected)
          c.setBackground(new Color(45, 45, 45)); // Lighter selection
        else
          c.setBackground(BG_DARK);
        ((JLabel) c).setBorder(new EmptyBorder(0, 15, 0, 0)); // Padding
        return c;
      }
    });

    // ID Column
    policyTable.getColumnModel().getColumn(0).setPreferredWidth(60);
    policyTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        l.setForeground(Color.GRAY);
        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
        l.setBorder(new EmptyBorder(0, 20, 0, 0));
        if (!isSelected)
          l.setBackground(BG_DARK);
        else
          l.setBackground(new Color(45, 45, 45));
        return l;
      }
    });

    // Type Column Badge Renderer
    policyTable.getColumnModel().getColumn(2).setCellRenderer(new BadgeRenderer());

    // Value Formatting
    policyTable.getColumnModel().getColumn(3).setCellRenderer(new ValueRenderer());

    // Status Toggle
    policyTable.getColumnModel().getColumn(4).setCellRenderer(new ToggleSwitchRenderer());
    policyTable.getColumnModel().getColumn(4).setCellEditor(new ToggleSwitchEditor(new JCheckBox()));

    // Actions
    policyTable.getColumnModel().getColumn(5).setCellRenderer(new ActionIconsRenderer());
    policyTable.getColumnModel().getColumn(5).setCellEditor(new ActionIconsEditor());
    policyTable.getColumnModel().getColumn(5).setMinWidth(140);
    policyTable.getColumnModel().getColumn(5).setMaxWidth(160);

    loadPolicies();

    sorter = new TableRowSorter<>(policyModel);
    policyTable.setRowSorter(sorter);

    JScrollPane sp = new JScrollPane(policyTable);
    sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    sp.getViewport().setBackground(BG_DARK);

    panel.add(sp, BorderLayout.CENTER);
    return panel;
  }

  // --- DIALOG FOR ADD/EDIT ---
  private void showPolicyDialog(DiscountPolicy policy) {
    JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
        policy == null ? "Add New Policy" : "Edit Policy", true);
    dlg.setSize(400, 350);
    dlg.setLocationRelativeTo(this);
    dlg.setLayout(new BorderLayout());

    JPanel p = new JPanel(null);
    p.setBackground(BG_DARK);

    JLabel lName = new JLabel("Policy Name");
    lName.setForeground(TXT_SECONDARY);
    lName.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lName.setBounds(30, 20, 200, 20);
    p.add(lName);

    JTextField txtName = new JTextField();
    if (policy != null)
      txtName.setText(policy.getName());
    txtName.setBounds(30, 45, 320, 35);
    txtName.setBackground(new Color(40, 40, 40));
    txtName.setForeground(Color.WHITE);
    txtName.setCaretColor(Color.WHITE);
    txtName.setBorder(new LineBorder(BORDER_COLOR));
    p.add(txtName);

    JLabel lType = new JLabel("Type");
    lType.setForeground(TXT_SECONDARY);
    lType.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lType.setBounds(30, 90, 100, 20);
    p.add(lType);

    JComboBox<String> cboType = new JComboBox<>(new String[] { "PERCENT", "FIXED" });
    if (policy != null)
      cboType.setSelectedItem(policy.getType());
    cboType.setBounds(30, 115, 140, 35);
    p.add(cboType);

    JLabel lVal = new JLabel("Value (Width % or Amount)");
    lVal.setForeground(TXT_SECONDARY);
    lVal.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lVal.setBounds(200, 90, 150, 20);
    p.add(lVal);

    JTextField txtVal = new JTextField();
    if (policy != null)
      txtVal.setText(String.valueOf(policy.getValue()));
    txtVal.setBounds(200, 115, 150, 35);
    txtVal.setBackground(new Color(40, 40, 40));
    txtVal.setForeground(Color.WHITE);
    txtVal.setCaretColor(Color.WHITE);
    txtVal.setBorder(new LineBorder(BORDER_COLOR));
    p.add(txtVal);

    JButton btnSave = new JButton("Save Policy");
    btnSave.setBackground(ACCENT_RED);
    btnSave.setForeground(Color.WHITE);
    btnSave.setBounds(30, 200, 320, 40);
    btnSave.setFocusPainted(false);
    btnSave.addActionListener(e -> {
      try {
        String n = txtName.getText().trim();
        if (n.isEmpty()) {
          JOptionPane.showMessageDialog(dlg, "Name required");
          return;
        }
        double v = Double.parseDouble(txtVal.getText().trim());
        String t = (String) cboType.getSelectedItem();

        if (policy == null) {
          DiscountPolicy newP = new DiscountPolicy(0, n, t, v, true);
          policyDAO.addPolicy(newP);
        } else {
          policy.setName(n);
          policy.setType(t);
          policy.setValue(v);
          policyDAO.updatePolicy(policy);
        }
        loadPolicies();
        dlg.dispose();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dlg, "Invalid Value");
      }
    });
    p.add(btnSave);

    dlg.add(p);
    dlg.setVisible(true);
  }

  private void loadPolicies() {
    policyModel.setRowCount(0);
    List<DiscountPolicy> list = policyDAO.getAllPolicies();
    for (DiscountPolicy p : list) {
      policyModel.addRow(new Object[] {
          String.format("%03d", p.getId()),
          p.getName(),
          p.getType(),
          p, // Value Object handled by renderer
          p.isActive(),
          p // Policy Object for actions
      });
    }
  }

  // =================================================================================
  // RENDERERS & EDITORS (TAB 1)
  // =================================================================================

  // Badge Renderer (PERCENT / FIXED)
  class BadgeRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      // ... [Rest of Renderers are identical to previous, just need to copy them
      // correctly or reference them]
      // To save context, re-writing them compactly
      String type = (String) value;
      JLabel l = new JLabel(type) {
        protected void paintComponent(Graphics g) {
          Graphics2D g2 = (Graphics2D) g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          if ("PERCENT".equalsIgnoreCase(getText())) {
            g2.setColor(new Color(0, 80, 180));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(Color.WHITE);
          } else {
            g2.setColor(new Color(0, 140, 80));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(Color.WHITE);
          }
          super.paintComponent(g);
        }
      };
      l.setHorizontalAlignment(CENTER);
      l.setFont(new Font("SansSerif", Font.BOLD, 11));
      l.setForeground(Color.WHITE);
      JPanel p = new JPanel(new GridBagLayout());
      p.setBackground(isSelected ? new Color(45, 45, 45) : BG_DARK);
      l.setPreferredSize(new Dimension(80, 24));
      p.add(l);
      return p;
    }
  }

  class ValueRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      DiscountPolicy p = (DiscountPolicy) value;
      String txt = "";
      if ("PERCENT".equalsIgnoreCase(p.getType())) {
        txt = String.format("%.1f%%", p.getValue());
      } else {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        txt = nf.format(p.getValue()) + " VND";
      }
      JLabel l = new JLabel(txt);
      l.setForeground(TXT_PRIMARY);
      l.setFont(new Font("SansSerif", Font.BOLD, 14));
      l.setBorder(new EmptyBorder(0, 15, 0, 0));
      l.setOpaque(true);
      l.setBackground(isSelected ? new Color(45, 45, 45) : BG_DARK);
      return l;
    }
  }

  class ToggleSwitchRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    private JToggleButton btn;

    public ToggleSwitchRenderer() {
      setLayout(new GridBagLayout());
      btn = createToggle();
      add(btn);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      setBackground(isSelected ? new Color(45, 45, 45) : BG_DARK);
      btn.setSelected((Boolean) value);
      return this;
    }
  }

  class ToggleSwitchEditor extends DefaultCellEditor {
    private JToggleButton btn;
    private boolean currVal;
    private JPanel p;

    public ToggleSwitchEditor(JCheckBox cb) {
      super(cb);
      p = new JPanel(new GridBagLayout());
      btn = createToggle();
      btn.addActionListener(e -> {
        currVal = btn.isSelected();
        fireEditingStopped();
        // Update DB immediately
        int row = policyTable.getSelectedRow();
        if (row != -1) {
          DiscountPolicy pol = (DiscountPolicy) policyModel.getValueAt(row, 5); // get obj
          pol.setActive(currVal);
          policyDAO.updatePolicy(pol);
        }
      });
      p.add(btn);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      p.setBackground(new Color(45, 45, 45));
      currVal = (Boolean) value;
      btn.setSelected(currVal);
      return p;
    }

    public Object getCellEditorValue() {
      return currVal;
    }
  }

  private JToggleButton createToggle() {
    JToggleButton t = new JToggleButton();
    t.setPreferredSize(new Dimension(50, 28));
    t.setContentAreaFilled(false);
    t.setBorder(null);
    t.setFocusPainted(false);
    t.setCursor(new Cursor(Cursor.HAND_CURSOR));
    t.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
      public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean sel = ((JToggleButton) c).isSelected();
        g2.setColor(sel ? ACCENT_RED : new Color(60, 60, 60));
        g2.fillRoundRect(0, 2, c.getWidth(), c.getHeight() - 4, 24, 24);
        g2.setColor(Color.WHITE);
        int cx = sel ? c.getWidth() - 22 : 4;
        g2.fillOval(cx, 4, 20, 20);
      }
    });
    return t;
  }

  class ActionIconsRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    public ActionIconsRenderer() {
      setLayout(new GridLayout(1, 2, 10, 0));
      setOpaque(true);
      add(createIconLabel("✎"));
      add(createIconLabel("🗑"));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      setBackground(isSelected ? new Color(45, 45, 45) : BG_DARK);
      return this;
    }

    private JLabel createIconLabel(String s) {
      JLabel l = new JLabel(s, SwingConstants.CENTER);
      l.setForeground(TXT_SECONDARY);
      l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
      return l;
    }
  }

  class ActionIconsEditor extends AbstractCellEditor implements TableCellEditor {
    JPanel p;
    JButton btnEdit, btnDel;
    DiscountPolicy currentPolicy; // Capture the object

    public ActionIconsEditor() {
      p = new JPanel(new GridLayout(1, 2, 10, 0));
      p.setOpaque(true);
      p.setBackground(new Color(45, 45, 45));

      btnEdit = createIconButton("✎");
      btnDel = createIconButton("🗑");

      btnEdit.addActionListener(e -> {
        fireEditingStopped();
        if (currentPolicy != null) {
          showPolicyDialog(currentPolicy);
        }
      });

      btnDel.addActionListener(e -> {
        fireEditingStopped();
        if (currentPolicy != null) {
          int cf = JOptionPane.showConfirmDialog(null, "Delete '" + currentPolicy.getName() + "'?", "Confirm",
              JOptionPane.YES_NO_OPTION);
          if (cf == JOptionPane.YES_OPTION) {
            policyDAO.deletePolicy(currentPolicy.getId());
            loadPolicies();
          }
        }
      });
      p.add(btnEdit);
      p.add(btnDel);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.currentPolicy = (DiscountPolicy) value; // Capture value
      p.setBackground(new Color(45, 45, 45));
      return p;
    }

    @Override
    public Object getCellEditorValue() {
      return currentPolicy;
    }

    private JButton createIconButton(String s) {
      JButton b = new JButton(s);
      b.setBorderPainted(false);
      b.setContentAreaFilled(false);
      b.setForeground(Color.WHITE);
      b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
      b.setCursor(new Cursor(Cursor.HAND_CURSOR));
      return b;
    }
  }

  // =================================================================================
  // TAB 2: WEEKLY PROMOTIONS (GRID LAYOUT + HOLIDAYS)
  // =================================================================================
  private JPanel createWeeklyPromoLayout() {
    JPanel root = new JPanel(new BorderLayout());
    root.setBackground(BG_DARK);

    editorPanel = new PromoEditorPanel();

    pCardList = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)) {
      @Override
      public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        Container c = getParent();
        if (c != null && c.getWidth() > 0) {
          int w = c.getWidth();
          int targetW = w - 40;
          int cardW = 240 + 20;
          int cols = Math.max(1, targetW / cardW);
          int rows = (int) Math.ceil((double) getComponentCount() / cols);
          d.width = w;
          d.height = rows * (360 + 20) + 40;
        }
        return d;
      }
    };
    pCardList.setBackground(BG_DARK);
    pCardList.setBorder(new EmptyBorder(20, 20, 20, 20));

    JScrollPane scroll = new JScrollPane(pCardList);
    scroll.setBorder(null);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroll.getVerticalScrollBar().setUnitIncrement(20);
    scroll.getViewport().setBackground(BG_DARK);

    scroll.getViewport().addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        pCardList.revalidate();
      }
    });

    loadWeeklyPromos();

    root.add(scroll, BorderLayout.CENTER);
    root.add(editorPanel, BorderLayout.EAST);
    return root;
  }

  private void loadWeeklyPromos() {
    weeklyPromos = promoDAO.getAllPromotions();
    // Seed Holidays if needed
    boolean needsReload = false;
    List<Integer> existingDays = new ArrayList<>();
    for (WeeklyPromotion wp : weeklyPromos)
      existingDays.add(wp.getDayOfWeek());

    String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    for (int i = 2; i <= 8; i++) {
      if (!existingDays.contains(i)) {
        WeeklyPromotion wp = new WeeklyPromotion(0, i, days[i - 2], 0, true, false);
        promoDAO.addPromotion(wp);
        needsReload = true;
      }
    }
    // Specific Holidays
    int[] holIds = { 11, 12, 13, 14 };
    String[] holNames = { "Lunar New Year (Tet)", "Hung Kings Festival", "Liberation Day (30/4)",
        "National Day (2/9)" };
    for (int k = 0; k < holIds.length; k++) {
      int id = holIds[k];
      if (!existingDays.contains(id)) {
        WeeklyPromotion wp = new WeeklyPromotion(0, id, holNames[k], 0, true, false);
        promoDAO.addPromotion(wp);
        needsReload = true;
      }
    }
    if (needsReload)
      weeklyPromos = promoDAO.getAllPromotions();

    refreshCardList();
    if (!weeklyPromos.isEmpty())
      selectPromo(weeklyPromos.get(0));
  }

  private void refreshCardList() {
    pCardList.removeAll();
    for (WeeklyPromotion wp : weeklyPromos) {
      pCardList.add(new PromoCard(wp));
    }
    pCardList.revalidate();
    pCardList.repaint();
  }

  private void selectPromo(WeeklyPromotion wp) {
    this.selectedPromo = wp;
    editorPanel.bindData(wp);
    refreshCardViewOnly();
  }

  private void refreshCardViewOnly() {
    for (Component c : pCardList.getComponents()) {
      if (c instanceof PromoCard) {
        ((PromoCard) c).updateVisuals();
      }
    }
  }

  private void saveChanges() {
    // Just for demo, assuming edits in editorPanel update 'selectedPromo' object
    // reference directly (or we pull fields)
    // For now, editorPanel handles its own saving in this code structure via 'bind'
    // but real app would need getFields()
    // But in the existing code, I see update logic in buttons inside EditorPanel?
    // Let's implement saveChanges inside EditorPanel primarily or call a method on
    // it.
    if (editorPanel != null && selectedPromo != null) {
      editorPanel.doSave();
    }
  }

  // =================================================================================
  // PROMO CARD UI
  // =================================================================================
  private class PromoCard extends JPanel {
    private WeeklyPromotion data;
    private JLabel lblDayBig, lblName, lblActiveStatus;
    private JToggleButton btnToggle;
    private JLabel lblBadge;

    public PromoCard(WeeklyPromotion wp) {
      this.data = wp;
      setPreferredSize(new Dimension(240, 360));
      setCursor(new Cursor(Cursor.HAND_CURSOR));
      setLayout(null);

      addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          doLayoutCard();
        }
      });

      addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          selectPromo(data);
        }
      });

      String dayName = getDayShortName(wp.getDayOfWeek());
      lblDayBig = new JLabel(dayName);
      lblDayBig.setFont(new Font("SansSerif", Font.BOLD, 42));
      lblDayBig.setForeground(new Color(255, 255, 255, 200));
      add(lblDayBig);

      if (wp.isActive()) {
        lblBadge = new JLabel("⚡");
        lblBadge.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblBadge.setForeground(ACCENT_RED);
        add(lblBadge);
      }

      lblActiveStatus = new JLabel(wp.isActive() ? "| PROMOTION ACTIVE" : "Inactive");
      lblActiveStatus.setFont(new Font("SansSerif", Font.BOLD, 10));
      lblActiveStatus.setForeground(wp.isActive() ? ACCENT_RED : TXT_SECONDARY);
      add(lblActiveStatus);

      lblName = new JLabel(wp.isActive() ? wp.getName() : "No Promo");
      lblName.setFont(new Font("SansSerif", Font.BOLD, 20));
      lblName.setForeground(Color.WHITE);
      add(lblName);

      JLabel lblActiveTxt = new JLabel(wp.isActive() ? "Active" : "Enable");
      lblActiveTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
      lblActiveTxt.setForeground(Color.LIGHT_GRAY);
      lblActiveTxt.setBounds(20, 320, 100, 20);
      add(lblActiveTxt);

      btnToggle = new JToggleButton();
      btnToggle.setSelected(wp.isActive());
      btnToggle.setFocusPainted(false);
      btnToggle.setContentAreaFilled(false);
      btnToggle.setBorder(null);
      btnToggle.addActionListener(e -> {
        boolean newState = btnToggle.isSelected();
        data.setActive(newState);
        if (data.getId() == 0)
          promoDAO.addPromotion(data);
        else
          promoDAO.updatePromotion(data);
        selectPromo(data);
      });
      btnToggle.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        public void paint(Graphics g, JComponent c) {
          Graphics2D g2 = (Graphics2D) g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          boolean sel = btnToggle.isSelected();
          g2.setColor(sel ? ACCENT_RED : new Color(60, 60, 60));
          g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 26, 26);
          g2.setColor(Color.WHITE);
          int circleX = sel ? c.getWidth() - 22 : 4;
          g2.fillOval(circleX, 4, 18, 18);
        }
      });
      add(btnToggle);
      doLayoutCard();
    }

    private void doLayoutCard() {
      int w = getWidth();
      if (w == 0)
        w = 240;
      int h = getHeight();
      if (h == 0)
        h = 360;

      lblDayBig.setBounds(20, 20, 200, 50);
      if (lblBadge != null)
        lblBadge.setBounds(w - 40, 20, 30, 30);
      lblActiveStatus.setBounds(20, h - 140, 200, 20);
      lblName.setBounds(20, h - 120, w - 40, 30);
      btnToggle.setBounds(w - 70, h - 55, 50, 26);
    }

    public void updateVisuals() {
      repaint();
      lblName.setText(data.isActive() ? data.getName() : "No Promo");
      lblActiveStatus.setText(data.isActive() ? "| PROMOTION ACTIVE" : "Enable");
      lblActiveStatus.setForeground(data.isActive() ? ACCENT_RED : TXT_SECONDARY);
      btnToggle.setSelected(data.isActive());
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      boolean isSelected = (selectedPromo != null && selectedPromo.equals(data));
      boolean isActive = data.isActive();

      if (isActive) {
        GradientPaint gp = new GradientPaint(0, 0, new Color(40, 10, 10), 0, getHeight(), Color.BLACK);
        g2.setPaint(gp);
      } else {
        g2.setColor(new Color(30, 30, 30));
      }
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

      if (isSelected) {
        g2.setColor(ACCENT_RED);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
      } else if (isActive) {
        g2.setColor(new Color(150, 40, 40));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
      }
    }
  }

  // =================================================================================
  // EDITOR PANEL
  // =================================================================================
  private class PromoEditorPanel extends JPanel {
    private JLabel lblDayHeader;
    private JTextField txtName, txtValue;
    private JComboBox<String> cboType;

    public PromoEditorPanel() {
      setPreferredSize(new Dimension(350, 0));
      setBackground(new Color(18, 18, 18));
      setBorder(new MatteBorder(0, 1, 0, 0, BORDER_COLOR));
      setLayout(null);

      lblDayHeader = new JLabel("MONDAY");
      lblDayHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
      lblDayHeader.setForeground(ACCENT_RED);
      lblDayHeader.setBounds(30, 40, 200, 20);
      add(lblDayHeader);

      JLabel lblTitle = new JLabel("Edit Promotion");
      lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
      lblTitle.setForeground(Color.WHITE);
      lblTitle.setBounds(30, 65, 250, 40);
      add(lblTitle);

      int y = 140;
      add(createLabel("PROMOTION NAME", 30, y));
      txtName = createTextField(30, y + 25, 290);
      add(txtName);

      y += 90;
      add(createLabel("DISCOUNT VALUE", 30, y));
      txtValue = createTextField(30, y + 25, 140);
      add(txtValue);

      cboType = new JComboBox<>(new String[] { "Fixed ($)", "Percent (%)" });
      cboType.setBounds(190, y + 25, 130, 40);
      cboType.setBackground(new Color(30, 30, 30));
      cboType.setForeground(Color.WHITE);
      add(cboType);

      JButton btnSave = new JButton("Save Changes");
      btnSave.setBounds(30, 600, 180, 45);
      btnSave.setBackground(ACCENT_RED);
      btnSave.setForeground(Color.WHITE);
      btnSave.setFocusPainted(false);
      btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
      btnSave.setBorder(null);
      btnSave.addActionListener(e -> doSave());
      add(btnSave);

      JButton btnRevert = new JButton("Revert");
      btnRevert.setBounds(230, 600, 90, 45);
      btnRevert.setBackground(new Color(40, 40, 40));
      btnRevert.setForeground(Color.WHITE);
      btnRevert.setFocusPainted(false);
      btnRevert.setBorder(null);
      btnRevert.addActionListener(e -> bindData(selectedPromo));
      add(btnRevert);
    }

    public void doSave() {
      if (selectedPromo == null)
        return;
      selectedPromo.setName(txtName.getText());
      try {
        selectedPromo.setDiscountValue(Double.parseDouble(txtValue.getText()));
      } catch (Exception e) {
      }
      selectedPromo.setPercent(cboType.getSelectedIndex() == 1);
      promoDAO.updatePromotion(selectedPromo);
      refreshCardViewOnly();
      JOptionPane.showMessageDialog(this, "Saved successfully!");
    }

    private JLabel createLabel(String text, int x, int y) {
      JLabel l = new JLabel(text);
      l.setFont(new Font("SansSerif", Font.BOLD, 10));
      l.setForeground(TXT_SECONDARY);
      l.setBounds(x, y, 200, 20);
      return l;
    }

    private JTextField createTextField(int x, int y, int w) {
      JTextField t = new JTextField();
      t.setBounds(x, y, w, 40);
      t.setBackground(new Color(30, 30, 30));
      t.setForeground(Color.WHITE);
      t.setCaretColor(Color.WHITE);
      t.setBorder(BorderFactory.createCompoundBorder(
          new LineBorder(BORDER_COLOR),
          new EmptyBorder(0, 10, 0, 10)));
      return t;
    }

    public void bindData(WeeklyPromotion wp) {
      if (wp == null)
        return;
      lblDayHeader.setText(getDayName(wp.getDayOfWeek()).toUpperCase());
      txtName.setText(wp.getName());
      txtValue.setText(String.valueOf(wp.getDiscountValue()));
      cboType.setSelectedIndex(wp.isPercent() ? 1 : 0);
    }
  }

  private String getDayShortName(int day) {
    switch (day) {
      case 2:
        return "MON";
      case 3:
        return "TUE";
      case 4:
        return "WED";
      case 5:
        return "THU";
      case 6:
        return "FRI";
      case 7:
        return "SAT";
      case 1:
      case 8:
        return "SUN";
      case 10:
        return "HOL";
      case 11:
        return "TET";
      case 12:
        return "GIO";
      case 13:
        return "30/4";
      case 14:
        return "2/9";
      default:
        return "UNK";
    }
  }

  private String getDayName(int day) {
    switch (day) {
      case 2:
        return "Monday";
      case 3:
        return "Tuesday";
      case 4:
        return "Wednesday";
      case 5:
        return "Thursday";
      case 6:
        return "Friday";
      case 7:
        return "Saturday";
      case 1:
      case 8:
        return "Sunday";
      case 10:
        return "Public Holiday";
      case 11:
        return "Tet Holiday";
      case 12:
        return "Gio To Hung Vuong";
      case 13:
        return "Liberation Day";
      case 14:
        return "National Day";
      default:
        return "Unknown";
    }
  }
}
