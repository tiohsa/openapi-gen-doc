val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .enablePlugins(SbtTwirl)
  .settings(
    name := "openapi-gen-doc",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "io.swagger.parser.v3" % "swagger-parser" % "2.1.16",
      "com.github.tototoshi" %% "scala-csv" % "1.3.10",
//      "com.typesafe.play" %% "twirl-api" % "1.6.2",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    ),
    TwirlKeys.templateImports += "generator.models._"
//    Compile / TwirlKeys.compileTemplates / sourceDirectories := (Compile / unmanagedSourceDirectories).value
  )
