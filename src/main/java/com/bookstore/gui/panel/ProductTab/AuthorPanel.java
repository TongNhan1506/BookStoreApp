package com.bookstore.gui.panel.ProductTab;

import javax.swing.*;
import java.awt.*;
public class AuthorPanel extends JPanel {
    public AuthorPanel() {
        initUI();
    }

    private void initUI() {
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY); 
    
    JLabel label = new JLabel("ĐÂY LÀ TRANG QUẢN LÝ TÁC GIẢ", SwingConstants.CENTER);
    label.setFont(new Font("Segoe UI", Font.BOLD, 20));
    add(label, BorderLayout.CENTER);
    
}  
}
