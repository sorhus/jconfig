package com.github.sorhus.jconfig

import scala.io.Source

class JConfig(path: String = "/etc/jconfig/config") {

  val (host, port) = getHostAndPort

  def get(key: String): Option[String] = {
    try {
      val url = "http://%s:%s/%s".format(host, port, key)
      val result = Source.fromURL(url).mkString
      Some(result)
    } catch {
      case e => 
        println(host, port)
        println(e)
        None
    }
  }

  def getHostAndPort: (String, String) = {
    val config = readConfig(path)
    val host = config.get("HOST").get
    val port = config.get("PORT").get
    (host, port)
  }

  def readConfig(path: String): Map[String, String] = {
    Source.fromFile(path)
      .mkString
      .split("\n")
      .map{ line =>
        val Array(key, value) = line.split("=")
        key -> value
      }
      .toMap
  }

}
