package kakaobank.project.com.kakaobankproject.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kakaobank.project.com.kakaobankproject.CommonConfig;
import kakaobank.project.com.kakaobankproject.R;

/**
 * 리스트 어댑터 item view 생성
 *
 * Created by sohee.park
 */
public class ImageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageListAdapter.ListItemClickListener mListItemClickListener; // 클릭 이벤트 리스너

    public LinearLayout container;
    public ImageView thumbnailImage; // 썸네일 이미지
    public TextView titleText; // 제목
    public TextView linkText; // '링크로 이동'

    /** */
    public ImageListViewHolder(View view, ImageListAdapter.ListItemClickListener ListItemClickListener) {
        super(view);

        mListItemClickListener = ListItemClickListener;

        container = (LinearLayout) view.findViewById(R.id.item_imagelist_layout);
        thumbnailImage = (ImageView) view.findViewById(R.id.item_imagelist_thumbnail);
        titleText = (TextView) view.findViewById(R.id.item_imagelist_title);
        linkText = (TextView) view.findViewById(R.id.item_imagelist_link);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            mListItemClickListener.onItemClick(getAdapterPosition(), v);
        } catch(Exception e) {
            Log.i(CommonConfig.TAG, "ImageListViewHolder onClick : "+e.toString());
            e.printStackTrace();
        }
    }
}
