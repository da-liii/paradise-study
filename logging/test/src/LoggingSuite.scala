package logging

import org.scalatest._
import org.slf4j.LoggerFactory

class LoggingSuite extends FunSuite {
  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  test("warn") {
    var cnt = 0
    def debugging: String = {
      cnt = cnt + 1
      "debugging"
    }

    logger.debug(s"This is a $debugging message")
    logger.underlying.debug(s"This is a $debugging message")

    logger.underlying.info(s"We have invoke debugging $cnt times")
  }
}
