import mill._, scalalib._

object logging extends ScalaModule {
  def scalaVersion = "2.13.1"

  def scalacOptions = Seq("-deprecation", "-Ymacro-annotations")

  def ivyDeps = Agg(
    ivy"org.scala-lang:scala-reflect:2.13.1",
    ivy"org.slf4j:slf4j-api:1.7.32",
  )

  object test extends Tests with TestModule.ScalaTest {
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.0.8",
      ivy"ch.qos.logback:logback-classic:1.2.3"
    )
  }
}

object lombok extends ScalaModule {
  def scalaVersion = "2.13.1"

  def scalacOptions = Seq("-deprecation", "-Ymacro-annotations")

  def ivyDeps = Agg(
    ivy"org.scala-lang:scala-reflect:2.13.1",
  )

  object test extends Tests with TestModule.ScalaTest {
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.0.8",
    )
  }
}

