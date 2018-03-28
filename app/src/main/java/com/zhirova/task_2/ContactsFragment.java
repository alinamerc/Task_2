package com.zhirova.task_2;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhirova.task_2.adapter.ContactsAdapter;
import com.zhirova.task_2.databinding.ContactsFragmentBinding;
import com.zhirova.task_2.model.Contact;
import com.zhirova.task_2.model.ContactReaderTask;

import java.util.Collections;
import java.util.List;


public class ContactsFragment extends Fragment implements ContactsAdapter.ClickListener{

    private final String TAG = "CONTACTS_FRAGMENT";
    private ContactReaderTask contactReaderTask;

    public ContactsFragmentBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DataBindingUtil.setContentView(getActivity(), R.layout.contacts_fragment);
    }


    @Override
    public void onResume() {
        super.onResume();
        contactReaderTask = new ContactReaderTask(this);
        contactReaderTask.execute();
    }


    @Override
    public void onPause() {
        super.onPause();
        contactReaderTask.cancel(true);
    }


    public void dataBinding(List<Contact> contacts){
        if (contacts.size() == 0) {
            binding.infoTextView.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.INVISIBLE);

        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.recyclerView.setLayoutManager(layoutManager);

            ContactsAdapter adapter = new ContactsAdapter(getContext());
            adapter.setClickListener(this);
            binding.recyclerView.setAdapter(adapter);
            Collections.sort(contacts, (contact1, contact2) -> contact1.getName().compareToIgnoreCase(contact2.getName()));
            adapter.setData(contacts);
        }
    }


    @Override
    public void onClick(Contact contact) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DetailFragment curFragment = new DetailFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, curFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
