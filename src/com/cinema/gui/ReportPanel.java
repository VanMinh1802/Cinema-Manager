package com.cinema.gui;

import com.cinema.dao.ThongKeDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ReportPanel extends JPanel {

  // --- COLORS ---
  private static final Color BG_MAIN = Color.decode("#09090B"); // Deep Black
  private static final Color BG_CARD = Color.decode("#18181B"); // Zinc-900
  private static final Color ACCENT_RED = Color.decode("#EF4444"); // Red-500
  private static final Color ACCENT_GREEN = Color.decode("#10B981"); // Emerald-500
  private static final Color TXT_PRIMARY = Color.decode("#FAFAFA"); // Zinc-50
  private static final Color TXT_SECONDARY = Color.decode("#A1A1AA"); // Zinc-400
  private static final Color BORDER_COLOR = Color.decode("#3F3F46"); // Zinc-700

  private ThongKeDAO dao = new ThongKeDAO();
  private Date selectedDate = new Date(); // Default: Today

  // UI References for Refreshing
  private JPanel statsContainer;
  private JPanel chartContainer;
  private JPanel transactionContainer; // Reference for updates
  private javax.swing.table.DefaultTableModel transactionModel;
  private JComboBox<String> txnFilter; // Filter Box
  private java.util.List<ThongKeDAO.TransactionRow> currentTransactions; // Store for filtering
  private JLabel dateLabel;

  public ReportPanel() {
    setLayout(new BorderLayout());
    setBackground(BG_MAIN);

    // 1. HEADER
    add(createHeader(), BorderLayout.NORTH);

    // 2. SCROLL CONTENT
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBackground(BG_MAIN);
    content.setBorder(new EmptyBorder(30, 40, 30, 40));

    // Stat Cards Grid
    statsContainer = new JPanel(new GridLayout(1, 4, 20, 0));
    statsContainer.setBackground(BG_MAIN);
    statsContainer.setMaximumSize(new Dimension(2000, 180));
    statsContainer.setPreferredSize(new Dimension(1000, 180));
    // refreshStats(); // Removed synchronous call
    content.add(statsContainer);

    content.add(Box.createVerticalStrut(30));

    // Main Chart
    chartContainer = new JPanel(new BorderLayout());
    chartContainer.setBackground(BG_MAIN);
    chartContainer.setMaximumSize(new Dimension(2000, 400));
    // Load initial data (Async)
    loadData();
    content.add(chartContainer);

    content.add(Box.createVerticalStrut(30));

    // 2b. Transaction Table
    transactionContainer = new JPanel(new BorderLayout());
    transactionContainer.setBackground(BG_MAIN);
    transactionContainer.setMaximumSize(new Dimension(2000, 400));
    // transactionContainer.setPreferredSize(new Dimension(1000, 300));

    // Add logic to init table Wrapper
    content.add(createTransactionPanel());

    // Push content up
    content.add(Box.createVerticalGlue());

    JScrollPane scroll = new JScrollPane(content);
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    scroll.getViewport().setBackground(BG_MAIN);
    add(scroll, BorderLayout.CENTER);

    // 3. FOOTER
    add(createFooter(), BorderLayout.SOUTH);
  }

  private void updateStatsUI(DashboardData data) {
    statsContainer.removeAll();

    // Card 1: Revenue
    statsContainer
        .add(createStatCard("Total Revenue", String.format("%,.0f VNĐ", data.revenue + data.concession), "wallet"));

    // Card 2: Tickets
    statsContainer.add(createStatCard("Total Tickets Sold", String.format("%,d", data.tickets), "ticket"));

    // Card 3: Occupancy
    statsContainer.add(createStatCard("Average Occupancy", String.format("%.1f%%", data.occupancy), "chair"));

    // Card 4: Concessions
    statsContainer.add(createStatCard("Concession Sales", String.format("%,.0f VNĐ", data.concession), "food"));

    statsContainer.revalidate();
    statsContainer.repaint();
  }

  private void updateChartsUI(DashboardData data) {
    chartContainer.removeAll();
    // Layout: Left (Timeline) 60%, Middle (Genre) 20%, Right (Concessions) 20%
    JPanel split = new JPanel(new GridBagLayout());
    split.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();

    // 1. Timeline Chart
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.6;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(0, 0, 0, 20); // Right padding
    split.add(createMainChart(data), gbc);

    // 2. Genre Chart (Middle Card)
    JPanel genreCard = createDonutCard("Genre Distribution", createGenreChart(data.genreData));
    gbc.gridx = 1;
    gbc.weightx = 0.2;
    gbc.insets = new Insets(0, 0, 0, 20);
    split.add(genreCard, gbc);

    // 3. Concession Chart (Right Card)
    JPanel concCard = createDonutCard("Concession Sales", createConcessionChart(data.concessionData));
    gbc.gridx = 2;
    gbc.weightx = 0.2;
    gbc.insets = new Insets(0, 0, 0, 0);
    split.add(concCard, gbc);

    chartContainer.add(split, BorderLayout.CENTER);
    chartContainer.revalidate();
    chartContainer.repaint();
  }

  // Filter Mode
  private boolean isMonthlyMode = false;

  private void openDatePicker() {
    JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Filter Report", true);
    d.setSize(350, 250);
    d.setLocationRelativeTo(this);
    d.setLayout(new BorderLayout());
    d.getContentPane().setBackground(BG_MAIN);

    JTabbedPane tabs = new JTabbedPane();

    // Tab 1: Daily
    JPanel pDaily = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
    pDaily.setBackground(BG_MAIN);
    JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
    dateSpinner.setEditor(timeEditor);
    dateSpinner.setValue(selectedDate);
    dateSpinner.setPreferredSize(new Dimension(150, 30));
    pDaily.add(dateSpinner);

    // Tab 2: Monthly
    JPanel pMonthly = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 40));
    pMonthly.setBackground(BG_MAIN);
    String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    JComboBox<String> cbMonth = new JComboBox<>(months);
    // Set current month
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(selectedDate);
    cbMonth.setSelectedIndex(cal.get(java.util.Calendar.MONTH));

    // Year spinner
    int currentYear = cal.get(java.util.Calendar.YEAR);
    JSpinner yrSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2030, 1));
    // Remove group separator (comma)
    JSpinner.NumberEditor ne = new JSpinner.NumberEditor(yrSpinner, "#");
    yrSpinner.setEditor(ne);

    pMonthly.add(cbMonth);
    pMonthly.add(yrSpinner);

    tabs.addTab("By Day", pDaily);
    tabs.addTab("By Month", pMonthly);
    if (isMonthlyMode)
      tabs.setSelectedIndex(1);

    d.add(tabs, BorderLayout.CENTER);

    JButton okBtn = new JButton("Apply Filter");
    okBtn.setBackground(ACCENT_RED);
    okBtn.setForeground(Color.WHITE);
    okBtn.addActionListener(e -> {
      if (tabs.getSelectedIndex() == 0) {
        // Daily
        isMonthlyMode = false;
        selectedDate = (Date) dateSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateLabel.setText("📅 " + sdf.format(selectedDate));
      } else {
        // Monthly
        isMonthlyMode = true;
        int m = cbMonth.getSelectedIndex(); // 0-11
        int y = (Integer) yrSpinner.getValue();

        // Set selectedDate to 1st of that month for consistency
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(y, m, 1);
        selectedDate = c.getTime();

        dateLabel.setText("📅 " + months[m] + " " + y);
      }
      loadData();
      d.dispose();
    });

    JPanel pBtn = new JPanel();
    pBtn.setBackground(BG_MAIN);
    pBtn.add(okBtn);
    d.add(pBtn, BorderLayout.SOUTH);

    d.setVisible(true);
  }

  private JPanel createHeader() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(BG_MAIN);
    p.setBorder(new EmptyBorder(20, 40, 20, 40));
    p.setPreferredSize(new Dimension(getWidth(), 100));

    // Left: Title & Location
    JPanel left = new JPanel(new GridLayout(2, 1));
    left.setBackground(BG_MAIN);

    JLabel title = new JLabel("Reports and Analytics Dashboard");
    title.setFont(new Font("SansSerif", Font.BOLD, 24));
    title.setForeground(TXT_PRIMARY);

    JLabel sub = new JLabel("");
    sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
    sub.setForeground(TXT_SECONDARY);

    left.add(title);
    left.add(sub);

    // Right: Date & Filter
    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    right.setBackground(BG_MAIN);

    // Date Picker Trigger
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    dateLabel = createButton("📅 " + sdf.format(selectedDate), "calendar");
    dateLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    dateLabel.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        openDatePicker();
      }
    });

    JLabel filterBtn = createButton("Filter", "filter");
    filterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    filterBtn.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        openDatePicker(); // Also open on filter click
      }
    });

    JLabel exportBtn = createButton("Export", "export");
    exportBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    exportBtn.setForeground(TXT_PRIMARY);
    exportBtn.setBackground(ACCENT_GREEN); // Highlight
    exportBtn.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        exportReport();
      }
    });

    right.add(dateLabel);
    right.add(Box.createHorizontalStrut(10));
    right.add(filterBtn);
    right.add(Box.createHorizontalStrut(10));
    right.add(exportBtn);

    p.add(left, BorderLayout.WEST);
    p.add(right, BorderLayout.EAST);
    return p;
  }

  private JLabel createButton(String text, String iconType) {
    JLabel btn = new JLabel(text);
    btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
    btn.setForeground(TXT_SECONDARY);
    btn.setBackground(BG_CARD);
    btn.setOpaque(true);
    btn.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(BORDER_COLOR, 1, true),
        new EmptyBorder(8, 15, 8, 15)));
    // Add icon char (mock)
    if (iconType.equals("filter"))
      btn.setText("⚡ " + text);
    else if (iconType.equals("export"))
      btn.setText("📥 " + text);
    else if (iconType.equals("calendar")) // Fix existing usage too if needed
      btn.setText(text); // Already has emoji in caller

    return btn;
  }

  private JPanel createStatCard(String title, String value, String iconType) {
    JPanel p = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BG_CARD);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
      }
    };
    p.setLayout(null);
    p.setOpaque(false);

    // 1. Icon Bubble
    int iconSize = 45;
    JLabel iconLbl = new JLabel();
    iconLbl.setHorizontalAlignment(SwingConstants.CENTER);

    // Choose Color & Icon based on type
    Color iconBg = new Color(39, 39, 42); // Zinc-800 default
    String emoji = "";

    if (iconType.equals("wallet")) {
      iconBg = new Color(220, 38, 38, 30); // Red tint
      iconLbl.setForeground(ACCENT_RED);
      emoji = "💰"; // Bag
    } else if (iconType.equals("ticket")) {
      iconBg = new Color(245, 158, 11, 30); // Amber tint
      iconLbl.setForeground(Color.decode("#F59E0B"));
      emoji = "🎟️";
    } else if (iconType.equals("chair")) {
      iconBg = new Color(59, 130, 246, 30); // Blue tint
      iconLbl.setForeground(Color.decode("#3B82F6"));
      emoji = "💺";
    } else if (iconType.equals("food")) {
      iconBg = new Color(16, 185, 129, 30); // Emerald tint
      iconLbl.setForeground(ACCENT_GREEN);
      emoji = "🍿";
    }

    iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
    iconLbl.setText(emoji);

    // Draw Circle Background for Icon
    final Color iBg = iconBg;
    JPanel iconPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(iBg);
        g2.fillOval(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
      }
    };
    iconPanel.setOpaque(false);
    iconPanel.setBounds(20, 25, iconSize, iconSize);
    iconPanel.setLayout(new BorderLayout());
    iconPanel.add(iconLbl, BorderLayout.CENTER);
    p.add(iconPanel);

    // 2. Title and Value
    // Pushed down slightly
    JLabel lTitle = new JLabel(title);
    lTitle.setForeground(TXT_SECONDARY);
    lTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
    lTitle.setBounds(20, 80, 200, 20);
    p.add(lTitle);

    JLabel lValue = new JLabel(value);
    lValue.setForeground(TXT_PRIMARY);
    lValue.setFont(new Font("SansSerif", Font.BOLD, 20));
    lValue.setBounds(20, 100, 200, 30);
    p.add(lValue);

    return p;
  }

  private JPanel createMainChart(DashboardData data) {
    JPanel p = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BG_CARD);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
      }
    };
    p.setLayout(new BorderLayout());
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(25, 30, 25, 30));
    p.setMaximumSize(new Dimension(2000, 400));
    p.setPreferredSize(new Dimension(800, 400));

    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    // Left Title
    JPanel left = new JPanel(new GridLayout(2, 1));
    left.setOpaque(false);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    JLabel t1 = new JLabel("Revenue Analysis (" + sdf.format(selectedDate) + ")");
    t1.setFont(new Font("SansSerif", Font.BOLD, 18));
    t1.setForeground(TXT_PRIMARY);
    JLabel t2 = new JLabel("Hourly breakdown of revenue for " + sdf.format(selectedDate));
    t2.setFont(new Font("SansSerif", Font.PLAIN, 12));
    t2.setForeground(TXT_SECONDARY);
    left.add(t1);
    left.add(t2);

    // Right Toggles
    JPanel toggles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    toggles.setOpaque(false);
    toggles.add(createLegendItem("Ticket Sales", ACCENT_RED));
    toggles.add(createLegendItem("Concessions", ACCENT_GREEN)); // Concessions
    // Total Revenue removed for Grouped Chart clarity

    header.add(left, BorderLayout.WEST);
    header.add(toggles, BorderLayout.EAST);

    p.add(header, BorderLayout.NORTH);

    // Chart Area
    p.add(new ChartArea(data.ticketDist, data.foodDist, data.numBars), BorderLayout.CENTER);

    return p;
  }

  private JPanel createLegendItem(String name, Color c) {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    p.setOpaque(false);

    JPanel box = new JPanel();
    box.setPreferredSize(new Dimension(12, 12));
    box.setBackground(c);

    JLabel l = new JLabel(name);
    l.setForeground(TXT_PRIMARY);
    l.setFont(new Font("SansSerif", Font.BOLD, 12));

    p.add(box);
    p.add(l);
    return p;
  }

  // Custom Chart (Stacked Bars with Tooltips)
  private class ChartArea extends JPanel {

    private double[] ticketData;
    private double[] foodData;
    private int numBars;

    public ChartArea(double[] tData, double[] fData, int nBars) {
      setOpaque(false);
      ToolTipManager.sharedInstance().registerComponent(this);

      this.ticketData = tData;
      this.foodData = fData;
      this.numBars = nBars;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
      int pX = 50;
      int x = e.getX();
      if (x < pX)
        return null;

      int w = getWidth();
      int availableWidth = w - pX - 20;
      int slotWidth = availableWidth / numBars;
      if (slotWidth < 1)
        slotWidth = 1;

      int idx = (x - pX) / slotWidth;
      int limit = isMonthlyMode ? 31 : 24;

      if (idx >= 0 && idx < limit) {
        int dataIdx = isMonthlyMode ? (idx + 1) : idx;
        if (dataIdx < ticketData.length) {
          double t = ticketData[dataIdx];
          double f = foodData[dataIdx];
          String timeLabel = isMonthlyMode ? ("Day " + dataIdx) : String.format("%02d:00", dataIdx);
          return String.format(
              "<html><div style='background-color:#1e1e1e; color:white; padding:5px; border-radius:5px;'>" +
                  "<b>%s</b><br>" +
                  "<span style='color:#ef4444;'>● Ticket:</span> %,.0f<br>" +
                  "<span style='color:#22c55e;'>● Food:</span> %,.0f<br>" +
                  "<b>Total: %,.0f</b></div></html>",
              timeLabel, t, f, t + f);
        }
      }
      return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      int w = getWidth();
      int h = getHeight();
      int pX = 50;
      int pB = 40;
      int pT = 40;

      double maxVal = 1000000;
      for (int i = 0; i < ticketData.length; i++) {
        double total = ticketData[i] + foodData[i];
        if (total > maxVal)
          maxVal = total;
      }

      // 1. Grid Lines
      g2.setColor(new Color(60, 60, 60));
      g2.setStroke(new BasicStroke(1f));
      int steps = 5;
      for (int i = 0; i <= steps; i++) {
        int y = pT + (h - pB - pT) * i / steps;
        g2.drawLine(pX, y, w - 20, y);

        double val = maxVal * (steps - i) / steps;
        String s = (val >= 1000000) ? String.format("%.1fm", val / 1000000) : String.format("%.0fk", val / 1000);
        if (val == 0)
          s = "0";
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2.setColor(TXT_SECONDARY);
        g2.drawString(s, 5, y + 4);
        g2.setColor(new Color(60, 60, 60));
      }

      // 2. Draw Bars (Stacked)
      int availableWidth = w - pX - 20;
      int slotWidth = availableWidth / numBars;
      if (slotWidth < 1)
        slotWidth = 1;

      int barWidth = (int) (slotWidth * 0.7); // 70% width
      if (barWidth < 2)
        barWidth = 2;

      int limit = isMonthlyMode ? 31 : 24;
      int xStep = isMonthlyMode ? 2 : 3;

      for (int i = 0; i < limit; i++) {
        int dataIdx = isMonthlyMode ? (i + 1) : i;
        if (dataIdx >= ticketData.length)
          break;

        double tVal = ticketData[dataIdx];
        double fVal = foodData[dataIdx];

        int xSlotStart = pX + i * slotWidth;
        int centerSlot = xSlotStart + slotWidth / 2;
        int xBar = centerSlot - barWidth / 2;

        int bottomY = h - pB;
        double graphHeight = h - pB - pT;

        int hTicket = (int) (tVal / maxVal * graphHeight);
        int hFood = (int) (fVal / maxVal * graphHeight);

        // Draw Ticket (Red)
        if (hTicket > 0) {
          g2.setColor(ACCENT_RED);
          g2.fillRect(xBar, bottomY - hTicket, barWidth, hTicket);
        }

        // Draw Food (Green) - Stacked
        if (hFood > 0) {
          g2.setColor(ACCENT_GREEN);
          g2.fillRect(xBar, bottomY - hTicket - hFood, barWidth, hFood);
        }
      }

      // 3. X-Axis Labels
      g2.setColor(TXT_SECONDARY);
      for (int i = 0; i < limit; i += xStep) {
        int x = pX + i * slotWidth;
        int val = isMonthlyMode ? (i + 1) : i;
        String label = isMonthlyMode ? String.valueOf(val) : String.format("%02d:00", val);
        g2.drawString(label, x, h - 10);
      }
    }
  }

  private JPanel createFooter() {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
    p.setBackground(Color.decode("#000000")); // Pure black status bar
    p.setPreferredSize(new Dimension(getWidth(), 35));

    // Status Dot
    JLabel status = new JLabel("● SYSTEMS ONLINE");
    status.setForeground(ACCENT_GREEN);
    status.setFont(new Font("SansSerif", Font.BOLD, 10));

    JLabel updated = new JLabel("|   LAST UPDATED: JUST NOW");
    updated.setForeground(TXT_SECONDARY);
    updated.setFont(new Font("SansSerif", Font.PLAIN, 10));

    p.add(status);
    p.add(updated);

    return p;
  }

  // --- Export Logic ---
  private void exportReport() {
    JFileChooser ch = new JFileChooser();
    // Save as .xls (HTML format) for best compatibility with Excel/Unicode
    ch.setSelectedFile(new java.io.File("Report_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".xls"));
    int result = ch.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      try {
        java.io.File f = ch.getSelectedFile();
        // Use UTF-8 encoding
        java.io.OutputStreamWriter os = new java.io.OutputStreamWriter(new java.io.FileOutputStream(f),
            java.nio.charset.StandardCharsets.UTF_8);
        java.io.BufferedWriter bw = new java.io.BufferedWriter(os);

        // HTML Header with UTF-8 Meta
        bw.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        bw.write(
            "<style>body{font-family:Arial;} table{border-collapse:collapse;width:100%;} th,td{border:1px solid #ddd;padding:8px;text-align:left;} th{background-color:#f2f2f2;}</style>");
        bw.write("</head><body>");

        bw.write("<h2>REPORT SUMMARY</h2>");
        bw.write("<p>Generated on: " + new Date() + "</p>");
        String dateStr;
        if (isMonthlyMode) {
          dateStr = new SimpleDateFormat("MMMM yyyy").format(selectedDate);
        } else {
          dateStr = new SimpleDateFormat("dd/MM/yyyy").format(selectedDate);
        }
        bw.write("<p>Filter Date: " + dateStr + "</p>");

        bw.write("<h3>METRICS</h3>");
        bw.write("<table>");

        double revenue, occupancy, concession;
        int tickets;

        if (isMonthlyMode) {
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(selectedDate);
          int m = cal.get(java.util.Calendar.MONTH) + 1;
          int y = cal.get(java.util.Calendar.YEAR);

          revenue = dao.getRevenue(m, y);
          tickets = dao.getTickets(m, y);
          occupancy = dao.getOccupancy(m, y);

          java.util.List<Object[]> concData = dao.getRevenueByConcessionType(m, y);
          concession = 0;
          for (Object[] row : concData)
            concession += (double) row[1];
        } else {
          revenue = dao.getRevenue(selectedDate);
          tickets = dao.getTickets(selectedDate);
          occupancy = dao.getOccupancy(selectedDate);
          concession = dao.getConcessionSales(selectedDate);
        }

        bw.write("<tr><td>Ticket Revenue</td><td>" + String.format("%.0f", revenue) + "</td></tr>");
        bw.write("<tr><td>Concession Revenue</td><td>" + String.format("%.0f", concession) + "</td></tr>");
        bw.write("<tr><td>Total Revenue</td><td>" + String.format("%.0f", revenue + concession) + "</td></tr>");
        bw.write("<tr><td>Tickets Sold</td><td>" + tickets + "</td></tr>");
        bw.write("<tr><td>Occupancy</td><td>" + String.format("%.2f", occupancy) + "%</td></tr>");
        bw.write("</table>");

        // 2. Concession Breakdown
        bw.write("<h3>CONCESSION BREAKDOWN</h3>");
        bw.write("<table>");
        bw.write("<tr><th>Category</th><th>Revenue</th><th>Percentage</th></tr>");

        java.util.List<Object[]> concList;

        if (isMonthlyMode) {
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(selectedDate);
          concList = dao.getRevenueByConcessionType(cal.get(java.util.Calendar.MONTH) + 1,
              cal.get(java.util.Calendar.YEAR));
        } else {
          concList = dao.getRevenueByConcessionType(selectedDate);
        }

        double totalConc = concession > 0 ? concession : 1;
        double totalConcRevenue = 0;
        for (Object[] row : concList) {
          double val = (double) row[1];
          totalConcRevenue += val;
          double pct = (val / totalConc) * 100;
          bw.write("<tr><td>" + row[0] + "</td><td>" + String.format("%.0f", val) + "</td><td>"
              + String.format("%.1f%%", pct) + "</td></tr>");
        }
        // Total Row for Concession
        bw.write(
            "<tr><td style='text-align:right; font-weight:bold;'>TOTAL</td><td style='font-weight:bold; color:#d32f2f;'>"
                + String.format("%,.0f", totalConcRevenue) + "</td><td>100%</td></tr>");
        bw.write("</table>");

        // 3. Top Movies
        bw.write("<h3>TOP PERFORMING MOVIES</h3>");
        bw.write("<table>");
        bw.write("<tr><th>Movie</th><th>Tickets</th><th>Revenue</th></tr>");

        java.util.List<Object[]> topMovies;
        if (isMonthlyMode) {
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(selectedDate);
          topMovies = dao.getDoanhThuTheoPhim(cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.YEAR));
        } else {
          topMovies = dao.getDoanhThuTheoPhim(selectedDate);
        }

        int totalMovTickets = 0;
        double totalMovRevenue = 0;

        for (Object[] row : topMovies) {
          int t = 0;
          double r = 0;
          if (row[1] instanceof Number)
            t = ((Number) row[1]).intValue();
          if (row[2] instanceof Number)
            r = ((Number) row[2]).doubleValue();

          totalMovTickets += t;
          totalMovRevenue += r;

          bw.write("<tr><td>" + row[0] + "</td><td>" + t + "</td><td>" + String.format("%.0f", r) + "</td></tr>");
        }

        // Total Row for Movies
        bw.write("<tr><td style='text-align:right; font-weight:bold;'>TOTAL</td>"
            + "<td style='font-weight:bold;'>" + String.format("%,d", totalMovTickets) + "</td>"
            + "<td style='font-weight:bold; color:#d32f2f;'>" + String.format("%,.0f", totalMovRevenue) + "</td></tr>");

        bw.write("</table>");

        // 4. Recent Transactions (Full List matching UI)
        bw.write("<h3>TRANSACTION DETAILS</h3>");
        bw.write("<table>");
        bw.write("<tr><th>Time</th><th>Order ID</th><th>Type</th><th>Item</th><th>Qty</th><th>Amount</th></tr>");

        java.util.List<ThongKeDAO.TransactionRow> trans;
        if (isMonthlyMode) {
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(selectedDate);
          trans = dao.getDetailTransactions(cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.YEAR));
        } else {
          trans = dao.getDetailTransactions(selectedDate);
        }

        double totalTransAmount = 0;

        for (ThongKeDAO.TransactionRow row : trans) {
          totalTransAmount += row.price;

          // Escape HTML in Item Name just in case
          String itemSafe = row.item.replace("<", "&lt;").replace(">", "&gt;");

          bw.write("<tr>");
          bw.write("<td>" + row.time + "</td>");
          bw.write("<td>" + row.orderId + "</td>");
          bw.write("<td>" + row.type + "</td>");
          bw.write("<td>" + itemSafe + "</td>");
          bw.write("<td>" + row.qty + "</td>");
          bw.write("<td>" + String.format("%,.0f", row.price) + "</td>");
          bw.write("</tr>");
        }

        // Add Total Row
        bw.write(
            "<tr><td colspan='5' style='text-align:right; font-weight:bold;'>TOTAL</td><td style='font-weight:bold; color:#d32f2f;'>"
                + String.format("%,.0f", totalTransAmount) + "</td></tr>");

        bw.write("</table>");

        bw.write("</body></html>");
        bw.close();
        JOptionPane.showMessageDialog(this, "Export Successful!\nSaved to: " + f.getAbsolutePath());
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error exporting file: " + e.getMessage());
      }
    }
  }

  // Helper for Donut Cards
  private JPanel createDonutCard(String title, JPanel chart) {
    JPanel card = new JPanel(new BorderLayout());
    card.setOpaque(false);

    JPanel internal = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BG_CARD);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
      }
    };
    internal.setOpaque(false);
    internal.setBorder(new EmptyBorder(15, 15, 15, 15));

    JLabel lbl = new JLabel(title);
    lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
    lbl.setForeground(TXT_PRIMARY);
    lbl.setBorder(new EmptyBorder(0, 0, 10, 0));

    internal.add(lbl, BorderLayout.NORTH);
    internal.add(chart, BorderLayout.CENTER);
    card.add(internal, BorderLayout.CENTER);
    return card;
  }

  // --- CHART 1: GENRES ---
  private JPanel createGenreChart(java.util.List<Object[]> data) {
    return new DonutChart(data, false);
  }

  // --- CHART 2: CONCESSIONS ---
  private JPanel createConcessionChart(java.util.List<Object[]> data) {
    return new DonutChart(data, true);
  }

  // Generalized Donut Chart
  private class DonutChart extends JPanel {
    java.util.List<Object[]> data;
    boolean isConcession; // To choose palette

    public DonutChart(java.util.List<Object[]> data, boolean isConcession) {
      this.data = data;
      this.isConcession = isConcession;
      setOpaque(false);
      ToolTipManager.sharedInstance().registerComponent(this);

      addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
          int w = getWidth();
          int h = getHeight();
          int size = Math.min(w, h) - 20;
          int x = (w - size) / 2;
          int y = (h - size) / 2;
          int centerX = x + size / 2;
          int centerY = y + size / 2;
          int radius = size / 2;
          int innerRadius = (int) (size * 0.6) / 2;

          int dx = e.getX() - centerX;
          int dy = e.getY() - centerY;
          double dist = Math.sqrt(dx * dx + dy * dy);

          if (dist >= innerRadius && dist <= radius) {
            // Calculate Angle (0-360, matching fillArc start at 90)
            // atan2 returns -pi to pi. 0 is Right (3 o'clock).


            double theta = Math.atan2(-dy, dx); // Note -dy because screen Y is down
            if (theta < 0)
              theta += 2 * Math.PI;
            double angleDeg = Math.toDegrees(theta); // 0 at 3 o'clock, CCW.



            double total = 0;
            for (Object[] row : data)
              total += (double) row[1];

            double relAngle = angleDeg - 90;
            if (relAngle < 0)
              relAngle += 360;

            boolean found = false;
            double cumulativeSweep = 0;

            for (Object[] r : data) {
              double v = (double) r[1];
              double s = (v / total) * 360;
              if (relAngle >= cumulativeSweep && relAngle < cumulativeSweep + s) {
                // Found it
                String name = (String) r[0];
                String percent = String.format("%.1f%%", (v / total) * 100);
                String amount = String.format("%,.0f VNĐ", v);
                setToolTipText("<html><b>" + name + "</b><br/>" + amount + "<br/>" + percent + "</html>");
                found = true;
                break;
              }
              cumulativeSweep += s;
            }
            if (!found)
              setToolTipText(null);

          } else {
            setToolTipText(null);
          }
        }
      });
    }

    @Override
    public String getToolTipText(MouseEvent event) {
      return super.getToolTipText(event); // Handled by setToolTipText in MouseMoved
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      int w = getWidth();
      int h = getHeight();
      int size = Math.min(w, h) - 20;
      int x = (w - size) / 2;
      int y = (h - size) / 2;

      double total = 0;
      for (Object[] row : data)
        total += (double) row[1];

      // Colors
      Color[] palette;
      if (isConcession) {
        palette = new Color[] {
            Color.decode("#F59E0B"), // Amber (Popcorn)
            Color.decode("#3B82F6"), // Blue (Drink)
            Color.decode("#8B5CF6"), // Purple (Combo)
            Color.decode("#10B981") // Emerald
        };
      } else {
        palette = new Color[] {
            Color.decode("#EF4444"),
            Color.decode("#F97316"),
            Color.decode("#EAB308"),
            Color.decode("#84CC16"),
            Color.decode("#06B6D4")
        };
      }

      if (total == 0) {
        g2.setColor(new Color(60, 60, 60));
        g2.setStroke(new BasicStroke(5));
        g2.drawOval(x, y, size, size);
        return;
      }

      double currentAngle = 90;
      int i = 0;
      for (Object[] row : data) {
        double val = (double) row[1];
        double angle = (val / total) * 360;

        g2.setColor(palette[i % palette.length]);
        g2.fillArc(x, y, size, size, (int) currentAngle, (int) angle + 1); // +1 to close gaps

        currentAngle += angle;
        i++;
      }

      // Cutout (Donut)
      g2.setColor(BG_CARD);
      int innerSize = (int) (size * 0.6);
      int innerX = x + (size - innerSize) / 2;
      int innerY = y + (size - innerSize) / 2;
      g2.fillOval(innerX, innerY, innerSize, innerSize);

      // Center Text (Total)
      g2.setColor(TXT_PRIMARY);
      g2.setFont(new Font("SansSerif", Font.BOLD, 12));
      String s = (total >= 1000000) ? String.format("%.1fM", total / 1000000) : String.format("%.0fk", total / 1000);
      FontMetrics fm = g2.getFontMetrics();
      g2.drawString(s, x + size / 2 - fm.stringWidth(s) / 2, y + size / 2 + 5);

      // Hint text below logic can be added if needed, but Tooltip is better.
    }
  }

  // --- TRANSACTION TABLE UI ---
  private JPanel createTransactionPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(BG_CARD);
    p.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(BORDER_COLOR, 1, true),
        new EmptyBorder(20, 20, 20, 20)));
    p.setMaximumSize(new Dimension(2000, 500));

    // Header Panel
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(BG_CARD);

    JLabel lbl = new JLabel("Recent Transactions");
    lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
    lbl.setForeground(TXT_PRIMARY);
    header.add(lbl, BorderLayout.WEST);

    // Filter
    String[] opts = { "All Types", "Ticket", "Concession" };
    txnFilter = new JComboBox<>(opts);
    txnFilter.setPreferredSize(new Dimension(120, 30));
    txnFilter.addActionListener(e -> updateTransactionTable());
    header.add(txnFilter, BorderLayout.EAST);

    p.add(header, BorderLayout.NORTH);

    // Table
    String[] cols = { "Time", "Order ID", "Type", "Item", "Qty", "Amount" };
    transactionModel = new javax.swing.table.DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int r, int c) {
        return false;
      }
    };

    JTable table = new JTable(transactionModel);
    table.setBackground(BG_CARD);
    table.setForeground(TXT_PRIMARY);
    table.setGridColor(BORDER_COLOR);
    table.setRowHeight(30);
    table.setShowVerticalLines(false);
    table.setFont(new Font("SansSerif", Font.PLAIN, 14));

    // Header Style
    javax.swing.table.JTableHeader th = table.getTableHeader();
    th.setBackground(BG_MAIN);
    th.setForeground(TXT_SECONDARY);
    th.setFont(new Font("SansSerif", Font.BOLD, 12));
    th.setBorder(new LineBorder(BORDER_COLOR, 0, false));

    JScrollPane scroll = new JScrollPane(table);
    scroll.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // Spacing
    scroll.getViewport().setBackground(BG_CARD);

    p.add(scroll, BorderLayout.CENTER);
    return p;
  }

  private void updateTransactionTable() {
    if (transactionModel == null)
      return;
    transactionModel.setRowCount(0);

    if (currentTransactions == null || currentTransactions.isEmpty())
      return;

    String filter = (String) txnFilter.getSelectedItem();

    for (ThongKeDAO.TransactionRow r : currentTransactions) {
      // Filter Logic
      if (filter != null && !filter.equals("All Types")) {
        if (!r.type.equalsIgnoreCase(filter))
          continue;
      }

      transactionModel.addRow(new Object[] {
          r.time, // Full Date Time
          r.orderId,
          r.type,
          r.item,
          r.qty,
          String.format("%,.0f", r.price)
      });
    }
  }

  // --- ASYNC DATA LOADING ---
  private static class DashboardData {
    double revenue, occupancy, concession;
    int tickets;
    double[] ticketDist, foodDist;
    int numBars;
    java.util.List<Object[]> genreData;
    java.util.List<Object[]> concessionData;
    java.util.List<ThongKeDAO.TransactionRow> transactions; // NEW
  }

  private void loadData() {
    // Capture state for background thread
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(selectedDate);
    final int m = cal.get(java.util.Calendar.MONTH) + 1;
    final int y = cal.get(java.util.Calendar.YEAR);
    final boolean monthly = isMonthlyMode;
    final java.util.Date sDate = selectedDate;

    // Use Global Loader
    com.cinema.util.UIUtils.runAsync(this, () -> {
      DashboardData data = new DashboardData();
      if (monthly) {
        data.revenue = dao.getRevenue(m, y);
        data.tickets = dao.getTickets(m, y);
        data.occupancy = dao.getOccupancy(m, y);

        // Monthly Concession
        double[] dailyC = dao.getDailyConcessionRevenueStruct(m, y);
        data.concession = 0;
        for (double d : dailyC)
          data.concession += d;

        data.ticketDist = dao.getDailyRevenueStruct(m, y);
        data.foodDist = dailyC;
        data.numBars = 31;

        data.genreData = dao.getRevenueByGenre(m, y);
        data.concessionData = dao.getRevenueByConcessionType(m, y);
        data.transactions = dao.getDetailTransactions(m, y); // Fetch Monthly Transactions
      } else {
        data.revenue = dao.getRevenue(sDate);
        data.revenue = dao.getRevenue(sDate);
        data.tickets = dao.getTickets(sDate);
        data.occupancy = dao.getOccupancy(sDate);
        data.concession = dao.getConcessionSales(sDate);

        data.ticketDist = dao.getHourlyRevenue(sDate);
        data.foodDist = dao.getHourlyConcessionRevenue(sDate);
        if (data.foodDist.length > 0)
          data.foodDist[0] = 0;
        data.numBars = 24;

        data.genreData = dao.getRevenueByGenre(sDate);
        data.genreData = dao.getRevenueByGenre(sDate);
        data.concessionData = dao.getRevenueByConcessionType(sDate);
        data.transactions = dao.getDetailTransactions(sDate); // Fetch Detailed Transactions
      }
      return data;
    }, (data) -> {
      updateStatsUI(data);
      updateStatsUI(data);
      updateChartsUI(data);
      currentTransactions = data.transactions; // Store
      updateTransactionTable(); // Update with current filter
    });
  }


}
