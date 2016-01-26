package io.github.mcvlaga.server.dispatch;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import io.github.mcvlaga.server.ContactDatabase;
import io.github.mcvlaga.shared.dispatch.Contact;
import io.github.mcvlaga.shared.dispatch.GetContactAction;
import io.github.mcvlaga.shared.dispatch.GetContactResult;

/**
 * Created by lol on 23/01/2016.
 */
public class GetContactHandler implements ActionHandler<GetContactAction, GetContactResult> {

    private final ContactDatabase database;

    @Inject
    GetContactHandler(ContactDatabase database) {
        this.database = database;
    }

    @Override
    public GetContactResult execute(GetContactAction getContactAction, ExecutionContext executionContext)
            throws ActionException {
        Contact contact = database.get(getContactAction.getName());

        if (contact == null) {
            throw new ActionException("Contact not found");
        }

        return new GetContactResult(contact);
    }

    @Override
    public Class<GetContactAction> getActionType() {
        return GetContactAction.class;
    }

    @Override
    public void undo(GetContactAction getContactAction, GetContactResult getContactResult, ExecutionContext executionContext)
            throws ActionException {

    }
}
