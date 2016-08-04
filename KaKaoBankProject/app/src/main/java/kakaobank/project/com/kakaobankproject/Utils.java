package kakaobank.project.com.kakaobankproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by scarlett
 */
public class Utils {

    /**
     * 이미지 URL을 연결하여 이미지를 비트맵으로 가져옴
     * @param imgUrlAddr 가져올 이미지 url
     * @return Bitmap 이미지뷰에 표시하기 위해 비트맵으로 받음
     */
    public static Bitmap getImageUrlToBitmap(String imgUrlAddr) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(imgUrlAddr);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true); //url로 input받는 flag 허용
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            return BitmapFactory.decodeStream(inputStream); // bitmap으로 받음
        } catch(Exception e) {
            Log.w(CommonConfig.TAG, "getImageUrlToBitmap : "+e.toString());
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch(Exception e) {}
        }
        return null;
    } // end -- getImageUrlToBitmap


    /**
     * 이미지 URL을 연결하여 이미지를 Base64로 가져옴
     * @param imgUrlAddr 가져올 이미지 url
     * @return
     */
    public static String getImageUrlToString(String imgUrlAddr) {
        ByteArrayOutputStream outputStream = null;

        try {
            outputStream = new ByteArrayOutputStream();

            Bitmap bitmap = getImageUrlToBitmap(imgUrlAddr);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) ;
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } catch(Exception e) {
            Log.w(CommonConfig.TAG, "getImageUrlToString : "+e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch(Exception e) {}
        }
        return null;
    } // end -- getImageUrlToString

    /**
     * Base64 String을 비트맵으로 변환
     * @param imgStr 변환할 base64 데이터
     * @return
     */
    public static Bitmap stringToBitmap(String imgStr) {
        byte[] imgByte = Base64.decode(imgStr.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }
}
