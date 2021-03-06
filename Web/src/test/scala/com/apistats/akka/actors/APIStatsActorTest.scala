package com.apistats.akka.actors

import akka.actor.Actor._
import akka.testkit.TestKit
import akka.util.duration._
import com.apistatsmodel.messages.APIStatsMessage
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ BeforeAndAfterAll, WordSpec }
import scala.collection.mutable.LinkedHashMap

@RunWith( classOf[JUnitRunner] )
class APIStatsActorTest extends WordSpec with BeforeAndAfterAll with ShouldMatchers with TestKit {

  val statsRef = actorOf( new APIStatsActor ).start

  override protected def afterAll() : Unit = {
    stopTestActor
    statsRef.stop
  }

  "An APIStatsActor" should {
    "respond to test message " in {
      within( 500 millis ) {
        statsRef ! "test"
        expectMsg( "Test OK" )
      }
    }
    "respond with the URL of the sent APIStatsMessage " in {
      within( 500 millis ) {
        val message = new APIStatsMessage( "Census API - By Coordinates", "broadbandmap", "http://www.broadbandmap.gov/broadbandmap/census/block?latitude=42.456&longitude=-74.987&format=json", LinkedHashMap( "geographyType" -> "block" ),
          LinkedHashMap( "latitude" -> "42.456", "longitude" -> "-74.987", "format" -> "json" ), new DateTime(), 23, true, "", false )
        assert( APIStatsActor.getMessageURL( message ) === "http://www.broadbandmap.gov/broadbandmap/census/block?latitude=42.456&longitude=-74.987&format=json" )
      }
    }
  }

}