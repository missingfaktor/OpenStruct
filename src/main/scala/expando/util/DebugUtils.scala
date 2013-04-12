package expando.util

object DebugUtils {
  implicit class Tapper[A](val underlying: A) extends AnyVal {
    def tap(message: String = "Object"): A = {
      println(s"$message: $underlying")
      underlying
    }
  }
}
