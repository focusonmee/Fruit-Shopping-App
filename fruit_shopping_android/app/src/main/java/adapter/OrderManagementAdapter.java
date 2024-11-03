package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fruitshopping.R;
import com.example.fruitshopping.adminActivity.OrderDetailAdminActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import model.Order;

public class OrderManagementAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;

    // Constructor
    public OrderManagementAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders != null ? orders.size() : 0; // Tránh lỗi NullPointerException
    }

    @Override
    public Object getItem(int position) {
        return orders != null ? orders.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder giúp tối ưu việc tái sử dụng View
    private static class ViewHolder {
        TextView orderId;
        TextView orderDate;
        TextView orderTotal;
        TextView orderStatus;
        TextView orderNote;
        TextView orderShippingAddress;
        TextView orderShippingMethod;
        TextView orderPaymentMethod;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Nếu convertView chưa tồn tại, inflate nó và tạo ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);

            // Tạo ViewHolder và ánh xạ các thành phần UI
            holder = new ViewHolder();
            holder.orderId = convertView.findViewById(R.id.orderId);
            holder.orderDate = convertView.findViewById(R.id.orderDate);
            holder.orderTotal = convertView.findViewById(R.id.orderTotal);
            holder.orderStatus = convertView.findViewById(R.id.orderStatus);
            holder.orderNote = convertView.findViewById(R.id.orderNote);
            holder.orderShippingAddress = convertView.findViewById(R.id.orderShippingAddress);
            holder.orderShippingMethod = convertView.findViewById(R.id.orderShippingMethod);
            holder.orderPaymentMethod = convertView.findViewById(R.id.orderPaymentMethod);

            // Lưu ViewHolder vào convertView
            convertView.setTag(holder);
        } else {
            // Lấy ViewHolder đã được lưu từ convertView
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy dữ liệu từ đối tượng Order
        Order order = orders.get(position);

        // Đặt dữ liệu lên các thành phần UI, kiểm tra null để tránh lỗi
        if (order != null) {
            holder.orderId.setText("Mã đơn hàng: " + order.getOrderId());
            holder.orderDate.setText("Thời gian đặt: " + order.getOrderDate());

            // Định dạng tổng tiền thành dạng tiền tệ
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotal = currencyFormat.format(order.getTotalMoney());
            holder.orderTotal.setText("Tổng tiền: " + formattedTotal);

            holder.orderStatus.setText("Trạng thái đơn hàng: " + order.getStatus());
            holder.orderNote.setText("Ghi chú: " + order.getNote());
            holder.orderShippingAddress.setText("Địa chỉ giao hàng: " + order.getShippingAddress());
            holder.orderShippingMethod.setText("Phương thức giao hàng: " + order.getShippingMethod());
            holder.orderPaymentMethod.setText("Phương thức thanh toán: " + order.getPaymentMethod());
        }

        // Sự kiện khi item được click
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailAdminActivity.class);
            intent.putExtra("orderId", order.getOrderId()); // Truyền orderId
            context.startActivity(intent);
        });

        return convertView;
    }
}
