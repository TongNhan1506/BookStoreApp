package com.bookstore.gui.panel.AccountTab;

import javax.swing.*;
import com.bookstore.util.Refreshable;
import com.bookstore.gui.panel.PriceTab.PricePanel;
import com.bookstore.gui.panel.PriceTab.PromotionPanel;
import com.formdev.flatlaf.FlatClientProperties;

import java.awt.*;

public class AccountTabbedPane extends JPanel implements Refreshable {
    JTabbedPane tabbedPane = new JTabbedPane();

    public AccountTabbedPane() {
        initUI();
    }

    @Override
    public void refresh() {
        Component selectedTab = tabbedPane.getSelectedComponent();
        if (selectedTab instanceof Refreshable r) {
            r.refresh();
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, FlatClientProperties.TABBED_PANE_ALIGN_CENTER);
        tabbedPane.addTab("Tài khoản", new AccountPanel());
        tabbedPane.addTab("Vai trò", new RolePanel());
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab instanceof Refreshable r) {
                r.refresh();
            }
        });
    }
}