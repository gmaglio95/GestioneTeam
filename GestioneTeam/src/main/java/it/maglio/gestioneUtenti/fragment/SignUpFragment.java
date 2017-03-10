package it.maglio.gestioneUtenti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.utility.InformationType;
import it.maglio.gestioneUtenti.utility.MessageStringFactory;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.maglio.gestioneUtenti.asyncTask.PostCall;
import it.tortuga.beans.RuoloApplicativo;
import it.tortuga.beans.User;

/**
 * Created by pc ads on 23/02/2017.
 */

public class SignUpFragment extends GeneralFragment {

    String[] ruoliApplicativi = {RuoloApplicativo.AMMINISTRATORE.getRuolo(), RuoloApplicativo.PARTECIPANTE.getRuolo()};

    @BindView(R.id.ruoloApplicativoSpinner)
    Spinner spinnerApplicativo;

    @BindView(R.id.nomeUtente)
    EditText nomeWidget;
    @BindView(R.id.cognomeUtente)
    EditText cognomeWidget;
    @BindView(R.id.emailRegistrazione)
    EditText emailWidget;
    @BindView(R.id.password_signup)
    EditText passwordWidget;
    @BindView(R.id.codiceFiscale)
    EditText codiceFiscaleWidget;
    @BindView(R.id.dataNascita)
    EditText dataNascitaWidget;
    @BindView(R.id.confermaPassword)
    EditText confermaPasswordWidget;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_up_layout, container, false);
        ButterKnife.bind(this, v);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_item, ruoliApplicativi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApplicativo.setAdapter(adapter);
        return v;
    }

    @OnClick(R.id.btn_signup)
    public void signUp(View v) {
        if (validate()) {
            RuoloApplicativo ruoloApplicativo = null;
            if (spinnerApplicativo.getSelectedItem().equals(RuoloApplicativo.AMMINISTRATORE)) {
                ruoloApplicativo = RuoloApplicativo.AMMINISTRATORE;
            } else {
                ruoloApplicativo = RuoloApplicativo.PARTECIPANTE;
            }
            User user = new User();
            user.setNome(String.valueOf(nomeWidget.getText()));
            user.setCognome(String.valueOf(cognomeWidget.getText()));
            user.set_id(String.valueOf(emailWidget.getText()));
            user.setPassword(String.valueOf(passwordWidget.getText()));
            user.setCodiceFiscale(String.valueOf(codiceFiscaleWidget.getText()));
            user.setRuoloApplicativo(ruoloApplicativo);
            PostCall callPost = new PostCall(PathUtils.URL, PathUtils.PORT, PathUtils.PATH_SIGNUP, this, InformationType.USER, MessageStringFactory.REGISTRAZIONE_IN_CORSO);
            callPost.execute(user);
        }

    }


    public boolean validate() {
        boolean valid = true;


        String email = emailWidget.getText().toString();
        String password = passwordWidget.getText().toString();
        String passwordConfirmed = confermaPasswordWidget.getText().toString();
        String codiceFiscale = codiceFiscaleWidget.getText().toString();
        String nome = nomeWidget.getText().toString();
        String cognome = cognomeWidget.getText().toString();
        String dataNascita = dataNascitaWidget.getText().toString();

        if (StringUtils.isBlank(email)) {
            valid = false;
            emailWidget.setError(MessageStringFactory.EMPTY_ERROR);
        }
        if (StringUtils.isBlank(password)) {
            valid = false;
            passwordWidget.setError(MessageStringFactory.EMPTY_ERROR);
        }
        if (StringUtils.isBlank(codiceFiscale)) {
            valid = false;
            codiceFiscaleWidget.setError(MessageStringFactory.EMPTY_ERROR);
        }
        if (StringUtils.isBlank(nome)) {
            valid = false;
            nomeWidget.setError(MessageStringFactory.EMPTY_ERROR);
        }
        if (StringUtils.isBlank(cognome)) {
            valid = false;
            cognomeWidget.setError(MessageStringFactory.EMPTY_ERROR);
        }
        if (StringUtils.isBlank(dataNascita)) {
            valid = false;
            dataNascitaWidget.setError(MessageStringFactory.EMPTY_ERROR);
        }


        if (!password.equals(passwordConfirmed)) {
            passwordWidget.setError(MessageStringFactory.PASSWORD_NOT_MATCH);
            confermaPasswordWidget.setError(MessageStringFactory.PASSWORD_NOT_MATCH);
            valid = false;
        } else {
            confermaPasswordWidget.setError(null);
            passwordWidget.setError(null);
            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                passwordWidget.setError(MessageStringFactory.PASSWORD_4_10_CHAR);
                valid = false;
            } else {
                passwordWidget.setError(null);
            }
        }
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascita);
            if (date.after(new Date())) {
                valid = false;
                dataNascitaWidget.setError(MessageStringFactory.NO_DATA_FUTURO);
            } else {
                dataNascitaWidget.setError(null);
            }
        } catch (ParseException e) {
            valid = false;
            dataNascitaWidget.setError(MessageStringFactory.NO_DATA_FORMATTED_OK);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailWidget.setError(MessageStringFactory.EMAIL_NOT_VALID);
            valid = false;
        } else {
            emailWidget.setError(null);
        }
        if (!String.valueOf(codiceFiscaleWidget.getText()).matches("[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]")) {
            valid = false;
            codiceFiscaleWidget.setError(MessageStringFactory.COD_FISC_NOT_VALID);
        }
        return valid;
    }

    @Override
    public void callBackAfterCall(Object object) {
        User user = (User) object;
        new MaterialDialog.Builder(getContext())
                .title("Registrazione Avvenuta")
                .content("L'utente con email " + user.get_id() + " E' stato registrato con successo")
                .positiveText(MessageStringFactory.OK_MESSAGE).show();
    }
}
