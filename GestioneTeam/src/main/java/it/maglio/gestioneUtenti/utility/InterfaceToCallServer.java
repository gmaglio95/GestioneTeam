package it.maglio.gestioneUtenti.utility;

/**
 * Created by pc ads on 24/02/2017.
 */

public interface InterfaceToCallServer {

    public abstract void callBackAfterCall(Object object);

    public void showErrorServerConnection();

    public void showPopupWithMessage(String message);

    public Object getInstance();
}
