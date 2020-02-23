package com.alfredmenu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredmenu.R;
import com.alfredmenu.global.UIHelp;

public class RvMenuOrderHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView titleCategory;
    public EditText notesCategory;
    public TextView priceCategory;
    public Button btnAdd, btnCancel;
    public BaseActivity context;
    public int id;

    public RvMenuOrderHolders(View itemView, BaseActivity context) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.context = context;
        titleCategory = (TextView) itemView.findViewById(R.id.titleCategory);
        notesCategory = (EditText) itemView.findViewById(R.id.txtNotes);
        priceCategory = (TextView) itemView.findViewById(R.id.priceCategory);
        btnAdd        = (Button) itemView.findViewById(R.id.btn_Add);
        btnCancel     = (Button) itemView.findViewById(R.id.btn_Cancel)
;    }

    @Override
    public void onClick(View view) {
//        UIHelp.startMenuOrderPage(context);
//        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

}