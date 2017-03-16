package it.maglio.gestioneUtenti.utility;

/**
 * Created by pc ads on 23/02/2017.
 */

public interface PathUtils {
    //    public static final String URL = "http://52.42.61.190:";
    public static final String URL = "http://192.168.5.58";
    public static final String BROKER_MQTT = "tcp://52.42.61.190:";
    public static final String PORT = "8080";
    public static final String CONTROLLER_PATH = "/spring";
    public static final String PATH_LOGIN = CONTROLLER_PATH + "/login";
    public static final String PATH_SIGNUP = CONTROLLER_PATH + "/signUp";
    public static final String PATH_CHANGE_PASSWORD = CONTROLLER_PATH + "/changePassword";
    public static final String PATH_USER_LIST = CONTROLLER_PATH + "/userList";
    public static final String PATH_UPLOAD_PROFILE_IMAGE = CONTROLLER_PATH + "/uploadImage";
    public static final String ID_DEVICE = "ID_DEVICE";

}
