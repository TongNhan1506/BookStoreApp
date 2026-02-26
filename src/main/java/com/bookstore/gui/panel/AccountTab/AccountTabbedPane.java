package com.bookstore.gui.panel.AccountTab;

import com.bookstore.util.PermissionUtil;
import com.bookstore.util.Refreshable;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class AccountTabbedPane extends JPanel implements Refreshable {
    private JTabbedPane tabbedPane = new JTabbedPane();
    private AccountPanel accountPanel;
    private RolePanel rolePanel;

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

        if (PermissionUtil.hasViewPermission("MANAGE_ACCOUNT")) {
            accountPanel = new AccountPanel();
            tabbedPane.addTab("Tài Khoản", accountPanel);
        }

        if (PermissionUtil.hasViewPermission("MANAGE_ROLE")) {
            rolePanel = new RolePanel();
            tabbedPane.addTab("Quyền", rolePanel);
        }

        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab instanceof Refreshable r) {
                r.refresh();
            }
        });
    }
}
