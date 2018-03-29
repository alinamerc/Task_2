package com.zhirova.task_2;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhirova.task_2.adapter.PhonesAdapter;
import com.zhirova.task_2.model.Contact;

import java.util.List;

public class DetailFragment extends Fragment implements PhonesAdapter.ClickListenerOnCall, PhonesAdapter.ClickListenerOnSms {

    private final String TAG = "DETAIL_FRAGMENT";
    private TextView nameContact;
    private RecyclerView phonesRecyclerView;
    private PhonesAdapter phonesAdapter;

    public Contact contactInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        phonesConnection();
    }


    private void initUI() {
        nameContact = getActivity().findViewById(R.id.detail_name_text_view);
        nameContact.setText(contactInfo.getName());
        phonesRecyclerView = getView().findViewById(R.id.detail_recycler_view_phones);
    }


    private void phonesConnection() {
        List<String> phones = contactInfo.getPhone();

        if (phones != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            phonesRecyclerView.setLayoutManager(layoutManager);

            phonesAdapter = new PhonesAdapter(getContext());
            phonesAdapter.setClickListenerOnCall(this);
            phonesAdapter.setClickListenerOnSms(this);

            phonesRecyclerView.setAdapter(phonesAdapter);
            phonesAdapter.setData(phones);
        }
    }


    @Override
    public void onClickCall(String phone) {
        Log.d(TAG, "onClickCall");
    }


    @Override
    public void onClickSms(String phone) {
        Log.d(TAG, "onClickSms");
    }


}
