package com.richard.akka.akka

/**
  * Created using Intellij IDE
  * Created by rnkoaa on 1/4/17.
  */

import java.io.File
import java.nio.file.Path

import akka.actor.{Actor, Props}
import akka.event.{Logging, LoggingAdapter}

import scala.collection.mutable.ListBuffer

class FolderScannerActor extends Actor {

  import FolderScannerActor._

  val log: LoggingAdapter = Logging.getLogger(context.system, this)

  var filesNumber = 0
  var responsesNumber = 0
  var words = new ListBuffer[String]

  def receive: PartialFunction[Any, Unit] = {
    case path: String =>
      log.info(s"Scanning $path")
      val directory = new File(path)
      val files = getFilesFromFolder(directory)
      filesNumber = files.size
      files.foreach(file => context.actorOf(FileReaderActor.props) ! file)
    case wordsList: List[String] =>
      log.info(s"New words are received $wordsList")
      responsesNumber += 1
      words insertAll(words.size, wordsList)
      if (filesNumber == responsesNumber) {
        writeResults(words.toList)
      }
    case _ => log.info("Nothing to scan...")
  }
}

object FolderScannerActor {

  def props = Props(new FolderScannerActor)

  def getFilesFromFolder(folder: File): List[File] = {
    if (folder.exists && folder.isDirectory) {
      println("FILES EXIT")
      folder.listFiles
        .toList
    }
    else {
      println("FILES DOES NOT EXIT")
      List[File]()
    }
  }

  def writeResults(words: List[String]): Path = {
    import java.nio.file.{Files, Paths}

    import scala.collection.JavaConverters._
    //Location where you want to write results
    val path = "data/result.txt"
    //val path = "/Users/rnkoaa/Downloads/results/result.txt"
    val resultPath = Paths.get(path)
    if (Files.exists(resultPath))
      Files.delete(resultPath)
    Files.createFile(resultPath)
    Files.write(Paths.get(path), words.asJava)
  }
}
