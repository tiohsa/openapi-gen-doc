package generator.models

case class TestCase(
    method: String,
    path: String,
    parameters: List[TestProperty],
    requestBody: List[TestProperty],
    responses: List[TestResponse]
) {
  import TestCase.*

  override def toString: String = {
    this.responses
      .flatMap { response =>
        List(titleString, givenString, whenString, thenString(response))
      }
      .mkString("\n")
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
}

object TestCase {

  def parameterString(name: String, value: String): String =
    s"${name}に\"${value}\"を設定する"

  def executionString(path: String, method: String): String =
    s"${method} ${path}にデータを送信する"

  def codeString(code: String): String =
    s"ステータスコード${code}が返ること"

  def responsesString(
      properties: List[TestProperty]
  ): List[String] =
    properties.map { case TestProperty(name, value) =>
      responseString(name, value)
    }

  def responseString(name: String, value: String): String =
    s"${name}に${value}が返ること"
}
