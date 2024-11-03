package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitshopping.adminActivity.OrderAdminHistoryActivity;
import com.example.fruitshopping.R;

import java.util.ArrayList;
import java.util.List;

import helper.AccountDBHelper;
import model.Account;

public class UserManagementAdapter extends BaseAdapter {
    private Context context;
    private List<Account> userList;
    private AccountDBHelper dbHelper;

    // Constructor
    public UserManagementAdapter(Context context, List<Account> accounts, String loggedInAdminEmail) {
        this.context = context;
        this.dbHelper = new AccountDBHelper(context);
        this.userList = new ArrayList<>();

        // Lọc danh sách để loại bỏ tài khoản admin hiện tại
        for (Account account : accounts) {
            if (!account.getEmail().equals(loggedInAdminEmail)) {
                this.userList.add(account);
            }
        }
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_management_item, parent, false);
        }

        // Lấy đối tượng người dùng hiện tại
        Account user = userList.get(position);

        // Liên kết dữ liệu với các TextView và Spinner
        TextView userIdTextView = convertView.findViewById(R.id.user_id);
        TextView userNameTextView = convertView.findViewById(R.id.user_name);
        TextView phoneNumberTextView = convertView.findViewById(R.id.phone_number);
        TextView addressTextView = convertView.findViewById(R.id.address);
        TextView emailTextView = convertView.findViewById(R.id.email);
        Spinner spinnerActive = convertView.findViewById(R.id.spinnerActive);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdate);

        // Thiết lập nội dung cho các TextView
        userIdTextView.setText("ID: " + user.getId());
        userNameTextView.setText("Tên: " + user.getName());
        phoneNumberTextView.setText("SĐT: " + user.getPhone());
        addressTextView.setText("Địa chỉ: " + user.getAddress());
        emailTextView.setText("Email: " + user.getEmail());

        // Tạo ArrayAdapter từ string-array cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.account_active,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActive.setAdapter(adapter);

        // Thiết lập giá trị mặc định cho Spinner dựa vào is_active
        if (user.getIsActive() == 1) {
            // "Kích hoạt tài khoản" nằm ở vị trí 0
            spinnerActive.setSelection(0); // 0 là vị trí của "Kích hoạt tài khoản"
        } else {
            // "Hủy kích hoạt tài khoản" nằm ở vị trí 1
            spinnerActive.setSelection(1); // 1 là vị trí của "Hủy kích hoạt tài khoản"
        }

        // Lắng nghe sự kiện khi nút "Cập Nhật" được nhấn
        btnUpdate.setOnClickListener(v -> {
            String selectedOption = spinnerActive.getSelectedItem().toString();

            if ("Kích hoạt".equals(selectedOption)) {
                dbHelper.activateAccount(user.getEmail());
                Toast.makeText(context, "Tài khoản đã được kích hoạt", Toast.LENGTH_SHORT).show();
            } else if ("Hủy kích hoạt".equals(selectedOption)) {
                dbHelper.deleteAccount(user.getEmail());
                Toast.makeText(context, "Tài khoản đã bị hủy kích hoạt", Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderAdminHistoryActivity.class); // Thay OrderActivity bằng tên Activity tương ứng
            intent.putExtra("user_id", user.getId());
            intent.putExtra("name", user.getName());// Chuyển ID của tài khoản
            context.startActivity(intent);
        });

        return convertView;
    }
}
