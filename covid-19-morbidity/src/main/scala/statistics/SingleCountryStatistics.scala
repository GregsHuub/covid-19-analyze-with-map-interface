package statistics

import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import downloaders.URLDataDownloader
import org.apache.spark.sql.{AnalysisException, DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, desc, sum}
import parser.DefaultColumnsParser

import scala.math.BigDecimal.RoundingMode

class SingleCountryStatistics(final val sparkSession: SparkSession, final val absoluteFilePath: String, outputData: String) {

  private val desiredFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private val dayBeforeCurrent: String = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
  private val currentDay: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

  /**
   * return dataframe with rows between @yyyyMMdd_from to @yyyyMMdd_to
   * cast columns processing as well
   *
   * @param yyyyMMdd_from required format yyyy-MM-dd, earlier date
   * @param yyyyMMdd_to   required format yyyy-MM-dd, later date
   */
  def dfBetweenDays(yyyyMMdd_from: String, yyyyMMdd_to: String): DataFrame = {
    val df = DefaultColumnsParser.parser(sparkSession, absoluteFilePath)
    df.select("*").filter(df.col("Date").between(yyyyMMdd_from, yyyyMMdd_to))
  }

  /**
   * return dataframe with rows between @yyyyMMdd_from to newest
   *
   * @param yyyyMMdd_from required format yyyy-MM-dd,
   */
  def dfBetweenDays(yyyyMMdd_from: String): DataFrame = {
    val fileDf = DefaultColumnsParser.parser(sparkSession, absoluteFilePath)
    val newestDate = newestDateFromDf(fileDf)
    fileDf.select("*").filter(fileDf.col("Date").between(yyyyMMdd_from, newestDate))
  }

  /**
   * Default statistics method
   *
   * @return default object with statistics
   */
  private def defaultStatisticMethod(): DefaultStatsObject = {
    val file = DefaultColumnsParser.parser(sparkSession, absoluteFilePath) //    file.createOrReplaceTempView("confirmed")
    //        val countryMax = sparkSession.sql("SELECT `Country/Region`, Value FROM confirmed").show()
    //    import org.apache.spark.sql.functions._
    /**
     * select only important fields, without Lat and Long data.
     * Pick data only from last day
     */
    val df: DataFrame = file.select("Province/State", "Country/Region", "Date", "Value")
    val groupedByOnlyOneDay = df.select(df.col("*"))
      .where(df.col("Date") === newestDateFromDf(df))
      .where(df.col("Value") > 0)
      .sort("Country/Region")
    /*
        println("groupedByOnlyOneDay show")
        groupedByOnlyOneDay.printSchema()
        groupedByOnlyOneDay.show()

     */
    /**
     * Sum Deaths
     */
    val value = groupedByOnlyOneDay.select(groupedByOnlyOneDay.col("Value"))
    val valueSum = value.agg(sum("Value").cast("int")).first().get(0).asInstanceOf[Int]

    /**
     * Country with maximum value
     * return(CountryName, Value)
     */

    //musi byc agg(sum) bo inaczej as/alias nie dziala
    val sortedByMaxValue = groupedByOnlyOneDay.groupBy("Country/Region")
      .agg(sum("Value").as("Total"))
      .orderBy(desc("Total"))


    val countryMax = sortedByMaxValue.head()

    /**
     * return DeathStats(Allsum: Int, countryMax: Map[String, Int])
     */
    DefaultStatsObject(Map("total" -> valueSum), Map(countryMax.get(0).toString -> countryMax(1).asInstanceOf[Long].toInt))
  }

