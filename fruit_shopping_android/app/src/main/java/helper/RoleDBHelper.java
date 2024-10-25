package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RoleDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Vegetable.db";
    private static final int DATABASE_VERSION = 2; // Incremented the database version
    private static final String TABLE_NAME = "Role";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT)";

    public RoleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

        // Insert default roles into the Role table
        db.execSQL("INSERT INTO " + TABLE_NAME + " (id, name) VALUES (1, 'Admin')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (id, name) VALUES (2, 'Customer')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addRole(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }
}
