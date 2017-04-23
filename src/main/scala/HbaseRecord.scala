import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes


case class InvalidUserAttack(timestamp: String, realuser: String, ip: String)

object HbaseRecord extends Serializable {
  final val patternList = List("Failed","failed")

  def parseEvent(str: Array[String]): InvalidUserAttack = {
  //  val a = str.split("\\s+").filter(_.length == 16).filter(l => patternList.exists(_.contains()))
    //val a = str.split("\\s+").filter(l => patternList.exists(_.contains()))

    //InvalidUserAttack(a(0) + " " + a(1) + " " + a(2), a(9), a(12))
    InvalidUserAttack(str(0) + " " + str(1) + " " + str(2), str(9), str(12))
  }

  //  Convert a row of Attack object data to an HBase put object
  def convertToPut(attack: InvalidUserAttack,cf:String): (ImmutableBytesWritable, Put) = {
    val dateTime = attack.timestamp
    // create a composite row key: Attackid_date time
    val rowkey = attack.ip
    val put = new Put(Bytes.toBytes(rowkey))
    // add to column family data, column  data values to put object
    put.add(Bytes.toBytes(cf), Bytes.toBytes(dateTime), Bytes.toBytes(attack.realuser))
    return (new ImmutableBytesWritable(Bytes.toBytes(rowkey)), put)
  }

}
