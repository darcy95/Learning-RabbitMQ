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

clean:
	rm -rf *.class