  def deathStatistics(sourceRoot: String = "src/resources/outputDataPaths.json"): DeathStats = {
    var fileDates = ""
    try {
      fileDates = newestDateFromDf()
    } catch {
      case e: AnalysisException =>
        System.err.println(s"Couldn't read file properly, cause source Path: [$absoluteFilePath] does not exist,\n or files are broken\n or path to file is incorrect\n $e")
        println("Files will be up to date automatically\n Start processing")
        val getData = new URLDataDownloader()
        getData.getDataFromUrl("downloadAll", sourceRoot)
        val default = defaultStatisticMethod()
        DeathStats(default.total, default.countryMax)
    }
    fileDates match {
      case `currentDay` | `dayBeforeCurrent` =>
        val default = defaultStatisticMethod()
        DeathStats(default.total, default.countryMax)
      case _ =>
        println("Data need to be up to date...\n Start processing")
        val getData = new URLDataDownloader()
        getData.getDataFromUrl("downloadAll", sourceRoot)
        val default = defaultStatisticMethod()
        DeathStats(default.total, default.countryMax)
    }
  }

  def confirmedStatistics(): ConfirmedStats = {
    var fileDates = ""
    try {
      fileDates = newestDateFromDf()
    } catch {
      case e: AnalysisException =>
        System.err.println(s"Couldn't read file properly, cause source Path: [$absoluteFilePath] does not exist,\n or files are broken\n or path to file is incorrect")
        println("Files will be up to date automatically\n Start processing")
        val getData = new URLDataDownloader()
        getData.getDataFromUrl("downloadAll", outputData)
        val default = defaultStatisticMethod()
        ConfirmedStats(default.total, default.countryMax)
    }
    fileDates match {
      case `currentDay` | `dayBeforeCurrent` =>
        val default = defaultStatisticMethod()
        ConfirmedStats(default.total, default.countryMax)
      case _ =>
        println("Data need to be up to date...\n Start processing")
        val getData = new URLDataDownloader()
        getData.getDataFromUrl("downloadAll", outputData)
        val default = defaultStatisticMethod()
        ConfirmedStats(default.total, default.countryMax)
    }
  }

  def recoveredStatistics(): RecoveredStats = {
    var fileDates = ""
    try {
      fileDates = newestDateFromDf()
    } catch {
      case e: AnalysisException =>
        System.err.println(s"Couldn't read file properly, cause source Path: [$absoluteFilePath] does not exist,\n or files are broken\n or path to file is incorrect")
        println("Files will be up to date automatically\n Start processing")
        val getData = new URLDataDownloader()
        getData.getDataFromUrl("downloadAll", outputData)
        val default = defaultStatisticMethod()
        RecoveredStats(default.total, default.countryMax)
    }
    fileDates match {
      case `currentDay` | `dayBeforeCurrent` =>
        val default = defaultStatisticMethod()
        RecoveredStats(default.total, default.countryMax)
      case _ =>
        println("Data need to be up to date...\n Start processing")
        val getData = new URLDataDownloader()
        getData.getDataFromUrl("downloadAll", outputData)
        val default = defaultStatisticMethod()
        RecoveredStats(default.total, default.countryMax)
    }
  }

  def newestDateFromDf(): String = {
    val dateDf = DefaultColumnsParser.parser(sparkSession, absoluteFilePath).select(col("Date")).head()
    dateDf.get(0).toString
  }

  private def newestDateFromDf(dataFrame: DataFrame): String = {
    val dataRows = dataFrame.select(col("Date")).head()
    dataRows.get(0).toString
  }

  def timeStrToLocal(yyyyMMdd: String): LocalDate = {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    if (yyyyMMdd.length == 10 && yyyyMMdd.charAt(4).equals('-') && yyyyMMdd.charAt(7).equals('-')) {
      LocalDate.parse(yyyyMMdd, format)
    }
    else {
      throw new Exception(s"Invalid format date: [$yyyyMMdd], required format type is [yyyy-MM-dd]")
    }
  }

  def setDoublePrecision(value: Double, precision: Int): Double = {
    BigDecimal.valueOf(value)
      .setScale(precision, RoundingMode.HALF_UP)
      .doubleValue()
  }

  case class DeathStats(total: Map[String, Int], countryMax: Map[String, Int])

  case class ConfirmedStats(total: Map[String, Int], countryMax: Map[String, Int])

  case class RecoveredStats(total: Map[String, Int], countryMax: Map[String, Int])

  case class DefaultStatsObject(total: Map[String, Int], countryMax: Map[String, Int])

}
