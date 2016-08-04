package kakaobank.project.com.kakaobankproject.fragment;

import java.util.ArrayList;

import kakaobank.project.com.kakaobankproject.CommonConfig;
import kakaobank.project.com.kakaobankproject.MainActivity;
import kakaobank.project.com.kakaobankproject.R;
import kakaobank.project.com.kakaobankproject.db.DBManager;
import kakaobank.project.com.kakaobankproject.model.ListItemClickAsyncTask;
import kakaobank.project.com.kakaobankproject.model.ImageListAdapter;
import kakaobank.project.com.kakaobankproject.model.ImageListItem;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 내 보관함 뷰
 * 
 * Created by sohee.park
 */
public class MyFavoriteFrg extends Fragment {

    private ProgressBar mProgressBar;

    private RecyclerView mRecyclerView;
    private TextView mNoDataText;

    private MyFavoriteAsync mFavoriteAsync;

    private DBManager mDbManager;


    public static MyFavoriteFrg newInstance() {
        return new MyFavoriteFrg();
    }

    /** default constructor  */
    public MyFavoriteFrg() {}

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_listview, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.fragment_listview_progressBar);
        
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_listview_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        mNoDataText = (TextView) rootView.findViewById(R.id.fragment_listview_nodata_text);
        mNoDataText.setText("저장한 이미지가 없습니다.");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // refresh
        this.mDbManager = ((MainActivity) getActivity()).getDbHelper();

        taskRun();
    }

    @Override
    public void onPause() {
        super.onPause();

        taskFin();
    }

    /**
     * AsyncTask 실행
     */
    private void taskRun() {
        mFavoriteAsync = new MyFavoriteAsync();
        mFavoriteAsync.execute();
    }

    /**
     * AsyncTask 종료
     */
    private void taskFin() {
        try {
            if (mFavoriteAsync.getStatus() == AsyncTask.Status.RUNNING) {
                mFavoriteAsync.cancel(true);
            }
        } catch (Exception e) {}
    }

    /**
     * DB에 저장한 이미지 데이터 가져오는 AsyncTask class
     */
    private class MyFavoriteAsync extends AsyncTask<Void, Void, ArrayList<ImageListItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 데이터를 가져올 동안 로딩 다이얼로그 보여주기
            mProgressBar.setVisibility(View.VISIBLE);

            // 뷰 초기화
            mRecyclerView.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<ImageListItem> doInBackground(Void... params) {
            ArrayList<ImageListItem> items = new ArrayList<ImageListItem>();

            try {
                items = ((MainActivity) getActivity()).getDbHelper().getAllData();
            } catch(Exception e) {
                Log.w(CommonConfig.TAG, "MyFavoriteAsync doInBackground : "+e.toString());
                e.printStackTrace();
            }

            return items;
        } // end -- doInBackground


        @Override
        protected void onPostExecute(final ArrayList<ImageListItem> result) {
            super.onPostExecute(result);

            // 데이터 가져오기가 끝나면 사라지게 함
            mProgressBar.setVisibility(View.GONE);

            // 리스트 데이터가 있는 경우
            if (result.size() > 0) {
            	mRecyclerView.setVisibility(View.VISIBLE);
                mNoDataText.setVisibility(View.GONE);

                mRecyclerView.setAdapter(listAdapterSetting(result));
            }
            // 리스트 데이터가 없는 경우
            else {
                mRecyclerView.setVisibility(View.GONE);
                mNoDataText.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 리스트 어댑터 설정
         * @param result
         * @return ImageListAdapter
         */
        private ImageListAdapter listAdapterSetting(final ArrayList<ImageListItem> result) {
            ImageListAdapter viewAdapter = new ImageListAdapter(result);
            // 리스트 아이템 클릭 이벤트 리스너
            viewAdapter.setOnItemClickListener(new ImageListAdapter.ListItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    listClick(result.get(position));
                }
            });
            return viewAdapter;
        }

        /**
         * 리스트 클릭 이벤트
         * @param item
         */
        private void listClick(final ImageListItem item) {
            ListItemClickAsyncTask listItemClickAsyncTask = new ListItemClickAsyncTask(getActivity(), item, CommonConfig.TYPE_FAVORITE);
            // 보관함 삭제 클릭 이벤트 지정, 삭제 후 리스트를 다시 호출
            listItemClickAsyncTask.setFavoriteDeleteClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean result = mDbManager.delete(item);
                    if (result) {
                        Toast.makeText(getActivity(), "보관함에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        taskRun();
                    }
                }
            });
            listItemClickAsyncTask.execute();
        } // end -- favorite listClick

    } // end -- private class MyFavoriteAsync
}
