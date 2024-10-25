package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 2; // Phiên bản cơ sở dữ liệu
    public static final String TABLE_NAME = "Category"; // Tên bảng
    private static final String COLUMN_ID = "id"; // Cột ID
    public static final String COLUMN_CATEGORY_NAME = "name"; // Cột tên danh mục

    // Câu lệnh tạo bảng
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_NAME + " TEXT)";

    // Constructor
    public CategoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE); // Thực thi câu lệnh tạo bảng
        initializeSampleData(db); // Gọi phương thức để thêm dữ liệu mẫu


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Xóa bảng cũ
        onCreate(db); // Tạo bảng mới
    }

    // Phương thức kiểm tra xem bảng có tồn tại không
    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Phương thức thêm dữ liệu mẫu cho Category
    private void initializeSampleData(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

    }


    // Thêm một danh mục mới
    private void addCategory(SQLiteDatabase db, String categoryName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName); // Thêm tên danh mục vào values
        db.insert(TABLE_NAME, null, values); // Thực hiện chèn vào bảng
    }

}
