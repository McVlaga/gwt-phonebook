package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Action;

/**
 * Created by lol on 23/01/2016.
 */
public class GetContactListAction implements Action<GetContactListResult> {

    private String substring;

    public GetContactListAction (String substring) {
        this.substring = substring;
    }

    protected GetContactListAction() {
        // Possibly for serialization.
    }

    public String getSubstring() {
        return substring;
    }

    @Override
    public String getServiceName() {
        return Action.DEFAULT_SERVICE_NAME + "GetContactList";
    }

    @Override
    public boolean isSecured() {
        return false;
    }
}
