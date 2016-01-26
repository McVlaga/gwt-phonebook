package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Action;

/**
 * Created by lol on 24/01/2016.
 */
public class ChangeContactAction implements Action<ChangeContactResult> {

    private String name;
    private String newName;
    private String newNumber;

    public ChangeContactAction (String name, String newName, String newNumber) {
        this.name = name;
        this.newName = newName;
        this.newNumber = newNumber;
    }

    protected ChangeContactAction() {
        // Possibly for serialization.
    }

    public String getName() {
        return name;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewNumber() {
        return newNumber;
    }

    @Override
    public String getServiceName() {
        return Action.DEFAULT_SERVICE_NAME + "ChangeContact";
    }

    @Override
    public boolean isSecured() {
        return false;
    }
}
