package com.alfredmenu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredmenu.global.App;
import com.alfredmenu.javabean.ModifierCPVariance;
import com.alfredmenu.listener.RvItemClickListener;
import com.alfredmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcong on 2017/7/25.
 */

public class WaiterModifierAdapter extends RvAdapter<ModifierCPVariance> {
    private List<Integer> modifierIds = new ArrayList<>();

    private Context mCon;

    public WaiterModifierAdapter(Context context, List<ModifierCPVariance> list, List<Integer> modifierIds, RvItemClickListener listener) {
        super(context, list, listener);
        this.modifierIds = modifierIds;
        this.mCon = context;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new WaiterModifierHolder(view, viewType, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTitle() ? 0 : 1;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.modifier_item_title : R.layout.modifier_item_content;
    }

    private class WaiterModifierHolder extends RvHolder<ModifierCPVariance> {
        private TextView title, min;
        private TextView tv_item;
        private RelativeLayout rl_modifier_content;

        public WaiterModifierHolder(View itemView, int type, RvItemClickListener listener) {
            super(itemView, type, listener);
            switch (type) {
                case 0:
                    title = (TextView) itemView.findViewById(R.id.tv_item_title);
                    min = (TextView) itemView.findViewById(R.id.tv_modifier_min);

                    break;
                case 1:
                    tv_item = (TextView) itemView.findViewById(R.id.tv_item);
                    rl_modifier_content = (RelativeLayout) itemView.findViewById(R.id.rl_modifier_content);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void bindHolder(ModifierCPVariance modifierCPVariance, int position) {
            int itemViewTtpe = WaiterModifierAdapter.this.getItemViewType(position);
            switch (itemViewTtpe) {
                case 0:
                    title.setText(list.get(position).getModifierName());
                  //  min.setText(mCon.getResources().getString(R.string.At_least) + list.get(position).getMin() + mCon.getResources().getString(R.string.items));


                    break;
                case 1:
                    rl_modifier_content.setBackgroundResource(R.drawable.modifier_bg);
                    tv_item.setTextColor(App.instance.getResources().getColor(R.color.black));
                    tv_item.setText(list.get(position).getModifierName());
                    tv_item.setTag(list.get(position));
                    if (modifierIds != null && modifierIds.size() > 0) {
                        for (Integer id : modifierIds) {
                            if (modifierCPVariance.getModifierId() == id) {
                                rl_modifier_content.setBackgroundResource(R.drawable.modifier_bg_selected);
                                tv_item.setTextColor(App.instance.getResources().getColor(R.color.white));
                            }
                        }
                    }
                    break;
                case 2:
                    break;
            }
        }
    }

}
