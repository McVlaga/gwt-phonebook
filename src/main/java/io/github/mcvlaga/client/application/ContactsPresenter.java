package io.github.mcvlaga.client.application;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import io.github.mcvlaga.client.place.NameTokens;
import io.github.mcvlaga.client.place.ParameterTokens;
import io.github.mcvlaga.shared.dispatch.*;

import java.util.List;

/**
 * Presenter главной страницы со списком контактов. Посылает запросы ContactDatabase
 * используя паттерн команда, и выводит результат в ContactsView.
 */
public class ContactsPresenter extends Presenter<ContactsPresenter.MyView, ContactsPresenter.MyProxy>
        implements ContactsUiHandlers {

    @ProxyStandard
    @NameToken(NameTokens.homePage)
    interface MyProxy extends ProxyPlace<ContactsPresenter> {
    }

    interface MyView extends View {
        void copyAllToFlexTable(List<Contact> contactList);
        void addRowToFlexTable(Contact contact);
        void removeRowFromFlexTable(int index);
        void changeRowOfFlexTable(Contact newContact, int row);
        void emptyAddTextBoxes();
        void setErrorMessage(String message);
        void setSuccessMessage(String message);
        void setSearchString(String searchString);
        String getSearchString();
    }

    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> SLOT_MAIN = new GwtEvent.Type<>();

    private final DispatchAsync dispatcher;
    private final PlaceManager placeManager;

    @Inject
    ContactsPresenter(
            EventBus eventBus,
            ContactsView view,
            MyProxy proxy,
            PlaceManager placeManager,
            DispatchAsync dispatcher) {
        super(eventBus, view, proxy, RevealType.Root);

        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        view.setUiHandlers(this);
    }

    @Override
    public void onBind() {
    }

    @Override
    public void addContact(final Contact newContact) {
        dispatcher.execute(new AddToContactListAction(newContact), new AsyncCallback<AddToContactListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setErrorMessage("Error: failed to add");
            }

            @Override
            public void onSuccess(AddToContactListResult addToContactListResult) {

                getView().emptyAddTextBoxes();

                if (!addToContactListResult.isSuccess()) {
                    getView().setErrorMessage("Error: there is a contact with name "
                            + newContact.getName() + " already");
                    return;
                }

                // Если поисковая строка пуста - добавить контакт в FlexTable сразу
                if (getView().getSearchString().length() == 0) {
                    getView().addRowToFlexTable(newContact);
                }
                else {
                    // Иначе - запросить список контактов и обновить FlexTable
                    getContactList(getView().getSearchString());
                }
                getView().setSuccessMessage(newContact.getName() + " was added");
            }
        });
    }

    @Override
    public void changeContact(final String name, final String newName, final String newNumber, final int index) {
        dispatcher.execute(new ChangeContactAction(name, newName, newNumber), new AsyncCallback<ChangeContactResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setErrorMessage("Error: failed to edit the contact");
            }

            @Override
            public void onSuccess(ChangeContactResult changeContactResult) {

                if (changeContactResult.getContact() == null) {
                    getView().setErrorMessage("Error: there is a contact with name "
                            + newName + " already");
                    return;
                }

                getView().changeRowOfFlexTable(new Contact(newName, newNumber), index);
                getView().setSuccessMessage("The contact was saved");
            }
        });
    }

    @Override
    public void removeContact(final String name, final int index) {
        dispatcher.execute(new RemoveFromContactListAction(name), new AsyncCallback<RemoveFromContactListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setErrorMessage(caught.getMessage());
            }

            @Override
            public void onSuccess(RemoveFromContactListResult removeFromContactListResult) {

                if (removeFromContactListResult.getIndex() < 0) {
                    getView().setErrorMessage("Error: contact " + name + " not found");
                    return;
                }

                getView().removeRowFromFlexTable(index);
                getView().setSuccessMessage(name + " was removed");
            }
        });
    }

    /**
     * Запрашивает список контактов, в которых встречается строка substring,
     * и вставляет список в таблицу вида.
     * Получает все контакты если substring пуста.
     */
    @Override
    public void getContactList(String substring) {
        dispatcher.execute(new GetContactListAction(substring), new AsyncCallback<GetContactListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setErrorMessage("Error: failed to get the contact list");
            }

            @Override
            public void onSuccess(GetContactListResult getContactListResult) {
                getView().copyAllToFlexTable(getContactListResult.getContacts());
            }
        });
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Выводит контакты согласно поисковой строке, взятой из URL страницы
        PlaceRequest currentRequest = placeManager.getCurrentPlaceRequest();
        String searchString = currentRequest.getParameter(ParameterTokens.SEARCH_STRING, "");
        getView().setSearchString(searchString);
        getContactList(searchString);
    }
}
