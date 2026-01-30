package com.cinema.gui;

import com.cinema.dto.SanPham;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class ConcessionCard extends JPanel {

  private SanPham product;

  private boolean isHovered = false;

  // Colors
  private static final Color BG_CARD = Color.decode("#1E1E1E");
  private static final Color BG_HOVER = Color.decode("#252525");
  private static final Color ACCENT_RED = Color.decode("#FF3333");
  private static final Color TXT_PRIMARY = Color.WHITE;
  private static final Color TXT_SECONDARY = new Color(150, 150, 150);
  private static final Color STOCK_GREEN = Color.decode("#2ECC71");
  private static final Color STOCK_LOW = Color.decode("#E74C3C"); // Red
  private static final Color STOCK_MED = Color.decode("#F39C12"); // Orange

  // Dimensions
  private static final int CARD_W = 240;
  private static final int CARD_H = 320;
  private static final int IMG_H = 180;

  public ConcessionCard(SanPham sp, ActionListener onEdit, ActionListener onDelete) {
    this.product = sp;
    this.product = sp;

    setPreferredSize(new Dimension(CARD_W, CARD_H));
    setOpaque(false);
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    setLayout(null);

    // Hover Effect
    // Hover Effect
    addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        isHovered = true;
        repaint();
      }

      public void mouseExited(MouseEvent e) {
        isHovered = false;
        repaint();
      }

    });

    // Add Buttons
    setLayout(null); // Absolute layout

    JButton btnEdit = new JButton("✎");
    btnEdit.setBounds(CARD_W - 80, 10, 30, 30);
    styleActionButton(btnEdit, new Color(40, 40, 40));
    btnEdit.addActionListener(onEdit);

    JButton btnDelete = new JButton("🗑");
    btnDelete.setBounds(CARD_W - 40, 10, 30, 30);
    styleActionButton(btnDelete, ACCENT_RED);
    btnDelete.addActionListener(onDelete);

    add(btnEdit);
    add(btnDelete);
  }

  private void styleActionButton(JButton btn, Color bg) {
    btn.setBackground(bg);
    btn.setForeground(Color.WHITE);
    btn.setBorder(null);
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
    // Make circular
    btn.setBorder(new javax.swing.border.LineBorder(new Color(255, 255, 255, 50), 1, true));
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    // 1. Background (Rounded)
    g2.setColor(isHovered ? BG_HOVER : BG_CARD);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

    // 2. Image Area (Top Half)
    drawProductImage(g2);

    // 3. Badges (Top Left / Right)
    drawBadges(g2);

    // 4. Content (Bottom Half)
    int y = IMG_H + 20;
    int x = 15;

    // Title
    g2.setColor(TXT_PRIMARY);
    g2.setFont(new Font("SansSerif", Font.BOLD, 16));
    g2.drawString(truncate(product.getTenSP(), 20), x, y);

    // SKU
    y += 20;
    g2.setColor(TXT_SECONDARY);
    g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
    String sku = generateSKU(product);
    g2.drawString("SKU: " + sku, x, y);

    // Price (Left)
    y += 40;
    g2.setColor(ACCENT_RED);
    g2.setFont(new Font("SansSerif", Font.BOLD, 20));
    String price = "$" + String.format("%.2f", product.getGiaBan()); // Assuming data is small enough to look like USD,
                                                                     // else modify
    if (product.getGiaBan() > 1000)
      price = String.format("%,.0f đ", product.getGiaBan()); // VND handling
    g2.drawString(price, x, y);

    // Stock Badge (Right)
    drawStockBadge(g2, getWidth() - 85, y - 20);

    // Stock Progress Bar (Bottom)
    drawProgressBar(g2, x, getHeight() - 25);

    // Stock Count Text
    g2.setColor(TXT_SECONDARY);
    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
    String stockTxt = String.valueOf(product.getSoLuongTon());
    FontMetrics fm = g2.getFontMetrics();
    g2.drawString(stockTxt, getWidth() - 15 - fm.stringWidth(stockTxt), getHeight() - 15);
  }

  private void drawProductImage(Graphics2D g2) {
    // Clip to rounded top
    Shape oldClip = g2.getClip();
    g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20)); // Clip whole card first

    // Draw Mock Placeholder or Real Image
    // In real app, load image. Here we simulate a nice bg.
    g2.setColor(new Color(30, 30, 30));
    g2.fillRect(0, 0, getWidth(), IMG_H);

    try {
      ImageIcon icon = new ImageIcon(product.getImageURL());
      if (icon.getIconWidth() > 0) {
        // Scale to fit centered
        Image img = icon.getImage();
        // Draw centered
        int iw = icon.getIconWidth();
        int ih = icon.getIconHeight();
        double scale = Math.min((double) getWidth() / iw, (double) IMG_H / ih);
        if (scale > 1)
          scale = 1; // Don't upscale too much

        int dw = (int) (iw * scale * 0.8); // 80% size
        int dh = (int) (ih * scale * 0.8);
        g2.drawImage(img, (getWidth() - dw) / 2, (IMG_H - dh) / 2 + 10, dw, dh, null);
      } else {
        // Fallback text
        drawCenteredString(g2, "No Image", getWidth() / 2, IMG_H / 2, TXT_SECONDARY);
      }
    } catch (Exception e) {
      drawCenteredString(g2, "Error", getWidth() / 2, IMG_H / 2, Color.RED);
    }

    g2.setClip(oldClip);
  }

  private void drawBadges(Graphics2D g2) {
    // Best Seller or Combo Deal logic
    String type = product.getLoaiSP();
    if ("Combo".equalsIgnoreCase(type)) {
      drawBadge(g2, "COMBO DEAL", 15, 15, ACCENT_RED);
    } else if (product.getSoLuongTon() > 50) {
      // drawBadge(g2, "BEST SELLER", 15, 15, new Color(200, 150, 0));
      // Optional, user mockup has it.
    }
  }

  private void drawBadge(Graphics2D g2, String text, int x, int y, Color bg) {
    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
    FontMetrics fm = g2.getFontMetrics();
    int w = fm.stringWidth(text) + 16;
    int h = 22;

    g2.setColor(bg);
    g2.fillRoundRect(x, y, w, h, 6, 6);
    g2.setColor(Color.WHITE);
    g2.drawString(text, x + 8, y + 15);
  }

  private void drawStockBadge(Graphics2D g2, int x, int y) {
    int stock = product.getSoLuongTon();
    Color bg = STOCK_GREEN;
    String text = "In Stock";
    Color txtColor = new Color(100, 255, 150);

    if (stock == 0) {
      bg = new Color(60, 60, 60);
      text = "Out of Stock";
      txtColor = Color.GRAY;
    } else if (stock < 20) {
      bg = new Color(60, 20, 20); // Dark Red
      text = "Low Stock";
      txtColor = new Color(255, 100, 100);
    } else {
      bg = new Color(20, 60, 30); // Dark Green
    }

    g2.setColor(bg);
    g2.fillRoundRect(x, y, 70, 24, 6, 6);

    g2.setColor(txtColor);
    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
    FontMetrics fm = g2.getFontMetrics();
    int tx = x + (70 - fm.stringWidth(text)) / 2;
    g2.drawString(text, tx, y + 16);
  }

  private void drawProgressBar(Graphics2D g2, int x, int y) {
    int w = 150;
    int h = 6;
    g2.setColor(new Color(60, 60, 60));
    g2.fillRoundRect(x, y, w, h, 6, 6);

    int stock = product.getSoLuongTon();
    // Assume max 100 for bar
    int max = 100;
    int fillW = Math.min(w, (int) ((double) stock / max * w));

    Color barColor = STOCK_GREEN;
    if (stock < 20)
      barColor = STOCK_LOW;
    else if (stock < 50)
      barColor = STOCK_MED;

    g2.setColor(barColor);
    g2.fillRoundRect(x, y, fillW, h, 6, 6);
  }

  private String generateSKU(SanPham sp) {
    String type = sp.getLoaiSP() != null ? sp.getLoaiSP() : "GEN";
    String prefix = type.length() >= 3 ? type.substring(0, 3).toUpperCase() : type.toUpperCase();
    return prefix + "-" + String.format("%03d", sp.getMaSP());
  }

  private String truncate(String s, int len) {
    if (s.length() > len)
      return s.substring(0, len) + "...";
    return s;
  }

  private void drawCenteredString(Graphics2D g2, String s, int x, int y, Color c) {
    g2.setColor(c);
    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
    FontMetrics fm = g2.getFontMetrics();
    g2.drawString(s, x - fm.stringWidth(s) / 2, y + 5);
  }
}
