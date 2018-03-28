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


public class ContactReaderTask extends AsyncTask<Void, Integer, List<Contact>> {

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
            int counter = 0;
            for (int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(0);
                publishProgress(++counter);
                if (isCancelled()) { return null; }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "ERROR", e);
        }
        return contacts;
    }


    @Override
    protected void onPostExecute(List<Contact> result) {
        super.onPostExecute(result);
        if (result != null) {
            fragment.dataBinding(result);
        }
        fragment.binding.progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        fragment.binding.progressBar.setProgress(values[0]);
    }


    private List<Contact> getContactsBindInfo() {
        List<Contact> bindContactsInfo = new ArrayList<>();
        Map<String, String> contacts = getContacts();
        Map<String, List<String>> phones = getPhones();
        Map<String, List<String>> emails = getEmails();

        for (String contactId: contacts.keySet()) {
            Contact curContact = new Contact(contactId, contacts.get(contactId), null, null);
            for (String contactIdInPhones: phones.keySet()) {
                if (contactId.equals(contactIdInPhones)) {
                    curContact.setPhone(phones.get(contactIdInPhones));
                }
            }

            for (String contactIdInEmails: emails.keySet()) {
                if (contactId.equals(contactIdInEmails)) {
                    curContact.setEmail(emails.get(contactIdInEmails));
                }
            }
            bindContactsInfo.add(curContact);
        }
        return bindContactsInfo;
    }


    private Map<String, String> getContacts() {
        final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = null;
        Map<String, String> contacts = new HashMap<>();
        try {
            ContentResolver contentResolver = fragment.getActivity().getContentResolver();
            contactsCursor = contentResolver.query(URI_CONTACTS,
                    null,
                    null,
                    null,
                    null);
            if (contactsCursor != null & contactsCursor.getCount() > 0) {
                int idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                while (contactsCursor.moveToNext()) {
                    String id = contactsCursor.getString(idIndex);
                    String name = contactsCursor.getString(nameIndex);
                    contacts.put(id, name);
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
        return contacts;
    }


    private Map<String, List<String>> getPhones() {
        final Uri URI_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor phoneCursor = null;
        Map<String, List<String>> phones = new HashMap<>();
        try {
            ContentResolver contentResolver = fragment.getActivity().getContentResolver();
            phoneCursor = contentResolver.query(URI_PHONE,
                    null,
                    null,
                    null,
                    null);

            if (phoneCursor != null & phoneCursor.getCount() > 0) {
                int idIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (phoneCursor.moveToNext()) {
                    String id = phoneCursor.getString(idIndex);
                    String phone = phoneCursor.getString(phoneIndex);

                    boolean isExistContact = false;
                    for (String contactId: phones.keySet()) {
                        if (contactId.equals(id)) {
                            isExistContact = true;
                            break;
                        }
                    }

                    if (isExistContact) {
                        phones.get(id).add(phone);
                    }
                    else {
                        List<String> allPhones = new ArrayList<>();
                        allPhones.add(phone);
                        phones.put(id, allPhones);
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
        return phones;
    }


    private Map<String, List<String>> getEmails(){
        final Uri URI_MAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        Cursor emailCursor = null;
        Map<String, List<String>> emails = new HashMap<>();
        try {
            ContentResolver contentResolver = fragment.getActivity().getContentResolver();
            emailCursor = contentResolver.query(URI_MAIL,
                    null,
                    null,
                    null,
                    null);
            if (emailCursor != null & emailCursor.getCount() > 0) {
                int idIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

                while (emailCursor.moveToNext()) {
                    String id = emailCursor.getString(idIndex);
                    String email = emailCursor.getString(emailIndex);

                    boolean isExistContact = false;
                    for (String contactId: emails.keySet()) {
                        if (contactId.equals(id)) {
                            isExistContact = true;
                            break;
                        }
                    }

                    if (isExistContact) {
                        emails.get(id).add(email);
                    }
                    else {
                        List<String> allEmails = new ArrayList<>();
                        allEmails.add(email);
                        emails.put(id, allEmails);
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
        return emails;
    }


    public void print(List<Contact> contacts) {
        if (contacts != null) {
            for (int i = 0; i < contacts.size(); i++) {
                Log.d(TAG, "============================");
                Contact curContact = contacts.get(i);
                Log.d(TAG, "id = " + curContact.getId() +
                        ", name = " + curContact.getName());

                if (curContact.getPhone() != null) {
                    for (int j = 0; j < curContact.getPhone().size(); j++) {
                        Log.d(TAG, "phone: " + curContact.getPhone().get(j) + "\n");
                    }
                }

                if (curContact.getEmail() != null) {
                    for (int j = 0; j < curContact.getEmail().size(); j++) {
                        Log.d(TAG, "email: " + curContact.getEmail().get(j) + "\n");
                    }
                }
            }
        }
    }


}
