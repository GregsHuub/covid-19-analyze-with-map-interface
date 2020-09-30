import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StringType

import scala.math.BigDecimal.RoundingMode
/**
 * @author Grzegorz Ozimski
 * @version 1.0
 */
object TestTimeMain extends App {

  val ss: SparkSession = SparkSession.builder().appName("covid-19").master("local[*]").getOrCreate()

  val affectedPeopleDir = ss.read.option("delimiter", ";").option("header", "true").csv("src/resources/Affected_MSISDN_2020-03-19.csv")
  val dataFrame = affectedPeopleDir
    .withColumn("Timestamp", col("###timestamp declared day").cast(StringType))
    .withColumn("msisdn", col("msisdn#").cast(StringType))
  dataFrame.printSchema()
//  val currentDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//  val _20DaysBefore = LocalDateTime.now().minusDays(40).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//
//  println("sparsowane na date: " + timeStrToLocal(currentDay))
//  println("sparsowane na date: " + timeStrToLocal("2020-03-033"))
//
//  def timeStrToLocal(yyyyMMdd: String): LocalDate = {
//    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    if(yyyyMMdd.length == 10 && yyyyMMdd.charAt(4).equals('-') && yyyyMMdd.charAt(7).equals('-')){
//     LocalDate.parse(yyyyMMdd, format)
//    }
//    else {
//      throw new Exception(s"Invalid format date: [$yyyyMMdd], required format type is [yyyy-MM-dd]")
//    }
//  }
//
}

