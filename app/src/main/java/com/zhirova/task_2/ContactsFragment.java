package com.zhirova.task_2;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhirova.task_2.adapter.ContactsAdapter;
import com.zhirova.task_2.databinding.ContactsFragmentBinding;
import com.zhirova.task_2.model.Contact;
import com.zhirova.task_2.model.ContactReaderTask;

import java.util.Collections;
import java.util.List;


public class ContactsFragment extends Fragment implements ContactsAdapter.ClickListener{

    private final String TAG = "CONTACTS_FRAGMENT";
    private ContactReaderTask contactReaderTask;

    private ContactsFragmentBinding binding;
    private List<Contact> contacts;
    private ContactsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.contacts_fragment, container, false);
        View view = binding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        initUI();
    }


    private void initUI() {
        if (contacts == null){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.infoTextView.setVisibility(View.GONE);
            binding.recyclerViewContacts.setVisibility(View.VISIBLE);
        }
        else {
            dataBinding(contacts);
        }
    }


    private void initAdapter() {
        adapter = new ContactsAdapter(getContext());
        adapter.setClickListener(this);
        binding.recyclerViewContacts.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewContacts.setLayoutManager(layoutManager);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (contacts == null){
            contactReaderTask = new ContactReaderTask(this);
            contactReaderTask.execute();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (contactReaderTask != null){
            contactReaderTask.cancel(true);
            contactReaderTask = null;
        }
    }


    public void dataBinding(List<Contact> contacts){
        binding.progressBar.setVisibility(View.GONE);
        this.contacts = contacts;
        contactReaderTask = null;
        if (contacts.size() == 0) {
            binding.infoTextView.setVisibility(View.VISIBLE);
            binding.recyclerViewContacts.setVisibility(View.GONE);
        } else {
            binding.infoTextView.setVisibility(View.GONE);
            binding.recyclerViewContacts.setVisibility(View.VISIBLE);
            Collections.sort(contacts, (contact1, contact2) -> contact1.getName().compareToIgnoreCase(contact2.getName()));
            adapter.setData(contacts);
        }
    }


    @Override
    public void onClick(Contact contact) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DetailFragment curFragment = DetailFragment.create(contact);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, curFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
