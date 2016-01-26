package io.github.mcvlaga.server.dispatch;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import io.github.mcvlaga.server.ContactDatabase;
import io.github.mcvlaga.shared.dispatch.Contact;
import io.github.mcvlaga.shared.dispatch.GetContactListAction;
import io.github.mcvlaga.shared.dispatch.GetContactListResult;

import java.util.List;

/**
 * Created by lol on 23/01/2016.
 */
public class GetContactListHandler implements ActionHandler<GetContactListAction, GetContactListResult> {

    private final ContactDatabase database;

    @Inject
    GetContactListHandler(ContactDatabase database) {
        this.database = database;
    }

    @Override
    public GetContactListResult execute(GetContactListAction getContactListAction, ExecutionContext executionContext)
            throws ActionException {
        List<Contact> contacts = database.getMatching(getContactListAction.getSubstring());
        return new GetContactListResult(contacts);
    }

    @Override
    public Class<GetContactListAction> getActionType() {
        return GetContactListAction.class;
    }

    @Override
    public void undo(GetContactListAction getContactListAction, GetContactListResult getContactListResult, ExecutionContext executionContext)
            throws ActionException {

    }
}
