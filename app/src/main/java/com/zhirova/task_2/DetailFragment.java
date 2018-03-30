package com.zhirova.task_2;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhirova.task_2.adapter.EmailsAdapter;
import com.zhirova.task_2.adapter.PhonesAdapter;
import com.zhirova.task_2.model.Contact;

import java.util.List;

public class DetailFragment extends Fragment implements PhonesAdapter.ClickListenerOnCall,
        PhonesAdapter.ClickListenerOnSms, EmailsAdapter.ClickListenerOnMail {

    private static final String BUNDLE_KEY = "CONTACT_ID";
    private final String TAG = "DETAIL_FRAGMENT";

    private TextView nameContact;
    private RecyclerView phonesRecyclerView;
    private RecyclerView emailsRecyclerView;

    private Contact curContact;
    private PhonesAdapter phonesAdapter;
    private EmailsAdapter emailsAdapter;


    public static DetailFragment create(Contact contact){
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, contact);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        curContact = bundle.getParcelable(BUNDLE_KEY);

        initUI();
        initAdapters();
        phonesConnection();
        emailsConnection();
    }


    private void initUI() {
        nameContact = getActivity().findViewById(R.id.detail_name_text_view);
        nameContact.setText(curContact.getName());
        phonesRecyclerView = getActivity().findViewById(R.id.detail_recycler_view_phones);
        emailsRecyclerView = getActivity().findViewById(R.id.detail_recycler_view_emails);
    }


    private void initAdapters() {
        phonesAdapter = new PhonesAdapter(getContext());
        phonesAdapter.setClickListenerOnCall(this);
        phonesAdapter.setClickListenerOnSms(this);
        phonesRecyclerView.setAdapter(phonesAdapter);
        LinearLayoutManager phonesLayoutManager = new LinearLayoutManager(getContext());
        phonesRecyclerView.setLayoutManager(phonesLayoutManager);

        emailsAdapter = new EmailsAdapter(getContext());
        emailsAdapter.setClickListenerOnMail(this);
        emailsRecyclerView.setAdapter(emailsAdapter);
        LinearLayoutManager emailsLayoutManager = new LinearLayoutManager(getContext());
        emailsRecyclerView.setLayoutManager(emailsLayoutManager);
    }


    private void phonesConnection() {
        List<String> phones = curContact.getPhones();
        phonesAdapter.setData(phones);
    }


    private void emailsConnection() {
        List<String> emails = curContact.getEmails();
        emailsAdapter.setData(emails);
    }


    @Override
    public void onClickCall(String phone) {
        String destination = "tel:" + phone;
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(destination));
        startActivity(callIntent);
    }


    @Override
    public void onClickSms(String phone) {
        String destination = "sms:" + phone;
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
        startActivity(smsIntent);
    }


    @Override
    public void onClickMail(String email) {
        String destination = "mailto:" + email;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(destination));
        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }


}
