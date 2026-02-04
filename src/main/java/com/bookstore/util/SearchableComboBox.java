package com.bookstore.util;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchableComboBox extends JComboBox<String> {
    private List<String> originalItems;
    private boolean isFiltering = false;

    public SearchableComboBox(List<String> items) {
        this.originalItems = new ArrayList<>(items);
        initUI();
    }

    public SearchableComboBox(String[] items) {
        this.originalItems = new ArrayList<>();
        for (String s : items) {
            this.originalItems.add(s);
        }
        initUI();
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if (isFiltering) {
            return;
        }

        super.setSelectedItem(anObject);
    }

    private void initUI() {
        setEditable(true);
        setModel(new DefaultComboBoxModel<>(originalItems.toArray(new String[0])));

        JTextField textEditor = (JTextField) this.getEditor().getEditorComponent();
            textEditor.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
                        return;
                    }
                    SwingUtilities.invokeLater(() -> filter(textEditor.getText()));
                }
            });
    }

    private void filter(String textInput) {
        isFiltering = true;

        List<String> filteredItems = new ArrayList<>();
        for (String item : originalItems) {
            if (item.toLowerCase().contains(textInput.toLowerCase())) {
                filteredItems.add(item);
            }
        }

        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>(filteredItems.toArray(new String[0]));
        setModel(newModel);

        JTextField textEditor = (JTextField) this.getEditor().getEditorComponent();
        textEditor.setText(textInput);

        if (!filteredItems.isEmpty()) {
            showPopup();
        } else {
            hidePopup();
        }

        isFiltering = false;
    }

    public void updateData(List<String> newItems) {
        this.originalItems = new ArrayList<>(newItems);
        setModel(new DefaultComboBoxModel<>(originalItems.toArray(new String[0])));
    }
}