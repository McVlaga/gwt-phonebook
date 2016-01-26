package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Action;

/**
 * Created by lol on 23/01/2016.
 */
public class GetContactAction implements Action<GetContactResult> {
    private String name;

    public GetContactAction(String name) {
        this.name = name;
    }

    protected GetContactAction() {
        // Possibly for serialization.
    }

    @Override
    public String getServiceName() {
        return Action.DEFAULT_SERVICE_NAME + "GetProduct";
    }

    @Override
    public boolean isSecured() {
        return false;
    }

    public String getName() {
        return name;
    }
}
