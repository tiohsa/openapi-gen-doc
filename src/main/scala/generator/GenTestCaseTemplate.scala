package generator

import generator.models.{TestCase, TestCaseOption, TestCases, TestResponse}

object GenTestCaseTemplate {

  def generate(
      testCasesList: List[TestCases],
      option: TestCaseOption
  ): String = {
    //    inputValues =
    //      option.codeInputValues.getOrElse(testcase.response.code, Map())
    //    outputValues =
    //      option.codeOutputValues.getOrElse(testcase.response.code, Map())
    testCasesList
      .flatMap { testCases =>
        List(
          templates(testCases.postMethod, option),
          templates(testCases.getMethod, option),
          templates(testCases.putMethod, option),
          templates(testCases.deleteMethod, option)
        )
      }
      .filter(_.nonEmpty)
      .flatten
      .mkString("\n")
  }

  def templates(
      testCaseList: List[TestCase],
      option: TestCaseOption
  ): List[String] = {
    testCaseList.flatMap { testCase =>
      template(testCase, option)
    }
  }

  def template(
      testCase: TestCase,
      option: TestCaseOption
  ): Option[String] = {

    if option.filterCode.isEmpty || option.filterCode.exists { f =>
        testCase.response.code.toIntOption.exists(f(_))
      }
    then {
      val result = testcase.txt
        .Ok(
          testCase.method,
          testCase.path,
          testCase.parameters,
          testCase.requestBody,
          testCase.response,
          option
        )
        .toString
        .split("\n")
        .filter(_.nonEmpty)
        .mkString("\n")
      Some(result)
    } else None
  }

}
