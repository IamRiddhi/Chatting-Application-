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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author riddhi
 */
public class ChatWindowFrame extends javax.swing.JFrame {

    /**
     * Creates new form ChatWindowFrame
     */
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Scanner sc;
    private PrintWriter pw;
    private String username;
    private boolean loggedOut;
    private ArrayList<PrivateChatter> privateChatters = new ArrayList<>();
    private DefaultListModel<String> model = new DefaultListModel<>();
    public void removeChatter(String username)
    {
        for(PrivateChatter pc:privateChatters)
            if(pc.getUsername().equals(username))
            {
                privateChatters.remove(pc);
                break;
            }    
    }
    public void close()
    {
        try {
            socket.close();
        } catch (IOException ex) {
        }
        finally{
            socket = null; 
            in = null; 
            out = null; 
            sc = null; 
            pw = null;
            username="";
        }
    }
    public ChatWindowFrame(Socket socket,String username) {
        initComponents();
        lstOnlineUsers.setModel(model);
        try {
            this.socket = socket;
            this.username = username;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            sc = new Scanner(in);
            pw = new PrintWriter(out,true);
            setTitle(username);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    while(true)
                    {
                       String response = "";
                       try
                       {
                            response = sc.nextLine();
                        }
                        catch(NoSuchElementException ex)
                        {
                            close();
                            if(loggedOut)
                            {
                                 JOptionPane.showMessageDialog(rootPane, "You have been logged out","Logout Successful!" , JOptionPane.INFORMATION_MESSAGE);
                                 new ClientLogin().setVisible(true);
                            }  
                            else
                                JOptionPane.showMessageDialog(rootPane, "Server Disconnected","Closing the Application!" , JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        char ch = response.charAt(0);
                        switch(ch)
                        {
                            case 'A':   int no = Integer.parseInt(response.substring(2));
                                        for(int i=0;i<no;i++)
                                        {
                                            String uname = sc.nextLine().substring(2);
                                            if(!uname.equals(username))
                                                model.addElement(uname);
                                        }
                                        break;
                            case 'O':  model.addElement(response.substring(2));
                                       break;
                            case 'F':  model.removeElement(response.substring(2));
                                       break;
                            case 'C':  int p = response.indexOf(':',2);
                                       taChats.append(response.substring(2,p)+ " -> "+response.substring(p+1)+"\n");
                                       break;
                            case 'P':  p = response.indexOf(':',2);
                                       String sender = response.substring(2,p);
                                       String message = response.substring(p+1);
                                       for(PrivateChatter pc : privateChatters)
                                           if(pc.getUsername().equals(sender))
                                           {
                                               pc.getFrame().receivedMessage(message);
                                               break;
                                           }
                                        break;
                            case 'Q'://private chat request
                                String uname = response.substring(2);
                                int option = JOptionPane.showConfirmDialog(rootPane, "You have received a private chat request from "+uname+".\n Do you want to accept?","New Private Chat Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if(option==JOptionPane.YES_OPTION)
                                {
                                    pw.println("Q:"+uname+":1");
                                    PrivateMessageWindow frame = new PrivateMessageWindow(username,uname, pw,ChatWindowFrame.this);
                                    privateChatters.add(new PrivateChatter(uname, frame));
                                    frame.setVisible(true);
                                }
                                else
                                    pw.println("Q:"+uname+":0");
                                break;
                            case 'R': //private chat request's response
                                    p = response.indexOf(':',2);
                                    uname = response.substring(2,p);
                                    int accept = Integer.parseInt(response.substring(p+1));
                                    if(accept==1)
                                    {
                                        PrivateMessageWindow frame = new PrivateMessageWindow(username,uname, pw,ChatWindowFrame.this);
                                        privateChatters.add(new PrivateChatter(uname, frame));
                                        frame.setVisible(true);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(rootPane, uname+" rejected your private chat request!", "Private Chat Request Rejected",JOptionPane.INFORMATION_MESSAGE);
                                    break;
                            case 'D': //private chat Disconnect request
                                    uname = response.substring(2);
                                    for(PrivateChatter pc:privateChatters)
                                        if(pc.getUsername().equals(uname))
                                        {
                                            privateChatters.remove(pc);
                                            pc.getFrame().dispose();
                                            break;
                                        }
                                    break;
                            case 'W': //warning
                                      uname = response.substring(2);
                                      if(uname.equals(username))
                                        JOptionPane.showMessageDialog(rootPane,"You are warned for violating our rules/policies and using abusive languages.", "WARNING",JOptionPane.WARNING_MESSAGE);
                                      break; 
                        }   
                    }
                }}
            );
            t.start();
            pw.println("A");
        } 
             catch (IOException ex) {
            Logger.getLogger(ChatWindowFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeading = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taChats = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstOnlineUsers = new javax.swing.JList<>();
        btnPrivateMessage = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblHeading.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        lblHeading.setText("CHAT ROOM");

        taChats.setColumns(20);
        taChats.setRows(5);
        jScrollPane1.setViewportView(taChats);

        jLabel2.setText("Chats");

        jLabel3.setText("Type Your Message");

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        jLabel4.setText("Online Users");

        jScrollPane2.setViewportView(lstOnlineUsers);

        btnPrivateMessage.setText("Private Chat");
        btnPrivateMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrivateMessageActionPerformed(evt);
            }
        });

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtMessage)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrivateMessage, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(84, 84, 84))
            .addGroup(layout.createSequentialGroup()
                .addGap(251, 251, 251)
                .addComponent(lblHeading)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblHeading)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrivateMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout)
                        .addGap(37, 37, 37))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String message = txtMessage.getText();
        pw.println("C:"+message);
        txtMessage.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        loggedOut = true;
        pw.println("T:");
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnPrivateMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrivateMessageActionPerformed
        // TODO add your handling code here:
        String username = lstOnlineUsers.getSelectedValue();
        for(PrivateChatter pc:privateChatters)
            if(pc.getUsername().equals(username))
            {
                pc.getFrame().requestFocus();
                return;
            }
        pw.println("R:"+lstOnlineUsers.getSelectedValue());
    }//GEN-LAST:event_btnPrivateMessageActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPrivateMessage;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHeading;
    private javax.swing.JList<String> lstOnlineUsers;
    private javax.swing.JTextArea taChats;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
