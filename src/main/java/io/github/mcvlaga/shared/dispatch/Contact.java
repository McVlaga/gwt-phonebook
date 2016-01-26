package io.github.mcvlaga.shared.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by lol on 23/01/2016.
 */

public class Contact implements IsSerializable {

    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    Contact() {
        // For serialization only
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
