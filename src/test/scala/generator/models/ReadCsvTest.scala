package generator.models

class ReadCsvTest extends munit.FunSuite:

  test("read property values csv") {
    val csv = ReadCsv.readPropertyValues("test-property-values.csv")
  }

  test("read property names csv") {
    val csv = ReadCsv.readPropertyNames("test-property-names.csv")
  }
