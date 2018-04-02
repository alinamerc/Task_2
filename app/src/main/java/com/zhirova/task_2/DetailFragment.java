package com.zhirova.task_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhirova.task_2.adapter.EmailsAdapter;
import com.zhirova.task_2.adapter.PhonesAdapter;
import com.zhirova.task_2.model.Contact;

import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class DetailFragment extends Fragment implements PhonesAdapter.ClickListenerOnCall,
        PhonesAdapter.ClickListenerOnSms, EmailsAdapter.ClickListenerOnMail {

    private static final String BUNDLE_KEY = "CONTACT_ID";
    public final String TAG = "DETAIL_FRAGMENT";
    private final int PERMISSION_REQUEST_CALL_PHONE = 1;
    private final int PERMISSION_REQUEST_SEND_SMS = 2;

    private TextView nameContact;
    private RecyclerView phonesRecyclerView;
    private RecyclerView emailsRecyclerView;
    private PhonesAdapter phonesAdapter;
    private EmailsAdapter emailsAdapter;

    private Contact curContact;
    private String curPhone;


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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callingToContact();
            } else {
                Snackbar.make(getView(), R.string.call_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                writingSms();
            } else {
                Snackbar.make(getView(), R.string.sms_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
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
        curPhone = phone;
        if (checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            callingToContact();
        } else {
            requestCallPermission();
        }
    }


    public void callingToContact() {
        String destination = "tel:" + curPhone;
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(destination));
        startActivity(callIntent);
    }


    private void requestCallPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            Snackbar.make(getView(), R.string.call_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL_PHONE);
                }
            }).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CALL_PHONE);
        }
    }


    @Override
    public void onClickSms(String phone) {
        curPhone = phone;
        if (checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            writingSms();
        } else {
            requestSmsPermission();
        }
    }


    public void writingSms() {
        String destination = "sms:" + curPhone;
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
        startActivity(smsIntent);
    }


    private void requestSmsPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
            Snackbar.make(getView(), R.string.sms_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                            PERMISSION_REQUEST_SEND_SMS);
                }
            }).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_SEND_SMS);
        }
    }


    @Override
    public void onClickMail(String email) {
        String destination = "mailto:" + email;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(destination));
        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }


}
