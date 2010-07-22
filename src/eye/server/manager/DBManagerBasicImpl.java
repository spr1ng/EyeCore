/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.server.manager;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Predicate;
import eye.core.model.Image;
import eye.core.model.Place;
import eye.core.model.RemoteSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author spr1ng
 * @version $Id: DBManagerEyeImpl.java 55 2010-07-08 02:23:31Z stream $
 */
public class DBManagerBasicImpl extends AbstractDBManager{
    
    private static ConfigLoader conf = ConfigLoader.getInstance();

    public ObjectContainer getContainer() {
        try {
        return Db4oClientServer.openClient(Db4oClientServer
                .newClientConfiguration(), conf.getHost(), conf.getPort(), conf.getUser(), conf.getPass());
        } catch(Db4oIOException dbioe){
            JOptionPane.showMessageDialog(null, dbioe);
            return null;
        }
    }

    /**
     * @deprecated use EyeAdmin instead
     */
    public void watchDb() {
        ObjectContainer db = getContainer();
        try {
            watchDb(db);
        } finally {
            db.close();
        }
    }

    /**
     * @deprecated use EyeAdmin instead
     * @param db
     */
    public void watchDb(ObjectContainer db) {
        ObjectSet objects = db.query().execute();

        int places = 0, images = 0;
        for (Object object : objects) {
            if (object instanceof Place) {
                places++;
                Place p = (Place) object;
                LOG.info("Place url: " + p.getUrl());
                if (p.getDate() != null) {
                    LOG.info("Place data: " + p.getDate());
                }
            }
        }
        LOG.info("-------> Total places: " + places);
        for (Object object : objects) {
            if (object instanceof Image) {
                images++;
                Image i = (Image) object;
                LOG.info("Image: " + i.getUrl());
                if (i.getEdgeMap() != null) {
                    LOG.info("!!!!!!!!!!!!!!!!!!!!!!!Points: " + i.getEdgeMap());
                }
            }
        }
        LOG.info("-------> Total images: " + images);

        LOG.info("Total objects: " + objects.size());
    }

    /**
     * Сохраняет все объекты списка в базу. Возвращает количество новых
     * сохраненных объектов
     * @param objects
     */
    public int store(List objects) {
        if (objects == null) {
            LOG.severe("A try to store a null list of objects");
            return 0;
        }
        ObjectContainer db = getContainer();
        int qty = 0;
        try {
            for (Object o : objects) {
                if (o != null) {
                    //Если такого объекта еще нет в базе
                    if (!isItemStored(o)) {
                        db.store(o);
                        qty++;
                    } //else LOG.error("A try to store a duplicate object"); //DELME:
                } else LOG.severe("A try to store a null object");
            }
        } finally { db.close(); }

        return qty;
    }

    /** Сохраняет удаленный источник в БД */
    public void store(RemoteSource rs) {
        try {
            new URL(rs.getUrl());
        } catch (MalformedURLException ex) {
            LOG.severe("Can't reach target: " + rs.getUrl());
        }
        List<RemoteSource> sources = new ArrayList<RemoteSource>(1);
        sources.add(rs);
        store(sources);
    }

    /** Retrieves all valid images from DB */
    public List<Image> getValidImages(){//PENDING: нельзя здесь закрывать. понять почему!!
        ObjectContainer db = getContainer();
        try {
            List<Image> images = db.query(new Predicate<Image>() {
            @Override
            public boolean match(Image image) {
                if (image.getEdgeMap() == null ||
                    image.getAngleMap() == null) return false;
                return true;
            }
        });
            return images;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        } /*finally {
            db.close();
        }*/
        
    }

}
