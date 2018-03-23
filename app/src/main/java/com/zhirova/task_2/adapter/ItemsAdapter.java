package com.zhirova.task_2.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhirova.task_2.R;
import com.zhirova.task_2.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<Contact> contacts = new ArrayList<>();
    private ClickListener clickListener;


    public ItemsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    public void setData(List<Contact> persons){
        this.contacts = persons;
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
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contacts_list_item, parent, false);
        ItemsViewHolder holder = new ItemsViewHolder(view);

        holder.itemView.setOnClickListener(v -> {
            if(clickListener != null){
                clickListener.onClick((Contact)v.getTag());
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Contact curContact = contacts.get(position);
        holder.itemView.setTag(curContact);

        holder.personId.setText(String.format(Locale.ENGLISH, "%d) ", position + 1));
        holder.personName.setText(curContact.getName());
        //holder.personPhone.setText(curContact.getPhone());
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }


     static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private TextView personId;
        private TextView personName;
        private TextView personPhone;

        ItemsViewHolder(View view){
            super(view);
//            personId = view.findViewById(R.id.category_id_text_view);
//            personName = view.findViewById(R.id.category_name_text_view);
//            personPhone = view.findViewById(R.id.category_phone_text_view);
//            personCircle = view.findViewById(R.id.category_image_view);
        }
    }


    public interface ClickListener{
        void onClick(Contact contact);
    }


}
