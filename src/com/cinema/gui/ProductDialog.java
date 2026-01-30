package com.cinema.gui;

import com.cinema.dto.SanPham;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductDialog extends JDialog {

  private boolean succeeded;
  private JTextField txtName, txtPrice, txtStock, txtImage, txtDesc;
  private JComboBox<String> cbType;
  private SanPham product;

  public ProductDialog(Frame parent, SanPham product) {
    super(parent, product == null ? "Add Product" : "Edit Product", true);
    this.product = product;
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout());
    JPanel pForm = new JPanel(new GridLayout(6, 2, 10, 10));
    pForm.setBorder(new EmptyBorder(20, 20, 20, 20));

    pForm.add(new JLabel("Product Name:"));
    txtName = new JTextField(20);
    pForm.add(txtName);

    pForm.add(new JLabel("Type:"));
    cbType = new JComboBox<>(new String[] { "DoAn", "Nuoc", "Combo", "Keo" }); // Added Keo
    pForm.add(cbType);

    pForm.add(new JLabel("Price (VNĐ):"));
    txtPrice = new JTextField();
    pForm.add(txtPrice);

    pForm.add(new JLabel("Stock:"));
    txtStock = new JTextField();
    pForm.add(txtStock);

    pForm.add(new JLabel("Image Path:"));

    // Image Upload Panel
    JPanel pImageUpload = new JPanel(new BorderLayout(5, 0));
    txtImage = new JTextField("default_product.png");
    pImageUpload.add(txtImage, BorderLayout.CENTER);

    JButton btnBrowse = new JButton("...");
    btnBrowse.setPreferredSize(new Dimension(30, 20));
    btnBrowse.addActionListener(e -> chooseImage());
    pImageUpload.add(btnBrowse, BorderLayout.EAST);

    pForm.add(pImageUpload);

    pForm.add(new JLabel("Description:"));
    txtDesc = new JTextField();
    pForm.add(txtDesc);

    add(pForm, BorderLayout.CENTER);

    // Buttons
    JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    btnSave.addActionListener(e -> {
      if (validateInput()) {
        succeeded = true;
        dispose();
      }
    });

    btnCancel.addActionListener(e -> dispose());

    pBtn.add(btnSave);
    pBtn.add(btnCancel);
    add(pBtn, BorderLayout.SOUTH);

    // Pre-fill if editing
    if (product != null) {
      txtName.setText(product.getTenSP());
      cbType.setSelectedItem(product.getLoaiSP());
      txtPrice.setText(String.valueOf((int) product.getGiaBan()));
      txtStock.setText(String.valueOf(product.getSoLuongTon()));
      txtImage.setText(product.getImageURL());
      txtDesc.setText(product.getMoTa());
    }

    pack();
    setLocationRelativeTo(getParent());
  }

  private boolean validateInput() {
    if (txtName.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Name is required!");
      return false;
    }
    try {
      Double.parseDouble(txtPrice.getText());
      Integer.parseInt(txtStock.getText());
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid Price or Stock!");
      return false;
    }
    return true;
  }

  private void chooseImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Product Image");
    fileChooser
        .setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter("Images (JPG, PNG, GIF)", "jpg", "png", "jpeg", "gif"));

    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      java.io.File selectedFile = fileChooser.getSelectedFile();

      // Define destination directory (create if not exists)
      java.io.File destDir = new java.io.File("images");
      if (!destDir.exists()) {
        destDir.mkdirs();
      }

      // Generate unique filename to avoid overwrites
      String newFilename = System.currentTimeMillis() + "_" + selectedFile.getName();
      java.io.File destFile = new java.io.File(destDir, newFilename);

      try {
        // Java 7+ method to copy file
        java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(),
            java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Set the relative path to be saved in DB
        txtImage.setText("images/" + newFilename);
      } catch (java.io.IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to upload image: " + ex.getMessage());
      }
    }
  }

  public boolean isSucceeded() {
    return succeeded;
  }

  public SanPham getProduct() {
    int id = (product == null) ? 0 : product.getMaSP();
    return new SanPham(
        id,
        txtName.getText().trim(),
        cbType.getSelectedItem().toString(),
        Double.parseDouble(txtPrice.getText()),
        Integer.parseInt(txtStock.getText()),
        txtImage.getText().trim(),
        txtDesc.getText().trim());
  }
}
