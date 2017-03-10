package it.maglio.gestioneUtenti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.utility.InterfaceToCallServer;

/**
 * Created by pc ads on 23/02/2017.
 */

public abstract class GeneralFragment extends Fragment implements InterfaceToCallServer {

    @Override
    public void showErrorServerConnection() {
        new MaterialDialog.Builder(this.getContext())
                .title("Errore Comuicazione con il Server")
                .content("Controllare che la connessione ad internet sia presente")
                .positiveText("Ok").show();
    }

    @Override
    public void showPopupWithMessage(String message) {
        new MaterialDialog.Builder(this.getContext())
                .title("Warning")
                .content(message)
                .positiveText("Ok").show();
    }

    @Override
    public Object getInstance() {
        return this.getActivity();
    }
}
