package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.fruitshopping.R;

import java.util.List;

import dtos.ProductDTO;
import helper.ProductDBHelper;
import model.Product; // Đảm bảo model Product có các phương thức cần thiết

public class ProductManagementAdapter extends BaseAdapter {

    private List<ProductDTO> productList;
    private Context context;
    private ProductDBHelper databaseHelper; // Thêm biến để truy cập cơ sở dữ liệu

    public ProductManagementAdapter(Context context, List<ProductDTO> productList) {
        this.context = context;
        this.productList = productList;
        this.databaseHelper = new ProductDBHelper(context); // Khởi tạo ProductDatabaseHelper
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_admin_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.editTextId = convertView.findViewById(R.id.editTextId);
            viewHolder.editTextName = convertView.findViewById(R.id.editTextName);
            viewHolder.editTextDescription = convertView.findViewById(R.id.editTextDescription);
            viewHolder.editTextPrice = convertView.findViewById(R.id.editTextPrice);
            viewHolder.editTextBanner = convertView.findViewById(R.id.editTextBanner);
            viewHolder.editTextImage = convertView.findViewById(R.id.editTextImage);
            viewHolder.textViewCategoryId = convertView.findViewById(R.id.textViewCategoryId);
            viewHolder.btnUpdate = convertView.findViewById(R.id.btnUpdate);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductDTO product = productList.get(position);

        // Thiết lập thông tin vào các View
        viewHolder.editTextId.setText(String.valueOf(product.getId()));
        viewHolder.editTextName.setText(product.getName());
        viewHolder.editTextDescription.setText(product.getDescription());
        viewHolder.editTextPrice.setText(String.valueOf(product.getPrice()));
        viewHolder.editTextBanner.setText(product.getBanner());
        viewHolder.editTextImage.setText(product.getImage());
        viewHolder.textViewCategoryId.setText("Loại sản phẩm: " + product.getCategoryName());

        viewHolder.btnUpdate.setOnClickListener(v -> {
            // Cập nhật thông tin sản phẩm trong cơ sở dữ liệu
            product.setName(viewHolder.editTextName.getText().toString());
            product.setDescription(viewHolder.editTextDescription.getText().toString());
            product.setPrice(Double.parseDouble(viewHolder.editTextPrice.getText().toString()));
            product.setBanner(viewHolder.editTextBanner.getText().toString());
            product.setImage(viewHolder.editTextImage.getText().toString());

            // Cập nhật vào DB
            boolean isUpdated = databaseHelper.updateProduct(product);
            if (isUpdated) {
                Toast.makeText(context, "Cập nhật sản phẩm: " + product.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.btnDelete.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận trước khi xóa
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm: " + product.getName() + " không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Kiểm tra xem sản phẩm có đơn hàng không trước khi xóa
                        if (databaseHelper.hasOrders(product.getId())) {
                            Toast.makeText(context, "Không thể xóa sản phẩm: " + product.getName() + " vì đã có đơn hàng.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xóa sản phẩm trong danh sách và cơ sở dữ liệu
                            boolean isDeleted = databaseHelper.deleteProduct(product.getId());
                            if (isDeleted) {
                                productList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Đã xóa sản phẩm: " + product.getName(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        return convertView;
    }

    private static class ViewHolder {
        EditText editTextId;
        EditText editTextName;
        EditText editTextDescription;
        EditText editTextPrice;
        EditText editTextBanner;
        EditText editTextImage;
        TextView textViewCategoryId;
        Button btnUpdate;
        Button btnDelete;
    }

    public void updateProductList(List<ProductDTO> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        notifyDataSetChanged();
    }
}
