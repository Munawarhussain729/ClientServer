
package com.mycompany.tcp_server;

import static com.mycompany.tcp_server.TCP_Server.count;
import static com.mycompany.tcp_server.TCP_Server.portNumber;
import static com.mycompany.tcp_server.TCP_Server.serverSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCP_Server {
    static int count = 0;
    static int portNumber = 5005;
    static ServerSocket serverSocket = null;
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for the client on port: " + portNumber);
            serverSocket = new ServerSocket(portNumber);
            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Got connection From " + clientSocket.getRemoteSocketAddress().toString());
                count++;
                System.out.println("Now Active Connections are: " + count);
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (IOException ex) {
            System.out.println("Something Went Wrong During Connection");
            Logger.getLogger(TCP_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
            System.out.println("Something Went Wrong During Connection");
        }
    }
}

class ClientHandler extends Thread{
    Socket clientSocket;
    BufferedReader clientMessage;  
    DataOutputStream serverMessage;
    String messageLine = null;
    public ClientHandler(Socket client){
        clientSocket = client;
    }
    @Override
    public void run(){
        try {
                String greeting = "Welcome to Server " + serverSocket.getLocalSocketAddress().toString();
                   
                clientMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                serverMessage = new DataOutputStream(clientSocket.getOutputStream());
                serverMessage.writeBytes("Welcome to Server" + serverSocket.getLocalSocketAddress().toString() + '\n');
                serverMessage.writeBytes("Enter save to Save Data and read to read Data from server" + '\n');
             
                messageLine = clientMessage.readLine();
                                

                if(messageLine.toLowerCase().compareTo("save") == 0){
                    
                    serverMessage.writeBytes("Write what you want to save" + '\n');
                    messageLine = clientMessage.readLine() + '\n';
                    String ip_address = clientSocket.getInetAddress().toString();
                    ip_address = ip_address.replaceFirst("/", "");                
                    String fileName = ip_address + ".txt";
                    String pathToDir = System.getProperty("user.dir") + "\\" + fileName;
                    File file = new File(pathToDir);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
                    writer.write(" " + messageLine);
                    writer.close();
                    serverMessage.writeBytes("Information save for " + ip_address + '\n');
                    
                }else if (messageLine.toLowerCase().compareTo("read") == 0){
                    String ip_address = clientSocket.getInetAddress().toString();
                    ip_address = ip_address.replaceFirst("/", "");                
                    String fileName = ip_address + ".txt";
                    String pathToDir = System.getProperty("user.dir") + "\\" + fileName;
                    System.out.println(pathToDir);
                    File file = new File(pathToDir);

                    if(file.exists()){
                        serverMessage.writeBytes("Client Data " + '\n');
                        BufferedReader fileData = new BufferedReader(new FileReader(file));
                        String str2=fileData.readLine();
                        fileData.close();
                        System.out.println(str2);
//                        String ip2 = serverSocket.getLocalSocketAddress().toString();
//                        String ip3=ip2.substring(0,ip2.indexOf("/"));
//                        String filename=ip3+"_"+ip_address +".txt";
//                        serverMessage.writeBytes(filename + '\n');
                        serverMessage.writeBytes(str2 + '\n');
                    }else{
                        serverMessage.writeBytes("Nothing Found"+'\n');
                        System.out.println("Nothing Found");
                    }
                }
                else{
                    serverMessage.writeBytes("Breaking connection Due to invalid input" +'\n');
                }
                clientMessage.close();
                serverMessage.close();
                clientSocket.close();
            } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally
        {
          count--;
          if(count==0){
              System.out.println("Waiting for clients on port:" + portNumber);
            }
        }
    }
}