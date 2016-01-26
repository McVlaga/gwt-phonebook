package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lol on 23/01/2016.
 */
public class GetContactListResult implements Result {
    private List<Contact> contacts;

    public GetContactListResult(List<Contact> products) {
        this.contacts = products;
    }

    protected GetContactListResult() {
        // Possibly for serialization.
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
