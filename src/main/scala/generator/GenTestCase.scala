package generator

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.{Components, OpenAPI, Operation, PathItem}
import io.swagger.v3.oas.models.media.{Content, MediaType, Schema}
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.parser.core.models.ParseOptions

import java.util
import scala.jdk.CollectionConverters.*

class GenTestCase(url: String) {
  import GenTestCase.*
  def openapi: OpenAPI = read(url).get
  def generate(): String = {
    println(openapi)
    openapi.getPaths.asScala
      .foreach { (path, pathItem) =>
        {
          val operation: Operation | Null = pathItem.getPut
          println(pathString(path, "Put", operation).mkString("\n"))
        }
      }
    ""
  }
}

object GenTestCase {
  def title(apiPath: String, method: String): String =
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

  def pathString(
      path: String,
      method: String,
      operation: Operation | Null
  ): List[String] =
    if operation == null then List()
    else
      List(title(path, method)) ++
        parametersString(operation.getParameters) ++
        requestBodyString(operation.getRequestBody)

  def parametersString(parameters: util.List[Parameter] | Null): List[String] =
    if parameters == null then List()
    else
      parameters.asScala.toList.map { parameter =>
        parameterString(parameter)
      }

  def parameterString(parameter: Parameter): String =
    s"${parameter.getName}に\"${parameter.getExample}\"を設定する"

  def requestBodyString(requestBody: RequestBody | Null): List[String] =
    if requestBody == null then List()
    else contentString(requestBody.getContent)

  def contentString(content: Content | Null): List[String] =
    if content == null then List()
    else
      content.asScala.toList.flatten { (name, mediaType) =>
        mediaTypeString(name, mediaType)
      }

  def mediaTypeString(name: String, mediaType: MediaType): List[String] =
    if mediaType == null then List()
    else schemaString(mediaType.getSchema)

  def schemaString[T](schema: Schema[T] | Null): List[String] =
    if schema == null then List()
    else
      val properties: util.Map[String, Schema[_]] | Null = schema.getProperties
      propertiesString(properties)

  def propertiesString(
      properties: util.Map[String, Schema[_]] | Null
  ): List[String] =
    if properties == null then List()
    else
      properties.asScala.flatMap { (name, property) =>
        propertyString(name, property)
      }.toList

  def propertyString(
      name: String,
      property: Schema[_] | Null
  ): Option[String] =
    if property == null then None
    else
      val str = s"${name}に\"${property.getExample}\"を設定する"
      Some(str)

  def toExecuteString(path1: String, method: String): String =
    s"${method} ${path1}を実行する"

  def toReulst(code: String, response: ApiResponse): String =
    s"ステータスコード${code}が返ること"

  def read(url: String): Option[OpenAPI] = {
    val parseOptions = new ParseOptions()
    parseOptions.setResolveFully(true)
    val result = new OpenAPIParser().readLocation(
      url,
      null,
      parseOptions
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

//  def createPutSering(
//      path: String,
//      operation: Operation,
//      components: Components
//  ): Option[String] = {
//    if operation == null then None
//    else {
//      val parameters = GenTestCase.parametersString(operation)
//      println(parameters)
//      val schema =
//        operation.getRequestBody.getContent
//          .get("application/json")
//          .getSchema
//      if schema.properties != null then
//        val schemas = schema.getProperties.asScala.map { (name, schema) =>
//          GenTestCase.toSchemaString(name, schema)
//        }.toList
//        println(schemas)
//      Some("")
//    }
//  }

}
