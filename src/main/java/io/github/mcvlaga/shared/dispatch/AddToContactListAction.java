package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Action;

/**
 * Created by lol on 23/01/2016.
 */
public class AddToContactListAction implements Action<AddToContactListResult> {

    private Contact contact;

    public AddToContactListAction (Contact contact) {
        this.contact = contact;
    }

    protected AddToContactListAction() {
        // Possibly for serialization.
    }

    public Contact getContact() {
        return contact;
    }

    @Override
    public String getServiceName() {
        return Action.DEFAULT_SERVICE_NAME + "AddToContactList";
    }

    @Override
    public boolean isSecured() {
        return false;
    }
}
