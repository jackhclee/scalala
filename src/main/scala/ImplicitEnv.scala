


object ImplicitEnv {

  implicit class Eng(in: Int) {
    val inEnglish = makeImplicitString(in)
  }

  def make(i: Int): String = {
    i match {
      case i if i == 1 => "One"
      case i if i == 40 => "Forty"
    }
  }

  implicit def makeImplicitString(i: Int): String = {
    i match {
      case i if i == 1 => "One"
      case i if i == 40 => "Forty"
    }
  }
}
