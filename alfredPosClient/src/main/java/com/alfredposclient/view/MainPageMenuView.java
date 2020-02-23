package com.alfredposclient.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.BitmapUtil;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.StockCallBack;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.adapter.ItemDetailAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.support.v7.widget.LinearSnapHelper;

public class MainPageMenuView extends LinearLayout {
	private static final int WIDTH = (int) (ScreenSizeUtil.width*(1 - (700+300)/ScreenSizeUtil.WIDTH_POS));
	private static final int OPEN_DELAY = 300;
	private static final int ONELEVELMENU= 0;
	private static final int TWOLEVELMENU = 1;

	private BaseActivity parent;
	private Handler handler;
	private RelativeLayout ll_menu;
	private RecyclerView oneLevelMenu;
	private RecyclerView twoLevelMenu;
	private ImageView iv_up;
	private ImageView iv_down;
	private ImageView iv_done;
	private LinearLayout ll_item_detail;
	private LinearLayout ll_sub_menu;
	private ScrollView sv_sub_menu;
	private ImageView iv_sub_menu_index;
	private Button btn_more_sub_menu;
	private int current_index = 0;
	private Order order;
	private TextTypeFace textTypeFace;
	private int height;
	private boolean flag;
	private int isSelectSub = 0;
	private boolean isFirst = false;
	private int touchRecyclerView = 0;
	private PagerSnapHelper pagerSnapHelper;
//	ItemDetailAdapter itemAdp;
	private List<ItemMainCategory> listMainCategorys = CoreData.getInstance()
			.getItemMainCategories();
	int size,tsize,color,textcolor;
	Context context;

	public MainPageMenuView(Context context) {
		super(context);
		this.context=context;
		init(context);
		initTextTypeFace();
		isFirst = true;
	}

