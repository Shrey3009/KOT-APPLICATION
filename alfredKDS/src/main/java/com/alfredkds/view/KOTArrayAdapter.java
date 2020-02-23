package com.alfredkds.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alfredkds.R;
import com.alfredkds.javabean.Kot;

import java.util.ArrayList;
import java.util.List;

public class KOTArrayAdapter extends RecyclerView.Adapter<KOTArrayAdapter.ViewHolder> {

    private Context mContext;
    private List<Kot> kots = new ArrayList<Kot>();
    private boolean addFirstItem = false;
    private Handler handler;
//	private Map<Integer, Long> times = new HashMap<Integer, Long>();

    public KOTArrayAdapter(Context mContext, Handler handler) {
        super();
//		times.clear();
        this.mContext = mContext;
        this.handler = handler;
    }

    public void setAddFirstItem(boolean addFirstItem) {
        this.addFirstItem = addFirstItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = View.inflate(mContext, R.layout.kot_array_view, null);
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.kotView = (KOTView) convertView.findViewById(R.id.kotview);
        viewHolder.kotView.setParams(mContext, handler);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Kot originKot = kots.get(position);
        holder.kotView.setData(originKot);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return kots.size();
    }

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder;
////		KOTView kotView;
//		if (convertView == null) {
//			convertView = View.inflate(mContext,R.layout.kot_array_view, null);
//			holder = new ViewHolder();
//			holder.kotView = (KOTView) convertView.findViewById(R.id.kotview);
//			holder.kotView.setParams(mContext, handler);
//			convertView.setTag(holder);
////			kotView = new KOTView(mContext, mHandler);
////			convertView = kotView;
////			convertView.setTag(kotView);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
////			kotView = (KOTView) convertView.getTag();
//		}
//		Kot originKot = kots.get(position);
//		holder.kotView.setData(originKot);
////		if (times.containsKey(position)){
////			Long tm = times.get(position);
////			tm = holder.kotView.getTime();
////		}else {
////			times.put(position, holder.kotView.getTime());
////		}
//		if (addFirstItem && position == 0) {
//			holder.kotView.showNewKOT();
//			addFirstItem = false;
//		}
//		return convertView;
//	}

    class ViewHolder extends RecyclerView.ViewHolder {
        public KOTView kotView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * @return the kots
     */
    public List<Kot> getKots() {
        return kots;
    }

    /**
     * @param kots the kots to set
     */
    public void setKots(List<Kot> kots) {
        this.kots = kots;
    }

}