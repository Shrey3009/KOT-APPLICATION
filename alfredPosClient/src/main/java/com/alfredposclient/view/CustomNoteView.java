package com.alfredposclient.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.Order;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.utils.DialogFactory;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.activity.kioskactivity.MainPageKiosk;
import com.alfredposclient.adapter.VoidOrFocAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.global.UIHelp;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 * @version 1
 * @time
 * @tags 订单的用户自定义部分：牌号；人数；折扣、备注等等
 */
public class CustomNoteView extends LinearLayout implements OnClickListener, OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final int DEFAULT_TABLE_COUNT = 100; // 默认牌号数量（可以没有牌号）
    private Context mContext;
    private BaseActivity parent;
    private boolean isShow = false;
    private LayoutInflater inflater;
    private CloseCustomNoteView mCloseCustomNoteView;

    private TextView custom_note_input_lable;
    private ImageView custom_note_close_img;

    private LinearLayout custom_note_discount_input_lyt;
    private RelativeLayout custom_note_person_ryt;
    private EditText custom_note_person_edt;
    private ImageView cunstom_note_person_clean_img, custom_note_sure_img;

    private GridView custom_note_gridview;

    private VoidOrFocAdapter voidOrFocAdapter;


    private Order order;

    private Handler handler;

    private boolean[] selectedFlag;
    private List<String> playDataList = new ArrayList<String>();
    private List<String> cannotClick = new ArrayList<String>();


    public CustomNoteView(Context context) {
        super(context);
        mContext = context;
        initUI();
    }

    public CustomNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUI();
    }

    public CustomNoteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initUI();
    }

    private void initUI() {
        View.inflate(mContext, R.layout.custom_notes_view, this);
        inflater = LayoutInflater.from(mContext);

        custom_note_input_lable = (TextView) findViewById(R.id.custom_note_input_lable);
        custom_note_close_img = (ImageView) findViewById(R.id.custom_note_close_img);
        custom_note_sure_img = (ImageView) findViewById(R.id.custom_note_sure_img);

        custom_note_discount_input_lyt = (LinearLayout) findViewById(R.id.custom_note_discount_input_lyt);

        custom_note_person_ryt = (RelativeLayout) findViewById(R.id.custom_note_person_ryt);

        custom_note_person_edt = (EditText) findViewById(R.id.custom_note_person_edt);

        cunstom_note_person_clean_img = (ImageView) findViewById(R.id.cunstom_note_person_clean_img);

        custom_note_gridview = (GridView) findViewById(R.id.custom_note_gridview);

        voidOrFocAdapter = new VoidOrFocAdapter(mContext,
                new ArrayList<String>(), new boolean[2], cannotClick);
        custom_note_gridview.setAdapter(voidOrFocAdapter);

//		registEvent();
    }

    private void setGridViewWidth(int width) {
        int itemWidth = 100;
        int numColumns = width / itemWidth;
        voidOrFocAdapter.setItemWidth(itemWidth);
        custom_note_gridview.setNumColumns(numColumns);
    }

    public void setValue(BaseActivity parent, Order order, Handler handler, int gridWidth) {
        isShow = true;
        this.parent = parent;
        this.order = order;
        custom_note_gridview.setVisibility(View.VISIBLE);
        custom_note_close_img.setVisibility(View.VISIBLE);
        custom_note_close_img.setOnClickListener(this);
        custom_note_gridview.setOnItemClickListener(this);
        custom_note_gridview.setOnItemLongClickListener(this);
        custom_note_input_lable.setOnClickListener(this);
        cunstom_note_person_clean_img.setOnClickListener(this);
        findViewById(R.id.custom_cottent_lyt).setOnClickListener(this);
        findViewById(R.id.custom_notes_root_lyt).setOnClickListener(this);
        custom_note_discount_input_lyt.setVisibility(View.GONE);
        custom_note_person_edt.setOnEditorActionListener(oeal);
        setGridViewWidth(gridWidth);
        this.handler = handler;
        playDataList.clear();
        playDataList.add("no num");
        List<String> sendFoodCardNumList = App.instance.getLocalRestaurantConfig().getSendFoodCardNumList();
        playDataList.addAll(sendFoodCardNumList);

        cannotClick = OrderSQL.getUsedTableNames();

        selectedFlag = new boolean[playDataList.size()];
        for (int i = 0; i < playDataList.size(); i++) {
            selectedFlag[i] = false;
        }

        int tableName = getTable(order.getTableName(), playDataList);
        if (TextUtils.isEmpty(order.getTableName())) {
            selectedFlag[0] = true;
        } else if (tableName <= DEFAULT_TABLE_COUNT) {
            selectedFlag[tableName] = true;
        } else {
            // TODO 超出最多范围，说明是输入的
            // selectedFlag[DEFAULT_TABLE_COUNT] = true;
        }
        voidOrFocAdapter.changeData(playDataList, selectedFlag, cannotClick);

    }

    public void setCloseCustomNoteView(CloseCustomNoteView closeCustomNoteView) {
        mCloseCustomNoteView = closeCustomNoteView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_note_close_img:
                if (mCloseCustomNoteView != null) {
                    mCloseCustomNoteView.onClose();
                }
                break;
            case R.id.custom_note_input_lable:
                custom_note_discount_input_lyt.setVisibility(View.VISIBLE);
                custom_note_person_ryt.setVisibility(View.VISIBLE);
                custom_note_person_edt.setHint(getResources().getString(R.string.input_table_num));
                custom_note_person_edt.setText("");
                break;
            default:
                break;
        }
    }


    //隐藏软键盘
    private void hideSoftInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) parent
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(this
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
    }


    /* 隐藏软键盘 */
    private void hideInput(View v) {

        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                    0);
        }
    }

    /**
     * 检查输入框中的数据是否合法
     *
     * @param param
     * @param defaultValue
     * @return
     */
    private String formatEditText(String param, String defaultValue) {
        if (!TextUtils.isEmpty(param)) {
            return param;
        }
        return defaultValue;
    }

    private int getInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
        }

        return 1;

    }

    private int getTable(String tableName, List<String> stringList) {
        if (!TextUtils.isEmpty(tableName) && stringList.contains(tableName)) {
            return stringList.indexOf(tableName);
        }
        return 0;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parentView, View view, final int position, long id) {
        if (position < 21) {
            UIHelp.showShortToast(parent, parent.getString(R.string.cannot_delete));
        } else {
            DialogFactory.commonTwoBtnDialog(parent, parent.getString(R.string.warning), parent.getString(R.string.delete_table_name) + " '" + playDataList.get(position) + "'?", parent.getString(R.string.cancel), parent.getString(R.string.ok), null, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> stringList = Store.getObject(parent, Store.SEND_TABLE_NAME_LIST, new TypeToken<List<String>>() {
                    }.getType());
                    if (stringList == null || stringList.size() == 0) {
                        UIHelp.showShortToast(parent, parent.getString(R.string.cannot_delete));
                    } else {
                        stringList.remove(playDataList.get(position));
                        App.instance.getLocalRestaurantConfig().getSendFoodCardNumList().remove(playDataList.get(position));
                        playDataList.remove(position);
                        voidOrFocAdapter.notifyDataSetChanged();
                        Store.saveObject(parent, Store.SEND_TABLE_NAME_LIST, stringList);
                    }
                }
            });
        }
        return true;
    }

    public interface CloseCustomNoteView {
        void onClose();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        boolean con = true;
        for (String str : cannotClick) {
            if (str.equals(playDataList.get(arg2))) {
                con = false;
                break;
            }
        }
        if (con) {
            for (int i = 0; i < playDataList.size(); i++) {
                selectedFlag[i] = false;
            }
            selectedFlag[arg2] = true;
            if (TextUtils.isEmpty(playDataList.get(arg2))) {
                OrderSQL.updateOrderTableName("", order.getId());
            } else {
                OrderSQL.updateOrderTableName(playDataList.get(arg2), order.getId());
            }
            cannotClick = OrderSQL.getUsedTableNames();
            voidOrFocAdapter.updateCannotClick(cannotClick);
            Message tableMsg = handler.obtainMessage();
            tableMsg.what = MainPage.VIEW_EVENT_SET_DATA;
            handler.sendMessage(tableMsg);
            Message tableMsg2 = handler.obtainMessage();
            tableMsg2.what = MainPageKiosk.CHECK_TO_CLOSE_CUSTOM_NOTE_VIEW;
            handler.sendMessageDelayed(tableMsg2, 200);
        }
        hideSoftInputFromWindow();
    }


    private OnEditorActionListener oeal = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // 判断是否是确定键
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                String table = custom_note_person_edt.getText().toString()
                        .trim();
                if (TextUtils.isEmpty(table)) {
                    UIHelp.showShortToast(parent, getResources().getString(R.string.input_table_num));
                } else {
                    List<String> stringList = Store.getObject(parent, Store.SEND_TABLE_NAME_LIST, new TypeToken<List<String>>() {
                    }.getType());
                    if (stringList == null) {
                        stringList = new ArrayList<String>();
                    }
                    if (!stringList.contains(table)) {
                        stringList.add(table);
                        App.instance.getLocalRestaurantConfig().getSendFoodCardNumList().add(table);
                        Store.saveObject(parent, Store.SEND_TABLE_NAME_LIST, stringList);
                    }

                    OrderSQL.updateOrderTableName(table + "",
                            order.getId());
                    Message tableMsg = handler.obtainMessage();
                    tableMsg.what = MainPage.VIEW_EVENT_SET_DATA;
                    handler.sendMessage(tableMsg);

                    Message closeMessage = handler.obtainMessage();
                    closeMessage.what = MainPageKiosk.CHECK_TO_CLOSE_CUSTOM_NOTE_VIEW;
                    handler.sendMessageDelayed(closeMessage, 200);
                }
                hideInput(v);
                return true;
            }
            return false;
        }
    };

    // 隐藏系统键盘   会员要用
//		public void hideSoftInputMethod(EditText ed) {
//			parent.getWindow().setSoftInputMode(
//					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//			int currentVersion = android.os.Build.VERSION.SDK_INT;
//			String methodName = null;
//			if (currentVersion >= 16) {
//				// 4.2
//				methodName = "setShowSoftInputOnFocus";
//			} else if (currentVersion >= 14) {
//				// 4.0
//				methodName = "setSoftInputShownOnFocus";
//			}
//
//			if (methodName == null) {
//				ed.setInputType(InputType.TYPE_NULL);
//			} else {
//				Class<EditText> cls = EditText.class;
//				Method setShowSoftInputOnFocus;
//				try {
//					setShowSoftInputOnFocus = cls.getMethod(methodName,
//							boolean.class);
//					setShowSoftInputOnFocus.setAccessible(true);
//					setShowSoftInputOnFocus.invoke(ed, false);
//				} catch (NoSuchMethodException e) {
//					ed.setInputType(InputType.TYPE_NULL);
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				}
//			}
//		}
    //会员卡号和电话号码请求
}
