package com.jinchao.express.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jinchao.express.R;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.dbentity.ExpressPackage;
import com.jinchao.express.utils.CommonUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

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
            }else{
                empty_view.setVisibility(View.GONE);
//                lv.setAdapter();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
