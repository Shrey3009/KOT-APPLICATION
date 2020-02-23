package com.alfredposclient.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.utils.BH;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CalendarCard extends RelativeLayout {
	
	private TextView cardTitle;
	private RelativeLayout previous;
	private RelativeLayout next;
	private int itemLayout = R.layout.card_item_simple;
	private OnItemRender mOnItemRender;
	private OnItemRender mOnItemRenderDefault;
	private OnCellItemClick mOnCellItemClick;
	private Calendar dateDisplay;
	private ArrayList<CheckableLayout> cells = new ArrayList<CheckableLayout>();
	private LinearLayout cardGrid;
	private int pos = 1;
	private String amount;
	Map<Date, String> grossTotals = new HashMap<>();

	public CalendarCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public CalendarCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CalendarCard(Context context) {
		super(context);
		init(context);
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setGrossTotals(Map<Date, String> grossTotals)
	{
		this.grossTotals = grossTotals;
	}

	private void init(final Context ctx) {
		if (isInEditMode()) return;
		View layout = LayoutInflater.from(ctx).inflate(R.layout.card_view, null, false);
		
		if (dateDisplay == null)
			dateDisplay = Calendar.getInstance(Locale.US);
		
		cardTitle = (TextView)layout.findViewById(R.id.cardTitle);
		cardGrid = (LinearLayout)layout.findViewById(R.id.cardGrid);
		previous = (RelativeLayout) layout.findViewById(R.id.previous);
		next = (RelativeLayout) layout.findViewById(R.id.next);
		previous.setOnClickListener(onClickListener);
		next.setOnClickListener(onClickListener);
		
		cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
		
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		((TextView)layout.findViewById(R.id.cardDay1)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay2)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay3)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay4)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay5)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay6)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay7)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		
		LayoutInflater la = LayoutInflater.from(ctx);
		for(int y=0; y<cardGrid.getChildCount(); y++) {
			LinearLayout row = (LinearLayout)cardGrid.getChildAt(y);
			for(int x=0; x<row.getChildCount(); x++) {
				CheckableLayout cell = (CheckableLayout)row.getChildAt(x);
				cell.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						for(CheckableLayout c : cells)
							c.setChecked(false);
						((CheckableLayout)v).setChecked(true);
						
						if (getOnCellItemClick()!= null)
							getOnCellItemClick().onCellClick(v, (CardGridItem)v.getTag());
					}
				});
				
				View cellContent = la.inflate(itemLayout, cell, false);
				cell.addView(cellContent);
				cells.add(cell);
			}
		}
		
		addView(layout);
		
		mOnItemRenderDefault = new OnItemRender() {
			@Override
			public void onRender(CheckableLayout v, CardGridItem item)
			{
				boolean boo = v.isEnabled();
				((CheckedTextView)((RelativeLayout)v.getChildAt(0)).getChildAt(0)).setText(item.getDayOfMonth().toString());
				Calendar cal = Calendar.getInstance(Locale.US);
				cal.setTime(new Date(App.instance.getBusinessDate()));
				String mStr = new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(cal.getTime());
				String str = new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime());

				int mDay = cal.get(Calendar.DAY_OF_MONTH);
				if (boo && mDay == item.getDayOfMonth() && mStr.equals(str)){
					v.setChecked(true);
					((RelativeLayout)v.getChildAt(0)).getChildAt(1).setVisibility(VISIBLE);
					v.setBackgroundColor(Color.parseColor("#EEE685"));
					if (!TextUtils.isEmpty(amount))
					{
						((TextView) ((RelativeLayout) v.getChildAt(0)).getChildAt(1)).setText(ctx.getString(R.string.gross_total_sales)+":" + "\n" + BH.formatMoney(amount));
					}
					else
					{
						((TextView) ((RelativeLayout) v.getChildAt(0)).getChildAt(1)).setText("");
					}
				}
				else
				{
					v.setChecked(false);
					v.setBackgroundResource(R.drawable.card_item_bg);
					((RelativeLayout)v.getChildAt(0)).getChildAt(1).setVisibility(INVISIBLE);
				}
//				((ScrollView)findViewById(R.id.sv_cardGrid)).arrowScroll(((ScrollView)findViewById(R.id.sv_cardGrid)).getMaxScrollAmount());
			}
		};
		updateCells();
	}

	OnClickListener onClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Calendar calendar = Calendar.getInstance(Locale.US);
			switch (v.getId())
			{
				case R.id.next:
					calendar.add(Calendar.MONTH, pos++);
					setDateDisplay(calendar);
					updateCells();
					break;
				case R.id.previous:
					calendar.add(Calendar.MONTH, pos--);
					setDateDisplay(calendar);
					updateCells();
					break;
			}
		}
	};
	
	private int getDaySpacing(int dayOfWeek) {
		if (Calendar.SUNDAY == dayOfWeek)
			return 6;
		else
			return dayOfWeek - 2;
	}
	
	private int getDaySpacingEnd(int dayOfWeek) {
		return 8 - dayOfWeek;
	}
	
	private void updateCells() {
		Calendar cal;
		Integer counter = 0;
		if (dateDisplay != null) 
			cal = (Calendar)dateDisplay.clone();
		else
			cal = Calendar.getInstance(Locale.US);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		int daySpacing = getDaySpacing(cal.get(Calendar.DAY_OF_WEEK));
		if (daySpacing > 0) {
			Calendar prevMonth = (Calendar)cal.clone();
			prevMonth.add(Calendar.MONTH, -1);
			prevMonth.set(Calendar.DAY_OF_MONTH, prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - daySpacing + 1);
			for(int i=0; i<daySpacing; i++) {
				CheckableLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(Integer.valueOf(prevMonth.get(Calendar.DAY_OF_MONTH))).setEnabled(false));
				cell.setEnabled(false);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
				counter++;
				prevMonth.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int firstDay = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDay = cal.get(Calendar.DAY_OF_MONTH)+1;
		for(int i=firstDay; i<lastDay; i++)
		{
			cal.set(Calendar.DAY_OF_MONTH, i-1);
			Calendar date = (Calendar)cal.clone();
			date.add(Calendar.DAY_OF_MONTH, 1);
			CheckableLayout cell = cells.get(counter);
			cell.setTag(new CardGridItem(i).setEnabled(true).setDate(date));
			cell.setEnabled(true);
			cell.setVisibility(View.VISIBLE);
			(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
			counter++;

			Set set = grossTotals.entrySet();
			for (Object o : set)
			{
				Map.Entry entry = (Map.Entry) o;
				date.set(Calendar.HOUR, 0);
				date.set(Calendar.MINUTE, 0);
				date.set(Calendar.SECOND, 0);
				date.set(Calendar.MILLISECOND, 0);
				date.set(Calendar.HOUR_OF_DAY, 0);
				Date curDate = date.getTime();
				if(String.valueOf(curDate).equals(String.valueOf(entry.getKey())))
				{
					((RelativeLayout)cell.getChildAt(0)).getChildAt(1).setVisibility(VISIBLE);
					((TextView) ((RelativeLayout) cell.getChildAt(0)).getChildAt(1)).setText(getContext().getString(R.string.gross_total_sales)+":" + "\n" + BH.formatMoney(String.valueOf(entry.getValue())));
				}
				else
				{
					if(!grossTotals.containsKey(curDate))
					{
						((TextView) ((RelativeLayout) cell.getChildAt(0)).getChildAt(1)).setText("");
					}
				}
			}
		}
		
		if (dateDisplay != null) 
			cal = (Calendar)dateDisplay.clone();
		else
			cal = Calendar.getInstance(Locale.US);
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		daySpacing = getDaySpacingEnd(cal.get(Calendar.DAY_OF_WEEK));

		if (daySpacing > 0) {
			for(int i=0; i<daySpacing; i++) {
				CheckableLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(i+1).setEnabled(false));
				cell.setEnabled(false);
				cell.setVisibility(View.VISIBLE);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
				counter++;
			}
		}
		
		if (counter < cells.size()) {
			for(int i=counter; i<cells.size(); i++) {
				cells.get(i).setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && cells.size() > 0) {
			int size = (r - l) / 7;
			for(CheckableLayout cell : cells) {
				cell.getLayoutParams().height = size;
			}
		}
	}

	public void setDateDisplay(Calendar dateDisplay) {
		this.dateDisplay = dateDisplay;
		cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
	}

	public OnCellItemClick getOnCellItemClick() {
		return mOnCellItemClick;
	}

	public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
		this.mOnCellItemClick = mOnCellItemClick;
	}

	public void notifyChanges() {
		updateCells();
	}

}
