package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable.db";
    private static final int DATABASE_VERSION = 2; // Incremented the database version
    private static final String TABLE_NAME = "Account";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE_ID = "role_id";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_PHONE + " TEXT, " +
            COLUMN_ADDRESS + " TEXT, " +
            COLUMN_EMAIL + " TEXT UNIQUE, " +
            COLUMN_PASSWORD + " TEXT, " +
            COLUMN_ROLE_ID + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_ROLE_ID + ") REFERENCES Role(id))";

    public AccountDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("DELETE FROM " + TABLE_NAME); // Xóa tất cả dữ liệu trong bảng
//        db.execSQL("INSERT OR IGNORE INTO Role (id, name) VALUES (1, 'Admin')");
//        db.execSQL("INSERT OR IGNORE INTO Role (id, name) VALUES (2, 'Customer')");
//        db.execSQL("INSERT OR IGNORE INTO " + TABLE_NAME + " (name, phone, address, email, password, role_id) " +
//                "VALUES ('Admin User', '123456789', 'Admin Address', 'admin@example.com', 'adminpass', 1)");
//        db.execSQL("INSERT OR IGNORE INTO " + TABLE_NAME + " (name, phone, address, email, password, role_id) " +
//                "VALUES ('Customer User', '987654321', 'Customer Address', 'customer@example.com', 'customerpass', 2)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS Role"); // Đảm bảo bảng Role cũng được xóa
        onCreate(db);
    }

    public boolean addAccount(String name, String phone, String address, String email, String password, int roleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE_ID, roleId);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAccountByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password = ? AND is_active = 1",
                new String[]{email, password}
        );
        return cursor;
    }


    public boolean isAccountExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Account", new String[]{"email"}, "email = ?", new String[]{email}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean updateAccount(String name, String phone, String address, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ADDRESS, address);

        // Cập nhật tài khoản theo email
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        // Cập nhật mật khẩu theo email
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
    }
    public Cursor getAccountByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
    }

    public void deleteAccount(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_active", 0); // Đặt is_active = 0

        // Cập nhật is_active = 0 cho tài khoản có email tương ứng
        db.update(TABLE_NAME, values, "email = ?", new String[]{email});
        db.close();
    }


}
