import scala.util._

object Util {
  def add(a: Int, b: Int): Int = {
    a + b
  }
}

class Util {
  def add(a: Int, b: Int): Int = {
    a + b
  }
}

trait Multiplier {
  def multiply(a: Int, b: Int): Int = {
    a * b
  }
}

case class Book(name: String)

object main {
  def main(args: Array[String]) = {
    println("Starting")
    // Try object's apply is used here
    // Intellij uses italics font to indicate that a object's apply() method is applied
    // here aTry is a Try[Unit]
    val aTry = Try {
      // Success("Success")
    }
    match {
      // object Util's add method is called here. Note the italics font used in intellij
      // here x and y are of type Unit
      case Success(x) => {
        println("Success")
        println(Util.add(1, 2))
      }
      case Failure(y) => {
        println("Failure")
        val m = new Object() with Multiplier
        Book("next")
        val u = new Util()
        println(m.multiply(3, 4))
      }
    }
    println("Ending")

    val ff: PartialFunction[Int, Int] = {
      case i: Int if i == 1 => i * i
    }


    println(ff.isDefinedAt(2))
  }

  println("object main's constructor method")

  def tt(): Unit = {
    println(Success(1).map(_ + 1).isSuccess)

    // Failure(new IllegalArgumentException("Wrong Argument")).map(_ + 1)
  }

  val pf : PartialFunction[Throwable,Try[Int]] = {
    case t: Throwable =>
      println("XXX cause is" + t.getMessage)
      Failure(t)
  }

  def dangerousDiv(denom: Int): Int = {
    1 / denom
  }

  val t1 = Try {
    dangerousDiv(0)
  }.map( x => println(s"Success $x")).recoverWith(pf)

  println(t1.isFailure)

  val t2 =Try {
    dangerousDiv(1)
  }.map( x => println(s"Success $x")).recoverWith(pf)
  println(t2.isFailure)

  import ImplicitEnv._

  println("Implicit conversion: " + 1.length)



  def mk : String = {
    "123"
  }

  println(mk)

  println("end of object main's constructor method")
}