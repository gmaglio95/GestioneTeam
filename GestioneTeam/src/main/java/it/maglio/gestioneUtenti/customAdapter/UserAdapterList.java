package it.maglio.gestioneUtenti.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.giuseppe.app.R;
import it.tortuga.beans.User;

/**
 * Created by pc ads on 01/03/2017.
 */

public class UserAdapterList extends ArrayAdapter<User> {


    public UserAdapterList(Context context, int textViewResourceId,
                           List<User> objects) {
        super(context, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_view, null);
        User user = getItem(position);
        TextView nome = (TextView) convertView.findViewById(R.id.nomeUtente);
        TextView cognome = (TextView) convertView.findViewById(R.id.cognomeUtente);
        nome.setText(user.get_id());
        cognome.setText(user.getCodiceFiscale());
        return convertView;
    }
}
