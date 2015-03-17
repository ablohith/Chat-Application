package com.span.bfs.chatapp;

import java.net.*;
import java.util.*;
import java.io.*;

//Server Class
public class chatServer
{   // Vectors to store and display the clients name and port 
    static Vector ClientSockets;
    static Vector LoginNames;
    
    chatServer() throws Exception
    {
        // create socket using the port 5222
        ServerSocket soc=new ServerSocket(5222);
        ClientSockets=new Vector();
        LoginNames=new Vector();
        
        
        // accept the connection from client
        while(true)
        {    
            Socket CSoc=soc.accept();        
            AcceptClient obClient=new AcceptClient(CSoc);
        }
    }
    
    //Main method to start the server 
    public static void main(String args[]) throws Exception
    {
        chatServer ob=new chatServer();
        
        //display the logged in clients
        System.out.println(LoginNames);
    }

class AcceptClient extends Thread
{
    Socket ClientSocket;
    DataInputStream din;
    DataOutputStream dout;
   
    // constructor to create the connection
    AcceptClient (Socket CSoc) throws Exception
    {
        ClientSocket=CSoc;

        din=new DataInputStream(ClientSocket.getInputStream());
        dout=new DataOutputStream(ClientSocket.getOutputStream());
        
        String LoginName=din.readUTF();

        //display the logged in clients name
        System.out.println("User Logged In :" + LoginName);
        LoginNames.add(LoginName);
        ClientSockets.add(ClientSocket);    
        start();
    }

    public void run()
    {
        while(true)
        {
            
            try
            {   
                // send the message to other client
                String msgFromClient=new String();
                msgFromClient=din.readUTF();
                StringTokenizer st=new StringTokenizer(msgFromClient);
                String Sendto=st.nextToken();                
                String MsgType=st.nextToken();
                int iCount=0;
    
                // display the logged out connections
                if(MsgType.equals("LOGOUT"))
                {
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.elementAt(iCount).equals(Sendto))
                        {
                            LoginNames.removeElementAt(iCount);
                            ClientSockets.removeElementAt(iCount);
                            System.out.println("User " + Sendto +" Logged Out ...");
                            break;
                        }
                    }
    
                }
                else
                {
                    String msg="";
                    while(st.hasMoreTokens())
                    {
                        msg=msg+" " +st.nextToken();
                    }
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.elementAt(iCount).equals(Sendto))
                        {    
                            Socket tSoc=(Socket)ClientSockets.elementAt(iCount);                            
                            DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                            tdout.writeUTF(msg);                            
                            break;
                        }
                    }
                    
                    //to display if the client doesnt exist / client logged out
                    if(iCount==LoginNames.size())
                    {
                        dout.writeUTF("HTTP/1.1 404 Not Found");
                    }
                    else
                    {
                        
                    }
                }
                
                if(MsgType.equals("LOGOUT"))
                {
                    break;
                }

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            
            
        }        
    }
}
}
