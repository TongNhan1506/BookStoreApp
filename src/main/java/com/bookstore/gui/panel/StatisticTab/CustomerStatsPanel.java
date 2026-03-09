package com.bookstore.gui.panel.StatisticTab;

import com.bookstore.util.Refreshable;

import javax.swing.*;
import java.awt.*;

public class CustomerStatsPanel extends JPanel implements Refreshable {
    public CustomerStatsPanel() {
        initUI();
    }

    @Override
    public void refresh() {

    }

    public void initUI() {
        setLayout(new BorderLayout(5,5));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    }


}
