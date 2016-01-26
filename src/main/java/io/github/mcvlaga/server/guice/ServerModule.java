package io.github.mcvlaga.server.guice;

/**
 * Created by lol on 23/01/2016.
 */

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import io.github.mcvlaga.server.dispatch.*;
import io.github.mcvlaga.shared.dispatch.*;

/**
 * Module which binds the handlers and configurations.
 */
public class ServerModule extends HandlerModule {
    @Override
    protected void configureHandlers() {
        bindHandler(GetContactAction.class, GetContactHandler.class);
        bindHandler(GetContactListAction.class, GetContactListHandler.class);
        bindHandler(AddToContactListAction.class, AddToContactListHandler.class);
        bindHandler(RemoveFromContactListAction.class, RemoveFromContactListHandler.class);
        bindHandler(ChangeContactAction.class, ChangeContactHandler.class);
    }
}
