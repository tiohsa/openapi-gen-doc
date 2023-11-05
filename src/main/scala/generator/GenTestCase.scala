package generator

import generator.models.*
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.media.*
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.{OpenAPI, Operation}
import io.swagger.v3.parser.core.models.ParseOptions

import java.util
import scala.jdk.CollectionConverters.*

case class GenTestCase(
    url: String
) {
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
    openapi.getPaths.asScala.toList
      .map { (path, pathItem) =>
        {
          TestCases(
            createTestCase(path, "Post", pathItem.getPost),
            createTestCase(path, "Get", pathItem.getGet),
            createTestCase(path, "Put", pathItem.getPut),
            createTestCase(path, "Delete", pathItem.getDelete)
          )
        }
      }
  }

  def createTestCase(
      path: String,
      method: String,
      operation: Operation | Null
  ): List[TestCase] =
    if operation == null then Nil
    else
      val parameters = parametersOrDefault(operation.getParameters)
      val requestBody = requestBodyOrDefault(operation.getRequestBody)
      val responses = responsesOrDefault(operation.getResponses)
      responses.map { response =>
        TestCase(
          method,
          path,
          parameters,
          requestBody,
          response
        )
      }
}

object GenTestCase {

  def parametersOrDefault(
      parameters: util.List[Parameter] | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if parameters == null then default
    else
      parameters.asScala.toList.flatMap { parameter =>
        exampleOrDefault(
          parameter.getName,
          parameter.getExample,
          parameter.getRequired
        )
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
    if mediaType == null || isSkipContentType(name) then default
    else schemaOrDefault(None, mediaType.getSchema, default)

  def isSkipContentType(contentType: String): Boolean = false
//    contentType != "application/json"

  def schemaOrDefault[T](
      name: Option[String],
      schema: Schema[T] | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if schema == null then default
    else {
      schema match {
        case x: ArraySchema => {
          val name = getValueOrDefault(x.getName)
          schemaOrDefault(name, x.getItems)
        }
        case _ => propertiesOrDefault(name, schema.getProperties, default)
      }
    }

  def getValueOrDefault(value: String | Null): Option[String] =
    if value == null then None else Some(value)

  def propertiesOrDefault(
      propertyName: Option[String],
      properties: util.Map[String, Schema[_]] | Null,
      default: List[TestProperty] = Nil
  ): List[TestProperty] =
    if properties == null then default
    else
      properties.asScala.toList.flatMap { (name, schema) =>
        schema match {
          case x: ObjectSchema => schemaOrDefault(Some(name), x)
          case x: ArraySchema =>
            val name = getValueOrDefault(x.getName)
            schemaOrDefault(name, x.getItems)
          case _ => {
            val schemaName = createPropertyNameofObject(propertyName, name)
            propertyOrDefault(schemaName, schema)
          }
        }
      }

  def createPropertyNameofObject(
      objectName: Option[String],
      propertyName: String
  ): String =
    objectName.map(name => s"${name}.${propertyName}").getOrElse(propertyName)

  def createPropertyNameofArray(
      objectName: Option[String],
      propertyName: String
  ): String =
    objectName.map(name => s"${name}.${propertyName}").getOrElse(propertyName)

  def propertyOrDefault(
      name: String,
      property: Schema[_] | Null
  ): Option[TestProperty] =
    if property == null then None
    else {
      val required: List[String] =
        if property.getRequired == null then Nil
        else property.getRequired.asScala.toList
      exampleOrDefault(
        name,
        property.getExample,
        required.contains(name)
      )
    }

  def exampleOrDefault(
      name: String,
      example: Object | Null,
      required: Boolean
  ): Option[TestProperty] =
//    if example == null then None
//    else Some(TestProperty(name, s"${example}"))
    Some(TestProperty(name, s"${example}", required))

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
