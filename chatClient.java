package com.span.bfs.chatapp;

import java.net.*;
import java.io.*;
import java.awt.*;

public class chatClient extends Frame implements Runnable
{
	private static final long serialVersionUID = 1L;
	Socket soc;    
    TextField tf;
    TextArea ta;
    Button btnSend,btnClose;
    String sendTo;
    String LoginName;
    Thread t=null;
    DataOutputStream dout;
    DataInputStream din;
    
    // Constructor to set the login name and the name to chatwith
    chatClient(String LoginName,String chatwith) throws Exception
    {
        super(LoginName);
        this.LoginName=LoginName;
        sendTo=chatwith;
        
        // frames to create UI
        tf=new TextField(50);
        ta=new TextArea(50,50);
        btnSend=new Button("Send");
        btnClose=new Button("Close");
        
        //connect to server using localhost as localport and 5222 as port 
        soc=new Socket("localhost",5222);

        // create stream for sending and recieving messages nad connection
        din=new DataInputStream(soc.getInputStream()); 
        dout=new DataOutputStream(soc.getOutputStream());        
        dout.writeUTF(LoginName);

        t=new Thread(this);
        t.start();

    }
    
    //method to create UI usinf jFrames
    void setup()
    {
        setSize(600,400);
        setLayout(new GridLayout(2,1));

        add(ta);
        Panel p=new Panel();
        
        p.add(tf);
        p.add(btnSend);
        p.add(btnClose);
        add(p);
        show();        
    }
    
    //action method To send And to logout
    public boolean action(Event e,Object o)
    {
        // to send message to the connected client
        if(e.arg.equals("Send"))
        {
            try
            {
                dout.writeUTF(sendTo + " "  + "DATA" + " " + tf.getText().toString());            
                ta.append("\n" + LoginName + " Says:" + tf.getText().toString());    
                tf.setText("");
            }
            catch(Exception ex)
            {
            }    
        }
        
        // Logout 
        else if(e.arg.equals("Close"))
        {
            try
            {
                dout.writeUTF(LoginName + " LOGOUT");
                System.exit(1);
            }
            catch(Exception ex)
            {
            }
            
        }
        
        return super.action(e,o);
    }
    public static void main(String args[]) throws Exception
    {   
    // creating a client , send the login name to connectwith name as command line arguement
        chatClient Client1=new chatClient(args[0],args[1]);
        // creating the UI
        Client1.setup();                
    }    
    
        public void run()
    {        
        while(true)
        {
            try
            {
                ta.append( "\n" + sendTo + " Says :" + din.readUTF());
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}

