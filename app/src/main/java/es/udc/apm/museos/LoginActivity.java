package es.udc.apm.museos;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

@EActivity
public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    
    @ViewById
    SignInButton loginButton;

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

        loginButton.setSize(SignInButton.SIZE_STANDARD);
        loginButton.setOnClickListener(view -> signIn());
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
