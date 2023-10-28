package generator

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.{Components, OpenAPI, Operation, PathItem}
import io.swagger.v3.oas.models.media.{Content, MediaType, Schema}
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.parser.core.models.ParseOptions

import java.util
import scala.jdk.CollectionConverters.*

case class TestCase(
    title: String,
    parameters: List[String],
    requestBody: List[String],
    execution: String,
    responses: List[String]
)

class GenTestCase(url: String) {
  import GenTestCase.*
  def openapi: OpenAPI = read(url).get
  def generate(): String = {
//    println(openapi)
    val result = openapi.getPaths.asScala
      .flatMap { (path, pathItem) =>
        {
          val operation: Operation | Null = pathItem.getPut
          pathString(path, "Put", operation)
        }
      }
      .mkString("\n")
    println(result)
    ""
  }
}

object GenTestCase {

  def pathString(
      path: String,
      method: String,
      operation: Operation | Null
  ): Option[TestCase] =
    if operation == null then None
    else
      val title = titleString(path, method)
      val parameters = parametersString(operation.getParameters)
      val requestBody = requestBodyString(operation.getRequestBody)
      val execution = executionString(path, method)
      val responses = responsesString(operation.getResponses)
      Some(
        TestCase(
          title,
          parameters,
          requestBody,
          execution,
          responses
        )
      )

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

  def responsesString(
      responses: util.Map[String, ApiResponse] | Null
  ): List[String] =
    if responses == null then List()
    else
      responses.asScala.toList.flatMap { (code, response) =>
        List(resultString(code)) ++
          responseString(response)
      }

  def responseString(
      apiResponse: ApiResponse | Null
  ): List[String] =
    if apiResponse == null then List()
    else contentString(apiResponse.getContent)

  def titleString(apiPath: String, method: String): String =
    s"${method} ${apiPath}のテスト"

  def executionString(path: String, method: String): String =
    s"${method} ${path}にデータを送信する"

  def resultString(code: String): String =
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
