package io.github.mcvlaga.client.application;

import com.gwtplatform.mvp.client.UiHandlers;
import io.github.mcvlaga.shared.dispatch.Contact;

/**
 * Created by lol on 23/01/2016.
 */
public interface ContactsUiHandlers extends UiHandlers {
    void addContact(Contact contact);
    void removeContact(String name, int index);
    void getContactList(String substring);
    void changeContact(String name, String newName, String newNumber, int index);
}
