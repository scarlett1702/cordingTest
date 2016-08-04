package kakaobank.project.com.kakaobankproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import kakaobank.project.com.kakaobankproject.fragment.ImageListFrg;
import kakaobank.project.com.kakaobankproject.fragment.MyFavoriteFrg;

/**
 *
 * Created by sohee.park
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_COUNT = 2; // page count

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    /** */
    public MainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        mFragmentManager = fragmentManager;
        mFragmentTags = new HashMap<Integer, String>();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ImageListFrg.newInstance();
            case 1:
                return MyFavoriteFrg.newInstance();
            default:
                return null;
        }
    }

    /** Returns the page title for the top indicator */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "이미지 리스트";
            case 1:
                return "내 보관함";
            default:
                return null;
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    /** */
    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }
}
