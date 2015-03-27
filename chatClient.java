package com.span.bfs.chat;


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
/**
 * Client Class to create connection with Server and start thr chat
 * */
public class chatClient extends Frame implements Runnable
{
	private static final long serialVersionUID = 1L;
    
    //initialize the variables
    Socket soc;    
    TextField tf;
    TextArea ta;
    TextArea tb;
    Button btnSend,btnClose;
    String sendTo;
    String LoginName;
    Thread t=null;
    DataOutputStream dout;
    DataInputStream din;
    public List s=new ArrayList();
    private static String tologin;
    
    chatClient(String LoginName,String chatwith) throws Exception
    {
        super(LoginName);
        this.LoginName=LoginName;
        sendTo=chatwith;
        tf=new TextField(50);
        ta=new TextArea(50,50);
        // tb=new TextArea("Online List : \n",50,50);
        btnSend=new Button("Send");
        btnClose=new Button("Close");
         soc=new Socket("localhost",Integer.parseInt(tologin));
        din=new DataInputStream(soc.getInputStream()); 
        dout=new DataOutputStream(soc.getOutputStream());        
        dout.writeUTF(LoginName);
	// Create new thread for the client and start the thread
        t=new Thread(this);
        t.start();
        
    }
    
    //Default Constucotr
    public chatClient() {
	}

    
    // Method to create GUI
    void setup()
    {
        setSize(800,500);
        setLayout(new GridLayout(2,2));

        add(ta);
        add(tb);
        Panel p=new Panel();
        p.add(tf);
        p.add(btnSend);
        p.add(btnClose);
        add(p);
        show();    
//    	tb.append("\n");
//    	tb.append(sendTo);
    }

    public boolean action(Event e,Object o)
    {
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
    tologin = JOptionPane.showInputDialog("Enter Port number");
    String tologin = JOptionPane.showInputDialog("Enter login name");
    String tochatwith = JOptionPane.showInputDialog("Enter name to chat with");
    chatClient Client1=new chatClient(tologin,tochatwith);
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

