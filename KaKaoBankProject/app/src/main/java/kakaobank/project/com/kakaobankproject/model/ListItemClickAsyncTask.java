package kakaobank.project.com.kakaobankproject.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kakaobank.project.com.kakaobankproject.CommonConfig;
import kakaobank.project.com.kakaobankproject.MainActivity;
import kakaobank.project.com.kakaobankproject.R;
import kakaobank.project.com.kakaobankproject.Utils;
import kakaobank.project.com.kakaobankproject.db.DBManager;

/**
 * 리스트 클릭 이벤트 실행하는 AsyncTask class
 *  -이미지 URL 호출
 * 
 * Created by sohee.park
 */
public class ListItemClickAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    private int mType = 0;

    private Activity mActivity;
    private ImageListItem mItem;

    private DBManager mDbManager;

    private ProgressDialog mDialog = null;

    // 내 보관함 삭제 클릭 이벤트 설정-삭제 후 리스트를 다시 불러오기 위함
    private DialogInterface.OnClickListener mFavoriteDeleteClickListener = null;


    /** */
    public ListItemClickAsyncTask(Activity activity, ImageListItem item, int type) {
        this.mActivity = activity;
        this.mItem = item;
        this.mType = type;

        this.mDbManager = ((MainActivity) mActivity).getDbHelper();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // 데이터 가져올 동안 progress dialog 실행
        if (mDialog == null) {
            try {
                mDialog = ProgressDialog.show(mActivity, "", "이미지를 불러오는 중 입니다", false);
            } catch(Exception e) {
                Log.w(CommonConfig.TAG, "ListClickAsyncTask dialog : "+e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        // 이미지 URL로 비트맵 이미지 가져오기
        String imageUrl = mItem.getImageUrl();
        if (imageUrl != null) {
            return Utils.getImageUrlToBitmap(imageUrl);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        // progress dialog 종료
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        // 결과값 처리
        if (bitmap != null) {
            customViewDialogSetting(bitmap);
        } else {
        	Toast.makeText(mActivity, "이미지 데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Custom View Dialog - view 설정
     *  - 이미지 URL로 가져온 이미지와 이미지 제목 표시
     *  - type에 따라 보관함에 저장 또는 삭제
     * @param bitmap
     */
    private void customViewDialogSetting(Bitmap bitmap) {
    	LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = (View) inflater.inflate(R.layout.dialog_imageview, null, false);

        // setting image view
        ImageView imageView = (ImageView) v.findViewById(R.id.dialog_imageview);
        imageView.setImageBitmap(bitmap);

        // setting title
        TextView titleText = (TextView) v.findViewById(R.id.dialog_imageview_title);
        titleText.setText(mItem.getTitle());

        // setting open url
        TextView linkText = (TextView) v.findViewById(R.id.dialog_imageview_link);
        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 링크로 이동
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mItem.getLink()));
                mActivity.startActivity(intent);
            }
        });

        // popup
        customViewDialogShow(v);
    }

    /**
     * Custom View Dialog - popup
     * @param v
     */
    private void customViewDialogShow(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setView(v);

        // type : 이미지 리스트
        if (mType == CommonConfig.TYPE_IMAGE_LIST) {
            dialog.setPositiveButton("보관함에 저장", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean result = mDbManager.insert(mItem);
                    if (result) {
                        Toast.makeText(mActivity, "보관함에 저장되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setNegativeButton("취소", null);
        }
        // type : 내 보관함
        else if (mType == CommonConfig.TYPE_FAVORITE) {
            dialog.setPositiveButton("보관함에서 삭제", getFavoriteDeleteClickListener());
            dialog.setNegativeButton("확인", null);
        }
        dialog.show();
    } // end -- customViewDialogShow


    /** */
    public DialogInterface.OnClickListener getFavoriteDeleteClickListener() {
        return mFavoriteDeleteClickListener;
    }

    /** */
    public void setFavoriteDeleteClickListener(DialogInterface.OnClickListener mFavoriteDeleteClickListener) {
        this.mFavoriteDeleteClickListener = mFavoriteDeleteClickListener;
    }


}
