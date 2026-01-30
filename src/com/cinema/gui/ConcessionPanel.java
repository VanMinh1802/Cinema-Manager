package com.cinema.gui;

import com.cinema.dao.SanPhamDAO;
import com.cinema.dto.SanPham;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ConcessionPanel extends JPanel {

    private SanPhamDAO spDAO = new SanPhamDAO();
    private List<SanPham> allProducts = new ArrayList<>();
    private JPanel pGrid;
    private JTextField txtSearch;
    private String currentFilter = "All Items";

    // Colors
    private static final Color BG_MAIN = Color.decode("#121212");

    private static final Color TXT_PRIMARY = Color.WHITE;
    private static final Color TXT_SECONDARY = Color.GRAY;
    private static final Color ACCENT_RED = Color.decode("#D93636");

    public ConcessionPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);

        // 1. Header (Title, Buttons)
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(BG_MAIN);
        pHeader.setBorder(new EmptyBorder(30, 40, 20, 40));

        // Title
        JPanel pTitle = new JPanel(new GridLayout(2, 1));
        pTitle.setBackground(BG_MAIN);
        JLabel lblTitle = new JLabel("Concession Inventory");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(TXT_PRIMARY);

        JLabel lblSub = new JLabel("Manage snacks, beverages, and combo availability in real-time.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(TXT_SECONDARY);
        pTitle.add(lblTitle);
        pTitle.add(lblSub);

        // Buttons
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pBtns.setBackground(BG_MAIN);

        JButton btnAdd = new JButton("+ Add Product");
        styleButton(btnAdd, ACCENT_RED, Color.WHITE);
        btnAdd.addActionListener(e -> showProductDialog(null));

        JButton btnAddCombo = new JButton("+ Add Combo");
        styleButton(btnAddCombo, new Color(255, 193, 7), Color.BLACK);
        btnAddCombo.addActionListener(e -> showComboDialog(null));

        pBtns.add(btnAdd);
        pBtns.add(btnAddCombo);

        pHeader.add(pTitle, BorderLayout.WEST);
        pHeader.add(pBtns, BorderLayout.EAST);
        add(pHeader, BorderLayout.NORTH);

        // 2. Toolbar & Content Wrapper
        JPanel pContent = new JPanel(new BorderLayout());
        pContent.setBackground(BG_MAIN);

        // Toolbar (Filters + Search)
        JPanel pToolbar = new JPanel(new BorderLayout());
        pToolbar.setBackground(BG_MAIN);
        pToolbar.setBorder(new EmptyBorder(0, 40, 20, 40));

        // Filter Tabs (Left)
        JPanel pTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pTabs.setBackground(BG_MAIN);
        String[] filters = { "All Items", "Popcorn", "Drinks", "Candies", "Combos" };
        ButtonGroup bg = new ButtonGroup();
        for (String f : filters) {
            JToggleButton btn = new JToggleButton(f);
            stylePillButton(btn);
            btn.addActionListener(e -> {
                currentFilter = f;
                filterList();
            });
            bg.add(btn);
            pTabs.add(btn);
            if (f.equals("All Items"))
                btn.setSelected(true);
        }

        // Search (Right)
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 38));
        txtSearch.setBackground(new Color(30, 30, 30));
        txtSearch.setForeground(TXT_PRIMARY);
        txtSearch.setCaretColor(TXT_PRIMARY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(0, 15, 0, 15)));
        txtSearch.putClientProperty("JTextField.placeholderText", "Search by name or SKU...");
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterList();
            }
        });

        pToolbar.add(pTabs, BorderLayout.WEST);
        JPanel pSearchWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pSearchWrap.setBackground(BG_MAIN);
        pSearchWrap.add(txtSearch);
        pToolbar.add(pSearchWrap, BorderLayout.EAST);

        pContent.add(pToolbar, BorderLayout.NORTH);

        // 3. Grid Content (Responsive Flow)
        pGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Container c = getParent();
                if (c != null && c.getWidth() > 0) {
                    int w = c.getWidth();
                    int targetW = w - 80; // margins
                    int cardW = 240 + 20; // card + gap
                    int cols = Math.max(1, targetW / cardW);
                    int rows = (int) Math.ceil((double) getComponentCount() / cols);
                    d.width = w;
                    d.height = rows * (320 + 20) + 40;
                }
                return d;
            }
        };
        pGrid.setBackground(BG_MAIN);
        pGrid.setBorder(new EmptyBorder(10, 40, 40, 40));

        JScrollPane scroll = new JScrollPane(pGrid);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getViewport().setBackground(BG_MAIN);
        // Force revalidation on resize
        scroll.getViewport().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                pGrid.revalidate();
            }
        });

        pContent.add(scroll, BorderLayout.CENTER);
        add(pContent, BorderLayout.CENTER);
    }

    private void loadData() {
        allProducts = spDAO.getAllSanPham();
        // Update Combo Stock dynamic calculation
        com.cinema.dao.ComboDAO comboDAO = new com.cinema.dao.ComboDAO();
        for (SanPham sp : allProducts) {
            if ("Combo".equalsIgnoreCase(sp.getLoaiSP())) {
                int realStock = comboDAO.calculateAvailableStock(sp.getMaSP());
                sp.setSoLuongTon(realStock);
            }
        }
        filterList();
    }

    private void filterList() {
        pGrid.removeAll();
        String query = txtSearch.getText().toLowerCase();
        List<SanPham> filtered = allProducts.stream().filter(p -> {
            boolean matchName = p.getTenSP().toLowerCase().contains(query);

            // Generate SKU for search
            String typeCode = p.getLoaiSP() != null ? p.getLoaiSP() : "GEN";
            String prefix = typeCode.length() >= 3 ? typeCode.substring(0, 3).toUpperCase() : typeCode.toUpperCase();
            String sku = prefix + "-" + String.format("%03d", p.getMaSP());
            boolean matchSku = sku.toLowerCase().contains(query);

            boolean matchType = true;
            String type = p.getLoaiSP() != null ? p.getLoaiSP() : "";

            // "Popcorn", "Drinks", "Candies", "Combos"
            if (currentFilter.equals("Popcorn") && !type.equalsIgnoreCase("DoAn"))
                matchType = false;
            if (currentFilter.equals("Drinks") && !type.equalsIgnoreCase("Nuoc"))
                matchType = false;
            if (currentFilter.equals("Combos") && !type.equalsIgnoreCase("Combo"))
                matchType = false;
            if (currentFilter.equals("Candies") && !type.equalsIgnoreCase("Keo"))
                matchType = false;

            return (matchName || matchSku) && matchType;
        }).collect(Collectors.toList());

        for (SanPham sp : filtered) {
            ConcessionCard card = new ConcessionCard(sp,
                    e -> showProductDialog(sp),
                    e -> deleteProduct(sp));
            pGrid.add(card);
        }
        pGrid.revalidate();
        pGrid.repaint();
    }

    private void showProductDialog(SanPham sp) {
        if (sp != null && "Combo".equalsIgnoreCase(sp.getLoaiSP())) {
            showComboDialog(sp);
            return;
        }
        ProductDialog dlg = new ProductDialog((Frame) SwingUtilities.getWindowAncestor(this), sp);
        dlg.setVisible(true);
        if (dlg.isSucceeded()) {
            SanPham result = dlg.getProduct();
            if (sp == null)
                spDAO.addSanPham(result);
            else {
                result.setMaSP(sp.getMaSP());
                spDAO.updateSanPham(result);
            }
            loadData();
        }
    }

    private void showComboDialog(SanPham sp) {
        ComboDialog dlg = new ComboDialog((Frame) SwingUtilities.getWindowAncestor(this), sp);
        dlg.setVisible(true);
        if (dlg.isSucceeded()) {
            loadData();
        }
    }

    private void deleteProduct(SanPham sp) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete '" + sp.getTenSP() + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            spDAO.deleteSanPham(sp.getMaSP());
            loadData();
        }
    }

    // Styles
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bg.darker(), 1),
                new EmptyBorder(10, 20, 10, 20)));
    }

    private void stylePillButton(JToggleButton btn) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                JToggleButton b = (JToggleButton) c;

                if (b.isSelected())
                    g2.setColor(Color.WHITE);
                else
                    g2.setColor(new Color(40, 40, 40));

                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);

                if (b.isSelected())
                    g2.setColor(Color.BLACK);
                else
                    g2.setColor(Color.LIGHT_GRAY);

                // int x = (c.getWidth() - fm.stringWidth(c.getName())) / 2; // Unused
                // Standard paint text
                super.paint(g2, c);
            }
        });
        // Override standard text painting above or just use simple tweaks:
        // Actually simpler:
        btn.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                JToggleButton b = (JToggleButton) c;
                if (b.isSelected()) {
                    g2.setColor(Color.WHITE);
                    b.setForeground(Color.BLACK);
                } else {
                    g2.setColor(new Color(35, 35, 35)); // Inner shell
                    b.setForeground(Color.LIGHT_GRAY);
                }
                // Rounded Pill Border
                if (!b.isSelected()) {
                    g2.setStroke(new BasicStroke(1));
                    g2.setColor(new Color(60, 60, 60));
                    g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 30, 30);
                } else {
                    g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);
                }

                super.paint(g, c);
            }
        });
        btn.setPreferredSize(new Dimension(100, 35));
    }
}
