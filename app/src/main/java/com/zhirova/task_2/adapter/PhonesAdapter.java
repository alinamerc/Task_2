package com.zhirova.task_2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.zhirova.task_2.R;

import java.util.ArrayList;
import java.util.List;


public class PhonesAdapter extends RecyclerView.Adapter<PhonesAdapter.PhonesViewHolder> {

    private final LayoutInflater inflater;
    private List<String> phones = new ArrayList<>();
    private PhonesAdapter.ClickListenerOnCall clickListenerCall;
    private PhonesAdapter.ClickListenerOnSms clickListenerSms;


    public PhonesAdapter (Context context) {
        this.inflater = LayoutInflater.from(context);
    }


    public void setData(List<String> phones){
        this.phones = phones;
    }


    public void setClickListenerOnCall(PhonesAdapter.ClickListenerOnCall clickListenerCall) {
        this.clickListenerCall = clickListenerCall;
    }


    public void setClickListenerOnSms(PhonesAdapter.ClickListenerOnSms clickListenerSms) {
        this.clickListenerSms = clickListenerSms;
    }


    @Override
    public PhonesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.phones_list_item, parent, false);
        PhonesViewHolder holder = new PhonesViewHolder(view);

        holder.callButton.setOnClickListener(v -> {
            if (clickListenerCall != null){
                clickListenerCall.onClickCall((String) v.getTag());
            }
        });

        holder.smsButton.setOnClickListener(v -> {
            if (clickListenerSms != null){
                clickListenerSms.onClickSms((String) v.getTag());
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(PhonesViewHolder holder, int position) {
        String curPhone = phones.get(position);
        holder.itemView.setTag(curPhone);
        holder.phoneText.setText(curPhone);
    }


    @Override
    public int getItemCount() {
        return phones.size();
    }


    static class PhonesViewHolder extends RecyclerView.ViewHolder {
        private TextView phoneText;
        private ImageButton callButton;
        private ImageButton smsButton;

        PhonesViewHolder(View view){
            super(view);
            phoneText = view.findViewById(R.id.detail_phone_text_view);
            callButton = view.findViewById(R.id.detail_call_image_button);
            smsButton = view.findViewById(R.id.detail_sms_image_button);
        }
    }


    public interface ClickListenerOnCall{
        void onClickCall(String phone);
    }


    public interface ClickListenerOnSms{
        void onClickSms(String phone);
    }


}
