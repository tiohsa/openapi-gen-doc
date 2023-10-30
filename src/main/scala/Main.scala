import generator.GenTestCase
import generator.models.{ReadCsv, TestCaseOption}

import java.io.{File, FileOutputStream, PrintWriter}

@main def main(): Unit = {
//  val file = "openapi.yaml"
  val file = "api-with-examples.yaml"
  val testData = ReadCsv.readPropertyValues("test-property-values.csv")
  val propertyNames = ReadCsv.readPropertyNames("test-property-names.csv")
  val option = TestCaseOption(
    testData.input,
    testData.output,
    Seq((code: Int) => 200 <= code && code < 300),
    propertyNames
  )
  val genTestCase =
    GenTestCase(file, option)

  var writer: PrintWriter | Null = null
  try
    writer = new PrintWriter(new FileOutputStream(new File("output.csv")))
    genTestCase.generate.foreach { testCase =>
      println(s"${testCase}\n")
      writer.write(s"${testCase}\n")
    }
  catch case e => throw e
  finally writer.close()
}
