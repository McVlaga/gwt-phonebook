package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

/**
 * Created by lol on 23/01/2016.
 */

public class GetContactResult implements Result {

    private Contact contact;

    public GetContactResult(Contact contact) {
        this.contact = contact;
    }

    protected GetContactResult() {
        // Possibly for serialization.
    }

    public Contact getContact() {
        return contact;
    }
}
