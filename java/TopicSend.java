import java.io.*;
import com.rabbitmq.client.*;

public class TopicSend {
    private static final String EXCHANGE_NAME = "topic_logs";

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
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, 
                                "topic"); // This is a type of an exchange. E.g., fanout, direct, topic ...
        String routingKey = argv[0];
        String message = argv[1];

        channel.basicPublish(EXCHANGE_NAME, 
                            routingKey, // So, here routing keys will be a wild-carded pattern rather than a single keyword used in 'direct' usecase
                            null,
                            message.getBytes("UTF-8"));

        System.out.println(" [x] Sent '" + routingKey + ": " + message + "'");

        channel.close();
        connection.close();
    }
}
