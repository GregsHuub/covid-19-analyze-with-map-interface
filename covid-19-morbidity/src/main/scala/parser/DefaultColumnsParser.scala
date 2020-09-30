package parser

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType}

object DefaultColumnsParser {

  def parser(sparkSession: SparkSession, absoluteFilePath: String, dataColumnToDate: Boolean = true): DataFrame = {
    val fileDf = sparkSession.read.option("header", "true").csv(absoluteFilePath)
    var dataFrame = fileDf.withColumn("Lat", col("Lat").cast(DoubleType))
      .withColumn("Long", col("Long").cast(DoubleType))
      .withColumn("Value", col("Value").cast(LongType))
    if (dataColumnToDate) dataFrame = dataFrame.withColumn("Date", col("Date").cast("date"))
    // wycinanie zbędnego wiersza z hashtagami
    val df = dataFrame.withColumn("Index", monotonically_increasing_id())
      .filter(col("Index") > 0)
      .drop("Index")
    df
  }

}
