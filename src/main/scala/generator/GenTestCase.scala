package generator

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.{OpenAPI, Operation}
import io.swagger.v3.oas.models.media.{Content, MediaType, Schema}
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.parser.core.models.ParseOptions
import generator.models.*

import java.util
import scala.jdk.CollectionConverters.*

case class GenTestCase(url: String) {
  import GenTestCase.*

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

  def openapi: OpenAPI = read(url).get
  def generate: List[TestCases] = {
//    println(openapi)
    val result = openapi.getPaths.asScala.toList
      .map { (path, pathItem) =>
        {
          TestCases(
            createTestCase(path, "Get", pathItem.getGet),
            createTestCase(path, "Post", pathItem.getPost),
            createTestCase(path, "Put", pathItem.getPut),
            createTestCase(path, "Delete", pathItem.getDelete)
          )
        }
      }
    result
  }
}

object GenTestCase {

  def createTestCase(
      path: String,
      method: String,
      operation: Operation | Null
  ): Option[TestCase] =
    if operation == null then None
    else
      val parameters = parametersOrDefault(operation.getParameters)
      val requestBody = requestBodyOrDefault(operation.getRequestBody)
      val responses = responsesOrDefault(operation.getResponses)
      Some(
        TestCase(
          method,
          path,
          parameters,
          requestBody,
          responses
        )
      )

  def parametersOrDefault(
      parameters: util.List[Parameter] | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if parameters == null then default
    else
      parameters.asScala.toList.flatMap { parameter =>
        exampleOrDefault(parameter.getName, parameter.getExample)
      }

  def requestBodyOrDefault(
      requestBody: RequestBody | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if requestBody == null then default
    else contentOrDefault(requestBody.getContent, default)

  def contentOrDefault(
      content: Content | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if content == null then default
    else
      content.asScala.toList.flatten { (name, mediaType) =>
        mediaTypeOrDefault(name, mediaType, default)
      }

  def mediaTypeOrDefault(
      name: String,
      mediaType: MediaType,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if mediaType == null then default
    else schemaOrDefault(mediaType.getSchema, default)

  def schemaOrDefault[T](
      schema: Schema[T] | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if schema == null then default
    else
      val properties: util.Map[String, Schema[_]] | Null = schema.getProperties
      propertiesOrDefault(properties, default)

  def propertiesOrDefault(
      properties: util.Map[String, Schema[_]] | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if properties == null then default
    else
      properties.asScala.toList.flatMap { (name, schema) =>
        propertyOrDefault(name, schema)
      }

  def propertyOrDefault(
      name: String,
      property: Schema[_] | Null
  ): Option[TestProperty] =
    if property == null then None
    else exampleOrDefault(name, property.getExample)

  def exampleOrDefault(
      name: String,
      example: Object | Null
  ): Option[TestProperty] =
    if example == null then None
    else Some(TestProperty(name, example.toString))

  def responsesOrDefault(
      responses: util.Map[String, ApiResponse] | Null,
      default: List[TestResponse] = Nil
  ): List[TestResponse] =
    if responses == null then default
    else
      responses.asScala.toList.map { (code, response) =>
        TestResponse(code, responseOrDefault(response))
      }

  def responseOrDefault(
      apiResponse: ApiResponse | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if apiResponse == null then default
    else contentOrDefault(apiResponse.getContent, default)

}
