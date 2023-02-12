/*
 * Created on May 11, 2005
 *
 */
package persistence;

import java.util.*;
import utility.ApplicationResourcesUtil;


/**
 * @author dkurth
 * 
 * Receives requests for dto classes and forwards them to the appropriate data 
 * access classes along with the appropriate ConnectionPool.
 *
 */
public final class PoolManager {
   
   private static PoolManager instance;
   private ConnectionPool pool;
   private String DBDriver, DBURL, DBUserId, DBPassword;

   ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
   
   public static PoolManager getInstance() throws Exception {
      if (instance == null) {
         instance = new PoolManager();
      }
      return instance;
   }

   private PoolManager() throws Exception {
      loadProperties();
      establishConnections();
   }

   private void loadProperties() throws Exception {
      DBDriver = resourceBundle.getString("DBDriver");
      DBURL = resourceBundle.getString("DBURL");
      DBUserId = resourceBundle.getString("DBUserId");
      DBPassword = resourceBundle.getString("DBPassword");
   }

   private void establishConnections() throws Exception {
      pool = new ConnectionPool(DBDriver, DBURL, DBUserId, DBPassword);
   }
   
   public ConnectionPool getConnectionPool() {
      return pool;
   }
   
   public void finalize() {
      pool = null;
   }     

   public static void main(String[] args) {
      try {
         PoolManager pb = PoolManager.getInstance(); 
         pb.getConnectionPool();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
