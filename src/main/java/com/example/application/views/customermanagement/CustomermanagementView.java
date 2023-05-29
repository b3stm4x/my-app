package com.example.application.views.customermanagement;

import com.example.application.data.entity.CustomerEntity;
import com.example.application.data.service.CustomerService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Customermanagement")
@Route(value = "customermanagement/:CustomerEntityID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class CustomermanagementView extends Div implements BeforeEnterObserver {

    private final String CUSTOMERENTITY_ID = "CustomerEntityID";
    private final String CUSTOMERENTITY_EDIT_ROUTE_TEMPLATE = "customermanagement/%s/edit";

    private final Grid<CustomerEntity> grid = new Grid<>(CustomerEntity.class, false);

    private TextField kundennummer;
    private TextField firma1;
    private TextField firma2;
    private TextField customerStrasse;
    private TextField customerHausnummer;
    private TextField customerStadt;
    private TextField customerLand;
    private TextField customerPLZ;
    private EmailField customerEmail;
    private TextField customerKontaktperson;
    private EmailField customerKontaktmail;
    private TextField customerUmsatzsteueridentifikation;
    private TextField customerNotiz;
    private TextField customerTelefonnummer;
    private TextField customerKontakttelefon;


    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button search = new Button("Search");

    private final BeanValidationBinder<CustomerEntity> binder;

    private CustomerEntity CustomerEntity;

    private final CustomerService CustomerService;

    public CustomermanagementView(CustomerService customerService) {
        this.CustomerService = customerService;
        addClassNames("customermanagement-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        // grid.addColumn("kundennummer").setAutoWidth(true);
        grid.addColumn("kundennummer").setHeader("Kundennummer").setAutoWidth(true);
        grid.addColumn("firma1").setHeader("Firma").setAutoWidth(true);
        grid.addColumn("customerStrasse").setHeader("Straße").setAutoWidth(true);
        grid.addColumn("customerHausnummer").setHeader("Hausnummer").setAutoWidth(true);
        grid.addColumn("customerStadt").setHeader("Stadt").setAutoWidth(true);
        grid.addColumn("customerPLZ").setHeader("PLZ").setAutoWidth(true);
        grid.addColumn("customerLand").setHeader("Land").setAutoWidth(true);
        grid.addColumn("customerTelefonnummer").setHeader("Telefon").setAutoWidth(true);
        grid.addColumn("customerEmail").setHeader("Email").setAutoWidth(true);
        grid.addColumn("customerUmsatzsteueridentifikation").setHeader("Umsatzsteueridentifikation").setAutoWidth(true);
        grid.addColumn("customerNotiz").setHeader("Notiz").setAutoWidth(true);
        grid.addColumn("customerKontaktperson").setHeader("Ansprechpartner").setAutoWidth(true);
        grid.addColumn("customerKontakttelefon").setHeader("Kontakt-Telefon").setAutoWidth(true);
        grid.addColumn("customerKontaktmail").setHeader("Kontakt-Mail").setAutoWidth(true);
        grid.addColumn("firma2").setHeader("Firma 2").setAutoWidth(true);




        grid.setItems(query -> CustomerService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CUSTOMERENTITY_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CustomermanagementView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(CustomerEntity.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.CustomerEntity == null) {
                    this.CustomerEntity = new CustomerEntity();
                }
                binder.writeBean(this.CustomerEntity);
                CustomerService.update(this.CustomerEntity);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(CustomermanagementView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> CustomerEntityId = event.getRouteParameters().get(CUSTOMERENTITY_ID).map(Long::parseLong);
        if (CustomerEntityId.isPresent()) {
            Optional<CustomerEntity> CustomerEntityFromBackend = CustomerService.get(CustomerEntityId.get());
            if (CustomerEntityFromBackend.isPresent()) {
                populateForm(CustomerEntityFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %s", CustomerEntityId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(CustomermanagementView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        kundennummer = new TextField("Kundennummer");
        firma1 = new TextField("firma1");
        customerStrasse = new TextField("Straße");
        customerHausnummer = new TextField("Hausnummer");
        customerStadt = new TextField("Stadt");
        customerPLZ = new TextField("PLZ");
        customerLand = new TextField("Land");
        customerTelefonnummer = new TextField("Telefonnummer");
        customerEmail = new EmailField("Email");
        customerUmsatzsteueridentifikation = new TextField("Umsatzsteueridentifikation");
        customerNotiz = new TextField("Notiz");
        customerKontaktperson = new TextField("Kontaktperson");
        customerKontakttelefon = new TextField("Kontakt-Telefon");
        customerKontaktmail = new EmailField("Kontakt-Email");
        firma2 = new TextField("firma2");
        formLayout.add(kundennummer, firma1, customerStrasse, customerHausnummer, customerStadt,
                customerPLZ, customerLand, customerTelefonnummer, customerEmail, customerUmsatzsteueridentifikation
        , customerNotiz, customerKontaktperson, customerKontakttelefon, customerKontaktmail, firma2);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(CustomerEntity value) {
        this.CustomerEntity = value;
        binder.readBean(this.CustomerEntity);

    }
}
