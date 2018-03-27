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
import java.util.List;
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
        List<Contact> contacts = getContacts();
        try {
            int counter = 0;
            for (int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(1);
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
        fragment.dataBinding(result);
        fragment.progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        fragment.progressBar.setProgress(values[0]);
    }


    private List<Contact> getContacts() {
        Cursor contactsCursor = null;
        List<Contact> contacts = new ArrayList<>();
        try {
            final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;
            final Uri URI_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            final Uri URI_MAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

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
                    Contact newContact = new Contact(id, name, null, null);
                    int hasPhoneNumber = contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

                    if (hasPhoneNumber > 0) {
                        Cursor phoneCursor = null;
                        try {
                            phoneCursor = contentResolver.query(URI_PHONE,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},
                                    null);

                            if (phoneCursor != null & phoneCursor.getCount() > 0) {
                                int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                List<String> allPhones = new ArrayList<>();

                                while (phoneCursor.moveToNext()) {
                                    String phone = phoneCursor.getString(phoneIndex);
                                    allPhones.add(phone);
                                    newContact.setPhone(allPhones);
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
                    }

                    Cursor emailCursor = null;
                    try {
                        emailCursor = contentResolver.query(URI_MAIL,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id},
                                null);

                        if (emailCursor != null & emailCursor.getCount() > 0) {
                            int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                            List<String> allEmailes = new ArrayList<>();

                            while (emailCursor.moveToNext()) {
                                String email = emailCursor.getString(emailIndex);
                                allEmailes.add(email);
                                newContact.setEmail(allEmailes);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR", e);
                    } finally {
                        emailCursor.close();
                    }
                    contacts.add(newContact);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            contactsCursor.close();
        }
        return contacts;
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
