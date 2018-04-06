import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	// initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream in     = null;
 
    // constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
 
            // sends output to the socket
            in    = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    
    public String fetchData() throws IOException{
        
    	String data = in.readUTF();
    	
        // close the connection
        try
        {
            in.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        return data;
    }

}
