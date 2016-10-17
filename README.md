Server environment:
- Ubuntu (x64) 14.04 LTS
- Memory: 2GB
- CPU: 1 core

Installation:
- sudo apt-get install rabbitmq-server
- sudo rabbitmq-plugins rammitmq_management
- sudo service rabbutmq-server restart

Creating users:
- sudo rabbitmqctl add_user <username> <password>
- sudo rabbitmqctl set_user_tags <username> <administrator|monitoring|policymaker|management>

Web management:
- http://localhost:15672
