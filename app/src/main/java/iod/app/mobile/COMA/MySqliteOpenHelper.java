package iod.app.mobile.COMA;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by dnjsd on 2016-11-21.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private static MySqliteOpenHelper mySqLiteOpenHelper = null;
    public static final String DATABASE_NAME = "COSMETIC.db";
    public static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    public static MySqliteOpenHelper getInstance(Context context) {
        if(mySqLiteOpenHelper == null) {
            mySqLiteOpenHelper = new MySqliteOpenHelper(context);
        }
        return mySqLiteOpenHelper;
    }

    private MySqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE COSMETIC (cosmetic_id INTEGER PRIMARY KEY AUTOINCREMENT, cosmetic_ingredient TEXT, cosmetic_type TEXT, cosmetic_name TEXT, cosmetic_brand_name TEXT, cosmetic_volume INTEGER, cosmetic_image BLOB, cosmetic_rank REAL);");
        db.execSQL("CREATE TABLE MY_COSMETIC (cosmetic_brand_and_name TEXT, cosmetic_type TEXT, cosmetic_image BLOB, cosmetic_id INTEGER PRIMARY KEY, FOREIGN KEY(cosmetic_id) REFERENCES COSMETIC(cosmetic_id));");
        db.execSQL("CREATE TABLE COSMETIC_REVIEW (cosmetic_brand_name TEXT, cosmetic_name TEXT, review_name TEXT, review_content TEXT, cosmetic_duration INTEGER, cosmetic_rank REAL, cosmetic_id INTEGER PRIMARY KEY, FOREIGN KEY(cosmetic_id) REFERENCES COSMETIC(cosmetic_id));");
        db.execSQL("CREATE TABLE COSMETIC_RANKING (cosmetic_brand_name TEXT, cosmetic_name TEXT, cosmetic_rank REAL, cosmetic_volume INTEGER, cosmetic_review_count INTEGER, cosmetic_id INTEGER PRIMARY KEY, FOREIGN KEY(cosmetic_id) REFERENCES COSMETIC(cosmetic_id));");
        db.execSQL("CREATE TABLE ALARM (sound TEXT, vibrate TEXT, time TEXT);");

        db.execSQL("INSERT INTO COSMETIC VALUES(1,'샌들우드오일,선백리향오일,타라곤뿌리추출물','바디','Dirty Spray','Lush',250,'asdf',4.5)");
        db.execSQL("INSERT INTO COSMETIC VALUES(2,'로즈마리잎오일,페퍼민트오일','스킨케어','스킨밀크','우르오스',450,'asdf',3.5)");
        db.execSQL("INSERT INTO COSMETIC VALUES(3,'향','향수','아티샨 블랙','존바바토스',180,'asdf',4)");
        db.execSQL("INSERT INTO COSMETIC VALUES(4,'향','향수','아티샨','존바바토스',180,'asdf',4)");
        db.execSQL("INSERT INTO MY_COSMETIC VALUES('Lush Dirty Spray','바디','asdf',1)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('Lush','Dirty Spray', 4.5, 250, 345, 1)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('존바바토스','아티샨 블랙', 4, 180, 4251, 3)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('존바바토스','아티샨', 4, 180, 3421, 4)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('우르오스','스킨밀크', 3.5, 450, 495, 2)");
    }

    /*@
        db초기화 메서드
     */
    public void myclear() {
        db.execSQL("DROP TABLE MY_COSMETIC");
        db.execSQL("DROP TABLE COSMETIC");

        db.execSQL("CREATE TABLE COSMETIC (cosmetic_id INTEGER PRIMARY KEY AUTOINCREMENT, cosmetic_ingredient TEXT, cosmetic_type TEXT, cosmetic_name TEXT, cosmetic_brand_name TEXT, cosmetic_volume INTEGER, cosmetic_image BLOB, cosmetic_rank REAL);");
        db.execSQL("CREATE TABLE MY_COSMETIC (cosmetic_brand_and_name TEXT, cosmetic_type TEXT, cosmetic_image BLOB, cosmetic_id INTEGER PRIMARY KEY, FOREIGN KEY(cosmetic_id) REFERENCES COSMETIC(cosmetic_id));");

        db.execSQL("INSERT INTO COSMETIC VALUES(1,'샌들우드오일,선백리향오일,타라곤뿌리추출물','바디','Dirty Spray','Lush',250,'asdf',4.5)");
        db.execSQL("INSERT INTO COSMETIC VALUES(2,'로즈마리잎오일,페퍼민트오일','스킨케어','스킨밀크','우르오스',450,'asdf',3.5)");
        db.execSQL("INSERT INTO COSMETIC VALUES(3,'향','향수','아티샨 블랙','존바바토스',180,'asdf',4)");
        db.execSQL("INSERT INTO COSMETIC VALUES(4,'향','향수','아티샨','존바바토스',180,'asdf',4)");
        db.execSQL("INSERT INTO MY_COSMETIC VALUES('Lush Dirty Spray','바디','asdf',1)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('Lush','Dirty Spray', 4.5, 250, 345, 1)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('존바바토스','아티샨 블랙', 4, 180, 4251, 3)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('존바바토스','아티샨', 4, 180, 3421, 4)");
        db.execSQL("INSERT INTO COSMETIC_RANKING VALUES('우르오스','스킨밀크', 3.5, 450, 495, 2)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    /*
    public void dbReset() {
        db.execSQL("DROP TABLE MY_COSMETIC");
        db.execSQL("DROP TABLE COSMETIC");
        db.execSQL("CREATE TABLE COSMETIC (cosmetic_id INTEGER PRIMARY KEY AUTOINCREMENT, cosmetic_ingredient TEXT, cosmetic_type TEXT, cosmetic_name TEXT, cosmetic_brand_name TEXT, cosmetic_volume INTEGER, cosmetic_image BLOB, cosmetic_rank REAL);");
        db.execSQL("CREATE TABLE MY_COSMETIC (cosmetic_brand_and_name TEXT, cosmetic_type TEXT, cosmetic_image BLOB, cosmetic_id INTEGER PRIMARY KEY, FOREIGN KEY(cosmetic_id) REFERENCES COSMETIC(cosmetic_id));");
        db.execSQL("INSERT INTO COSMETIC VALUES(1,'샌들우드오일,선백리향오일,타라곤뿌리추출물','바디','Dirty Spray','Lush',250,'asdf',4.5)");
        db.execSQL("INSERT INTO COSMETIC VALUES(2,'로즈마리잎오일,페퍼민트오일','스킨케어','스킨밀크','우르오스',450,'asdf',3.5)");
        db.execSQL("INSERT INTO COSMETIC VALUES(3,'향','향수','아티샨 블랙','존바바토스',180,'asdf',4)");
        db.execSQL("INSERT INTO COSMETIC VALUES(4,'향','향수','아티샨','존바바토스',180,'asdf',4)");
        db.execSQL("INSERT INTO MY_COSMETIC VALUES('Lush Dirty Spray','바디','asdf',1)");
        db.execSQL("INSERT INTO MY_COSMETIC VALUES('우르오스 스킨밀크','스킨케어','asdf',2)");
    }*/

    public String getCosmeticData(int cosmetic_id) {
        String result = "";
        Cursor cur = db.rawQuery("SELECT * FROM COSMETIC WHERE cosmetic_id = "+ cosmetic_id,null);
        while (cur.moveToNext()) {
            result += cur.getString(4)
                    + "/"
                    + cur.getString(3)
                    + "/"
                    + cur.getDouble(7)
                    + "/"
                    + cur.getString(1);
        }
        return result;
    }

    public boolean checkMyCosmetic(int cosmetic_id) {
        Cursor cur = db.rawQuery("SELECT * FROM MY_COSMETIC WHERE cosmetic_id = "+ cosmetic_id,null);
        if(cur.moveToFirst() == false) {
            return true;
        }else {
            return false;
        }
    }

    public void deleteMyCosmetic(int cosmetic_id) {
        db.execSQL("DELETE FROM MY_COSMETIC WHERE cosmetic_id = " + cosmetic_id);
    }
    public String getCosmeticImageSrc(int cosmetic_id) {
        String result = "";
        Cursor cur = db.rawQuery("SELECT cosmetic_image FROM COSMETIC WHERE cosmetic_id = " + cosmetic_id,null);
        while (cur.moveToNext()) {
            result += cur.getString(0);
        }
        return result;
    }
    public String getCosmeticNameAndBrand() {
        String result = "";
        Cursor cur = db.rawQuery("SELECT * FROM COSMETIC ",null);
        while (cur.moveToNext()) {
            result += cur.getString(3)
                    + "/"
                    + cur.getString(4)
                    + "/"
                    + cur.getInt(0)
                    + "/"
                    + "\n";
        }
        return result;
    }

    public String getCosmeticNameAndVoluemAndType() {
        String result = "";
        Cursor cur = db.rawQuery("SELECT * FROM COSMETIC ",null);
        while (cur.moveToNext()) {
            result += cur.getString(3)
                    + "/"
                    + cur.getString(4)
                    + "/"
                    + cur.getInt(0)
                    + "/"
                    + cur.getInt(5)
                    + "/"
                    + cur.getString(2)
                    + "\n";
        }
        return result;
    }

    public String getAlarmData() {
        Cursor cur = db.rawQuery("SELECT * FROM ALARM",null);
        if(cur.moveToFirst() == false) {
            return "NoData";
        }else {
            cur.moveToLast();
            String data = cur.getString(0) + "/" + cur.getString(1) + "/" + cur.getString(2);
            return data;
        }
    }

    public void setAlarm(Boolean sound, Boolean vibrate, String time) {
        db.execSQL("INSERT INTO ALARM VALUES ('" + sound + "','" + vibrate + "','" + time + "')");
    }

    public void insertCosmetic(String bandname,String name, String type, String image, int cosmetic_id) {
        db.execSQL("INSERT INTO MY_COSMETIC VALUES('" + bandname + " " + name + "','" + type + "','" + image + "'," + cosmetic_id + ")");
    }
    public int getLastItemIndexFromMyItem(){
        Cursor cur = db.rawQuery("SELECT MAX(cosmetic_id) FROM MY_COSMETIC",null);
        cur.moveToFirst();
        return cur.getInt(0);
    }

    public String getCosmeticRanking() {
        String result = "";
        Cursor cur = db.rawQuery("SELECT * FROM COSMETIC_RANKING",null);
        while (cur.moveToNext()) {
            result += cur.getString(0)
                    + ","
                    + cur.getString(1)
                    + ":"
                    + cur.getDouble(2)
                    + ","
                    + cur.getInt(3)
                    + ","
                    + cur.getInt(4)
                    + ","
                    + cur.getInt(5)
                    + "\n";
        }
        return result;
    }

    public String getMyCosmetic() {
        String result = "";
        Cursor cur = db.rawQuery("SELECT * FROM MY_COSMETIC",null);
        int count = 0;
        while (cur.moveToNext()) {
            count++;
            result += cur.getString(0)
                    + ","
                    + cur.getString(1)
                    + ":"
                    + cur.getInt(3)
                    + "\n";
        }
        return result;
    }
}
