package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Action;

/**
 * Created by lol on 23/01/2016.
 */
public class RemoveFromContactListAction implements Action<RemoveFromContactListResult> {

    private String name;

    public RemoveFromContactListAction (String name) {
        this.name = name;
    }

    protected RemoveFromContactListAction() {
        // Possibly for serialization.
    }

    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return Action.DEFAULT_SERVICE_NAME + "RemoveFromContactList";
    }

    @Override
    public boolean isSecured() {
        return false;
    }
}
