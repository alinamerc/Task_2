package com.zhirova.task_2.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.zhirova.task_2.ContactsFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ContactReaderTask extends AsyncTask<Void, Void, List<Contact>> {

    private final String TAG = "CONTACT_READER_TASK";
    private final ContactsFragment fragment;


    public ContactReaderTask(ContactsFragment fragment){
        this.fragment = fragment;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected List<Contact> doInBackground(Void... values) {
        List<Contact> contacts = getContactsBindInfo();
        //print(contacts);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Log.e(TAG, "ERROR", e);
        }
        return contacts;
    }


    @Override
    protected void onPostExecute(List<Contact> result) {
        super.onPostExecute(result);
        fragment.dataBinding(result);
    }


    private List<Contact> getContactsBindInfo() {
        List<Contact> bindContactsInfo = new ArrayList<>();
        Map<String, String> contactsMap = getContacts();
        Map<String, List<String>> phonesMap = getPhones();
        Map<String, List<String>> emailsMap = getEmails();

        for (String contactId: contactsMap.keySet()) {
            Contact curContact = new Contact(contactId, contactsMap.get(contactId));
            curContact.setPhones(phonesMap.get(contactId));
            curContact.setEmails(emailsMap.get(contactId));
            bindContactsInfo.add(curContact);
        }
        return bindContactsInfo;
    }


    private Map<String, String> getContacts() {
        final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = null;
        Map<String, String> contactsMap = new HashMap<>();
        try {
            ContentResolver contentResolver = fragment.getActivity().getContentResolver();
            contactsCursor = contentResolver.query(URI_CONTACTS,
                    null,
                    null,
                    null,
                    null);
            if (contactsCursor != null) {
                int idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                while (contactsCursor.moveToNext()) {
                    String id = contactsCursor.getString(idIndex);
                    String name = contactsCursor.getString(nameIndex);
                    contactsMap.put(id, name);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            try{
                contactsCursor.close();
            } catch (Exception e){
                Log.e(TAG, "ERROR", e);
            }
        }
        return contactsMap;
    }


    private Map<String, List<String>> getPhones() {
        final Uri URI_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor phoneCursor = null;
        Map<String, List<String>> phoneMap = new HashMap<>();
        try {
            ContentResolver contentResolver = fragment.getActivity().getContentResolver();
            phoneCursor = contentResolver.query(URI_PHONE,
                    null,
                    null,
                    null,
                    null);

            if (phoneCursor != null) {
                int idIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (phoneCursor.moveToNext()) {
                    String id = phoneCursor.getString(idIndex);
                    String phone = phoneCursor.getString(phoneIndex);

                    List<String> phones = phoneMap.get(id);
                    if (phones == null){
                        List<String> allPhones = new ArrayList<>();
                        allPhones.add(phone);
                        phoneMap.put(id, allPhones);
                    } else {
                        phones.add(phone);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            try{
                phoneCursor.close();
            } catch (Exception e){
                Log.e(TAG, "ERROR", e);
            }
        }
        return phoneMap;
    }


    private Map<String, List<String>> getEmails(){
        final Uri URI_MAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        Cursor emailCursor = null;
        Map<String, List<String>> emailsMap = new HashMap<>();
        try {
            ContentResolver contentResolver = fragment.getActivity().getContentResolver();
            emailCursor = contentResolver.query(URI_MAIL,
                    null,
                    null,
                    null,
                    null);
            if (emailCursor != null) {
                int idIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

                while (emailCursor.moveToNext()) {
                    String id = emailCursor.getString(idIndex);
                    String email = emailCursor.getString(emailIndex);

                    List<String> emails = emailsMap.get(id);
                    if (emails == null){
                        List<String> allEmails = new ArrayList<>();
                        allEmails.add(email);
                        emailsMap.put(id, allEmails);
                    } else {
                        emails.add(email);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            try{
                emailCursor.close();
            } catch (Exception e){
                Log.e(TAG, "ERROR", e);
            }
        }
        return emailsMap;
    }


    public void print(List<Contact> contacts) {
        if (contacts != null) {
            for (int i = 0; i < contacts.size(); i++) {
                Log.d(TAG, "============================");
                Contact curContact = contacts.get(i);
                Log.d(TAG, "id = " + curContact.getId() +
                        ", name = " + curContact.getName());

                if (curContact.getPhones() != null) {
                    for (int j = 0; j < curContact.getPhones().size(); j++) {
                        Log.d(TAG, "phone: " + curContact.getPhones().get(j) + "\n");
                    }
                }

                if (curContact.getEmails() != null) {
                    for (int j = 0; j < curContact.getEmails().size(); j++) {
                        Log.d(TAG, "email: " + curContact.getEmails().get(j) + "\n");
                    }
                }
            }
        }
    }


}
