package io.github.mcvlaga.client.application.contact;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import io.github.mcvlaga.client.place.NameTokens;
import io.github.mcvlaga.client.place.ParameterTokens;
import io.github.mcvlaga.shared.dispatch.*;

/**
 * Presenter страницы с карточкой контакта. Посылает запросы ContactDatabase
 * используя паттерн команда, и выводит результат в ContactCardView.
 */
public class ContactCardPresenter extends Presenter<ContactCardPresenter.MyView, ContactCardPresenter.MyProxy>
        implements ContactCardUiHandlers {

    @ProxyCodeSplit
    @NameToken(NameTokens.contact)
    public interface MyProxy extends ProxyPlace<ContactCardPresenter> {
    }

    public interface MyView extends View {
        void setBackLinkHistoryToken(String historyToken);
        void setContactDetails(Contact contact);
        void setMessage(String message);
        void setErrorMessage(String message);
        void setSuccessMessage(String message);
    }

    private final DispatchAsync dispatcher;
    private final PlaceManager placeManager;

    private String name;

    @Inject
    ContactCardPresenter(
            EventBus eventBus,
            ContactCardView view,
            MyProxy proxy,
            PlaceManager placeManager,
            DispatchAsync dispatcher) {
        super(eventBus, view, proxy, RevealType.Root);

        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        view.setUiHandlers(this);
    }

    @Override
    public void removeContact() {
        dispatcher.execute(new RemoveFromContactListAction(name), new AsyncCallback<RemoveFromContactListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setMessage("Unknown contact");
            }

            @Override
            public void onSuccess(RemoveFromContactListResult removeFromContactListResult) {
                getView().setMessage(name + " deleted");
            }
        });
    }

    @Override
    public void changeContact(final String newName, final String newNumber) {
        dispatcher.execute(new ChangeContactAction(name, newName, newNumber), new AsyncCallback<ChangeContactResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setMessage("Error: failed to edit the contact");
            }

            @Override
            public void onSuccess(ChangeContactResult changeContactResult) {
                if (changeContactResult.getContact() == null) {
                    getView().setErrorMessage("Error: there is a contact with name "
                            + newName + " already");
                    return;
                }
                name = newName;
                getView().setContactDetails(new Contact(newName, newNumber));
                getView().setSuccessMessage("The contact was saved");
            }
        });
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        name = request.getParameter(ParameterTokens.TOKEN_ID, null).replace('_',' ');
    }

    @Override
    protected void onReset() {
        super.onReset();

        getView().setMessage("Loading...");
        getView().setSuccessMessage("");

        // Отправляет виду предыдущую ссылку для возможности возвращения назад.
        getView().setBackLinkHistoryToken(placeManager.buildRelativeHistoryToken(-1));

        dispatcher.execute(new GetContactAction(name), new AsyncCallback<GetContactResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setMessage("Unknown contact");
            }

            @Override
            public void onSuccess(GetContactResult getContactResult) {
                getView().setContactDetails(getContactResult.getContact());
            }
        });
    }
}
