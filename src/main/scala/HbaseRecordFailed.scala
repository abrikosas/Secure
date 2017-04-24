import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes


case class FailedUserAttack(timestamp: String, realuser: String, ip: String)

object HbaseRecordFailed extends Serializable {
  final val patternList = List("Failed","root")

  def parseEvent(str: Array[String]): FailedUserAttack = {
    //  val a = str.split("\\s+").filter(_.length == 16).filter(l => patternList.exists(_.contains()))
   // val a = str.split("\\s+").filter(l => patternList.exists(_.contains()))

    //InvalidUserAttack(a(0) + " " + a(1) + " " + a(2), a(9), a(12))
    FailedUserAttack(str(0) + " " + str(1) + " " + str(2), str(9), str(10))
  }

  //  Convert a row of Attack object data to an HBase put object
  def convertToPut(attack: FailedUserAttack,cf:String): (ImmutableBytesWritable, Put) = {
    val dateTime = attack.timestamp
    println("Event date:"+dateTime)
    // create a composite row key: Attackid_date time
    val rowkey = attack.ip
    println("Key :"+ rowkey)
    val put = new Put(Bytes.toBytes(rowkey))
    // add to column family data, column  data values to put object
    put.add(Bytes.toBytes(cf), Bytes.toBytes(attack.realuser), Bytes.toBytes(dateTime))
    return (new ImmutableBytesWritable(Bytes.toBytes(rowkey)), put)
  }

}
