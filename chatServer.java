package com.span.bfs.chat;


import java.net.*;
import java.util.*;
import java.io.*;

public class chatServer
{
    //initialize the variables
    static List ClientSockets;
    static List LoginNames;
     private static String tologin;
    
	chatServer() throws Exception
    {
        //create the socket in the port entered
        tologin = JOptionPane.showInputDialog("Enter Port number");
	//display in close 
	System.out.println("Server Started at port no." +tologin);
        ServerSocket soc=new ServerSocket(Integer.parseInt(tologin));
        ClientSockets=new ArrayList();
        LoginNames=new ArrayList();


        // while loop to accept multiple clients
        while(true)
        {    
            Socket CSoc=soc.accept();        
            AcceptClient obClient=new AcceptClient(CSoc);
        }
    }
    
    
    public static void main(String args[]) throws Exception
    {
        
        chatServer ob=new chatServer();
    }

class AcceptClient extends Thread
{
    Socket ClientSocket;
    DataInputStream din;
    DataOutputStream dout;
    AcceptClient (Socket CSoc) throws Exception
    {
        ClientSocket=CSoc;

        din=new DataInputStream(ClientSocket.getInputStream());
        dout=new DataOutputStream(ClientSocket.getOutputStream());
        
        String LoginName=din.readUTF();
        /*if(LoginNames.contains(LoginName))
        {
        	throw new nameexistException(LoginName);
        	System.out.println("User already Exist");
        }
        else
        {*/
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
                String msgFromClient=new String();
                msgFromClient=din.readUTF();
                StringTokenizer st=new StringTokenizer(msgFromClient);
                String Sendto=st.nextToken();                
                String MsgType=st.nextToken();
                int iCount=0;
    
                //display the logout message
                if(MsgType.equals("LOGOUT"))
                {
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.get(iCount).equals(Sendto))
                        {
                            LoginNames.remove(iCount);
                            ClientSockets.remove(iCount);
                            olName.remove(LoginNames);
                            System.out.println("User " + Sendto +" Logged Out ...");
                            break;
                        }
                    }
    
                }
                
                //display the Messages in server
                else
                {
                    String msg="";
                    while(st.hasMoreTokens())
                    {
                        msg=msg+" " +st.nextToken();
                    }
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.get(iCount).equals(Sendto))
                        {    
                            Socket tSoc=(Socket)ClientSockets.get(iCount);                            
                            DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                            tdout.writeUTF(msg);  
                            System.out.println(Sendto +" says : " +msg);
                            break;
                        }
                    }
                    
                    //display the offline message
                    if(iCount==LoginNames.size())
                    {
                        dout.writeUTF("I am offline");
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
