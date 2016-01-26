package io.github.mcvlaga.server.dispatch;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import io.github.mcvlaga.server.ContactDatabase;
import io.github.mcvlaga.shared.dispatch.AddToContactListAction;
import io.github.mcvlaga.shared.dispatch.AddToContactListResult;

/**
 * Created by lol on 23/01/2016.
 */
public class AddToContactListHandler implements ActionHandler<AddToContactListAction, AddToContactListResult> {

    private ContactDatabase database;

    @Inject
    AddToContactListHandler(ContactDatabase database) {
        this.database = database;
    }

    @Override
    public AddToContactListResult execute(AddToContactListAction addToContactListAction, ExecutionContext executionContext)
            throws ActionException {
        boolean success = database.add(addToContactListAction.getContact());
        return new AddToContactListResult(success);
    }

    @Override
    public Class<AddToContactListAction> getActionType() {
        return AddToContactListAction.class;
    }

    @Override
    public void undo(AddToContactListAction addToContactListAction, AddToContactListResult addToContactListResult, ExecutionContext executionContext)
            throws ActionException {

    }
}
