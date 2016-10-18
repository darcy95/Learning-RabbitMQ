import java.io.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("../conn.db"));

        String host = "";
        String user = "";
        String pass = "";
        String virt = "";
    
        try {
            String line = "";

            while ((line = br.readLine()) != null) {
                if (line.contains("HOST: ")) {
                    host = line.replaceFirst("HOST: ", "").trim();
                } else if (line.contains("USER: ")) {            
                    user = line.replaceFirst("USER: ", "").trim();
                } else if (line.contains("PASS: ")) {            
                    pass = line.replaceFirst("PASS: ", "").trim();
                } else if (line.contains("VIRT: ")) {            
                    virt = line.replaceFirst("VIRT: ", "").trim();
                }
            }
        
        } finally {
            br.close();
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(user);      // guest (default)
        factory.setPassword(pass);      // guest (default)
        factory.setVirtualHost(virt);   // "/" (default)
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, // The name of the queue
                            true,  // Message durability: if this is set to true, a message won't be deleted until the delivery is acked by a consumer
                            false, 
                            false, 
                            null);

        String message = "Hello World!";

        channel.basicPublish("", 
                            QUEUE_NAME, // The name of the queue
                            MessageProperties.PERSISTENT_TEXT_PLAIN, // marking this message persistent (since Message durability is set above). Otherwise, null.
                            message.getBytes("UTF-8"));

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
