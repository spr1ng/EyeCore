/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 *
 * @author spr1ng
 * @version $Id: InetUtils.java 43 2010-07-06 02:16:08Z spr1ng $
 */
public class InetUtils {
    
    private final static Logger LOG = Logger.getAnonymousLogger();

    /**
     * @param host
     * @param port
     * @param login
     * @param pass
     */
    public void setProxy(String host, Integer port, final String login, final String pass) {
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port.toString());
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, pass.toCharArray());
            }
        });
    }
    /**
     * Определяет, доступен ли удаленный ресурс
     * @param url
     * @param timeout
     * @return
     */
    public static boolean isReachable(String url, int timeout){
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            conn.setConnectTimeout(timeout);
            conn.connect();
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
        return true;
    }

    /** Определяет, доступен ли удаленный ресурс */
    public static boolean isReachable(String url){
        return isReachable(url, 500);
    }

    /** Полезен при определении, есть ли у модуля доступ к интернет */
    public static boolean hasInetConnection(){
        return isReachable("http://ya.ru", 3000);
    }


}
