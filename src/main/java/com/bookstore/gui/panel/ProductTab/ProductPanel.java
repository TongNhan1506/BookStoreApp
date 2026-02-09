package com.bookstore.gui.panel.ProductTab;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ProductPanel extends JPanel {
    // Colors
    private static final Color HEADER_COLOR = Color.decode("#062d1e");
    private static final Color PRIMARY_GREEN = Color.decode("#4CAF50");
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = Color.decode("#E0E0E0");
    
    // Components
    private JTextField searchField;
    private SearchableComboBox authorCombo;
    private SearchableComboBox categoryCombo;
    private SearchableComboBox supplierCombo;
    private JComboBox<String> statusCombo;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private Set<String> selectedTags = new HashSet<>();
    private JButton tagFilterButton;
    
    // Mock Data
    private List<Book> allBooks;
    private List<Author> authors;
    private List<Category> categories;
    private List<Supplier> suppliers;
    private Set<String> allTags;

    public ProductPanel() {
        initMockData();
        initUI();
        loadTableData();
    }

    private void initMockData() {
        // Authors
        authors = Arrays.asList(
            new Author(1, "Nguyễn Nhật Ánh"),
            new Author(2, "Tô Hoài"),
            new Author(3, "Nam Cao"),
            new Author(4, "Ngô Tất Tố"),
            new Author(5, "Paulo Coelho"),
            new Author(6, "J.K. Rowling"),
            new Author(7, "Haruki Murakami")
        );
        
        // Categories
        categories = Arrays.asList(
            new Category(1, "Văn học"),
            new Category(2, "Thiếu nhi"),
            new Category(3, "Kinh tế"),
            new Category(4, "Tâm lý - Kỹ năng"),
            new Category(5, "Ngoại ngữ")
        );
        
        // Suppliers
        suppliers = Arrays.asList(
            new Supplier(1, "Kim Đồng"),
            new Supplier(2, "Nhà xuất bản Trẻ"),
            new Supplier(3, "Alphabooks")
        );
        
        // All tags
        allTags = new HashSet<>(Arrays.asList(
            "Truyện dài", "Tình bạn", "Thiếu nhi", "Nguyễn Nhật Ánh",
            "Văn học Việt Nam", "Bestseller", "Tâm lý", "Phiêu lưu",
            "Tiểu thuyết", "Kinh điển", "Dịch giả", "Mới phát hành"
        ));
        
        // Books - Use ArrayList instead of Arrays.asList to allow adding/removing
        allBooks = new ArrayList<>();
        allBooks.add(new Book(1, "Bàn Có Năm Chỗ Ngồi", 55000, 150, null, null,
                "Bàn có năm chỗ ngồi xoay quanh câu chuyện tình bạn giữa 5 người bạn. Đó là Huy, Hiền, Quang, Đại, Bảy – họ là năm người bạn với năm cá tính và hoàn cảnh khác nhau cùng chung trong một lớp học.",
                1, 1, 1, "Truyện dài,Tình bạn,Thiếu nhi,Nguyễn Nhật Ánh",
                Arrays.asList(1)));
        
        allBooks.add(new Book(2, "Mắt Biếc", 89000, 200, null, null,
                "Mắt biếc là một truyện dài của nhà văn Nguyễn Nhật Ánh. Tác phẩm được viết trong 10 năm (1990-2000) và được xuất bản lần đầu năm 2007.",
                1, 1, 2, "Văn học Việt Nam,Bestseller,Nguyễn Nhật Ánh,Tình cảm",
                Arrays.asList(1)));
        
        allBooks.add(new Book(3, "Dế Mèn Phiêu Lưu Ký", 45000, 80, null, null,
                "Dế Mèn phiêu lưu ký là tác phẩm văn xuôi dành cho lứa tuổi thiếu nhi của nhà văn Tô Hoài.",
                1, 2, 1, "Thiếu nhi,Văn học Việt Nam,Kinh điển,Phiêu lưu",
                Arrays.asList(2)));
        
        allBooks.add(new Book(4, "Harry Potter và Hòn Đá Phù Thủy", 150000, 50, "Lý Lan", null,
                "Cuốn sách mở đầu bộ truyện Harry Potter nổi tiếng toàn cầu của tác giả J.K. Rowling.",
                1, 2, 3, "Thiếu nhi,Phiêu lưu,Bestseller,Dịch giả",
                Arrays.asList(6)));
        
        allBooks.add(new Book(5, "Nhà Giả Kim", 79000, 120, "Lê Chu Cầu", null,
                "Nhà giả kim là tiểu thuyết được xuất bản lần đầu tiên tại Brazil năm 1988 của nhà văn Paulo Coelho.",
                1, 1, 2, "Tiểu thuyết,Bestseller,Tâm lý,Dịch giả",
                Arrays.asList(5)));
        
        allBooks.add(new Book(6, "Lão Hạc", 35000, 0, null, null,
                "Lão Hạc là một truyện ngắn nổi tiếng của nhà văn Nam Cao, được viết năm 1943.",
                0, 1, 1, "Văn học Việt Nam,Kinh điển,Tiểu thuyết",
                Arrays.asList(3)));
        
        allBooks.add(new Book(7, "Tắt Đèn", 42000, 90, null, null,
                "Tắt đèn là một tiểu thuyết của nhà văn Ngô Tất Tố, được viết từ năm 1937 đến năm 1939.",
                1, 1, 2, "Văn học Việt Nam,Kinh điển,Tiểu thuyết",
                Arrays.asList(4)));
        
        allBooks.add(new Book(8, "Kafka Bên Bờ Biển", 125000, 60, "Dương Tường", null,
                "Kafka Bên Bờ Biển là một tiểu thuyết của nhà văn Nhật Bản Haruki Murakami.",
                1, 1, 3, "Tiểu thuyết,Bestseller,Dịch giả,Văn học Nhật",
                Arrays.asList(7)));
        
        allBooks.add(new Book(9, "Chú Bé Rồng", 68000, 110, null, null,
                "Một câu chuyện phiêu lưu kỳ thú dành cho thiếu nhi.",
                1, 2, 1, "Thiếu nhi,Phiêu lưu,Mới phát hành",
                Arrays.asList(1, 2)));
        
        allBooks.add(new Book(10, "Đời Thừa", 52000, 75, null, null,
                "Đời thừa là tác phẩm nổi tiếng của nhà văn Nam Cao.",
                1, 1, 2, "Văn học Việt Nam,Kinh điển",
                Arrays.asList(3)));
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_COLOR);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Top panel (Search + Filters + Add button)
        JPanel topPanel = createTopPanel();
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(BG_COLOR);
        
        // Search and Add button row
        JPanel searchRow = new JPanel(new BorderLayout(15, 0));
        searchRow.setBackground(BG_COLOR);
        
        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterBooks();
            }
        });
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BG_COLOR);
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        searchRow.add(searchPanel, BorderLayout.CENTER);
        
        // Add book button
        JButton addButton = createStyledButton("+ THÊM SÁCH", PRIMARY_GREEN);
        addButton.setPreferredSize(new Dimension(160, 38));
        addButton.addActionListener(e -> showAddBookDialog());
        searchRow.add(addButton, BorderLayout.EAST);
        
        topPanel.add(searchRow, BorderLayout.NORTH);
        
        // Filters row
        JPanel filtersPanel = createFiltersPanel();
        topPanel.add(filtersPanel, BorderLayout.CENTER);
        
        return topPanel;
    }

    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.Y_AXIS));
        filtersPanel.setBackground(BG_COLOR);
        
        // Main grid panel for 2x2 filters
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(BG_COLOR);
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Row 0, Col 0: Author Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gridPanel.add(createFilterLabel("Tác giả:"), gbc);
        
        // Row 0, Col 1: Author ComboBox
        gbc.gridx = 1;
        gbc.weightx = 1;
        authorCombo = new SearchableComboBox(getAuthorNames());
        authorCombo.setPreferredSize(new Dimension(220, 35));
        authorCombo.addActionListener(e -> filterBooks());
        gridPanel.add(authorCombo, gbc);
        
        // Row 0, Col 2: Category Label
        gbc.gridx = 2;
        gbc.weightx = 0;
        gridPanel.add(createFilterLabel("Thể loại:"), gbc);
        
        // Row 0, Col 3: Category ComboBox
        gbc.gridx = 3;
        gbc.weightx = 1;
        categoryCombo = new SearchableComboBox(getCategoryNames());
        categoryCombo.setPreferredSize(new Dimension(220, 35));
        categoryCombo.addActionListener(e -> filterBooks());
        gridPanel.add(categoryCombo, gbc);
        
        // Row 1, Col 0: Supplier Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gridPanel.add(createFilterLabel("Nhà cung cấp:"), gbc);
        
        // Row 1, Col 1: Supplier ComboBox
        gbc.gridx = 1;
        gbc.weightx = 1;
        supplierCombo = new SearchableComboBox(getSupplierNames());
        supplierCombo.setPreferredSize(new Dimension(220, 35));
        supplierCombo.addActionListener(e -> filterBooks());
        gridPanel.add(supplierCombo, gbc);
        
        // Row 1, Col 2: Status Label
        gbc.gridx = 2;
        gbc.weightx = 0;
        gridPanel.add(createFilterLabel("Trạng thái:"), gbc);
        
        // Row 1, Col 3: Status ComboBox
        gbc.gridx = 3;
        gbc.weightx = 1;
        statusCombo = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang bán", "Ngừng bán"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusCombo.setBackground(Color.WHITE);
        statusCombo.setPreferredSize(new Dimension(220, 35));
        statusCombo.addActionListener(e -> filterBooks());
        gridPanel.add(statusCombo, gbc);
        
        filtersPanel.add(gridPanel);
        filtersPanel.add(Box.createVerticalStrut(12));
        
        // Row for Tag filter and Reset button
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setBackground(BG_COLOR);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tag filter button
        tagFilterButton = new JButton("Tags +");
        tagFilterButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tagFilterButton.setForeground(PRIMARY_GREEN);
        tagFilterButton.setBackground(BG_COLOR);
        tagFilterButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 2),
            BorderFactory.createEmptyBorder(7, 18, 7, 18)
        ));
        tagFilterButton.setFocusPainted(false);
        tagFilterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tagFilterButton.addActionListener(e -> showTagFilterPanel());
        buttonRow.add(tagFilterButton);
        
        // Reset filter button
        JButton resetButton = new JButton("✖ Xóa bộ lọc");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resetButton.setForeground(Color.decode("#666666"));
        resetButton.setBackground(BG_COLOR);
        resetButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(7, 15, 7, 15)
        ));
        resetButton.setFocusPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(e -> resetFilters());
        buttonRow.add(resetButton);
        
        filtersPanel.add(buttonRow);
        
        return filtersPanel;
    }

    private JLabel createFilterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(Color.decode("#333333"));
        return label;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG_COLOR);
        tablePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        
        // Table
        String[] columns = {"Sách", "Tác giả", "Thể loại", "Nhà cung cấp", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only action column editable
            }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookTable.setRowHeight(80);
        bookTable.setShowVerticalLines(true);
        bookTable.setShowHorizontalLines(true);
        bookTable.setGridColor(BORDER_COLOR);
        bookTable.setSelectionBackground(Color.decode("#E8F5E9"));
        bookTable.setSelectionForeground(Color.BLACK);
        
        // Disable column reordering and resizing
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.getTableHeader().setResizingAllowed(false);
        
        // Custom header
        JTableHeader header = bookTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        // Column widths
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(250); // Sách
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Tác giả
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Thể loại
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(150); // NCC
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Trạng thái
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(180); // Thao tác
        
        // Custom renderers
        bookTable.getColumnModel().getColumn(0).setCellRenderer(new BookCellRenderer());
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ActionCellRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ActionCellEditor());
        
        // Center align other columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }

    private void loadTableData() {
        loadTableData(allBooks);
    }

    private void loadTableData(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            Object[] row = new Object[6];
            row[0] = book; // Will be rendered by BookCellRenderer
            row[1] = getAuthorNamesForBook(book);
            row[2] = getCategoryName(book.categoryId);
            row[3] = getSupplierName(book.supplierId);
            row[4] = book.status == 1 ? "Đang bán" : "Ngừng bán";
            row[5] = book; // For action buttons
            tableModel.addRow(row);
        }
    }

    private void filterBooks() {
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedAuthor = (String) authorCombo.getSelectedItem();
        String selectedCategory = (String) categoryCombo.getSelectedItem();
        String selectedSupplier = (String) supplierCombo.getSelectedItem();
        String selectedStatus = (String) statusCombo.getSelectedItem();
        
        List<Book> filtered = new ArrayList<>();
        
        for (Book book : allBooks) {
            // Search filter
            if (!searchText.isEmpty() && !book.bookName.toLowerCase().contains(searchText)) {
                continue;
            }
            
            // Author filter
            if (selectedAuthor != null && !selectedAuthor.equals("Tất cả tác giả")) {
                String authorNames = getAuthorNamesForBook(book);
                if (!authorNames.contains(selectedAuthor)) {
                    continue;
                }
            }
            
            // Category filter
            if (selectedCategory != null && !selectedCategory.equals("Tất cả thể loại")) {
                if (!getCategoryName(book.categoryId).equals(selectedCategory)) {
                    continue;
                }
            }
            
            // Supplier filter
            if (selectedSupplier != null && !selectedSupplier.equals("Tất cả nhà cung cấp")) {
                if (!getSupplierName(book.supplierId).equals(selectedSupplier)) {
                    continue;
                }
            }
            
            // Status filter
            if (!selectedStatus.equals("Tất cả trạng thái")) {
                int requiredStatus = selectedStatus.equals("Đang bán") ? 1 : 0;
                if (book.status != requiredStatus) {
                    continue;
                }
            }
            
            // Tag filter
            if (!selectedTags.isEmpty()) {
                boolean hasTag = false;
                if (book.tagDetail != null) {
                    String[] bookTags = book.tagDetail.split(",");
                    for (String tag : bookTags) {
                        if (selectedTags.contains(tag.trim())) {
                            hasTag = true;
                            break;
                        }
                    }
                }
                if (!hasTag) {
                    continue;
                }
            }
            
            filtered.add(book);
        }
        
        loadTableData(filtered);
    }

    private void showTagFilterPanel() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Lọc theo Tags", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(BG_COLOR);
        
        // Title
        JLabel title = new JLabel("Chọn Tags để lọc");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contentPanel.add(title, BorderLayout.NORTH);
        
        // Tag chips panel
        JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        chipsPanel.setBackground(BG_COLOR);
        
        for (String tag : allTags) {
            JToggleButton chip = createTagChip(tag);
            chip.setSelected(selectedTags.contains(tag));
            chipsPanel.add(chip);
        }
        
        JScrollPane scrollPane = new JScrollPane(chipsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BG_COLOR);
        
        JButton cancelButton = createStyledButton("Hủy", Color.decode("#757575"));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton applyButton = createStyledButton("Áp dụng", PRIMARY_GREEN);
        applyButton.addActionListener(e -> {
            selectedTags.clear();
            for (Component comp : chipsPanel.getComponents()) {
                if (comp instanceof JToggleButton) {
                    JToggleButton chip = (JToggleButton) comp;
                    if (chip.isSelected()) {
                        selectedTags.add(chip.getText());
                    }
                }
            }
            updateTagButtonText();
            filterBooks();
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private JToggleButton createTagChip(String text) {
        JToggleButton chip = new JToggleButton(text);
        chip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chip.setFocusPainted(false);
        chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chip.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        
        chip.setBackground(Color.decode("#F5F5F5"));
        chip.setForeground(Color.decode("#333333"));
        
        chip.addItemListener(e -> {
            if (chip.isSelected()) {
                chip.setBackground(PRIMARY_GREEN);
                chip.setForeground(Color.WHITE);
            } else {
                chip.setBackground(Color.decode("#F5F5F5"));
                chip.setForeground(Color.decode("#333333"));
            }
        });
        
        return chip;
    }

    private void updateTagButtonText() {
        if (selectedTags.isEmpty()) {
            tagFilterButton.setText("Tags +");
        } else {
            tagFilterButton.setText("Tags (" + selectedTags.size() + ")");
        }
    }

    private void resetFilters() {
        searchField.setText("");
        authorCombo.setSelectedIndex(0);
        categoryCombo.setSelectedIndex(0);
        supplierCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        selectedTags.clear();
        updateTagButtonText();
        filterBooks();
    }

    private void showAddBookDialog() {
        BookFormDialog dialog = new BookFormDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Thêm sách mới", null);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            Book newBook = dialog.getBook();
            // Check duplicate
            for (Book b : allBooks) {
                if (b.bookName.equalsIgnoreCase(newBook.bookName)) {
                    JOptionPane.showMessageDialog(this, 
                        "Sách \"" + newBook.bookName + "\" đã tồn tại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            newBook.bookId = allBooks.size() + 1;
            allBooks.add(newBook);
            filterBooks();
            JOptionPane.showMessageDialog(this, "Thêm sách thành công!", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditBookDialog(Book book) {
        BookFormDialog dialog = new BookFormDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Chỉnh sửa sách", book);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            Book updatedBook = dialog.getBook();
            // Update book in list
            for (int i = 0; i < allBooks.size(); i++) {
                if (allBooks.get(i).bookId == book.bookId) {
                    updatedBook.bookId = book.bookId;
                    allBooks.set(i, updatedBook);
                    break;
                }
            }
            filterBooks();
            JOptionPane.showMessageDialog(this, "Cập nhật sách thành công!", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showBookDetail(Book book) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Chi tiết sách", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BG_COLOR);
        
        // Image
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(BG_COLOR);
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.decode("#F5F5F5"));
        
        // Draw placeholder
        imageLabel.setIcon(new ImageIcon(createBookPlaceholder(150, 200)));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        contentPanel.add(imagePanel, BorderLayout.WEST);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(BG_COLOR);
        
        addDetailRow(infoPanel, "Tên sách:", book.bookName);
        addDetailRow(infoPanel, "Tác giả:", getAuthorNamesForBook(book));
        addDetailRow(infoPanel, "Thể loại:", getCategoryName(book.categoryId));
        addDetailRow(infoPanel, "Người dịch:", book.translator != null ? book.translator : "Không có");
        addDetailRow(infoPanel, "Nhà cung cấp:", getSupplierName(book.supplierId));
        addDetailRow(infoPanel, "Trạng thái:", book.status == 1 ? "Đang bán" : "Ngừng bán");
        addDetailRow(infoPanel, "Tags:", book.tagDetail != null ? book.tagDetail.replace(",", ", ") : "Không có");
        
        // Description
        infoPanel.add(Box.createVerticalStrut(10));
        JLabel descLabel = new JLabel("Mô tả:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);
        
        JTextArea descArea = new JTextArea(book.description != null ? book.description : "Không có mô tả");
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(BG_COLOR);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(350, 100));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descScroll);
        
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BG_COLOR);
        JButton closeButton = createStyledButton("Đóng", Color.decode("#757575"));
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(BG_COLOR);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setPreferredSize(new Dimension(100, 20));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        row.add(lblLabel);
        row.add(lblValue);
        panel.add(row);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private String[] getAuthorNames() {
        String[] names = new String[authors.size() + 1];
        names[0] = "Tất cả tác giả";
        for (int i = 0; i < authors.size(); i++) {
            names[i + 1] = authors.get(i).name;
        }
        return names;
    }

    private String[] getCategoryNames() {
        String[] names = new String[categories.size() + 1];
        names[0] = "Tất cả thể loại";
        for (int i = 0; i < categories.size(); i++) {
            names[i + 1] = categories.get(i).name;
        }
        return names;
    }

    private String[] getSupplierNames() {
        String[] names = new String[suppliers.size() + 1];
        names[0] = "Tất cả nhà cung cấp";
        for (int i = 0; i < suppliers.size(); i++) {
            names[i + 1] = suppliers.get(i).name;
        }
        return names;
    }

    private String getAuthorNamesForBook(Book book) {
        if (book.authorIds == null || book.authorIds.isEmpty()) {
            return "Không rõ";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < book.authorIds.size(); i++) {
            int authorId = book.authorIds.get(i);
            for (Author author : authors) {
                if (author.id == authorId) {
                    sb.append(author.name);
                    if (i < book.authorIds.size() - 1) {
                        sb.append(", ");
                    }
                    break;
                }
            }
        }
        return sb.toString();
    }

    private String getCategoryName(int categoryId) {
        for (Category cat : categories) {
            if (cat.id == categoryId) {
                return cat.name;
            }
        }
        return "Không rõ";
    }

    private String getSupplierName(int supplierId) {
        for (Supplier sup : suppliers) {
            if (sup.id == supplierId) {
                return sup.name;
            }
        }
        return "Không rõ";
    }

    private Image createBookPlaceholder(int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        
        // Background
        g2d.setColor(Color.decode("#E0E0E0"));
        g2d.fillRect(0, 0, width, height);
        
        // Border
        g2d.setColor(Color.decode("#BDBDBD"));
        g2d.drawRect(0, 0, width - 1, height - 1);
        
        // Icon
        g2d.setColor(Color.decode("#9E9E9E"));
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        String text = "📖";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return img;
    }

    // Inner classes for cell renderers
    class BookCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Book book = (Book) value;
            
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setOpaque(true);
            panel.setBorder(new EmptyBorder(5, 10, 5, 10));
            
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            
            // Image
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(50, 70));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.decode("#F5F5F5"));
            imageLabel.setIcon(new ImageIcon(createBookPlaceholder(50, 70)));
            
            panel.add(imageLabel, BorderLayout.WEST);
            
            // Book name
            JLabel nameLabel = new JLabel("<html><b>" + book.bookName + "</b></html>");
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            panel.add(nameLabel, BorderLayout.CENTER);
            
            return panel;
        }
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel label = new JLabel(value.toString());
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            
            if (value.toString().equals("Đang bán")) {
                label.setForeground(Color.decode("#2E7D32"));
                label.setBackground(Color.decode("#E8F5E9"));
            } else {
                label.setForeground(Color.decode("#C62828"));
                label.setBackground(Color.decode("#FFEBEE"));
            }
            
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
            }
            
            return label;
        }
    }

    class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton;
        private JButton viewButton;
        
        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20)); // Changed vertical gap to 20 for vertical centering
            setOpaque(true);
            
            editButton = new JButton("Sửa");
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(Color.decode("#2196F3"));
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            editButton.setFocusPainted(false);
            
            viewButton = new JButton("Xem");
            viewButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            viewButton.setForeground(Color.WHITE);
            viewButton.setBackground(Color.decode("#757575"));
            viewButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            viewButton.setFocusPainted(false);
            
            add(editButton);
            add(viewButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            
            return this;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton viewButton;
        private Book currentBook;
        
        public ActionCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            editButton = new JButton("Sửa");
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(Color.decode("#2196F3"));
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            editButton.setFocusPainted(false);
            editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editButton.addActionListener(e -> {
                showEditBookDialog(currentBook);
                fireEditingStopped();
            });
            
            viewButton = new JButton("Xem");
            viewButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            viewButton.setForeground(Color.WHITE);
            viewButton.setBackground(Color.decode("#757575"));
            viewButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            viewButton.setFocusPainted(false);
            viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            viewButton.addActionListener(e -> {
                showBookDetail(currentBook);
                fireEditingStopped();
            });
            
            panel.add(editButton);
            panel.add(viewButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentBook = (Book) value;
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return currentBook;
        }
    }

    // Data classes
    static class Book {
        int bookId;
        String bookName;
        double sellingPrice;
        int quantity;
        String translator;
        String image;
        String description;
        int status;  // 0 = Ngừng bán, 1 = Đang bán
        int categoryId;
        int supplierId;
        String tagDetail;
        List<Integer> authorIds;
        
        public Book(int bookId, String bookName, double sellingPrice, int quantity,
                String translator, String image, String description, int status,
                int categoryId, int supplierId, String tagDetail, List<Integer> authorIds) {
            this.bookId = bookId;
            this.bookName = bookName;
            this.sellingPrice = sellingPrice;
            this.quantity = quantity;
            this.translator = translator;
            this.image = image;
            this.description = description;
            this.status = status;
            this.categoryId = categoryId;
            this.supplierId = supplierId;
            this.tagDetail = tagDetail;
            this.authorIds = authorIds;
        }
    }

    static class Author {
        int id;
        String name;
        
        public Author(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Category {
        int id;
        String name;
        
        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Supplier {
        int id;
        String name;
        
        public Supplier(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}