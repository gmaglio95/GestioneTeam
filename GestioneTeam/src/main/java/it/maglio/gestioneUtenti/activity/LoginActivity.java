package it.maglio.gestioneUtenti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.utility.InformationType;
import it.maglio.gestioneUtenti.utility.MessageStringFactory;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.maglio.gestioneUtenti.bean.SessionBean;
import it.maglio.gestioneUtenti.asyncTask.PostCall;
import it.tortuga.beans.RuoloApplicativo;
import it.tortuga.beans.User;

public class LoginActivity extends GeneralActivity {


    @BindView(R.id.input_email)
    TextView emailWidget;
    @BindView(R.id.input_password)
    TextView passwordWidget;

    @OnClick(R.id.btn_login)
    public void submit(View view) {
        login();
    }

    @OnClick(R.id.skipLogin)
    public void skipLogin(View view) {
        User user = new User();
        user.set_id("giuseppe.maglio@hotmail.com");
        user.setCodiceFiscale("MGLGPP95T15E986Y");
        user.setCognome("Maglio");
        user.setNome("Giuseppe Edoardo");
        user.setRuoloApplicativo(RuoloApplicativo.AMMINISTRATORE);
        user.setDataNacita(new Date());
        SessionBean.createSessionBean(user);
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
    }

    @Override
    public void callBackAfterCall(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            SessionBean.createSessionBean(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            this.showPopupWithMessage("Problema di Login contattare il supporto");
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailWidget.getText().toString();
        String password = passwordWidget.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailWidget.setError(MessageStringFactory.EMAIL_NOT_VALID);
            valid = false;
        } else {
            emailWidget.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordWidget.setError(MessageStringFactory.PASSWORD_4_10_CHAR);
            valid = false;
        } else {
            passwordWidget.setError(null);
        }

        return valid;
    }

    private void login() {
        if (validate()) {
            User user = new User();
            user.set_id(emailWidget.getText().toString());
            user.setPassword(passwordWidget.getText().toString());
            PostCall postCall = new PostCall(PathUtils.URL, PathUtils.PORT, PathUtils.PATH_LOGIN, this, InformationType.USER, MessageStringFactory.ACCESSO_IN_CORSO);
            postCall.execute(user);
        }
    }
}
