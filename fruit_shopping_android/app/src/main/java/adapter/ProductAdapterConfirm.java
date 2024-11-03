package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitshopping.UserActivity.ConfirmProductActivity;
import com.example.fruitshopping.R;

import java.util.List;

import model.CartItem;
import dtos.ProductDTO;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductAdapterConfirm extends BaseAdapter {

    private Context context;
    private List<CartItem> cartItems;

    public ProductAdapterConfirm(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_confirm_item, parent, false);
        }

        // Ánh xạ các view trong layout
        ImageView picture = convertView.findViewById(R.id.picture);
        TextView title = convertView.findViewById(R.id.title);
        TextView price = convertView.findViewById(R.id.price);
        TextView category = convertView.findViewById(R.id.category);
        TextView quantity = convertView.findViewById(R.id.quantity); // Ánh xạ quantity
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton); // Ánh xạ nút xóa

        // Lấy đối tượng CartItem hiện tại
        CartItem cartItem = cartItems.get(position);
        ProductDTO product = cartItem.getProduct();

        // Thiết lập thông tin sản phẩm vào các view
        title.setText(product.getName());
        price.setText(formatCurrency(product.getPrice())); // Đã sử dụng hàm formatCurrency
        category.setText(product.getCategoryName() != null ? product.getCategoryName() : "Không xác định");

        // Thiết lập số lượng
        int productQuantity = cartItem.getQuantity(); // Giả định bạn có phương thức getQuantity trong CartItem
        quantity.setText("Số lượng: " + productQuantity + "Kg");

        // Thiết lập hình ảnh (nếu có)
        int resId = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            picture.setImageResource(resId);
        } else {
            picture.setImageResource(R.drawable.placeholder);
        }

        // Gán OnClickListener cho nút xóa
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa sản phẩm khỏi giỏ hàng
                cartItems.remove(position); // Xóa khỏi danh sách
                notifyDataSetChanged(); // Cập nhật adapter
                ((ConfirmProductActivity) context).updateTotalPrice(); // Cập nhật tổng tiền
                Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show(); // Thông báo cho người dùng
            }
        });

        return convertView;
    }

    // Định dạng tiền tệ
    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return numberFormat.format(amount);
    }
}
