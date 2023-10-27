val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "openapi-gen-doc",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "io.swagger.parser.v3" % "swagger-parser" % "2.1.16",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
