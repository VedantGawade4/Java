/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vshare;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Vedant_1996
 */
public class Reciever {
    private static final int PORT = 9000;
    private static final int BUFF_SIZE = 1024;//1KB
    private Socket recvSocket;
    private String serverIp;
    private String fileName;
    private String workSpace = "C:\\Users\\Vedant_1996\\Desktop\\VShare\\";//Replace this with path of you choice
    private static final Scanner in = new Scanner(System.in);
    
    public Reciever(String serverIp)
    {
        this.serverIp = serverIp;
    }
    private void connect() throws IOException
    {
        recvSocket = new Socket(serverIp, PORT);
    }
    private boolean fileExists(String fileName)//Checks whether file exixts 
    {
        File fileToSave = new File(workSpace+fileName);
        System.out.println("fileToSave : "+fileToSave.getAbsolutePath());
        File[] list = new File(workSpace).listFiles();
        for(File file : list)
        {
            System.out.println("file : "+file.getAbsolutePath());
            if((fileToSave.getAbsolutePath()).equals(file.getAbsolutePath()))
            {
                return true;
            }
        }
        return false;
    }
    private String getNewName(String fileName)//Returns a new name for file, if it already exixts in a given directory[ img.jpg -> img(1).jpg ]. Else returns the input as it is.
    {
       // Pattern pattern = Pattern.compile("(.*?)(\\((\\d+)\\))?(\\.[^.]*)?");
        Pattern pattern = Pattern.compile("(\\w+)(\\((\\d+)\\))?(\\.[^.]*)?");
        if(fileExists(fileName))
        {
            System.out.println("File name matched");
            Matcher m = pattern.matcher(fileName);
            if(m.matches())
            {
                String prefix = m.group(1);
                String last = m.group(3);
                String suffix = m.group(4);
                if(suffix==null)
                {
                    suffix = " ";
                }
                int count = last!=null?Integer.parseInt(last):0;
                do
                {
                    count++;
                    fileName = prefix + "(" + Integer.toString(count) + ")" + suffix;
                }while(fileExists(fileName));
            }
        }
        return fileName;
    }
    public void recieveFile() throws IOException
    {
        System.out.println("Trying to connect "+serverIp+". . .");
        connect();
        System.out.println("Connected successfully to "+serverIp+"!");
        BufferedInputStream sin = new BufferedInputStream(recvSocket.getInputStream());
        DataInputStream din = new DataInputStream(recvSocket.getInputStream());
        int current=0,bytesRead;
        long fileSize;
        
        System.out.println("Reading file size. . .");
        fileSize = Long.parseLong(din.readUTF());
        System.out.println("File size recieved.");
        System.out.println("File size : "+fileSize);
        System.out.println("Reading file name. . .");
        String actualFileName = din.readUTF();
        System.out.println("File name recieved.");
        System.out.println("File name : "+actualFileName);
        System.out.println("File name(Before checking) : "+actualFileName);
        actualFileName = getNewName(actualFileName);
        System.out.println("File name(After checking) : "+actualFileName);   
        fileName = workSpace + actualFileName;
        File file = new File(fileName);
        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buff = new byte[BUFF_SIZE];
        System.out.println("Downloading file. . .");
        
        while(fileSize-current > 0)
        {
            bytesRead = sin.read(buff,0,BUFF_SIZE); 
            if(bytesRead!=-1)
            {
                System.out.println("Bytes read : "+bytesRead);
                current = current + bytesRead;  
                fout.write(buff,0,bytesRead);
                fout.flush();
            }
        }  
        System.out.println("File ready.");
        sin.close();
        din.close();
        fout.close();
        recvSocket.close();
    }
}
