package io.github.mcvlaga.client.application;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import io.github.mcvlaga.client.application.validation.UiValidation;
import io.github.mcvlaga.client.application.validation.UiValidationResult;
import io.github.mcvlaga.client.place.NameTokens;
import io.github.mcvlaga.client.place.ParameterTokens;
import io.github.mcvlaga.client.resources.AppResources;
import io.github.mcvlaga.shared.dispatch.Contact;

import java.util.List;

/**
 * View главной страницы со списком контактов.
 */
public class ContactsView extends ViewWithUiHandlers<ContactsUiHandlers>
        implements ContactsPresenter.MyView {

    interface Binder extends UiBinder<Widget, ContactsView> {
    }

    /**
     * Класс для индикации столбцов таблицы контактов.
     */
    private static class Column {
        public static final int NAME = 0;
        public static final int NUMBER = 1;
        public static final int TOOLS = 2;
    }

    @UiField
    FlexTable contactsFlexTable;
    @UiField
    TextBox nameTextBox;
    @UiField
    TextBox numberTextBox;
    @UiField
    Button addButton;
    @UiField
    TextBox searchTextBox;
    @UiField
    Label messageLabel;
    @UiField
    AppResources resources;

    private final PlaceManager placeManager;

    @Inject
    ContactsView(Binder uiBinder,
                 PlaceManager placeManager) {
        this.placeManager = placeManager;
        initWidget(uiBinder.createAndBindUi(this));
        nameTextBox.getElement().setPropertyString("placeholder", "Enter the name");
        numberTextBox.getElement().setPropertyString("placeholder", "Enter the number");
        searchTextBox.getElement().setPropertyString("placeholder", "Search by name...");

        // Добавляем handlers на нажатие кнопки Enter для добавления контакта.
        addEnterKeyHandlers();

        // Добавляем handler на ввод символов для поискового запроса,
        // чтобы результат появлялся во время печатания.
        addKeyHandlerForSearch();
    }

    @UiHandler("addButton")
    public void onAddButtonClicked(ClickEvent e) {
        String name = nameTextBox.getText();
        String number = numberTextBox.getText();

        // Проверяем формат введенных данных.
        UiValidationResult result = UiValidation.validate(name, number);
        if (!result.isValid()) {
            setErrorMessage(result.getMessage());
            return;
        }

        setErrorMessage("");

        // Запрос презентеру на добавление контакта.
        getUiHandlers().addContact(new Contact(name, number));
    }

    private void onEditButtonClicked(int rowIndex, Contact contact) {

        // Меняем ссылку имени (Anchor) на TextBox для изменения имени контакта.
        Anchor contactLink = (Anchor) contactsFlexTable.getWidget(rowIndex, Column.NAME);
        TextBox nameTextBox = new TextBox();
        nameTextBox.setText(contactLink.getHTML());
        nameTextBox.setName(contactLink.getHTML());
        contactsFlexTable.setWidget(rowIndex, Column.NAME, nameTextBox);

        // Меняем строку номера на TextBox для изменения номера контакта.
        TextBox numberTextBox = new TextBox();
        numberTextBox.setText(contactsFlexTable.getHTML(rowIndex, 1));
        contactsFlexTable.setWidget(rowIndex, Column.NUMBER, numberTextBox);

        // Заменяем кнопку "Edit" на кнопку "Save".
        HorizontalPanel toolsPanel = (HorizontalPanel) contactsFlexTable.getWidget(rowIndex, Column.TOOLS);
        toolsPanel.remove(0);
        toolsPanel.insert(getNewSaveButton(contact), 0);
    }

    private void onSaveButtonClicked(int rowIndex, Contact contact) {
        String newName = ((TextBox) contactsFlexTable.getWidget(rowIndex, Column.NAME)).getText();
        String newNumber = ((TextBox) contactsFlexTable.getWidget(rowIndex, Column.NUMBER)).getText();

        // Если введенные данные теже, то просто убираем редактирование этого ряда.
        if (newName.equals(contact.getName()) && newNumber.equals(contact.getNumber())) {
            changeRowOfFlexTable(contact, rowIndex);
            return;
        }

        // Проверяем формат введенных данных.
        UiValidationResult result = UiValidation.validate(newName, newNumber);
        if (!result.isValid()) {
            setErrorMessage(result.getMessage());
            return;
        }

        // Запрос презентеру на изменение контакта.
        getUiHandlers().changeContact(contact.getName(), newName, newNumber, rowIndex);
    }

    private void onDeleteButtonClicked(int rowIndex) {
        String name;

        // Если контакт редактируется в данный момент, то берем имя из TextBox'а.
        if (contactsFlexTable.getWidget(rowIndex, Column.NAME).getClass().equals(TextBox.class)) {
            name = ((TextBox) contactsFlexTable.getWidget(rowIndex, Column.NAME)).getName();
        }
        else {
            // Иначе, берем имя из ссылки.
            name = ((Anchor) contactsFlexTable.getWidget(rowIndex, Column.NAME)).getHTML();
        }

        // Запрос презентеру на удаление контакта.
        getUiHandlers().removeContact(name, rowIndex);
    }

    private void onContactLinkClicked(Contact contact) {
        String searchString = searchTextBox.getText();

        if (!searchString.equals("")){

            // Добавляем Search параметр в URL и, соответственно, в историю.
            PlaceRequest searchRequest = new PlaceRequest.Builder()
                    .nameToken(NameTokens.homePage)
                    .with(ParameterTokens.SEARCH_STRING, searchString.replace(' ','_'))
                    .build();
            placeManager.updateHistory(searchRequest, true);
        }

        // Добавляем параметр имени в URl.
        PlaceRequest contactRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.contact)
                .with(ParameterTokens.TOKEN_ID, contact.getName().replace(' ','_'))
                .build();

        placeManager.revealRelativePlace(contactRequest);
        setErrorMessage("");
    }

    @Override
    public void copyAllToFlexTable(List<Contact> contactList) {
        contactsFlexTable.removeAllRows();
        for (Contact contact : contactList) {
            addRowToFlexTable(contact);
        }
    }

    @Override
    public void addRowToFlexTable(Contact contact) {
        int numRows = contactsFlexTable.getRowCount();

        contactsFlexTable.setWidget(numRows, Column.NAME, getNewContactLink(contact));
        contactsFlexTable.setHTML(numRows, Column.NUMBER, contact.getNumber());
        contactsFlexTable.setWidget(numRows, Column.TOOLS, getNewHorizontalToolsPanel(contact));
    }

    @Override
    public void removeRowFromFlexTable(int index) {
        contactsFlexTable.removeRow(index);
    }

    /**
     * Обновляет ряд новыми данными и убирает редактирование.
     */
    @Override
    public void changeRowOfFlexTable(Contact newContact, int row) {

        // Запрашиваем ссылку с новым именем.
        contactsFlexTable.setWidget(row, Column.NAME, getNewContactLink(newContact));

        // Добавляем новый номер.
        contactsFlexTable.setHTML(row, Column.NUMBER, newContact.getNumber());

        // Заменяяем "Save" кнопку на новую "Edit" кнопку.
        HorizontalPanel toolsPanel = (HorizontalPanel) contactsFlexTable.getWidget(row, 2);
        toolsPanel.remove(0);
        toolsPanel.insert(getNewEditButton(newContact), 0);
        setErrorMessage("");
    }

    /**
     * Использует Anchor в виде ссылки для возможности добавления ClickHandler.
     * При нажатии на ссылку добавляется параметр поисковой строки в историю.
     */
    private Anchor getNewContactLink(final Contact contact) {

        Anchor contactLink = new Anchor(contact.getName());

        contactLink.addClickHandler(new ClickHandler() {

            // Т.к. Anchor не "слышит" альтернативных нажатий (СКМ и т.п.)
            // используем HyperlinkImpl.
            HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);
            public void onClick(ClickEvent event) {
                if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
                    onContactLinkClicked(contact);
                }
            }
        });

        return contactLink;
    }

    private HorizontalPanel getNewHorizontalToolsPanel(final Contact contact) {
        HorizontalPanel toolsPanel = new HorizontalPanel();
        toolsPanel.addStyleName(resources.style().tools_panel_no_border());

        toolsPanel.add(getNewEditButton(contact));
        toolsPanel.add(getNewDeleteButton());
        return toolsPanel;
    }

    private Button getNewEditButton(final Contact contact) {
        return new Button("Edit", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                int rowIndex = contactsFlexTable.getCellForEvent(clickEvent).getRowIndex();
                onEditButtonClicked(rowIndex, contact);
            }
        });
    }

    private Button getNewDeleteButton() {
        return new Button("x", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                int rowIndex = contactsFlexTable.getCellForEvent(clickEvent).getRowIndex();
                onDeleteButtonClicked(rowIndex);
            }
        });
    }

    private Button getNewSaveButton(final Contact contact) {
        return new Button("Save", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                int rowIndex = contactsFlexTable.getCellForEvent(clickEvent).getRowIndex();
                onSaveButtonClicked(rowIndex, contact);
            }
        });
    }

    @Override
    public void setErrorMessage(String message) {
        messageLabel.setStyleName(resources.style().error_label_red());
        messageLabel.setText(message);
    }

    @Override
    public void setSuccessMessage(String message) {
        messageLabel.setStyleName(resources.style().success_label_green());
        messageLabel.setText(message);
    }

    @Override
    public void setSearchString(String searchString) {
        searchTextBox.setText(searchString);
    }

    @Override
    public String getSearchString() {
        return searchTextBox.getText();
    }

    @Override
    public void emptyAddTextBoxes() {
        nameTextBox.setText("");
        numberTextBox.setText("");
        nameTextBox.setFocus(true);
    }

    private void addEnterKeyHandlers() {
        nameTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    addButton.click();
                }
            }
        });
        numberTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    addButton.click();
                }
            }
        });
    }

    /**
     * Слушает любые нажатия в поисковом TextBox'е и обновляет таблицу контактов.
     *
     */
    private void addKeyHandlerForSearch() {
        searchTextBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {

                String substring = searchTextBox.getText();

                // Обновляем таблицу контактов.
                getUiHandlers().getContactList(substring);

                setErrorMessage("");

                // Убираем Search параметр из URL если из
                // поискового TextBox'а было все удалено.
                if (substring.equals("")) {
                    PlaceRequest searchRequest = new PlaceRequest.Builder()
                            .nameToken(NameTokens.homePage)
                            .build();
                    placeManager.updateHistory(searchRequest, true);
                }
            }
        });
    }
}
