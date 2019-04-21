package logging

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object Logger {
  def apply(underlying: org.slf4j.Logger): Logger =
    new Logger(underlying)
}

final class Logger private (val underlying: org.slf4j.Logger) {
  def debug(message: String): Unit = macro LoggerMacro.debugMessage
}

private object LoggerMacro {

  type LoggerContext = blackbox.Context {type PrefixType = Logger}

  /** Checks whether `message` is an interpolated string and transforms it into SLF4J string interpolation. */
  private def deconstructInterpolatedMessage(c: LoggerContext)(message: c.Expr[String]) = {
    import c.universe._

    message.tree match {
      case q"scala.StringContext.apply(..$parts).s(..$args)" =>
        val format = parts.iterator.map({ case Literal(Constant(str: String)) => str })
          // Emulate standard interpolator escaping
          .map(StringContext.treatEscapes)
          // Escape literal slf4j format anchors if the resulting call will require a format string
          .map(str => if (args.nonEmpty) str.replace("{}", "\\{}") else str)
          .mkString("{}")

        val formatArgs = args.map(t => c.Expr[Any](t))

        (c.Expr(q"$format"), formatArgs)

      case _ => (message, Seq.empty)
    }
  }

  private def formatArgs(c: LoggerContext)(args: c.Expr[Any]*) = {
    import c.universe._
    args.map { arg =>
      c.Expr[AnyRef](if (arg.tree.tpe <:< weakTypeOf[AnyRef]) arg.tree else q"$arg.asInstanceOf[_root_.scala.AnyRef]")
    }
  }

  def debugMessageArgs(c: LoggerContext)(message: c.Expr[String], args: c.Expr[Any]*): c.universe.Tree = {
    import c.universe._
    val underlying = q"${c.prefix}.underlying"
    val anyRefArgs = formatArgs(c)(args: _*)
    if (args.length == 2)
      q"if ($underlying.isDebugEnabled) $underlying.debug($message, _root_.scala.Array(${anyRefArgs.head}, ${anyRefArgs(1)}): _*)"
    else
      q"if ($underlying.isDebugEnabled) $underlying.debug($message, ..$anyRefArgs)"
  }

  def debugMessage(c: LoggerContext)(message: c.Expr[String]): c.universe.Tree = {
    val (messageFormat, args) = deconstructInterpolatedMessage(c)(message)
    debugMessageArgs(c)(messageFormat, args: _*)
  }
}

