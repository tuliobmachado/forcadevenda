package br.com.informsistemas.forcadevenda.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.fragments.AcessoFragment;
import br.com.informsistemas.forcadevenda.fragments.LoginFragment;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;

public class LoginActivity extends AppCompatActivity {

    private String token;
    private Fragment fragmentLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
            }
        });

        String status = getIntent().getStringExtra("Status");

        if (status == null) {
            onShowLogin();
        }else{
            onShowAcesso();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            // Disable going back to the MainActivity
            moveTaskToBack(true);
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void onShowLogin(){
        fragmentLogin = getSupportFragmentManager().findFragmentByTag("loginFragment");

        if (fragmentLogin == null){
            fragmentLogin = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragmentLogin, "loginFragment");
            ft.commit();
        }
    }

    private void onShowAcesso(){
        Fragment fragmentAcesso = getSupportFragmentManager().findFragmentByTag("acessoFragment");

        if (fragmentAcesso == null){
            fragmentAcesso = new AcessoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragmentAcesso, "AcessoFragment");
            ft.commit();
        }
    }

    public String getToken(){
        return token;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQUESTCODE.READ_PHONE_STATE){
            Constants.PERMISSION.READ_PHONE_STATE = grantResults[0];

            if (Constants.PERMISSION.ACCESS_FINE_LOCATION == PackageManager.PERMISSION_DENIED){
                Misc.SolicitaPermissao(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION_REQUESTCODE.ACCESS_FINE_LOCATION);
            }
        }

        if (requestCode == Constants.PERMISSION_REQUESTCODE.ACCESS_FINE_LOCATION){
            Constants.PERMISSION.ACCESS_FINE_LOCATION = grantResults[0];
        }

        if (Constants.PERMISSION.READ_PHONE_STATE == PackageManager.PERMISSION_GRANTED && Constants.PERMISSION.ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            ((LoginFragment) fragmentLogin).login();
        }
    }

}
