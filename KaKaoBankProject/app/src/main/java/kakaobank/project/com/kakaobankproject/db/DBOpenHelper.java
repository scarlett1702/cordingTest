package kakaobank.project.com.kakaobankproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB 생성 및 버전 관리
 * Created by sohee.park
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    /**
     * constructor
     * @param context
     * @param name DB 명
     * @param factory Custom Cursor 사용 시 지정, 기본은 null
     * @param version DB 버전
     */
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /** DB 처음 생성할 때 호출됨  */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성
        db.execSQL(DBConfig.CREATE_TABLE);
    }

    /** DB에 변경사항이 있을 경우, 테이블 삭제 후 다시 생성 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConfig.TABLE_NAME);

        onCreate(db);
    }
}
