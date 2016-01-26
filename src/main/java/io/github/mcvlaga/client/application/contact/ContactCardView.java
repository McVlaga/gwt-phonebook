package io.github.mcvlaga.client.application.contact;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import io.github.mcvlaga.client.application.validation.UiValidation;
import io.github.mcvlaga.client.application.validation.UiValidationResult;
import io.github.mcvlaga.client.place.NameTokens;
import io.github.mcvlaga.client.place.ParameterTokens;
import io.github.mcvlaga.client.resources.AppResources;
import io.github.mcvlaga.shared.dispatch.Contact;

/**
 * View страницы с карточкой контакта.
 */
public class ContactCardView extends ViewWithUiHandlers<ContactCardUiHandlers> implements ContactCardPresenter.MyView {
    interface Binder extends UiBinder<Widget, ContactCardView> {
    }

    @UiField
    HTMLPanel contactDetails;
    @UiField
    VerticalPanel namePanel;
    @UiField
    HorizontalPanel toolsPanel;
    @UiField
    VerticalPanel numberPanel;
    @UiField
    Hyperlink backLink;
    @UiField
    Label nameLabel;
    @UiField
    Button editButton;
    @UiField
    Button deleteButton;
    @UiField
    Label numberLabel;
    @UiField
    Label messageLabel;
    @UiField
    Label errorLabel;
    @UiField
    AppResources resources;

    private final PlaceManager placeManager;
    private Button saveButton;
    private TextBox nameTextBox;
    private TextBox numberTextBox;
    private boolean isEditing = false;

    @Inject
    ContactCardView(Binder binder,
                    PlaceManager placeManager) {
        this.placeManager = placeManager;
        initWidget(binder.createAndBindUi(this));

        // Настраиваем начальный UI
        nameLabel.addStyleName(resources.style().big_font());
        backLink.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        errorLabel.setVisible(false);
        createSaveButton();
    }

    @UiHandler("deleteButton")
    public void onDeleteButtonClicked(ClickEvent e) {

        // Запрос презентеру на удаление контакта.
        getUiHandlers().removeContact();

        errorLabel.setVisible(false);
    }

    @UiHandler("editButton")
    public void onEditButtonClicked(ClickEvent e) {
        isEditing = true;

        // Заменяем Label имени на TextBox.
        nameTextBox = new TextBox();
        nameTextBox.setStyleName(resources.style().big_font());
        nameTextBox.setText(nameLabel.getText());
        nameTextBox.setName(nameLabel.getText());
        nameLabel.removeFromParent();
        namePanel.insert(nameTextBox, 0);

        // Заменяем Label номера на TextBox.
        numberTextBox = new TextBox();
        numberTextBox.setText(numberLabel.getText());
        numberTextBox.setName(numberLabel.getText());
        numberLabel.removeFromParent();
        numberPanel.insert(numberTextBox, 1);

        // Заменяем кнопку "Edit" на кнопку "Save"
        toolsPanel.remove(0);
        toolsPanel.insert(saveButton, 0);
    }

    private void onSaveButtonClicked() {
        String newName = nameTextBox.getText();
        String newNumber = numberTextBox.getText();

        // Если введенные данные теже, то просто убираем редактирование этого ряда.
        if (newName.equals(nameTextBox.getName()) && newNumber.equals(numberTextBox.getName())) {
            changeContactDetails(newName, newNumber);
            return;
        }

        // Проверяем формат введенных данных.
        UiValidationResult result = UiValidation.validate(newName, newNumber);
        if (!result.isValid()) {
            setErrorMessage(result.getMessage());
            return;
        }

        // Запрос презентеру на изменение контакта.
        getUiHandlers().changeContact(newName, newNumber);
    }

    /**
     * Обновляет UI новыми данными и убирает редактирование.
     */
    private void changeContactDetails (String name, String number) {

        // Добавляем новое имя.
        nameTextBox.removeFromParent();
        nameLabel.setText(name);
        namePanel.insert(nameLabel, 0);

        // Добавляем новый номер.
        numberTextBox.removeFromParent();
        numberLabel.setText(number);
        numberPanel.insert(numberLabel, 1);

        // Заменяяем "Save" кнопку на "Edit" кнопку.
        toolsPanel.remove(0);
        toolsPanel.insert(editButton, 0);

        isEditing = false;
        errorLabel.setVisible(false);
    }

    @Override
    public void setBackLinkHistoryToken(String historyToken) {

        // Кнопка "назад" ведет на страницу со списком контактов.
        backLink.setTargetHistoryToken(historyToken);
    }

    @Override
    public void setContactDetails(Contact contact) {

        // Если редактирование включено (была нажата кнопка "Edit" или
        // редактирование не выключено с прошлой карточки контакта) то, ...
        if (isEditing) {

            // ...обновляем имя и номер, и выключаем редактирование.
            changeContactDetails(contact.getName(), contact.getNumber());

            // Добавляем новое имя контакта в URL.
            PlaceRequest changedNameRequest = new PlaceRequest.Builder()
                    .nameToken(NameTokens.contact)
                    .with(ParameterTokens.TOKEN_ID, contact.getName().replace(' ','_'))
                    .build();

            placeManager.updateHistory(changedNameRequest, true);
        }

        messageLabel.setVisible(false);
        contactDetails.setVisible(true);
        nameLabel.setText(contact.getName());
        numberLabel.setText(contact.getNumber());
    }

    @Override
    public void setMessage(String message) {
        messageLabel.setVisible(true);
        messageLabel.setText(message);
        contactDetails.setVisible(false);
    }

    @Override
    public void setErrorMessage(String message) {
        errorLabel.setVisible(true);
        errorLabel.setStyleName(resources.style().error_label_red());
        errorLabel.setText(message);
    }

    @Override
    public void setSuccessMessage(String message) {
        errorLabel.setVisible(true);
        errorLabel.setStyleName(resources.style().success_label_green());
        errorLabel.setText(message);
    }

    private void createSaveButton() {
        saveButton = new Button("Save", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                onSaveButtonClicked();
            }
        });
    }
}