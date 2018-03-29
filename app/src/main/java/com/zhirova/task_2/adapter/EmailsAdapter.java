package com.zhirova.task_2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhirova.task_2.R;

import java.util.ArrayList;
import java.util.List;


public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailsViewHolder> {

    private final LayoutInflater inflater;
    private List<String> emails = new ArrayList<>();
    private ClickListenerOnMail clickListenerMail;


    public EmailsAdapter (Context context) {
        this.inflater = LayoutInflater.from(context);
    }


    public void setData(List<String> emails){
        this.emails = emails;
    }


    public void setClickListenerOnMail(ClickListenerOnMail clickListenerMail) {
        this.clickListenerMail = clickListenerMail;
    }


    @Override
    public EmailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.emails_list_item, parent, false);
        EmailsViewHolder holder = new EmailsViewHolder(view);

        holder.emailButton.setOnClickListener(v -> {
            if (clickListenerMail != null){
                clickListenerMail.onClickMail((String) v.getTag());
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(EmailsViewHolder holder, int position) {
        String curEmail = emails.get(position);
        holder.itemView.setTag(curEmail);
        holder.emailText.setText(curEmail);
    }


    @Override
    public int getItemCount() {
        return emails.size();
    }


    static class EmailsViewHolder extends RecyclerView.ViewHolder {
        private TextView emailText;
        private ImageButton emailButton;

        EmailsViewHolder(View view){
            super(view);
            emailText = view.findViewById(R.id.detail_email_text_view);
            emailButton = view.findViewById(R.id.detail_email_image_button);
        }
    }


    public interface ClickListenerOnMail{
        void onClickMail(String email);
    }


}
