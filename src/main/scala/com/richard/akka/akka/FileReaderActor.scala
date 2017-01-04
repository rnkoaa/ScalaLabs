package com.richard.akka.akka

import akka.event.LoggingAdapter

/**
  * Created using Intellij IDE
  * Created by rnkoaa on 1/4/17.
  */

import java.io.File

import akka.actor.{Actor, PoisonPill, Props}
import akka.event.Logging

import scala.collection.mutable.ListBuffer
import scala.io.Source

class FileReaderActor extends Actor {

  val log: LoggingAdapter = Logging.getLogger(context.system, this)

  def receive: PartialFunction[Any, Unit] = {
    case f: File =>
      log.info(s"Reading file ${f.getName}")
      var words = new ListBuffer[String]
      Source.fromFile(f).getLines().foreach(line => words += line)
      sender() ! words.toList
      self ! PoisonPill
    case _ => log.info("Still waiting for a text file")
  }

}

object FileReaderActor {
  def props = Props(new FileReaderActor)
}
