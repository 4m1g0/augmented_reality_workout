package es.udc.apm.museos.view.activity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import es.udc.apm.museos.R;
import es.udc.apm.museos.presenter.LoginPresenter;
import es.udc.apm.museos.presenter.LoginPresenterMock;
import es.udc.apm.museos.view.LoginView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

@EActivity
public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final int RC_SIGN_IN = 1;
    SignInButton loginButton;
    LoginPresenter loginPresenter;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Log.e("sign-in", "no available connection"))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setContentView(R.layout.activity_login);

        loginButton = (SignInButton) findViewById(R.id.login_button);
    }

    @Click(R.id.login_button)
    void loginButtonClicked() {

        loginButton.setEnabled(false);
        navigateToSignIn();
    }

    @Bean
    protected void setLoginPresenter(LoginPresenterMock loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            loginPresenter.handleLoginResult(this,result);
        }
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
        loginButton.setEnabled(true);
    }
}
