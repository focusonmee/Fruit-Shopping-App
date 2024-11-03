package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fruitshopping.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import dtos.ProductDTO;

public class ProductAdapter extends ArrayAdapter<ProductDTO> {
    private Context context;
    private ArrayList<ProductDTO> products;

    public ProductAdapter(Context context, int resource, ArrayList<ProductDTO> products) {
        super(context, resource, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra nếu convertView chưa được khởi tạo
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.product_item, parent, false); // Đảm bảo sử dụng layout đúng
        }

        // Lấy sản phẩm hiện tại dựa vào vị trí
        ProductDTO product = products.get(position);

        // Ánh xạ các view trong layout
        ImageView picture = convertView.findViewById(R.id.picture);
        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        TextView price = convertView.findViewById(R.id.price);
        TextView category = convertView.findViewById(R.id.category);

        // Thiết lập hình ảnh sản phẩm
        try {
            int resId = 0; // Khởi tạo resId với giá trị mặc định
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                resId = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
            }

            // Nếu resId là 0 hoặc không tìm thấy tài nguyên, sử dụng hình ảnh mặc định
            if (resId == 0) {
                picture.setImageResource(R.drawable.placeholder);
            } else {
                picture.setImageResource(resId);
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ProductAdapter", "Error setting image resource", e);
            picture.setImageResource(R.drawable.placeholder); // Hình ảnh mặc định khi có lỗi
        }



        // Thiết lập dữ liệu vào các view
        title.setText(product.getName() != null ? product.getName() : "Không xác định");
        description.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");

        // Định dạng giá theo VND
        String formattedPrice = formatCurrency(product.getPrice());
        price.setText("Giá: " + formattedPrice + " 1Kg");

        category.setText(product.getCategoryName() != null ? product.getCategoryName() : "Không xác định");

        return convertView;
    }

    // Phương thức định dạng giá
    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return numberFormat.format(amount);
    }
}
