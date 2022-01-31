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

## Web Socket Session Flow

1. This app will provide sockets over `http:<host>/sessions`. The VueJS client will connect to these sockets via SockJS. An example would be `ws://localhost:8000/sessions/557/o2khgpne/websocket`
2. These connections are first initiated via handshake  before actual web socket connections are established. See `AppHttpSessionHandshakeInterceptor.java` 
3. The VueJS client also subscribe to the topic `<host>/topic/*`
4. When web socket sessions are established, a document of the web socket session is created in MongoDB.