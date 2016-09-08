package shared;
import com.mycompany.ca1.server.ClientHandler;
import com.mycompany.ca1.server.EchoServer;
import com.mycompany.ca1.server.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProtocolStrings
{
    
    private Socket socket;
    private PrintWriter writer;
    private String username = "";
    private Scanner input; 
    private String message;
    
    public ProtocolStrings(Socket socket) throws IOException
    {
        input =  new Scanner(socket.getInputStream());
        this.socket = socket;
    }
    
    
    

  
    public static enum ARGS
    {
        STOP, LOGIN, MSG, LOGOUT, CLIENTLIST, MSGRESP;
    }

    public final String STOP = "STOP";

    public void message(String username)
    {  
        message = input.nextLine(); //IMPORTANT blocking call
        while (!message.equals(ProtocolStrings.ARGS.LOGOUT + ":")) {
                String msg[] = message.split(":");
                String[] names;
                if (msg[0].equalsIgnoreCase("LOGIN")) {
               //    writer.println("MSGRES:SERVER:Already Logged in");
                } else if (msg[0].equalsIgnoreCase("MSG")) {
                    if (msg[1].equals("")) {
                        names = new String[EchoServer.clients.size()];
                        for (int i = 0; i < EchoServer.clients.size(); i++) {
                            if (!EchoServer.clients.get(i).getUsername().equals(username)) {
                                names[i] = EchoServer.clients.get(i).getUsername();
                            }
                        }
                    } else {
                        names = msg[1].split(",");
                        if (names.length == 0) {
                            String[] tmp = {msg[1]};
                            names = tmp;
                        }
                    }
                    for (String name : names) {
                        for (ClientHandler client : EchoServer.clients) {
                            if (client.getUsername().equals(name)) {
                                client.sendMessage(ProtocolStrings.ARGS.MSGRESP + ":" + username + ":" + msg[2]);
                            }
                        }
                    }
                }
                //writer.println(message.toUpperCase());
                //System.out.println(String.format("Received the message: %1$S ", message.toUpperCase()));
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
                message = input.nextLine(); //IMPORTANT blocking call
            }
    }

    public String login()
    {


        while (username.equalsIgnoreCase(""))
        {
            message = input.nextLine(); //IMPORTANT blocking call
            String msg[] = message.split(":");
            if (msg[0].equalsIgnoreCase("LOGIN") && msg.length > 1)
            {
                username = msg[1];  
            }
           
        }

        return username;
    }

    public String logout()
    {
        return "Logged out!";
    }

    public String clientList()
    {
        return "";
    }

    public String msgRes()
    {
        return "";
    }

    public String sendMessage(ARGS args, String msg)
    {
        if (args.equals(ARGS.MSGRESP))
        {

        }
        return args + ":" + msg;
    }

    public void sendMessage(String message)
    {
        System.out.println(""+message);
        writer.println(message);
    }

  
    private boolean usernameExists(String username)
    {
        for (ClientHandler client : EchoServer.clients)
        {
            if (username.equalsIgnoreCase(username))
            {
                return true;
            }
        }
        return false;
    }

}
