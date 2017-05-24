package es.udc.apm.museos.view;

import es.udc.apm.museos.model.User;

public interface LoginView {
    void navigateToARCamera();
    void showLoginError();
    void saveUser(User user);
}
