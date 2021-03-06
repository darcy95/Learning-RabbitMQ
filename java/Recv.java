import java.io.*;
import com.rabbitmq.client.*;
import java.io.IOException;

public class Recv {
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
        factory.setUsername(user); // guest (default)
        factory.setPassword(pass); // guest (default)
        factory.setVirtualHost(virt); // "/" (default)

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, 
                            true, // Message durability
                            false, 
                            false, 
                            null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1); // This tells RabbitMQ not to give more than one message to a consumer at a time.

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

                try {
                    doWork(message);
                } finally {
                    System.out.println(" [x] The work is done!");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, 
                            false, // disabling the automatic acknowledgement. 
                            consumer);
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
