package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import model.Account;

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
    private static final String COLUMN_IS_ACTIVE ="is_active";
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
        db.execSQL(CREATE_TABLE); // Tạo bảng khi lần đầu tiên khởi tạo cơ sở dữ liệu
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
        values.put(COLUMN_ROLE_ID, roleId); // Thêm roleId
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAccountByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password = ?",
                new String[]{email, password}
        );
        return cursor;
    }

    public boolean isAccountExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"email"}, "email = ?", new String[]{email}, null, null, null);
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

    public void activateAccount(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_active", 1); // Đặt is_active = 1

        // Cập nhật is_active = 1 cho tài khoản có email tương ứng
        db.update(TABLE_NAME, values, "email = ?", new String[]{email});
        db.close();
    }


    public List<Account> getAllUsers() {
        List<Account> accountList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Chỉ định các cột mà bạn muốn truy vấn
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_PHONE,
                COLUMN_ADDRESS,
                COLUMN_EMAIL,
                COLUMN_PASSWORD,
                COLUMN_ROLE_ID,
                COLUMN_IS_ACTIVE
        };

        Cursor cursor = null;
        try {
            // Thực hiện truy vấn
            cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

            // Kiểm tra nếu cursor không null và di chuyển đến bản ghi đầu tiên
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Lấy chỉ số cột
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                    int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                    int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                    int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                    int roleIdIndex = cursor.getColumnIndex(COLUMN_ROLE_ID);
                    int isActiveIndex = cursor.getColumnIndex(COLUMN_IS_ACTIVE);

                    // Tạo đối tượng Account và thêm vào danh sách
                    Account account = new Account(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(phoneIndex),
                            cursor.getString(addressIndex),
                            cursor.getString(emailIndex),
                            cursor.getString(passwordIndex),
                            cursor.getInt(roleIdIndex),
                            cursor.getInt(isActiveIndex)
                    );

                    accountList.add(account);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi nếu có
        } finally {
            // Đảm bảo rằng cursor và database được đóng
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return accountList; // Trả về danh sách người dùng
    }

}
