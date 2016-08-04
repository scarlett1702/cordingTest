package kakaobank.project.com.kakaobankproject.model;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kakaobank.project.com.kakaobankproject.R;
import kakaobank.project.com.kakaobankproject.Utils;

/**
 * List View Adapter
 * 
 * Created by sohee.park
 */
public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;


    private View.OnClickListener mFooterClickListener; // footer click listener
    private ImageListAdapter.ListItemClickListener mListItemClickListener; // item click listener

    private ArrayList<ImageListItem> mItems; // 어댑터에 표시할 데이터

    private boolean isUseFooter = false; // footer 사용 유무, true:사용 false:사용하지않음


    /** */
    public ImageListAdapter(ArrayList<ImageListItem> items) {
        this.mItems = items;
    }

    /** */
    public void setUseFooter(boolean useFooter) {
        isUseFooter = useFooter;
    }

    @Override
    public int getItemCount() {
        if (isUseFooter) {
            return mItems.size() + 1;
        }
        return mItems.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            return new FooterViewHolder(view);
        }
        else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagelist, parent, false);
            return new ImageListViewHolder(view, mListItemClickListener);
        }
        // 일치하는 뷰 타입이 없으므로 에러 표시
        throw new RuntimeException("there is no type that matches the type " + viewType
                + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.addButton.setOnClickListener(mFooterClickListener);
        }
        else if (holder instanceof ImageListViewHolder) {
            ImageListItem item = mItems.get(position);

            ImageListViewHolder listViewHolder = (ImageListViewHolder) holder;

            // thumbnail 이미지에 맞게 이미지뷰 조정
            Bitmap thumbnail = Utils.stringToBitmap(item.getThumbnailContent());
            int thumbnailWidth = thumbnail.getWidth(); // thumbnail 130
            int thumbnailHeight = thumbnail.getHeight(); // thumbnail 130

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailWidth, thumbnailHeight);
            listViewHolder.thumbnailImage.setLayoutParams(params);

            listViewHolder.thumbnailImage.setImageBitmap(thumbnail);
            listViewHolder.titleText.setText(item.getTitle());
            listViewHolder.linkText.setText(item.getLink());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionFooter(position) && isUseFooter)
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    /**
     * footer view 넣을 곳 지정
     * @param position
     * @return
     */
    private boolean isPositionFooter(int position) {
        return position == mItems.size();
    }


    /** */
    public void setOnFooterClickListener(View.OnClickListener footerClickListener) {
        mFooterClickListener = footerClickListener;
    }

    /** */
    public void setOnItemClickListener(ImageListAdapter.ListItemClickListener ListItemClickListener) {
        mListItemClickListener = ListItemClickListener;
    }

    /** */
    public interface ListItemClickListener {
        void onItemClick(int position, View v);
    }

}
