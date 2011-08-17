package com.apistats.lift.record //import net.liftweb.mongodb.record.field.MongoJsonObjectListField
import com.apistatsmodel.messages.APIStatsMessage
import com.foursquare.rogue.Rogue._
import com.foursquare.rogue.LatLong
import java.util.Locale
import net.liftweb.mongodb.record.field._
import net.liftweb.mongodb.record.{MongoId, MongoMetaRecord, MongoRecord}
import net.liftweb.mongodb.JsonObject
import net.liftweb.record.field._
import org.joda.time.DateTime
import scala.collection.mutable.LinkedHashMap
class APIStatsMessageDoc private () extends MongoRecord[APIStatsMessageDoc] with MongoId[APIStatsMessageDoc] {

  def meta = APIStatsMessageDoc

  object apiName extends StringField(this, 100)

  object contextPath extends StringField(this, 20)

  object resourceURL extends StringField(this, 255)
  object pathParams extends BsonRecordListField[APIStatsMessageDoc, APIParam](this, APIParam)  
  object queryParams extends BsonRecordListField[APIStatsMessageDoc, APIParam](this, APIParam)

  object date extends DateField(this)

  object responseTime extends LongField(this);    object isGeospatialAPI extends BooleanField(this);    object exceptionMessage extends StringField(this, 255);    object hasFailed extends BooleanField(this);    object geolatlng extends MongoCaseClassField[APIStatsMessageDoc, LatLong](this){override def name = "latlng"} 
};

object APIStatsMessageDoc extends APIStatsMessageDoc with MongoMetaRecord[APIStatsMessageDoc] {    def saveMessage(message: APIStatsMessage):Unit = {    val queryParams = message.queryParams.toMap;    val pathParams = message.pathParams.toMap;    val queryParamsLinkedHashMap = LinkedHashMap(queryParams.toSeq:_*);    val pathParamsLinkedHashMap = LinkedHashMap(pathParams.toSeq:_*);    var listQueryParam:List[APIParam] = List[APIParam]();    var listPathParam:List[APIParam] = List[APIParam]();    queryParamsLinkedHashMap.foreach(x => {      val queryParam = APIParam.createRecord.key(x._1.toString).value(x._2.toString);      listQueryParam ++= List(queryParam)    });    pathParamsLinkedHashMap.foreach(x => {      val pathParam = APIParam.createRecord.key(x._1.toString).value(x._2.toString);      listPathParam ++= List(pathParam)    });    val record = APIStatsMessageDoc.createRecord;    if (message.isGeospatial == true){      val latitudeQueryParam = listQueryParam.filter(x => x.key.toString == "latitude");      val longitudeQueryParam = listQueryParam.filter(x => x.key.toString == "longitude");      val latitude = latitudeQueryParam.iterator.next.value.toString.toDouble;      val longitude = longitudeQueryParam.iterator.next.value.toString.toDouble;      val latlng = LatLong(latitude, longitude);      record.geolatlng(latlng)    };    record.apiName(message.apiName)      .contextPath(message.contextPath)      .resourceURL(message.resourceURL)      .pathParams(listPathParam)      .queryParams(listQueryParam)      .date(message.date.toCalendar(new Locale("en-US")).getTime)      .responseTime(message.reponseTime)      .hasFailed(message.hasFailed)      .exceptionMessage(message.exceptionMessage)      .isGeospatialAPI(message.isGeospatial)      .save;  };    def numberOfTotalMessages():Long ={    val numOfMessages = APIStatsMessageDoc count();    numOfMessages.toString.toLong  };    def numberOfTotalGeospatialMessages():Long = {    val numOfMessages = APIStatsMessageDoc where ( _.isGeospatialAPI eqs true ) count ();    numOfMessages.toString.toLong  };    def numberOMessagesByAPIName(apiName:String):Long = {   val numOfMessages = APIStatsMessageDoc where (_.apiName eqs apiName) count();   numOfMessages.toString.toLong  }     }

