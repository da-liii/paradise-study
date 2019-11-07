transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)

scalaVersion in ThisBuild := "2.13.1"

val paradiseVersion = "2.1.0"
val scalatestVersion = "3.0.8"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  ),
  scalacOptions ++= Seq("-deprecation", "-Ymacro-annotations")
  // addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full),
)

lazy val root = (project in file("."))
  .aggregate(lombok, logging)
  .settings(
    name := "paradise-study"
  )

lazy val lombok = (project in file("lombok"))
  .settings(commonSettings)
  .settings(
  )

lazy val logging = (project in file("logging"))
  .settings(commonSettings)
  .settings()

