package generator

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import generator.models.{ReadCsv, TestCaseOption}

class GenTestCaseTest extends munit.FunSuite:

  test("run") {
    val testData = ReadCsv.readPropertyValues("test-property-values.csv")
    val names = ReadCsv.readPropertyNames("test-property-names.csv")
    val file = "openapi.yaml"
//    val file = "api-with-examples.yaml"
    val isOk = (code: Int) => 200 <= code && code < 300
    val option = TestCaseOption(
      testData.input,
      testData.output,
      Seq(isOk),
      names
    )
    val genTestCase =
      GenTestCase(file, option)
    genTestCase.generate.foreach { testCase =>
      println(s"${testCase}\n")
    }
  }
