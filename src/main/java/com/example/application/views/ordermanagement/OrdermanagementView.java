package com.example.application.views.ordermanagement;

import com.example.application.data.entity.Orders;
import com.example.application.data.service.OrdersService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
//import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;

//import org.apache.catalina.servlets.DefaultServlet.SortManager.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Ordermanagement")
@Route(value = "ordermanagement/:ordersID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class OrdermanagementView extends Div implements BeforeEnterObserver {

    private final String ORDERS_ID = "ordersID";
    private final String ORDERS_EDIT_ROUTE_TEMPLATE = "ordermanagement/%s/edit";

    private final Grid<Orders> grid = new Grid<>(Orders.class, false);

    private TextField orderId;
    private TextField customerId;
    private TextField objectDescription;
    private TextField pickupAdress;
    private TextField pickupCountry;
    private TextField deliveryAdress;
    private TextField deliveryCountry;
    private TextField vehicleId;
    private DatePicker etd;
    private DatePicker eta;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button search = new Button("Search");

    private final BeanValidationBinder<Orders> binder;

    private Orders orders;

    private final OrdersService ordersService;

    public OrdermanagementView(OrdersService ordersService) {
        this.ordersService = ordersService;
        addClassNames("ordermanagement-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("orderId").setAutoWidth(true);
        grid.addColumn("customerId").setAutoWidth(true);
        grid.addColumn("objectDescription").setAutoWidth(true);
        grid.addColumn("pickupAdress").setAutoWidth(true);
        grid.addColumn("pickupCountry").setAutoWidth(true);
        grid.addColumn("deliveryAdress").setAutoWidth(true);
        grid.addColumn("deliveryCountry").setAutoWidth(true);
        grid.addColumn("vehicleId").setAutoWidth(true);
        grid.addColumn("etd").setAutoWidth(true);
        grid.addColumn("eta").setAutoWidth(true);

        grid.setItems(query -> ordersService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ORDERS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(OrdermanagementView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Orders.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        delete.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        search.addClickListener(e -> {
            
        });

        save.addClickListener(e -> {
            try {
                if (this.orders == null) {
                    this.orders = new Orders();
                }
                binder.writeBean(this.orders);
                ordersService.update(this.orders);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(OrdermanagementView.class);
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
        Optional<Long> ordersId = event.getRouteParameters().get(ORDERS_ID).map(Long::parseLong);
        if (ordersId.isPresent()) {
            Optional<Orders> ordersFromBackend = ordersService.get(ordersId.get());
            if (ordersFromBackend.isPresent()) {
                populateForm(ordersFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested Order was not found, ID = %s", ordersId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(OrdermanagementView.class);
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
        orderId = new TextField("Order ID");
        customerId = new TextField("Customer ID");
        objectDescription = new TextField("Object Description");
        pickupAdress = new TextField("Adress for Pick Up");
        pickupCountry = new TextField("Country for Pick Up");
        deliveryAdress = new TextField("Adress for Delivery");
        deliveryCountry = new TextField("Country for Delivery");
        vehicleId = new TextField("Vehicle ID");
        etd = new DatePicker("Estimated Time of Departure");
        eta = new DatePicker("Estimated Time of Arrival");

        formLayout.add(orderId, customerId, objectDescription, pickupAdress, pickupCountry, deliveryAdress, deliveryCountry, vehicleId, etd, eta);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        search.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(search, cancel, save, delete);
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

    private void populateForm(Orders value) {
        this.orders = value;
        binder.readBean(this.orders);

    }
}
