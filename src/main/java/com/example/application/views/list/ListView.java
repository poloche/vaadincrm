package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Contacts | Vaadin CRM ")
@Route(value = "")
public class ListView extends VerticalLayout {
    private final CrmService service;
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolBar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");

        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void deleteContact(ContactForm.DeleteEvent deleteEvent) {
        service.deleteContact(deleteEvent.getContact());
        updateList();
        closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent saveEvent) {
        service.saveContact(saveEvent.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filtering by name ....");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(e -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");

        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));

    }

    private void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            form.addClassName("editing");
        }
    }

}
