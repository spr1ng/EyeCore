package eye.server.manager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author stream
 * @version $Id: ConfigLoader.java 44 2010-07-06 02:16:36Z spr1ng $
 */
public class ConfigLoader {

    private static String file;
    private static String host;
    private static int port;
    private static String user;
    private static String pass;
    private static Properties prop;
    private static ConfigLoader conf = new ConfigLoader();

    private ConfigLoader() {
    }

    public static ConfigLoader getInstance(String fName) {
        load(fName);
        return conf;
    }

    public static ConfigLoader getInstance() {
        load();
        return conf;
    }

    private static void init() {
        file = get("FILE") != null ? getString("FILE")  : System.getProperty("user.home") + "/eyedb.db4o";
        host = get("HOST") != null ? getString("HOST")  : "localhost";
        port = get("PORT") != null ? getInt("PORT")     : 4488;
        user = get("USER") != null ? getString("USER")  : "db4o";
        pass = get("PASS") != null ? getString("PASS")  : "db4o";
    }

    private static void load() {
        load("conf.properties");
    }

    private static void load(String fName) {
        prop = new Properties();
        try {
            prop.load(new FileReader(fName));
        } catch (IOException ex) {
            System.out.println("Config not found. Loading default one.");
        }
        init();
    }

    public void save() {
        try {
            prop.put("HOST", host);
            prop.put("PORT", Integer.toString(port));
            prop.put("USER", user);
            prop.put("PASS", pass);
            prop.store(new FileWriter(new File("conf.properties")), "#");
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    private static Object get(String key) {
        return prop.get(key);
    }

    private static boolean getBool(String key) {
        return Boolean.parseBoolean(prop.get(key).toString());
    }

    private static int getInt(String key) {
        return Integer.parseInt(prop.get(key).toString());
    }

    private static String getString(String key) {
        return prop.get(key).toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        ConfigLoader.host = host;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        ConfigLoader.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        ConfigLoader.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        ConfigLoader.user = user;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        ConfigLoader.file = file;
    }
    
}
