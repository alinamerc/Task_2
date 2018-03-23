package com.zhirova.task_2;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhirova.task_2.adapter.ContactsAdapter;
import com.zhirova.task_2.model.Contact;
import com.zhirova.task_2.model.ContactStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsFragment extends Fragment implements ContactsAdapter.ClickListener{

    private final String TAG = "CONTACTS_FRAGMENT";
    private ContactsAdapter adapter;
    private RecyclerView recyclerView;
    private ContactStore contactStore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recycler_view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerViewConnection();
    }


    private void recyclerViewConnection() {
        contactStore = ContactStore.getInstance(getActivity());
        List<Contact> contacts = contactStore.getContacts();

        contactStore.print();

        adapter = new ContactsAdapter(getContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        Collections.sort(contacts, (contact1, contact2) -> contact1.getName().compareToIgnoreCase(contact2.getName()));
        adapter.setData(contacts);
    }


    @Override
    public void onClick(Contact contact) {

    }


}
