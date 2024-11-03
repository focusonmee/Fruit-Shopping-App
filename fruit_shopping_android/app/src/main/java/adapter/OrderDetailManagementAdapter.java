package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fruitshopping.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import model.OrderDetail;

public class OrderDetailManagementAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDetail> orderDetailList;

    public OrderDetailManagementAdapter(Context context, List<OrderDetail> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @Override
    public int getCount() {
        return orderDetailList != null ? orderDetailList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return orderDetailList != null ? orderDetailList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return orderDetailList != null ? orderDetailList.get(position).getOrderDetailId() : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // Sử dụng layout orderdetail_management_item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.orderdetail_management_item, parent, false);

            holder = new ViewHolder();
            holder.textViewProductName = convertView.findViewById(R.id.name);
            holder.textViewProductPrice = convertView.findViewById(R.id.price);
            holder.textViewQuantity = convertView.findViewById(R.id.quantity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin chi tiết đơn hàng
        OrderDetail orderDetail = orderDetailList.get(position);

        // Định dạng giá tiền thành tiền tệ
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(orderDetail.getProductPrice());

        // Thiết lập dữ liệu cho các TextView
        holder.textViewProductName.setText("Tên sản phẩm: " + orderDetail.getProductName());
        holder.textViewProductPrice.setText("Giá: " + formattedPrice);
        holder.textViewQuantity.setText("Số lượng: " + orderDetail.getQuantity());

        return convertView;
    }

    static class ViewHolder {
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewQuantity;
    }
}
