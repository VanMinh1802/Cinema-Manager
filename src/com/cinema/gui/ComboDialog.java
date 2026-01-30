package com.cinema.gui;

import com.cinema.dao.ComboDAO;
import com.cinema.dao.SanPhamDAO;
import com.cinema.dto.SanPham;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class ComboDialog extends JDialog {

  private JTextField txtName, txtPrice, txtDesc;
  private JLabel lblImagePreview;
  private String selectedImagePath = "default_combo.png";
  private boolean succeeded = false;
  private SanPham comboResult = null;

  private JTable itemTable;
  private DefaultTableModel itemModel;
  private JComboBox<SanPham> cboProducts;
  private JSpinner spinQty;

  private SanPhamDAO spDAO = new SanPhamDAO();
  private ComboDAO comboDAO = new ComboDAO();
  private SanPham currentEditingCombo; // Null if adding new

  // Colors
  private static final Color BG_DARK = Color.decode("#121212");
  // private static final Color BG_INPUT = Color.decode("#2A2A2A"); // unused
  // private static final Color TXT_PRIMARY = Color.WHITE; // unused
  private static final Color ACCENT_RED = Color.decode("#D93636");

  public ComboDialog(Frame parent, SanPham combo) {
    super(parent, "Manage Combo Meal", true);
    this.currentEditingCombo = combo;

    initComponents();
    loadProducts();

    if (combo != null) {
      fillData();
    }

    setSize(800, 600);
    setLocationRelativeTo(parent);
  }

  private void initComponents() {
    setLayout(new BorderLayout());
    setBackground(BG_DARK);

    JPanel pMain = new JPanel(new GridLayout(1, 2, 20, 0)); // Split: Left (Info) | Right (Recipe)
    pMain.setBackground(BG_DARK);
    pMain.setBorder(new EmptyBorder(20, 20, 20, 20));

    // --- LEFT: Basic Info ---
    JPanel pLeft = new JPanel();
    pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
    pLeft.setBackground(BG_DARK);

    txtName = createInput("Combo Name");
    txtPrice = createInput("Price (VNĐ)");
    txtDesc = createInput("Description");

    // Image
    lblImagePreview = new JLabel("No Image", SwingConstants.CENTER);
    lblImagePreview.setPreferredSize(new Dimension(150, 150));
    lblImagePreview.setBorder(new LineBorder(Color.GRAY));
    lblImagePreview.setForeground(Color.GRAY);

    JButton btnBrowse = new JButton("Choose Image");
    btnBrowse.addActionListener(e -> chooseImage());

    JPanel pImg = new JPanel(new BorderLayout(0, 10));
    pImg.setBackground(BG_DARK);
    pImg.add(lblImagePreview, BorderLayout.CENTER);
    pImg.add(btnBrowse, BorderLayout.SOUTH);

    pLeft.add(new JLabel("Combo Details"));
    pLeft.add(Box.createVerticalStrut(10));
    pLeft.add(txtName);
    pLeft.add(Box.createVerticalStrut(10));
    pLeft.add(txtPrice);
    pLeft.add(Box.createVerticalStrut(10));
    pLeft.add(txtDesc);
    pLeft.add(Box.createVerticalStrut(20));
    pLeft.add(pImg);

    // --- RIGHT: Recipe Builder ---
    JPanel pRight = new JPanel(new BorderLayout(0, 10));
    pRight.setBackground(BG_DARK);
    pRight.setBorder(BorderFactory.createTitledBorder(
        new LineBorder(Color.GRAY), "Recipe (Items in this Combo)",
        0, 0, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));

    // Add Item Row
    JPanel pAdd = new JPanel(new GridBagLayout());
    pAdd.setBackground(BG_DARK);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    cboProducts = new JComboBox<>();
    cboProducts.setPreferredSize(new Dimension(200, 30));

    spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    spinQty.setPreferredSize(new Dimension(60, 30));

    JButton btnAddItem = new JButton("+ Add");
    btnAddItem.setBackground(new Color(40, 40, 40));
    btnAddItem.setForeground(Color.WHITE);
    btnAddItem.addActionListener(e -> addItemToTable());

    JLabel lblQty = new JLabel("Qty:");
    lblQty.setForeground(Color.WHITE);

    gbc.gridx = 0;
    gbc.weightx = 1.0;
    pAdd.add(cboProducts, gbc);

    gbc.gridx = 1;
    gbc.weightx = 0;
    pAdd.add(lblQty, gbc);

    gbc.gridx = 2;
    pAdd.add(spinQty, gbc);

    gbc.gridx = 3;
    pAdd.add(btnAddItem, gbc);

    // Table
    String[] cols = { "ID", "Item Name", "Qty", "Action" };
    itemModel = new DefaultTableModel(cols, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 3; // Only Action button
      }
    };
    itemTable = new JTable(itemModel);
    // Basic Table Styling (skipped complex renderer for speed, add standard if
    // needed)

    JScrollPane scroll = new JScrollPane(itemTable);

    // Remove Row Button (Need simple click listener for now)
    JButton btnRemoveRow = new JButton("Remove Selected Item");
    btnRemoveRow.addActionListener(e -> {
      int row = itemTable.getSelectedRow();
      if (row != -1)
        itemModel.removeRow(row);
    });

    pRight.add(pAdd, BorderLayout.NORTH);
    pRight.add(scroll, BorderLayout.CENTER);
    pRight.add(btnRemoveRow, BorderLayout.SOUTH);

    pMain.add(pLeft);
    pMain.add(pRight);

    add(pMain, BorderLayout.CENTER);

    // --- BOTTOM: Actions ---
    JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pBottom.setBackground(BG_DARK);

    JButton btnSave = new JButton("Save Combo");
    btnSave.setBackground(ACCENT_RED);
    btnSave.setForeground(Color.WHITE);
    btnSave.addActionListener(e -> save());

    JButton btnCancel = new JButton("Cancel");
    btnCancel.addActionListener(e -> dispose());

    pBottom.add(btnCancel);
    pBottom.add(btnSave);
    add(pBottom, BorderLayout.SOUTH);
  }

  private JTextField createInput(String title) {
    JTextField tf = new JTextField();
    tf.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), title));
    return tf;
  }

  private void loadProducts() {
    // Load only NON-Combo products to avoid cycles
    List<SanPham> list = spDAO.getAllSanPham();
    for (SanPham sp : list) {
      if (!"Combo".equalsIgnoreCase(sp.getLoaiSP())) {
        cboProducts.addItem(sp); // toString() of SanPham uses just address? Need override or renderer
      }
    }
    // Custom Renderer for ComboBox
    cboProducts.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
          boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof SanPham) {
          setText(((SanPham) value).getTenSP());
        }
        return this;
      }
    });
  }

  private void addItemToTable() {
    SanPham sp = (SanPham) cboProducts.getSelectedItem();
    int qty = (int) spinQty.getValue();
    if (sp == null)
      return;

    // Check duplicate
    for (int i = 0; i < itemModel.getRowCount(); i++) {
      if (Integer.parseInt(itemModel.getValueAt(i, 0).toString()) == sp.getMaSP()) {
        // Update qty
        int oldQ = Integer.parseInt(itemModel.getValueAt(i, 2).toString());
        itemModel.setValueAt(oldQ + qty, i, 2);
        return;
      }
    }

    itemModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), qty, "Remove" });
  }

  private void chooseImage() {
    JFileChooser fc = new JFileChooser();
    javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
        "Images (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
    fc.setFileFilter(filter);

    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = fc.getSelectedFile();
      // Use timestamp to avoid collisions strings
      String newFilename = System.currentTimeMillis() + "_" + f.getName();
      File dest = new File("images/" + newFilename);

      try {
        if (!new File("images").exists())
          new File("images").mkdirs();

        Files.copy(f.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Fix: Save with directory prefix
        selectedImagePath = "images/" + newFilename;

        lblImagePreview.setText("");
        lblImagePreview.setIcon(new ImageIcon(
            new ImageIcon(dest.getAbsolutePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error uploading image: " + e.getMessage());
      }
    }
  }

  private void fillData() {
    txtName.setText(currentEditingCombo.getTenSP());
    txtPrice.setText(String.valueOf((int) currentEditingCombo.getGiaBan()));
    txtDesc.setText(currentEditingCombo.getMoTa());
    selectedImagePath = currentEditingCombo.getImageURL();
    // Load Recipe
    Map<Integer, Integer> recipe = comboDAO.getRecipe(currentEditingCombo.getMaSP());
    for (Map.Entry<Integer, Integer> entry : recipe.entrySet()) {
      // Find name? Need helper or cache.
      // Minimal: select name from DB or filter loaded combo box
      SanPham sp = findProductById(entry.getKey());
      if (sp != null) {
        itemModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), entry.getValue(), "Remove" });
      }
    }
  }

  private SanPham findProductById(int id) {
    for (int i = 0; i < cboProducts.getItemCount(); i++) {
      SanPham sp = cboProducts.getItemAt(i);
      if (sp.getMaSP() == id)
        return sp;
    }
    return null;
  }

  private void save() {
    try {
      String name = txtName.getText();
      double price = Double.parseDouble(txtPrice.getText());
      String desc = txtDesc.getText();

      if (currentEditingCombo == null) {
        // ADD NEW
        comboResult = new SanPham(0, name, "Combo", price, 0, selectedImagePath, desc); // Stock 0 for combos? Or
                                                                                        // infinite?
        // Stock for combo usually virtual, let's put 100 or ignore.
        comboResult.setSoLuongTon(100);

        spDAO.addSanPham(comboResult);

        // Get ID (Assuming last ID or we need to fetch)
        // Need method to get Last ID or re-query by Name.
        // For safety:
        List<SanPham> all = spDAO.getAllSanPham();
        int newID = all.get(all.size() - 1).getMaSP(); // Risky but works for prototype

        saveRecipe(newID);

      } else {
        // UPDATE
        currentEditingCombo.setTenSP(name);
        currentEditingCombo.setGiaBan(price);
        currentEditingCombo.setMoTa(desc);
        currentEditingCombo.setImageURL(selectedImagePath);
        spDAO.updateSanPham(currentEditingCombo);

        // Clear old recipe and save new
        comboDAO.clearRecipe(currentEditingCombo.getMaSP());
        saveRecipe(currentEditingCombo.getMaSP());
      }
      succeeded = true;
      dispose();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage());
    }
  }

  private void saveRecipe(int maCombo) {
    // First clear if we had logic... for new combo it's fine.
    for (int i = 0; i < itemModel.getRowCount(); i++) {
      int maItem = Integer.parseInt(itemModel.getValueAt(i, 0).toString());
      int qty = Integer.parseInt(itemModel.getValueAt(i, 2).toString());
      comboDAO.addRecipeItem(maCombo, maItem, qty);
    }
  }

  public boolean isSucceeded() {
    return succeeded;
  }
}
