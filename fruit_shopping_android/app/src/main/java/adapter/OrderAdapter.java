package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fruitshopping.OrderDetailActivity;
import com.example.fruitshopping.R;

import java.util.List;

import model.Order;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;

    // Constructor
    public OrderAdapter(Context context, int resource, List<Order> orders) {
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
        holder.orderId.setText(order != null ?"Mã đơn hàng: "+ String.valueOf(order.getOrderId()) : "");
        holder.orderDate.setText(order != null ?"Thời gian đặt: "+ order.getOrderDate() : "");
        holder.orderTotal.setText(order != null ?"Tổng tiền: "+ String.valueOf(order.getTotalMoney()) : "");
        holder.orderStatus.setText(order != null ?"Trạng thái đơn hàng: "+ order.getStatus() : "");
        holder.orderNote.setText(order != null ?"Ghi chú: "+ order.getNote() : "");
        holder.orderShippingAddress.setText(order != null ?"Địa chỉ giao hàng: "+ order.getShippingAddress() : "");
        holder.orderShippingMethod.setText(order != null ?"Phương thức giao hàng: "+ order.getShippingMethod() : "");
        holder.orderPaymentMethod.setText(order != null ?"Phương thức thanh toán: "+ order.getPaymentMethod() : "");


        convertView.setOnClickListener(v -> {
            if (order != null) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrderId()); // Truyền orderId
                context.startActivity(intent);
            }
        });

        return convertView;


    }
}
