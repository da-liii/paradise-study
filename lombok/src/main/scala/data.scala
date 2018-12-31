package lombok
import scala.reflect.macros.blackbox
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

object dataMacro {
  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = {
      annottees.map(_.tree).toList match {
        case q"""
              class $name {
                ..$vars
              }
              """ :: Nil =>

          // Generate the Getter and Setter from VarDefs
          val beanMethods = vars.collect {
            case q"$mods var $name: $tpt = $expr" =>
              val getName = TermName("get" + name.encodedName.toString.capitalize)
              val setName = TermName("set" + name.encodedName.toString.capitalize)
              println(getName)
              val ident = Ident(name)
              List (
                q"def $getName: $tpt = $ident",
                q"def $setName(paramX: $tpt): Unit = { $ident = paramX }"
              )
          }.flatten

          // Insert the generated Getter and Setter
          q"""
             class $name {
               ..$vars
               ..$beanMethods
             }
           """
        case _ =>
          throw new Exception("Macro Error")
      }
    }
    c.Expr[Any](result)
  }
}

class data extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro dataMacro.impl
}
