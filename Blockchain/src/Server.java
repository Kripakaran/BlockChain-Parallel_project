import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	//initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataOutputStream out       =  null;
 
    // constructor with port
    public Server(int port, String data)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
 
            // takes input from the client socket
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(data);
            
            System.out.println("Closing connection");
 
            // close connection
            socket.close();
            server.close();
            out.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
	
}
