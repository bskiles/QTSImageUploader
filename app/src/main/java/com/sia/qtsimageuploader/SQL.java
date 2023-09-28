package com.sia.qtsimageuploader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
    public static final int SQL_ALARM_EVENT = 1;
    public static final int SQL_ERROR_EVENT = 3;
    public static final int SQL_INFO_EVENT = 2;
    public static final int SQL_SOURCE_ID = 11;
    public static int EquipID = 0;
    public static String DeviceName = "";
    public static boolean SqlStatus = false;

    public int equipID = 0;

    // SQL Connection settings
    private static String ip = "10.216.3.60";
    private static String port = "1433";
    private static String _class = "net.sourceforge.jtds.jdbc.Driver";
    private static String db = "SIA_ALC_DB";
    private static String username = "AndroidPBS";
    private static String password = "Eznz3ynfydqKZ$mOXXLnX8";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+db;
    public static Connection conn = null;

    public static void InitSql(){
        // TODO
    }

    /// Initialize the sql connection
    public static boolean ConnectSql(){
        try {
            Class.forName(_class);
            conn = DriverManager.getConnection(url, username, password);
            SqlStatus = true;
            return true;
        } catch (ClassNotFoundException e) {
            LocalEventLogging.LogEvent("SQL error", LocalEventLogging.CustomEventType.Error, e);
            SqlStatus = false;
        } catch (SQLException throwables) {
            LocalEventLogging.LogEvent("SQL error", LocalEventLogging.CustomEventType.Error, null);
            SqlStatus = false;
        } catch(Exception e){
            LocalEventLogging.LogEvent("SQL error", LocalEventLogging.CustomEventType.Error, e);
            SqlStatus = false;
        }
        return false;
    }

    /// Disconnect from the sql connection
    public static void DisconnectSql(){
        try {
            if(conn != null && !conn.isClosed()){
                conn.close();
            }
            SqlStatus = false;
        } catch (SQLException throwables) {
            SqlStatus = false;
        }
    }

    static boolean LogEvent(String msg, int type){
        if(conn != null) {
            int id = 0;
            Statement statement = null;
            try {
                statement = conn.createStatement();
                return statement.execute("exec log.dbo.spLogEventToHistory " + SQL_SOURCE_ID + ", " + type + ", '" + msg + "', '" + "AndroidUser" + "'");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                LocalEventLogging.LogEvent("LogEvent", LocalEventLogging.CustomEventType.Error, null);
            }
        }
        return false;
    }


}
