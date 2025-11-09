/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBHelper;
import model.Expense;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public void addExpense(Expense e) throws SQLException {
        String sql = "INSERT INTO expenses (date, category, description, amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getDate()));
            ps.setString(2, e.getCategory());
            ps.setString(3, e.getDescription());
            ps.setDouble(4, e.getAmount());
            ps.executeUpdate();
        }
    }

    public void updateExpense(Expense e) throws SQLException {
        String sql = "UPDATE expenses SET date = ?, category = ?, description = ?, amount = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getDate()));
            ps.setString(2, e.getCategory());
            ps.setString(3, e.getDescription());
            ps.setDouble(4, e.getAmount());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    public void deleteExpense(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT id, date, category, description, amount FROM expenses ORDER BY date DESC";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Expense e = new Expense(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDouble("amount")
                );
                list.add(e);
            }
        }
        return list;
    }

    // monthly total: yearMonth example "2025-11"
    public double getMonthlyTotal(String yearMonth) throws SQLException {
        String sql = "SELECT IFNULL(SUM(amount),0) AS total FROM expenses WHERE DATE_FORMAT(date, '%Y-%m') = ?";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, yearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0.0;
            }
        }
    }

    // yearly total: year example "2025"
    public double getYearlyTotal(String year) throws SQLException {
        String sql = "SELECT IFNULL(SUM(amount),0) AS total FROM expenses WHERE YEAR(date) = ?";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(year));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0.0;
            }
        }
    }
}
