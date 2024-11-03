package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitshopping.R;

import java.util.List;

import helper.CategoryDBHelper;
import model.Category;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categoryList;
    private LayoutInflater inflater;

    // Tạo danh sách các trạng thái
    private String[] statuses = {"Kích hoạt", "Hủy kích hoạt"};

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return categoryList != null ? categoryList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_item, parent, false);
            holder = new ViewHolder();
            holder.textViewId = convertView.findViewById(R.id.category_id);
            holder.editTextName = convertView.findViewById(R.id.user_name);
            holder.spinnerStatus = convertView.findViewById(R.id.category_status);
            holder.btnUpdate = convertView.findViewById(R.id.btnUpdate); // Ánh xạ nút Cập Nhật
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy category hiện tại
        Category category = categoryList.get(position);
        holder.textViewId.setText("ID: " + category.getId());
        holder.editTextName.setText(category.getName());

        // Thiết lập ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerStatus.setAdapter(adapter);

        // Thiết lập giá trị Spinner dựa trên is_active
        int statusPosition = category.getIsActive() == 1 ? 0 : 1; // 0 cho "Kích hoạt", 1 cho "Hủy kích hoạt"
        holder.spinnerStatus.setSelection(statusPosition);

        // Thêm sự kiện nhấp vào nút Cập Nhật
        // Trong phương thức setOnClickListener của nút Cập Nhật
        holder.btnUpdate.setOnClickListener(v -> {
            // Lấy tên và trạng thái từ các trường
            String updatedName = holder.editTextName.getText().toString().trim();
            int selectedStatus = holder.spinnerStatus.getSelectedItemPosition(); // 0: Kích hoạt, 1: Hủy kích hoạt
            int isActive = (selectedStatus == 0) ? 1 : 0; // Cập nhật isActive dựa trên vị trí đã chọn

            // Cập nhật thông tin category
            category.setName(updatedName);
            category.setIsActive(isActive);

            // Tạo instance của CategoryDBHelper
            CategoryDBHelper dbHelper = new CategoryDBHelper(context);
            // Gọi phương thức cập nhật
            dbHelper.updateCategory(category);

            // Hiển thị thông báo cập nhật thành công
            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });


        return convertView;
    }

    private static class ViewHolder {
        TextView textViewId;
        EditText editTextName;
        Spinner spinnerStatus;
        Button btnUpdate; // Thêm nút Cập Nhật vào ViewHolder
    }
}
