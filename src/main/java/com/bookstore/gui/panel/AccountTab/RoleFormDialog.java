package com.bookstore.gui.panel.AccountTab;

import com.bookstore.bus.*;
import com.bookstore.dto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleFormDialog extends JDialog {
    private RoleDTO role;
    private JTextField txtName;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<ActionDTO> listActions; // Lưu danh sách Action để lấy ID khi lưu

    private RoleBUS roleBUS = new RoleBUS();
    private ActionBUS actionBUS = new ActionBUS();
    private PermissionBUS permissionBUS = new PermissionBUS();

    private boolean isSaved = false;
    private boolean isAddMode; // Thêm biến cờ để nhận biết chế độ

    public RoleFormDialog(Frame owner, RoleDTO role) {
        // Đổi title tùy theo chế độ
        super(owner, role.getRoleId() == 0 ? "Thêm mới Chức vụ" : "Chỉnh sửa Chức vụ", true);
        this.role = role;
        this.isAddMode = (role.getRoleId() == 0); // Nhận diện Thêm hay Sửa

        // ... (các khởi tạo BUS giữ nguyên) ...
        initUI();
        loadData();
    }

    private void initUI() {
        setSize(650, 550);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(0, 10));

        // --- Bố cục Top: Sửa tên ---
        JPanel pInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pInfo.add(new JLabel("Tên chức vụ (*): "));
        txtName = new JTextField(role.getRoleName(), 30);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pInfo.add(txtName);
        add(pInfo, BorderLayout.NORTH);

        // --- Bố cục Center: Bảng Checkbox ---
        String[] headers = {"Tên chức năng", "Quyền Xem", "Quyền Thao tác"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1 || columnIndex == 2) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2; // CHỈ CHO PHÉP CLICK CỘT CHECKBOX
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableModel.addTableModelListener(e -> {
            // Chỉ bắt sự kiện khi có ô bị thay đổi giá trị (UPDATE)
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();

                // Đảm bảo thao tác nằm ở cột Checkbox (cột 1: Xem, cột 2: Thao tác)
                if (row >= 0 && (col == 1 || col == 2)) {
                    boolean isView = (boolean) tableModel.getValueAt(row, 1);
                    boolean isAction = (boolean) tableModel.getValueAt(row, 2);

                    // Kịch bản 1: Người dùng vừa tick "Thao tác" (cột 2) -> Ép phải "Xem"
                    if (col == 2 && isAction && !isView) {
                        // Tự động tick ô Xem
                        tableModel.setValueAt(true, row, 1);
                    }

                    // Kịch bản 2: Người dùng vừa bỏ tick "Xem" (cột 1) -> Tước luôn quyền "Thao tác"
                    else if (col == 1 && !isView && isAction) {
                        // Tự động bỏ tick ô Thao tác
                        tableModel.setValueAt(false, row, 2);
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Bố cục Bottom: Nút Lưu ---
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnSave = new JButton("Lưu thay đổi");
        btnSave.setBackground(Color.decode("#00A364"));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.addActionListener(e -> saveRole());

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dispose());

        pBottom.add(btnCancel);
        pBottom.add(btnSave);
        add(pBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        listActions = actionBUS.selectAllActions(); // Lấy 16 actions từ DB
        Map<String, PermissionDTO> currentPerms = permissionBUS.getPermissionsByRoleId(role.getRoleId());

        tableModel.setRowCount(0);
        for (ActionDTO action : listActions) {
            PermissionDTO perm = currentPerms.get(action.getActionCode());
            boolean canView = (perm != null && perm.isView());
            boolean canAction = (perm != null && perm.isAction());

            tableModel.addRow(new Object[]{ action.getActionName(), canView, canAction });
        }
    }

    private void saveRole() {
        // 1. Chốt dữ liệu trên UI (Tránh trường hợp đang tick dở mà bấm Lưu)
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        // 2. Kiểm tra dữ liệu đầu vào
        String newName = txtName.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên chức vụ không được để trống!");
            return;
        }

        role.setRoleName(newName);
        int targetRoleId = role.getRoleId(); // Biến này sẽ giữ ID cuối cùng để lưu quyền

        // 3. XỬ LÝ LOGIC: THÊM MỚI hay CẬP NHẬT?
        if (isAddMode) {
            // === TRƯỜNG HỢP THÊM MỚI ===
            // Gọi BUS để Insert vào bảng Role trước, sau đó lấy ID mới về
            targetRoleId = roleBUS.insertRole(role);

            if (targetRoleId == 0) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Tên chức vụ có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return; // Dừng lại ngay, không lưu quyền nữa
            }
        } else {
            // === TRƯỜNG HỢP SỬA ===
            // Gọi BUS để Update tên Role (nếu người dùng có sửa tên)
            boolean updateSuccess = roleBUS.updateRole(role);

            if (!updateSuccess) {
                JOptionPane.showMessageDialog(this, "Cập nhật tên chức vụ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 4. Thu thập dữ liệu Checkbox từ Table (Giữ nguyên logic cũ)
        List<PermissionDTO> newPerms = new ArrayList<>();
        List<Integer> actionIds = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean isView = (boolean) tableModel.getValueAt(i, 1);
            boolean isAction = (boolean) tableModel.getValueAt(i, 2);

            ActionDTO action = listActions.get(i); // Lấy Action tương ứng dòng i

            // Tạo DTO quyền mới
            newPerms.add(new PermissionDTO(action.getActionCode(), isView, isAction));
            // Lấy ID của Action để lát nữa Insert vào bảng permission
            actionIds.add(action.getActionId());
        }

        // 5. GỌI TRANSACTION ĐỂ LƯU QUYỀN (Quan trọng: Dùng targetRoleId)
        // Nếu là Thêm mới: targetRoleId là ID vừa sinh ra.
        // Nếu là Sửa: targetRoleId là ID cũ.
        boolean success = permissionBUS.saveRolePermissions(targetRoleId, newPerms, actionIds);

        // 6. Thông báo kết quả
        if (success) {
            String msg = isAddMode ? "Thêm chức vụ mới thành công!" : "Cập nhật phân quyền thành công!";
            JOptionPane.showMessageDialog(this, msg);
            isSaved = true; // Cờ báo hiệu cho Panel cha biết để refresh lại bảng
            dispose();      // Đóng cửa sổ
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi lưu chi tiết quyền!", "Lỗi Fatal", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return isSaved; }
}