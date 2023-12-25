package newpackage;
import java.io.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    public static void main(String[]args){
        try {
            Socket clientSocket = new Socket("localhost",5005);
           
            DataOutputStream clientMessage = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String serverSent = serverMessage.readLine(); 
            System.out.println(serverSent); //Printing Greeting Message
            serverSent = serverMessage.readLine();
            System.out.println(serverSent); //Enter save or read
            String inputData = inputBuffer.readLine();
            
            clientMessage.writeBytes(inputData + '\n');
            if(inputData.toLowerCase().compareTo("save")== 0){
                
                serverSent = serverMessage.readLine();
                System.out.println(serverSent);
                inputData = inputBuffer.readLine();
                clientMessage.writeBytes(inputData + '\n');
                serverSent = serverMessage.readLine();
                System.out.println(serverSent); 
            }
            else if(inputData.toLowerCase().compareTo("read")== 0){
                String ipAddress = clientSocket.getLocalAddress().toString();
                ipAddress = ipAddress.replaceFirst("/", "");
                serverSent = serverMessage.readLine();
                
                if(serverSent.compareTo("Nothing Found"+ '\n') == 0){
                    System.out.println("Nothing Found To Print");
                }
                else{
                    System.out.println(serverSent);
                    serverSent = serverMessage.readLine();
                    System.out.println(serverSent);
//                    String fileName = serverSent;
//                    serverSent = serverMessage.readLine();
//                    String path = System.getProperty("user.dir") + "\\"+fileName;
//                    System.out.println(path);
//                    File file = new File(path);
//                    if(file.exists()){
//                        System.out.println("File Found");
//                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//                        writer.write(" "+serverSent);
//                    }
//                    else{
//                        System.out.println("File not Found");
//                    }
                }  
            }
            else{
                serverSent = serverMessage.readLine();
                System.out.println(serverSent);
            }
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex){
            System.out.println("Some thing Went Wrong in Client");
        }
    }
}
