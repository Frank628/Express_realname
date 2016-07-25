package com.jinchao.express.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jinchao.express.R;
import com.jinchao.express.base.BaseFragment;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by OfferJiShu01 on 2016/7/12.
 */
@ContentView(R.layout.fragment_senddata)
public class SendDataFragment extends BaseFragment {
@ViewInject(R.id.recyclerview) RecyclerView recyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
