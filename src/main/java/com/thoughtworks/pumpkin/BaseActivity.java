package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.RoboSlidingFragmentActivity;
import com.thoughtworks.pumpkin.fragment.SidePanel;


public class BaseActivity extends RoboSlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSlidingActionBarEnabled(true);
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SidePanel()).commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
