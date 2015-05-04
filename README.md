JConfig
=======

JConfig is a small webapp that will let you store key value pairs in memeory, sql or redis. 
It comes with a Java client as well as a command line tool. 
It is intended for small config files and such.

To start the server with default settings (in-memory mode, port 8080, etc):
```bash
$ mvn clean package
$ java -jar server/target/jconfig-server-<version>-jar-with-dependencies.jar
```

To use the cli with default server settings (localhost:8080)
```bash
$ java -jar client/target/jconfig-client-<version>-jar-with-dependencies.jar -i my_id -j '{"json":"here"}'
$ java -jar client/target/jconfig-client-<version>-jar-with-dependencies.jar -i my_id
{"json":"here"}
$
```
