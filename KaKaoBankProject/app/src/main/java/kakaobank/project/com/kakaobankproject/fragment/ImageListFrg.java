package kakaobank.project.com.kakaobankproject.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kakaobank.project.com.kakaobankproject.CommonConfig;
import kakaobank.project.com.kakaobankproject.R;
import kakaobank.project.com.kakaobankproject.Utils;
import kakaobank.project.com.kakaobankproject.model.ListItemClickAsyncTask;
import kakaobank.project.com.kakaobankproject.model.ImageListAdapter;
import kakaobank.project.com.kakaobankproject.model.ImageListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 이미지 리스트 뷰
 * 
 * Created by sohee.park
 */
public class ImageListFrg extends Fragment {

    private ProgressBar mProgressBar;

    private RecyclerView mRecyclerView;
    private TextView mNoDataText;

    private ArrayList<ImageListItem> mImageListItems;
    private int mTotalCount = 0;

    private ImageListAsync mImageListAsync;

    private int mPageNo = 1; // 검색 URL에 설정할 페이지 번호


    /** */
    public static ImageListFrg newInstance() {
        return new ImageListFrg();
    }

    /** default constructor  */
    public ImageListFrg() { }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_listview, container, false);

        // setting view
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.fragment_listview_progressBar);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_listview_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        mNoDataText = (TextView) rootView.findViewById(R.id.fragment_listview_nodata_text);
        mNoDataText.setText("가져올 이미지 데이터가 없습니다");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mImageListItems = new ArrayList<ImageListItem>();

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
        mImageListAsync = new ImageListAsync();
        mImageListAsync.execute();
    }

    /**
     * AsyncTask 종료
     */
    private void taskFin() {
        try {
            if (mImageListAsync.getStatus() == AsyncTask.Status.RUNNING) {
                mImageListAsync.cancel(true);
            }
        } catch (Exception e) {}
    }

    /**
     * URL 연결하여 이미지 데이터 가져오는 AsyncTask class
     */
    private class ImageListAsync extends AsyncTask<Void, Void, ArrayList<ImageListItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 데이터를 가져올 동안 로딩 다이얼로그 보여주기
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ImageListItem> doInBackground(Void... params) {

            // 이미지 데이터 가져올 URL
            String urlAddr = "https://apis.daum.net/search/image?apikey=57b08bcf2b5194d7d181437eaa390e49" +
                    "&q=카카오" +
                    "&pageno=" + mPageNo +
                    "&result=20" +
                    "&output=json";

            String jsonData = getUrlJsonData(urlAddr);
            Log.i(CommonConfig.TAG, "jsonData : "+jsonData);


            // 가져온 데이터가 NULL값이 아니면 list에 저장
            if (jsonData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONObject channel = jsonObject.getJSONObject("channel");

                    mTotalCount = channel.getInt("pageCount");

                    JSONArray arrayItem = channel.getJSONArray("item");
                    saveArrayItem(arrayItem);
                } catch (Exception e) {
                    Log.w(CommonConfig.TAG, "ImageListAsync doInBackground : " + e.toString());
                    e.printStackTrace();
                }
            }
            return mImageListItems;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageListItem> result) {
            super.onPostExecute(result);

            // 데이터 가져오기가 끝나면 사라지게 함
            mProgressBar.setVisibility(View.GONE);

            // 리스트 데이터가 있는 경우
            if (result.size() > 0) {
                mNoDataText.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                mRecyclerView.setAdapter(listAdapterSetting(result));
            }
            // 리스트 데이터가 없는 경우
            else {
            	mRecyclerView.setVisibility(View.GONE);
                mNoDataText.setVisibility(View.VISIBLE);
            }
        } // end -- onPostExecute


        /**
         * URL 연결하여 데이터 가져옴
         * @param urlAddr 데이터 가져올 URL
         * @return String JSON DATA
         */
        private String getUrlJsonData(String urlAddr) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(urlAddr);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(10000); // 10 sec
                urlConnection.setDoInput(true);
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                Log.i(CommonConfig.TAG, "responseCode : "+responseCode);
                
                // 200:정상일때만 데이터 가져옴
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String str = null;
                    while(true) {
                        str = bufferedReader.readLine();
                        if (str == null) {
                            break;
                        }
                        builder.append(str);
                    }
                    return builder.toString();
                }
            } catch(Exception e) {
                Log.w(CommonConfig.TAG, "ImageListAsync getJsonData : "+e.toString());
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                } catch(Exception e) {}

                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch(Exception e) {}
            }
            return null;
        } // end -- getJsonData


        /**
         * JSON 데이터 파싱 및 리스트에 저장
         *  - 파싱 중 오류나는 것은 리스트에 저장하지 않음
         * @param arrayItem 파싱할 json 데이터
         */
        private void saveArrayItem(JSONArray arrayItem) {
            for (int i = 0; i < arrayItem.length(); i++) {
                try {
                    JSONObject jsonArrayItem = arrayItem.getJSONObject(i);

                    String title = Html.fromHtml(jsonArrayItem.getString("title")).toString();
                    String thumbnailUrl = jsonArrayItem.getString("thumbnail");
                    String thumbnailContent = Utils.getImageUrlToString(thumbnailUrl);
                    String imageUrl = jsonArrayItem.getString("image");
                    String link = jsonArrayItem.getString("link");

                    ImageListItem item = new ImageListItem();
                    item.setImageListItem(0, title, thumbnailUrl, thumbnailContent, imageUrl, link);

                    mImageListItems.add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } // end -- saveArrayItem

        /**
         * 리스트 어댑터 설정
         * @param result
         * @return ImageListAdapter
         */
        private ImageListAdapter listAdapterSetting(final ArrayList<ImageListItem> result) {
            ImageListAdapter viewAdapter = new ImageListAdapter(result);

            // 리스트에 FOOTER 추가 여부
            viewAdapter.setUseFooter(isAddButtonView());

            // 리스트 아이템 클릭 이벤트 리스너
            viewAdapter.setOnItemClickListener(new ImageListAdapter.ListItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    listClick(result.get(position));
                }
            });
            // FOOTER 클릭 이벤트 리스너
            viewAdapter.setOnFooterClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageNo++;

                    ImageListAsync imageListAsync = new ImageListAsync();
                    imageListAsync.execute();
                }
            });

            return viewAdapter;
        }
        
        /**
         * 리스트 클릭 이벤트
         * @param item
         */
        private void listClick(final ImageListItem item) {
            ListItemClickAsyncTask listItemClickAsyncTask = new ListItemClickAsyncTask(getActivity(), item, CommonConfig.TYPE_IMAGE_LIST);
            listItemClickAsyncTask.execute();
        } // end -- imageList listClick

        /**
         * 리스트에 footer 추가 체크
         * @return true: add footer, false: delete footer
         */
        private boolean isAddButtonView() {
            int size = mImageListItems.size();
            if (size >= mTotalCount) {
                return false;
            }
            return true;
        }

    } // end -- private class ImageListAsync

}

