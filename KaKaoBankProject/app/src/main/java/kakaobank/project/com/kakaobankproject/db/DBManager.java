package kakaobank.project.com.kakaobankproject.db;

import java.util.ArrayList;

import kakaobank.project.com.kakaobankproject.Utils;
import kakaobank.project.com.kakaobankproject.model.ImageListItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 테이블 관리
 *  - INSERT, DELETE, UPDATE, SELECT
 *
 * Created by sohee.park
 */
public class DBManager {

    private Context mContext;
    private SQLiteDatabase mDB = null;
    private DBOpenHelper mDBHelper = null;

    /** */
    public DBManager(Context context) {
        this.mContext = context;
    }

    /**
     * DB 열기
     * @return
     * @throws SQLException
     */
    public DBManager open() throws SQLException {
        mDBHelper = new DBOpenHelper(mContext, DBConfig.DATABASE_NAME, null, DBConfig.DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase(); // DB 쓰기 허용
        return this;
    }

    /**
     * DB 닫기
     */
    public void close() {
        mDB.close();
    }

    /**
     * 레코드 추가, 각 썸네일과 이미지의 URL만 저장한다
     * @param item
     * @return true: 추가 성공
     */
    public boolean insert(ImageListItem item) {
        ContentValues values = new ContentValues();
        values.put(DBConfig.TITLE, item.getTitle());
        values.put(DBConfig.THUMBNAIL_URL, item.getThumbnailUrl());
        values.put(DBConfig.IMAGE_URL, item.getImageUrl());
        values.put(DBConfig.LINK, item.getLink());

        return mDB.insert(DBConfig.TABLE_NAME, null, values) > 0;
    }

    /**
     * 레코드 삭제
     * @param item
     * @return true: 삭제 성공
     */
    public boolean delete(ImageListItem item) {
        int id = item.getId();
        if (id == -1 ) {
            return false;
        }
        return mDB.delete(DBConfig.TABLE_NAME, DBConfig._ID + "=" + id, null) > 0;
    }

    /**
     * DB에서 모든 ImageListItem 데이터 가져오기
     * @return ImageListItem data list
     */
    public ArrayList<ImageListItem> getAllData() {
        mDB = mDBHelper.getReadableDatabase(); // DB 읽기 허용
        Cursor cursor = mDB.rawQuery("SELECT * from " + DBConfig.TABLE_NAME, null);

        ArrayList<ImageListItem> items = new ArrayList<ImageListItem>();

        while(cursor.moveToNext()) {
            ImageListItem item = new ImageListItem();

            int id = cursor.getInt(cursor.getColumnIndex(DBConfig._ID));
            String title = cursor.getString(cursor.getColumnIndex(DBConfig.TITLE));
            String thumbnailUrl = cursor.getString(cursor.getColumnIndex(DBConfig.THUMBNAIL_URL));
            String thumbnailContent = Utils.getImageUrlToString(thumbnailUrl);
            String imageUrl = cursor.getString(cursor.getColumnIndex(DBConfig.IMAGE_URL));
            String link = cursor.getString(cursor.getColumnIndex(DBConfig.LINK));

            item.setImageListItem(id, title, thumbnailUrl, thumbnailContent, imageUrl, link);

            items.add(item);
        }

        cursor.close();
        return items;
    } // end -- getAllData
}
