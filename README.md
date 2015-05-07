jconfig - basic key value store
=======

JConfig is a small webapp that will let you store key value pairs in memeory, sql or redis.
It comes with a Java client as well as a command line tool.
It is intended for small config files and such.

To start the server with default settings (in-memory mode, port 8080, etc):
```bash
$ mvn clean package
$ java -jar server/target/jconfig-server.jar
```

To use the cli with default server settings (localhost:8080)
```bash
$ java -jar client/target/jconfig-client.jar -k my_key -v '{"json":"here"}'
$ java -jar client/target/jconfig-client.jar -k my_key -s
{"json":"here"}
$
```

There is also a higher level bash client for convenient modification of a value.
`client/src/main/resources/jconfig my_key` will open a file in vim with any content associated to the key. 
On exit the file content will be saved.
