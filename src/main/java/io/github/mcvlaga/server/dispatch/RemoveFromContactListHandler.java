package io.github.mcvlaga.server.dispatch;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import io.github.mcvlaga.server.ContactDatabase;
import io.github.mcvlaga.shared.dispatch.RemoveFromContactListAction;
import io.github.mcvlaga.shared.dispatch.RemoveFromContactListResult;

/**
 * Created by lol on 23/01/2016.
 */
public class RemoveFromContactListHandler implements ActionHandler<RemoveFromContactListAction, RemoveFromContactListResult> {

    private ContactDatabase database;

    @Inject
    RemoveFromContactListHandler(ContactDatabase database) {
        this.database = database;
    }

    @Override
    public RemoveFromContactListResult execute(RemoveFromContactListAction removeFromContactListAction, ExecutionContext executionContext)
            throws ActionException {
        int index = database.remove(removeFromContactListAction.getName());
        return new RemoveFromContactListResult(index);
    }

    @Override
    public Class<RemoveFromContactListAction> getActionType() {
        return RemoveFromContactListAction.class;
    }

    @Override
    public void undo(RemoveFromContactListAction removeFromContactListAction, RemoveFromContactListResult removeFromContactListResult, ExecutionContext executionContext)
            throws ActionException {

    }
}
