package com.bookstore;

import com.bookstore.gui.panel.BillPanel;

import javax.swing.*;

public class TestBillPanel {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Test Bill Panel");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new BillPanel());

            frame.setVisible(true);
        });
    }
}