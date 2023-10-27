// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import generator.GenTestCase
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.{Content, MediaType, Schema}
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import scala.jdk.CollectionConverters._

class GenTestCaseTest extends munit.FunSuite:

  test("create test case title") {
    val title = GenTestCase.createTitle("path", "GET")
    assertEquals(title, "GET pathのテスト")
  }

  test("get method string") {
    val get = new Operation()
    val pathItem = new PathItem()
    pathItem.setGet(get)
    val method = GenTestCase.toGetMethodString(pathItem).get
    assertEquals(method, "GET")
  }

  test("create parameter string") {
    val parameter = new Parameter()
    parameter.setName("param1")
    parameter.setDescription("パラメータ1")
    parameter.setExample("abc")
    val message = GenTestCase.toParameterString(parameter)
    assertEquals(message, "パラメータ1に\"abc\"を設定する")
  }

  test("create request body string") {
    val schema = new Schema()
    schema.setExample("abc")
    val message = GenTestCase.toSchemaString("パラメータ1", schema)
    assertEquals(message, "パラメータ1に\"abc\"を設定する")
  }

  test("create execute string") {
    val operation = new Operation()
    val message = GenTestCase.toExecuteString("/path1", "GET")
    assertEquals(message, "GET /path1を実行する")
  }

  test("create result string") {
    val responses = new ApiResponses()
    val response = new ApiResponse()
    responses.addApiResponse("200", response)
    val message = GenTestCase.toReulst("200", response)
    assertEquals(message, "ステータスコード200が返ること")
  }

  test("run") {
    val openapi = GenTestCase.read("openapi.json").get
    val result = openapi.getPaths.asScala
      .map((path, pathItem) => {
//      GenTestCase.createGetSering(path, pathItem.getGet)
//        GenTestCase.createPostSering(path, pathItem.getPost)
        GenTestCase.createPutSering(path, pathItem.getPut)
//      GenTestCase.createDeleteSering(path, pathItem.getDelete)
      })
      .toSeq
      .mkString("\n")
  }
