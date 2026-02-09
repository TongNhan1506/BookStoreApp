package com.bookstore.gui.panel.ProductTab;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SearchableComboBox extends JPanel {
    private JTextField searchField;
    private JPopupMenu popup;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private String[] allItems;
    private String selectedItem;
    
    public SearchableComboBox(String[] items) {
        this.allItems = items;
        this.selectedItem = items[0];
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1));
        
        // Search field
        searchField = new JTextField(selectedItem);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        searchField.setEditable(false);
        searchField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Dropdown arrow button
        JButton dropdownButton = new JButton("▼");
        dropdownButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dropdownButton.setForeground(Color.decode("#666666"));
        dropdownButton.setBackground(Color.WHITE);
        dropdownButton.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        dropdownButton.setFocusPainted(false);
        dropdownButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        add(searchField, BorderLayout.CENTER);
        add(dropdownButton, BorderLayout.EAST);
        
        // Create popup menu
        popup = new JPopupMenu();
        popup.setLayout(new BorderLayout());
        popup.setBorder(BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1));
        
        // Search input in popup
        JTextField popupSearchField = new JTextField();
        popupSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        popupSearchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#E0E0E0")),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        popupSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterList(popupSearchField.getText());
            }
        });
        
        // List
        listModel = new DefaultListModel<>();
        for (String item : items) {
            listModel.addElement(item);
        }
        
        list = new JList<>(listModel);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCursor(new Cursor(Cursor.HAND_CURSOR));
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                selectedItem = list.getSelectedValue();
                searchField.setText(selectedItem);
                popup.setVisible(false);
                fireActionEvent();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(getPreferredSize().width, 200));
        
        JPanel popupContent = new JPanel(new BorderLayout());
        popupContent.add(popupSearchField, BorderLayout.NORTH);
        popupContent.add(scrollPane, BorderLayout.CENTER);
        
        popup.add(popupContent);
        
        // Show popup on click
        MouseAdapter showPopupListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupSearchField.setText("");
                filterList("");
                popup.show(SearchableComboBox.this, 0, getHeight());
                popupSearchField.requestFocus();
            }
        };
        
        searchField.addMouseListener(showPopupListener);
        dropdownButton.addMouseListener(showPopupListener);
        
        // Hide popup when clicking outside
        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                popupSearchField.setText("");
            }
            
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }
    
    private void filterList(String searchText) {
        listModel.clear();
        String lowerSearch = searchText.toLowerCase().trim();
        
        for (String item : allItems) {
            if (item.toLowerCase().contains(lowerSearch)) {
                listModel.addElement(item);
            }
        }
    }
    
    public String getSelectedItem() {
        return selectedItem;
    }
    
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < allItems.length) {
            selectedItem = allItems[index];
            searchField.setText(selectedItem);
            fireActionEvent();
        }
    }
    
    // Action listener support
    private java.util.List<ActionListener> actionListeners = new ArrayList<>();
    
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectionChanged");
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }
}
