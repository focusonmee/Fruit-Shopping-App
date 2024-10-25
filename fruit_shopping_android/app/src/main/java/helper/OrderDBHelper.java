package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.OrderDetail;

public class OrderDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "Orders"; // Đổi tên bảng thành "Orders" để tránh xung đột từ khóa
    private static final String COLUMN_ID = "id"; // Thay đổi tên cột cho phù hợp với Order
    private static final String COLUMN_USER_ID = "user_id"; // userId từ lớp Order
    private static final String COLUMN_TOTAL_MONEY = "total_money"; // totalMoney từ lớp Order
    private static final String COLUMN_STATUS = "status"; // status từ lớp Order
    private static final String COLUMN_NOTE = "note"; // note từ lớp Order
    private static final String COLUMN_SHIPPING_ADDRESS = "shipping_address"; // shippingAddress từ lớp Order
    private static final String COLUMN_SHIPPING_METHOD = "shipping_method"; // shippingMethod từ lớp Order
    private static final String COLUMN_PAYMENT_METHOD = "payment_method"; // paymentMethod từ lớp Order
    private static final String COLUMN_IS_ACTIVE = "is_active"; // isActive từ lớp Order
    private static final String COLUMN_ORDER_DATE = "orderDate"; // orderDate từ lớp Order
    private OrderDetailDBHelper orderDetailDBHelper;
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " INTEGER, " +
            COLUMN_TOTAL_MONEY + " REAL NOT NULL, " +
            COLUMN_STATUS + " TEXT, " +
            COLUMN_NOTE + " TEXT, " +
            COLUMN_SHIPPING_ADDRESS + " TEXT, " +
            COLUMN_SHIPPING_METHOD + " TEXT, " +
            COLUMN_PAYMENT_METHOD + " TEXT, " +
            COLUMN_IS_ACTIVE + " INTEGER NOT NULL, " +
            COLUMN_ORDER_DATE + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES Account(id) ON DELETE CASCADE" + // Giả định có bảng Account
            ");";

    public OrderDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        // Nếu bạn muốn tạo bảng khác, hãy thêm câu lệnh tạo bảng ở đây
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addOrder(int userId, float totalMoney, String status, String note,
                         String shippingAddress, String shippingMethod, String paymentMethod,
                         int isActive, String orderDate) {
        SQLiteDatabase db = null;
        long orderId = -1;  // Khởi tạo giá trị mặc định cho orderId
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_TOTAL_MONEY, totalMoney);
            values.put(COLUMN_STATUS, status);
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_SHIPPING_ADDRESS, shippingAddress);
            values.put(COLUMN_SHIPPING_METHOD, shippingMethod);
            values.put(COLUMN_PAYMENT_METHOD, paymentMethod);
            values.put(COLUMN_IS_ACTIVE, isActive);
            values.put(COLUMN_ORDER_DATE, orderDate);

            // Thực hiện insert và lấy ID của đơn hàng vừa thêm
            orderId = db.insert(TABLE_NAME, null, values);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return orderId;  // Trả về ID của đơn hàng
    }


    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Kiểm tra userId hợp lệ
            if (userId <= 0) {
                Log.e("getOrdersByUserId", "Invalid userId: " + userId);
                return orders; // Trả về danh sách rỗng nếu userId không hợp lệ
            }

            // Thực hiện truy vấn
            cursor = db.query(TABLE_NAME, null, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);

            // Kiểm tra kết quả truy vấn
            if (cursor != null) {
                Log.d("getOrdersByUserId", "Number of orders found: " + cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        int orderIdIndex = cursor.getColumnIndex(COLUMN_ID);
                        int totalMoneyIndex = cursor.getColumnIndex(COLUMN_TOTAL_MONEY);
                        int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
                        int noteIndex = cursor.getColumnIndex(COLUMN_NOTE);
                        int shippingAddressIndex = cursor.getColumnIndex(COLUMN_SHIPPING_ADDRESS);
                        int shippingMethodIndex = cursor.getColumnIndex(COLUMN_SHIPPING_METHOD);
                        int paymentMethodIndex = cursor.getColumnIndex(COLUMN_PAYMENT_METHOD);
                        int isActiveIndex = cursor.getColumnIndex(COLUMN_IS_ACTIVE);
                        int orderDateIndex = cursor.getColumnIndex(COLUMN_ORDER_DATE);

                        // Kiểm tra chỉ số cột
                        if (orderIdIndex != -1 && totalMoneyIndex != -1 && statusIndex != -1 &&
                                noteIndex != -1 && shippingAddressIndex != -1 &&
                                shippingMethodIndex != -1 && paymentMethodIndex != -1 &&
                                isActiveIndex != -1 && orderDateIndex != -1) {

                            int orderId = cursor.getInt(orderIdIndex);
                            float totalMoney = cursor.getFloat(totalMoneyIndex);
                            String status = cursor.getString(statusIndex);
                            String note = cursor.getString(noteIndex);
                            String shippingAddress = cursor.getString(shippingAddressIndex);
                            String shippingMethod = cursor.getString(shippingMethodIndex);
                            String paymentMethod = cursor.getString(paymentMethodIndex);
                            int isActive = cursor.getInt(isActiveIndex);
                            String orderDate = cursor.getString(orderDateIndex);

                            // Tạo một đối tượng Order
                            Order order = new Order(orderId, userId, totalMoney, status, note, shippingAddress, shippingMethod, paymentMethod, isActive, orderDate, null); // orderDetails là null vì không cần lấy
                            orders.add(order);
                        }
                    } while (cursor.moveToNext());
                } else {
                    Log.e("getOrdersByUserId", "No orders found for userId: " + userId);
                }
            } else {
                Log.e("getOrdersByUserId", "Cursor is null.");
            }
        } catch (Exception e) {
            Log.e("getOrdersByUserId", "Error retrieving orders: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return orders; // Trả về danh sách đơn hàng
    }






}
