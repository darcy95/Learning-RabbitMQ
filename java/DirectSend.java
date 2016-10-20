import java.io.*;
import com.rabbitmq.client.*;

public class DirectSend {
    private static final String EXCHANGE_NAME = "direct_logs";

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
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setVirtualHost(virt);

        Connection connection = factory.newConnection();
        Channel channel =  connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, 
                                "direct"); // This is a type of an exchange. E.g., fanout, direct, etc.

        String severity = argv[0];
        String message = argv[1];

        channel.basicPublish(EXCHANGE_NAME, 
                            severity, // A message will only be routed to a queue which is binding with this key
                            null,
                            message.getBytes("UTF-8"));
        
        System.out.println(" [x] Sent '" + severity + ": " + message + "'");

        channel.close();
        connection.close();
    }
}
