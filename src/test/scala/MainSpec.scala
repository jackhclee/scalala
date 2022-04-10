import cats.{Monoid, Semigroup}
import cats.implicits.toFoldableOps
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers.be
import org.scalatest.matchers.should
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.{LocalDate, LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.Locale
import scala.util.{Failure, Success, Try}


class MainSpec extends AnyWordSpec with Matchers {

  val keyApple = "APPLE"
  val keyBanana = "BANANA"
  val valApple = "123456"
  val valBanana = "7890"

  "Test 01 should " should {
    "comply" in {
      import ImplicitEnv._

      1.length should equal(3)
      1.inEnglish should startWith("One")
      //        1.inEnglish should startWith ("One")
      //        40.inEnglish.length should equal(5)
      "Hello" should startWith("Hello")
    }
  }

  "Test 02 exception" should {
    "make" in {
      val stringOpt = {
        import scala.util.Random
        val ran = Random
        the[ArithmeticException] thrownBy
          Some(1 / 0) should have message "/ by zero"
      }
    }
  }

  "Test 03" should {
    "make" in {
      val a: Option[Int] = Some(1)
      val b: Option[Int] = None

      val result = for {
        // j <- b
        i <- a

      } yield i + 1
      result should be(Some(1))

      val e: Either[String, Int] = Left("H")

      val re = e.map((x) => 1.0)


      val re2 = e.left.flatMap((x) => Right(1.0))

      val r = Right(12)

      val t1 = Try(1)

      for {
        t <- t1
      } yield t

      val ft = for {
        t <- t1 if t > 9
      } yield t

    }
  }

  "Test 04" should {
    "a" in {
      import cats.implicits._

      Semigroup[Int].combine(1, 2) should be(
        3
      )
      Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6)) should be(
        List(1, 2, 3, 4, 5, 6)
      )
      Semigroup[Option[Int]].combine(Option(1), Option(2)) should be(
        Option(3)
      )
      Semigroup[Option[Int]].combine(Option(1), None) should be(
        Some(1)
      )

      Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6)
    }
  }

  "Test 05" should {
    "should get something from Database" in {
      Database.get(keyApple) should be(Some(valApple))
      Database.set(keyBanana, valBanana)
      Database.get(keyBanana) should be(Some(valBanana))
    }
  }

  "Test 06" should {
    "should get something from Service" in {
      Service.get(keyApple) should be(Some(valApple))
      Service.set(keyBanana, valBanana)
      Service.get(keyBanana) should be(Some(valBanana))
      val randomKeyVal = util.Random.nextString(5)
      Service.set(randomKeyVal, randomKeyVal)
      Service.get(randomKeyVal) should be(Some(randomKeyVal))
      Service.get(util.Random.nextString(5)) should be(None)
    }
  }

  "Test 07" should {
    "should be knowing the identity" in {
      val l = List(1, 2, 3, 4, 5)
      l.foldMap(identity) should be(
        15
      )
      l.foldMap(i => i.toString) should be(
        "12345"
      )
    }
  }

  "Test 08" should {
    "should be knowing the identity" in {
      Monoid[Int].combineAll(List(1, 2, 3, 4, 5)) should be(15)

      val addArity2 = (a: Int, b: Int) => a + b

      val addArity3 = (a: Int, b: Int, c: Int) => a + b + c

      import cats.implicits._
      val option2 = (Option(1), Option(2))
      val option3 = (option2._1, option2._2, Option.empty[Int])

      option2 mapN addArity2 should be(Some(3))
      option3 mapN addArity3 should be(None)

      option2 apWith Some(addArity2) should be(Some(3))
      option3 apWith Some(addArity3) should be(None)

      option2.tupled should be(Some((1, 2)))
      option3.tupled should be(None)

    }
  }

  "Test 09 Semigroup" should {
    "sca" in {
      import cats.implicits._
      Semigroup[Option[Int]].combine(Option(1), None) should be(
        Some(1)
      )
      Semigroup[Int => Int].combine({
        println("fa")
        _ + 1
      }, {
        println("fb")
        _ * 10
      }).apply(6) should be(
        67
      )
    }
  }

  "Test 09 Monoid" should {
    "sca" in {
      import cats.implicits._
      Monoid[Int].combineAll(List(1, 2, 3)) should be(6)

      Monoid[(Int, Int)].combine((1, 2), (3, 4)) should be((4, 6))

      Monoid[(Int, Int)].combineAll(List((1, 2), (3, 4), (5, 6))) should be((9, 12))


      val eLeft: Either[String, Int] = Left[String, Int]("ABC").map(x => x + 1).leftFlatMap(x => Left(x + "!!!"))

      val eRight: Either[String, Int] = Right[String, Int](1).map(x => x + 1).leftFlatMap(x => Left(x + "!!!"))

      eLeft.left.get should be("ABC!!!")

      eRight should be(Right(2))

      val l1 = List(1, 2)

      l1.flatMap(i => List(1)) should be(List(1, 1))

      val o1 = Some(1)
      val o2 = Some(2)
      val vo = for {
        l <- l1
        o <- o1
        o <- o2
      } yield o

      //      val eRight : Either[String, Int] = Right(1).flatMap(x => Right(1))
      //
      //      val v1 = Try(1/0).map(x => Try(1))
    }
  }

  "Test 10" should {
    "xyz" in {
      def step1(in1: Option[Int]): Option[Int] = {
        in1.map(_ + 1)
      }

      step1(Some(1))
        .map(_ + 1)
        .map(_ + 1) should be(Some(4))

      step1(None)
        .map(_ + 1)
        .map(_ + 1) should be(None)

      val r3 = step1(Some(1))
        .flatMap { x =>
          Option("A")
        }

      r3.map(_ + 1) should be(Some("A1"))
    }
  }

  "Test 11" should {
    "aaa" in {
      def abc(a: Int)(b: Int)(c: Int): Int = {
        a + b + c
      }

      abc(1)(2)(3) should be(6)
    }
  }

  def parseDateStr(dateStr: String): Try[LocalDate] = {
    Try(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
  }

  val refDate = LocalDate.of(2022, 3, 22)

  "Test 12" should {
    "bbb" in {
      parseDateStr("2022-03-22") should be(Success(refDate))
      parseDateStr("2022-22-03") shouldBe a[Failure[DateTimeParseException]]
      val r = Try(1 + 1).flatMap(x => Try(x / 1)).map(y => y / 0)

      r shouldBe a[Failure[IllegalArgumentException]]
    }
  }

  def mapDateISO(dateStr: String): Option[DateTime] = {
    println("ISO")
    Try(DateTime.parse(dateStr)).toOption
  }

  def mapDateUS(dateStr: String): Option[DateTime] = {
    println("US")
    Try(DateTime.parse(dateStr, org.joda.time.format.DateTimeFormat.forPattern("MM-dd-yyyy"))).toOption
  }

  def mapDateEU(dateStr: String): Option[DateTime] = {
    println("EU")
    Try(DateTime.parse(dateStr, org.joda.time.format.DateTimeFormat.forPattern("dd-MM-yyyy"))).toOption
  }


  def mapDateError(dateStr: String): Option[DateTime] = {
    println("ERROR")
    Try(DateTime.parse(dateStr, org.joda.time.format.DateTimeFormat.forPattern("yyyy-dd-MM"))).toOption
  }

  def mapDateBestEffort(dateStr: String): Option[DateTime] = {
    mapDateISO(dateStr)
      .orElse(mapDateUS(dateStr))
      .orElse(mapDateEU(dateStr)).orElse(mapDateError(dateStr))
  }

  val dateRegex = raw"\d{1,4}-\d{1,4}-\d{1,4}".r

  def testParseDate(dateStr: String)(implicit expeected: DateTime): Unit = {
    mapDateBestEffort(dateStr).get should be(expeected)
    println("===")
  }

  "Test 13" should {
    "ccc" in {

      implicit val expectedDate = new DateTime(2022, 3, 22, 0, 0)

      testParseDate("2022-03-22")

      testParseDate("03-22-2022")

      testParseDate("22-03-2022")

      testParseDate("2022-22-03")

      testParseDate(dateRegex.findFirstIn("This is something 2022-03-22/2099-09-09").get)

      testParseDate(dateRegex.findFirstIn("This is something 03-22-2022/2099-09-09").get)

      testParseDate(dateRegex.findFirstIn("This is something 22-03-2022/2099-09-09").get)

      testParseDate(dateRegex.findFirstIn("This is something 2022-22-03/2099-09-09").get)

    }
  }

  "Test 14" should {
    "ddd" in {
      implicit val expectedDate = new DateTime(2022, 3, 22, 0, 0)

      val frenchStyle = DateTimeFormat.patternForStyle("S-", Locale.FRENCH)

      DateTimeFormat.forPattern(frenchStyle).print(expectedDate)  should be ("22/03/2022")

      val germanStyle = DateTimeFormat.patternForStyle("S-", Locale.GERMAN)

      DateTimeFormat.forPattern(germanStyle).print(expectedDate)  should be ("22.03.22")

      val usStyle = DateTimeFormat.patternForStyle("S-", Locale.US)

      DateTimeFormat.forPattern(usStyle).print(expectedDate)  should be ("3/22/22")
    }
  }
}
