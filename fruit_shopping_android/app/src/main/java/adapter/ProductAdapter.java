package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fruitshopping.R;

import java.util.ArrayList;

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

        // Thiết lập dữ liệu vào các view
        int resId = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            picture.setImageResource(resId); // Kiểm tra xem resource có tồn tại không
        } else {
            picture.setImageResource(R.drawable.ic_launcher_background); // Đặt hình ảnh mặc định nếu không tìm thấy
        }

        title.setText(product.getName());
        description.setText(product.getDescription());
        price.setText("Giá: " + product.getPrice() + " VND");
        category.setText(product.getCategoryName() != null ? product.getCategoryName() : "Không xác định");

        return convertView;
    }



}
