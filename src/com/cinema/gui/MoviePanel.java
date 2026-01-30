package com.cinema.gui;

import com.cinema.dao.PhimDAO;
import com.cinema.dto.Phim;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.io.File;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MoviePanel extends JPanel {

    // --- COLORS ---
    private static final Color BG_MAIN = Color.decode("#120808"); // Deep dark red/brown
    private static final Color BG_PANEL = Color.decode("#1E1617"); // Lighter panel bg
    private static final Color ACCENT_RED = Color.decode("#E50914"); // Netflix-like Red
    private static final Color TXT_PRIMARY = Color.white;
    private static final Color TXT_SECONDARY = new Color(170, 170, 170);
    private static final Color SELECTION_BG = new Color(35, 20, 20); // Subtle red tint for selection

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenPhim, txtThoiLuong, txtDaoDien, txtReleaseDate;
    private JComboBox<String> cboTheLoai, cboStatus;
    private JTextArea txtSynopsis;
    private JTextField txtSearch; // Promoted
    private PhimDAO phimDAO = new PhimDAO();
    private int selectedMovieId = -1;
    private String currentPosterPath = null;

    public MoviePanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);

        // 1. Header (Title + Toolbar)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Movie List (Center)
        add(createListPanel(), BorderLayout.CENTER);

        // 3. Detail/Edit Section (South)
        add(createDetailPanel(), BorderLayout.SOUTH);
    }

    // =========================================================================
    // SECTION 1: HEADER & TOOLBAR
    // =========================================================================
    private JPanel createHeaderPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_MAIN);
        p.setBorder(new EmptyBorder(25, 40, 20, 40));

        // Top Row: Title & Stats
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setOpaque(false);

        JPanel pTitle = new JPanel(new GridLayout(2, 1));
        pTitle.setOpaque(false);
        JLabel lblTitle = new JLabel("Movie Management");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(TXT_PRIMARY);
        JLabel lblSub = new JLabel("Manage your cinema's movie database and details");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(TXT_SECONDARY);
        pTitle.add(lblTitle);
        pTitle.add(lblSub);

        pTop.add(pTitle, BorderLayout.WEST);

        // Bottom Row: Toolbar
        JPanel pToolbar = new JPanel(new BorderLayout());
        pToolbar.setOpaque(false);
        pToolbar.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel pActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pActions.setOpaque(false);

        JButton btnAdd = createButton("+ Add New Movie", ACCENT_RED, Color.WHITE);
        btnAdd.addActionListener(e -> clearInput());

        JButton btnDel = createButton("Delete", BG_PANEL, new Color(255, 100, 100));
        btnDel.setBorder(new LineBorder(new Color(100, 40, 40)));
        btnDel.addActionListener(e -> deleteMovie());

        pActions.add(btnAdd);
        pActions.add(btnAdd);
        pActions.add(btnDel);

        // Search
        txtSearch = new JTextField("  Search by title, genre, or ID...");
        txtSearch.setPreferredSize(new Dimension(300, 40));
        txtSearch.setBackground(BG_PANEL);
        txtSearch.setForeground(TXT_SECONDARY);
        txtSearch.setBorder(new LineBorder(new Color(50, 50, 50), 1, true));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadData(txtSearch.getText().trim());
            }
        });
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals("  Search by title, genre, or ID...")) {
                    txtSearch.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("  Search by title, genre, or ID...");
                }
            }
        });

        pToolbar.add(pActions, BorderLayout.WEST);
        pToolbar.add(txtSearch, BorderLayout.EAST);

        p.add(pTop, BorderLayout.NORTH);
        p.add(pToolbar, BorderLayout.SOUTH);

        return p;
    }

    // =========================================================================
    // SECTION 2: MOVIE LIST (TABLE)
    // =========================================================================
    private JPanel createListPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_MAIN);
        p.setBorder(new EmptyBorder(0, 40, 0, 40));

        String[] cols = { "POSTER", "MOVIE TITLE", "GENRE", "DURATION", "RELEASE DATE", "STATUS" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setBackground(BG_MAIN); // Make rows transparent-ish
        table.setForeground(TXT_PRIMARY);
        table.setRowHeight(120);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0)); // No gaps, handled by border
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(TXT_PRIMARY);

        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_MAIN);
        header.setForeground(new Color(120, 120, 120)); // Dim header text
        header.setFont(new Font("SansSerif", Font.BOLD, 10));
        header.setBorder(new LineBorder(new Color(40, 40, 40), 1));
        header.setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Apply Renderers
        table.getColumnModel().getColumn(0).setCellRenderer(new PosterCellRenderer()); // Col 0: Poster + Strip
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setCellRenderer(new TitleCellRenderer());
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setCellRenderer(new BadgeCellRenderer()); // Genre
        table.getColumnModel().getColumn(3).setCellRenderer(new TextCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new TextCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new BadgeCellRenderer()); // Status

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
                loadDetails(table.getSelectedRow());
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_MAIN);
        scroll.setBorder(null);
        p.add(scroll);
        return p;
    }

    // =========================================================================
    // SECTION 3: EDIT DETAILS (3-COLUMN LAYOUT)
    // =========================================================================
    private JPanel createDetailPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_MAIN);
        p.setBorder(new EmptyBorder(10, 40, 20, 40));

        // Wrapper for the Card Logic
        JPanel card = new RoundedPanel(15, BG_PANEL);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Header (Edit Movie Details --- Save Button)
        JPanel pHead = new JPanel(new BorderLayout());
        pHead.setOpaque(false);
        JLabel lbl = new JLabel("Edit Movie Details", new ImageIcon("edit_icon_placeholder.png"), JLabel.LEFT);
        lbl.setForeground(TXT_PRIMARY);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel pBtns = new JPanel(new FlowLayout());
        pBtns.setOpaque(false);
        JButton btnSave = createButton("Save Changes", ACCENT_RED, Color.WHITE);
        btnSave.addActionListener(e -> saveMovie());

        pBtns.add(btnSave);

        pHead.add(lbl, BorderLayout.WEST);
        pHead.add(pBtns, BorderLayout.EAST);

        card.add(pHead, BorderLayout.NORTH);

        // --- 3-COLUMN CONTENT ---
        JPanel pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.X_AXIS)); // Horizontal layout
        pContent.setOpaque(false);
        pContent.setBorder(new EmptyBorder(15, 0, 0, 0));

        // COL 1: POSTER (Large)
        JPanel pPoster = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Image if exists
                if (currentPosterPath != null && !currentPosterPath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(currentPosterPath);
                    g2.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Art style placeholder
                    GradientPaint gp = new GradientPaint(0, 0, new Color(200, 200, 200), 0, getHeight(),
                            new Color(100, 100, 100));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                    // Hanging Strings
                    g2.setColor(new Color(50, 50, 50));
                    g2.drawLine(40, 0, 40, 10);
                    g2.drawLine(120, 0, 120, 10);

                    // Text
                    g2.setColor(Color.DARK_GRAY);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                    FontMetrics fm = g2.getFontMetrics();
                    String text = "CLICK UPLOAD";
                    g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, getHeight() / 2);
                }
            }
        };
        pPoster.setOpaque(false);
        pPoster.setPreferredSize(new Dimension(150, 200));
        pPoster.setMaximumSize(new Dimension(150, 200));
        pPoster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pPoster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                choosePoster();
            }
        });

        // COL 2: FORM FIELDS
        JPanel pForm = new JPanel(new GridLayout(4, 2, 20, 20)); // 4 rows, 2 cols logic
        pForm.setOpaque(false);
        pForm.setBorder(new EmptyBorder(0, 20, 0, 30));

        // Row 1: Title (Full Width effectively in grid logic)
        // Check gridbag for better layout. Staying simple for now.
        // Actually, let's just do vertical stack if we want precise control.
        // Let's stick to GridBag for the center form.
        JPanel pCenter = new JPanel(new GridBagLayout());
        pCenter.setOpaque(false);
        pCenter.setBorder(new EmptyBorder(0, 30, 0, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 15);

        // Title (Span 2)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtTenPhim = createField();
        pCenter.add(createInputGroup("MOVIE TITLE", txtTenPhim), gbc);

        // Genre | Status
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        cboTheLoai = new JComboBox<>(new String[] { "Sci-Fi", "Action", "Drama", "Horror", "Comedy" });
        styleCombo(cboTheLoai);
        pCenter.add(createInputGroup("GENRE", cboTheLoai), gbc);

        gbc.gridx = 1;
        cboStatus = new JComboBox<>(new String[] { "Active", "Archived", "Coming Soon" });
        styleCombo(cboStatus);
        pCenter.add(createInputGroup("STATUS", cboStatus), gbc);

        // Duration | Release Date
        gbc.gridx = 0;
        gbc.gridy++;
        txtThoiLuong = createField();
        pCenter.add(createInputGroup("DURATION (MINS)", txtThoiLuong), gbc);

        gbc.gridx = 1;
        txtReleaseDate = createField();
        txtReleaseDate.setText("07/16/2010"); // Mock
        pCenter.add(createInputGroup("RELEASE DATE", txtReleaseDate), gbc);

        // Director
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        txtDaoDien = createField();
        pCenter.add(createInputGroup("DIRECTOR", txtDaoDien), gbc);

        // COL 3: SYNOPSIS
        JPanel pSynopsis = new JPanel(new BorderLayout());
        pSynopsis.setOpaque(false);
        JLabel lblSyn = new JLabel("SYNOPSIS");
        lblSyn.setForeground(TXT_SECONDARY);
        lblSyn.setFont(new Font("SansSerif", Font.BOLD, 10));

        txtSynopsis = new JTextArea(
                "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person's idea into a target's subconscious.");
        txtSynopsis.setLineWrap(true);
        txtSynopsis.setWrapStyleWord(true);
        txtSynopsis.setBackground(new Color(25, 20, 20)); // slightly darker
        txtSynopsis.setForeground(new Color(200, 200, 200));
        txtSynopsis.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtSynopsis.setBorder(new EmptyBorder(10, 10, 10, 10));

        pSynopsis.add(lblSyn, BorderLayout.NORTH);
        pSynopsis.add(txtSynopsis, BorderLayout.CENTER);
        pSynopsis.setPreferredSize(new Dimension(300, 0));

        // Assemble
        pContent.add(pPoster);
        pContent.add(pCenter);
        pContent.add(pSynopsis);

        card.add(pContent, BorderLayout.CENTER);
        p.add(card, BorderLayout.CENTER);

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
        return p;
    }

    // =========================================================================
    // SECTION 4: RENDERERS (THE MAGIC)
    // =========================================================================

    // 1. Poster Renderer (Handles Selection Strip + Gradient Poster)

    // 2. Title Renderer (Title + ID)
    class TitleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFoc, int r,
                int c) {
            JPanel p = new JPanel(new GridLayout(2, 1));
            p.setBackground(isSel ? SELECTION_BG : BG_MAIN);
            p.setBorder(new EmptyBorder(0, 0, 0, 0));

            JLabel lTitle = new JLabel(v.toString());
            lTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
            lTitle.setForeground(TXT_PRIMARY);

            // Mock ID from table row to handle missing Phim obj in this cell
            int id = 2010 + r;
            JLabel lSub = new JLabel("ID: #MV-" + id);
            lSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lSub.setForeground(new Color(100, 100, 100));

            p.add(lTitle);
            p.add(lSub);
            return p;
        }
    }

    // 3. Badge Renderer (For Genre and Status)
    class BadgeCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFoc, int r,
                int c) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.setBackground(isSel ? SELECTION_BG : BG_MAIN);

            String text = v.toString();
            Color bg, fg;
            Color border = null;

            // Logic for Color
            if (text.equals("Sci-Fi")) {
                bg = new Color(20, 30, 50);
                fg = new Color(100, 181, 246);
                border = fg;
            } else if (text.equals("Action")) {
                bg = new Color(50, 20, 20);
                fg = new Color(229, 115, 115);
                border = fg;
            } else if (text.equals("Active")) {
                bg = new Color(20, 40, 20);
                fg = new Color(102, 187, 106);
                border = fg;
            } else {
                bg = new Color(40, 40, 40);
                fg = Color.GRAY;
                border = Color.GRAY;
            }

            JLabel l = new JLabel(text);
            l.setFont(new Font("SansSerif", Font.BOLD, 10));
            l.setForeground(fg);
            l.setBorder(new EmptyBorder(4, 10, 4, 10));

            // Custom Paint for Round Pill
            JPanel badge = new JPanel(new BorderLayout()) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 100)); // Dim border
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    super.paintComponent(g);
                }
            };
            badge.setOpaque(false);
            badge.add(l);
            p.add(badge);
            return p;
        }
    }

    // 4. Simple Text Renderer
    class TextCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFoc, int r,
                int c) {
            Component com = super.getTableCellRendererComponent(t, v, isSel, hasFoc, r, c);
            com.setBackground(isSel ? SELECTION_BG : BG_MAIN);
            com.setForeground(new Color(180, 180, 180));
            return com;
        }
    }

    // --- HELPERS & LOGIC ---
    private void choosePoster() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
        if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            currentPosterPath = f.getAbsolutePath();
            // Trigger repaint of the visible detail panel
            // Since pPoster is local in createDetailPanel, we might need a reference or
            // just repaint main panel
            // Ideally we should have made pPoster a class field, but for now:
            repaint();
        }
    }

    private void loadData() {
        loadData(txtSearch.getText().trim());
    }

    private void loadData(String query) {
        // Capture context for background thread
        final String fQuery = query;

        com.cinema.util.UIUtils.runAsync(this, () -> {
            return phimDAO.getAllPhim();
        }, (list) -> {
            tableModel.setRowCount(0);

            boolean isSearch = !fQuery.isEmpty() && !fQuery.equals("Search by title, genre, or ID...");
            String q = fQuery.toLowerCase();

            for (Phim p : list) {
                if (isSearch) {
                    boolean match = p.getTenPhim().toLowerCase().contains(q) ||
                            p.getTheLoai().toLowerCase().contains(q) ||
                            String.valueOf(p.getMaPhim()).contains(q);
                    if (!match)
                        continue;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                String releaseDate = p.getNamSanXuat() != null ? sdf.format(p.getNamSanXuat()) : "TBA";

                tableModel.addRow(new Object[] {
                        p, // Object for Poster
                        p.getTenPhim(),
                        p.getTheLoai(),
                        p.getThoiLuong() + " min",
                        releaseDate,
                        p.getTrangThai()
                });
            }
        });
    }

    private void loadDataLegacy(String query) {
        tableModel.setRowCount(0);
        List<Phim> list = phimDAO.getAllPhim();

        boolean isSearch = !query.isEmpty() && !query.equals("Search by title, genre, or ID...");
        String q = query.toLowerCase();

        for (Phim p : list) {
            if (isSearch) {
                boolean match = p.getTenPhim().toLowerCase().contains(q) ||
                        p.getTheLoai().toLowerCase().contains(q) ||
                        String.valueOf(p.getMaPhim()).contains(q);
                if (!match)
                    continue;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            String releaseDate = p.getNamSanXuat() != null ? sdf.format(p.getNamSanXuat()) : "TBA";

            tableModel.addRow(new Object[] {
                    p, // Object for Poster
                    p.getTenPhim(),
                    p.getTheLoai(),
                    p.getThoiLuong() + " min",
                    releaseDate,
                    p.getTrangThai()
            });
        }
    }

    private void loadDetails(int row) {
        try {
            Phim p = (Phim) tableModel.getValueAt(row, 0);
            selectedMovieId = p.getMaPhim();
            txtTenPhim.setText(p.getTenPhim());
            cboTheLoai.setSelectedItem(p.getTheLoai());
            txtThoiLuong.setText(String.valueOf(p.getThoiLuong()));
            txtDaoDien.setText(p.getDaoDien());

            SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy");
            String dateStr = p.getNamSanXuat() != null ? sdfInput.format(p.getNamSanXuat()) : "";
            txtReleaseDate.setText(dateStr);

            txtSynopsis.setText(p.getMoTa() != null ? p.getMoTa() : "");

            currentPosterPath = p.getPoster();
            cboStatus.setSelectedItem(p.getTrangThai());

            repaint(); // Repaint to show new poster

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearInput() {
        selectedMovieId = -1;
        txtTenPhim.setText("");
        txtThoiLuong.setText("");
        txtDaoDien.setText("");
        txtSynopsis.setText("");
        txtReleaseDate.setText("");
        currentPosterPath = null;
        cboStatus.setSelectedIndex(0);
        table.clearSelection();
        repaint();
    }

    private void saveMovie() {
        try {
            String ten = txtTenPhim.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter movie title!");
                return;
            }

            String loai = cboTheLoai.getSelectedItem().toString();

            int thoiLuong;
            try {
                thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Duration!");
                return;
            }

            String daoDien = txtDaoDien.getText().trim();
            String moTa = txtSynopsis.getText().trim();
            String trangThai = cboStatus.getSelectedItem().toString();

            java.sql.Date releaseDate = null;
            try {
                java.util.Date parsed = new SimpleDateFormat("MM/dd/yyyy").parse(txtReleaseDate.getText().trim());
                releaseDate = new java.sql.Date(parsed.getTime());
            } catch (Exception ex) {
                // Ignore or warn
            }

            Phim p = new Phim(selectedMovieId == -1 ? 0 : selectedMovieId, ten, loai, thoiLuong, daoDien, moTa,
                    trangThai, currentPosterPath, releaseDate);

            boolean success;
            if (selectedMovieId == -1) {
                success = phimDAO.addPhim(p);
            } else {
                success = phimDAO.updatePhim(p);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Saved successfully!");
                loadData();
                clearInput();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save to database.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteMovie() {
        if (selectedMovieId != -1 && JOptionPane.showConfirmDialog(this, "Delete?") == 0) {
            phimDAO.deletePhim(selectedMovieId);
            loadData();
            clearInput();
        }
    }

    // --- UI FACTORIES ---
    // ... basic factories check ...

    // RENDERERS
    // 1. Poster Renderer
    class PosterCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFoc, int r,
                int c) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            p.setBackground(isSel ? SELECTION_BG : BG_MAIN);

            if (isSel) {
                p.setBorder(new CompoundBorder(
                        new EmptyBorder(5, 0, 5, 0),
                        new MatteBorder(0, 3, 0, 0, ACCENT_RED)));
            } else {
                p.setBorder(new EmptyBorder(5, 3, 5, 0));
            }

            if (v instanceof Phim) {
                Phim phim = (Phim) v;
                JPanel poster = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        if (phim.getPoster() != null && !phim.getPoster().isEmpty()) {
                            ImageIcon ico = new ImageIcon(phim.getPoster());
                            g2.drawImage(ico.getImage(), 0, 0, 35, 52, null);
                        } else {
                            GradientPaint gp = new GradientPaint(0, 0, new Color(44, 62, 80), 30, 50,
                                    new Color(76, 161, 175));
                            g2.setPaint(gp);
                            g2.fillRoundRect(0, 0, 35, 52, 5, 5);
                            g2.setColor(new Color(255, 255, 255, 100));
                            String s = phim.getTenPhim().isEmpty() ? "" : phim.getTenPhim().substring(0, 1);
                            g2.drawString(s, 12, 32);
                        }
                    }
                };
                poster.setPreferredSize(new Dimension(35, 52));
                poster.setOpaque(false);
                p.add(poster);
            }
            return p;
        }
    }

    // --- UI FACTORIES ---

    private JButton createButton(String t, Color bg, Color fg) {
        JButton b = new JButton(t);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel createInputGroup(String title, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel l = new JLabel(title);
        l.setFont(new Font("SansSerif", Font.BOLD, 10)); // Uppercase style
        l.setForeground(new Color(100, 100, 100));
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField createField() {
        JTextField t = new JTextField();
        t.setBackground(new Color(30, 30, 30));
        t.setForeground(TXT_PRIMARY);
        t.setCaretColor(ACCENT_RED);
        t.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60)), new EmptyBorder(8, 10, 8, 10)));
        return t;
    }

    private void styleCombo(JComboBox box) {
        box.setBackground(new Color(30, 30, 30));
        box.setForeground(TXT_PRIMARY);
    }

    class RoundedPanel extends JPanel {
        private int r;
        private Color c;

        public RoundedPanel(int radius, Color bg) {
            r = radius;
            c = bg;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        }
    }
}
