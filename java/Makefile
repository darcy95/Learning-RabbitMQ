RMQ_VERSION=3.6.5
RMQ_JAVA_BIN=rabbitmq-java-client-bin-${RMQ_VERSION}
JAVA_CP=.:./classes/common-io-1.2.jar:./classes/common-cli-1.1.jar:./classes/rabbitmq-client.jar

all:

install:
	wget http://www.rabbitmq.com/releases/rabbitmq-java-client/v${RMQ_VERSION}/${RMQ_JAVA_BIN}.tar.gz
	tar zxvfp ${RMQ_JAVA_BIN}.tar.gz
	mv rabbitmq-java-client-bin-${RMQ_VERSION} classes
	rm -rf ${RMQ_JAVA_BIN}.tar.gz

hello:
	javac -cp ./classes/rabbitmq-client.jar Send.java Recv.java
	@java -cp ${JAVA_CP} Send
	@java -cp ${JAVA_CP} Send
	@java -cp ${JAVA_CP} Send
	@java -cp ${JAVA_CP} Recv

clean:
	rm -rf *.class