	public MainPageMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init(context);
		initTextTypeFace();
		isFirst = true;
	}
	
	public void setParent(BaseActivity parent){
		this.parent = parent;
	}
	public void refreshAllMenu(){
		TwoLevelMenuAdapter twoLevelMenuAdapter = (TwoLevelMenuAdapter) twoLevelMenu.getAdapter();
		twoLevelMenuAdapter.notifyDataSetChanged();
	}
	public void setParam(Order order, Handler handler) {
		this.handler = handler;
		this.order = order;
		size= Store.getInt(App.instance, Store.TEXT_SIZE, 0);
		tsize=Store.getInt(App.instance, Store.T_TEXT_SIZE, 0);

		color= Store.getInt(App.instance, Store.COLOR_PICKER, Color.WHITE);
		textcolor=Store.getInt(App.instance, Store.T_COLOR_PICKER, Color.WHITE);
		listMainCategorys = CoreData.getInstance()
				.getItemMainCategories();
		if(size!=tsize||color!=textcolor){
			isFirst=true;

			current_index=0;
			Store.putInt(App.instance,Store.T_TEXT_SIZE,size);
			Store.putInt(App.instance,Store.T_COLOR_PICKER,color);
		}
		if(isFirst){
			oneLevelMenu.setAdapter(new OneLevelMenuAdapter());
			twoLevelMenu.setAdapter(new TwoLevelMenuAdapter());
			isFirst = false;
		}else{
//			if(oneLevelMenu != null){
//				oneLevelMenu.getAdapter().notifyDataSetChanged();
//			}
			notifyItemStockNum(current_index);
		}

	}

	private void notifyItemStockNum(int position){
		if(twoLevelMenu != null){
			TwoLevelMenuAdapter.MenuViewHolder viewHolder = (TwoLevelMenuAdapter.MenuViewHolder) twoLevelMenu.findViewHolderForAdapterPosition(position);
			if(viewHolder != null) {
				ItemDetailAdapter itemDetailAdapter = (ItemDetailAdapter) viewHolder.gv_menu_detail.getAdapter();
				if (itemDetailAdapter != null) {
					itemDetailAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private void initTextTypeFace(){
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_item_name));
	}


	List<View> itemMainCategoryViews = new ArrayList<View>();
    private void hightlightMainCategoryLabel(int index) {
    	Resources res = this.getResources();
    	TextView currentLabel =  (TextView) itemMainCategoryViews.get(index+1);
    	for (View txtview: itemMainCategoryViews) {
    		if (txtview != currentLabel) {
    			((TextView) txtview).setTextColor(res.getColor(R.color.black));
    		}else {
    			((TextView) txtview).setTextColor(res.getColor(R.color.white));
    		}
    	}
    }

	private List<ItemCategory> getItemCategory(int index){
    	List<ItemMainCategory> listMainCategorys = CoreData.getInstance()
				.getItemMainCategories();
    	ItemMainCategory itemMainCategory = null;
		if (listMainCategorys==null){
			return null;
		}
		itemMainCategory = listMainCategorys.get(index);
		final List<ItemCategory> currentItemCategories = new ArrayList<ItemCategory>();
		
		for (ItemCategory itemCategory : CoreData.getInstance()
				.getItemCategories()) {
			if (itemCategory.getItemMainCategoryId().intValue() == itemMainCategory
					.getId().intValue()) {
				currentItemCategories.add(itemCategory);
			}
		}
		return currentItemCategories;
    }
	
	private void filterItemsInSubCategory (int maincategoryid) {
//		View view = ;
		TwoLevelMenuAdapter.MenuViewHolder viewHolder = (TwoLevelMenuAdapter.MenuViewHolder) twoLevelMenu.findViewHolderForAdapterPosition(current_index);
		if(viewHolder == null) {
			return;
		}
		ItemDetailAdapter subadp = (ItemDetailAdapter) viewHolder.gv_menu_detail.getAdapter();

		List<ItemDetail>  currentItemDetails = new ArrayList<ItemDetail>();
		for (ItemDetail itemDetail : CoreData.getInstance()
				.getItemDetails()) {
			if ((itemDetail.getItemMainCategoryId().intValue() == maincategoryid)) {
				currentItemDetails.add(itemDetail);
			}
		}
		subadp.setItemDetails(currentItemDetails);
	}   
	public void filterItemsInSubCategory (int maincategoryid, int subcategoryid) {
//		View view = twoLevelMenu.getChildAt(getIndex(twoLevelMenu));
		TwoLevelMenuAdapter.MenuViewHolder viewHolder = (TwoLevelMenuAdapter.MenuViewHolder) twoLevelMenu.findViewHolderForAdapterPosition(current_index);
		if(viewHolder == null) {
			return;
		}
		ItemDetailAdapter subadp = (ItemDetailAdapter) viewHolder.gv_menu_detail.getAdapter();
		
		List<ItemDetail>  currentItemDetails = new ArrayList<ItemDetail>();
		for (ItemDetail itemDetail : CoreData.getInstance()
				.getItemDetails()) {
			if ((itemDetail.getItemMainCategoryId().intValue() == maincategoryid) 
					&& (itemDetail.getItemCategoryId().intValue() == subcategoryid)) {
				currentItemDetails.add(itemDetail);
			}
		}

		subadp.setItemDetails(currentItemDetails);		
	}
	class OneLevelMenuAdapter extends RecyclerView.Adapter<OneLevelMenuAdapter.CategoryViewHolder>{
		private List<ItemMainCategory> itemMainCategoryList = new ArrayList<>();
		public OneLevelMenuAdapter(){
			itemMainCategoryList.clear();
			itemMainCategoryList.addAll(CoreData.getInstance().getItemMainCategories());
			itemMainCategoryList.add(0, null);
			itemMainCategoryList.add(null);

		}
		@Override
		public CategoryViewHolder onCreateViewHolder(ViewGroup arg1, int viewType) {

			return new CategoryViewHolder(LayoutInflater.from(parent).inflate(R.layout.item_main_category, null));
		}

		@Override
		public void onBindViewHolder(CategoryViewHolder holder, final int position) {
			ItemMainCategory itemMainCategory = itemMainCategoryList.get(position);
			if(itemMainCategory == null){
				holder.tv_main_category.setText("");
				holder.tv_main_category.setOnClickListener(null);
			}else {
				holder.tv_main_category.setText(itemMainCategory.getMainCategoryName());
				LogUtil.e("TEST", itemMainCategory.getMainCategoryName());
				holder.tv_main_category.setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (!ButtonClickTimer.canClick(v)) {
									return;
								}
								if(oneLevelMenu.isAnimating() || twoLevelMenu.isAnimating()){
									return;
								}
								//
								if(position == 0 || position == itemMainCategoryList.size() -1){
									return;
								}
								if (position - 1 == current_index) {//选中点击
									if (!flag) {//点击切换
										openSubMenu();
										flag = true;
									} else {
										closeSubMenu(listMainCategorys.get(current_index).getId());
									}

								} else {
									LogUtil.e("TEST", "===" + position);
									closeSubMenu(listMainCategorys.get(current_index).getId());
									moveToPosition(oneLevelMenu, position - 1);
									moveToPosition(twoLevelMenu, position - 1);
									current_index = position - 1;
								}
							}
						});
			}
		}

		@Override
		public int getItemCount() {
			return itemMainCategoryList.size();
		}

		class CategoryViewHolder extends RecyclerView.ViewHolder{
			TextView tv_main_category;
			public CategoryViewHolder(View itemView) {
				super(itemView);
				tv_main_category = (TextView) itemView
						.findViewById(R.id.tv_main_category);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						WIDTH / 3, oneLevelMenu.getHeight());
				tv_main_category.setLayoutParams(params);
			}
		}
	}

	class TwoLevelMenuAdapter extends RecyclerView.Adapter<TwoLevelMenuAdapter.MenuViewHolder>{
		private List<ItemMainCategory> itemMainCategoryList = CoreData.getInstance().getItemMainCategories();;
		@Override
		public MenuViewHolder onCreateViewHolder(ViewGroup arg1, int viewType) {
			View  itemView = LayoutInflater.from(parent).inflate(R.layout.item_menu_detial, arg1, false);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			WIDTH, LayoutParams.MATCH_PARENT);
			itemView.setLayoutParams(params);
			return new MenuViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(MenuViewHolder holder, int position) {
			ItemMainCategory itemMainCategory = itemMainCategoryList.get(position);
			List<ItemDetail> currentItemDetails = new ArrayList<ItemDetail>();
			for (ItemDetail itemDetail : CoreData.getInstance()
					.getItemDetails()) {
				if (itemDetail.getItemMainCategoryId().intValue() == itemMainCategory
						.getId().intValue()) {
					currentItemDetails.add(itemDetail);
				}
			}
			ItemDetailAdapter itemAdp = new ItemDetailAdapter(parent,
					currentItemDetails);
			holder.gv_menu_detail.setAdapter(itemAdp);
		}

		@Override
		public int getItemCount() {
			return itemMainCategoryList.size();
		}

		class MenuViewHolder extends RecyclerView.ViewHolder{
			MyGridView gv_menu_detail;
			public MenuViewHolder(View itemView) {
				super(itemView);
				gv_menu_detail = (MyGridView) itemView
						.findViewById(R.id.gv_menu_detail);

				if( Store.getInt(App.instance, Store.TEXT_SIZE, 0)==1)
				{
					gv_menu_detail.setVerticalSpacing(ScreenSizeUtil.dip2px((Activity) context, 5));
					gv_menu_detail.setHorizontalSpacing(ScreenSizeUtil.dip2px((Activity) context, 5));
				}
				gv_menu_detail.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						int numColumns = (int)Math.floor(gv_menu_detail.getWidth()/(gv_menu_detail.getVerticalSpacing() + ScreenSizeUtil.dip2px(parent, ItemDetailAdapter.ITEM_WIDTH_HEIGHT)));
						gv_menu_detail.setNumColumns(numColumns);
//						LogUtil.e("TEST", "宽===" + gv_menu_detail.getWidth() + "高====" + gv_menu_detail.getHeight());
					}
				});
				gv_menu_detail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View view, final int position, long id) {

						final ItemDetail itemDetail = (ItemDetail) arg0
								.getItemAtPosition(position);
						RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
						if(remainingStock!=null) {
							DialogFactory.commonTwoBtnInputIntDialog(parent, false, parent.getString(R.string.num), "", context.getString(R.string.cancel), context.getString(R.string.done).toUpperCase(),
									new OnClickListener() {
										@Override
										public void onClick(View view) {
//
										}
									},
									new OnClickListener() {
										@Override
										public void onClick(View view) {

											EditText editText = (EditText) view;
											String num = editText.getText().toString();
											if (!TextUtils.isEmpty(num)) {
												Map<String, Object> reMap = new HashMap<String, Object>();
												reMap.put("itemId", itemDetail.getItemTemplateId());
												reMap.put("num", Integer.valueOf(num).intValue());
												RemainingStockSQL.updateRemainingNum(Integer.valueOf(num).intValue(), itemDetail.getItemTemplateId());
												SyncCentre.getInstance().updateReaminingStockByItemId(context, reMap, null);
												notifyItemStockNum(current_index);
											}
										}
									});
						}

						return true;
					}
				});
				gv_menu_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
						final ItemDetail itemDetail = (ItemDetail) arg0
								.getItemAtPosition(arg2);
						RemainingStock remainingStock=RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
						final OrderDetail orderDetail = ObjectFactory.getInstance()
								.getOrderDetail(order, itemDetail, 0);
						if(remainingStock!=null) {
							int num =orderDetail.getItemNum();
//                            int existedOrderDetailNum = OrderDetailSQL.getOrderDetailCountByOrderIdAndItemDetailId(order.getId(), itemDetail.getId());
//                            existedOrderDetailNum += orderDetail.getItemNum();
						     RemainingStockHelper.updateRemainingStockNum(remainingStock, num, false, new StockCallBack() {
								@Override
								public void onSuccess(Boolean isStock) {
									if (isStock) {
										App.instance.getSyncJob().updateRemainingStockNum(itemDetail.getItemTemplateId());
										Message msg = handler.obtainMessage();
										msg.what = MainPage.VIEW_EVENT_ADD_ORDER_DETAIL;
										msg.obj = orderDetail;
										handler.sendMessage(msg);

									}else{
										UIHelp.showShortToast(parent, parent.getString(R.string.out_of_stock));
									}
								}
							});

						}else {
							Message msg = handler.obtainMessage();
							msg.what = MainPage.VIEW_EVENT_ADD_ORDER_DETAIL;
							msg.obj = orderDetail;
							handler.sendMessage(msg);
						}
					}
				});
			}
		}
	}

	private void init(Context context) {
		View.inflate(context, R.layout.main_page_menu_view_test, this);
		ll_menu = (RelativeLayout) findViewById(R.id.rl_menu);
		iv_done = (ImageView) findViewById(R.id.iv_done);
		iv_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!ButtonClickTimer.canClick(v)){
					return;
				}
				closeModifiers();
			}
		});
		ll_item_detail = (LinearLayout) findViewById(R.id.ll_item_detail);
		ll_item_detail.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				height = ll_item_detail.getMeasuredHeight();
			}
		});
		sv_sub_menu = (ScrollView) findViewById(R.id.sv_sub_menu);
		iv_sub_menu_index = (ImageView) findViewById(R.id.iv_sub_menu_index);
		ll_sub_menu = (LinearLayout) findViewById(R.id.ll_sub_menu);
		iv_up = (ImageView) findViewById(R.id.iv_up);
		iv_down = (ImageView) findViewById(R.id.iv_down);
		oneLevelMenu = (RecyclerView) findViewById(R.id.hsv_one_level_menu);
		oneLevelMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
		LinearSnapHelper snapHelper = new LinearSnapHelper();
		snapHelper.attachToRecyclerView(oneLevelMenu);
		twoLevelMenu = (RecyclerView) findViewById(R.id.hsv_two_level_menu);
		twoLevelMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
		twoLevelMenu.setNestedScrollingEnabled(false);
		pagerSnapHelper = new PagerSnapHelper();
		pagerSnapHelper.attachToRecyclerView(twoLevelMenu);

		oneLevelMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				LogUtil.e("TEST", "oneLevelMenuin == " + touchRecyclerView + "newState == " + newState );
				if(newState == RecyclerView.SCROLL_STATE_IDLE) {
					if (touchRecyclerView == ONELEVELMENU) {
						closeSubMenu(listMainCategorys.get(current_index).getId());
						int index = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
						moveToPosition(twoLevelMenu, index);
						current_index = index;
					}
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView,dx,dy);
			}
		});

		twoLevelMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				LogUtil.e("TEST", "twoLevelMenuin == " + touchRecyclerView + "newState == " + newState );
				if(newState == RecyclerView.SCROLL_STATE_IDLE){
					LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
					int index = linearLayoutManager.findFirstVisibleItemPosition();
					if(touchRecyclerView == TWOLEVELMENU){
						moveToPosition(oneLevelMenu, index);
						current_index = index;
					}
					if(listMainCategorys.get(current_index).getId() == isSelectSub){
						closeSubMenu(isSelectSub);
					}
				}

			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView,dx,dy);
			}
		});


		oneLevelMenu.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				touchRecyclerView = ONELEVELMENU;
				LogUtil.e("TEST", "twoLevelMenu===in==" + touchRecyclerView);
				return false;
			}
		});

		twoLevelMenu.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				touchRecyclerView = TWOLEVELMENU;
				LogUtil.e("TEST", "twoLevelMenu===in==" + touchRecyclerView);
				return false;
			}
		});

	}



	private void initMenuModifierDetail(Order order, OrderDetail orderDetail,
										List<ItemModifier> itemModifiers) {
		ll_item_detail.removeAllViews();
		for (ItemModifier itemModifier : itemModifiers) {
			ModifierView modifierView = new ModifierView(parent);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			modifierView.setLayoutParams(params);
			if (itemModifiers.indexOf(itemModifier) % 2 == 1) {//判断为奇数时，背景色改变
				modifierView.setBackgroundResource(R.color.modifier_odd);
			}
			modifierView.setParams(order, orderDetail, itemModifier, handler,height);
			ll_item_detail.addView(modifierView);
		}
	}

	private void initSubMenu(List<ItemCategory> itemCategories){
		ll_sub_menu.removeAllViews();
		SubMenuView subMenuView = new SubMenuView(parent);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		params.topMargin = 20;
		subMenuView.setLayoutParams(params);
		subMenuView.setParams(WIDTH, itemCategories, handler);
		ll_sub_menu.addView(subMenuView);
	}
	
	public void openSubMenu() {
		initSubMenu(getItemCategory(current_index));
		sv_sub_menu.setVisibility(View.VISIBLE);
		iv_sub_menu_index.setVisibility(View.VISIBLE);
		if (AnimatorListenerImpl.isRunning) {
			return;
		}
	}


	public void closeSubMenu(int maincategoryid,int subcategoryid){
		isSelectSub = maincategoryid;
		flag = false;
		filterItemsInSubCategory(maincategoryid, subcategoryid);
		if (AnimatorListenerImpl.isRunning) {
			return;
		}
		sv_sub_menu.setVisibility(View.GONE);
		iv_sub_menu_index.setVisibility(View.GONE);
	}
	public void closeSubMenu(int maincategoryid){
		flag = false;
		if(isSelectSub != 0) {
			filterItemsInSubCategory(maincategoryid);
		}
		if (AnimatorListenerImpl.isRunning ) {
			return;
		}
		sv_sub_menu.setVisibility(View.GONE);
		iv_sub_menu_index.setVisibility(View.GONE);
		isSelectSub = 0;
	}
	
	public void openModifiers(Order order, OrderDetail orderDetail,
			List<ItemModifier> itemModifiers) {
		initMenuModifierDetail(order, orderDetail, itemModifiers);
		if (AnimatorListenerImpl.isRunning) {
			return;
		}
		iv_up.setVisibility(View.VISIBLE);
		iv_down.setVisibility(View.VISIBLE);
		Bitmap bitmap = BitmapUtil.convertViewToBitmap(ll_menu);
		iv_up.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight() / 2));
		iv_down.setImageBitmap(Bitmap.createBitmap(bitmap, 0,
				bitmap.getHeight() / 2, bitmap.getWidth(),
				bitmap.getHeight() / 2));
		
		if (!bitmap.isRecycled())
			bitmap.recycle();

		ll_menu.setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_item_name)).setText(CoreData.getInstance().getItemDetailById(orderDetail.getItemId(),orderDetail.getItemName()).getItemName());
		ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_up, "y",
				iv_up.getY(), iv_up.getY() - iv_up.getHeight()).setDuration(
				OPEN_DELAY);
		ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_down, "y",
				iv_down.getY(), iv_down.getY() + iv_down.getHeight())
				.setDuration(OPEN_DELAY);
		final ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv_up, "y",
				iv_up.getY() - iv_up.getHeight(), iv_up.getY()).setDuration(
				1);
		final ObjectAnimator animator4 = ObjectAnimator.ofFloat(iv_down, "y",
				iv_down.getY() + iv_down.getHeight(), iv_down.getY())
				.setDuration(1);
		AnimatorSet animSet = new AnimatorSet();
		animSet.playTogether(animator1, animator2);
		animSet.addListener(new AnimatorListenerImpl(){
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				ll_menu.post(new Runnable() {

					@Override
					public void run() {
						System.out.println("==========donghua");
						AnimatorSet animSet1 = new AnimatorSet();
						animSet1.playTogether(animator3, animator4);
						animSet1.start();
						iv_up.setVisibility(View.GONE);
						iv_down.setVisibility(View.GONE);
						
					}
				});
			};
		});
		animSet.start();
		
	}

	public boolean isModifierOpen(){
		if(ll_menu != null && ll_menu.getVisibility() != View.VISIBLE)
			return true;
		else
			return false;
	}

	public void closeModifiers() {
		if (AnimatorListenerImpl.isRunning || ll_menu.getVisibility() == View.VISIBLE) {
			return;
		}
		iv_up.setVisibility(View.VISIBLE);
		iv_down.setVisibility(View.VISIBLE);
		ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv_up, "y",
				iv_up.getY(), iv_up.getY() - iv_up.getHeight()).setDuration(
				0);
		ObjectAnimator animator4 = ObjectAnimator.ofFloat(iv_down, "y",
				iv_down.getY(), iv_down.getY() + iv_down.getHeight())
				.setDuration(0);
		
		AnimatorSet animSet = new AnimatorSet();
		animSet.playTogether(animator3, animator4);
		animSet.addListener(new AnimatorListenerImpl() {
			public void onAnimationEnd(Animator animation) {
				AnimatorSet animSet = new AnimatorSet();
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_up, "y",
						iv_up.getY(), iv_up.getY() + iv_up.getHeight()).setDuration(
						OPEN_DELAY);
				ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_down, "y",
						iv_down.getY(), iv_down.getY() - iv_down.getHeight())
						.setDuration(OPEN_DELAY);
				animSet.playTogether(animator1, animator2);
				animSet.start();
				animSet.addListener(new AnimatorListenerImpl(){
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						ll_menu.post(new Runnable() {

							@Override
							public void run() {
								ll_menu.setVisibility(View.VISIBLE);
							}
						});
					}
				});
			};
		});
		animSet.start();
	}

	private void moveToPosition(final RecyclerView hsv, final int index) {
		hsv.post(new Runnable() {
			@Override
			public void run() {
				//先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
				int firstItem = ((LinearLayoutManager)hsv.getLayoutManager()).findFirstVisibleItemPosition();
				int lastItem = ((LinearLayoutManager)hsv.getLayoutManager()).findLastVisibleItemPosition();
				if(hsv.getId() == R.id.hsv_one_level_menu){
					//然后区分情况
					if (index <= firstItem) {
						//当要置顶的项在当前显示的第一个项的前面时
						hsv.scrollToPosition(index);
					} else if (index <= lastItem) {
						//当要置顶的项已经在屏幕上显示时
						int top = hsv.getLayoutManager().findViewByPosition(index).getLeft();
						hsv.scrollBy(top, 0);
					} else {
						//当要置顶的项在当前显示的最后一项的后面时
						hsv.scrollToPosition(index);
						//这里这个变量是用在RecyclerView滚动监听里面的
					}
				}else {
					//然后区分情况
					if (index <= firstItem) {
						//当要置顶的项在当前显示的第一个项的前面时
						hsv.smoothScrollToPosition(index);
					} else if (index <= lastItem) {
						//当要置顶的项已经在屏幕上显示时
						int top = hsv.getLayoutManager().findViewByPosition(index).getLeft();
						hsv.smoothScrollBy(top, 0);
					} else {
						//当要置顶的项在当前显示的最后一项的后面时
						hsv.smoothScrollToPosition(index);
						//这里这个变量是用在RecyclerView滚动监听里面的
					}
				}
			}
		});
	}

	public void hintKeyBoard () {
		//拿到 InputMethodManager
		InputMethodManager imm = (InputMethodManager)parent.getSystemService(Context.INPUT_METHOD_SERVICE); //如果window上view获取焦点 && view不为空
		if(imm.isActive()&&parent.getCurrentFocus()!=null){
			//拿到view的token 不为空
			if (parent.getCurrentFocus().getWindowToken()!=null)
			{ //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
				imm.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}}
}
