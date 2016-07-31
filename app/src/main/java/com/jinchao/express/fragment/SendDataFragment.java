package com.jinchao.express.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jinchao.express.R;
import com.jinchao.express.activity.MainActivity;
import com.jinchao.express.activity.PackageDetailActivity;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.base.CommonAdapter;
import com.jinchao.express.base.ViewHolder;
import com.jinchao.express.dbentity.ExpressPackage;
import com.jinchao.express.utils.CommonUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/7/12.
 */
@ContentView(R.layout.fragment_senddata)
public class SendDataFragment extends BaseFragment {
    @ViewInject(R.id.lv) ListView lv;
    @ViewInject(R.id.empty_view) RelativeLayout empty_view;
    private DbManager db;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db= CommonUtils.getDbManager();
        try {
            List<ExpressPackage> list=db.findAll(ExpressPackage.class);
            if (list==null||list.size()==0){
                empty_view.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).tv_right.setVisibility(View.GONE);
            }else{
                empty_view.setVisibility(View.GONE);
                ((MainActivity)getActivity()).tv_right.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                CommonAdapter<ExpressPackage> adapter =new CommonAdapter<ExpressPackage>(getActivity(),list,R.layout.item_senddata) {
                    @Override
                    public void convert(ViewHolder helper, ExpressPackage item, int position) {
                        helper.setText(R.id.tv_yundanhao,"运单号："+item.getYundanhao());
                        helper.setText(R.id.tv_name,"寄件人："+item.getName());
                        helper.setText(R.id.tv_time,"日期："+item.getTime());
                        x.image().bind(((ImageView)helper.getView(R.id.iv_kuaijian)),item.getPackagepic());
                    }
                };
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ExpressPackage expressPackage = (ExpressPackage) ((ListView)parent).getItemAtPosition(position);
                        Intent intent =new Intent(getActivity(), PackageDetailActivity.class);
                        intent.putExtra("package",expressPackage);
                        getActivity().startActivity(intent);
                    }
                });
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
