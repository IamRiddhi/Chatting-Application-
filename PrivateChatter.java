/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingapp;

import javax.swing.JFrame;

/**
 *
 * @author riddhi
 */
public class PrivateChatter {
    private String username;
    private PrivateMessageWindow frame;

    public PrivateChatter(String username, PrivateMessageWindow frame) {
        this.username = username;
        this.frame = frame;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PrivateMessageWindow getFrame() {
        return frame;
    }

    public void setFrame(PrivateMessageWindow frame) {
        this.frame = frame;
    }
    
    
    
    
}
