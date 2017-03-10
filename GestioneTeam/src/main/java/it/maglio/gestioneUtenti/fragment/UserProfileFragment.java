package it.maglio.gestioneUtenti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.customAdapter.UserAdapterList;
import it.maglio.gestioneUtenti.asyncTask.PostCall;
import it.maglio.gestioneUtenti.utility.InformationType;
import it.maglio.gestioneUtenti.utility.MessageStringFactory;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.tortuga.beans.FilterGeneralBean;
import it.tortuga.beans.GeneralBean;
import it.tortuga.beans.ListUsersBean;
import it.tortuga.beans.User;

/**
 * Created by pc ads on 01/03/2017.
 */

public class UserProfileFragment extends GeneralFragment {

    @BindView(R.id.listView)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_prova, container, false);
        ButterKnife.bind(this, v);
        getUsersToView();
        return v;
    }

    public void createListViewer(List<User> users) {
        UserAdapterList adapterList = new UserAdapterList(this.getContext(), R.layout.list_item_view, users);
        listView.setAdapter(adapterList);
    }

    public void getUsersToView() {
        PostCall post = new PostCall(PathUtils.URL, PathUtils.PORT, PathUtils.PATH_USER_LIST, this, InformationType.FILTER_BEAN, MessageStringFactory.RECUPERO_INFORMAZIONI);
        post.execute((GeneralBean[]) null);
    }

    public FilterGeneralBean createFilter() {
        FilterGeneralBean filter = new FilterGeneralBean();
        return filter;
    }


    @Override
    public void callBackAfterCall(Object object) {
        List<User> users = new ArrayList(((ListUsersBean) object).getUsers());
        createListViewer(users);
    }
}
