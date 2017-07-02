
package vshare;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Vedant_1996
 */
public class Sender {
    private static final int PORT = 9000;//Port to which the application connects
    private static final int BUFF_SIZE = 1024;//1KB
    private static ServerSocket SERVER_SOCKET;
    private Socket clientSocket;
    private String fileName;
    
    public Sender(String fileName)
    {
        this.fileName = fileName; 
    }
    private void initServer() throws IOException
    {	
        SERVER_SOCKET = new ServerSocket(PORT);    
    }
    private void acceptClient() throws IOException
    {
        this.clientSocket = SERVER_SOCKET.accept();
    }
    private String getActualFileName(File file)
    {
        return file.getName();
    }
    public void sendFile() throws IOException
    {
    	System.out.println("Initializing server. . .");
    	initServer();
    	System.out.println("Server socket created successfully.");
        System.out.println("Waiting for reciever. . . ");
        acceptClient();
        System.out.println("Connection established successfully with "+clientSocket.getInetAddress());
        
        File file = new File(fileName);
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream sout = new BufferedOutputStream(clientSocket.getOutputStream());
        DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
        long fileSize = file.length();
        int current=0,bytesRead;
        byte[] buff = new byte[BUFF_SIZE];
        
        System.out.println("Sending file size. . .");
        dout.writeUTF(Long.toString(fileSize));
        System.out.println("File size sent.");
        System.out.println("Sending file name. . .");
        dout.writeUTF(getActualFileName(file));
        System.out.println("File name sent.");
        System.out.println("Reading data from file. . .");
        
        while(fileSize-current > 0)
        {
            bytesRead = fin.read(buff,0,BUFF_SIZE); 
            System.out.println("Bytes read : "+bytesRead);
            current = current + bytesRead;  
            sout.write(buff,0,bytesRead);
            sout.flush();
        }     
        System.out.println("File sent successfully!");
        fin.close();
        sout.close();
        dout.close();
        SERVER_SOCKET.close();
        clientSocket.close();
    }    
}
