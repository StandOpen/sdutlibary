package hleper;

import android.content.ContentValues;  
import android.content.Context;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteDatabase.CursorFactory;  
import android.database.sqlite.SQLiteOpenHelper;  
  
public class DBHelper extends SQLiteOpenHelper {     
      
    public static final String TB_NAME = "people";     
    public static final String ID = "_id";     
    public static final String NAME = "name";     
    public static final String Url = "url";   
      
    public DBHelper(Context context, String name,      
            CursorFactory factory, int version) {     
        super(context, name, factory, version);  
        this.getWritableDatabase();    
    }  
      
    /** 
     * should be invoke when you never use DBhelper 
     * To release the database and etc. 
     */  
    public void Close() {  
        this.getWritableDatabase().close();  
    }  
    
    public void onCreate(SQLiteDatabase db) {     
        db.execSQL("CREATE TABLE IF NOT EXISTS "      
                + TB_NAME + " ("      
                + ID + " INTEGER PRIMARY KEY,"      
                + NAME + " VARCHAR,"    
                + Url + " VARCHAR)");     
    }     
    
    public void onUpgrade(SQLiteDatabase db,      
            int oldVersion, int newVersion) {     
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);     
        onCreate(db);     
    }  
      
    public void addPeople(String name, String number) {  
        ContentValues values = new ContentValues();     
        values.put(DBHelper.NAME, name);     
        values.put(DBHelper.Url, number);     
        this.getWritableDatabase().insert(  
                DBHelper.TB_NAME, DBHelper.ID, values);    
    }  
      
    @SuppressWarnings("static-access")
	public void delPeople(int id) {  
        this.getWritableDatabase().delete(  
                DBHelper.TB_NAME, this.ID + " = " + id, null);  
    }  
      
    public void delAllPeople() {  
        this.getWritableDatabase().delete(  
                DBHelper.TB_NAME, null, null);  
    }  
}    
