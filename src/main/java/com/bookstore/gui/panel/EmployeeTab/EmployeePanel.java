package com.bookstore.gui.panel.EmployeeTab;

import com.bookstore.bus.EmployeeBUS;
import com.bookstore.bus.RoleBUS;
import com.bookstore.dto.EmployeeDTO;
import com.bookstore.dto.RoleDTO;
import com.bookstore.util.AppConstant;
import com.bookstore.util.Refreshable;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmployeePanel extends JPanel implements Refreshable {
    private JTextField txtSearch;
    private JComboBox<String> cboRole;
    private JComboBox<String> cboStatus;

    private JButton btnResetFilter;
    private JButton btnAddEmployee;
    private JButton btnEditEmployee;
    private JButton btnViewEmployee;

    private JTable tblEmployee;
    private DefaultTableModel employeeModel;

    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final RoleBUS roleBUS = new RoleBUS();
    private List<EmployeeDTO> listEmployees;

    public EmployeePanel() {
        initUI();
    }

    @Override
    public void refresh() {
        loadEmployeeTable();
        loadRolesToComboBox();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        refresh();
        addEvents();
    }

    private JPanel createTopPanel() {
        JPanel pTop = new JPanel(new BorderLayout(0, 15));
        pTop.setOpaque(false);

        // --- HÀNG 1: TÌM KIẾM VÀ NÚT THÊM ---
        JPanel pSearchRow = new JPanel(new BorderLayout(15, 0));
        pSearchRow.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 14));
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo tên hoặc số điện thoại...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("icon/search_icon.svg").derive(20, 20));

        btnAddEmployee = new JButton("+ Thêm Nhân Viên");
        btnAddEmployee.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        btnAddEmployee.setForeground(Color.WHITE);
        btnAddEmployee.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        btnAddEmployee.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddEmployee.setPreferredSize(new Dimension(200, 40));
        btnAddEmployee.putClientProperty(FlatClientProperties.STYLE, "arc: 5; hoverBackground: #00A364;");

        pSearchRow.add(txtSearch, BorderLayout.CENTER);
        pSearchRow.add(btnAddEmployee, BorderLayout.EAST);

        // --- HÀNG 2: BỘ LỌC VÀ THAO TÁC ---
        JPanel pFilterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pFilterRow.setOpaque(false);

        // Bộ lọc chức vụ
        JPanel pRole = createFilterGroup("Chức vụ:");
        cboRole = new JComboBox<>();
        cboRole.addItem("Tất cả chức vụ");
        cboRole.setPreferredSize(new Dimension(200, 35));
        cboRole.setBackground(Color.WHITE);
        pRole.add(cboRole);

        // Bộ lọc trạng thái
        JPanel pStatus = createFilterGroup("Trạng thái:");
        cboStatus = new JComboBox<>(new String[]{"Tất cả", "Còn làm việc", "Nghỉ việc"});
        cboStatus.setPreferredSize(new Dimension(150, 35));
        cboStatus.setBackground(Color.WHITE);
        pStatus.add(cboStatus);

        // Khởi tạo ColorFilter cho icon trắng
        FlatSVGIcon.ColorFilter whiteFilter = new FlatSVGIcon.ColorFilter(color -> Color.WHITE);

        // Nút Xóa bộ lọc
        FlatSVGIcon resetIcon = new FlatSVGIcon("icon/reset_icon.svg").derive(16, 16);
        resetIcon.setColorFilter(whiteFilter);
        btnResetFilter = new JButton("Xóa bộ lọc", resetIcon);
        btnResetFilter.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        btnResetFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetFilter.setPreferredSize(new Dimension(150, 35));
        btnResetFilter.setBackground(Color.decode("#ff3131"));
        btnResetFilter.setForeground(Color.WHITE);
        btnResetFilter.putClientProperty(FlatClientProperties.STYLE, "arc: 5; hoverBackground: #ff5757;");

        // Nút Sửa
        FlatSVGIcon editIcon = new FlatSVGIcon("icon/edit_icon.svg").derive(16, 16);
        editIcon.setColorFilter(whiteFilter);
        btnEditEmployee = new JButton("Sửa", editIcon);
        btnEditEmployee.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        btnEditEmployee.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditEmployee.setPreferredSize(new Dimension(100, 35));
        btnEditEmployee.setBackground(Color.decode("#ffcb4b"));
        btnEditEmployee.setForeground(Color.WHITE);
        btnEditEmployee.putClientProperty(FlatClientProperties.STYLE, "arc: 5; hoverBackground: #ffdd53;");

        // Nút Xem chi tiết
        FlatSVGIcon viewIcon = new FlatSVGIcon("icon/info_icon.svg").derive(16, 16);
        viewIcon.setColorFilter(whiteFilter);
        btnViewEmployee = new JButton("Xem chi tiết", viewIcon);
        btnViewEmployee.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        btnViewEmployee.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewEmployee.setPreferredSize(new Dimension(140, 35));
        btnViewEmployee.setBackground(Color.decode("#5674ff"));
        btnViewEmployee.setForeground(Color.WHITE);
        btnViewEmployee.putClientProperty(FlatClientProperties.STYLE, "arc: 5; hoverBackground: #64B5F6;");

        // Wrappers để đẩy nút xuống ngang hàng TextField
        pFilterRow.add(pRole);
        pFilterRow.add(pStatus);
        pFilterRow.add(createWrapper(btnResetFilter));
        pFilterRow.add(createWrapper(btnEditEmployee));
        pFilterRow.add(createWrapper(btnViewEmployee));

        pTop.add(pSearchRow, BorderLayout.NORTH);
        pTop.add(pFilterRow, BorderLayout.CENTER);

        return pTop;
    }

    private JPanel createFilterGroup(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel label = new JLabel(title);
        label.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 13));
        label.setForeground(Color.DARK_GRAY);
        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createWrapper(JButton btn) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(22, 0, 0, 0));
        wrapper.add(btn, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Chỉ khai báo các cột cần hiển thị + ID để định danh
        String[] headers = {"ID", "Tên nhân viên", "Số điện thoại", "Ngày sinh", "Chức vụ", "Trạng thái"};
        employeeModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEmployee = new JTable(employeeModel);
        styleTable(tblEmployee);

        // Ẩn cột ID
        tblEmployee.getColumnModel().getColumn(0).setMinWidth(0);
        tblEmployee.getColumnModel().getColumn(0).setMaxWidth(0);
        tblEmployee.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scrollPane = new JScrollPane(tblEmployee);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#E0E0E0")));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        header.setForeground(Color.WHITE);
        header.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(Color.decode("#E0E0E0"));
        table.setSelectionBackground(Color.decode("#d4ffee"));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadEmployeeTable() {
        // Kéo toàn bộ dữ liệu từ Database lên RAM
        listEmployees = employeeBUS.getAllEmployees();
        updateEmployeeTable(listEmployees);
    }

    private void updateEmployeeTable(java.util.List<EmployeeDTO> list) {
        employeeModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (EmployeeDTO emp : list) {
            String birthdayStr = emp.getBirthday() != null ? sdf.format(emp.getBirthday()) : "Chưa cập nhật";
            String statusStr = emp.getStatus() == 1 ? "Còn làm việc" : "Nghỉ việc";

            employeeModel.addRow(new Object[]{
                    emp.getEmployeeId(),
                    emp.getEmployeeName(),
                    emp.getEmployeePhone(),
                    birthdayStr,
                    emp.getRoleName(),
                    statusStr
            });
        }
    }

    private void filterEmployees() {
        if (listEmployees == null) return;

        String keyword = txtSearch.getText().trim().toLowerCase();
        String selectedRole = cboRole.getSelectedItem() != null ? cboRole.getSelectedItem().toString() : "Tất cả chức vụ";
        String selectedStatus = cboStatus.getSelectedItem() != null ? cboStatus.getSelectedItem().toString() : "Tất cả";

        List<EmployeeDTO> filteredList = new ArrayList<>();

        for (EmployeeDTO emp : listEmployees) {
            String name = emp.getEmployeeName().toLowerCase();
            String phone = emp.getEmployeePhone().toLowerCase();

            boolean matchKeyword = keyword.isEmpty() || name.contains(keyword) || phone.contains(keyword);
            boolean matchRole = selectedRole.equals("Tất cả chức vụ") ||
                    (emp.getRoleName() != null && emp.getRoleName().equals(selectedRole));

            boolean matchStatus = false;
            if (selectedStatus.equals("Tất cả")) {
                matchStatus = true;
            } else if (selectedStatus.equals("Còn làm việc") && emp.getStatus() == 1) {
                matchStatus = true;
            } else if (selectedStatus.equals("Nghỉ việc") && emp.getStatus() == 0) {
                matchStatus = true;
            }

            if (matchKeyword && matchRole && matchStatus) {
                filteredList.add(emp);
            }
        }

        updateEmployeeTable(filteredList);
    }

    private void loadRolesToComboBox() {
        cboRole.removeAllItems();
        cboRole.addItem("Tất cả chức vụ");
        List<RoleDTO> allRoles = roleBUS.getAllRoles();
        for (RoleDTO role : allRoles) {
            cboRole.addItem(role.getRoleName());
        }

        cboRole.addActionListener(e -> checkAndFilterRole());
    }

    private void checkAndFilterRole() {
        String selectedRole = cboRole.getSelectedItem() != null ? cboRole.getSelectedItem().toString() : "Tất cả chức vụ";

        if (!selectedRole.equals("Tất cả chức vụ")) {
            boolean hasEmployee = false;

            if (listEmployees != null) {
                for (EmployeeDTO emp : listEmployees) {
                    if (emp.getRoleName() != null && emp.getRoleName().equals(selectedRole)) {
                        hasEmployee = true;
                        break;
                    }
                }
            }

            if (!hasEmployee) {
                JOptionPane.showMessageDialog(this,
                        "Hiện tại chưa có nhân viên nào đảm nhận chức vụ: " + selectedRole,
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                cboRole.setSelectedIndex(0);
            }
        }

        filterEmployees();
    }

    private void resetFilter() {
        txtSearch.setText("");
        cboRole.setSelectedIndex(0);
        cboStatus.setSelectedIndex(0);
    }

    private void addEvents() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterEmployees(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterEmployees(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterEmployees(); }
        });

        cboStatus.addActionListener(e -> filterEmployees());
        btnResetFilter.addActionListener(e -> resetFilter());
    }
}