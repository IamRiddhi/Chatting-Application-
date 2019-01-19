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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

/**
 *
 * @author riddhi
 */
public class ServerFrame extends javax.swing.JFrame {

    /**
     * Creates new form ServerFrame
     */
    ArrayList<Client> clients = new ArrayList<>();
    public static ServerFrame frame;
    private DefaultListModel<Client> model = new DefaultListModel<>();
    public ServerSocket ss;
    public void broadcast(String msg)
    {
        for(Client client:clients)
            if(client.getSocket()!=null)
            {
                PrintWriter pw = client.getPrintWriter();
                pw.println(msg);
            }
    }
    public ServerFrame() {
            initComponents();
            btnDisconnect.setEnabled(false);
            btnWarn.setEnabled(false);
            Database database = new Database();
            clients = database.getClients();
            for(Client client:clients)
               model.addElement(client);
            lstChatters.setModel(model);
            try {
            ss = new ServerSocket(8189);
            Thread t = new Thread(
                new Runnable() {
                @Override
                public void run()
                {
                    while(true)
                    {
                        try {
                            Socket socket = ss.accept();
                            InputStream in = socket.getInputStream();
                            Scanner sc = new Scanner(in);
                            OutputStream out = socket.getOutputStream();
                            PrintWriter pw = new PrintWriter(out,true);
                            String inp = sc.nextLine();
                            String arr[] = inp.split(":");
                            String username = arr[1];
                            String password = arr[2];
                            boolean found = false;
                            if(arr[0].equals("L"))
                            {
                                for(Client c:clients)
                                    if(c.getUsername().equals(username) && c.getPassword().equals(password))
                                    {
                                        if(c.getSocket()!=null) // already logged in
                                        {
                                            pw.println("L:2");
                                            found = true;
                                            socket.close();
                                        }
                                        else
                                        {
                                            c.handle(socket);
                                            found = true;
                                            lstChatters.repaint();
                                            pw.println("L:1");
                                            taChats.append(username+" joined the chat room.\n");
                                        }
                                        break;
                                    }
                                if(!found)
                                {
                                  pw.println("L:0");
                                  socket.close();
                                }
                            }
                            else if(arr[0].equals("R"))
                            {
                                if(Client.searchByName(username)!=null)
                                    pw.println("R:0");
                                else
                                {
                                    Client client = database.registerClient(username, password);
                                    if(client==null)
                                        pw.println("R:0");
                                    else
                                    {
                                        client.handle(socket);
                                        clients.add(client);
                                        model.addElement(client);
                                        taChats.append(username+" Registered.\n");
                                        taChats.append(username+" joined the chat room.\n");
                                        pw.println("R:1");
                                    }
                                }
                            }
                            
                            
                            
                            
                        } catch (IOException ex) {
                            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            );
            t.start();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstChatters = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taChats = new javax.swing.JTextArea();
        btnWarn = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setText("S E R V E R");

        jLabel2.setText("Chat Users");

        lstChatters.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstChattersValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstChatters);

        jLabel3.setText("Chats");

        taChats.setColumns(20);
        taChats.setRows(5);
        jScrollPane2.setViewportView(taChats);

        btnWarn.setText("Warn");
        btnWarn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWarnActionPerformed(evt);
            }
        });

        btnDisconnect.setText("Disconnect");
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(27, 27, 27)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnWarn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel1)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(jLabel2)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnWarn)
                        .addGap(26, 26, 26)
                        .addComponent(btnDisconnect))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        try {
            // TODO add your handling code here:
            lstChatters.getSelectedValue().getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDisconnectActionPerformed

    private void btnWarnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWarnActionPerformed
        // TODO add your handling code here:
        Client client = lstChatters.getSelectedValue();
        String msg = "W:"+client.getUsername();
        taChats.append(client.getUsername()+" is warned.\n");
        broadcast(msg);
    }//GEN-LAST:event_btnWarnActionPerformed

    private void lstChattersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstChattersValueChanged
        // TODO add your handling code here:
        btnDisconnect.setEnabled(lstChatters.getSelectedValue().getSocket()!=null);
        btnWarn.setEnabled(lstChatters.getSelectedValue().getSocket()!=null);
    }//GEN-LAST:event_lstChattersValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new ServerFrame();
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnWarn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JList<Client> lstChatters;
    public javax.swing.JTextArea taChats;
    // End of variables declaration//GEN-END:variables
}
