package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

/**
 * Created by lol on 23/01/2016.
 */
public class AddToContactListResult implements Result {

    boolean success;

    public AddToContactListResult(Boolean success) {
        this.success = success;
    }

    protected AddToContactListResult() {
        // Possibly for serialization.
    }

    public Boolean isSuccess() {
        return success;
    }
}
