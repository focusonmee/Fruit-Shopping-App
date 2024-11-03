package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import model.Category;

public class CategoryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable.db"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 2; // Phiên bản cơ sở dữ liệu
    public static final String TABLE_NAME = "Category"; // Tên bảng
    private static final String COLUMN_ID = "id"; // Cột ID
    public static final String COLUMN_CATEGORY_NAME = "name"; // Cột tên danh mục
    public static final String COLUMN_IS_ACTIVE = "is_active"; // Cột trạng thái

    // Câu lệnh tạo bảng
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_NAME + " TEXT, " +
            COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1)"; // Thêm cột is_active

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

    // Phương thức thêm một danh mục mới
    public long addCategory(Category category) {
        long id = -1; // Khởi tạo ID với giá trị không hợp lệ
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, category.getName());
            values.put(COLUMN_IS_ACTIVE, category.getIsActive());

            // Chèn dòng mới
            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(); // Đóng cơ sở dữ liệu
        }
        return id; // Trả về ID của dòng mới
    }

    // Lấy tất cả các danh mục
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME));
                        int isActive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE));

                        // Thêm đối tượng Category vào danh sách
                        categoryList.add(new Category(id, name, isActive));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng con trỏ nếu không null
            }
            db.close(); // Đóng cơ sở dữ liệu
        }

        return categoryList;
    }

    // Cập nhật một danh mục
    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_IS_ACTIVE, category.getIsActive());

        // Cập nhật hàng trong cơ sở dữ liệu
        try {
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(category.getId())});
        } finally {
            db.close(); // Đóng cơ sở dữ liệu
        }
    }

    // Phương thức kiểm tra xem bảng có tồn tại không (không cần dùng trong trường hợp này)
    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Thêm dữ liệu mẫu (nếu cần thiết)
    private void initializeSampleData(SQLiteDatabase db) {
        // Bạn có thể thêm dữ liệu mẫu vào đây nếu cần
    }
}
