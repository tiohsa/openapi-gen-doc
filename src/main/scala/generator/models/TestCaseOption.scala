package generator.models

case class TestCaseOption(
    codeInputValues: Map[String, Map[String, String]] = Map.empty,
    codeOutputValues: Map[String, Map[String, String]] = Map.empty,
    filterCode: Seq[(Int => Boolean)] = Nil,
    propertyNames: Map[String, String] = Map.empty
) {

  def inputValues(code: String): Map[String, String] =
    this.codeInputValues.getOrElse(code, Map())

  def outputValues(code: String): Map[String, String] =
    this.codeOutputValues.getOrElse(code, Map())
}
