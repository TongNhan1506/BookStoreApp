package com.bookstore.gui.panel.SellingTabMiniFrame;

import com.bookstore.util.AppConstant;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class BookDetailMiniFrame extends JFrame {
    public BookDetailMiniFrame() {
        initUI();
    }

    public void initUI() {
        setTitle("Chi Tiết Sản Phẩm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 700);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pHeader = new JPanel();
        pHeader.setBackground(Color.WHITE);
        JLabel lbHeader = new JLabel("Chi Tiết Sản Phẩm");
        lbHeader.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 20));
        lbHeader.setForeground(Color.BLACK);
        lbHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbHeader.setHorizontalAlignment(SwingConstants.CENTER);
        pHeader.add(lbHeader);

        add(pHeader, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new BookDetailMiniFrame().setVisible(true);
    }
}
