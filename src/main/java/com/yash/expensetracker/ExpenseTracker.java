/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.yash.expensetracker;

/**
 * Simple launcher for the app.
 * Keeps main small — the UI code lives in ui.MainForm.
 */
public class ExpenseTracker {
    public static void main(String[] args) {
        // Delegate to the Swing form's main (which already sets look & feel)
        ui.MainForm.main(args);
    }
}
