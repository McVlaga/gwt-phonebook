package io.github.mcvlaga.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import io.github.mcvlaga.client.application.contact.ContactCardPresenter;
import io.github.mcvlaga.client.application.contact.ContactCardView;

public class ContactsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ContactsPresenter.class, ContactsPresenter.MyView.class, ContactsView.class,
                ContactsPresenter.MyProxy.class);
        bindPresenter(ContactCardPresenter.class, ContactCardPresenter.MyView.class, ContactCardView.class,
                ContactCardPresenter.MyProxy.class);
    }
}
