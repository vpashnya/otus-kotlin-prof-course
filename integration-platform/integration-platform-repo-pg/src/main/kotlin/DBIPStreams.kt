import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

object DBIPStreams : LongIdTable("ip_stream") {
  val description = varchar("description", 4000)
  val classShortName = varchar("class_short_name", 20)
  val methodShortName = varchar("method_short_name", 20)
  val transportParams = varchar("transport_params", 200)
  val active = bool("active").default(false)
  val version = long("version").default(0)
}

class DBIPStream(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<DBIPStream>(DBIPStreams)

  var description by DBIPStreams.description
  var classShortName by DBIPStreams.classShortName
  var methodShortName by DBIPStreams.methodShortName
  var transportParams by DBIPStreams.transportParams
  var active by DBIPStreams.active
  var version by DBIPStreams.version

  override fun toString() = "DBIPStream(id=$id, description=$description, classShortName=$classShortName, methodShortName=$methodShortName, transportParams=$transportParams, active=$active, version=$version)"

}
