package org.vaadin.jchristophe.views.login;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.jchristophe.security.AuthenticatedUser;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Remember Me");
        i18n.getHeader().setDescription("Login using user/user or admin/admin");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);
        addRememberMeCheckbox();

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }


    public void addRememberMeCheckbox() {
        Checkbox rememberMe = new Checkbox("Remember me");
        rememberMe.getElement().setAttribute("name", "remember-me");
        Element loginFormElement = getElement();
        Element element = rememberMe.getElement();
        loginFormElement.appendChild(element);

        String executeJsForFieldString = "const field = document.getElementById($0);" +
                "if(field) {" +
                "   field.after($1)" +
                "} else {" +
                "   console.error('could not find field', $0);" +
                "}";
        getElement().executeJs(executeJsForFieldString, "vaadinLoginPassword", element);
    }
}
