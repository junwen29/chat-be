# Chat Backend

This app will be the backend to manage user sessions, chat rooms, chat messages.

## Backend App Infra

### RabbitMQ

```shell
docker run -d --hostname my-rabbit --name rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management 
```
### MongoDB

```shell
docker run -d --name mongo -p 27017:27017 \ 
-e MONGO_INITDB_ROOT_USERNAME=mongoadmin \ 
-e MONGO_INITDB_ROOT_PASSWORD=secret \ 
mongo:latest
```