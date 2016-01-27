package com.dprojekt.presentation.decisions;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dprojekt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Decision Tutorial Activity.
 */
public class DecTutActivity extends Activity {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // Keys for arguments bundle */
    private static final String KEY_CURRENT_PAGE = "current_page";

    /** Array with pages of the tutorial */
    private static final TutPage[] TUT_PAGES = {
            new TutPage(R.color.main_blue, R.drawable.ic_timer, R.string.dec_tut_page1_title,
                    R.string.dec_tut_page1_description),
            new TutPage(R.color.main_pink, R.drawable.ic_bullhorn, R.string.dec_tut_page2_title,
                    R.string.dec_tut_page2_description),
            new TutPage(R.color.yellow_pref, R.drawable.ic_star, R.string.dec_tut_page3_title,
                    R.string.dec_tut_page3_description),
            new TutPage(R.color.red_pref, R.drawable.ic_block, R.string.dec_tut_page4_title,
                    R.string.dec_tut_page4_description),
            new TutPage(R.color.green_pref, R.drawable.ic_check, R.string.dec_tut_page5_title,
                    R.string.dec_tut_page5_description),
    };

    /** Number of pages of the tutorial */
    private static final int NUM_PAGES = TUT_PAGES.length;


    // ==========================================================================
    // Member variables
    // ==========================================================================

    // Bound layout views
    @Bind(R.id.pager) ViewPager vp;
    @Bind(R.id.skip_button) Button b_skip;
    @Bind(R.id.next_button) Button b_next;
    @Bind(R.id.indicators_container) LinearLayout ll_indicators_container;

    /** Current page of the tutorial */
    private int mCurrentPage;

    // ==========================================================================
    // Activity lifecycle methods
    // ==========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec_tut);
        ButterKnife.bind(this);

        // Init the views
        this.initViews();
    }

    /** Initialize views of this activity */
    private void initViews() {

        // Set initial background color (different for every page)
        final Resources r = getResources();
        getWindow().getDecorView().setBackgroundColor(r.getColor(TUT_PAGES[0].getBgColor()));

        // Set view pager to show tutorial pages
        DecTutPagerAdapter pagerAdapter = new DecTutPagerAdapter(getFragmentManager());
        vp.setAdapter(pagerAdapter);

        // Inflate and add page indicators
        final View[] indicators = new View[NUM_PAGES];
        for (int i = 0; i < NUM_PAGES; i++) {
            ImageView indicator = (ImageView) getLayoutInflater()
                    .inflate(R.layout.view_indicator, ll_indicators_container, false);
            indicators[i] = indicator;
            ll_indicators_container.addView(indicator);
        }
        // Set indicator of the first page as active
        indicators[0].setAlpha(1);

        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            // Create color interpolator to interpolate background color between pages
            private ArgbEvaluator mColorInterpolator = new ArgbEvaluator();

            @Override
            public void onPageSelected(int position) {
                // Set page controls and indicators according to current page
                mCurrentPage = position;

                // Update page indicators
                for (int i = 0; i < NUM_PAGES; i++) {
                    if (i == position) {
                        indicators[i].setAlpha(1);
                    } else {
                        indicators[i].setAlpha(0.6f);
                    }
                }

                // Update page controls display
                if (position == NUM_PAGES - 1) {
                    b_skip.setVisibility(View.GONE);
                    b_next.setText(R.string.done);
                } else {
                    b_skip.setVisibility(View.VISIBLE);
                    b_next.setText(">");
                }
            }

            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
                // Interpolate background color on page scroll. Fade to transparent and finish
                // after last actual page.
                if (pos < NUM_PAGES -1) {
                    // Interpolate background color
                    Integer bgColor = (Integer) mColorInterpolator.evaluate(positionOffset,
                            r.getColor(TUT_PAGES[pos].getBgColor()),
                            r.getColor(TUT_PAGES[pos + 1].getBgColor()));
                    getWindow().getDecorView().setBackgroundColor(bgColor);
                } else if (pos == NUM_PAGES) {
                    // After the last actual page, fade to transparent on scroll using an
                    // empty page.
                    getWindow().getDecorView().setAlpha(1 - positionOffset);
                } else if (pos > NUM_PAGES) {
                    // We have reached the empty page. Finish activity.
                    finish();
                }
            }
        });
    }

    // ==========================================================================
    // User Input
    // ==========================================================================

    // Finish tutorial immediately
    @OnClick(R.id.skip_button)
    public void onSkipClick() {
        finish();
    }

    // Show next page of tutorial or finish it if it is the last one
    @OnClick(R.id.next_button)
    public void onNextClick() {
        if (mCurrentPage == NUM_PAGES - 1) {
            finish();
        } else {
            vp.setCurrentItem(mCurrentPage + 1, true);
        }
    }

    // ==========================================================================
    // Custom PagerAdapter
    // ==========================================================================

    /** Pager adapter to show pages of the tutorial in a ViewPager */
    private class DecTutPagerAdapter extends FragmentPagerAdapter {
        public DecTutPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            // Always get a TutPageFragment with the corresponding page as argument
            Fragment fragment = new TutPageFragment();
            Bundle args = new Bundle(1);
            args.putInt(KEY_CURRENT_PAGE, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // We use an extra empty page to fade tutorial and finish it on scroll
            return NUM_PAGES + 1;
        }
    }

    // ==========================================================================
    // Custom Fragment
    // ==========================================================================

    /** Fragment for DecTutPagerAdapter containing a page of the tutorial */
    private static class TutPageFragment extends Fragment {

        // Bound layout views
        @Bind(R.id.root) View v_root;
        @Bind(R.id.image) ImageView iv_image;
        @Bind(android.R.id.text1) TextView tv_text1;
        @Bind(android.R.id.text2) TextView tv_text2;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tut_page, container, false);
            ButterKnife.bind(this, rootView);

            // Get the page number or position and render it
            int pos = getArguments().getInt(KEY_CURRENT_PAGE, -1);
            this.renderPage(pos);
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }

        /** Render teh given tutorial page by filling the views accordingly */
        public void renderPage(int pos) {
            if (pos < NUM_PAGES) {
                iv_image.setImageResource(TUT_PAGES[pos].getIcon());
                tv_text1.setText(TUT_PAGES[pos].getTitle());
                tv_text2.setText(TUT_PAGES[pos].getDescription());
            } else {
                // Last page is empty and not visible, used to fade tutorial and finish it
                // on scroll
                v_root.setVisibility(View.GONE);
            }
        }
    }

    // ==========================================================================
    // Tutorial page POJO
    // ==========================================================================

    /** POJO for a tutorial page content and layout */
    private static class TutPage {

        /** Page background color */
        private int bgColor;
        /** Page icon */
        private int icon;
        /** Page title */
        private int title;
        /** Page description */
        private int description;

        public TutPage(int bgColor, int icon, int title, int description) {
            this.bgColor = bgColor;
            this.icon = icon;
            this.title = title;
            this.description = description;
        }

        public int getBgColor() {
            return bgColor;
        }

        public int getIcon() {
            return icon;
        }

        public int getTitle() {
            return title;
        }

        public int getDescription() {
            return description;
        }
    }
}
