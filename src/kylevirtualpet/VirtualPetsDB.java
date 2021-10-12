package kylevirtualpet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualPetsDB {
    
    private static final DBManager dbManager = new DBManager();;
    private static final Connection conn  = dbManager.getConnection();;
    
    private VirtualPetsDB() {
    }
    
    public static void connectVirtualPetDB() {
        try {
            String createBook = "CREATE TABLE PETS (PETID INT, NAME VARCHAR(50), ANIMAL VARCHAR(50), BREED VARCHAR(50), DIFF VARCHAR(20), HAPPY INT, FOOD INT, CLEAN INT)";
            String insertBook = "INSERT INTO PETS VALUES " +
                    "(1, 'Mordecai', 'Bird', 'Blue Jay', 'Easy', 10, 8, 9),\n" +
                    "(2, 'Eve', 'Cat', 'Shorthair', 'Medium', 7, 6, 6),\n" +
                    "(3, 'Charlie', 'Dog', 'Labrador', 'Hard', 5, 4, 3)";
            
            dbManager.establishConnection();
            Statement statement = conn.createStatement();
            //drop if exists
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, "PETS", null);
            if(rs.next())
                statement.execute("DROP TABLE PETS");
            
            //create and insert
            dbManager.updateDB(createBook);
            dbManager.updateDB(insertBook);
            
        } catch (SQLException ex) {
            Logger.getLogger(VirtualPetsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createOwners()
    {
        try {
            String createOwners = "CREATE TABLE OWNERS (ownerID INT, username VARCHAR(50), password VARCHAR(50), petID INT)";
            Statement statement = conn.createStatement();
            //drop if exists
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, "OWNERS", null);
            if(rs.next())
                statement.execute("DROP TABLE OWNERS");
            
            dbManager.updateDB(createOwners);
        } catch (SQLException ex) {
            Logger.getLogger(VirtualPetsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void insertOwner(String username, String password, int petID)
    {
        if(petID > 0 && petID < 4)
        {
            try {
                String selectWeek = "SELECT ownerID FROM owners";
                
                Statement statement = conn.createStatement();
                Set<Integer> idSet = new LinkedHashSet<>();
                ResultSet rs = statement.executeQuery(selectWeek);
                int ownerID = 1;
                while(rs.next())
                {
                    int id = rs.getInt(1);
                    ownerID++;
                    idSet.add(id);
                }
                
                while(!idSet.add(ownerID))
                {
                    ownerID++;
                }
                String insertOwner = "INSERT INTO OWNERS VALUES "
                        + "(" +ownerID+ ", '" +username+ "', '" +password+ "', " +petID+ ")";
                dbManager.updateDB(insertOwner);
                
            } catch (SQLException ex) {
                Logger.getLogger(VirtualPetsDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            System.err.println("petID is not in range");
    }
    
        public static ResultSet getPetsInfo() {
        ResultSet rs = null;
        try {
            String selectPets = "SELECT name, happy, food, clean, diff FROM pets";
            
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(selectPets);
            
            while(rs.next())
            {
                String name = rs.getString(1);
                int happy = rs.getInt(2);
                int food = rs.getInt(3);
                int clean = rs.getInt(4);
                String diff = rs.getString(5);
                
                System.out.println(name+ "\nHappy: " +happy+ "\tFood: " +food+ "\tClean: " +clean+ "\tDiff: " +diff);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VirtualPetsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
