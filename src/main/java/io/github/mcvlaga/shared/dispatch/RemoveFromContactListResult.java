package io.github.mcvlaga.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;

/**
 * Created by lol on 23/01/2016.
 */
public class RemoveFromContactListResult implements Result {

    private int index;

    public RemoveFromContactListResult(int index) {
        this.index = index;
    }

    protected RemoveFromContactListResult() {
        // Possibly for serialization.
    }

    public int getIndex() {
        return index;
    }
}
