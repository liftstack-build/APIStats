package com.apistats.lift {
  package snippet {

    import com.apistats.lift.record.APIStatsMessageDoc
    import scala.xml.NodeSeq
    import net.liftweb._
    import util.Helpers._
    import common._

    object InitStats {
      
      def totalCount():NodeSeq = {
        <strong>{APIStatsMessageDoc.numberOfTotalMessages}</strong>
      }
      
      def percentageGeospatial():NodeSeq = {
        val percentageGeospatial = (APIStatsMessageDoc.numberOfTotalGeospatialMessages / APIStatsMessageDoc.numberOfTotalMessages)* 100
        println("% Geospatial: " + percentageGeospatial)
        <span>{percentageGeospatial}</span>
      }
            
    }
  }
}