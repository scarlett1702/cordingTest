package kakaobank.project.com.kakaobankproject.db;

import android.provider.BaseColumns;

/**
 * Created by sohee.park
 */
public class DBConfig implements BaseColumns {

    // DB 명, DB 버전
    public static final String DATABASE_NAME = "kakaobank.db";
    public static final int DATABASE_VERSION = 1;

    // 테이블명
    public static final String TABLE_NAME = "myfavorite";

    // 컬럼명
    public static final String TITLE = "title";
    public static final String THUMBNAIL_URL = "thumbnail_url";
    public static final String IMAGE_URL = "image_url";
    public static final String LINK = "link";

    // 테이블 생성 구문
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + _ID + " integer primary key autoincrement, "
            + TITLE + " text not null, "
            + THUMBNAIL_URL + " text not null, "
            + IMAGE_URL + " text not null, "
            + LINK + " text not null "
            + ");";

}
