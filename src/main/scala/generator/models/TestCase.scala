package generator.models

case class TestCase(
    method: String,
    path: String,
    parameters: List[TestProperty],
    requestBody: List[TestProperty],
    response: TestResponse
) {
  import TestCase.*

//  override def toString: String = {
//    createTestCase(this.response).getOrElse("")
//  }
//
//  private var inputValues: Map[String, String] = Map()
//  private var outputValues: Map[String, String] = Map()
//
//  def createTestCase(response: TestResponse): Option[String] = {
//    inputValues = option.codeInputValues.getOrElse(response.code, Map())
//    outputValues = option.codeOutputValues.getOrElse(response.code, Map())
//
//    if option.filterCode.isEmpty || option.filterCode.exists { f =>
//        response.code.toIntOption.exists(f(_))
//      }
//    then {
//      val result = testcase.txt
//        .Ok(
//          this.method,
//          this.path,
//          this.parameters,
//          this.requestBody,
//          response
//        )
//        .toString
//        .split("\n")
//        .filter(_.nonEmpty)
//        .mkString("")
//      Some(result)
////      List(
////        titleString,
////        givenString(this.parameters),
////        whenString,
////        thenString(response)
////      )
//    } else None
//  }

//  def createTestCase2(response: TestResponse): List[String] = {
//    inputValues = option.codeInputValues.getOrElse(response.code, Map())
//    outputValues = option.codeOutputValues.getOrElse(response.code, Map())
//
//    if option.filterCode.isEmpty || option.filterCode.exists { f =>
//        response.code.toIntOption.exists(f(_))
//      }
//    then {
//      List(
//        titleString,
//        givenString(this.parameters),
//        whenString,
//        thenString(response)
//      )
//    } else Nil
//  }

//  def combinationParameters(
//      parameters: List[TestProperty]
//  ): List[List[TestProperty]] = {
//    val results = parameters.zipWithIndex.map { (parameter, index) =>
//      (parameter :: parameters.drop(index + 1) ++ parameters.filter(
//        _.required
//      )).distinct.sortBy(_.name)
//    }
//    results
//  }

//  def givenString(parameters: List[TestProperty]): String =
//    val givenParameters = parameters.map { case TestProperty(name, value, _) =>
//      parameterString(name, value)
//    }
//    val givenRequestBody = this.requestBody.map {
//      case TestProperty(name, value, _) =>
//        parameterString(name, value)
//    }
//    (givenParameters ++ givenRequestBody).mkString("\n")
//
//  def whenString: String =
//    executionString(this.path, this.method)
//
//  def thenString(response: TestResponse): String =
//    (codeString(response.code) ::
//      responsesString(response.properties)).mkString("\n") + "\n"
//
//  def titleString: String =
//    s"${this.method} ${this.path}のテスト"
//
//  def executionString(path: String, method: String): String =
//    s"${method} ${path}にデータを送信する"
//
//  def codeString(code: String): String =
//    s"ステータスコード\"${code}\"が返ること"
//
//  def responsesString(
//      properties: List[TestProperty]
//  ): List[String] =
//    properties.map { case TestProperty(name, value, _) =>
//      responseString(name, value)
//    }
//
//  def parameterString(name: String, value: String): String =
//    val input = inputValues.getOrElse(name, value)
//    val propertyName = option.propertyNames.getOrElse(name, name)
//    s"${propertyName}に\"${input}\"を設定する"

//  def responseString(name: String, value: String): String =
//    val output = outputValues.getOrElse(name, value)
//    val propertyName = option.propertyNames.getOrElse(name, name)
//    s"${propertyName}に\"${output}\"が返ること"
}
