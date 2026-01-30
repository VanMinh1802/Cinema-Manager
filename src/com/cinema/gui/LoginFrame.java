package com.cinema.gui;

import com.cinema.db.DBConnection;
import com.cinema.dto.NhanVien;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginFrame extends JFrame {

    // --- COLORS ---
    private static final Color BG_GRADIENT_START = Color.decode("#0F0505"); // Dark Black/Red
    private static final Color BG_GRADIENT_END = Color.decode("#3E1010"); // Deep Red
    private static final Color CARD_BG = Color.decode("#181818");
    private static final Color ACCENT_RED = Color.decode("#D32F2F");
    private static final Color TXT_PRIMARY = Color.white;
    private static final Color TXT_SECONDARY = Color.gray;
    private static final Color INPUT_BG = Color.decode("#2B2B2B");

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("CineMe - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Launch in fullscreen

        // Custom Content Pane for Gradient Background
        setContentPane(new GradientPanel());
        setLayout(new GridBagLayout()); // Center the card

        initComponents();
    }

    private void initComponents() {
        // --- LOGIN CARD ---
        JPanel card = new RoundedPanel(20, CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(460, 600)); // Wider and taller
        card.setBorder(new EmptyBorder(50, 50, 50, 50)); // More padding

        // 1. Logo Icon (Custom Painted)
        JPanel logoContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoContainer.setOpaque(false);
        logoContainer.add(new LogoPanel());
        // For BoxLayout stability, consistent MaxSize / Alignment
        logoContainer.setMaximumSize(new Dimension(460, 150));
        logoContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logoContainer);

        card.add(Box.createVerticalStrut(20));

        // 2. Title
        JLabel lblTitle = new JLabel("CineMe");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(TXT_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);

        card.add(Box.createVerticalStrut(5));

        // 3. Subtitle
        JLabel lblSub = new JLabel("Secure Admin Portal Access");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(TXT_SECONDARY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSub);

        card.add(Box.createVerticalStrut(40));

        // 4. Username Section (Wrapper for Left Alignment)
        JPanel pUserLabel = new JPanel(new BorderLayout());
        pUserLabel.setBackground(CARD_BG);
        pUserLabel.setMaximumSize(new Dimension(420, 20)); // Match input width
        pUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself
        JLabel lblUser = createLabel("Username");
        pUserLabel.add(lblUser, BorderLayout.WEST);
        card.add(pUserLabel);

        card.add(Box.createVerticalStrut(8));

        txtUsername = createTextField();
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(txtUsername);

        card.add(Box.createVerticalStrut(20));

        // 5. Password Section (Wrapper for Left/Right Alignment)
        JPanel pPassHeader = new JPanel(new BorderLayout());
        pPassHeader.setBackground(CARD_BG);
        pPassHeader.setMaximumSize(new Dimension(420, 20));
        pPassHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPass = createLabel("Password");
        JLabel lblForgot = new JLabel("Forgot Password?");
        lblForgot.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblForgot.setForeground(ACCENT_RED);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleForgotPassword();
            }
        });

        pPassHeader.add(lblPass, BorderLayout.WEST);
        pPassHeader.add(lblForgot, BorderLayout.EAST);
        card.add(pPassHeader);

        card.add(Box.createVerticalStrut(8));

        txtPassword = createPasswordField();
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(txtPassword);

        card.add(Box.createVerticalStrut(40));

        // 6. Login Button (Full Width)
        btnLogin = new JButton("Login  →");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(ACCENT_RED);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setMaximumSize(new Dimension(420, 50)); // Full Card Width
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> {
            // Disable button to prevent double-click
            btnLogin.setEnabled(false);
            checkLogin();
        });

        // Hover Effect
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(ACCENT_RED.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(ACCENT_RED);
            }
        });

        card.add(btnLogin);

        card.add(Box.createVerticalGlue());

        add(card); // Add card to GridBagLayout (Center)

        // Config Toggle (Bottom Right Corner)
        addConfigButton(card);

        // Enable Enter Key to Trigger Login
        getRootPane().setDefaultButton(btnLogin);
        txtUsername.addActionListener(e -> {
            btnLogin.setEnabled(false);
            checkLogin();
        });
        txtPassword.addActionListener(e -> {
            btnLogin.setEnabled(false);
            checkLogin();
        });
    }

    private void handleForgotPassword() {
        String username = JOptionPane.showInputDialog(this, "Enter your Username:");
        if (username == null || username.trim().isEmpty())
            return;

        // Security Check: Verify Employee ID
        String idStr = JOptionPane.showInputDialog(this, "Security Check: Enter your Employee ID:");
        if (idStr == null || idStr.trim().isEmpty())
            return;

        try {
            int maNV = Integer.parseInt(idStr.trim());

            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ? AND MaNV = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, username);
            pstm.setInt(2, maNV);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                // Identity Verified
                JPasswordField pf = new JPasswordField();
                int action = JOptionPane.showConfirmDialog(this, pf, "Identity Verified! Enter New Password:",
                        JOptionPane.OK_CANCEL_OPTION);

                if (action == JOptionPane.OK_OPTION) {
                    String newPass = new String(pf.getPassword());
                    if (newPass != null && !newPass.trim().isEmpty()) {
                        if (newPass.length() < 6) {
                            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String updateSql = "UPDATE NhanVien SET MatKhau = ? WHERE TaiKhoan = ?";
                        PreparedStatement updatePstm = conn.prepareStatement(updateSql);
                        updatePstm.setString(1, newPass);
                        updatePstm.setString(2, username);
                        updatePstm.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Password reset successfully! Please login.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Username or Employee ID!", "Verification Failed",
                        JOptionPane.ERROR_MESSAGE);

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Employee ID must be a number!", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // --- HELPER METHODS ---

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(TXT_PRIMARY);
        return l; // Alignment handled by parent panel
    }

    private JTextField createTextField() {
        JTextField t = new JTextField();
        styleField(t);
        return t;
    }

    private JPasswordField createPasswordField() {
        JPasswordField t = new JPasswordField();
        styleField(t);
        return t;
    }

    private void styleField(JTextField t) {
        t.setBackground(INPUT_BG);
        t.setForeground(TXT_PRIMARY);
        t.setCaretColor(TXT_PRIMARY);
        t.setFont(new Font("SansSerif", Font.PLAIN, 15));
        t.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1),
                new EmptyBorder(12, 12, 12, 12) // More internal padding
        ));
        t.setMaximumSize(new Dimension(420, 45)); // Taller input
    }

    private void checkLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            btnLogin.setEnabled(true);
            return;
        }

        // Use UIUtils for Async Login
        com.cinema.util.UIUtils.runAsync(this, () -> {
            // Background Task
            // (Removed buggy empty try block comments)

            // Actually, I must fix the checking logic to not close the connection.
            // Let's rewrite this block safely.

            // 1. Get Connection (don't close it)
            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                // Return a special flag or handle in UI, but here we just return null to
                // indicate failure
                return null;
            }
            String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ? AND MatKhau = ?";
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, user);
                pstm.setString(2, pass);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        NhanVien nv = new NhanVien();
                        nv.setMaNV(rs.getInt("MaNV"));
                        nv.setHoTen(rs.getString("HoTen"));
                        nv.setChucVu(rs.getString("ChucVu"));
                        return nv; // Success
                    }
                }
            }
            return null; // Failure

        }, (result) -> {
            // On Success (EDT)
            btnLogin.setEnabled(true);
            if (result != null) {
                dispose();
                new MainFrame(result).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Username or Password!", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // --- CUSTOM COMPONENTS ---

    // Image Background
    class GradientPanel extends JPanel {
        private Image bgImage;

        public GradientPanel() {
            try {
                bgImage = new ImageIcon("resources/login_bg.jpg").getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

                // Optional: Dark overlay for better contrast if needed,
                // but the card has its own background so we might just do a light tint or none.
                // Let's add a very subtle dark tint to unify it.
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                // Fallback to gradient if image fails
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, BG_GRADIENT_START, w, h, BG_GRADIENT_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        }
    }

    private void addConfigButton(JPanel parent) {
        com.cinema.db.DBConnection.DBMode current = com.cinema.db.DBConnection.getCurrentMode();
        JButton btnConfig = new JButton("DB Mode: " + current);
        btnConfig.setFont(new Font("Monospaced", Font.BOLD, 12));
        btnConfig.setForeground(Color.GRAY);
        btnConfig.setContentAreaFilled(false);
        btnConfig.setBorderPainted(false);
        btnConfig.setFocusPainted(false);
        btnConfig.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfig.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnConfig.addActionListener(e -> {
            com.cinema.db.DBConnection.DBMode mode = com.cinema.db.DBConnection.getCurrentMode();
            com.cinema.db.DBConnection.DBMode newMode = (mode == com.cinema.db.DBConnection.DBMode.CLOUD)
                    ? com.cinema.db.DBConnection.DBMode.LOCAL
                    : com.cinema.db.DBConnection.DBMode.CLOUD;

            com.cinema.db.DBConnection.switchMode(newMode);
            btnConfig.setText("DB Mode: " + newMode);

            if (newMode == com.cinema.db.DBConnection.DBMode.LOCAL) {
                btnConfig.setForeground(new Color(0, 180, 0));
            } else {
                btnConfig.setForeground(Color.GRAY);
            }
        });

        // Add spacer and button directly to the card
        parent.add(Box.createVerticalStrut(10));
        parent.add(btnConfig);
    }

    // Rounded Card Panel
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    // Custom Logo Panel
    class LogoPanel extends JPanel {
        public LogoPanel() {
            setPreferredSize(new Dimension(80, 80)); // Increased size for image
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Fix paint artifact
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            try {
                ImageIcon icon = new ImageIcon("resources/logo.png");
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g2.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Fallback to red circle if image missing
                    g2.setColor(Color.RED);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
