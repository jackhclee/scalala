import scala.util._

object Util {

  val name = "Lam"

  def odd() = {
    println("Example")
  }
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
  def main(args: Array[String]): Unit = {
    println("Starting")
    // Try object's apply is used here
    // Intellij uses italics font to indicate that a object's apply() method is applied
    // here aTry is a Try[Unit]
    val aTry = Try {
      // Success("Success")
    } match {
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

  val pf: PartialFunction[Throwable, Try[Int]] = { case t: Throwable =>
    println("XXX cause is" + t.getMessage)
    Failure(t)
  }

  def dangerousDiv(denom: Int): Int = {
    1 / denom
  }

  val t1: Try[AnyVal] = Try {
    dangerousDiv(0)
  }.map(x => println(s"Success $x")).recoverWith(pf)

  println(t1.isFailure)

  val t2: Try[AnyVal] = Try {
    dangerousDiv(1)
  }.map(x => println(s"Success $x")).recoverWith(pf)
  println(t2.isFailure)

  import ImplicitEnv._

  println("Implicit conversion: " + 1.inEnglish)

  def mk: String = {
    "123"
  }

  println(mk)

  val r1 = Right(1)
  val r2 = Right(2)
  val r3 : Either[Double, Int] = Left(33.0D)  // Right(3)

  println(s"Left flatMap ${r2.flatMap(_ => Left(88.0))}")

  val s1 = for {
    a1 <- r1
    a2 <- r2
    a3 <- r3
  } yield a1 + a2 + a3

  val s2 = r1
    .flatMap((a1: Int) =>
      r2.flatMap((a2: Int) =>
          r3.map((a3: Int) => a1.+(a2.+(a3)))
        )
    )
  println(s"s1 $s1")
  println(s"s2 $s2")

  val q1 : Either[Double, Int] = Left(99.0D)

  println(s"q1.map ${q1.map(_ + 1)}")
  println(s"q1.flatMap ${q1.flatMap(_ => Right(99))}")

  val q2: Either[Double, Int] = Right(99)

  println(s"q2.map ${q2.map(_ + 1)}")
  println(s"q2.flatMap ${q2.flatMap(_ => Right(99))}")

  //val l1 = List(Some(1), Some(2), None)
  val l1 = List(Right(1), Right(2), Left(33.0D))
  val a_l1 = for {
    a1 <- l1
  } yield a1

  println(a_l1)

  println("end of object main's constructor method")
}
