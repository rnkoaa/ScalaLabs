package com.richard.akka.akka

import akka.actor.ActorSystem

/**
  * Created using Intellij IDE
  * Created by rnkoaa on 1/4/17.
  */
object MainApp extends App {
  println("Started.")
  val system = ActorSystem.create("file-reader")
  val scanner = system.actorOf(FolderScannerActor.props, "scanner")
  val directoryPath = getClass.getResource("/a-words").getPath

  scanner ! directoryPath
}
