package io.github.mcvlaga.server;

import com.google.inject.Singleton;
import io.github.mcvlaga.shared.dispatch.Contact;

import java.util.LinkedList;
import java.util.List;


@Singleton
public class ContactDatabase {
    private List<Contact> contacts = new LinkedList<>();

    public Boolean add(Contact contact) {
        if (isPresent(contact.getName())) {
            return false;
        }
        contacts.add(contact);
        return true;
    }

    public Contact change(String name, String newName, String newNumber) {
        if (name.equals(newName) || !isPresent(newName)) {
            for (Contact currentContact : contacts) {
                if (currentContact.getName().equals(name)) {
                    currentContact.setName(newName);
                    currentContact.setNumber(newNumber);
                    return currentContact;
                }
            }
        }
        return null;
    }

    private Boolean isPresent(String name) {
        for (Contact contactCurrent : contacts) {
            if (contactCurrent.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Contact get(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public int remove(String name) {
        for (Contact contactCurrent : contacts) {
            if (contactCurrent.getName().equals(name)) {
                int index = contacts.indexOf(contactCurrent);
                contacts.remove(index);
                return index;
            }
        }
        return -1;
    }

    public List<Contact> getMatching(String substring) {
        if (substring.length() == 0) {
            return contacts;
        }

        List<Contact> result = new LinkedList<>();

        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(substring.toLowerCase())){
                result.add(contact);
            }
        }

        if (result.size() > 0) {
            return result;
        }

        return null;
    }
}
