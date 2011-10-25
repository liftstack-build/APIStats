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
import java.text.DecimalFormat
class APIStatsMessageDoc private () extends MongoRecord[APIStatsMessageDoc] with MongoId[APIStatsMessageDoc] {

  def meta = APIStatsMessageDoc

  object apiName extends StringField(this, 100)

  object contextPath extends StringField(this, 20)

  object resourceURL extends StringField(this, 255)

  object queryParams extends BsonRecordListField[APIStatsMessageDoc, APIParam](this, APIParam)

  object date extends DateField(this)

  object responseTime extends LongField(this);
};

object APIStatsMessageDoc extends APIStatsMessageDoc with MongoMetaRecord[APIStatsMessageDoc] {
