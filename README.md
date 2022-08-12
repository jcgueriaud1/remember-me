# Remember Me

This project is based on start.vaadin.com.

It's an example of an implementation of Spring Security "Remember Me".

## Steps to add remember me

### Add the Remember Me checkbox in the login view

You need to create a checkbox with name="remember-me" that will be posted in the login form.It's unfortunately not supported by the Vaadin login component. (See this ticket: https://github.com/vaadin/web-components/issues/737 )
```
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
```
It might not work in Vaadin 14 since the structure of the component is different.

https://github.com/jcgueriaud1/remember-me/blob/remember-me/src/main/java/org/vaadin/jchristophe/views/login/LoginView.java#L47

### Add the Spring configuration

```
   @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class, LOGOUT_SUCCESS_URL);
        String privateSecretKeyToChange = "JKJDSKLDJdJSisdjsdfjmkdjdfkljkJKLjlk";
        http.rememberMe().key(privateSecretKeyToChange).tokenValiditySeconds(7200).userDetailsService(this.userDetailsService);
        http.logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me");
    }
```

It's a standard Spring configuration for Remember me for example here: https://www.baeldung.com/spring-security-remember-me

You need a private key and also a userDetailsService (in your application with a real authentication it might be unnecessary).
By default it will create a cookie "remember-me" after login with the checkbox checked.

When you logged out, you need to clear this cookie.

### Logout

To logout from the application you can call:
```
VaadinServletRequest.getCurrent().getHttpServletRequest().logout();
```

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different 
IDEs](https://vaadin.com/docs/latest/flow/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/rememberme-1.0-SNAPSHOT.jar`

