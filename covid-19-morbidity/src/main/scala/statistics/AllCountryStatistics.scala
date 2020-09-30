package statistics

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{abs, desc, lag, sum}
import org.apache.spark.sql.types.DataTypes
import parser.DefaultColumnsParser

class AllCountryStatistics(final val sparkSession: SparkSession, val absoluteFilePath: String) {

  def percentageIncreaseInAll(saveToFile: Boolean = false, savePath: String = ""): DataFrame = {
    val df = DefaultColumnsParser.parser(sparkSession, absoluteFilePath)
    val totalByDate = df.select("Date", "Value").groupBy("Date").agg(sum("Value").as("Total"))
      .sort(desc("Date"))
    val substractedByDay = totalByDate.withColumn("Daily Increase", totalByDate.col("Total") - lag("Total", 1, 0)
      .over(Window.orderBy(desc("Date"))))
    val totalValueIncreaseDayByDay = substractedByDay.withColumn("Daily Increase", abs(substractedByDay.col("Daily Increase")))
    val percentageRaise = totalValueIncreaseDayByDay
      .withColumn("Percentage", totalValueIncreaseDayByDay.col("Daily Increase") * 100 / totalValueIncreaseDayByDay.col("Total"))
    val parsedPercentage = percentageRaise.withColumn("Percentage", percentageRaise.col("Percentage").cast(DataTypes.createDecimalType(32, 2)))
    if (saveToFile) {
      parsedPercentage.repartition(1).write.option("header", "true").csv(savePath)
      parsedPercentage
    } else {
      parsedPercentage
    }
  }

  def percentageIncreaseInCountries(skipNoVirusTime: Boolean = true, saveToFile: Boolean = false, savePath: String = ""): DataFrame = {
    val df = DefaultColumnsParser.parser(sparkSession, absoluteFilePath)
    val countryDf = df.select("Country/Region", "Date", "Value")
      .withColumnRenamed("Value", "Total")
    val countryGrouped = countryDf.select("*").groupBy("Country/Region", "Date").agg(sum("Total").as("Total"))
      .orderBy(countryDf.col("Country/Region"), desc("Date"))
    val addDailyIncrease = countryGrouped.withColumn("Daily Increase", countryGrouped.col("Total") - lag("Total", 1, 0)
      .over(Window.orderBy("Country/Region")))
    val countriesValueIncreaseDayByDay = addDailyIncrease
      .withColumn("Daily Increase", abs(addDailyIncrease.col("Daily Increase")))
    val percentageCountryRaise = countriesValueIncreaseDayByDay
      .withColumn("Percentage", countriesValueIncreaseDayByDay.col("Daily Increase") * 100 / countriesValueIncreaseDayByDay.col("Total"))
    val percentageCountryWithPrecision = percentageCountryRaise
      .withColumn("Percentage", percentageCountryRaise.col("Percentage").cast(DataTypes.createDecimalType(32, 2)))
    if (skipNoVirusTime) percentageCountryWithPrecision.where(percentageCountryRaise.col("Percentage").isNotNull)
    else return percentageCountryWithPrecision

    if (saveToFile) {
      percentageCountryWithPrecision.repartition(1).write.option("header", "true").csv(savePath)
      percentageCountryWithPrecision
    } else {
      percentageCountryWithPrecision
    }
  }


}
