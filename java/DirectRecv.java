import java.io.*;
import com.rabbitmq.client.*;

public class DirectRecv {
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
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, 
                                "direct"); // The type of an exchange

        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1) {
            System.err.println("Usage: DirectRecv [info] [warning] [error]");
            System.exit(1);
        }

        for (String severity : argv) {
            channel.queueBind(queueName,    // 
                            EXCHANGE_NAME,  // 
                            severity);      // the name of key (info, warning, error)
        }

        System.out.println(" [*] Waiting for messages. To Exit press CONTROL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + ": " + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}
