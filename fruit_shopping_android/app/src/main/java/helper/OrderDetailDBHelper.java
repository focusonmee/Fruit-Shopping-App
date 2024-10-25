package helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.OrderDetail;

public class OrderDetailDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "OrderDetail";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_QUANTITY = "quantity";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ORDER_ID + " INTEGER, " +
            COLUMN_PRODUCT_ID + " INTEGER, " +
            COLUMN_QUANTITY + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_ORDER_ID + ") REFERENCES OrderTable(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (" + COLUMN_PRODUCT_ID + ") REFERENCES Product(id) ON DELETE CASCADE" +
            ");";

    public OrderDetailDBHelper(@Nullable Context context) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm chi tiết đơn hàng
    public boolean addOrderDetail(int orderId, int productId, int quantity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ORDER_ID, orderId);
            values.put(COLUMN_PRODUCT_ID, productId);
            values.put(COLUMN_QUANTITY, quantity);

            long result = db.insert(TABLE_NAME, null, values);
            return result != -1;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Lấy tất cả chi tiết đơn hàng
//    public List<OrderDetail> getAllOrderDetails() {
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME;
//
//        Cursor cursor = db.rawQuery(query, null);
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
//            int orderId = cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID));
//            int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
//            int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
//
//            OrderDetail orderDetail = new OrderDetail(id, orderId, productId, quantity);
//            orderDetails.add(orderDetail);
//        }
//        cursor.close();
//        return orderDetails;
//    }

    // Lấy chi tiết theo orderId


    // Cập nhật chi tiết đơn hàng
    public boolean updateOrderDetail(int id, int orderId, int productId, int quantity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ORDER_ID, orderId);
            values.put(COLUMN_PRODUCT_ID, productId);
            values.put(COLUMN_QUANTITY, quantity);

            int result = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            return result > 0;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Xóa chi tiết đơn hàng
    public boolean deleteOrderDetail(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int rowsAffected = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            return rowsAffected > 0;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Lấy chi tiết đơn hàng theo orderId
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT od.id AS orderDetailId, od.order_id, od.product_id, od.quantity, p.name AS productName, p.price AS productPrice " +
                "FROM " + TABLE_NAME + " od " +
                "JOIN Product p ON od.product_id = p.id " +
                "WHERE od.order_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        // Lấy chỉ số cột và kiểm tra
        int orderDetailIdIndex = cursor.getColumnIndexOrThrow("orderDetailId");
        int productIdIndex = cursor.getColumnIndexOrThrow("product_id");
        int quantityIndex = cursor.getColumnIndexOrThrow("quantity");
        int productNameIndex = cursor.getColumnIndexOrThrow("productName");
        int productPriceIndex = cursor.getColumnIndexOrThrow("productPrice");

        // Kiểm tra tất cả các chỉ số cột có hợp lệ không
        if (orderDetailIdIndex != -1 && productIdIndex != -1 && quantityIndex != -1 &&
                productNameIndex != -1 && productPriceIndex != -1) {

            while (cursor.moveToNext()) {
                int orderDetailId = cursor.getInt(orderDetailIdIndex);
                int productId = cursor.getInt(productIdIndex);
                int quantity = cursor.getInt(quantityIndex);
                String productName = cursor.getString(productNameIndex);
                double productPrice = cursor.getDouble(productPriceIndex);

                // Tạo đối tượng OrderDetail mới với thông tin cần thiết
                OrderDetail orderDetail = new OrderDetail(orderDetailId, quantity, productId, orderId);
                orderDetail.setProductName(productName); // Thêm tên sản phẩm
                orderDetail.setProductPrice(productPrice); // Thêm giá sản phẩm
                orderDetails.add(orderDetail);
            }
        }
        cursor.close();
        return orderDetails;
    }


}

