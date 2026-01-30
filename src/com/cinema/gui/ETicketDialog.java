package com.cinema.gui;

import com.cinema.dto.LichChieu;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ETicketDialog extends JDialog {

  private final LichChieu showtime;
  private final Map<String, Double> seatPrices;
  private final Map<String, Integer> snacks;
  private final Map<String, Double> snackPrices;
  private final List<String> discountDetails;
  private final double totalAmount;
  private final int invoiceId;
  // private final String customerName; // Unused
  private final String orderCode;

  public ETicketDialog(Window owner, LichChieu showtime, Map<String, Double> seatPrices, Map<String, Integer> snacks,
      Map<String, Double> snackPrices,
      List<String> discountDetails, double total, int invoiceId, String custName, String orderCode) {
    super(owner, "Digital E-Ticket", ModalityType.APPLICATION_MODAL);
    this.showtime = showtime;
    this.seatPrices = seatPrices;
    this.snacks = snacks;
    this.snackPrices = snackPrices;
    this.discountDetails = discountDetails;
    this.totalAmount = total;
    this.invoiceId = invoiceId;
    // this.customerName = custName;
    this.orderCode = orderCode;

    initUI();
  }

  private void initUI() {
    setLayout(new BorderLayout());
    setBackground(new Color(240, 240, 245));

    // Scroll Pane for the ticket content in case it's long
    TicketPanel ticketPanel = new TicketPanel();
    JScrollPane scroll = new JScrollPane(ticketPanel);
    scroll.setBorder(null);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    add(scroll, BorderLayout.CENTER);

    // Buttons
    JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pBtns.setBackground(Color.WHITE);

    JButton btnPrint = new JButton("Print Ticket");
    btnPrint.setBackground(new Color(40, 40, 40));
    btnPrint.setForeground(Color.WHITE);
    btnPrint.setFocusPainted(false);
    btnPrint.addActionListener(e -> printTicket(ticketPanel));

    JButton btnClose = new JButton("Close");
    btnClose.addActionListener(e -> dispose());

    pBtns.add(btnPrint);
    pBtns.add(btnClose);
    add(pBtns, BorderLayout.SOUTH);

    setSize(420, 750);
    setLocationRelativeTo(getOwner());
  }

  private void printTicket(JPanel panel) {
    java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
    job.setJobName("E-Ticket #" + invoiceId);
    job.setPrintable((graphics, pageFormat, pageIndex) -> {
      if (pageIndex > 0)
        return java.awt.print.Printable.NO_SUCH_PAGE;
      Graphics2D g2 = (Graphics2D) graphics;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      // Scale to fit width
      // Scale to fit both width and height (fit to page)
      double scaleX = pageFormat.getImageableWidth() / panel.getWidth();
      double scaleY = pageFormat.getImageableHeight() / panel.getHeight();
      double scale = Math.min(scaleX, scaleY);

      if (scale > 1)
        scale = 1; // Don't upscale small receipts? Actually upscaling might be fine, but safe to
                   // cap at 1 to keep crisp.

      g2.scale(scale, scale);
      panel.print(g2);
      return java.awt.print.Printable.PAGE_EXISTS;
    });

    boolean doPrint = job.printDialog();
    if (doPrint) {
      try {
        job.print();
      } catch (java.awt.print.PrinterException ex) {
        JOptionPane.showMessageDialog(this, "Print Error: " + ex.getMessage());
      }
    }
  }

  // Custom Panel to draw the E-Ticket
  private class TicketPanel extends JPanel {

    public TicketPanel() {
      int h = 750; // Base height increased
      if (seatPrices != null && !seatPrices.isEmpty())
        h += (seatPrices.size() * 30); // Increased buffer
      if (snacks != null && !snacks.isEmpty())
        h += (snacks.size() * 30) + 100; // Increased fixed header buffer
      if (discountDetails != null && !discountDetails.isEmpty())
        h += (discountDetails.size() * 30) + 100; // Increased fixed header buffer

      h += 100; // Safety bottom padding
      setPreferredSize(new Dimension(380, h));
      setBackground(new Color(245, 245, 250)); // Match scroll bg
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      int w = getWidth();
      int cardH = getHeight() - 40; // Approx
      int margin = 20;
      int cardW = w - (margin * 2);
      int x = margin;
      int y = 20;

      // 1. Draw Ticket Paper Shadow
      g2.setColor(new Color(200, 200, 200));
      g2.fillRoundRect(x + 5, y + 5, cardW, cardH, 15, 15);

      // 2. Draw Ticket Paper (White)
      g2.setColor(Color.WHITE);
      g2.fillRoundRect(x, y, cardW, cardH, 15, 15);

      // --- CONTENT DRAWING ---
      int cx = x + 20; // Content X padding
      int cy = y + 40;

      // Logo Icon
      g2.setColor(new Color(229, 9, 20)); // Netflix Red
      g2.fillRoundRect(w / 2 - 20, cy, 40, 25, 5, 5);
      g2.setColor(Color.WHITE);
      g2.fillOval(w / 2 - 15, cy + 8, 4, 4);
      g2.fillOval(w / 2 + 11, cy + 8, 4, 4);
      cy += 40;

      // Cinema Name
      g2.setColor(Color.BLACK);
      g2.setFont(new Font("SansSerif", Font.BOLD, 22));
      drawCenteredString(g2, "CINEMA MANAGER", w, cy);
      cy += 20;

      // Subtitle
      g2.setColor(Color.GRAY);
      g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
      drawCenteredString(g2, "DIGITAL RECEIPT & INVOICE #" + invoiceId, w, cy);
      cy += 15;

      // Order Code
      g2.setColor(new Color(229, 9, 20));
      g2.setFont(new Font("Monospaced", Font.BOLD, 12));
      drawCenteredString(g2, "REF: " + (orderCode != null ? orderCode : "---"), w, cy);
      cy += 20;

      // Dotted Separator
      drawDottedLine(g2, x, x + cardW, cy);
      cy += 30;

      if (showtime != null) {
        // Section: MOVIE
        drawLabel(g2, "MOVIE", cx, cy);
        cy += 20;
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString(showtime.getTenPhim(), cx, cy);
        cy += 30;

        // Section: DATETIME
        drawLabel(g2, "DATE", cx, cy);
        drawLabel(g2, "TIME", cx + 160, cy);
        cy += 20;

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        String dateStr = showtime.getNgayChieu().toString();
        g2.drawString(dateStr, cx, cy);
        g2.drawString(showtime.getGioChieu().toString().substring(0, 5), cx + 160, cy);
        cy += 30;

        // Section: HALL
        drawLabel(g2, "HALL", cx, cy);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(showtime.getTenPhong(), cx, cy + 20);
        cy += 50;

        // Section: ITEMS
        drawLabel(g2, "ITEMS", cx, cy);
        cy += 20;

        if (seatPrices != null) {
          for (Map.Entry<String, Double> entry : seatPrices.entrySet()) {
            String sName = "Seat " + entry.getKey();
            double sPrice = entry.getValue();
            String right = String.format("%,.0f", sPrice);
            drawRow(g2, "\uD83C\uDFAB " + sName, right, cx, cy, cardW - 40);
            cy += 20;
          }
        }
      }

      // SNACKS
      if (snacks != null && !snacks.isEmpty()) {
        cy += 10;
        drawLabel(g2, "SNACKS & DRINKS", cx, cy);
        cy += 20;
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

        for (Map.Entry<String, Integer> entry : snacks.entrySet()) {
          String name = entry.getKey();
          int qty = entry.getValue();
          if (name.equalsIgnoreCase("Bap"))
            name = "Popcorn (L)";
          if (name.equalsIgnoreCase("CoCa CoLA"))
            name = "Coca Cola (L)";

          String qtyStr = "x" + qty;
          double price = 0;
          if (snackPrices != null && snackPrices.containsKey(entry.getKey())) {
            price = snackPrices.get(entry.getKey());
          }

          String right = String.format("%,.0f", price);
          drawRow(g2, name + " " + qtyStr, right, cx, cy, cardW - 40);
          cy += 20;
        }
        cy += 10;
      }

      // DISCOUNTS
      if (discountDetails != null && !discountDetails.isEmpty()) {
        cy += 10;
        drawLabel(g2, "DISCOUNTS", cx, cy);
        cy += 20;

        for (String d : discountDetails) {
          String[] parts = d.split("\\|");
          String name = parts[0];
          double val = 0;
          try {
            val = Double.parseDouble(parts[1]);
          } catch (Exception e) {
          }

          g2.setColor(new Color(220, 80, 80));
          g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

          String right = "-" + String.format("%,.0f", val);

          g2.drawString(name, cx, cy);
          int rw = g2.getFontMetrics().stringWidth(right);
          g2.drawString(right, cx + (cardW - 40) - rw, cy);

          cy += 20;
        }
        cy += 10;
      }

      // TOTAL moved up
      drawDottedLine(g2, x, x + cardW, cy);
      cy += 20;

      g2.setColor(Color.BLACK);
      g2.setFont(new Font("SansSerif", Font.BOLD, 16));
      drawRow(g2, "TOTAL", String.format("%,.0f VNĐ", totalAmount), cx, cy, cardW - 40);
      cy += 40;

      // QR Code
      int qrSize = 100;
      int qrX = (w - qrSize) / 2;
      drawQRCode(g2, qrX, cy, qrSize);
      cy += qrSize + 20;

      drawCenteredString(g2, "SCAN AT GATE ENTRANCE", w, cy);
      cy += 20;

      drawDottedLine(g2, x, x + cardW, cy);
      cy += 20;

      // Footer
      g2.setColor(Color.GRAY);
      g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
      String footer = "Thank you for choosing Cinema Manager.";
      drawCenteredString(g2, footer, w, cy);
    }
  }

  // Helper methods (can be in ETicketDialog or TicketPanel, put here for access)

  private void drawLabel(Graphics2D g2, String text, int x, int y) {
    g2.setColor(new Color(150, 150, 150));
    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
    g2.drawString(text, x, y);
  }

  private void drawRow(Graphics2D g2, String left, String right, int x, int y, int w) {
    g2.setColor(Color.DARK_GRAY);
    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
    g2.drawString(left, x, y);
    int rw = g2.getFontMetrics().stringWidth(right);
    g2.drawString(right, x + w - rw, y);
  }

  private void drawCenteredString(Graphics2D g2, String text, int totalWidth, int y) {
    FontMetrics fm = g2.getFontMetrics();
    int x = (totalWidth - fm.stringWidth(text)) / 2;
    g2.drawString(text, x, y);
  }

  private void drawDottedLine(Graphics2D g2, int x1, int x2, int y) {
    Stroke original = g2.getStroke();
    g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5 }, 0));
    g2.setColor(Color.LIGHT_GRAY);
    g2.drawLine(x1, y, x2, y);
    g2.setStroke(original);
  }

  private void drawQRCode(Graphics2D g2, int x, int y, int size) {
    g2.setColor(Color.WHITE);
    g2.fillRect(x, y, size, size);

    int cells = 25;
    int cellSize = size / cells;
    int realSize = cellSize * cells;
    int margin = (size - realSize) / 2;
    int startX = x + margin;
    int startY = y + margin;

    g2.setColor(Color.BLACK);
    Random r = new Random(invoiceId);

    for (int i = 0; i < cells; i++) {
      for (int j = 0; j < cells; j++) {
        // Skip Finder Patterns
        if (i < 7 && j < 7)
          continue;
        if (i >= cells - 7 && j < 7)
          continue;
        if (i < 7 && j >= cells - 7)
          continue;

        if (r.nextBoolean()) {
          g2.fillRect(startX + i * cellSize, startY + j * cellSize, cellSize, cellSize);
        }
      }
    }

    drawFinderPattern(g2, startX, startY, cellSize);
    drawFinderPattern(g2, startX + (cells - 7) * cellSize, startY, cellSize);
    drawFinderPattern(g2, startX, startY + (cells - 7) * cellSize, cellSize);
  }

  private void drawFinderPattern(Graphics2D g2, int x, int y, int cellSize) {
    g2.setColor(Color.BLACK);
    g2.fillRect(x, y, 7 * cellSize, 7 * cellSize);
    g2.setColor(Color.WHITE);
    g2.fillRect(x + cellSize, y + cellSize, 5 * cellSize, 5 * cellSize);
    g2.setColor(Color.BLACK);
    g2.fillRect(x + 2 * cellSize, y + 2 * cellSize, 3 * cellSize, 3 * cellSize);
  }
}
