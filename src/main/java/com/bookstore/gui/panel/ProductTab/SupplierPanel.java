package com.bookstore.gui.panel.ProductTab;

import javax.swing.*;
import java.awt.*;
public class SupplierPanel extends JPanel {
    public SupplierPanel() {
        initUI();
    }

    private void initUI() {
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY); 
    
    JLabel label = new JLabel("ĐÂY LÀ TRANG QUẢN LÝ NHÀ CUNG CẤP", SwingConstants.CENTER);
    label.setFont(new Font("Segoe UI", Font.BOLD, 20));
    add(label, BorderLayout.CENTER);
    
}
}