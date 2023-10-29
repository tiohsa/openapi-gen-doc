package generator.models

case class TestCaseOption(
    codeInputValues: Map[String, Map[String, String]] = Map.empty,
    codeOutputValues: Map[String, Map[String, String]] = Map.empty,
    filterCode: Seq[String] = Nil,
    propertyNames: Map[String, String] = Map.empty
)
