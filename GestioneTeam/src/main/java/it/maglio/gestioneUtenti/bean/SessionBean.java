package it.maglio.gestioneUtenti.bean;

import it.tortuga.beans.User;

/**
 * Created by pc ads on 17/02/2017.
 */

public class SessionBean {


    private User user;

    private static SessionBean sessionBean;

    private SessionBean() {
    }

    ;


    public static void createSessionBean(User user) {
        sessionBean = new SessionBean();
        sessionBean.user = user;
    }

    public static SessionBean getSessionBean() {
        return sessionBean;
    }

    public User getUser() {
        return user;
    }
}
