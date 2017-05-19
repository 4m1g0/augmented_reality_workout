package es.udc.apm.museos.presenter;

import es.udc.apm.museos.view.LoginView;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public interface LoginPresenter {
    void handleLoginResult(LoginView view, GoogleSignInResult result);
}
