package com.cinema.gui;

import com.cinema.dao.*;
import com.cinema.dto.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class BanVePanel extends JPanel {

    // --- COLORS ---
    private static final Color BG_MAIN = Color.decode("#1A1214");
    private static final Color BG_PANEL = Color.decode("#2B1F22");
    private static final Color BG_ACCENT = Color.decode("#D93636"); // Red
    private static final Color TXT_PRIMARY = Color.white;
    private static final Color TXT_SECONDARY = Color.gray;
    private static final Color SEAT_AVAILABLE = Color.decode("#3E2C30");
    private static final Color SEAT_SELECTED = Color.decode("#00E676"); // Green
    private static final Color SEAT_BOOKED = Color.decode("#D93636"); // Red
    private static final Color ACCENT_RED = Color.decode("#EF4444");
    private static final Color BORDER_COLOR = Color.decode("#3F3F46");

    // --- DATA ---
    private LichChieuDAO lichChieuDAO = new LichChieuDAO();
    private VeDAO veDAO = new VeDAO();
    private DiscountPolicyDAO discountDAO = new DiscountPolicyDAO();
    private WeeklyPromotionDAO promoDAO = new WeeklyPromotionDAO();
    private LoyaltyDAO loyaltyDAO = new LoyaltyDAO();
    private KhachHangDAO khachHangDAO = new KhachHangDAO();

    private LichChieu currentLichChieu;
    private List<String> listGheDaBan = new ArrayList<>();
    private List<String> listGheDangChon = new ArrayList<>();
    private Map<String, String> seatTypeMap = new HashMap<>();
    private Map<String, Integer> currentSnackList = new HashMap<>();
    private List<String> currentDiscountDetails = new ArrayList<>(); // NEW

    private NhanVien nhanVien;


    // Dynamic Product Management
    private SanPhamDAO spDAO = new SanPhamDAO();
    private Map<String, SanPham> dynamicProductCache = new HashMap<>(); // Name -> SanPham Object
    private Map<String, Double> roomPrices = new HashMap<>(); // Dynamic Room Pricing

    // Loyalty & Pricing State
    private KhachHang currentMember = null;
    private DiscountPolicy selectedPolicy = null;
    private WeeklyPromotion todaysPromo = null;
    private int pointsToRedeem = 0;

    // --- COMPONENTS ---
    private JList<LichChieu> listMovies;
    private DefaultListModel<LichChieu> movieModel;
    private JPanel pnlSeatMap;
    private JPanel pnlCartItems;
    private JPanel pDiscountList; // Detailed Breakdown
    private JLabel lblSubtotal, lblTax, lblTotal, lblDiscount, lblOriginalPrice;
    private JLabel lblPointsAvail, lblMoneyVal; // New Loyalty Fields
    private JLabel lblPromoInfo; // NEW: Dynamic Promo Label

    // UI Controls
    private JTextField txtMemberSearch;
    private JLabel lblMemberInfo; // Used for "Member not found" etc.
    private JComboBox<DiscountPolicy> cboPolicies;
    private JTextField txtRedeem;

    // Order ID
    private JLabel lblOrder;
    private String currentOrderCode;

    // Cart State
    private double currentCartTotal = 0;
    private double currentCartTax = 0;
    private double currentCartDiscount = 0;
    private double currentCartSubtotal = 0;

    private void generateNewOrderCode() {
        int random = (int) (Math.random() * 900000) + 100000;
        currentOrderCode = "ORD-" + random;
        if (lblOrder != null) {
            lblOrder.setText(currentOrderCode);
        }
    }

    public BanVePanel(NhanVien nv) {
        this.nhanVien = nv;

        // Ensure guest customer exists
        new KhachHangDAO().ensureGuestCustomer();

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(BG_MAIN);

        // Init Model to avoid NullPointer in CreateLeftPanel
        movieModel = new DefaultListModel<>();

        // Load Data First
        // Initial Load (Async) - Delayed until added to Frame
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                if (fullMovieList == null || fullMovieList.isEmpty()) {
                    loadMoviesAsync();
                }
                // Determine if we need to remove listener?
                // Better keep it safely or use a flag if we only want ONCE.
                // Usually OK to keep if we check empty.
                removeAncestorListener(this);
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });



        // --- LEFT: MOVIE LIST ---
        add(createLeftPanel(), BorderLayout.WEST);

        // --- CENTER: SEAT MAP ---
        add(createCenterPanel(), BorderLayout.CENTER);

        // --- RIGHT: CHECKOUT ---
        JScrollPane scrollRight = new JScrollPane(createRightPanel());
        scrollRight.setBorder(null);
        scrollRight.setOpaque(false);
        scrollRight.getViewport().setOpaque(false);
        // Increase scroll speed
        scrollRight.getVerticalScrollBar().setUnitIncrement(16);
        scrollRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollRight, BorderLayout.EAST);

        generateNewOrderCode();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Background removed as per user request
        g.setColor(BG_MAIN);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // ================= LEFT PANEL =================
    private JPanel createLeftPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false); // Transparent
        p.setPreferredSize(new Dimension(320, 0));
        p.setBorder(new EmptyBorder(20, 20, 20, 0)); // Padding

        // 1. Search Bar
        JTextField txtSearch = new JTextField("Search movies...");
        txtSearch.setBackground(new Color(43, 31, 34)); // Opaque
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BG_PANEL, 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        txtSearch.setPreferredSize(new Dimension(0, 40));

        // 1b. Date Filter
        JPanel pFilter = new JPanel(new BorderLayout(10, 0));
        pFilter.setOpaque(false);
        pFilter.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblDate = new JLabel("Date:");
        lblDate.setForeground(TXT_SECONDARY);

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(java.util.Date.from(java.time.Instant.now())); // Default today
        ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setBackground(BG_PANEL);
        ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setForeground(TXT_PRIMARY);

        pFilter.add(lblDate, BorderLayout.WEST);
        pFilter.add(dateSpinner, BorderLayout.CENTER);

        JPanel pTop = new JPanel(new BorderLayout(0, 10));
        pTop.setOpaque(false);
        pTop.add(txtSearch, BorderLayout.NORTH);
        pTop.add(pFilter, BorderLayout.CENTER);

        p.add(pTop, BorderLayout.NORTH);

        // 2. Movie List
        listMovies = new JList<>(movieModel);
        listMovies.setBackground(new Color(26, 18, 20, 100)); // Semi-transparent
        listMovies.setOpaque(false);
        listMovies.setCellRenderer(new MovieCellRenderer());
        listMovies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMovies.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentLichChieu = listMovies.getSelectedValue();
                if (currentLichChieu != null) {
                    // Update Promo based on Showtime Date
                    java.time.LocalDate showDate = currentLichChieu.getNgayChieu().toLocalDate();
                    // Convert Java Date WeekDay to DB format (Sun=1, Mon=2...)
                    // Java: Mon=1, Sun=7
                    int javaDay = showDate.getDayOfWeek().getValue();
                    int dbDay = javaDay + 1;
                    if (javaDay == 7)
                        dbDay = 1;

                    todaysPromo = promoDAO.getPromotionByDay(dbDay);
                    // Also check for Special Holidays if implemented
                }
                loadSeatMap();
                resetCart();
            }
        });

        // Filter Logic
        dateSpinner.addChangeListener(e -> {
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            java.time.LocalDate localDate = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            filterMovies(localDate, txtSearch.getText());
        });

        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals("Search movies...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(TXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Search movies...");
                    txtSearch.setForeground(TXT_SECONDARY);
                }
            }
        });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
                java.time.LocalDate localDate = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                filterMovies(localDate, txtSearch.getText());
            }
        });

        // Initial Filter
        java.time.LocalDate today = java.time.LocalDate.now();
        filterMovies(today, ""); // Initial load filtered by today

        JScrollPane scroll = new JScrollPane(listMovies);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private void loadMoviesAsync() {
        movieModel.clear();
        com.cinema.util.UIUtils.runAsync(this, () -> {
            return lichChieuDAO.getAvailableShowtimes();
        }, (dbList) -> {
            fullMovieList = dbList; // Keep reference for filtering
            // Initial filter
            filterMovies(java.time.LocalDate.now(), "");
        });
    }

    private List<LichChieu> fullMovieList = new ArrayList<>();

    private void filterMovies(java.time.LocalDate date, String query) {
        if (movieModel == null)
            return;
        movieModel.clear();

        if (fullMovieList == null || fullMovieList.isEmpty())
            return;

        // Sort by Time
        fullMovieList.sort((l1, l2) -> l1.getGioChieu().compareTo(l2.getGioChieu()));

        for (LichChieu lc : fullMovieList) {
            boolean dateMatch = lc.getNgayChieu().toLocalDate().isEqual(date);
            boolean queryMatch = query.equals("Search movies...") || query.isEmpty() ||
                    lc.getTenPhim().toLowerCase().contains(query.toLowerCase());

            if (dateMatch && queryMatch) {
                movieModel.addElement(lc);
            }
        }
    }

    // ================= CENTER PANEL =================
    private JPanel createCenterPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Choose Seats");
        lblTitle.setForeground(TXT_PRIMARY);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pHeader.add(lblTitle, BorderLayout.WEST);
        p.add(pHeader, BorderLayout.NORTH);

        // Screen & Map Wrapper
        JPanel pWrapper = new JPanel(new BorderLayout());
        pWrapper.setOpaque(false);

        // Custom Paint for Screen (Solid White Arc)
        JPanel pScreen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                int w = getWidth();
                g2.fillArc(-50, -20, w + 100, 60, 190, 160); // Simplified curve top
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String s = "SCREEN";
                g2.drawString(s, (w - fm.stringWidth(s)) / 2, 25);
            }
        };
        pScreen.setOpaque(false);
        pScreen.setPreferredSize(new Dimension(0, 50));
        pWrapper.add(pScreen, BorderLayout.NORTH);

        // Seats Container (Row Labels + Grid)
        JPanel pMapContainer = new JPanel(new BorderLayout(10, 0));
        pMapContainer.setOpaque(false);

        // This panel will hold the 1-10 numbering
        JPanel pRowLabels = new JPanel();
        pRowLabels.setLayout(new BoxLayout(pRowLabels, BoxLayout.Y_AXIS)); // Will set layout in loadSeatMap matches
                                                                           // grid
        pRowLabels.setOpaque(false);

        // Sets Grid
        pnlSeatMap = new JPanel();
        pnlSeatMap.setOpaque(false);

        pMapContainer.add(pRowLabels, BorderLayout.WEST);
        pMapContainer.add(pnlSeatMap, BorderLayout.CENTER);

        // Centering Wrapper using GridBagLayout
        JPanel pCenteringWrapper = new JPanel(new GridBagLayout());
        pCenteringWrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 0, 0); // Spacing from screen
        pCenteringWrapper.add(pMapContainer, gbc);

        // Wrap in ScrollPane
        JScrollPane scrollMap = new JScrollPane(pCenteringWrapper);
        scrollMap.setBorder(null);
        scrollMap.getViewport().setOpaque(false);
        scrollMap.setOpaque(false);
        scrollMap.getVerticalScrollBar().setUnitIncrement(16);

        pWrapper.add(scrollMap, BorderLayout.CENTER);

        // Legend
        JPanel pLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pLegend.setOpaque(false);
        pLegend.add(createLegendItem(SEAT_AVAILABLE, "Standard"));
        pLegend.add(createLegendItem(new Color(255, 193, 7), "VIP"));
        pLegend.add(createLegendItem(Color.decode("#E91E63"), "Double/Couple"));
        pLegend.add(createLegendItem(SEAT_SELECTED, "Selected"));
        pLegend.add(createLegendItem(SEAT_BOOKED, "Booked"));
        pWrapper.add(pLegend, BorderLayout.SOUTH);

        p.add(pWrapper, BorderLayout.CENTER);

        // Save references for dynamic update
        pnlSeatMap.putClientProperty("rowLabels", pRowLabels);

        return p;
    }

    // ================= SEAT MAP LOGIC =================
    // --- Async Data Container ---
    private static class SeatMapData {
        List<String> bookedSeats;
        Map<String, Double> prices;
        Map<String, List<com.cinema.dao.GheDAO.SeatInfo>> rowMap;
        int maxCol;
    }

    private void loadSeatMap() {
        pnlSeatMap.removeAll();
        JPanel pRowLabels = (JPanel) pnlSeatMap.getClientProperty("rowLabels");
        if (pRowLabels != null)
            pRowLabels.removeAll();

        if (currentLichChieu == null)
            return;

        final int maLCM = currentLichChieu.getMaLichChieu();
        final String tenPhong = currentLichChieu.getTenPhong();

        // Async Load
        com.cinema.util.UIUtils.runAsync(this, () -> {
            SeatMapData data = new SeatMapData();

            // 1. Get Booked Seats
            VeDAO vDao = new VeDAO();
            data.bookedSeats = vDao.getGheDaBan(maLCM);

            // 2. Resolve Room ID
            com.cinema.dao.PhongChieuDAO pDao = new com.cinema.dao.PhongChieuDAO();
            int maPhong = -1;
            for (com.cinema.dto.PhongChieu pc : pDao.getAllPhong()) {
                if (pc.getTenPhong().equals(tenPhong)) {
                    maPhong = pc.getMaPhong();
                    break;
                }
            }

            if (maPhong == -1)
                return null; // Error

            // 3. Pricing & Seats
            data.prices = pDao.getRoomPricing(maPhong);
            com.cinema.dao.GheDAO gDao = new com.cinema.dao.GheDAO();
            List<com.cinema.dao.GheDAO.SeatInfo> seats = gDao.getSeatsByRoom(maPhong);

            // 4. Process Logic
            data.rowMap = new java.util.TreeMap<>();
            data.maxCol = 0;

            for (com.cinema.dao.GheDAO.SeatInfo s : seats) {
                String name = s.tenGhe;
                if (name == null || name.length() < 2)
                    continue;

                String rowChar = name.substring(0, 1);
                String colStr = name.substring(1);
                try {
                    int col = Integer.parseInt(colStr);
                    data.maxCol = Math.max(data.maxCol, col);
                } catch (Exception e) {
                }

                data.rowMap.computeIfAbsent(rowChar, k -> new ArrayList<>()).add(s);
            }
            if (data.maxCol == 0)
                data.maxCol = 10;

            return data;
        }, (data) -> {
            if (data == null)
                return;

            // Update State
            this.listGheDaBan = data.bookedSeats;
            this.roomPrices = data.prices;
            this.listGheDangChon.clear();
            this.seatTypeMap.clear();

            // Populate Seat Types
            if (data.rowMap != null) {
                for (java.util.List<com.cinema.dao.GheDAO.SeatInfo> list : data.rowMap.values()) {
                    for (com.cinema.dao.GheDAO.SeatInfo s : list) {
                        this.seatTypeMap.put(s.tenGhe, s.loaiGhe != null ? s.loaiGhe : "Thuong");
                    }
                }
            }

            // Render UI

            int rowsNeeded = data.rowMap.size();
            int gap = 10;
            pnlSeatMap.setLayout(new GridLayout(rowsNeeded, data.maxCol, gap, gap));
            if (pRowLabels != null)
                pRowLabels.setLayout(new GridLayout(rowsNeeded, 1, gap, gap));

            for (Map.Entry<String, List<com.cinema.dao.GheDAO.SeatInfo>> entry : data.rowMap.entrySet()) {
                String rowLabel = entry.getKey();
                List<com.cinema.dao.GheDAO.SeatInfo> rowSeats = entry.getValue();

                // Label
                JLabel lblRow = new JLabel(rowLabel, SwingConstants.RIGHT);
                lblRow.setFont(new Font("SansSerif", Font.BOLD, 14));
                lblRow.setForeground(Color.WHITE);
                if (pRowLabels != null)
                    pRowLabels.add(lblRow);

                // Seats
                rowSeats.sort((s1, s2) -> {
                    try {
                        int c1 = Integer.parseInt(s1.tenGhe.substring(1));
                        int c2 = Integer.parseInt(s2.tenGhe.substring(1));
                        return Integer.compare(c1, c2);
                    } catch (Exception e) {
                        return s1.tenGhe.compareTo(s2.tenGhe);
                    }
                });

                int currentPos = 1;
                for (com.cinema.dao.GheDAO.SeatInfo seat : rowSeats) {
                    try {
                        int col = Integer.parseInt(seat.tenGhe.substring(1));
                        // Add gaps
                        while (currentPos < col) {
                            pnlSeatMap.add(Box.createRigidArea(new Dimension(45, 45)));
                            currentPos++;
                        }

                        // Copy-paste creation logic
                        String seatName = seat.tenGhe;
                        SeatButton btn = new SeatButton(seatName);
                        btn.setPreferredSize(new Dimension(45, 45));

                        if (listGheDaBan.contains(seatName)) {
                            btn.setBackground(SEAT_BOOKED); // Red
                            btn.setForeground(Color.WHITE);
                        } else {
                            String type = seatTypeMap.getOrDefault(seatName, "Thuong");
                            if ("VIP".equalsIgnoreCase(type)) {
                                btn.setBackground(new Color(255, 193, 7)); // Gold
                                btn.setForeground(Color.BLACK);
                            } else if ("Double".equalsIgnoreCase(type) || "Doi".equalsIgnoreCase(type)) {
                                btn.setBackground(Color.decode("#E91E63")); // Pink
                                btn.setForeground(Color.WHITE);
                            } else {
                                btn.setBackground(SEAT_AVAILABLE);
                                btn.setForeground(TXT_SECONDARY);
                            }
                        }
                        btn.addActionListener(e -> toggleSeat(btn, seatName));
                        pnlSeatMap.add(btn);
                        currentPos++;

                    } catch (Exception e) {
                    }
                }

                // Fill remaining
                while (currentPos <= data.maxCol) {
                    pnlSeatMap.add(Box.createRigidArea(new Dimension(45, 45)));
                    currentPos++;
                }
            }

            pnlSeatMap.revalidate();
            pnlSeatMap.repaint();
            if (pRowLabels != null) {
                pRowLabels.revalidate();
                pRowLabels.repaint();
            }
        });
    }



    // Custom Seat Button Inner Class (Armchair Shape)
    private class SeatButton extends JButton {
        public SeatButton(String text) {
            super(text); // Pass text to super so getText() works
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setToolTipText(text);
            setFont(new Font("SansSerif", Font.PLAIN, 10)); // Font for the label
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Color bg = getBackground();

            // If selected, use Green
            if (listGheDangChon.contains(getText())) {
                bg = SEAT_SELECTED;
            } else if (listGheDaBan.contains(getText())) {
                bg = SEAT_BOOKED; // Red for Sold
            } else if (!isEnabled()) {
                bg = SEAT_BOOKED;
            }

            g2.setColor(bg);

            // Draw Armchair Shape
            // 1. Backrest (Top Rounded)
            int backH = (int) (h * 0.6);
            int backW = (int) (w * 0.8);
            int backX = (w - backW) / 2;
            g2.fillRoundRect(backX, 2, backW, backH, 10, 10);

            // 2. Seat / Arms (Bottom wrapper)
            int armW = (int) (w * 0.2);
            int baseH = h - backH + 4; // slight overlap
            int baseY = backH - 4;

            // Left Arm
            g2.fillRoundRect(0, baseY + 6, armW, baseH - 6, 8, 8);
            // Right Arm
            g2.fillRoundRect(w - armW, baseY + 6, armW, baseH - 6, 8, 8);

            // Seat Cushion (Center)
            g2.fillRoundRect(armW - 2, baseY, w - (2 * armW) + 4, baseH, 6, 6);

            // Draw Text
            g2.setColor(Color.WHITE);
            if (bg.equals(SEAT_SELECTED))
                g2.setColor(Color.BLACK); // Better contrast on green

            String s = getText();
            FontMetrics fm = g2.getFontMetrics();
            // Center text on Backrest
            int tx = (w - fm.stringWidth(s)) / 2;
            int ty = backH / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(s, tx, ty);
        }
    }

    // ================= RIGHT PANEL =================
    private JPanel createRightPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(30, 20, 22)); // Slightly darker checkout bg
        p.setBorder(new EmptyBorder(25, 25, 25, 25));

        // 1. CHECKOUT HEADER
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setOpaque(false);
        pHeader.setMaximumSize(new Dimension(500, 30));

        JLabel lblCheck = new JLabel("CHECKOUT");
        lblCheck.setForeground(Color.WHITE);
        lblCheck.setFont(new Font("SansSerif", Font.BOLD, 18));



        lblOrder = new JLabel(currentOrderCode);
        lblOrder.setForeground(ACCENT_RED);
        lblOrder.setFont(new Font("SansSerif", Font.BOLD, 12));

        pHeader.add(lblCheck, BorderLayout.WEST);
        pHeader.add(lblOrder, BorderLayout.EAST);
        p.add(pHeader);
        p.add(Box.createVerticalStrut(20));

        // 2. CART ITEMS (Full width)
        pnlCartItems = new JPanel();
        pnlCartItems.setLayout(new BoxLayout(pnlCartItems, BoxLayout.Y_AXIS));
        pnlCartItems.setBackground(p.getBackground());
        p.add(pnlCartItems);
        p.add(Box.createVerticalStrut(20));

        // 2b. SNACKS QUICK ADD
        // 2b. SNACKS QUICK ADD
        JPanel pSnacks = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Use Flow for multiple rows
        pSnacks.setOpaque(false);
        pSnacks.setMaximumSize(new Dimension(500, 150)); // Allow more height

        // Load & Generate Buttons
        List<SanPham> productList = spDAO.getAllSanPham();
        dynamicProductCache.clear();

        for (SanPham sp : productList) {
            // Only add Snacks/Drinks/Combos to this quick panel (Exclude Tickets if any)
            String type = sp.getLoaiSP() != null ? sp.getLoaiSP().toLowerCase() : "";
            // Assuming "ticket" or "ve" logic handled elsewhere.
            // Add to cache
            dynamicProductCache.put(sp.getTenSP(), sp);

            // Icon logic
            String icon = "📦";
            if (type.contains("bap") || type.contains("popcorn"))
                icon = "🍿";
            else if (type.contains("nuoc") || type.contains("drink") || type.contains("coca"))
                icon = "🥤";
            else if (type.contains("combo"))
                icon = "🍱";
            else if (type.contains("keo"))
                icon = "🍬";

            JButton btnProd = createQuickAddBtn(sp.getTenSP(), sp.getTenSP(), sp.getGiaBan(), icon);
            pSnacks.add(btnProd);
        }

        p.add(pSnacks);
        p.add(Box.createVerticalStrut(20));

        // 3. DISCOUNT & PROMOTIONS CARD
        JPanel pPromoCard = createSectionCard("DISCOUNTS & PROMOTIONS");

        // Demographic
        JLabel lblDemo = new JLabel("DEMOGRAPHIC");
        lblDemo.setForeground(new Color(150, 150, 150));
        lblDemo.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblDemo.setAlignmentX(Component.LEFT_ALIGNMENT);

        cboPolicies = new JComboBox<>();
        cboPolicies.setBackground(new Color(50, 40, 40));
        cboPolicies.setForeground(Color.WHITE);
        cboPolicies.addItem(new DiscountPolicy(0, "No Discount (0%)", "PERCENT", 0, true));
        for (DiscountPolicy dp : discountDAO.getAllPolicies()) {
            if (dp.isActive())
                cboPolicies.addItem(dp);
        }
        cboPolicies.addActionListener(e -> {
            selectedPolicy = (DiscountPolicy) cboPolicies.getSelectedItem();
            updateCart();
        });
        cboPolicies.setMaximumSize(new Dimension(500, 35));

        // Promo Badge (If Active)
        if (todaysPromo != null && todaysPromo.isActive()) {
            JPanel pBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pBadge.setOpaque(false);
            JLabel lblBadge = new JLabel(" ● " + todaysPromo.getName().toUpperCase());
            lblBadge.setForeground(new Color(100, 255, 100));
            lblBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
            lblBadge.setBorder(new LineBorder(new Color(40, 100, 40), 1, true));
            // pPromoCard.add(lblBadge); // Add logic to header ?? For now add to body
        }

        // Promo Info Label (Always added, content updated dynamically)
        lblPromoInfo = new JLabel("");
        lblPromoInfo.setForeground(new Color(100, 255, 100));
        lblPromoInfo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPromoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        pPromoCard.add(lblPromoInfo);
        pPromoCard.add(Box.createVerticalStrut(10));

        // Initial update
        updatePromoLabel();

        pPromoCard.add(lblDemo);
        pPromoCard.add(Box.createVerticalStrut(5));
        pPromoCard.add(cboPolicies);

        p.add(pPromoCard);
        p.add(Box.createVerticalStrut(15));

        // 4. LOYALTY CARD
        JPanel pLoyaltyCard = createSectionCard("<html><font color='#E53935'>★</font> LOYALTY POINTS</html>");

        // Member Search Row
        JLabel lblMem = new JLabel("PHONE NUMBER");
        lblMem.setForeground(new Color(150, 150, 150));
        lblMem.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblMem.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pSearchRow = new JPanel(new BorderLayout(5, 0));
        pSearchRow.setOpaque(false);
        pSearchRow.setMaximumSize(new Dimension(500, 35));
        pSearchRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtMemberSearch = new JTextField("e.g. 555-0123");
        txtMemberSearch.setBackground(new Color(60, 45, 45));
        txtMemberSearch.setForeground(Color.LIGHT_GRAY);
        txtMemberSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(80, 60, 60), 1),
                new EmptyBorder(5, 10, 5, 10)));

        JButton btnFind = new JButton("Find");
        btnFind.setBackground(new Color(80, 50, 50));
        btnFind.setForeground(Color.WHITE);
        btnFind.setFocusPainted(false);
        btnFind.addActionListener(e -> searchMemberLogic());

        // New Button (Small +)
        JButton btnNew = new JButton("+");
        btnNew.setBackground(new Color(46, 125, 50));
        btnNew.setForeground(Color.WHITE);
        btnNew.addActionListener(e -> {
            AddMemberDialog dlg = new AddMemberDialog(SwingUtilities.getWindowAncestor(this), "");
            dlg.setVisible(true);
            KhachHang newM = dlg.getNewMember();
            if (newM != null) {
                currentMember = newM;
                txtMemberSearch.setText(newM.getSdt());
                refreshLoyaltyUI();
                updateCart();
            }
        });

        JPanel pBtns = new JPanel(new GridLayout(1, 2, 5, 0));
        pBtns.setOpaque(false);
        pBtns.add(btnFind);
        pBtns.add(btnNew);

        pSearchRow.add(txtMemberSearch, BorderLayout.CENTER);
        pSearchRow.add(pBtns, BorderLayout.EAST);

        // Member Info Label (Result)
        lblMemberInfo = new JLabel("Guest");
        lblMemberInfo.setForeground(Color.LIGHT_GRAY);
        lblMemberInfo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblMemberInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Inner Card (Points Display & Input)
        JPanel pInner = new JPanel();
        pInner.setLayout(new BoxLayout(pInner, BoxLayout.Y_AXIS));
        pInner.setBackground(new Color(35, 25, 27));
        pInner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(80, 50, 50), 1, true),
                new EmptyBorder(10, 10, 10, 10)));
        pInner.setAlignmentX(Component.LEFT_ALIGNMENT);
        pInner.setMaximumSize(new Dimension(500, 100));

        // Info Row (Points | Value)
        JPanel pInfo = new JPanel(new BorderLayout());
        pInfo.setOpaque(false);
        pInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left: AVAILABLE POINTS
        JPanel pInfoLeft = new JPanel(new GridLayout(2, 1));
        pInfoLeft.setOpaque(false);
        JLabel lblL1 = new JLabel("AVAILABLE POINTS");
        lblL1.setForeground(new Color(180, 140, 140)); // Rose-grey
        lblL1.setFont(new Font("SansSerif", Font.BOLD, 9));

        lblPointsAvail = new JLabel("0 pts");
        lblPointsAvail.setForeground(Color.WHITE);
        lblPointsAvail.setFont(new Font("SansSerif", Font.BOLD, 16));

        pInfoLeft.add(lblL1);
        pInfoLeft.add(lblPointsAvail);

        // Right: VALUE
        JPanel pInfoRight = new JPanel(new GridLayout(2, 1));
        pInfoRight.setOpaque(false);
        JLabel lblL2 = new JLabel("VALUE");
        lblL2.setForeground(new Color(180, 140, 140));
        lblL2.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblL2.setHorizontalAlignment(SwingConstants.RIGHT);

        lblMoneyVal = new JLabel("0 VNĐ"); // Or VNĐ formatted
        lblMoneyVal.setForeground(new Color(50, 220, 100));
        lblMoneyVal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMoneyVal.setHorizontalAlignment(SwingConstants.RIGHT);

        pInfoRight.add(lblL2);
        pInfoRight.add(lblMoneyVal);

        pInfo.add(pInfoLeft, BorderLayout.WEST);
        pInfo.add(pInfoRight, BorderLayout.EAST);

        // Input Row (Points to Redeem | Apply)
        JPanel pInputRow = new JPanel(new BorderLayout(5, 0));
        pInputRow.setOpaque(false);
        pInputRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        txtRedeem = new JTextField("Points to Redeem");
        txtRedeem.setBackground(new Color(60, 45, 45));
        txtRedeem.setForeground(Color.GRAY);
        txtRedeem.setBorder(new EmptyBorder(5, 10, 5, 10));
        txtRedeem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtRedeem.getText().equals("Points to Redeem")) {
                    txtRedeem.setText("");
                    txtRedeem.setForeground(Color.WHITE);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtRedeem.getText().isEmpty()) {
                    txtRedeem.setText("Points to Redeem");
                    txtRedeem.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnApply = new JButton("Apply");
        btnApply.setBackground(new Color(160, 30, 30)); // Dark Red
        btnApply.setForeground(Color.WHITE);
        btnApply.setFocusPainted(false);
        btnApply.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnApply.addActionListener(e -> {
            try {
                if (currentMember == null)
                    return;
                String val = txtRedeem.getText().replace("Points to Redeem", "").trim();
                if (val.isEmpty())
                    return;
                int pts = Integer.parseInt(val);
                if (pts > currentMember.getDiemTichLuy()) {
                    JOptionPane.showMessageDialog(this, "Not enough points!");
                    return;
                }
                pointsToRedeem = pts;
                updateCart();
            } catch (Exception ex) {
                pointsToRedeem = 0;
            }
        });

        pInputRow.add(txtRedeem, BorderLayout.CENTER);
        pInputRow.add(btnApply, BorderLayout.EAST);

        pInner.add(pInfo);
        pInner.add(pInputRow);

        pLoyaltyCard.add(lblMem);
        pLoyaltyCard.add(Box.createVerticalStrut(5));
        pLoyaltyCard.add(pSearchRow);
        pLoyaltyCard.add(Box.createVerticalStrut(5));
        pLoyaltyCard.add(lblMemberInfo); // Added here
        pLoyaltyCard.add(Box.createVerticalStrut(10));
        pLoyaltyCard.add(pInner);

        p.add(pLoyaltyCard);
        p.add(Box.createVerticalStrut(15));

        // 4.5 DETAILED DISCOUNT LIST
        pDiscountList = new JPanel();
        pDiscountList.setLayout(new BoxLayout(pDiscountList, BoxLayout.Y_AXIS));
        pDiscountList.setOpaque(false);
        p.add(pDiscountList);
        p.add(Box.createVerticalStrut(10));

        // 5. TOTALS SECTION
        JPanel pTotals = new JPanel(new GridLayout(0, 2));
        pTotals.setOpaque(false);
        pTotals.setMaximumSize(new Dimension(500, 80));

        lblSubtotal = new JLabel("Subtotal: 0");
        lblSubtotal.setForeground(TXT_SECONDARY);
        lblDiscount = new JLabel("Discount: 0"); // This will now serve as "Discounts Applied" header
        lblDiscount.setForeground(ACCENT_RED);
        lblTax = new JLabel("Tax (5%): 0");
        lblTax.setForeground(TXT_SECONDARY);

        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTax.setHorizontalAlignment(SwingConstants.RIGHT);

        pTotals.add(new JLabel(""));
        pTotals.add(lblSubtotal);
        pTotals.add(new JLabel(""));
        pTotals.add(lblDiscount);
        pTotals.add(new JLabel(""));
        pTotals.add(lblTax);

        p.add(pTotals);
        p.add(Box.createVerticalStrut(10));
        p.add(new JSeparator(SwingConstants.HORIZONTAL));
        p.add(Box.createVerticalStrut(10));

        JPanel pFinalParams = new JPanel(new BorderLayout());
        pFinalParams.setOpaque(false);
        pFinalParams.setMaximumSize(new Dimension(500, 40));

        lblOriginalPrice = new JLabel("Original: 0");
        lblOriginalPrice.setForeground(Color.GRAY);


        lblTotal = new JLabel("0 VNĐ");
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 24));

        pFinalParams.add(lblOriginalPrice, BorderLayout.WEST); // Placeholder
        pFinalParams.add(lblTotal, BorderLayout.EAST);

        p.add(pFinalParams);
        p.add(Box.createVerticalStrut(20));

        // 6. ACTION BUTTONS
        JPanel pActions = new JPanel(new BorderLayout(10, 0));
        pActions.setOpaque(false);
        pActions.setMaximumSize(new Dimension(500, 50));

        JButton btnTrash = new JButton("🗑️");
        btnTrash.setBackground(new Color(60, 40, 40));
        btnTrash.setForeground(Color.WHITE);
        btnTrash.setPreferredSize(new Dimension(50, 50));
        btnTrash.setFocusPainted(false);
        btnTrash.addActionListener(e -> {
            resetCart();
            loadSeatMap();
        });

        JButton btnPrint = new JButton("Payment Confirmation"); // Keep text or change to "Print Ticket"
        btnPrint.setBackground(ACCENT_RED);
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnPrint.setFocusPainted(false);
        btnPrint.addActionListener(e -> processPayment());

        pActions.add(btnTrash, BorderLayout.WEST);
        pActions.add(btnPrint, BorderLayout.CENTER);

        p.add(pActions);
        p.add(Box.createVerticalGlue());

        return p;
    }

    // Helper to create the titled card containers
    private JPanel createSectionCard(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(45, 35, 37)); // Card BG
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 50, 50), 1),
                new EmptyBorder(15, 15, 15, 15)));
        p.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Row
        JPanel pTitle = new JPanel(new BorderLayout());
        pTitle.setOpaque(false);
        pTitle.setMaximumSize(new Dimension(500, 20));
        pTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(title);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        // Add icon?

        pTitle.add(l, BorderLayout.WEST);

        p.add(pTitle);
        p.add(Box.createVerticalStrut(15));

        return p;
    }

    private void searchMemberLogic() {
        String keyword = txtMemberSearch.getText();
        java.util.List<KhachHang> list = khachHangDAO.searchKhachHang(keyword);

        if (!list.isEmpty()) {
            // Pick first or best match
            currentMember = list.get(0);
            // If multiple, try to find exact phone match
            for (KhachHang k : list) {
                if (k.getSdt().equals(keyword)) {
                    currentMember = k;
                    break;
                }
            }
            txtMemberSearch.setText(currentMember.getSdt());

            // Show Name & Tier
            String info = "<html><font color='white'><b>" + currentMember.getHoTen() + "</b></font>";
            if (currentMember.getHangThanhVien() != null) {
                String tier = currentMember.getHangThanhVien();
                String color = "#A1887F"; // Bronze
                if ("Gold".equalsIgnoreCase(tier))
                    color = "#FFD700";
                if ("Platinum".equalsIgnoreCase(tier))
                    color = "#E5E4E2"; // Platinum/Silver-ish
                info += " <small><font color='" + color + "'>[" + tier + "]</font></small>";
            }
            info += "</html>";
            lblMemberInfo.setText(info);

            refreshLoyaltyUI();
        } else {
            lblPointsAvail.setText("0 pts");
            lblMoneyVal.setText("0 VNĐ");
            currentMember = null;
            JOptionPane.showMessageDialog(this, "Member not found.");
        }
    }

    private void refreshLoyaltyUI() {
        if (currentMember == null)
            return;

        lblPointsAvail.setText(currentMember.getDiemTichLuy() + " pts");

        double valPerPoint = 1000;
        try {
            valPerPoint = Double.parseDouble(loyaltyDAO.getRules().getOrDefault("POINT_VALUE_VND", "1000"));
        } catch (Exception e) {
        }

        double money = currentMember.getDiemTichLuy() * valPerPoint;
        lblMoneyVal.setText(String.format("%,.0f VNĐ", money));
    }

    private void toggleSeat(JButton btn, String seatName) {
        // 1. CHECK REFUND (If seat is SOLD)
        if (listGheDaBan.contains(seatName)) {
            // Permission check can be added here
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Seat " + seatName + " is SOLD. Do you want to process a REFUND?",
                    "Refund Ticket", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = veDAO.refundTicket(currentLichChieu.getMaLichChieu(), seatName);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Refund Successful! Ticket is now VOID.");
                    loadSeatMap(); // Refresh Map
                    // Optional: Recalculate revenue or log action
                } else {
                    JOptionPane.showMessageDialog(this, "Refund Failed. Check database/logs.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            return; // EXIT, do not process selection
        }

        if (listGheDangChon.contains(seatName)) {
            listGheDangChon.remove(seatName);
            // Revert to Available (Restore Correct Color)
            String type = seatTypeMap.getOrDefault(seatName, "Thuong");
            if ("VIP".equalsIgnoreCase(type)) {
                btn.setBackground(new Color(255, 193, 7)); // Amber
                btn.setForeground(Color.BLACK);
            } else if ("Double".equalsIgnoreCase(type) || "Doi".equalsIgnoreCase(type)) {
                btn.setBackground(Color.decode("#E91E63")); // Pink
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(SEAT_AVAILABLE);
                btn.setForeground(TXT_SECONDARY);
            }
            btn.repaint();
        } else {
            listGheDangChon.add(seatName);
            // Set to Selected (Green)
            btn.setBackground(SEAT_SELECTED);
            btn.repaint();
        }
        updateCart();
    }

    private void addSnack(String name, double price) {
        currentSnackList.put(name, currentSnackList.getOrDefault(name, 0) + 1);
        updateCart();
    }

    private void resetCart() {
        listGheDangChon.clear();
        currentSnackList.clear();
        generateNewOrderCode();
        updateCart();
    }

    private void updateCart() {
        // Refresh Promo Label first
        updatePromoLabel();

        pnlCartItems.removeAll();
        pDiscountList.removeAll();
        currentDiscountDetails.clear(); // Clear old details

        double baseTicketSub = 0;
        double promoDiscountTotal = 0;
        double snackSub = 0;

        // 1. Tickets
        if (currentLichChieu != null) {
            double basePrice = currentLichChieu.getGiaVe();
            String promoName = "";

            // Calculate Promo Logic per unit or overall?
            // Usually Promo applies to Base Price OR Total.
            // Let's calculate Real Price per seat (Base + Surcharge) then apply Promo?
            // Standard: Discount applies to TICKET PRICE.

            for (String seat : listGheDangChon) {
                String type = seatTypeMap.getOrDefault(seat, "Thuong");

                // Determine Pricing Key
                String priceKey = "Standard";
                if ("VIP".equalsIgnoreCase(type))
                    priceKey = "VIP";
                else if ("Double".equalsIgnoreCase(type) || "Doi".equalsIgnoreCase(type))
                    priceKey = "Double";

                // Fetch Price (Priority: Room Config -> Schedule Base -> Hardcoded Fallback)
                double seatPrice = 0.0;
                if (roomPrices.containsKey(priceKey) && roomPrices.get(priceKey) > 0) {
                    seatPrice = roomPrices.get(priceKey);
                } else {
                    // Fallback using basePrice
                    seatPrice = basePrice;
                    if ("VIP".equalsIgnoreCase(type))
                        seatPrice += 20000;
                }

                // Safety Fallback: If still 0 (e.g. Schedule has no price), force default
                if (seatPrice == 0) {
                    seatPrice = 45000; // Default Standard
                    if ("VIP".equalsIgnoreCase(type))
                        seatPrice += 20000; // Add VIP diff
                    if ("Double".equalsIgnoreCase(type))
                        seatPrice = 90000;
                }

                double finalSeatPrice = seatPrice;

                // Apply Weekly Promo
                if (todaysPromo != null && todaysPromo.isActive()) {
                    if (todaysPromo.isPercent()) {
                        finalSeatPrice = seatPrice * (1.0 - (todaysPromo.getDiscountValue() / 100.0));
                    } else {
                        finalSeatPrice = Math.max(0, seatPrice - todaysPromo.getDiscountValue());
                    }
                    promoName = todaysPromo.getName();
                }

                String label = "Seat " + seat + (type.equalsIgnoreCase("VIP") ? " (VIP)" : "");
                // Show original price if discounted
                if (finalSeatPrice < seatPrice) {
                    int percent = (int) Math.round((1 - finalSeatPrice / seatPrice) * 100);
                    label = String.format(
                            "<html>%s <font color='#999'><s>%,.0f</s></font> <font color='#4CAF50'>-%d%%</font></html>",
                            label, seatPrice, percent);
                }
                pnlCartItems.add(createCartItem(label, finalSeatPrice, "ticket"));

                baseTicketSub += seatPrice; // Add undiscounted price to subtotal base?
                // Wait, subtotal usually means list price.
                // Let's track: List Price vs Pay Price.

                promoDiscountTotal += (seatPrice - finalSeatPrice);
            }

            if (promoDiscountTotal > 0 && !listGheDangChon.isEmpty()) {
                addDiscountRow(promoName, promoDiscountTotal);
            }
        }

        double currentTicketSub = baseTicketSub - promoDiscountTotal;

        // Apply Policy Discount (on Ticket Subtotal)
        double policyDisc = 0;
        if (selectedPolicy != null && selectedPolicy.getId() != 0) {
            if ("PERCENT".equalsIgnoreCase(selectedPolicy.getType())) {
                policyDisc = currentTicketSub * (selectedPolicy.getValue() / 100.0);
            } else {
                policyDisc = selectedPolicy.getValue();
            }
            if (policyDisc > 0) {
                addDiscountRow(selectedPolicy.getName(), policyDisc);
            }
        }

        // Apply Tier Discount (Auto-detected)
        double tierDisc = 0;
        if (currentMember != null) {
            String tier = currentMember.getHangThanhVien();
            if (tier != null) {
                if ("Gold".equalsIgnoreCase(tier)) {
                    tierDisc = currentTicketSub * 0.05;
                    addDiscountRow("Tier Benefit: Gold Member (5%)", tierDisc);
                } else if ("Platinum".equalsIgnoreCase(tier)) {
                    tierDisc = currentTicketSub * 0.10;
                    addDiscountRow("Tier Benefit: Platinum Member (10%)", tierDisc);
                }
            }
        }

        // 2. Snacks
        for (Map.Entry<String, Integer> entry : currentSnackList.entrySet()) {
            double price = 0;
            String key = entry.getKey();

            // Dynamic Lookup
            String type = "coca"; // default icon style
            if (dynamicProductCache.containsKey(key)) {
                SanPham sp = dynamicProductCache.get(key);
                price = sp.getGiaBan();

                // Icon Type Logic
                String loai = sp.getLoaiSP() != null ? sp.getLoaiSP().toLowerCase() : "";
                if (loai.contains("bap") || loai.contains("popcorn"))
                    type = "bap";
                else if (loai.contains("combo"))
                    type = "bap"; // Use popcorn icon for combo or add new
            } else {
                // Legacy Fallback
                if (key.equalsIgnoreCase("Bap") || key.equalsIgnoreCase("Popcorn")) {
                    price = 100000;
                    type = "bap";
                } else if (key.equalsIgnoreCase("CoCa CoLA") || key.toLowerCase().contains("coca")) {
                    price = 50000;
                    type = "coca";
                }
            }

            double totalItem = price * entry.getValue();
            pnlCartItems.add(createCartItem(key + " x" + entry.getValue(), totalItem, type));
            snackSub += totalItem;
        }

        // 3. Loyalty Redemption
        double loyaltyDisc = 0;
        if (pointsToRedeem > 0) {
            Map<String, String> rules = loyaltyDAO.getRules();
            double valPerPoint = 1000; // Default
            try {
                valPerPoint = Double.parseDouble(rules.getOrDefault("POINT_VALUE_VND", "1000"));
            } catch (Exception e) {
            }
            loyaltyDisc = pointsToRedeem * valPerPoint;
            addDiscountRow("Points Redemption (" + pointsToRedeem + " pts)", loyaltyDisc);
        }

        pnlCartItems.revalidate();
        pnlCartItems.repaint();
        pDiscountList.revalidate();
        pDiscountList.repaint();

        // Totals Calculation
        double subTotal_ListPrice = baseTicketSub + snackSub;
        double totalDiscount = promoDiscountTotal + policyDisc + loyaltyDisc + tierDisc;

        // Cap discount
        if (totalDiscount > subTotal_ListPrice)
            totalDiscount = subTotal_ListPrice;

        double taxableAmount = subTotal_ListPrice - totalDiscount;
        double tax = taxableAmount * 0.05;
        double finalTotal = taxableAmount + tax;

        // Cache for Payment
        currentCartSubtotal = subTotal_ListPrice;
        currentCartDiscount = totalDiscount;
        currentCartTax = tax;
        currentCartTotal = finalTotal;

        lblSubtotal.setText("Subtotal: " + String.format("%,.0f", subTotal_ListPrice));
        lblDiscount.setText("Applied: -" + String.format("%,.0f", totalDiscount));
        lblTax.setText("Tax (5%): " + String.format("%,.0f", tax));
        lblTotal.setText("Total: " + String.format("%,.0f VNĐ", finalTotal));

        if (totalDiscount > 0) {
            lblOriginalPrice
                    .setText("<html><strike>" + String.format("%,.0f", subTotal_ListPrice + tax) + "</strike></html>");
            lblOriginalPrice.setVisible(true);
        } else {
            lblOriginalPrice.setText("");
            lblOriginalPrice.setVisible(false);
        }
    }

    private void addDiscountRow(String name, double amount) {
        JPanel pRow = new JPanel(new BorderLayout());
        pRow.setOpaque(false);
        pRow.setMaximumSize(new Dimension(500, 20));

        JLabel lblName = new JLabel(name);
        lblName.setForeground(new Color(180, 180, 180));
        lblName.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel lblAmt = new JLabel("-" + String.format("%,.0f", amount));
        lblAmt.setForeground(new Color(255, 100, 100)); // Reddish
        lblAmt.setFont(new Font("SansSerif", Font.PLAIN, 12));

        pRow.add(lblName, BorderLayout.WEST);
        pRow.add(lblAmt, BorderLayout.EAST);

        pDiscountList.add(pRow);
        pDiscountList.add(Box.createVerticalStrut(5));

        // Capture for E-Ticket
        currentDiscountDetails.add(name + "|" + amount);
    }

    private void processPayment() {
        if (listGheDangChon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least 1 seat!");
            return;
        }

        // 1. Use Cached Values (What You See Is What You Pay)
        double total = currentCartTotal;
        double subTotal = currentCartSubtotal;
        double discount = currentCartDiscount;
        double calculatedTax = currentCartTax;

        // 2. Create Invoice
        HoaDonDichVu hd = new HoaDonDichVu();
        hd.setMaNV(nhanVien != null ? nhanVien.getMaNV() : 1);
        int custId = (currentMember != null) ? currentMember.getMaKH() : new KhachHangDAO().ensureGuestCustomer();
        hd.setMaKH(custId);
        hd.setNgayLap(new java.sql.Date(System.currentTimeMillis()));
        hd.setTongTien(total);
        hd.setOrderCode(currentOrderCode);

        HoaDonDichVuDAO hdDAO = new HoaDonDichVuDAO();
        int maHD = hdDAO.insertHoaDon(hd);

        if (maHD == -1) {
            JOptionPane.showMessageDialog(this, "Error creating invoice! Check DB connection.");
            return;
        }

        // 3. Save Tickets & Invoice Details
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
        int maSP_Ve = -1;
        // Lookup ticket product ID dynamically
        for (SanPham sp : spDAO.getAllSanPham()) {
            String name = sp.getTenSP().toLowerCase();
            if (name.contains("vé") || name.contains("ticket") || name.contains("ve xem phim")) {
                maSP_Ve = sp.getMaSP();
                break;
            }
        }

        double basePrice = currentLichChieu.getGiaVe();

        // Map for E-Ticket (Must be reconstructed for Dialog)
        Map<String, Double> seatPricesMap = new LinkedHashMap<>();

        for (String seat : listGheDangChon) {
            String type = seatTypeMap.getOrDefault(seat, "Thuong");

            // Determine Pricing Key (Same as updateCart)
            String priceKey = "Standard";
            if ("VIP".equalsIgnoreCase(type))
                priceKey = "VIP";
            else if ("Double".equalsIgnoreCase(type) || "Doi".equalsIgnoreCase(type))
                priceKey = "Double";

            // Fetch Price (Priority: Room Config -> Schedule Base -> Hardcoded Fallback)
            double baseP = 0.0;
            if (roomPrices.containsKey(priceKey) && roomPrices.get(priceKey) > 0) {
                baseP = roomPrices.get(priceKey);
            } else {
                baseP = basePrice;
                if ("VIP".equalsIgnoreCase(type))
                    baseP += 20000;
            }

            // Safety Fallback for Payment Processing
            if (baseP == 0) {
                baseP = 45000;
                if ("VIP".equalsIgnoreCase(type))
                    baseP += 20000;
                if ("Double".equalsIgnoreCase(type))
                    baseP = 90000;
            }

            // Logic for DB (Actual Payment)
            double p = baseP;
            // Apply Promo
            if (todaysPromo != null && todaysPromo.isActive()) {
                if (todaysPromo.isPercent()) {
                    p = p * (1.0 - (todaysPromo.getDiscountValue() / 100.0));
                } else {
                    p = Math.max(0, p - todaysPromo.getDiscountValue());
                }
            }
            double effectivePrice = p;

            // Update Map for E-Ticket: Use BASE PRICE (Pre-discount) to avoid "0"
            // The discount is listed separately, so we show full price here.
            seatPricesMap.put(seat, baseP);

            // Save to Ve Table (MUST use effectivePrice)
            veDAO.luuVe(currentLichChieu.getMaLichChieu(), seat,
                    (nhanVien != null ? nhanVien.getMaNV() : 1),
                    effectivePrice,
                    maHD);

            // Save to ChiTietHoaDon
            if (maSP_Ve != -1) {
                ChiTietHoaDon cthd = new ChiTietHoaDon(maHD, maSP_Ve, 1, effectivePrice, "Vé Ghế: " + seat);
                cthdDAO.insertChiTiet(cthd);
            }
        }

        // Save Snacks with Stock Deduction
        HoaDonDichVuDAO hddvDAO = new HoaDonDichVuDAO();
        // New Map for E-Ticket Snack Prices
        Map<String, Double> snackPricesMap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : currentSnackList.entrySet()) {
            String name = entry.getKey();
            int qty = entry.getValue();

            if (dynamicProductCache.containsKey(name)) {
                SanPham sp = dynamicProductCache.get(name);
                double lineTotal = sp.getGiaBan() * qty; // Basic Price, no discount logic for snacks yet?

                // Track for E-Ticket
                snackPricesMap.put(name, lineTotal);

                // Note: Snack discount logic is not implemented in calculateTotal loop either
                // (except general footer discount).
                // So lineTotal is base price.

                hddvDAO.addChiTietHoaDon(maHD, sp.getMaSP(), qty, lineTotal);
            }
        }

        // 4. Loyalty Processing
        if (currentMember != null) {
            // Deduct Points
            if (pointsToRedeem > 0) {
                boolean success = loyaltyDAO.redeemPoints(currentMember.getMaKH(), pointsToRedeem,
                        "Redemption for Order #" + maHD);
                if (!success) {
                    System.err.println("Failed to redeem points for member " + currentMember.getMaKH());
                }
            }

            // Accumulate Points
            if (total > 0) {
                loyaltyDAO.addPoints(currentMember.getMaKH(), 0, total, "Points earned from Order #" + maHD);
            }
        }

        // 5. SUCCESS DIALOG & E-TICKET

        String custName = (currentMember != null) ? currentMember.getHoTen() : "Guest";

        // Capture final list for E-Ticket (since resetCart clears it)
        LichChieu finalShow = currentLichChieu;

        Object[] options = { "Print Invoice", "View E-Ticket", "New Order" };
        int response = JOptionPane.showOptionDialog(this,
                "Payment Successful!\nTotal: " + String.format("%,.0f", total) + " VNĐ",
                "Transaction Complete",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[2]);

        if (response == 0) {
            printInvoice(maHD, total, discount, calculatedTax, subTotal, custName, currentOrderCode);
        } else if (response == 1) {
            // Updated Constructor to pass seatDetails and snackPrices
            ETicketDialog dlg = new ETicketDialog(SwingUtilities.getWindowAncestor(this), finalShow,
                    seatPricesMap, // Pre-discount prices
                    new HashMap<>(currentSnackList),
                    snackPricesMap, // New Argument
                    new ArrayList<>(currentDiscountDetails), total, maHD, custName,
                    currentOrderCode);
            dlg.setVisible(true);
        }

        // Reset Session State FIRST
        currentMember = null;
        selectedPolicy = null;
        pointsToRedeem = 0;
        txtMemberSearch.setText("Phone No...");
        lblMemberInfo.setText("Guest");
        lblMemberInfo.setForeground(Color.LIGHT_GRAY);
        txtRedeem.setText("Points to Redeem"); // Reset to placeholder
        txtRedeem.setForeground(Color.GRAY);

        // Reset Loyalty UI Labels
        if (lblPointsAvail != null)
            lblPointsAvail.setText("0 pts");
        if (lblMoneyVal != null)
            lblMoneyVal.setText("0 VNĐ");

        // Then Reset Cart (Calculations will now use empty state)
        resetCart();
        loadSeatMap();
    }

    // --- COMPONENT FACTORIES ---

    private JPanel createCartItem(String name, double price, String type) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setMaximumSize(new Dimension(300, 35)); // Slightly taller
        p.setBorder(new EmptyBorder(5, 5, 5, 5)); // More padding

        // Icon based on type
        String icon = "🎫";
        if (type.equals("bap"))
            icon = "🍿";
        if (type.equals("coca"))
            icon = "🥤";

        // Use a container for Left side to separate Icon (Emoji font) and Name
        // (SansSerif)
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setBackground(BG_PANEL);

        JLabel lIcon = new JLabel(icon);
        lIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); // Force Emoji Font
        lIcon.setForeground(TXT_PRIMARY);

        JLabel lName = new JLabel(name);
        lName.setForeground(TXT_PRIMARY);
        lName.setFont(new Font("SansSerif", Font.PLAIN, 13));

        left.add(lIcon);
        left.add(lName);

        JLabel lPrice = new JLabel(String.format("%,.0f", price));
        lPrice.setForeground(TXT_SECONDARY); // Secondary color for price to differentiate
        lPrice.setFont(new Font("SansSerif", Font.PLAIN, 13));

        p.add(left, BorderLayout.WEST);
        p.add(lPrice, BorderLayout.EAST);
        return p;
    }

    private JPanel createLegendItem(Color c, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(BG_MAIN);
        JPanel icon = new JPanel();
        icon.setBackground(c);
        icon.setPreferredSize(new Dimension(15, 15));
        // Rounded border for legend icon

        JLabel l = new JLabel(text);
        l.setForeground(TXT_SECONDARY);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));

        p.add(icon);
        p.add(l);
        return p;
    }

    private JButton createQuickAddBtn(String dbName, String label, double price, String emoji) {
        JButton btn = new JButton(
                "<html><center><font face='Segoe UI Emoji' size='5'>" + emoji + "</font> " + label + "<br><small>"
                        + String.format("%,.0f", price / 1000)
                        + "k</small></center></html>");
        btn.setBackground(new Color(39, 39, 42)); // Zinc-800
        btn.setForeground(TXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BORDER_COLOR, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> addSnack(dbName, price));
        return btn;
    }




    // --- PRINTING LOGIC ---
    private void printInvoice(int maHD, double total, double discount, double tax, double subTotal,
            String customerName, String orderCode) {
        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
        job.setJobName("Cinema Ticket Invoice #" + maHD);

        job.setPrintable(new java.awt.print.Printable() {
            public int print(Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0)
                    return NO_SUCH_PAGE;

                Graphics2D g2 = (Graphics2D) graphics;
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                int y = 20;
                int x = 20;

                // Header
                g2.setFont(new Font("Monospaced", Font.BOLD, 12));
                g2.drawString("CINEMA MANAGER SYSTEM", x, y);
                y += 15;
                g2.setFont(new Font("Monospaced", Font.PLAIN, 8));
                g2.drawString("123 Movie Street, Cinema City", x, y);
                y += 10;
                g2.drawString("Tel: (08) 1234-5678", x, y);
                y += 20;

                g2.drawLine(x, y, 200, y);
                y += 15;

                // Invoice Info
                g2.setFont(new Font("Monospaced", Font.BOLD, 10));
                // Invoice Info
                g2.setFont(new Font("Monospaced", Font.BOLD, 10));
                g2.drawString("INVOICE #" + maHD, x, y);
                y += 12;
                g2.drawString("REF: " + orderCode, x, y);
                y += 12;
                g2.setFont(new Font("Monospaced", Font.PLAIN, 8));
                g2.drawString("Date: " + new java.util.Date(), x, y);
                y += 12;
                g2.drawString("Cust: " + (customerName != null ? customerName : "Guest"), x, y);
                y += 12;
                g2.drawString("Staff ID: " + (nhanVien != null ? nhanVien.getMaNV() : 1), x, y);
                y += 20;

                g2.drawLine(x, y, 200, y);
                y += 15;

                // Items Header
                g2.drawString("Item", x, y);
                g2.drawString("Amt", 160, y);
                y += 10;

                // Items
                g2.setFont(new Font("Monospaced", Font.PLAIN, 8));

                if (currentLichChieu != null) {
                    double ticketPrice = currentLichChieu.getGiaVe();
                    // Showing tickets
                    for (String seat : listGheDangChon) {
                        g2.drawString("Ticket Seat " + seat, x, y);
                        g2.drawString(String.format("%,.0f", ticketPrice), 160, y);
                        y += 10;
                    }
                }

                for (Map.Entry<String, Integer> entry : currentSnackList.entrySet()) {
                    double p = (entry.getKey().equals("Bap") ? 100000 : 50000);
                    g2.drawString(entry.getKey() + " x" + entry.getValue(), x, y);
                    g2.drawString(String.format("%,.0f", p * entry.getValue()), 160, y);
                    y += 10;
                }

                y += 10;
                g2.drawLine(x, y, 200, y);
                y += 15;

                // Totals
                g2.drawString("Subtotal:", x + 80, y);
                g2.drawString(String.format("%,.0f", subTotal), 160, y);
                y += 10;

                if (discount > 0) {
                    g2.drawString("Discount:", x + 80, y);
                    g2.drawString("-" + String.format("%,.0f", discount), 160, y);
                    y += 10;
                }

                g2.drawString("Tax (5%):", x + 80, y);
                g2.drawString(String.format("%,.0f", tax), 160, y);
                y += 15;

                g2.setFont(new Font("Monospaced", Font.BOLD, 12));
                g2.drawString("TOTAL:", x + 60, y);
                g2.drawString(String.format("%,.0f", total), 150, y);
                y += 30;

                g2.setFont(new Font("Monospaced", Font.ITALIC, 8));
                g2.drawString("Thank you for your visit!", x + 30, y);

                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (java.awt.print.PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    // --- RENDERERS ---

    private class MovieCellRenderer extends JPanel implements ListCellRenderer<LichChieu> {
        private JLabel lblTitle = new JLabel();
        private JLabel lblTime = new JLabel();
        private JLabel lblHall = new JLabel();

        public MovieCellRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(BG_MAIN);

            JPanel pText = new JPanel(new GridLayout(2, 1));
            pText.setOpaque(false);

            lblTitle.setForeground(TXT_PRIMARY);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));

            lblTime.setForeground(BG_ACCENT);
            lblTime.setFont(new Font("SansSerif", Font.BOLD, 12));

            lblHall.setForeground(TXT_SECONDARY);
            lblHall.setFont(new Font("SansSerif", Font.PLAIN, 10));

            pText.add(lblTitle);
            JPanel pSub = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pSub.setOpaque(false);
            pSub.add(lblTime);
            pSub.add(new JLabel("  |  ")); // Sep
            pSub.add(lblHall);
            pText.add(pSub);

            add(pText, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends LichChieu> list, LichChieu value, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblTitle.setText(value.getTenPhim());
            lblTime.setText(value.getGioChieu().toString());
            lblHall.setText(value.getTenPhong());

            setOpaque(true); // Always opaque
            if (isSelected) {
                setBackground(new Color(60, 40, 40));
                setBorder(BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 3, 0, 0, BG_ACCENT),
                        new EmptyBorder(10, 7, 10, 10)));
            } else {
                setBackground(BG_MAIN); // Match main background
                setBorder(new EmptyBorder(10, 10, 10, 10));
            }
            return this;
        }
    }

    private void updatePromoLabel() {
        if (lblPromoInfo == null)
            return;

        if (todaysPromo != null && todaysPromo.isActive()) {
            String txt = "Active Promo: " + todaysPromo.getName() + " ("
                    + (todaysPromo.isPercent() ? "-" + (int) todaysPromo.getDiscountValue() + "%"
                            : "-" + String.format("%,.0f", todaysPromo.getDiscountValue()))
                    + ")";
            lblPromoInfo.setText(txt);
            lblPromoInfo.setVisible(true);
        } else {
            lblPromoInfo.setText("");
            lblPromoInfo.setVisible(false);
        }
    }
}
