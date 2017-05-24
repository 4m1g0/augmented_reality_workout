package es.udc.apm.museos.view.activity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import es.udc.apm.museos.R;
import es.udc.apm.museos.model.User;
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
    ProgressDialog progress;
    SharedPreferences prefs;

    @Override
    protected void onResume() {
        super.onResume();

        loginButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("es.udc.apm.museos", Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Log.e("sign-in", "no available connection"))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setContentView(R.layout.activity_login);

        loginButton = (SignInButton) findViewById(R.id.login_button);

        progress = new ProgressDialog(this);
        progress.setTitle("Login in");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        String token = prefs.getString("es.udc.apm.museos.email", null);

        if (token != null){
            navigateToARCamera();
            return;
        }
    }

    @Click(R.id.login_button)
    void loginButtonClicked() {
        progress.show();
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
        progress.dismiss();
        Log.d("LoginActivity", "Navigate to camera");
        startActivity(new Intent(this, MapActivity_.class));
    }

    @Override
    public void showLoginError() {
        runOnUiThread(() -> {
            progress.dismiss();
            Log.d("LoginActivity", "Show error");
            loginButton.setEnabled(true);
            TextView errorText = (TextView) findViewById(R.id.error_text);
            errorText.setText(R.string.error_login);
        });
    }

    @Override
    public void saveUser(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("es.udc.apm.museos.email", user.email);
        editor.putString("es.udc.apm.museos.name", user.name);

        editor.apply();
    }
}
