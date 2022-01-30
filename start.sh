docker stop rabbit || true && docker rm rabbit || true
docker stop mongo || true && docker rm mongo || true

wait $!

docker run -d --hostname my-rabbit --name rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
docker run -d --name mongo -p 27017:27017 \
-e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
-e MONGO_INITDB_ROOT_PASSWORD=secret \
mongo:latest
