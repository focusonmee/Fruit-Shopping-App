package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dtos.ProductDTO;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable.db";
    private static final int DATABASE_VERSION = 2; // Tăng giá trị phiên bản cơ sở dữ liệu
    private static final String TABLE_NAME = "Product";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_BANNER = "banner";
    private static final String COLUMN_IMAGE = "image";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_CATEGORY_ID + " INTEGER, " +
            COLUMN_PRICE + " REAL NOT NULL, " +
            COLUMN_BANNER + " TEXT, " +
            COLUMN_IMAGE + " TEXT)";

    public ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        initializeSampleData(db);
    }

    private void initializeSampleData(SQLiteDatabase db) {
        // Kiểm tra xem bảng có dữ liệu hay không
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            // Thêm dữ liệu mẫu
            addSampleProduct(db, "Cà chua", "Cà chua sạch", 1, 20000.0, "cachuabanner", "cachua");
            addSampleProduct(db, "Cà rốt", "Cà rốt tươi ngon", 1, 15000.0, "carotbanner", "carot");
            addSampleProduct(db, "Rau muống", "Rau muống sạch", 2, 10000.0, "raumuongbanner", "raumuong");
            addSampleProduct(db, "Cà pháo", "Cà pháo đẹp", 1, 12000.0, "caphaobanner", "caphao");
            addSampleProduct(db, "Cà tím", "Cà bao tím", 1, 17000.0, "catimbanner", "catim");
        }
    }

    private void addSampleProduct(SQLiteDatabase db, String name, String description, int categoryId, double price, String banner, String image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY_ID, categoryId);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_BANNER, banner);
        values.put(COLUMN_IMAGE, image);
        db.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Xóa bảng cũ
            onCreate(db); // Tạo bảng mới
        }
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Xóa bảng cũ
        onCreate(db); // Tạo bảng mới
    }

    public ArrayList<ProductDTO> getAllProducts() {
        ArrayList<ProductDTO> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Product.*, Category.name AS category_name FROM Product JOIN Category ON Product.category_id = Category.id", null);

        if (cursor.moveToFirst()) {
            do {
                ProductDTO product = new ProductDTO();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setBanner(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BANNER)));
                product.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                product.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow("category_name"))); // Thêm trường category_name

                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Đảm bảo đóng cursor
        db.close(); // Đóng cơ sở dữ liệu
        return productList;
    }
    public ArrayList<String> getAllCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM categories", null); // Thay "categories" với tên bảng của bạn

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name")); // Thay "name" với tên cột của bạn
                categoryNames.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categoryNames;
    }
    public boolean addProduct(String name, double price, String banner, String image, String description, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("banner", banner);
        values.put("image", image);
        values.put("description", description);
        values.put("category_id", categoryId); // ID của danh mục sản phẩm

        long result = db.insert("Product", null, values); // Thêm sản phẩm vào bảng "products"
        db.close();
        return result != -1; // Trả về true nếu thêm thành công, false nếu thất bại
    }

    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn tất cả sản phẩm
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();

                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int descriptionIndex = cursor.getColumnIndex("description");
                    int priceIndex = cursor.getColumnIndex("price");
                    int bannerIndex = cursor.getColumnIndex("banner");
                    int imageIndex = cursor.getColumnIndex("image");

                    // Kiểm tra xem chỉ số cột có hợp lệ không
                    if (idIndex != -1) {
                        product.setId(cursor.getInt(idIndex));
                    }
                    if (nameIndex != -1) {
                        product.setName(cursor.getString(nameIndex));
                    }
                    if (descriptionIndex != -1) {
                        product.setDescription(cursor.getString(descriptionIndex));
                    }
                    if (priceIndex != -1) {
                        product.setPrice(cursor.getDouble(priceIndex));
                    }
                    if (bannerIndex != -1) {
                        product.setBanner(cursor.getString(bannerIndex));
                    }
                    if (imageIndex != -1) {
                        product.setImage(cursor.getString(imageIndex));
                    }

                    productList.add(product);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close(); // Đóng cơ sở dữ liệu
        return productList; // Trả về danh sách sản phẩm
    }
    public boolean updateProduct(ProductDTO product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getImage());
        values.put(COLUMN_BANNER, product.getBanner());
        values.put(COLUMN_CATEGORY_ID, product.getCategoryId());

        // Cập nhật sản phẩm
        int result = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        return result > 0; // Trả về true nếu cập nhật thành công
    }
    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Xóa sản phẩm
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        return result > 0; // Trả về true nếu xóa thành công
    }

    // Phương thức kiểm tra xem sản phẩm có đơn hàng không
    public boolean hasOrders(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + "OrderDetail" + " WHERE " + "product_id" + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});
        boolean hasOrders = false;

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            hasOrders = count > 0; // Nếu có đơn hàng, count sẽ lớn hơn 0
        }
        cursor.close();
        return hasOrders;
    }

}
