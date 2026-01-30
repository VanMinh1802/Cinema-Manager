package com.cinema.gui;

import com.cinema.dao.KhachHangDAO;
import com.cinema.dto.KhachHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Date;

public class AddMemberDialog extends JDialog {
  private JTextField txtName;
  private JTextField txtPhone;
  private JTextField txtEmail;
  private JSpinner dateSpinner;
  private boolean saved = false;
  private KhachHang newMember = null;

  private Color BG_DIALOG = new Color(50, 50, 55);
  private Color TXT_COLOR = Color.WHITE;
  private Color ACCENT_COLOR = Color.decode("#D93636");

  public AddMemberDialog(Window owner) {
    super(owner, "Add New Loyalty Member", ModalityType.APPLICATION_MODAL);
    initComponents();
    setSize(450, 450);
    setLocationRelativeTo(owner);
  }

  public AddMemberDialog(Window owner, String phone) {
    this(owner);
    if (phone != null)
      txtPhone.setText(phone);
  }

  private void initComponents() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(BG_DIALOG);
    p.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Header
    JLabel lblTitle = new JLabel("Add New Loyalty Member");
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
    lblTitle.setForeground(TXT_COLOR);

    JLabel lblSub = new JLabel(
        "<html><body style='width: 300px'>Enter customer details to create a new loyalty account.</body></html>");
    lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lblSub.setForeground(Color.GRAY);

    JPanel pHeader = new JPanel(new BorderLayout());
    pHeader.setOpaque(false);
    pHeader.add(lblTitle, BorderLayout.NORTH);
    pHeader.add(lblSub, BorderLayout.CENTER);

    // Form
    JPanel pForm = new JPanel(new GridLayout(0, 1, 0, 15));
    pForm.setOpaque(false);
    pForm.setBorder(new EmptyBorder(20, 0, 20, 0));

    pForm.add(createField("Customer Name", txtName = new JTextField()));
    pForm.add(createField("Phone Number", txtPhone = new JTextField()));
    pForm.add(createField("Email Address", txtEmail = new JTextField()));

    // Date Spinner
    JPanel pDate = new JPanel(new BorderLayout(0, 5));
    pDate.setOpaque(false);
    JLabel lDate = new JLabel("Date of Birth");
    lDate.setForeground(Color.LIGHT_GRAY);
    dateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor de = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
    dateSpinner.setEditor(de);
    // Style spinner
    ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setBackground(new Color(60, 60, 65));
    ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setForeground(Color.WHITE);
    pDate.add(lDate, BorderLayout.NORTH);
    pDate.add(dateSpinner, BorderLayout.CENTER);
    pForm.add(pDate);

    // Buttons
    JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pBtn.setOpaque(false);

    JButton btnCancel = new JButton("Cancel");
    styleButton(btnCancel, new Color(80, 80, 80));
    btnCancel.addActionListener(e -> dispose());

    JButton btnSave = new JButton("Save Member");
    styleButton(btnSave, ACCENT_COLOR);
    btnSave.addActionListener(e -> save());

    pBtn.add(btnCancel);
    pBtn.add(btnSave);

    p.add(pHeader, BorderLayout.NORTH);
    p.add(pForm, BorderLayout.CENTER);
    p.add(pBtn, BorderLayout.SOUTH);

    setContentPane(p);
  }

  private JPanel createField(String label, JTextField txt) {
    JPanel p = new JPanel(new BorderLayout(0, 5));
    p.setOpaque(false);
    JLabel l = new JLabel(label);
    l.setForeground(Color.LIGHT_GRAY);
    l.setFont(new Font("SansSerif", Font.BOLD, 12));

    txt.setBackground(new Color(60, 60, 65));
    txt.setForeground(Color.WHITE);
    txt.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(new Color(80, 80, 80)),
        new EmptyBorder(8, 10, 8, 10)));

    p.add(l, BorderLayout.NORTH);
    p.add(txt, BorderLayout.CENTER);
    return p;
  }

  private void styleButton(JButton b, Color bg) {
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    b.setBorder(new EmptyBorder(8, 15, 8, 15));
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    b.setFont(new Font("SansSerif", Font.BOLD, 12));
  }

  private void save() {
    if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Name and Phone are required.");
      return;
    }

    KhachHang kh = new KhachHang();
    kh.setHoTen(txtName.getText().trim());
    kh.setSdt(txtPhone.getText().trim());
    kh.setEmail(txtEmail.getText().trim());

    java.util.Date d = (java.util.Date) dateSpinner.getValue();
    kh.setNgaySinh(new Date(d.getTime()));

    KhachHangDAO dao = new KhachHangDAO();
    int id = dao.insertKhachHang(kh);

    if (id != -1) {
      kh.setMaKH(id);
      newMember = kh;
      saved = true;
      dispose();
    } else {
      JOptionPane.showMessageDialog(this, "Failed to save member. Phone might be duplicate.");
    }
  }

  public KhachHang getNewMember() {
    return saved ? newMember : null;
  }
}
