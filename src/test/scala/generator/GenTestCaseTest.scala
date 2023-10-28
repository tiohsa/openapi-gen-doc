package generator

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.{Operation, PathItem}
import io.swagger.v3.oas.models.media.{Content, MediaType, Schema}
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.{ApiResponse, ApiResponses}

import scala.jdk.CollectionConverters.*

class GenTestCaseTest extends munit.FunSuite:

//  test("create test case title") {
//    val title = GenTestCase.title("path", "GET")
//    assertEquals(title, "GET pathのテスト")
//  }
//
//  test("get method string") {
//    val get = new Operation()
//    val pathItem = new PathItem()
//    pathItem.setGet(get)
//    val method = GenTestCase.toGetMethodString(pathItem).get
//    assertEquals(method, "GET")
//  }
//
//  test("create parameter string") {
//    val parameter = new Parameter()
//    parameter.setName("param1")
//    parameter.setDescription("パラメータ1")
//    parameter.setExample("abc")
//    val message = GenTestCase.parameterString(parameter)
//    assertEquals(message, "param1に\"abc\"を設定する")
//  }
//
//  test("create request body string") {
//    val schema = new Schema()
//    schema.setExample("abc")
//    val message = GenTestCase.schemaString(schema)
//    assertEquals(message, List("パラメータ1に\"abc\"を設定する"))
//  }
//
//  test("create execute string") {
//    val operation = new Operation()
//    val message = GenTestCase.toExecuteString("/path1", "GET")
//    assertEquals(message, "GET /path1を実行する")
//  }
//
//  test("create result string") {
//    val responses = new ApiResponses()
//    val response = new ApiResponse()
//    responses.addApiResponse("200", response)
//    val message = GenTestCase.toReulst("200", response)
//    assertEquals(message, "ステータスコード200が返ること")
//  }
//
  test("run") {
    val genTestCase = GenTestCase("openapi.yaml")
    genTestCase.generate.foreach { testCase =>
      println(s"${testCase}\n")
    }
  }
