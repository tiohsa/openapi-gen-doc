package generator.models

case class TestCase(
    method: String,
    path: String,
    parameters: List[TestProperty],
    requestBody: List[TestProperty],
    responses: List[TestResponse],
    codeInputValues: Map[String, Map[String, String]],
    codeOutputValues: Map[String, Map[String, String]]
) {
  import TestCase.*

  override def toString: String = {
    this.responses
      .flatMap { response =>
        createTestCase(response)
      }
      .mkString("\n")
  }

  private var inputValues: Map[String, String] = Map()
  private var outputValues: Map[String, String] = Map()

  def createTestCase(response: TestResponse): List[String] = {
    inputValues = codeInputValues.getOrElse(response.code, Map())
    outputValues = codeOutputValues.getOrElse(response.code, Map())
    List(titleString, givenString, whenString, thenString(response))
    //        if response.code == "200" then
    //          List(titleString, givenString, whenString, thenString(response))
    //        else Nil
  }

  def givenString: String =
    val givenParameters = this.parameters.map {
      case TestProperty(name, value) =>
        parameterString(name, value)
    }
    val givenRequestBody = this.requestBody.map {
      case TestProperty(name, value) =>
        parameterString(name, value)
    }
    (givenParameters ++ givenRequestBody).mkString("\n")

  def whenString: String =
    executionString(this.path, this.method)

  def thenString(response: TestResponse): String =
    (codeString(response.code) ::
      responsesString(response.properties)).mkString("\n") + "\n"

  def titleString: String =
    s"${this.method} ${this.path}のテスト"

  def executionString(path: String, method: String): String =
    s"${method} ${path}にデータを送信する"

  def codeString(code: String): String =
    s"ステータスコード\"${code}\"が返ること"

  def responsesString(
      properties: List[TestProperty]
  ): List[String] =
    properties.map { case TestProperty(name, value) =>
      responseString(name, value)
    }

  def parameterString(name: String, value: String): String =
    val input = inputValues.getOrElse(name, value)
    s"${name}に\"${input}\"を設定する"

  def responseString(name: String, value: String): String =
    val output = outputValues.getOrElse(name, value)
    s"${name}に\"${output}\"が返ること"
}
