package com.zhirova.task_2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhirova.task_2.R;
import com.zhirova.task_2.model.Contact;

import java.util.ArrayList;
import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private final LayoutInflater inflater;
    private List<Contact> contacts = new ArrayList<>();
    private ClickListener clickListener;


    public ContactsAdapter (Context context) {
        this.inflater = LayoutInflater.from(context);
    }


    public void setData(List<Contact> contacts){
        this.contacts = contacts;
        //notifyDataSetChanged();
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public int positionById(String id){
        for(int i = 0; i < contacts.size(); i++){
            if (contacts.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contacts_list_item, parent, false);
        ContactsViewHolder holder = new ContactsViewHolder(view);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null){
                clickListener.onClick((Contact)v.getTag());
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Contact curContact = contacts.get(position);
        holder.itemView.setTag(curContact);
        holder.contactName.setText(curContact.getName());
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }


    static class ContactsViewHolder extends RecyclerView.ViewHolder {
        private TextView contactName;

        ContactsViewHolder(View view){
            super(view);
            contactName = view.findViewById(R.id.contact_list_text_view);
        }
    }


    public interface ClickListener{
        void onClick(Contact person);
    }

}
