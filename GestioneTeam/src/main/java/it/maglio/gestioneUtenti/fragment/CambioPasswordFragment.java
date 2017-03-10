package it.maglio.gestioneUtenti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.utility.InformationType;
import it.maglio.gestioneUtenti.utility.MessageStringFactory;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.maglio.gestioneUtenti.bean.SessionBean;
import it.maglio.gestioneUtenti.asyncTask.PostCall;
import it.tortuga.beans.ElementToUpdateBean;

/**
 * Created by pc ads on 24/02/2017.
 */

public class CambioPasswordFragment extends GeneralFragment {


    @BindView(R.id.old_password)
    EditText oldPasswordWidget;
    @BindView(R.id.password_change)
    EditText passwordChangeWidget;
    @BindView(R.id.confermaPassword)
    EditText passwordConfirmedWidget;

    @OnClick(R.id.btn_confirm)
    public void changePassword(View view) {

        if (validate()) {
            PostCall postCall = new PostCall(PathUtils.URL, PathUtils.PORT, PathUtils.PATH_CHANGE_PASSWORD, this, InformationType.ELEMENT_TO_UPDATE, MessageStringFactory.MODIFICA_IN_CORSO);
            ElementToUpdateBean beanToSend = new ElementToUpdateBean();
            SessionBean.getSessionBean().getUser().setPassword(oldPasswordWidget.getText().toString());
            beanToSend.setUser(SessionBean.getSessionBean().getUser());
            beanToSend.setElementToUpdate(passwordChangeWidget.getText().toString());
            postCall.execute(beanToSend);
        }

    }

    private boolean validate() {
        boolean valid = true;
        String password = passwordChangeWidget.getText().toString();
        String passwordConfirmed = passwordConfirmedWidget.getText().toString();
        String oldPassword = oldPasswordWidget.getText().toString();
        if (StringUtils.isBlank(password)) {
            passwordChangeWidget.setError(MessageStringFactory.EMPTY_ERROR);
            valid = false;
        }
        if (StringUtils.isBlank(passwordConfirmed)) {
            passwordConfirmedWidget.setError(MessageStringFactory.EMPTY_ERROR);
            valid = false;
        }
        if (StringUtils.isBlank(oldPassword)) {
            oldPasswordWidget.setError(MessageStringFactory.EMPTY_ERROR);
            valid = false;
        }

        if (!password.equals(passwordConfirmed)) {
            passwordChangeWidget.setError(MessageStringFactory.PASSWORD_NOT_MATCH);
            valid = false;
        }

        return valid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cambio_password, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void callBackAfterCall(Object object) {
        new MaterialDialog.Builder(getContext())
                .title("Cambio Password")
                .content("La tua password e' stata aggiornata con successo !")
                .positiveText(MessageStringFactory.OK_MESSAGE).show();
    }
}
