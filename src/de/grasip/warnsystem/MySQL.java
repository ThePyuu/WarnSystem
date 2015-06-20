package de.grasip.warnsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL
{
  private String user = "";
  private String pass = "";
  private String host = "";
  private String db = "";
  private Connection connection;

  public MySQL(String user,String pass,String host,String db) {
	  this.user=user;
	  this.pass=pass;
	  this.host=host;
	  this.db=db;
	  connect();
	}
  
  public void close()
  {
    try
    {
      if (connection != null)
        connection.close();
    }
    catch (Exception ex) {
    	System.err.println(ex);
    }
  }

  public void connect() {

	   try {
		
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db, 
				user, pass);
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
  }
  
  public void Update(String qry) {
	    try {
	      Statement stmt = connection.createStatement();
	      stmt.executeUpdate(qry);

	      stmt.close();
	    } catch (Exception ex) {
	    	connect();
	    	
	    	System.err.println(ex);
	    	
	    }
	  }
  
  public ResultSet Query(String qry) {
	    ResultSet rs = null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(qry);
	    }
	    catch (Exception ex) {
	    	connect();
	    	System.err.println(ex);
	    }

	    return rs;
	  }
}