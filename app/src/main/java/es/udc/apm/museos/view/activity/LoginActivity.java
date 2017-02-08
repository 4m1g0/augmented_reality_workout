package es.udc.apm.museos.view.activity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import es.udc.apm.museos.R;
import es.udc.apm.museos.presenter.LoginPresenter;
import es.udc.apm.museos.presenter.LoginPresenterMock;
import es.udc.apm.museos.view.LoginView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

@EActivity
public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String clientId = "480537394409-sfhu3hcvhupalgug7hpgf9r7vp8fqdp9.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 1;

    LoginPresenter loginPresenter;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Log.e("sign-in", "no available connection"))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setContentView(R.layout.activity_login);
    }

    @Click(R.id.login_button)
    void loginButtonClicked() {
        navigateToSignIn();
    }

    @Bean
    protected void setLoginPresenter(LoginPresenterMock loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @OnActivityResult(RC_SIGN_IN)
    void onResult(Intent data) {
        Log.d("Login", "onResult");
        loginPresenter.handleLoginResult(this, Auth.GoogleSignInApi.getSignInResultFromIntent(data));
    }

    private void navigateToSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void navigateToARCamera() {
        Log.d("LoginActivity", "Navigate to camera");
        startActivity(new Intent(this, ARCameraActivity_.class));
    }

    @Override
    public void showLoginError() {
        Log.d("LoginActivity", "Show error");
    }
}
