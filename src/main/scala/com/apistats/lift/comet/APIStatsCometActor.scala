package com.apistats.lift.comet

import net.liftweb._
import http._
import util._
import Helpers._
import scala.xml.NodeSeq

class APIStatsCometActor extends CometActor with CometListener {

  private var apiMessagesCount: Long = 0

  def registerWith = APIStatsLiftActor

  override def lowPriority = {
    case count: Long => apiMessagesCount = count; reRender()
  }

  def render = "#apicount *" #> apiMessagesCount

}