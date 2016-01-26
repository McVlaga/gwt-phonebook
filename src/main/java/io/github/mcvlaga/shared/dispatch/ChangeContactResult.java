package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

/**
 * Created by lol on 24/01/2016.
 */
public class ChangeContactResult implements Result {

    private Contact contact;

    public ChangeContactResult(Contact contact) {
        this.contact = contact;
    }

    protected ChangeContactResult() {
        // Possibly for serialization.
    }

    public Contact getContact() {
        return contact;
    }
}
