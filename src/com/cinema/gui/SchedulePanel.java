package com.cinema.gui;

import com.cinema.dao.*;
import com.cinema.dto.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SchedulePanel extends JPanel {

    // --- COLORS ---
    private static final Color BG_MAIN = new Color(11, 7, 7);
    // private static final Color BG_SIDEBAR = new Color(18, 12, 12); // unused

    private static final Color ACCENT_RED = new Color(229, 9, 20);
    private static final Color GRID_LINE = new Color(40, 30, 30);
    private static final Color TXT_PRIMARY = Color.WHITE;
    private static final Color TXT_SECONDARY = new Color(150, 150, 150);

    // --- DATA ---
    private PhimDAO phimDAO = new PhimDAO();
    private LichChieuDAO lichChieuDAO = new LichChieuDAO();
    private PhongChieuDAO phongDAO = new PhongChieuDAO();
    private VeDAO veDAO = new VeDAO();

    private List<Phim> movieList = new ArrayList<>();
    private List<PhongChieu> roomList = new ArrayList<>();
    private List<LichChieu> allSchedules = new ArrayList<>();
    private List<LichChieu> dateSchedules = new ArrayList<>();

    private LocalDate currentDate = LocalDate.now();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    private Map<String, Integer> durationMap = new HashMap<>();

    // --- COMPONENTS ---
    private TimelineCanvas canvas;
    private JScrollPane timelineScroll;
    private JLabel lDate;
    private JPanel pList; // Sidebar list container

    public SchedulePanel() {
        setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        loadData();

        add(createSidebar(), BorderLayout.WEST);
        add(createTimelineArea(), BorderLayout.CENTER);

        // Auto-refresh when panel becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadData();
            }
        });
    }

    private void loadData() {
        movieList = phimDAO.getAllPhim();
        roomList = phongDAO.getAllPhong();
        allSchedules = lichChieuDAO.getListLichChieu();

        durationMap.clear();
        for (Phim p : movieList) {
            durationMap.put(p.getTenPhim(), p.getThoiLuong());
        }

        filterScheduleByDate();
    }

    private void filterScheduleByDate() {
        if (allSchedules == null) {
            dateSchedules = new ArrayList<>();
            return;
        }

        dateSchedules = allSchedules.stream()
                .filter(lc -> {
                    if (lc.getNgayChieu() == null)
                        return false;
                    return lc.getNgayChieu().toLocalDate().isEqual(currentDate);
                })
                .collect(Collectors.toList());

        if (canvas != null)
            canvas.renderBlocks();
        if (lDate != null)
            lDate.setText(currentDate.format(dateFormatter));
    }

    // =========================================================================
    // SIDEBAR
    // =========================================================================
    private JPanel createSidebar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(50, 50, 50));
        p.setPreferredSize(new Dimension(280, 0));
        p.setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel pHead = new JPanel(new BorderLayout());
        pHead.setOpaque(false);
        JLabel lblTitle = new JLabel("Movie Library");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(TXT_PRIMARY);
        pHead.add(lblTitle, BorderLayout.NORTH);

        JTextField txtSearch = new JTextField(" Search movies...");
        txtSearch.setBackground(new Color(30, 30, 30));
        txtSearch.setForeground(TXT_SECONDARY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(50, 50, 50), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        pHead.add(Box.createVerticalStrut(15), BorderLayout.CENTER);

        p.add(pHead, BorderLayout.NORTH);

        pList = new JPanel();
        pList.setLayout(new BoxLayout(pList, BoxLayout.Y_AXIS));
        pList.setOpaque(false);
        pList.setBorder(new EmptyBorder(20, 0, 0, 0));

        for (Phim ph : movieList) {
            // Filter out 'Archived' movies from the schedule sidebar
            if ("Archived".equalsIgnoreCase(ph.getTrangThai())) {
                continue;
            }
            pList.add(createMovieCard(ph));
            pList.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(pList);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private JPanel createMovieCard(Phim p) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setOpaque(false);
        card.setBackground(new Color(35, 25, 25));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(280, 70));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel thumb = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (p.getPoster() != null && !p.getPoster().isEmpty()) {
                    ImageIcon icon = new ImageIcon(p.getPoster());
                    g2.drawImage(icon.getImage(), 0, 0, 40, 50, null);
                } else {
                    g2.setColor(new Color(60, 40, 40));
                    g2.fillRoundRect(0, 0, 40, 50, 5, 5);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 14));
                    String s = p.getTenPhim().length() > 0 ? p.getTenPhim().substring(0, 1) : "M";
                    g2.drawString(s, 15, 30);
                }
            }
        };
        thumb.setPreferredSize(new Dimension(40, 50));
        thumb.setOpaque(false);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel lName = new JLabel(p.getTenPhim());
        lName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lName.setForeground(TXT_PRIMARY);
        JLabel lMeta = new JLabel(p.getThoiLuong() + " min");
        lMeta.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lMeta.setForeground(TXT_SECONDARY);
        if ("Coming Soon".equalsIgnoreCase(p.getTrangThai())) {
            lMeta.setText(lMeta.getText() + " (Coming Soon)");
            lMeta.setForeground(new Color(255, 200, 0)); // Gold/Yellow
        }

        info.add(lName);
        info.add(lMeta);

        card.add(thumb, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        boolean canDrag = !"Coming Soon".equalsIgnoreCase(p.getTrangThai());
        if (!canDrag) {
            card.setCursor(Cursor.getDefaultCursor());
            // Make name dimmer to indicate disabled state
            lName.setForeground(new Color(100, 100, 100));
        }

        TransferHandler handler = new TransferHandler("text") {
            public Transferable createTransferable(JComponent c) {
                return new StringSelection(String.valueOf(p.getMaPhim()));
            }

            public int getSourceActions(JComponent c) {
                return canDrag ? COPY : NONE;
            }
        };

        MouseAdapter ma = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!canDrag)
                    return; // Prevent drag start
                JComponent c = (JComponent) e.getSource();
                TransferHandler th = c.getTransferHandler();
                if (th != null) {
                    th.exportAsDrag(c, e, TransferHandler.COPY);
                }
            }
        };

        applyDnDRecursive(card, handler, ma);
        return card;
    }

    private void applyDnDRecursive(Container container, TransferHandler handler, MouseListener listener) {
        if (container instanceof JComponent) {
            ((JComponent) container).setTransferHandler(handler);
        }
        container.addMouseListener(listener);
        for (Component c : container.getComponents()) {
            if (c instanceof Container) {
                applyDnDRecursive((Container) c, handler, listener);
            } else if (c instanceof JComponent) {
                ((JComponent) c).setTransferHandler(handler);
                c.addMouseListener(listener);
            }
        }
    }

    // =========================================================================
    // TIMELINE AREA
    // =========================================================================
    private JPanel createTimelineArea() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_MAIN);

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBackground(BG_MAIN);
        pTop.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pDate = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pDate.setOpaque(false);
        lDate = new JLabel(currentDate.format(dateFormatter));
        lDate.setForeground(TXT_PRIMARY);
        lDate.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton btnPrev = createNavButton("<");
        btnPrev.addActionListener(e -> {
            currentDate = currentDate.minusDays(1);
            filterScheduleByDate();
        });

        JButton btnNext = createNavButton(">");
        btnNext.addActionListener(e -> {
            currentDate = currentDate.plusDays(1);
            filterScheduleByDate();
        });

        pDate.add(btnPrev);
        pDate.add(lDate);
        pDate.add(btnNext);

        JPanel pActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pActions.setOpaque(false);

        JButton btnClear = createButton("Clear Day", ACCENT_RED);
        btnClear.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "DELETE ALL Schedules for " + currentDate + "?\nThis cannot be undone.", "Confirm Clear All",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (lichChieuDAO.deleteLichChieuByDate(currentDate.toString())) {
                    loadData();
                    repaint();
                }
            }
        });

        pActions.add(btnClear);

        JPanel pTitleWrap = new JPanel(new BorderLayout());
        pTitleWrap.setOpaque(false);
        JLabel title = new JLabel("CinemaScheduler Pro");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TXT_PRIMARY);
        pTitleWrap.add(title, BorderLayout.WEST);
        pTitleWrap.add(pDate, BorderLayout.CENTER);
        pTitleWrap.add(pActions, BorderLayout.EAST);

        pTop.add(pTitleWrap, BorderLayout.CENTER);
        p.add(pTop, BorderLayout.NORTH);

        canvas = new TimelineCanvas();
        RoomHeaderPanel roomHeader = new RoomHeaderPanel();

        timelineScroll = new JScrollPane(canvas);
        timelineScroll.setBorder(null);
        timelineScroll.getViewport().setBackground(BG_MAIN);
        timelineScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Set Fixed Row Header (Room List)
        timelineScroll.setRowHeaderView(roomHeader);

        // Set Corner (Top Left)
        JPanel corner = new JPanel();
        corner.setBackground(BG_MAIN);
        timelineScroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);

        addCanvasListeners(); // Connect the listener

        p.add(timelineScroll, BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    // TIMELINE CANVAS
    // =========================================================================
    class TimelineCanvas extends JPanel {
        private final int ROW_HEIGHT = 120;
        private final int HEADER_HEIGHT = 40;
        private final int SLOT_WIDTH = 100;

        private final int START_HOUR = 8;
        private final int END_HOUR = 24;

        public TimelineCanvas() {
            setLayout(null);
            setOpaque(false);
            refreshSize();

            new DropTarget(this, new DropTargetAdapter() {
                public void drop(DropTargetDropEvent dtde) {
                    try {
                        Transferable tr = dtde.getTransferable();
                        if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY);
                            String maPhimStr = (String) tr.getTransferData(DataFlavor.stringFlavor);
                            int maPhim = Integer.parseInt(maPhimStr);
                            handleDrop(maPhim, dtde.getLocation());
                            dtde.dropComplete(true);
                        } else {
                            dtde.rejectDrop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dtde.rejectDrop();
                    }
                }
            });
            renderBlocks();
        }

        public void refreshSize() {
            int width = (END_HOUR - START_HOUR) * SLOT_WIDTH; // Removed ROOM_INFO_WIDTH
            int height = HEADER_HEIGHT + roomList.size() * ROW_HEIGHT + 100;
            setPreferredSize(new Dimension(width, height));
            revalidate();
        }

        private String normalizeTime(String input) throws Exception {
            input = input.trim();
            // Case: "13" -> "13:00:00"
            if (input.matches("^\\d{1,2}$")) {
                int h = Integer.parseInt(input);
                if (h < 0 || h > 23)
                    throw new Exception("Hour must be 0-23");
                return String.format("%02d:00:00", h);
            }
            // Case: "13:30" -> "13:30:00"
            if (input.matches("^\\d{1,2}:\\d{2}$")) {
                String[] parts = input.split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                if (h < 0 || h > 23 || m < 0 || m > 59)
                    throw new Exception("Invalid Time");
                return String.format("%02d:%02d:00", h, m);
            }
            // Case: "13:30:00" -> keep
            if (input.matches("^\\d{1,2}:\\d{2}:\\d{2}$")) {
                return input;
            }
            throw new Exception("Invalid Format. Use HH, HH:mm, or HH:mm:ss");
        }

        private void handleDrop(int maPhim, Point p) {
            int y = p.y - HEADER_HEIGHT;
            int rowIdx = y / ROW_HEIGHT;
            if (rowIdx < 0 || rowIdx >= roomList.size())
                return;
            PhongChieu room = roomList.get(rowIdx);

            if (room.getTinhTrang() == 0) {
                JOptionPane.showMessageDialog(SchedulePanel.this, "Room is under maintenance!", "Blocked",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int x = p.x; // No offset
            if (x < 0)
                x = 0;
            double hours = (double) x / SLOT_WIDTH;
            double actualHour = START_HOUR + hours;
            int h = (int) actualHour;
            int m = (int) ((actualHour - h) * 60);

            String timeStr = String.format("%02d:%02d:00", h, m);
            String dateStr = currentDate.toString();

            JTextField txtTime = new JTextField(timeStr);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Selected Movie ID: " + maPhim));
            panel.add(new JLabel("Room: " + room.getTenPhong()));
            panel.add(new JLabel("Date: " + dateStr));
            panel.add(new JLabel("Start Time (HH, HH:mm, HH:mm:ss):"));
            panel.add(txtTime);

            int result = JOptionPane.showConfirmDialog(SchedulePanel.this, panel,
                    "Confirm Schedule Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String finalTime = normalizeTime(txtTime.getText());
                    double price = 0; // Default to 0 as pricing is handled elsewhere

                    boolean ok = lichChieuDAO.themLichChieu(maPhim, room.getMaPhong(), dateStr, finalTime, price);
                    if (ok) {
                        loadData();
                        revalidate();
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(SchedulePanel.this, "Save Failed. Check Database Constraints.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(SchedulePanel.this, "Error: " + e.getMessage());
                }
            }
        }

        public void renderBlocks() {
            removeAll();

            for (LichChieu lc : dateSchedules) {
                // 1. Find Room & Row Index
                int rowIdx = -1;
                PhongChieu room = null;
                for (int i = 0; i < roomList.size(); i++) {
                    if (roomList.get(i).getTenPhong().equals(lc.getTenPhong())) {
                        rowIdx = i;
                        room = roomList.get(i);
                        break;
                    }
                }
                if (rowIdx == -1 || room == null)
                    continue;

                // 2. Calculate Position & Size
                Time t = lc.getGioChieu();
                if (t == null)
                    continue;
                int h = t.toLocalTime().getHour();
                int m = t.toLocalTime().getMinute();
                double hoursFromStart = (h - START_HOUR) + (m / 60.0);
                int x = (int) (hoursFromStart * SLOT_WIDTH); // No offset
                int y = HEADER_HEIGHT + rowIdx * ROW_HEIGHT + 20; // Padding top

                // 3. Get Movie Details (Duration, Name)
                Phim movie = null;
                int duration = 120;
                for (Phim p : movieList) {
                    if (p.getTenPhim().equals(lc.getTenPhim())) {
                        movie = p;
                        duration = p.getThoiLuong();
                        break;
                    }
                }

                int w = (int) ((duration / 60.0) * SLOT_WIDTH);

                // 4. Sold Out Logic
                int soldCount = veDAO.countVe(lc.getMaLichChieu());
                int capacity = room.getSoLuongGhe();
                // boolean isSoldOut = soldCount >= capacity; // Unused

                // 5. Create Advanced Block
                JPanel block = createAdvancedBlock(lc, movie, duration, soldCount, capacity);
                block.setBounds(x, y, w, ROW_HEIGHT - 30); // Slightly shorter for aesthetics
                add(block);
            }
            revalidate();
            repaint();
        }

        private JPanel createAdvancedBlock(LichChieu lc, Phim movie, int duration, int sold, int capacity) {
            JPanel p = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    // Custom Paint for Background & Red Accent
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Background (Dark Gradient)
                    GradientPaint gp = new GradientPaint(0, 0, new Color(40, 10, 10), 0, getHeight(),
                            new Color(20, 5, 5));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                    // Left Accent Bar (Red)
                    g2.setColor(new Color(229, 9, 20));
                    g2.fillRoundRect(0, 0, 6, getHeight(), 10, 10); // Rounded left edge
                    g2.fillRect(4, 0, 4, getHeight()); // Fill to make it square on right side of bar

                    // --- PROGRESS BAR RENDERING ---
                    int barX = 65;
                    int barY = 60;
                    int barW = getWidth() - 75; // dynamic width
                    int barH = 6;

                    if (barW > 0) {
                        // Track (Background)
                        g2.setColor(new Color(60, 30, 30));
                        g2.fillRoundRect(barX, barY, barW, barH, 6, 6);

                        // Progress (Foreground)
                        double pct = (double) sold / capacity;
                        if (pct > 1.0)
                            pct = 1.0;
                        int progressW = (int) (barW * pct);

                        if (progressW > 0) {
                            g2.setColor(new Color(255, 193, 7)); // Amber/Gold
                            g2.fillRoundRect(barX, barY, progressW, barH, 6, 6);
                        }
                    }
                }
            };
            p.setLayout(null);
            p.setOpaque(false);
            p.setCursor(new Cursor(Cursor.HAND_CURSOR));
            p.setToolTipText("Sold: " + sold + "/" + capacity);

            // 1. Poster Thumbnail (Left)
            JPanel poster = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (movie != null && movie.getPoster() != null && !movie.getPoster().isEmpty()) {
                        ImageIcon icon = new ImageIcon(movie.getPoster());
                        g2.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
                    } else {
                        g2.setColor(Color.WHITE);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        // Mock Image
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
                        // Initial
                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("Serif", Font.BOLD, 20));
                        String s = (movie != null) ? movie.getTenPhim().substring(0, 1) : "M";
                        g2.drawString(s, 12, 35);
                    }
                }
            };
            poster.setBounds(15, 10, 40, 55);
            p.add(poster);

            // 2. Title (Top Right)
            JLabel lTitle = new JLabel(movie != null ? movie.getTenPhim() : lc.getTenPhim());
            lTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
            lTitle.setForeground(Color.WHITE);
            lTitle.setBounds(65, 10, 200, 20);
            p.add(lTitle);

            // 3. Time Range (Red Text)
            Time start = lc.getGioChieu();
            int h = start.toLocalTime().getHour();
            int m = start.toLocalTime().getMinute();
            int endMin = h * 60 + m + duration;
            int hEnd = (endMin / 60) % 24;
            int mEnd = endMin % 60;
            String timeStr = String.format("%02d:%02d - %02d:%02d", h, m, hEnd, mEnd);

            JLabel lTime = new JLabel(timeStr);
            lTime.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lTime.setForeground(new Color(229, 9, 20)); // Red text
            lTime.setBounds(65, 30, 200, 20);
            p.add(lTime);

            // 4. Percentage Text
            int percent = (int) (((double) sold / capacity) * 100);
            JLabel lPct = new JLabel(percent + "% Full", SwingConstants.RIGHT);
            lPct.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lPct.setForeground(new Color(200, 200, 200)); // Light Gray
            // Align to bottom right of the progress bar area
            lPct.setBounds(65, 68, 100, 15);
            p.add(lPct);

            // 5. Sold Out Badge (Bottom Left, below poster) - Optional if we have progress
            // bar?
            // User still wants "sold out" logic? Maybe the bar is enough, but badge is
            // clear.
            // Let's keep badge if 100% full?
            if (sold >= capacity) {
                // Maybe change text color to Red if full
                lPct.setForeground(new Color(229, 9, 20));
                lPct.setText("SOLD OUT");
            }

            // Click Interaction
            p.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showEditDialog(lc);
                }
            });

            return p;
        }

        private void showEditDialog(LichChieu lc) {
            String currentTime = lc.getGioChieu().toString();

            JTextField txtTime = new JTextField(currentTime);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Edit Schedule: " + lc.getTenPhim()));
            panel.add(new JLabel("Time (HH, HH:mm, HH:mm:ss):"));
            panel.add(txtTime);

            Object[] options = { "Update", "Delete", "Cancel" };
            int result = JOptionPane.showOptionDialog(SchedulePanel.this, panel, "Edit Schedule",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (result == 0) { // Update
                try {
                    String newTime = normalizeTime(txtTime.getText());
                    double newPrice = lc.getGiaVe(); // Keep existing price unaltered

                    if (lichChieuDAO.updateLichChieu(lc.getMaLichChieu(), newTime, newPrice)) {
                        loadData(); // This will trigger filterByDate and renderBlocks
                        revalidate(); // Add Revalidate here key
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(SchedulePanel.this, "Update Failed");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SchedulePanel.this, "Invalid Input: " + ex.getMessage());
                }
            } else if (result == 1) { // Delete
                int confirm = JOptionPane.showConfirmDialog(SchedulePanel.this,
                        "Are you sure you want to delete this schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (lichChieuDAO.deleteLichChieu(lc.getMaLichChieu())) {
                        loadData();
                        revalidate(); // Add revalidate here
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(SchedulePanel.this, "Delete Failed");
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(GRID_LINE);
            for (int i = 0; i <= (END_HOUR - START_HOUR); i++) {
                int x = (i * SLOT_WIDTH); // No offset
                g2.drawLine(x, 0, x, getHeight());
                if (i < (END_HOUR - START_HOUR)) {
                    g2.setColor(TXT_SECONDARY);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    int h = START_HOUR + i;
                    String label = (h < 12 ? h + " AM" : (h == 12 ? 12 : h - 12) + " PM");
                    g2.drawString(label, x + 10, 25);
                    g2.setColor(GRID_LINE);
                }
            }

            // Draw horizontal lines for rows
            for (int i = 0; i < roomList.size(); i++) {
                int y = HEADER_HEIGHT + (i * ROW_HEIGHT);
                PhongChieu room = roomList.get(i);

                // If maintenance, draw striped/dark background
                if (room.getTinhTrang() == 0) {
                    g2.setColor(new Color(20, 10, 10, 150)); // Semi-transparent red overlay
                    g2.fillRect(0, y, getWidth(), ROW_HEIGHT);

                    // Optional: Striped pattern
                    g2.setColor(new Color(50, 20, 20, 100));
                    for (int k = 0; k < getWidth(); k += 20) {
                        g2.drawLine(k, y, k + 10, y + ROW_HEIGHT);
                    }
                }

                g2.setColor(new Color(30, 30, 30));
                g2.drawLine(0, y, getWidth(), y);
            }
        }
    }

    // --- ROW HEADER PANEL (Fixed Room List) ---
    class RoomHeaderPanel extends JPanel {
        private final int ROW_HEIGHT = 120;
        private final int HEADER_HEIGHT = 40;
        private final int WIDTH = 200;

        public RoomHeaderPanel() {
            setPreferredSize(new Dimension(WIDTH, 0)); // Height managed by layout/scroll
            setBackground(BG_MAIN);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getY() > HEADER_HEIGHT) {
                        int rowIdx = (e.getY() - HEADER_HEIGHT) / ROW_HEIGHT;
                        if (rowIdx >= 0 && rowIdx < roomList.size()) {
                            editRoom(roomList.get(rowIdx));
                        }
                    }
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            // Dynamic height based on room list to match canvas
            int height = HEADER_HEIGHT + roomList.size() * ROW_HEIGHT + 100;
            return new Dimension(WIDTH, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw Clickable Room Areas
            for (int i = 0; i < roomList.size(); i++) {
                int y = HEADER_HEIGHT + (i * ROW_HEIGHT);
                g2.setColor(new Color(30, 30, 30));
                g2.drawLine(0, y, getWidth(), y);
                PhongChieu room = roomList.get(i);
                // Name
                g2.setColor(room.getTinhTrang() == 1 ? TXT_PRIMARY : new Color(150, 100, 100));
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString(room.getTenPhong(), 20, y + 40);

                // Capacity or Status
                g2.setColor(TXT_SECONDARY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                if (room.getTinhTrang() == 1) {
                    g2.drawString("Capacity: " + room.getSoLuongGhe(), 20, y + 60);
                } else {
                    g2.setColor(new Color(255, 100, 100)); // Red
                    g2.drawString("⚠️ MAINTENANCE", 20, y + 60);
                }

                // Hint
                g2.setColor(new Color(70, 70, 70));
                g2.setFont(new Font("SansSerif", Font.ITALIC, 10));
                g2.drawString("(Click to Edit)", 20, y + 80);
            }
            // Vertical Separator Line
            g2.setColor(new Color(50, 50, 50));
            g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(8, 15, 8, 15));
        return b;
    }

    // Add MouseListener to Canvas (Optional for other interactions)
    private void addCanvasListeners() {
        // Room edit listener moved to RoomHeaderPanel
    }

    private void editRoom(PhongChieu p) {
        JTextField txtName = new JTextField(p.getTenPhong());
        JSpinner spnSeats = new JSpinner(new SpinnerNumberModel(p.getSoLuongGhe(), 1, 1000, 10)); // Use Spinner for
                                                                                                  // safety
        JCheckBox chkReset = new JCheckBox("Regenerate Seat Layout (Warning: Resets all seats)");

        Object[] msg = {
                "Edit Room Details",
                "Name:", txtName,
                "Seats:", spnSeats,
                chkReset
        };

        int result = JOptionPane.showConfirmDialog(this, msg, "Manage Room", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) { // Update
            try {
                String name = txtName.getText();
                int seats = (int) spnSeats.getValue();

                if (phongDAO.updatePhong(p.getMaPhong(), name, seats)) {
                    if (chkReset.isSelected()) {
                        new com.cinema.dao.SeatGenerator().generateSeats(p.getMaPhong(), seats);
                    }
                    loadData();
                    repaint();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Data");
            }
        }
        // Note: Delete button removed from this simplified view to encourage using Room
        // Management for heavy ops,
        // or we can keep it but standard dialog doesn't have custom buttons easily.
        // If user wants delete, they can use the actual Room Management or I can
        // implement custom option dialog again if needed.
        // For now, aligning with RoomPanel's edit style (logic-wise).
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(60, 60, 60));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(new EmptyBorder(5, 12, 5, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    class RoundedPanel extends JPanel {
        int r;
        Color c;

        RoundedPanel(int r, Color c) {
            this.r = r;
            this.c = c;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            super.paintComponent(g);
        }
    }
}
