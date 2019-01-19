/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author riddhi
 */
public class Database {
    
    private Connection connection;
    public Database()
    {
        try {
            String url = "jdbc:mysql://localhost/chatting?user=root&password=Liverolin7";
            connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<Client> getClients()
    {
        try {
            ArrayList<Client> clients = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from login");
            while(rs.next())
            {
                String username = rs.getString(1);
                String password = rs.getString(2);
                Client client = new Client(username,password);
                clients.add(client);
            }
            return clients;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public Client registerClient(String username,String password)
    {
        Client client = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into login values(?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2,password);
            pstmt.executeUpdate();
            client = new Client(username,password);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
    }
}
