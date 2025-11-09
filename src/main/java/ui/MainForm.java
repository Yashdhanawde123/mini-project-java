/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;
import dao.ExpenseDAO;
import db.DBHelper;
import model.Expense;
import ui.LoginForm;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author Yash
 */
public class MainForm extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainForm.class.getName());
    private final ExpenseDAO dao = new ExpenseDAO();
    private final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;


    // UI components
    private JFormattedTextField txtDate;
    private JComboBox<String> cmbCategory;
    private JTextField txtDescription;
    private JTextField txtAmount;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnCalcMonth, btnCalcYear, btnExportCSV;
    private JTextField txtMonth, txtYear;
    private JLabel lblMonthlyTotal, lblYearlyTotal;
    private JTable tblExpenses;
    private DefaultTableModel tableModel;

    public MainForm() {
        initComponents();
        refreshCategoryCombo();
        refreshTableData();
    }
    

    private void initComponents() {
        setTitle("Personal Expense Tracker");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        // Left panel components
        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");
        txtDate = new JFormattedTextField();
        txtDate.setColumns(12);

        JLabel lblCategory = new JLabel("Category:");
        cmbCategory = new JComboBox<>(new String[] {"Food", "Rent", "Transport", "Bills", "Entertainment", "Other"});

        JLabel lblDescription = new JLabel("Description:");
        txtDescription = new JTextField(12);

        JLabel lblAmount = new JLabel("Amount:");
        txtAmount = new JTextField(10);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Summary inputs
        JLabel lMonth = new JLabel("Month (YYYY-MM):");
        txtMonth = new JTextField(8);
        btnCalcMonth = new JButton("Calc");
        lblMonthlyTotal = new JLabel("0.00");

        JLabel lYear = new JLabel("Year (YYYY):");
        txtYear = new JTextField(6);
        btnCalcYear = new JButton("Calc");
        lblYearlyTotal = new JLabel("0.00");

        btnExportCSV = new JButton("Export CSV");

        // Table
        tableModel = new DefaultTableModel(new Object[] {"ID", "Date", "Category", "Description", "Amount"}, 0) {
            final Class[] types = new Class[]{ Integer.class, String.class, String.class, String.class, Double.class };
            @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex]; }
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblExpenses = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(tblExpenses);

        // Layout using GroupLayout (keeps similar to NetBeans)
        JPanel left = new JPanel();
        GroupLayout layout = new GroupLayout(left);
        left.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // horizontal
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblDate).addComponent(lblCategory).addComponent(lblDescription).addComponent(lblAmount))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtDate)
                        .addComponent(cmbCategory)
                        .addComponent(txtDescription)
                        .addComponent(txtAmount)))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnAdd).addComponent(btnUpdate).addComponent(btnDelete).addComponent(btnClear))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lMonth).addComponent(lYear))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addComponent(txtMonth).addComponent(btnCalcMonth).addComponent(lblMonthlyTotal))
                        .addGroup(layout.createSequentialGroup().addComponent(txtYear).addComponent(btnCalcYear).addComponent(lblYearlyTotal))))
                .addComponent(btnExportCSV)
        );

        // vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDate).addComponent(txtDate))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCategory).addComponent(cmbCategory))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDescription).addComponent(txtDescription))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblAmount).addComponent(txtAmount))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnAdd).addComponent(btnUpdate).addComponent(btnDelete).addComponent(btnClear))
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lMonth).addComponent(txtMonth).addComponent(btnCalcMonth).addComponent(lblMonthlyTotal))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lYear).addComponent(txtYear).addComponent(btnCalcYear).addComponent(lblYearlyTotal))
                .addGap(10)
                .addComponent(btnExportCSV)
        );

        // Main frame layout: left panel + table
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, tableScroll);
        split.setDividerLocation(340);
        getContentPane().add(split);

        // Wire up listeners
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnClear.addActionListener(e -> clearInputs());
        btnCalcMonth.addActionListener(e -> onCalcMonth());
        btnCalcYear.addActionListener(e -> onCalcYear());
        btnExportCSV.addActionListener(e -> onExportCSV());

        tblExpenses.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { tableRowClicked(); }
        });
    }

