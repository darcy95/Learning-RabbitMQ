import java.io.*;
//import com.rabbitmq.client.*;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RPCServer {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        return n * fib(n - 1);
    }

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

        // This is a queue, not an exchange!
        channel.queueDeclare(RPC_QUEUE_NAME, 
                            false, // Message durability: if this is set to true, a message won't be deleted until the delivery is acked by a consumer
                            false, //
                            false, //
                            null); //

        channel.basicQos(1); // The queue ensures that a worker deals with only 1 task at a time (unless the worker sends an ACK, queue won't deliver any further task to the worker)

        QueueingConsumer consumer = new QueueingConsumer(channel); // This is new from the previous tutorials
        channel.basicConsume(RPC_QUEUE_NAME, 
                            false, // disabling the automatic acknowledgement. Used together with channel.basicQos(n) (true: auto-ack, false: no auto-ack)
                            consumer);

        System.out.println(" [x] Waiting for RPC requests");

        while (true) {
            String response = null;

            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            BasicProperties props = delivery.getProperties();
            BasicProperties replyProps = new BasicProperties.Builder().correlationId(props.getCorrelationId()).build();

            try {
                String message = new String(delivery.getBody(), "UTF-8");
                int n = Integer.parseInt(message);

                System.out.println(" [.] fib(" + message + ")");
                response = "" + fib(n);
            } catch (Exception e) {
                System.out.println(" [.] " + e.toString());
                response = "";
            } finally {
                channel.basicPublish("", // An exchange name here when used. 
                                    props.getReplyTo(), // In previous tutorials, a queue name was written here when using a queue in Send/Recv tutorial. And, routingKey was here when using an exchange.
                                    replyProps, // FYI, often set to null in previous tutorials
                                    response.getBytes("UTF-8"));
            
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        }
    }
}
