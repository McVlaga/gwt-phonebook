package io.github.mcvlaga.client.application.contact;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * Created by lol on 25/01/2016.
 */
public interface ContactCardUiHandlers extends UiHandlers {
    void removeContact();
    void changeContact(String newName, String newNumber);
}
