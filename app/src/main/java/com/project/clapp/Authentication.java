package com.project.clapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clapp.impl.UserFirebaseManager;

public class Authentication extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    private EditText emailUser;
    private EditText passUser;
    private TextView errorMessage;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        mAuth = FirebaseAuth.getInstance();
        emailUser = findViewById(R.id.inputEmail);
        passUser = findViewById(R.id.inputPass);
        errorMessage = findViewById(R.id.errorMessage);
        imgView = findViewById(R.id.imgIcon);
        //imgView.setImageResource(R.drawable.hashsip);

        mAuth.signOut();
    }

    public void login(View view) {
        String email = emailUser.getText().toString();
        String password = passUser.getText().toString();
        //Verifica se o utilizador existe na Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "PODE ENTRAR");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Envia os dados para a RealTimeDatabase do Firebase
                            UserFirebaseManager ufm = UserFirebaseManager.getInstance();
                            ufm.addUser(user.getUid(), user.getEmail());

                            //PASSAGEM PARA A HOME PAGE
                            Intent intent = new Intent(Authentication.this, LoadingActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "UTILIZADOR INEXISTENTE", task.getException());
                            errorMessage.setText("User does not exist");
                        }
                    }
                });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        System.out.println("back press");
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(i);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