/*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtDate = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        txtDescription = new javax.swing.JTextField();
        txtAmount = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblExpenses = new javax.swing.JTable();
        btnCalcMonth = new javax.swing.JButton();
        btnCalcYear = new javax.swing.JButton();
        btnExportCSV = new javax.swing.JButton();
        txtMonth = new javax.swing.JLabel();
        txtYear = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Date");

        jLabel2.setText("Category");

        jLabel3.setText("Description");

        jLabel4.setText("Amount");

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Food", "Rent", "Transport", "Bills", "Entertainment", "Other" }));
        cmbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoryActionPerformed(evt);
            }
        });

        txtDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescriptionActionPerformed(evt);
            }
        });

        txtAmount.setText(" ");
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });

        btnAdd.setText("ADD");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");

        btnClear.setText("Clear");

        tblExpenses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Category", "Description", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Short.class, java.lang.Long.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblExpenses);

        btnCalcMonth.setText("cal");
        btnCalcMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcMonthActionPerformed(evt);
            }
        });

        btnCalcYear.setText("cal");

        btnExportCSV.setText("dis");
        btnExportCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportCSVActionPerformed(evt);
            }
        });

        txtMonth.setText("Month");

        txtYear.setText("Year");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnClear)
                            .addComponent(btnDelete)
                            .addComponent(btnUpdate)
                            .addComponent(btnAdd)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCalcMonth)
                            .addComponent(btnCalcYear)
                            .addComponent(btnExportCSV))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(txtMonth))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCalcMonth)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtYear)
                            .addComponent(btnCalcYear))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportCSV))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoryActionPerformed

    private void txtDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescriptionActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCalcMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCalcMonthActionPerformed

    private void btnExportCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportCSVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportCSVActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCalcMonth;
    private javax.swing.JButton btnCalcYear;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExportCSV;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblExpenses;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JFormattedTextField txtDate;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JLabel txtMonth;
    private javax.swing.JLabel txtYear;
    // End of variables declaration//GEN-END:variables
    */
    private void refreshCategoryCombo() {
        if (cmbCategory.getItemCount() == 0) {
            cmbCategory.addItem("Food");
            cmbCategory.addItem("Rent");
            cmbCategory.addItem("Transport");
            cmbCategory.addItem("Bills");
            cmbCategory.addItem("Entertainment");
            cmbCategory.addItem("Other");
        }
    }

    private void refreshTableData() {
        try {
            List<Expense> list = dao.getAllExpenses();
            tableModel.setRowCount(0);
            for (Expense ex : list) {
                tableModel.addRow(new Object[] {
                    ex.getId(),
                    ex.getDate().toString(),
                    ex.getCategory(),
                    ex.getDescription(),
                    ex.getAmount()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private Expense getExpenseFromInput() {
        String dateStr = txtDate.getText().trim();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DATE_FMT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format");
            return null;
        }
        String category = (String) cmbCategory.getSelectedItem();
        String desc = txtDescription.getText().trim();
        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric amount");
            return null;
        }
        return new Expense(date, category, desc, amount);
    }

    // Event handlers
    private void onAdd() {
        Expense e = getExpenseFromInput();
        if (e == null) return;
        try {
            dao.addExpense(e);
            JOptionPane.showMessageDialog(this, "Added.");
            clearInputs();
            refreshTableData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding: " + ex.getMessage());
        }
    }

    private void onUpdate() {
        int row = tblExpenses.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }
        Integer id = (Integer) tableModel.getValueAt(row, 0);
        Expense e = getExpenseFromInput();
        if (e == null) return;
        e.setId(id);
        try {
            dao.updateExpense(e);
            JOptionPane.showMessageDialog(this, "Updated.");
            clearInputs();
            refreshTableData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
        }
    }

    private void onDelete() {
        int row = tblExpenses.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        Integer id = (Integer) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected expense?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            dao.deleteExpense(id);
            JOptionPane.showMessageDialog(this, "Deleted.");
            clearInputs();
            refreshTableData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage());
        }
    }

    private void tableRowClicked() {
        int row = tblExpenses.getSelectedRow();
        if (row == -1) return;
        txtDate.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        cmbCategory.setSelectedItem(tableModel.getValueAt(row, 2));
        txtDescription.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        txtAmount.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private void onCalcMonth() {
        String ym = txtMonth.getText().trim(); // "YYYY-MM"
        if (!ym.matches("\\d{4}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Enter month in YYYY-MM format");
            return;
        }
        try {
            double total = dao.getMonthlyTotal(ym);
            lblMonthlyTotal.setText(String.format("%.2f", total));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating monthly total: " + ex.getMessage());
        }
    }

    private void onCalcYear() {
        String year = txtYear.getText().trim();
        if (!year.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Enter year in YYYY format");
            return;
        }
        try {
            double total = dao.getYearlyTotal(year);
            lblYearlyTotal.setText(String.format("%.2f", total));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating yearly total: " + ex.getMessage());
        }
    }

    private void onExportCSV() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        java.io.File file = chooser.getSelectedFile();
        try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
            // header
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                pw.print(tableModel.getColumnName(c));
                if (c < tableModel.getColumnCount() - 1) pw.print(",");
            }
            pw.println();
            // rows
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                for (int c = 0; c < tableModel.getColumnCount(); c++) {
                    pw.print(tableModel.getValueAt(r, c));
                    if (c < tableModel.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }
            JOptionPane.showMessageDialog(this, "Exported to " + file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        }
    }

    private void clearInputs() {
        txtDate.setText("");
        cmbCategory.setSelectedIndex(0);
        txtDescription.setText("");
        txtAmount.setText("");
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
        // optional DB initialization - if your DAO/DBHelper needs any startup, call it here:
        // DBHelper.initialize(); // if implemented

    };
    }






