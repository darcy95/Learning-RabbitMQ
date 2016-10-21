RMQ_VERSION=3.6.5
RMQ_JAVA_BIN=rabbitmq-java-client-bin-${RMQ_VERSION}
JAVA_CP=.:./classes/common-io-1.2.jar:./classes/common-cli-1.1.jar:./classes/rabbitmq-client.jar

all:

install:
	wget http://www.rabbitmq.com/releases/rabbitmq-java-client/v${RMQ_VERSION}/${RMQ_JAVA_BIN}.tar.gz
	tar zxvfp ${RMQ_JAVA_BIN}.tar.gz
	mv rabbitmq-java-client-bin-${RMQ_VERSION} classes
	rm -rf ${RMQ_JAVA_BIN}.tar.gz

hello-send: Send.class
	@java -cp ${JAVA_CP} Send Hello.
	@java -cp ${JAVA_CP} Send World..
	@java -cp ${JAVA_CP} Send Hi...
	@java -cp ${JAVA_CP} Send Welcome....
	@java -cp ${JAVA_CP} Send to.....
	@java -cp ${JAVA_CP} Send Matrix......

Send.class:
	javac -cp ./classes/rabbitmq-client.jar Send.java

hello-recv: Recv.class
	@java -cp ${JAVA_CP} Recv

Recv.class:
	javac -cp ./classes/rabbitmq-client.jar Recv.java

fanout-send: FanoutSend.class
	@java -cp ${JAVA_CP} FanoutSend 1.Hello
	@java -cp ${JAVA_CP} FanoutSend 2.World
	@java -cp ${JAVA_CP} FanoutSend 3.Rabbit
	@java -cp ${JAVA_CP} FanoutSend 4.Message
	@java -cp ${JAVA_CP} FanoutSend 5.Welcome
	@java -cp ${JAVA_CP} FanoutSend 6.to
	@java -cp ${JAVA_CP} FanoutSend 7.Matrix

FanoutSend.class:
	javac -cp ./classes/rabbitmq-client.jar FanoutSend.java

fanout-recv: RecvLog.class
	@java -cp ${JAVA_CP} FanoutRecv

FanoutRecv.class:
	javac -cp ./classes/rabbitmq-client.jar FanoutRecv.java

direct-send: DirectSend.class
	@java -cp ${JAVA_CP} DirectSend warning "Fire in the hole!"
	@java -cp ${JAVA_CP} DirectSend error "Run! Run! It will explode!"
	@java -cp ${JAVA_CP} DirectSend info "All ants are exterminated."
	@java -cp ${JAVA_CP} DirectSend warning "Oh, no! A giant zombie ant is attcking us."
	@java -cp ${JAVA_CP} DirectSend error "The commander is dead! The commander is dead! We are so doomed!!!"

DirectSend.class:
	javac -cp ./classes/rabbitmq-client.jar DirectSend.java

direct-recv-1: DirectRecv.class
	@java -cp ${JAVA_CP} DirectRecv error warning
	
direct-recv-2: DirectRecv.class
	@java -cp ${JAVA_CP} DirectRecv info warning

DirectRecv.class:
	javac -cp ./classes/rabbitmq-client.jar DirectRecv.java

topic-send: TopicSend.class
	@java -cp ${JAVA_CP} TopicSend "kern.critical" "A critical kernel error"
	@java -cp ${JAVA_CP} TopicSend "kitchen.critical" "A critical kitchen error"
	@java -cp ${JAVA_CP} TopicSend "livingroom.critical" "A critical living room error"
	@java -cp ${JAVA_CP} TopicSend "kern.boring" "A boring kernel error"
	@java -cp ${JAVA_CP} TopicSend "kern.typical" "A typical kernel error"

TopicSend.class:
	javac -cp ./classes/rabbitmq-client.jar TopicSend.java

topic-recv-1: TopicRecv.class
	@java -cp ${JAVA_CP} TopicRecv "kern.*"

topic-recv-2: TopicRecv.class
	@java -cp ${JAVA_CP} TopicRecv "kern.*" "*.critical"

TopicRecv.class:
	javac -cp ./classes/rabbitmq-client.jar TopicRecv.java

rpc-server: RPCServer.class
	@java -cp ${JAVA_CP} RPCServer

RPCServer.class:
	javac -cp ${JAVA_CP} RPCServer.java

rpc-client: RPCClient.class
	@java -cp ${JAVA_CP} RPCClient

RPCClient.class:
	javac -cp ${JAVA_CP} RPCClient.java

clean:
	rm -rf *.class
