package com.alfredposclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredposclient.R;
import com.alfredposclient.adapter.DeliveryApdater;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDialogActivity extends Activity {


    // 布局
    private Button selectAll, invert, sure, cancle;

    private ListView layoutList;

    // 布局状态监听
    private boolean selectAllState;

    // 数据
    private List<AppOrder> appOrders = new ArrayList<AppOrder>();

    private ArrayList<AppOrder> selectedStudent = new ArrayList<AppOrder>();
    private DeliveryApdater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.de_dialog_actvity);
        //  initData();
        appOrders= (List<AppOrder>) getIntent().getSerializableExtra("apporder");
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {


        sure = (Button) findViewById(R.id.dialogActivity_sure);
        cancle = (Button) findViewById(R.id.dialogActivity_cancle);

        layoutList = (ListView) findViewById(R.id.dialogActivity_listView);
        adapter = new DeliveryApdater(DeliveryDialogActivity.this, (ArrayList<AppOrder>) appOrders,
                new DeliveryApdater.OnSelectedItemChanged() {


                    public void getSelectedCount(int count) {
                        // tv_count.setText("总计：    " + count + " 次");
                    }


                    public void getSelectedItem(AppOrder appOrder) {
//                        Toast.makeText(DeliveryDialogActivity.this, appOrder.getOrderNo(),
//                                Toast.LENGTH_SHORT).show();
                    }
                });
        layoutList.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
//    private void initData() {
//        selectAllState = false;
//        dataController = new DataController();
//        if (dataController.getData() != null) {
//            students = dataController.getData();
//        } else {
//            Toast.makeText(DialogActivity.this, "获取数据失败!", Toast.LENGTH_SHORT)
//                    .show();
//        }
//    }

    /**
     * 被选项的获取
     *
     * @return
     */
    public ArrayList<AppOrder> getSelectedStudent() {
        return selectedStudent;
    }

    /**
     * 全选、反选、确定、取消 四个按钮的事件监听
     *
     * @param view
     */
    public void doClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
//            case R.id.dialogActivity_selectAll:// 全选/全不选
//                selectAllState=selectAllState==false?true:false;
//                if (selectAllState) {
//                    adapter.selectAll();//通知变更状态
//                } else
//                    adapter.disSelectAll();
//                break;
//            case R.id.dialogActivity_Invert:// 反选
//                adapter.switchSelect();
//                break;
            case R.id.dialogActivity_sure:// 确定
                selectedStudent = adapter.currentSelect();
                intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedApporder",
                        selectedStudent);// 将数据打包存入intent
                intent.putExtras(bundle);

                DeliveryDialogActivity.this.setResult(200,
                        intent);
                DeliveryDialogActivity.this.finish();
                break;
//            case R.id.dialogActivity_cancle:// 取消
//                intent = new Intent();
//                DialogActivity.this.setResult(
//                        Consts.RESULT_CODE_DIALOG2MAIN_CANCLE, intent);
//                DialogActivity.this.finish();
//                break;
            default:
                break;
        }
    }
}
