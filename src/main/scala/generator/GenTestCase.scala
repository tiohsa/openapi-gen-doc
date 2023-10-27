package generator

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.{Components, OpenAPI, Operation, PathItem}
import io.swagger.v3.oas.models.media.{Content, MediaType, Schema}
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse

import scala.jdk.CollectionConverters.*

object GenTestCase {
  def createTitle(apiPath: String, method: String): String =
    s"${method} ${apiPath}のテスト"

  def toGetMethodString(pathItem: PathItem): Option[String] =
    if pathItem.getGet != null then Some("GET")
    else None

  def toPostMethodString(pathItem: PathItem): Option[String] =
    if pathItem.getPost != null then Some("Post")
    else None

  def toPutMethodString(pathItem: PathItem): Option[String] =
    if pathItem.getPost != null then Some("Put")
    else None

  def toDeleteMethodString(pathItem: PathItem): Option[String] =
    if pathItem.getDelete != null then Some("Delte")
    else None

  def toParametersString(operation: Operation): List[String] =
    if operation.getParameters == null then List()
    else
      operation.getParameters.asScala.toList.map(parameter =>
        GenTestCase.toParameterString(parameter)
      )

  def toParameterString(parameter: Parameter): String =
    s"${parameter.getName}に\"${parameter.getExample}\"を設定する"

  def toSchemaString[T](name: String, schema: Schema[T]): String =
    s"${name}に\"${schema.getExample}\"を設定する"

  def toExecuteString(path1: String, method: String): String =
    s"${method} ${path1}を実行する"

  def toReulst(code: String, response: ApiResponse): String =
    s"ステータスコード${code}が返ること"

  def read(url: String): Option[OpenAPI] = {
    val result = new OpenAPIParser().readLocation(
      url,
      null,
      null
    )
    val openAPI = result.getOpenAPI
    if (result.getMessages != null) {
      result.getMessages
        .forEach(msg => {
          println(msg)
        })
    }
    if openAPI != null then Some(openAPI)
    else None
  }

  def createPutSering(
      path: String,
      operation: Operation,
      components: Components
  ): Option[String] = {
    if operation == null then None
    else {
      val parameters = GenTestCase
        .toParametersString(operation)
      println(parameters)
      val schemaRef =
        operation.getRequestBody.getContent
          .get("application/json")
          .getSchema
          .get$ref
          .replace("#/components/schemas/", "")
      val schema = components.getSchemas.get(schemaRef)
      if schema.properties != null then
        val schemas = schema.getProperties.asScala
          .map((name, schema) => GenTestCase.toSchemaString(name, schema))
          .toList
        println(schemas)
      Some("")
    }
  }

}
