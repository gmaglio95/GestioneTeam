package it.maglio.gestioneUtenti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.giuseppe.app.R;

/**
 * Created by pc ads on 16/03/2017.
 */

public class ChatFragment extends GeneralFragment {
    @Override
    public void callBackAfterCall(Object object) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_dialog, container, false);
        ButterKnife.bind(this, v);
        return v;
    }



}
