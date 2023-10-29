package generator.models

import com.github.tototoshi.csv.{CSVReader, defaultCSVFormat}

import java.io.File

case class TestData(
    input: Map[String, Map[String, String]],
    output: Map[String, Map[String, String]]
)

object ReadCsv {
  def readPropertyValues(
      path: String
  ): TestData = {
    var reader: CSVReader | Null = null
    try
      reader = CSVReader.open(new File(path))
      val rows = reader.allWithHeaders()
      TestData(
        createPropertyValues("input", rows),
        createPropertyValues("output", rows)
      )
    catch case e => throw e
    finally if reader != null then reader.close()
  }

  def createPropertyValues(
      propertyType: String,
      rows: List[Map[String, String]]
  ): Map[String, Map[String, String]] = {
//    type,code,property,value
    val typeValues =
      rows.filter(row => row("type") == propertyType)
    val propertiesMap = typeValues
      .map(_("code"))
      .distinct
      .map { code =>
        val properties = typeValues
          .filter(row => row("code") == code)
          .map(row => row("property") -> row("value"))
          .toMap
        (code -> properties)
      }
      .toMap
    propertiesMap
  }

  def readPropertyNames(
      path: String
  ): Map[String, String] = {
    var reader: CSVReader | Null = null
    try
      reader = CSVReader.open(new File(path))
      val rows = reader.allWithHeaders()
      createPropertyNames(rows)
    catch case e => throw e
    finally if reader != null then reader.close()
  }

  def createPropertyNames(
      rows: List[Map[String, String]]
  ): Map[String, String] = {
//    property,name
    val propertiesMap =
      rows.map(row => row("property") -> row("name")).toMap
    propertiesMap
  }
}
