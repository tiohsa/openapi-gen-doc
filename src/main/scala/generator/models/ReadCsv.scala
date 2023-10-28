package generator.models

import com.github.tototoshi.csv.CSVReader

import java.io.File
import scala.util.{Failure, Success, Try}

object ReadCsv {
  def read(path: String): Map[String, Map[String, String]] = {
    val reader = CSVReader.open(new File(path))
    val csv = Try(reader.all())
    csv match
      case Success(c) => c
      case Failure(e) => println(e)
    reader.close()
  }
  Map()
}
