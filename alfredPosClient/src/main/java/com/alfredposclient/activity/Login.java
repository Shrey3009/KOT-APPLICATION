package com.alfredposclient.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.User;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.Numerickeyboard;
import com.alfredbase.view.Numerickeyboard.KeyBoardClickListener;
import com.alfredposclient.R;
import com.alfredposclient.adapter.HomePageViewPagerAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.floatwindow.float_lib.FloatActionController;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends BaseActivity implements KeyBoardClickListener {

    private static final int KEY_LENGTH = 5;
    private static final int STATE_IN_ENTER_ID = 0;
    private static final int STATE_IN_ENTER_PASSWORD = 1;
    /**
     * 当前键盘输入对应的状态，0表示输入的员工ID，1表示输入的密码
     */
    private int state = STATE_IN_ENTER_ID;
    private ViewPager viewPager;
    private ArrayList<View> views = new ArrayList<View>();
    private View login_view_1;
    private View login_view_2;
    private Numerickeyboard numerickeyboard;
    private TextView tv_psw_1;
    private TextView tv_psw_2;
    private TextView tv_psw_3;
    private TextView tv_psw_4;
    private TextView tv_psw_5;
    private StringBuffer keyBuf = new StringBuffer();
    private String employee_ID;
    private String password;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView tv_title_name_one;
    private TextView tv_title_name_two;
    private TextTypeFace textTypeFace;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_login);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        login_view_1 = getLayoutInflater().inflate(R.layout.login_view_1, null);
        views.add(login_view_1);
        login_view_2 = getLayoutInflater().inflate(R.layout.login_view_2, null);
        views.add(login_view_2);
        viewPager.setAdapter(new HomePageViewPagerAdapter(views));
        login_view_1.findViewById(R.id.btn_sign_up).setOnClickListener(this);

        String title = getString(R.string.cashier_login_tips1);
        Restaurant rest = CoreData.getInstance().getRestaurant();
        RevenueCenter revenueCenter = App.instance.getRevenueCenter();
        String name = "";
        if (rest != null) {
            name = rest.getRestaurantName();
            if (revenueCenter != null) {
                name = name + "\n" + revenueCenter.getRevName();
            }
        }
        ((TextView) (login_view_2.findViewById(R.id.tv_rest_name))).setText(name);
        ((TextView) (login_view_2.findViewById(R.id.tv_login_tips))).setText(title);

        numerickeyboard = (Numerickeyboard) login_view_2
                .findViewById(R.id.numerickeyboard);
        numerickeyboard.setKeyBoardClickListener(this);
        numerickeyboard.setParams(context);
        tv_psw_1 = (TextView) login_view_2.findViewById(R.id.tv_psw_1);
        tv_psw_2 = (TextView) login_view_2.findViewById(R.id.tv_psw_2);
        tv_psw_3 = (TextView) login_view_2.findViewById(R.id.tv_psw_3);
        tv_psw_4 = (TextView) login_view_2.findViewById(R.id.tv_psw_4);
        tv_psw_5 = (TextView) login_view_2.findViewById(R.id.tv_psw_5);
        App.instance.finishAllActivityExceptOne(getClass());
        initTitle();
        initTextTypeFace(login_view_1, login_view_2);

        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + ": " + App.instance.VERSION);

    }

    public void initTitle() {
        tv_title_name_one = (TextView) findViewById(R.id.tv_title_name_one);
        tv_title_name_one.setVisibility(View.GONE);
        tv_title_name_two = (TextView) findViewById(R.id.tv_title_name_two);
        tv_title_name_two.setVisibility(View.GONE);
    }

    public void initTextTypeFace(View login_view_1, View login_view_2) {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_version));
        textTypeFace.setTrajanProRegular((TextView) login_view_1.findViewById(R.id.tv_sign_up_tips));
        textTypeFace.setTrajanProRegular((Button) login_view_1.findViewById(R.id.btn_sign_up));
        textTypeFace.setTrajanProBlod((TextView) login_view_2.findViewById(R.id.tv_rest_name));
        textTypeFace.setTrajanProRegular((TextView) login_view_2.findViewById(R.id.tv_login_tips));
        textTypeFace.setTrajanProRegular(tv_psw_1);
        textTypeFace.setTrajanProRegular(tv_psw_2);
        textTypeFace.setTrajanProRegular(tv_psw_3);
        textTypeFace.setTrajanProRegular(tv_psw_4);
        textTypeFace.setTrajanProRegular(tv_psw_5);
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.btn_sign_up: {
                viewPager.setCurrentItem(1);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onKeyBoardClick(String key) {
        BugseeHelper.buttonClicked(key);
        if (keyBuf.length() >= KEY_LENGTH)
            return;
        if (key.equals("X")) {
            if (keyBuf.length() > 0) {
                keyBuf.deleteCharAt(keyBuf.length() - 1);
            }
        } else {
            keyBuf.append(key);
        }
        int key_len = keyBuf.length();
        setPassword(key_len);
        if (key_len == KEY_LENGTH) {
            if (state == STATE_IN_ENTER_ID) {
                String title = getString(R.string.cashier_login_tips1);
                ((TextView) (findViewById(R.id.tv_login_tips))).setText(title);

//				state = STATE_IN_ENTER_PASSWORD;
                employee_ID = keyBuf.toString();
                keyBuf.delete(0, key_len);
                setPassword(keyBuf.length());
//			} else if (state == STATE_IN_ENTER_PASSWORD) {
//				password = keyBuf.toString();

//				User user = CoreData.getInstance().getUser(employee_ID,
//						password);
                User user = CoreData.getInstance().getUserByEmpId(Integer.parseInt(employee_ID));
                boolean cashierAccess = false;
                if (user != null) {
                    RevenueCenter revenueCenter = App.instance
                            .getRevenueCenter();
                    cashierAccess = CoreData.getInstance()
                            .checkUserCashierAccessInRevcenter(user,
                                    revenueCenter.getRestaurantId(),
                                    revenueCenter.getId());

                    BugseeHelper.trace("login", true);
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("user_id", user.getId());
                    param.put("account_name", user.getAccountName());
                    param.put("first_name", user.getFirstName());
                    param.put("cashier_access", cashierAccess);
                    BugseeHelper.event("login data", param);

                    if (cashierAccess) {
                        App.instance.setUser(user);
                        context.overridePendingTransition(
                                R.anim.slide_bottom_in, R.anim.slide_top_out);
                        UIHelp.startOpenRestaruant(context);
                        finish();
                        return;
                    } else {
                        UIHelp.showToast(context, context.getResources().getString(R.string.login_required));
                        title = getString(R.string.cashier_login_tips1);
                        ((TextView) (findViewById(R.id.tv_login_tips))).setText(title);
                        state = STATE_IN_ENTER_ID;
                        employee_ID = null;
                        password = null;
                    }
                } else {
                    UIHelp.showToast(context, context.getResources().getString(R.string.invalid_employee));
                    title = getString(R.string.cashier_login_tips1);
                    ((TextView) (findViewById(R.id.tv_login_tips))).setText(title);
                    state = STATE_IN_ENTER_ID;
                    employee_ID = null;
                    password = null;
                }
                keyBuf.delete(0, key_len);
                setPassword(keyBuf.length());
            }
        }
    }

    private void setPassword(int key_len) {
        switch (key_len) {
            case 0: {
                tv_psw_1.setText("");
                tv_psw_2.setText("");
                tv_psw_3.setText("");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 1: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("");
                tv_psw_3.setText("");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 2: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 3: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("*");
                tv_psw_4.setText("");
                tv_psw_5.setText("");
                break;
            }
            case 4: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("*");
                tv_psw_4.setText("*");
                tv_psw_5.setText("");
                break;
            }
            case 5: {
                tv_psw_1.setText("*");
                tv_psw_2.setText("*");
                tv_psw_3.setText("*");
                tv_psw_4.setText("*");
                tv_psw_5.setText("*");
                break;
            }
            default:
                break;
        }
    }

	@Override
	public void onBackPressed() {
		 if (doubleBackToExitPressedOnce) {
			 FloatActionController.getInstance().stopMonkServer(context);
		        super.onBackPressed();
		        return;
		    }

		    this.doubleBackToExitPressedOnce = true;
		    UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

		BaseApplication.postHandler.postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            doubleBackToExitPressedOnce=false;                       
		        }
		    }, 2000);
	}
}
