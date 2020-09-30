package util

import downloaders.URLDataDownloader

/**
 * methods download files from hopkins website
 * replace if exist
 * @param outputData json path with details
 */
class FileUpdateOnly(val outputData:String) extends URLResolver {

  def updateAll() {
    val getData = new URLDataDownloader()
    getData.getDataFromUrl("downloadAll", outputData)
  }

  /**
   *
   * @param downloadType (confirm, death, recovered)
   */
  def update(downloadType :String = "confirm"): Unit ={
    val getData = new URLDataDownloader()
    getData.getDataFromUrl(downloadType, outputData)
  }

}
