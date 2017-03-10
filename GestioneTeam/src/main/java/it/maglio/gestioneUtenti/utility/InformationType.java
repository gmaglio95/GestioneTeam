package it.maglio.gestioneUtenti.utility;

import it.tortuga.beans.ElementToUpdateBean;
import it.tortuga.beans.IstitutoAllenamento;
import it.tortuga.beans.ListUsersBean;
import it.tortuga.beans.Squadra;
import it.tortuga.beans.User;

/**
 * Created by pc ads on 23/02/2017.
 */

public enum InformationType {

    USER(User.class), TEAM(Squadra.class), ISTITUTO(IstitutoAllenamento.class), ELEMENT_TO_UPDATE(ElementToUpdateBean.class), FILTER_BEAN(ListUsersBean.class);

    private InformationType(Class clazz) {
        this.clazz = clazz;
    }

    private Class clazz;

    public Class getClazz() {
        return clazz;
    }
}
