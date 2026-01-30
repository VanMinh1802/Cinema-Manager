package com.cinema.gui;

import com.cinema.dto.NhanVien;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class StatsPanel extends JPanel {

    private NhanVien nhanVien;

    // UI Components
    private GlowLabel lblTime;
    private JLabel lblDate;
    private JLabel lblGreeting;
    private JLabel lblQuote;
    private JPanel centerPanel;

    // Logic
    private Timer updateTimer;
    private Point mousePt = new Point(0, 0);
    private long startTime;

    // Effects
    private List<Star> stars;
    private List<Nebula> nebulas;
    private List<Planet> planets; // NEW: Planets
    private ShootingStar shootingStar;
    private MouseTrail mouseTrail;
    private List<HUDRing> hudRings;

    // Typewriter
    private String targetQuote = "";
    private StringBuilder currentQuote = new StringBuilder();
    private int quoteCharIndex = 0;
    private int quoteDelay = 0;

    private static final String[] QUOTES = {
            "\"May the Force be with you.\" - Star Wars",
            "\"Here's looking at you, kid.\" - Casablanca",
            "\"To infinity and beyond!\" - Toy Story",
            "\"I'm going to make him an offer he can't refuse.\" - The Godfather",
            "\"Life is like a box of chocolates.\" - Forrest Gump",
            "\"Why so serious?\" - The Dark Knight",
            "\"I am Iron Man.\" - Avengers: Endgame",
            "\"Winter is coming.\" - Game of Thrones",
            "\"Whatever it takes.\" - Avengers: Endgame",
            "\"Do or do not. There is no try.\" - Yoda"
    };

    public StatsPanel(NhanVien nv) {
        this.nhanVien = nv;
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#050505"));
        startTime = System.currentTimeMillis();

        // 1. Initial Data
        initParticles();
        targetQuote = QUOTES[new Random().nextInt(QUOTES.length)];

        // 2. Interaction
        MouseAdapter ma = new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                mousePt = e.getPoint();
                mouseTrail.addPoint(e.getX(), e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                mousePt = e.getPoint();
                mouseTrail.addPoint(e.getX(), e.getY());
            }
        };
        addMouseMotionListener(ma);
        addMouseListener(ma);

        // 3. UI Layout
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Time with Glow
        lblTime = new GlowLabel("00:00", SwingConstants.CENTER);
        lblTime.setForeground(Color.WHITE);
        lblTime.setGlowColor(new Color(100, 200, 255, 40));
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        setFontForTime();

        // Date
        lblDate = new JLabel("DATE PLACEHOLDER", SwingConstants.CENTER);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDate.setForeground(new Color(180, 180, 180));
        lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Greeting
        lblGreeting = new JLabel("Welcome", SwingConstants.CENTER);
        lblGreeting.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
        lblGreeting.setForeground(Color.WHITE);
        lblGreeting.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Quote
        lblQuote = new JLabel("|", SwingConstants.CENTER);
        lblQuote.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblQuote.setForeground(new Color(255, 215, 0));
        lblQuote.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblQuote.setPreferredSize(new Dimension(800, 30));

        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(lblTime);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblDate);
        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(lblGreeting);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(lblQuote);

        add(centerPanel);

        // 4. Game Loop
        updateTimer = new Timer(16, e -> gameLoop());
        updateTimer.start();

        updateTextData();
    }

    private void setFontForTime() {
        Font f = new Font("Segoe UI Light", Font.PLAIN, 130);
        if (f.canDisplay('0')) {
            Map<TextAttribute, Object> attr = (Map<TextAttribute, Object>) f.getAttributes();
            attr.put(TextAttribute.TRACKING, 0.05);
            lblTime.setFont(f.deriveFont(attr));
        } else {
            lblTime.setFont(new Font("SansSerif", Font.PLAIN, 120));
        }
    }

    private void initParticles() {
        Random r = new Random();
        int w = 1920;
        int h = 1080;

        // Stars/Nebulas
        stars = new ArrayList<>();
        for (int i = 0; i < 300; i++)
            stars.add(new Star(r.nextInt(w), r.nextInt(h), r.nextFloat() * 2 + 0.5f));

        nebulas = new ArrayList<>();
        nebulas.add(new Nebula(300, 300, 800, new Color(40, 0, 80, 30)));
        nebulas.add(new Nebula(1600, 700, 900, new Color(0, 30, 70, 30)));

        // Planets (Mini Universe - Procedural Generation)
        planets = new ArrayList<>();
        // Themes: [MainColor, ShadowColor]
        Color[][] themes = {
                { new Color(255, 80, 50), new Color(100, 20, 10) }, // Lava/Mars
                { new Color(0, 100, 255), new Color(0, 20, 60) }, // Ice/Water
                { new Color(0, 200, 100), new Color(0, 50, 20) }, // Terran/Forest
                { new Color(200, 140, 80), new Color(50, 30, 10) }, // Gas Giant (Jupiter)
                { new Color(150, 100, 255), new Color(40, 10, 80) }, // Alien/Purple
                { new Color(200, 200, 200), new Color(50, 50, 50) } // Moon/rock
        };

        // Generate 15 planets scattered
        for (int i = 0; i < 15; i++) {
            int themeIdx = r.nextInt(themes.length);
            float z = 0.2f + r.nextFloat() * 1.5f; // Depth
            float size = (30 + r.nextInt(150)) * z; // Size depends on depth perspective
            float x = r.nextInt(w);
            float y = r.nextInt(h);

            // Avoid center area (where text is)
            if (Math.abs(x - w / 2) < 400 && Math.abs(y - h / 2) < 200) {
                x += (x < w / 2) ? -400 : 400;
            }

            planets.add(new Planet(x, y, size, themes[themeIdx][0], themes[themeIdx][1], z));
        }

        shootingStar = new ShootingStar();
        mouseTrail = new MouseTrail();

        hudRings = new ArrayList<>();
        hudRings.add(new HUDRing(280, 1.0f, 0.005f, true, new Color(100, 200, 255, 30)));
        hudRings.add(new HUDRing(320, 2.0f, -0.003f, false, new Color(100, 200, 255, 20)));
        hudRings.add(new HUDRing(260, 5.0f, 0.008f, true, new Color(100, 200, 255, 40)));
    }

    private void gameLoop() {
        long nowMs = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        // 1. Entrance Fade
        long age = nowMs - startTime;
        float entranceProgress = Math.min(1.0f, age / 1500.0f);
        entranceProgress = 1 - (1 - entranceProgress) * (1 - entranceProgress) * (1 - entranceProgress);

        int alpha = (int) (entranceProgress * 255);
        Color baseWhite = new Color(255, 255, 255, alpha);
        Color baseGrey = new Color(180, 180, 180, alpha);
        Color baseGold = new Color(255, 215, 0, alpha);
        Color baseGlow = new Color(100, 200, 255, (int) (40 * entranceProgress));

        if (age < 2000) {
            lblTime.setForeground(baseWhite);
            lblTime.setGlowColor(baseGlow);
            lblDate.setForeground(baseGrey);
            lblQuote.setForeground(baseGold);
            lblGreeting.setForeground(baseWhite);
        }

        // 2. Typewriter
        if (quoteDelay > 0)
            quoteDelay--;
        else if (quoteCharIndex < targetQuote.length()) {
            currentQuote.append(targetQuote.charAt(quoteCharIndex++));
            lblQuote.setText(currentQuote.toString() + " |");
            quoteDelay = 2;
        } else {
            if ((nowMs / 500) % 2 == 0)
                lblQuote.setText(currentQuote.toString() + " |");
            else
                lblQuote.setText(currentQuote.toString() + "  ");
            if (now.getSecond() % 20 == 0 && now.getNano() < 50000000)
                changeQuote();
        }

        // 3. Time Data
        if (getComponentCount() > 0) {
            String fullTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            if (!lblTime.getText().equals(fullTime))
                lblTime.setText(fullTime);
            if (now.getSecond() == 0)
                updateTextData();
        }

        // 4. Effects
        int w = getWidth();
        int h = getHeight();
        shootingStar.update(w, h);
        mouseTrail.update();
        for (HUDRing r : hudRings)
            r.update();

        repaint();
    }

    private void changeQuote() {
        targetQuote = QUOTES[new Random().nextInt(QUOTES.length)];
        currentQuote.setLength(0);
        quoteCharIndex = 0;
        quoteDelay = 0;
    }

    private void updateTextData() {
        LocalDateTime now = LocalDateTime.now();
        lblDate.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH)).toUpperCase());

        int h = now.getHour();
        String gr = "Good Morning";
        if (h >= 12 && h < 17)
            gr = "Good Afternoon";
        else if (h >= 17)
            gr = "Good Evening";

        String name = (nhanVien != null) ? nhanVien.getHoTen() : "User";
        lblGreeting.setText("<html>" + gr + ", <font color='#64b5f6'><b>" + name + "</b></font></html>");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        float pX = (mousePt.x - w / 2) * 0.02f;
        float pY = (mousePt.y - h / 2) * 0.02f;

        // 1. Nebulas
        long time = System.currentTimeMillis();
        for (Nebula n : nebulas) {
            double wx = Math.sin(time * 0.0005 + n.offset) * 50;
            double wy = Math.cos(time * 0.0007 + n.offset) * 30;
            float cx = (float) (n.x + wx - pX * 0.5);
            float cy = (float) (n.y + wy - pY * 0.5);

            float[] dist = { 0.0f, 1.0f };
            Color[] colors = { n.color, new Color(0, 0, 0, 0) };
            if (n.size > 0) {
                RadialGradientPaint paint = new RadialGradientPaint(new Point2D.Float(cx, cy), n.size, dist, colors,
                        MultipleGradientPaint.CycleMethod.NO_CYCLE);
                g2.setPaint(paint);
                g2.fillRect(0, 0, w, h);
            }
        }

        // 2. Stars
        for (Star s : stars) {
            float moveX = s.baseX - pX * s.z;
            float moveY = s.baseY - pY * s.z;
            float size = Math.max(1, s.z * 1.5f);
            int alpha = (int) (100 + s.z * 60);
            if (alpha > 255)
                alpha = 255;
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fillOval((int) moveX, (int) moveY, (int) size, (int) size);
        }

        // 3. Planets (NEW - Draw after stars to be "in front" but behind HUD)
        for (Planet p : planets) {
            p.draw(g2, pX, pY);
        }

        // 4. HUD Rings (Behind Time)
        int cx = w / 2;
        int cy = h / 2 - 50;
        for (HUDRing r : hudRings) {
            r.draw(g2, cx, cy);
        }

        // 5. Shooting Star
        if (shootingStar.active)
            shootingStar.draw(g2);

        // 6. Mouse Trail
        mouseTrail.draw(g2);

        // 7. Letterbox
        g2.setColor(Color.BLACK);
        int barHeight = (int) (h * 0.05);
        g2.fillRect(0, 0, w, barHeight);
        g2.fillRect(0, h - barHeight, w, barHeight);

        // HUD Text
        g2.setColor(new Color(100, 200, 255, 150));
        g2.setFont(new Font("Consolas", Font.PLAIN, 10));
        g2.drawString("SYS.STATUS: NOMINAL", 20, barHeight - 10);
        g2.drawString("MEM: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024
                + " MB", w - 150, barHeight - 10);
        g2.drawString("SESSION: " + (nhanVien != null ? nhanVien.getMaNV() : "GUEST"), 20, h - 10);
        g2.drawString("CONNECTED: LOCALHOST", w - 150, h - 10);
    }

    // --- classes ---

    private class Star {
        float baseX, baseY, z;

        Star(float x, float y, float z) {
            this.baseX = x;
            this.baseY = y;
            this.z = z;
        }
    }

    private class Nebula {
        float x, y, size, offset;
        Color color;

        Nebula(float x, float y, float s, Color c) {
            this.x = x;
            this.y = y;
            this.size = s;
            this.color = c;
            this.offset = new Random().nextFloat() * 100;
        }
    }

    // NEW: Planet Class
    private class Planet {
        float x, y, size, z;
        Color colorMain, colorShadow;

        Planet(float x, float y, float size, Color main, Color shadow, float z) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.colorMain = main;
            this.colorShadow = shadow;
            this.z = z;
        }

        void draw(Graphics2D g2, float dx, float dy) {
            float drawX = x - dx * z * 2; // Move faster if closer (z higher)
            float drawY = y - dy * z * 2;

            // 3D Shading using RadialGradient (Light source top-left offset)

            float[] dist = { 0.0f, 1.0f };
            Color[] colors = { colorMain, colorShadow };



            // Simpler paint since focus center requires complex constructor
            // Let's use standard constructor
            RadialGradientPaint p2 = new RadialGradientPaint(
                    new Point2D.Float(drawX + size / 2, drawY + size / 2), // Center of planet
                    size / 2, // Radius
                    new Point2D.Float(drawX + size / 3, drawY + size / 3), // Focus of light (Top Left)
                    dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);

            g2.setPaint(p2);
            g2.fillOval((int) drawX, (int) drawY, (int) size, (int) size);
        }
    }

    private class GlowLabel extends JLabel {
        private Color glowColor = Color.WHITE;

        public GlowLabel(String t, int a) {
            super(t, a);
        }

        public void setGlowColor(Color c) {
            this.glowColor = c;
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            String text = getText();
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(glowColor);
            for (int i = -2; i <= 2; i++)
                for (int j = -2; j <= 2; j++)
                    if (i != 0 || j != 0)
                        g2.drawString(text, x + i, y + j);
            g2.setColor(getForeground());
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }

    private class ShootingStar {
        float x, y, vx, vy;
        int length = 0;
        boolean active = false;
        Random r = new Random();

        void update(int w, int h) {
            if (!active) {
                if (r.nextFloat() < 0.04f) { // Increased frequency (was 0.008f)
                    active = true;
                    x = r.nextInt(w);
                    y = r.nextInt(h / 3);
                    vx = -15 - r.nextFloat() * 10;
                    vy = 8 + r.nextFloat() * 5;
                    length = 0;
                }
            } else {
                x += vx;
                y += vy;
                if (length < 200)
                    length += 10;
                if (x < -300 || y > h + 300)
                    active = false;
            }
        }

        void draw(Graphics2D g2) {
            if (!active)
                return;
            GradientPaint trail = new GradientPaint(x, y, new Color(255, 255, 255, 200), x - vx * 3, y - vy * 3,
                    new Color(255, 255, 255, 0));
            g2.setPaint(trail);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine((int) x, (int) y, (int) (x - vx * 3), (int) (y - vy * 3));
            g2.setColor(Color.WHITE);
            g2.fillOval((int) x, (int) y, 5, 5);
        }
    }

    private class HUDRing {
        float radius;
        float angle = 0;
        float speed;
        boolean solid;
        Color color;

        HUDRing(float r, float stroke, float spd, boolean solid, Color c) {
            this.radius = r;
            this.speed = spd;
            this.solid = solid;
            this.color = c;
        }

        void update() {
            angle += speed;
        }

        void draw(Graphics2D g2, int cx, int cy) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1));
            AffineTransform old = g2.getTransform();
            g2.rotate(angle, cx, cy);
            int r = (int) radius;
            if (solid)
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            else {
                int dashLen = 20;
                for (int i = 0; i < 360; i += 30)
                    g2.drawArc(cx - r, cy - r, r * 2, r * 2, i, dashLen);
            }
            g2.fillOval(cx - r - 2, cy - 2, 4, 4);
            g2.fillOval(cx + r - 2, cy - 2, 4, 4);
            g2.setTransform(old);
        }
    }

    private class MouseTrail {
        class Particle {
            float x, y, vx, vy, life, size;
            Color color;

            Particle(float x, float y) {
                this.x = x;
                this.y = y;
                this.life = 1.0f;
                Random r = new Random();
                this.vx = (r.nextFloat() - 0.5f) * 2;
                this.vy = (r.nextFloat() - 0.5f) * 2 + 1;
                this.size = r.nextInt(4) + 2;
                if (r.nextBoolean())
                    this.color = new Color(200, 230, 255);
                else
                    this.color = new Color(255, 255, 200);
            }
        }

        List<Particle> particles = new ArrayList<>();

        void addPoint(float x, float y) {
            for (int i = 0; i < 3; i++)
                particles.add(new Particle(x, y));
        }

        void update() {
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.x += p.vx;
                p.y += p.vy;
                p.life -= 0.02f;
                if (p.life <= 0)
                    particles.remove(i);
            }
        }

        void draw(Graphics2D g2) {
            for (Particle p : particles) {
                int alpha = (int) (255 * p.life);
                if (alpha < 0)
                    alpha = 0;
                if (alpha > 255)
                    alpha = 255;
                g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), alpha));
                g2.fillOval((int) p.x, (int) p.y, (int) p.size, (int) p.size);
            }
        }
    }
}
