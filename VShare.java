
package vshare;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author Vedant_1996
 */
public class VShare {

    /**
     * @param args the command line arguments
     */
    private static final int SEND = 1;
    private static final int RECIEVE = 2;
    private static final int EXIT = 3;
    private static final Scanner in = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        // TODO code application logic here 
        int choice;
        do
        {
            System.out.println("***Menu***");
            System.out.println("***Choose a option***");   
            System.out.println("1. Send");
            System.out.println("2. Recieve");
            System.out.println("3. Exit");
            System.out.print("Enter your choice : ");
            choice = in.nextInt();     
            
            switch(choice)
            {
                case SEND :
                    System.out.print("Enter file name to be send : ");
                    String fileToSent = in.next();
                    new Sender(fileToSent).sendFile();
                    break;
                case RECIEVE :
                    System.out.print("Enter sender's IP address : ");
                    String senderIp = in.next();
                    new Reciever(senderIp).recieveFile();
                    break;
                case EXIT : break;

            }
        }while(choice!=EXIT);
    }
    
}
