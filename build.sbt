transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)

scalaVersion in ThisBuild := "2.12.8"

val paradiseVersion = "2.1.0"
val scalatestVersion = "3.0.5"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  ),
  addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full),
)

lazy val root = (project in file("."))
  .aggregate(lombok)
  .settings(
    name := "paradise-study"
  )

lazy val lombok = (project in file("lombok"))
  .settings(commonSettings)
  .settings(
  )

