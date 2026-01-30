package com.cinema.gui;

import com.cinema.dto.CaLamViec;
import com.cinema.dto.NhanVien;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import com.cinema.dao.CaLamViecDAO;
import com.cinema.dao.ChamCongDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    private NhanVien taiKhoanHienTai;
    private JPanel mainContentPanel;
    private JPanel sidebarPanel;
    private NavButton currentActiveBtn;
    private List<NavButton> navButtons = new ArrayList<>();

    // Colors
    private static final Color BG_CONTENT = new Color(20, 20, 20); // Dark content bg
    private static final Color ACCENT_RED = new Color(211, 47, 47); // Red
    private static final Color TXT_INACTIVE = new Color(140, 140, 140);

    private CaLamViecDAO caLamViecDAO;
    private ChamCongDAO chamCongDAO;
    private boolean isCheckedIn = false;

    public MainFrame(NhanVien nv) {
        caLamViecDAO = new CaLamViecDAO();
        chamCongDAO = new ChamCongDAO();
        this.taiKhoanHienTai = nv;
        initComponents();
        phanQuyen();
    }

    private void initComponents() {
        setTitle("CineMe - Admin Console");
        setSize(1280, 800);
        setMinimumSize(new Dimension(800, 600)); // Prevent window from shrinking too small
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Launch in fullscreen
        setLayout(new BorderLayout());

        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        // Custom Gradient Panel
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 12), 0, h, new Color(25, 25, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }
        };
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        sidebarPanel.setMinimumSize(new Dimension(280, 0));

        // 1. Brand Header
        JPanel pBrand = new JPanel(new BorderLayout(15, 0));
        pBrand.setOpaque(false);
        pBrand.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Custom Logo Icon
        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                ImageIcon icon = new ImageIcon("resources/logo.png");
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image img = icon.getImage();
                    int imgW = img.getWidth(null);
                    int imgH = img.getHeight(null);
                    int panelW = getWidth();
                    int panelH = getHeight();

                    // Calculate aspect ratio
                    float ratio = Math.min((float) panelW / imgW, (float) panelH / imgH);
                    int newW = (int) (imgW * ratio);
                    int newH = (int) (imgH * ratio);

                    // Center the image
                    int x = (panelW - newW) / 2;
                    int y = (panelH - newH) / 2;

                    g2.drawImage(img, x, y, newW, newH, null);
                } else {
                    // Fallback
                    g2.setColor(ACCENT_RED);
                    g2.fillRoundRect(0, 0, 40, 40, 12, 12);
                    g2.setColor(Color.WHITE);
                    Polygon play = new Polygon();
                    play.addPoint(14, 10);
                    play.addPoint(14, 30);
                    play.addPoint(30, 20);
                    g2.fill(play);
                }
            }
        };
        logoIcon.setPreferredSize(new Dimension(40, 40));
        logoIcon.setOpaque(false);

        JPanel pText = new JPanel(new GridLayout(2, 1));
        pText.setOpaque(false);

        JLabel lblBrand = new JLabel(" CineMe");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBrand.setForeground(Color.WHITE);

        String roleTitle = "Admin Console";
        if (!"QuanLy".equalsIgnoreCase(taiKhoanHienTai.getChucVu())) {
            roleTitle = "Employee Console";
        }
        JLabel lblSub = new JLabel("  " + roleTitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(150, 150, 150));

        pText.add(lblBrand);
        pText.add(lblSub);

        pBrand.add(logoIcon, BorderLayout.WEST);
        pBrand.add(pText, BorderLayout.CENTER);

        sidebarPanel.add(pBrand, BorderLayout.NORTH);

        // 2. Navigation Items (Center)
        JPanel pNav = new JPanel();
        pNav.setLayout(new BoxLayout(pNav, BoxLayout.Y_AXIS));
        pNav.setOpaque(false);
        pNav.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Main Section
        pNav.add(createNavButton("Home", "dashboard", e -> switchPanel(new StatsPanel(taiKhoanHienTai))));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Booking", "ticket", e -> switchPanel(new BanVePanel(taiKhoanHienTai))));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Concessions", "food", e -> switchPanel(new ConcessionPanel())));

        // Divider
        pNav.add(Box.createVerticalStrut(20));
        JLabel lblMgmt = new JLabel("MANAGEMENT");
        lblMgmt.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMgmt.setForeground(new Color(100, 100, 100));
        lblMgmt.setBorder(new EmptyBorder(0, 15, 10, 0));
        lblMgmt.setAlignmentX(Component.LEFT_ALIGNMENT);
        pNav.add(lblMgmt);

        // Mgmt Section
        pNav.add(createNavButton("Movies", "movie", e -> switchPanel(new MoviePanel())));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Schedule", "calendar", e -> switchPanel(new SchedulePanel())));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Rooms", "movie", e -> switchPanel(new RoomPanel()))); // Icon reused
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Staff", "user", e -> switchPanel(new EmployeePanel())));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Shifts", "calendar", e -> switchPanel(new ShiftPanel())));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Loyalty", "star", e -> switchPanel(new LoyaltyPanel())));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Policies", "gear", e -> switchPanel(new DiscountPanel())));
        pNav.add(Box.createVerticalStrut(5));
        pNav.add(createNavButton("Dashboard", "report", e -> switchPanel(new ReportPanel())));

        sidebarPanel.add(pNav, BorderLayout.CENTER);

        // 3. Footer (User Profile)
        JPanel pFooter = new JPanel(new BorderLayout());
        pFooter.setOpaque(false);
        pFooter.setBorder(new EmptyBorder(15, 15, 15, 15)); // Reduced padding for more space

        // Separator line
        JPanel line = new JPanel();
        line.setBackground(new Color(40, 40, 40));
        line.setPreferredSize(new Dimension(0, 1));
        pFooter.add(line, BorderLayout.NORTH);

        // User Panel using BorderLayout to prevent wrapping
        JPanel pUser = new JPanel(new BorderLayout(10, 0)); // Horizontal gap 10
        pUser.setOpaque(false);
        pUser.setBorder(new EmptyBorder(15, 0, 0, 0)); // Top padding

        // Avatar
        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 40;
                int padX = (getWidth() - size) / 2;
                int padY = (getHeight() - size) / 2;

                // Draw Circle Centered
                g2.setColor(new Color(60, 60, 65));
                g2.fillOval(padX, padY, size, size);

                // Initials
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String initials = "U";
                if (taiKhoanHienTai.getHoTen().length() > 0)
                    initials = taiKhoanHienTai.getHoTen().substring(0, 1);

                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(initials, g2);

                // Center text within the circle
                int x = padX + (size - (int) r.getWidth()) / 2;
                // Visual vertical centering: slightly different from mathematical center for
                // Caps
                int y = padY + (size - (int) r.getHeight()) / 2 + fm.getAscent() - 2;

                g2.drawString(initials, x, y);
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setOpaque(false);

        // Add Avatar to West
        pUser.add(avatar, BorderLayout.WEST);

        JPanel pUserInfo = new JPanel(new GridLayout(2, 1));
        pUserInfo.setOpaque(false);

        JLabel lName = new JLabel(taiKhoanHienTai.getHoTen());
        lName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lName.setForeground(Color.WHITE);

        isCheckedIn = chamCongDAO.isCheckedIn(taiKhoanHienTai.getMaNV());
        String status = isCheckedIn ? "Online" : "Offline";

        JLabel lRole = new JLabel(taiKhoanHienTai.getChucVu() + " • " + status);
        lRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lRole.setForeground(isCheckedIn ? new Color(100, 255, 100) : TXT_INACTIVE);

        pUserInfo.add(lName);
        pUserInfo.add(lRole);

        // Logout
        JButton btnLogout = new JButton("Log Out");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout.setForeground(new Color(255, 100, 100)); // Light Red
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(100, 50, 50)));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(80, 25));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        // Status Badge
        // Status Badge (Now acts as container for Check In/Out btn)
        JPanel pStatus = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Center alignment for button area

        pStatus.setOpaque(false);

        // ... Buttons (kept as is, but logic below) ...

        // Check-In Button
        JButton btnIn = new JButton("Check In");
        btnIn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnIn.setForeground(new Color(100, 255, 100));
        // Add padding via CompoundBorder
        btnIn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 50)),
                new EmptyBorder(4, 12, 4, 12)));
        btnIn.setContentAreaFilled(false);
        btnIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIn.setPreferredSize(new Dimension(90, 25));

        // Check-Out Button
        JButton btnOut = new JButton("Check Out");
        btnOut.setFont(new Font("Segoe UI", Font.BOLD, 11)); // Slightly larger font
        btnOut.setForeground(new Color(255, 100, 100));
        // Add padding via CompoundBorder
        btnOut.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 50, 50)),
                new EmptyBorder(4, 12, 4, 12)));
        btnOut.setContentAreaFilled(false);
        btnOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOut.setPreferredSize(new Dimension(90, 25));

        // Logic
        btnIn.addActionListener(e -> {
            if (isCheckedIn)
                return;

            java.util.List<CaLamViec> shifts = caLamViecDAO.getAllCaLamViec();
            if (shifts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No shifts defined in the system!");
                return;
            }

            // Attempt to pre-select current shift
            CaLamViec currentShift = caLamViecDAO.getCurrentShift();
            CaLamViec selectedShift = (CaLamViec) JOptionPane.showInputDialog(
                    this,
                    "Select Shift to Check In:",
                    "Shift Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    shifts.toArray(),
                    currentShift != null ? currentShift : shifts.get(0));

            if (selectedShift != null) {
                if (chamCongDAO.checkIn(taiKhoanHienTai.getMaNV(), selectedShift.getMaCa())) {
                    JOptionPane.showMessageDialog(this, "Checked In for shift: " + selectedShift.getTenCa());
                    dispose();
                    new MainFrame(taiKhoanHienTai).setVisible(true); // Restart to refresh state
                } else {
                    JOptionPane.showMessageDialog(this, "Check-In Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnOut.addActionListener(e -> {
            if (!isCheckedIn)
                return;
            if (chamCongDAO.checkOut(taiKhoanHienTai.getMaNV())) {
                JOptionPane.showMessageDialog(this, "Checked Out Successfully!");
                dispose();
                new MainFrame(taiKhoanHienTai).setVisible(true); // Restart
            } else {
                JOptionPane.showMessageDialog(this, "Check-Out Failed!");
            }
        });

        // Visibility based on status
        btnIn.setVisible(!isCheckedIn);
        btnOut.setVisible(isCheckedIn);

        pStatus.add(btnIn);
        pStatus.add(btnOut);

        JPanel pAction = new JPanel(new BorderLayout(0, 5));
        pAction.setOpaque(false);

        // Composite User Info Panel (Text Only)
        JPanel pInfoComposite = new JPanel(new BorderLayout());
        pInfoComposite.setOpaque(false);
        pInfoComposite.add(pUserInfo, BorderLayout.CENTER);

        // Button Bar (Check In/Out + Logout)
        JPanel pButtonBar = new JPanel(new GridLayout(1, 2, 5, 0)); // 2 cols, 5px gap
        pButtonBar.setOpaque(false);
        pButtonBar.add(pStatus); // Check In/Out (FlowLayout) will contain the button
        pButtonBar.add(btnLogout);

        pAction.add(pInfoComposite, BorderLayout.CENTER);
        pAction.add(pButtonBar, BorderLayout.SOUTH);

        // Add Action Panel to Center (BorderLayout) of pUser
        pUser.add(pAction, BorderLayout.CENTER);

        pFooter.add(pUser, BorderLayout.CENTER);
        sidebarPanel.add(pFooter, BorderLayout.SOUTH);

        add(sidebarPanel, BorderLayout.WEST);

        // --- CONTENT AREA ---
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(BG_CONTENT);
        // Default View
        JLabel dummy = new JLabel("Welcome to CineMe", JLabel.CENTER);
        dummy.setForeground(Color.GRAY);
        mainContentPanel.add(dummy, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    // --- HELPER: Nav Button Creation ---
    private NavButton createNavButton(String text, String iconType, java.awt.event.ActionListener action) {
        NavButton btn = new NavButton(text, iconType);
        if (action != null) {
            btn.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    setActiveButton(btn);
                    action.actionPerformed(null);
                }
            });
        }
        navButtons.add(btn);
        return btn;
    }

    private void setActiveButton(NavButton btn) {
        if (currentActiveBtn != null)
            currentActiveBtn.setActive(false);
        currentActiveBtn = btn;
        currentActiveBtn.setActive(true);
    }

    public void switchPanel(JPanel p) {
        mainContentPanel.removeAll();
        mainContentPanel.add(p, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void phanQuyen() {
        if ("BanVe".equalsIgnoreCase(taiKhoanHienTai.getChucVu())) {
            // "BanVe" sees: Dashboard (0), Booking (1), Concessions (2), Policies (9)
            // Hides: Movies (3), Schedule (4), Rooms (5), Staff (6), Shifts (7), Loyalty
            // (8), Reports (10)

            navButtons.get(3).setVisible(false); // Movies
            navButtons.get(4).setVisible(false); // Schedule
            navButtons.get(5).setVisible(false); // Rooms
            navButtons.get(6).setVisible(false); // Staff
            navButtons.get(7).setVisible(false); // Shifts
            navButtons.get(8).setVisible(false); // Loyalty
            navButtons.get(10).setVisible(false); // Report

            navButtons.get(1).setVisible(true); // Booking
            navButtons.get(9).setVisible(false); // Policies (Hidden for cashier?)

            setActiveButton(navButtons.get(0)); // Default to Dashboard
            switchPanel(new StatsPanel(taiKhoanHienTai));
        } else {
            // "QuanLy" sees everything
            setActiveButton(navButtons.get(0));
            switchPanel(new StatsPanel(taiKhoanHienTai));
        }
    }

    // Custom NavButton Class
    class NavButton extends JPanel {
        private boolean isActive = false;
        private boolean isHover = false;
        private String text;
        private String iconType;

        public NavButton(String text, String iconType) {
            this.text = text;
            this.iconType = iconType;
            setOpaque(false);
            setPreferredSize(new Dimension(280, 50));
            setMaximumSize(new Dimension(280, 50));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 15, 0, 0)); // Internal padding

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
            });
        }

        public void setActive(boolean b) {
            this.isActive = b;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Active / Hover State Background
            if (isActive) {
                // Gradient for active state
                GradientPaint gp = new GradientPaint(0, 0, new Color(220, 50, 50, 40), getWidth(), 0,
                        new Color(220, 50, 50, 5));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 12, 12);

                // Accent Bar (Left)
                g2.setColor(ACCENT_RED);
                g2.fillRoundRect(0, 8, 4, getHeight() - 16, 4, 4);

            } else if (isHover) {
                g2.setColor(new Color(255, 255, 255, 10)); // Subtle white hover
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 12, 12);
            }

            // Text & Icon Color
            Color fg = isActive ? Color.WHITE : new Color(170, 170, 170); // Dimmed gray for inactive
            if (isHover && !isActive)
                fg = Color.WHITE;

            g2.setColor(fg);

            // Draw Icon
            int iconSize = 20;
            int iconY = (getHeight() - iconSize) / 2;
            int iconX = 20;

            drawIcon(g2, iconType, iconX, iconY, iconSize);

            // Draw Text
            g2.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 15));
            g2.drawString(text, 55, 30);

            // Optional: Chevron for active?
            if (isActive) {
                g2.setColor(new Color(255, 255, 255, 50));
                // g2.drawString("›", getWidth() - 20, 30);
            }
        }

        private void drawIcon(Graphics2D g2, String type, int x, int y, int s) {
            Stroke old = g2.getStroke();
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (type) {
                case "dashboard":
                    g2.drawRect(x, y, 7, 7);
                    g2.drawRect(x + 10, y, 7, 7);
                    g2.drawRect(x, y + 10, 7, 7);
                    g2.drawRect(x + 10, y + 10, 7, 7);
                    break;
                case "movie":
                    g2.drawRoundRect(x, y + 2, 18, 16, 4, 4);
                    g2.drawLine(x, y + 7, x + 18, y + 7);
                    g2.fillOval(x + 7, y + 9, 4, 4);
                    break;
                case "calendar":
                    g2.drawRoundRect(x, y + 2, 18, 16, 4, 4);
                    g2.drawLine(x + 5, y, x + 5, y + 4);
                    g2.drawLine(x + 13, y, x + 13, y + 4);
                    g2.drawLine(x, y + 7, x + 18, y + 7);
                    break;
                case "ticket":
                    Path2D t = new Path2D.Float();
                    t.moveTo(x, y + 4);
                    t.lineTo(x + 18, y + 4);
                    t.lineTo(x + 18, y + 16);
                    t.lineTo(x, y + 16);
                    t.closePath();
                    g2.draw(t);
                    g2.drawLine(x + 14, y + 4, x + 14, y + 16);
                    break;
                case "food":
                    int[] bx = { x + 4, x + 14, x + 12, x + 6 };
                    int[] by = { y + 6, y + 6, y + 18, y + 18 };
                    g2.drawPolygon(bx, by, 4);
                    g2.drawArc(x + 4, y + 2, 4, 4, 0, 360);
                    g2.drawArc(x + 10, y + 2, 4, 4, 0, 360);
                    break;
                case "user":
                    g2.drawOval(x + 5, y, 8, 8);
                    g2.drawArc(x, y + 10, 18, 10, 0, 180);
                    break;
                case "star":
                    Path2D star = new Path2D.Double();
                    double cx = x + 9;
                    double cy = y + 9;
                    double ro = 9;
                    double ri = 4;
                    for (int i = 0; i < 10; i++) {
                        double ang = -Math.PI / 2 + i * Math.PI / 5;
                        double r = (i % 2 == 0) ? ro : ri;
                        if (i == 0)
                            star.moveTo(cx + Math.cos(ang) * r, cy + Math.sin(ang) * r);
                        else
                            star.lineTo(cx + Math.cos(ang) * r, cy + Math.sin(ang) * r);
                    }
                    star.closePath();
                    g2.draw(star);
                    break;
                case "gear":
                    g2.drawOval(x + 2, y + 2, 14, 14);
                    g2.drawOval(x + 6, y + 6, 6, 6);
                    break;
                case "report":
                    g2.drawRect(x + 2, y + 2, 14, 16);
                    g2.drawLine(x + 6, y + 6, x + 12, y + 6);
                    g2.drawLine(x + 6, y + 10, x + 12, y + 10);
                    break;
            }
            g2.setStroke(old);
        }
    }
}
