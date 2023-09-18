package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("list")
@Route(value = "")
public class ListView extends VerticalLayout {

    public ListView() {
        Button button = new Button("Click me");
        TextField name = new TextField("Name");

        HorizontalLayout hl = new HorizontalLayout(name, button);
        hl.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        button.addClickListener(e -> Notification.show("Hello " + name.getValue()));
        add(hl);
    }

}
