package expando.util

import scala.reflect.runtime.universe._

trait Reflect {
  def getField(name: String): Any
  def invokeMethod(name: String, args: Any*): Any
}

object Reflect {
  def on(obj: Any) = new Reflect {
    private def klass = obj.getClass

    private def classLoaderMirror = runtimeMirror(klass.getClassLoader)

    private def instanceMirror = classLoaderMirror.reflect(obj)

    private def member(name: String): Symbol = {
      val classSymbol = classLoaderMirror.classSymbol(klass)
      val classType = classSymbol.typeSignature
      classType.member(newTermName(name))
    }

    def invokeMethod(name: String, args: Any*): Any = {
      instanceMirror.reflectMethod(member(name).asMethod).apply(args: _*)
    }

    def getField(name: String): Any = {
      instanceMirror.reflectField(member(name).asTerm).get
    }
  }
}
