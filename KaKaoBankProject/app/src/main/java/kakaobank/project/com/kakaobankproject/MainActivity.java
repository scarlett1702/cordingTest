package kakaobank.project.com.kakaobankproject;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.os.Bundle;

import kakaobank.project.com.kakaobankproject.db.DBManager;

/**
 * 메인 액티비티
 *
 * Created by sohee.park
 */
public class MainActivity extends AppCompatActivity {

    private MainPagerAdapter mMainPagerAdapter;

    private ViewPager mViewPager;

    //
    public DBManager mDbManager = null;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) { }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = mMainPagerAdapter.getFragment(position);
                if (fragment != null) {
                    fragment.onResume();
                }
            }
        });

        //
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    protected void onStop() {
        super.onStop();

        // DB 종료
        try {
            if (mDbManager != null) {
                mDbManager.close();
                mDbManager = null;
            }
        } catch(Exception e) {}
    }

    
    /**
     * 프래그먼트 뷰에서 DB 사용을 위해 만듬
     * @return DBManager
     */
    public DBManager getDbHelper() {
        // DB 열기
        if (mDbManager == null) {
            mDbManager = new DBManager(MainActivity.this);
            mDbManager.open();
        }
        return mDbManager;
    }
}
