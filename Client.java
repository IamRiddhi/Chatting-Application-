/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author riddhi
 */
public class Client 
{
    private String username,password;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Scanner sc;
    private PrintWriter pw;

    public Scanner getScanner() {
        return sc;
    }

    public void setScanner(Scanner sc) {
        this.sc = sc;
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    public void setPrintWriter(PrintWriter pw) {
        this.pw = pw;
    }
    
    public Client(String username,String password)
    {
        this.username = username;
        this.password = password;
    }
    public void sendMessage(String str)
    {
        pw.println(str);
    }
    public String getUsername() {
        return username;
    }
    public String toString()
    {
        String s = username;
        if(socket!=null)
            s += "(Online)";
        else
            s+= "(Offline)";
        return s;
    }
    public Socket getSocket() {
        return socket;
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void close()
    {
        try {
            socket.close();
        } catch (IOException ex) {
        }
        finally{
            Client.this.socket = null; 
            Client.this.in = null; 
            Client.this.out = null; 
            Client.this.sc = null; 
            Client.this.pw = null;
            ServerFrame.frame.lstChatters.repaint();
            ServerFrame.frame.taChats.append(username+" left the chat room.\n");
            ServerFrame.frame.broadcast("F:"+username);
        }
    }
    public static Client searchByName(String name)
    {
        ArrayList<Client> clients = ServerFrame.frame.clients;
        for(Client client:clients)
            if(client.getUsername().equals(name))
                return client;
        return null;
    }
     public void handle(Socket socket)
    {
        try {
            ServerFrame.frame.broadcast("O:"+username);
            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            sc = new Scanner(in);
            pw = new PrintWriter(out,true);
            Thread t = new Thread(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            while(true)
                            {
                                String request = null;
                                try{
                                    request = sc.nextLine();
                                }
                                catch(NoSuchElementException ex)
                                {
                                    close();
                                    break;
                                }
                                if(request.charAt(0)=='T') //logout
                                {
                                    close();
                                    break;
                                }
                                else if(request.charAt(0)=='C')//public chatting
                                {
                                    String chat = request.substring(2);
                                    String message = "C:" + username + ":" + chat;
                                    ServerFrame.frame.broadcast(message);
                                    ServerFrame.frame.taChats.append(username +": "+chat+"\n");
                                }
                                else if(request.charAt(0)=='R') //client 1(sender) requests for private chat
                                {
                                    String client2uname = request.substring(2);
                                    Client client = searchByName(client2uname);
                                    client.getPrintWriter().println("Q:"+username);
                                    ServerFrame.frame.taChats.append(username +" requested "+client2uname +" for a private chat.\n");
                                }
                                else if(request.charAt(0)=='Q') //client 2(receiver) sends back response whether he accepts the private chat request by client 1.
                                {
                                    String[] req = request.split(":");
                                    String client1uname = req[1];
                                    Client client1 = searchByName(client1uname);
                                    int isAccept = Integer.parseInt(req[2]);
                                    if(isAccept==0) //not accepted
                                        ServerFrame.frame.taChats.append(username+" rejected private chat request of "+client1uname+".\n");
                                    else //accepted
                                        ServerFrame.frame.taChats.append(username+" accepted private chat request of "+client1uname+".\n");
                                    client1.pw.println("R:"+username+":"+isAccept);
                                }
                                else if(request.charAt(0)=='P') //private chat
                                {
                                    int p = request.indexOf(':',2);
                                    String toClientStr = request.substring(2,p);
                                    String message = request.substring(p+1);
                                    Client toClient = searchByName(toClientStr);
                                    toClient.getPrintWriter().println("P:"+username+":"+message);
                                }
                                else if(request.charAt(0)=='D') //private chat disconnect
                                {
                                    String toClientStr = request.substring(2);
                                    Client toClient = searchByName(toClientStr);
                                    toClient.getPrintWriter().println("D:"+username);
                                    ServerFrame.frame.taChats.append(username+" ended private chat with "+ toClientStr+".\n");
                                }
                                else if(request.charAt(0)=='A') // get all logged in users for user who have just logged in.
                                {
                                    int no = 0;
                                    String resp = "";
                                    for(Client client:ServerFrame.frame.clients)
                                        if(client.getSocket()!=null)
                                        {
                                            resp += ("\nA:"+client.getUsername());
                                            no++;
                                        }
                                    pw.println("A:"+no+resp);
                                }
                            }
                        }

                    }
            );
            t.start();
           } catch (IOException   ex) {
               close();
        }
        
        
    }
    
}
