package es.udc.apm.museos.presenter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import android.util.Log;
import es.udc.apm.museos.model.Token;
import es.udc.apm.museos.model.User;
import es.udc.apm.museos.rest.RestClient;
import es.udc.apm.museos.view.LoginView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

@EBean
public class LoginPresenterMock implements LoginPresenter {

    @RestService
    RestClient restClient;

    @Override
    @Background
    public void handleLoginResult(LoginView view, GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d("LoginPresenter", "GoogleSignIn success");
            try {
                GoogleSignInAccount acct = result.getSignInAccount();
                User user = new User(acct.getDisplayName(), acct.getEmail());
                view.saveUser(user);
                // Initialize the local db with user data!
                view.navigateToARCamera();
            } catch (RestClientException e) {
                Log.d("LoginPresenter", e.getMessage());
                view.showLoginError();
            }
        } else {
            Log.d("LoginPresenter", "Error on sign in");
            view.showLoginError();
        }
    }

}
