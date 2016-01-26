package io.github.mcvlaga.server.dispatch;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import io.github.mcvlaga.server.ContactDatabase;
import io.github.mcvlaga.shared.dispatch.ChangeContactAction;
import io.github.mcvlaga.shared.dispatch.ChangeContactResult;
import io.github.mcvlaga.shared.dispatch.Contact;

/**
 * Created by lol on 24/01/2016.
 */
public class ChangeContactHandler implements ActionHandler<ChangeContactAction, ChangeContactResult> {

    private ContactDatabase database;

    @Inject
    ChangeContactHandler(ContactDatabase database) {
        this.database = database;
    }

    @Override
    public ChangeContactResult execute(ChangeContactAction changeContactAction,
                                       ExecutionContext executionContext) throws ActionException {
        Contact contact = database.change(changeContactAction.getName(),
                changeContactAction.getNewName(), changeContactAction.getNewNumber());

        return new ChangeContactResult(contact);
    }

    @Override
    public Class<ChangeContactAction> getActionType() {
        return ChangeContactAction.class;
    }

    @Override
    public void undo(ChangeContactAction changeContactAction, ChangeContactResult changeContactResult, ExecutionContext executionContext) throws ActionException {

    }
}
