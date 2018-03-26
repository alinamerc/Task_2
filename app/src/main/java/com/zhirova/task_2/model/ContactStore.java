package com.zhirova.task_2.model;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactStore {

    private final String TAG = "CONTACT_STORE";
    private static ContactStore INSTANCE;
    private List<Contact> contacts = new ArrayList<>();


    public static ContactStore getInstance(Activity activity) {
        if (INSTANCE == null) {
            INSTANCE = new ContactStore(activity);
        }
        return INSTANCE;
    }


    public List<Contact> getContacts() {
        return contacts;
    }


    public void print() {
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


    private ContactStore(Activity activity) {
        Cursor contactsCursor = null;
        try {
            final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;
            final Uri URI_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            final Uri URI_MAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

            ContentResolver contentResolver = activity.getContentResolver();
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
                            e.printStackTrace();
                        } finally {
                            phoneCursor.close();
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
                        e.printStackTrace();
                    } finally {
                        emailCursor.close();
                    }
                    contacts.add(newContact);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            contactsCursor.close();
        }
    }


}
