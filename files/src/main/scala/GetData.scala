import java.io.{File, IOException, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import json.JsonMapperUtils
import org.jsoup.Jsoup
import scala.io.{BufferedSource, Source}
import scala.language.postfixOps

class GetData {

  def getDataFromUrl(covidFileType: String = "downloadAll", outputDataJsonPath: String): Unit = {
    val outputDataPaths = new JsonMapperUtils().jsonFileToMap(outputDataJsonPath)
    val outputDirectory = outputDataPaths("output_files_path").asInstanceOf[String]
    val fileNames = outputDataPaths("file_names").asInstanceOf[Map[String, String]]
    covidFileType match {
      case "downloadAll" =>
        URLDownloader.fileDownloaderChannel("confirmed", outputDirectory + fileNames("confirmed"))
        URLDownloader.fileDownloaderChannel("death", outputDirectory + fileNames("death"))
        URLDownloader.fileDownloaderChannel("recovered", outputDirectory + fileNames("recovered"))
      case "confirmed" => URLDownloader.fileDownloaderChannel("confirmed", outputDirectory + fileNames("confirmed"))
      case "death" => URLDownloader.fileDownloaderChannel("death", outputDirectory + fileNames("death"))
      case "recovered" => URLDownloader.fileDownloaderChannel("recovered", outputDirectory + fileNames("recovered"))
    }
  }

  private case object URLDownloader {
    private val confirmedNthChild = "4"
    private val deathsNthChild = "5"
    private val recoveredNthChild = "6"
    private val mainUrl = "https://data.humdata.org/dataset/novel-coronavirus-2019-ncov-cases"
    val doc = Jsoup.connect(mainUrl).get()
    val confirmedHref = doc.select(s"#data-resources-0 > div > ul > li:nth-child($confirmedNthChild) > div.hdx-btn-group > a").attr("abs:href")
    val deathsHref = doc.select(s"#data-resources-0 > div > ul > li:nth-child($deathsNthChild) > div.hdx-btn-group > a").attr("abs:href")
    val recoveredHref = doc.select(s"#data-resources-0 > div > ul > li:nth-child($recoveredNthChild) > div.hdx-btn-group > a").attr("abs:href")

    /**
     * method use Jsoup library to get href from https://data.humdata.org/dataset/novel-coronavirus-2019-ncov-cases where links are inserted
     * Main functionality is to download csv files from URL and save within the file through channel output stream
     *
     * @param dataType   is type of csv file which user want to download.
     *                   Three types to choose
     *                   "confirmed" => confirmed covid-19 infections
     *                   "death" => how many people alredy died becaouse of covid-19
     *                   "recovered" => how many people were already recovered
     *                   default value is "confirmed"
     *                   every unlisted value will be converted to default => "confirmed
     * @param outputPath is path where downloaded file will be save
     * @param mode       default is rw read/write.
     */
    def fileDownloaderChannel(dataType: String = "confirmed", outputPath: String, mode: String = "rw"): Unit = {
      val fileUrl = dataType match {
        case "confirmed" => confirmedHref
        case "death" => deathsHref
        case "recovered" => recoveredHref
        case _ =>
          println("cause of wrong typed, method use default value - confirmed")
          confirmedHref
      }
      println(s"start getting resources [$dataType] from [$mainUrl] ...")
      var source: BufferedSource = null
      var channel: FileChannel = null
      var stream: RandomAccessFile = null
      try {
        source = Source.fromURL(fileUrl)
        stream = new RandomAccessFile(new File(outputPath), mode)
        channel = stream.getChannel
        val bytes = source.mkString.getBytes
        val buffer = ByteBuffer.allocate(bytes.length)
        buffer.put(bytes)
        buffer.flip()
        channel.write(buffer)
        println("file created.")
      } catch {
        case e: IOException => println("url not recognized: \n" + e)
        case e: Exception => print("default exception " + e)
      }
      finally {
        stream.close()
        channel.close()
        println("stream closed.")
      }
    }
  }

}

//  def bashScriptRunner(pathToFile: String): Unit = {
//    pathToFile.endsWith(".sh") match {
//      case true => Process(s"bash -f $pathToFile").lineStream
//      case false => Process(s"bash -f $pathToFile.sh").lineStream
//      case _ => throw new Exception
//    }
//  val gitBash = "C:/Program Files/Git/git-bash.exe"
//  val command = "cd c/Users/GregHP/Desktop/SparkProj/testExceliDownl/downl.sh"
//  val commands: Array[String] = Array(gitBash, command)
//  }
