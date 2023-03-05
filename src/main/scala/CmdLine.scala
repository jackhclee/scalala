import cats.instances.EitherInstances
import org.json4s.jackson.Serialization

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object CmdLine {
  def main(args: Array[String]) = {
    println("Happy")

    val list1 = List(1, 2, 3)
    val list1_1 = for {
      l <- list1
    } yield l + 1

    list1_1.foreach(print)
    println()

    val list2 = List(Some(1), None, Some(3))
    val list2_1 = for {
      l_some <- list2
      l <- l_some
    } yield l + 1

    list2_1.foreach(print)

    println()

    val v = Some(1).flatMap(_ => {
      print("run");
      None
    })

    print(v)

    val v1 = None.flatMap(_ => {
      print("run");
      None
    })

    print(v1)

    println()
    class Bun[T] {
      def map(t: T => Int) = 1
    }

    val bun = new Bun[Int]

    val b_result = for {
      b <- bun
    } yield b

    println(b_result)

    val list3 = List(1,2,3)

    val list3_1 = list3.withFilter( _ % 2 == 0)
    println(list3_1)

    import org.json4s._
    import org.json4s.jackson.JsonMethods._

    val n1 = parse(""" { "numbers" : [1, 2, 3, 4] } """)

    println(n1)
    val n2 = parse("""{"name":"Toy","price":35.35}""", useBigDecimalForDouble = true)

    println(n2)

    case class Person(name: String, children: Seq[Child])
    case class Child(name: String, age: Int, alias: Option[String], zonedDateTime: ZonedDateTime)


    import org.json4s.jackson.Serialization
    import org.json4s.jackson.Serialization.{read, write}

    implicit val formats: Formats = DefaultFormats ++ org.json4s.ext.JavaTimeSerializers.all//Serialization.formats(NoTypeHints)

    val ser = write(Person("John",List(Child("Mary", 5, None, ZonedDateTime.now()))))

    println(ser)

    println(read[Person](ser))

  }

}


