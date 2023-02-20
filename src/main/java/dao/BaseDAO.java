package dao;

import java.sql.Connection;

import persistence.ConnectionPool;
import persistence.PoolManager;

public class BaseDAO {
   protected ConnectionPool pool;
   protected Connection con;  

   public BaseDAO() {
      initPool();
   } 
   
   protected void initPool() {
      try {
         PoolManager pm = PoolManager.getInstance();
         pool = pm.getConnectionPool();
      }
      catch (Exception e) {
         e.printStackTrace();
      }     
   }
   
}