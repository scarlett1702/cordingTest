package kakaobank.project.com.kakaobankproject.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import kakaobank.project.com.kakaobankproject.R;

/**
 * 리스트 어댑터 footer view 생성
 *
 * Created by sohee.park
 */
public class FooterViewHolder extends RecyclerView.ViewHolder {

    public Button addButton;

    /** */
    public FooterViewHolder(View view) {
        super(view);

        // 더보기 버튼
        addButton = (Button) view.findViewById(R.id.item_footer_addlist);
    }
}
