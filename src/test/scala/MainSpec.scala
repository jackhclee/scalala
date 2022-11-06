import cats.effect.IO
import cats.{Monoid, Semigroup}
import cats.implicits.toFoldableOps
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.{options, wireMockConfig}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers.be
import org.scalatest.matchers.should
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import retry.RetryPolicies

import java.time.{LocalDate, LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.Locale
import scala.util.{Failure, Success, Try}
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import sttp.client3.quick._

import scala.concurrent.duration.FiniteDuration
import retry._
import retry.RetryDetails._
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.json4s.Extraction.decompose
import org.scalactic.TimesOnInt.convertIntToRepeater
import org.scalamock.scalatest.MockFactory
import org.json4s._
import org.json4s.jackson.JsonMethods._

import java.io.StringWriter

class MainSpec extends AnyWordSpec with Matchers with MockFactory {

  val keyApple: String = "APPLE"
  val keyBanana: String = "BANANA"
  val valApple: String = "123456"
  val valBanana: String = "7890"

  //  "Test 01 should " should {
  //    "comply" in {
  //      import ImplicitEnv._
  //
  //      1.length should equal(3)
  //      1.inEnglish should startWith("One")
  //      //        1.inEnglish should startWith ("One")
  //      //        40.inEnglish.length should equal(5)
  //      "Hello" should startWith("Hello")
  //    }
  //  }
  //
  //  "Test 02 exception" should {
  //    "make" in {
  //      val stringOpt: _root_.org.scalatest.Assertion = {
  //        import scala.util.Random
  //        val ran: _root_.scala.util.Random.type = Random
  //        the[ArithmeticException] thrownBy
  //          Some(1 / 0) should have message "/ by zero"
  //      }
  //    }
  //  }
  //
  //  "Test 03" should {
  //    "make" in {
  //      val a: Option[Int] = Some(1)
  //      val b: Option[Int] = None
  //
  //      val result = for {
  //        // j <- b
  //        i <- a
  //
  //      } yield i + 1
  //      result should be(Some(1))
  //
  //      val e: Either[String, Int] = Left("H")
  //
  //      val re = e.map((x) => 1.0)
  //
  //
  //      val re2 = e.left.flatMap((x) => Right(1.0))
  //
  //      val r = Right(12)
  //
  //      val t1 = Try(1)
  //
  //      for {
  //        t <- t1
  //      } yield t
  //
  //      val ft = for {
  //        t <- t1 if t > 9
  //      } yield t
  //
  //    }
  //  }
  //
  //  "Test 04" should {
  //    "a" in {
  //      import cats.implicits._
  //
  //      Semigroup[Int].combine(1, 2) should be(
  //        3
  //      )
  //      Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6)) should be(
  //        List(1, 2, 3, 4, 5, 6)
  //      )
  //      Semigroup[Option[Int]].combine(Option(1), Option(2)) should be(
  //        Option(3)
  //      )
  //      Semigroup[Option[Int]].combine(Option(1), None) should be(
  //        Some(1)
  //      )
  //
  //      Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6)
  //    }
  //  }
  //
  //  "Test 05" should {
  //    "should get something from Database" in {
  //      Database.get(keyApple) should be(Some(valApple))
  //      Database.set(keyBanana, valBanana)
  //      Database.get(keyBanana) should be(Some(valBanana))
  //    }
  //  }
  //
  //  "Test 06" should {
  //    "should get something from Service" in {
  //      Service.get(keyApple) should be(Some(valApple))
  //      Service.set(keyBanana, valBanana)
  //      Service.get(keyBanana) should be(Some(valBanana))
  //      val randomKeyVal = util.Random.nextString(5)
  //      Service.set(randomKeyVal, randomKeyVal)
  //      Service.get(randomKeyVal) should be(Some(randomKeyVal))
  //      Service.get(util.Random.nextString(5)) should be(None)
  //    }
  //  }
  //
  //  "Test 07" should {
  //    "should be knowing the identity" in {
  //      val l = List(1, 2, 3, 4, 5)
  //      l.foldMap(identity) should be(
  //        15
  //      )
  //      l.foldMap(i => i.toString) should be(
  //        "12345"
  //      )
  //    }
  //  }
  //
  //  "Test 08" should {
  //    "should be knowing the identity" in {
  //      Monoid[Int].combineAll(List(1, 2, 3, 4, 5)) should be(15)
  //
  //      val addArity2 = (a: Int, b: Int) => a + b
  //
  //      val addArity3 = (a: Int, b: Int, c: Int) => a + b + c
  //
  //      import cats.implicits._
  //      val option2 = (Option(1), Option(2))
  //      val option3 = (option2._1, option2._2, Option.empty[Int])
  //
  //      option2 mapN addArity2 should be(Some(3))
  //      option3 mapN addArity3 should be(None)
  //
  //      option2 apWith Some(addArity2) should be(Some(3))
  //      option3 apWith Some(addArity3) should be(None)
  //
  //      option2.tupled should be(Some((1, 2)))
  //      option3.tupled should be(None)
  //
  //    }
  //  }
  //
  //  "Test 09 Semigroup" should {
  //    "sca" in {
  //      import cats.implicits._
  //      Semigroup[Option[Int]].combine(Option(1), None) should be(
  //        Some(1)
  //      )
  //      Semigroup[Int => Int].combine({
  //        println("fa")
  //        _ + 1
  //      }, {
  //        println("fb")
  //        _ * 10
  //      }).apply(6) should be(
  //        67
  //      )
  //    }
  //  }
  //
  //  "Test 09 Monoid" should {
  //    "sca" in {
  //      import cats.implicits._
  //      Monoid[Int].combineAll(List(1, 2, 3)) should be(6)
  //
  //      Monoid[(Int, Int)].combine((1, 2), (3, 4)) should be((4, 6))
  //
  //      Monoid[(Int, Int)].combineAll(List((1, 2), (3, 4), (5, 6))) should be((9, 12))
  //
  //
  //      val eLeft: Either[String, Int] = Left[String, Int]("ABC").map(x => x + 1).leftFlatMap(x => Left(x + "!!!"))
  //
  //      val eRight: Either[String, Int] = Right[String, Int](1).map(x => x + 1).leftFlatMap(x => Left(x + "!!!"))
  //
  //      eLeft.left.get should be("ABC!!!")
  //
  //      eRight should be(Right(2))
  //
  //      val l1 = List(1, 2)
  //
  //      l1.flatMap(i => List(1)) should be(List(1, 1))
  //
  //      val o1 = Some(1)
  //      val o2 = Some(2)
  //      val vo = for {
  //        l <- l1
  //        o <- o1
  //        o <- o2
  //      } yield o
  //
  //      //      val eRight : Either[String, Int] = Right(1).flatMap(x => Right(1))
  //      //
  //      //      val v1 = Try(1/0).map(x => Try(1))
  //    }
  //  }
  //
  //  "Test 10" should {
  //    "xyz" in {
  //      def step1(in1: Option[Int]): Option[Int] = {
  //        in1.map(_ + 1)
  //      }
  //
  //      step1(Some(1))
  //        .map(_ + 1)
  //        .map(_ + 1) should be(Some(4))
  //
  //      step1(None)
  //        .map(_ + 1)
  //        .map(_ + 1) should be(None)
  //
  //      val r3 = step1(Some(1))
  //        .flatMap { x =>
  //          Option("A")
  //        }
  //
  //      r3.map(_ + 1) should be(Some("A1"))
  //    }
  //  }
  //
  //  "Test 11" should {
  //    "aaa" in {
  //      def abc(a: Int)(b: Int)(c: Int): Int = {
  //        a + b + c
  //      }
  //
  //      abc(1)(2)(3) should be(6)
  //    }
  //  }
  //
  //  def parseDateStr(dateStr: String): Try[LocalDate] = {
  //    Try(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
  //  }
  //
  //  val refDate = LocalDate.of(2022, 3, 22)
  //
  //  "Test 12" should {
  //    "bbb" in {
  //      parseDateStr("2022-03-22") should be(Success(refDate))
  //      parseDateStr("2022-22-03") shouldBe a[Failure[DateTimeParseException]]
  //      val r = Try(1 + 1).flatMap(x => Try(x / 1)).map(y => y / 0)
  //
  //      r shouldBe a[Failure[IllegalArgumentException]]
  //    }
  //  }
  //
  //  def mapDateISO(dateStr: String): Option[DateTime] = {
  //    println("ISO")
  //    Try(DateTime.parse(dateStr)).toOption
  //  }
  //
  //  def mapDateUS(dateStr: String): Option[DateTime] = {
  //    println("US")
  //    Try(DateTime.parse(dateStr, org.joda.time.format.DateTimeFormat.forPattern("MM-dd-yyyy"))).toOption
  //  }
  //
  //  def mapDateEU(dateStr: String): Option[DateTime] = {
  //    println("EU")
  //    Try(DateTime.parse(dateStr, org.joda.time.format.DateTimeFormat.forPattern("dd-MM-yyyy"))).toOption
  //  }
  //
  //
  //  def mapDateError(dateStr: String): Option[DateTime] = {
  //    println("ERROR")
  //    Try(DateTime.parse(dateStr, org.joda.time.format.DateTimeFormat.forPattern("yyyy-dd-MM"))).toOption
  //  }
  //
  //  def mapDateBestEffort(dateStr: String): Option[DateTime] = {
  //    mapDateISO(dateStr)
  //      .orElse(mapDateUS(dateStr))
  //      .orElse(mapDateEU(dateStr)).orElse(mapDateError(dateStr))
  //  }
  //
  //  val dateRegex = raw"\d{1,4}-\d{1,4}-\d{1,4}".r
  //
  //  def testParseDate(dateStr: String)(implicit expeected: DateTime): Unit = {
  //    mapDateBestEffort(dateStr).get should be(expeected)
  //    println("===")
  //  }
  //
  //  "Test 13" should {
  //    "ccc" in {
  //
  //      implicit val expectedDate = new DateTime(2022, 3, 22, 0, 0)
  //
  //      testParseDate("2022-03-22")
  //
  //      testParseDate("03-22-2022")
  //
  //      testParseDate("22-03-2022")
  //
  //      testParseDate("2022-22-03")
  //
  //      testParseDate(dateRegex.findFirstIn("This is something 2022-03-22/2099-09-09").get)
  //
  //      testParseDate(dateRegex.findFirstIn("This is something 03-22-2022/2099-09-09").get)
  //
  //      testParseDate(dateRegex.findFirstIn("This is something 22-03-2022/2099-09-09").get)
  //
  //      testParseDate(dateRegex.findFirstIn("This is something 2022-22-03/2099-09-09").get)
  //
  //    }
  //  }
  //
  //  "Test 14" should {
  //    "ddd" in {
  //      implicit val expectedDate = new DateTime(2022, 3, 22, 0, 0)
  //
  //      val frenchStyle = DateTimeFormat.patternForStyle("S-", Locale.FRENCH)
  //
  //      DateTimeFormat.forPattern(frenchStyle).print(expectedDate)  should be ("22/03/2022")
  //
  //      val germanStyle = DateTimeFormat.patternForStyle("S-", Locale.GERMAN)
  //
  //      DateTimeFormat.forPattern(germanStyle).print(expectedDate)  should be ("22.03.22")
  //
  //      val usStyle = DateTimeFormat.patternForStyle("S-", Locale.US)
  //
  //      DateTimeFormat.forPattern(usStyle).print(expectedDate)  should be ("3/22/22")
  //    }
  //  }
  //
  //  case class Book(title: String, isbn: Option[Isbn])
  //  case class Isbn(sys: String, code: Option[String])
  //
  //
  //  "Test 15" should {
  //    "eee" in {
  //      val book = Book("HK", Some(Isbn("OLD", Some("1234-5678-9"))))
  //      book.isbn.map(isbn => {
  //        Isbn("OLD", Some("1234-5678-99"))
  //      }) should be (Some(Isbn("OLD", Some("1234-5678-99"))))
  //
  //      val book2 = Book("HK", Some(Isbn("OLD", Some("1234-5678-99"))))
  //      book2.isbn.flatMap(isbn => {
  //        isbn.code.map(code => code)
  //      }) should be (Some("1234-5678-99"))
  //    }
  //  }
  //
  //  "Test 16" should {
  //    "fff" in {
  //      val strList = List("A","BB","CCC")
  //      implicit val num: Int = 1000
  //      def a(a: Int)(implicit  b: Int) = a + b
  //      val mn = a(100)
  //      println(mn)
  //      strList
  //        .foreach((s: String) => Predef.println())
  //
  //      strList.flatMap( c => c).foreach(print)
  //    }
  //  }
  //
  //  "Test 17" should {
  //    "abcd" in {
  //      val a = Some("String")
  //      val b = Some("Again")
  //      val o_map = for {
  //        x <- a
  //        y <- x
  //      } {
  //        print (y)
  //        print ("#")
  //      }
  //
  //      //o_map.shouldBe(Some("String!"))
  //
  //      List("A","B","C").map(_.toLowerCase).shouldBe(List("a","b","c"))
  //
  //      "ABCD".flatMap(x => "X").shouldBe ("XXXX")
  //      "ABCD".map(x => "X").shouldBe ("XYZ")
  //
  //      class Food {
  //        protected val foodType = "Cake";
  //
  //      }
  //
  //      class Cake extends Food {
  //        val filling = "Red Bean"
  //      };
  //
  //      object aCake extends Cake {
  //        def printCakeFilling: Unit = {
  //          println(this.filling)
  //          println(this.foodType)
  //        }
  //      }
  //
  //
  //
  //    }
  //  }

//  "Test 18" should {
//    "First download fails" in {
//
//      val wireMockServer = new WireMockServer(options().port(8089)
//        .usingFilesUnderClasspath("wiremock")
//      ) //No-args constructor will start on port 8080, no HTTPS
//
//      wireMockServer.start()
//
//      val request = basicRequest
//        .get(uri"http://127.0.0.1:8089/sample")
//
//      val retryFiveTimes = RetryPolicies.limitRetries[IO](5)
//
//      val logMessages = collection.mutable.ArrayBuffer.empty[String]
//      // logMessages: collection.mutable.ArrayBuffer[String] = ArrayBuffer(
//      //   "Failed to download. So far we have retried 0 times.",
//      // )
//
//      def logError(err: Throwable, details: RetryDetails): IO[Unit] = details match {
//
//        case WillDelayAndRetry(nextDelay: FiniteDuration,
//        retriesSoFar: Int,
//        cumulativeDelay: FiniteDuration) =>
//          IO {
//            logMessages.append(
//              s"Failed to download. So far we have retried $retriesSoFar times.")
//          }
//
//        case GivingUp(totalRetries: Int, totalDelay: FiniteDuration) =>
//          IO {
//            logMessages.append(s"Giving up after $totalRetries retries")
//          }
//
//      }
//
//      // Now we have a retry policy and an error handler, we can wrap our `IO` inretries.
//
//      val flakyRequest: IO[String] = IO {
//        println("calling remote API")
//        request.send(backend).body match {
//          case Left(error) => throw new Error(s"Error when executing request:")
//          case Right(data) =>
//            data
//        }
//      }
//
//      val flakyRequestWithRetry: IO[String] =
//        retryingOnAllErrors[String](
//          policy = retryFiveTimes,
//          onError = logError
//        )(flakyRequest)
//
//      val result = flakyRequestWithRetry.unsafeRunSync()
//
//      logMessages.foreach(println)
//
//
//
//      // Failed to download. So far we have retried 0 times.
//
//      WireMock.configureFor("127.0.0.1", 8089)
//      WireMock.reset()
//
//      wireMockServer.shutdown();
//
//      result should be("This is Step 2")
//    }
//
//    "Second download fails" in {
//
//      val wireMockServer = new WireMockServer(options().port(9000)
//        .usingFilesUnderClasspath("wiremock")
//      ) //No-args constructor will start on port 8080, no HTTPS
//
//      wireMockServer.start()
//
//      val request = basicRequest
//        .get(uri"http://127.0.0.1:9000/scenario-2")
//
//      val retryFiveTimes = RetryPolicies.limitRetries[IO](3)
//
//      val logMessages = collection.mutable.ArrayBuffer.empty[String]
//      // logMessages: collection.mutable.ArrayBuffer[String] = ArrayBuffer(
//      //   "Failed to download. So far we have retried 0 times.",
//      // )
//
//      def logError(err: Throwable, details: RetryDetails): IO[Unit] = details match {
//
//        case WillDelayAndRetry(nextDelay: FiniteDuration,
//        retriesSoFar: Int,
//        cumulativeDelay: FiniteDuration) =>
//          IO {
//            logMessages.append(
//              s"Failed to download. So far we have retried $retriesSoFar times.")
//          }
//
//        case GivingUp(totalRetries: Int, totalDelay: FiniteDuration) =>
//          IO {
//            logMessages.append(s"Giving up after $totalRetries retries")
//          }
//
//      }
//
//      // Now we have a retry policy and an error handler, we can wrap our `IO` inretries.
//
//      val flakyRequest: IO[String] = IO {
//        println("calling remote API")
//        println("==================")
//        request.send(backend).body match {
//          case Left(error) => throw new Error(s"Error when executing request:")
//          case Right(data) =>
//            data
//        }
//      }
//
//      val flakyRequestWithRetry: IO[String] =
//        retryingOnAllErrors[String](
//          policy = retryFiveTimes,
//          onError = logError
//        )(flakyRequest)
//
//      val result = flakyRequestWithRetry.unsafeRunSync()
//
//      logMessages.foreach(println)
//
//      // Failed to download. So far we have retried 0 times.
//
//      WireMock.configureFor("127.0.0.1", 9000)
//
//      wireMockServer.shutdown();
//
//      result should be("This is Step 1")
//    }
//  }

//  "Test 20" should {
//    "Second download fails" in {
//
//      val wireMockServer = new WireMockServer(options().port(10000)
//        .usingFilesUnderClasspath("wiremock")
//      ) //No-args constructor will start on port 8080, no HTTPS
//
//      wireMockServer.start()
//
//
//      val retryFiveTimes = RetryPolicies.limitRetries[IO](3)
//
//      val logMessages = collection.mutable.ArrayBuffer.empty[String]
//      // logMessages: collection.mutable.ArrayBuffer[String] = ArrayBuffer(
//      //   "Failed to download. So far we have retried 0 times.",
//      // )
//
//      def logError(err: Throwable, details: RetryDetails): IO[Unit] = details match {
//
//        case WillDelayAndRetry(nextDelay: FiniteDuration,
//        retriesSoFar: Int,
//        cumulativeDelay: FiniteDuration) =>
//          IO {
//            logMessages.append(
//              s"Failed to download. So far we have retried $retriesSoFar times.")
//          }
//
//        case GivingUp(totalRetries: Int, totalDelay: FiniteDuration) =>
//          IO {
//            logMessages.append(s"Giving up after $totalRetries retries")
//          }
//
//      }
//
//      // Now we have a retry policy and an error handler, we can wrap our `IO` inretries.
//
//      val flakyRequest: IO[java.io.File] = IO {
//        println("calling remote API")
//
//        val request = basicRequest
//          .get(uri"http://127.0.0.1:10000/scenario-4")
//
//        //        val file = java.io.File.createTempFile("temp", "xyz")
//        val file = new java.io.File("abc.tar")
//        //        file.createNewFile()
//        println(file.getAbsolutePath())
//
//        request.response(asFile(file))
//        val response = request.send(backend)
//
//        response.headers.foreach(println)
//        val responseBody = response.body
//
//        println(file.length())
//        responseBody match {
//          case Left(error) => throw new Exception(s"Error when executing request:")
//          case Right(data) =>
//            println(s"data.length ${data.length}")
//            println(file.length())
//            file
//        }
//      }
//
//      val flakyRequestWithRetry: IO[java.io.File] =
//        retryingOnAllErrors[java.io.File](
//          policy = retryFiveTimes,
//          onError = logError
//        )(flakyRequest)
//
//      import better.files.{File => BetterFile, _}
//      val result = flakyRequestWithRetry.unsafeRunSync()
//
//      println(result.length())
//      println(BetterFile(result.getPath).md5)
//
//      logMessages.foreach(println)
//
//      // Failed to download. So far we have retried 0 times.
//
//      WireMock.configureFor("127.0.0.1", 10000)
//
//      wireMockServer.shutdown();
//
//      result should be("This is Step 1")
//    }
//
//    "Simple" in {
//      val wireMockServer = new WireMockServer(options().port(10000)
//        .usingFilesUnderClasspath("wiremock")
//      ) //No-args constructor will start on port 8080, no HTTPS
//
//      wireMockServer.start()
//
//
//      // Now we have a retry policy and an error handler, we can wrap our `IO` inretries.
//
//      println("calling remote API")
//
//      val file = new java.io.File("abc.tar")
//
//      val request = basicRequest
//        .get(uri"http://127.0.0.1:8080/TARA.tar")
//
//      //        val file = java.io.File.createTempFile("temp", "xyz")
//
//      //        file.createNewFile()
//      println(file.getAbsolutePath())
//
//      val response = request.response(asFile(file)).send(backend)
//
//      response.headers.foreach(println)
//      val responseBody = response.body
//
//      println(file.length())
//      responseBody match {
//        case Left(error) => throw new Exception(s"Error when executing request:")
//        case Right(data) =>
//          println(s"data.length ${data.length}")
//          println(file.length())
//      }
//
//      import better.files.{File => BetterFile, _}
//
//      println(file.length())
//
//      // Failed to download. So far we have retried 0 times.
//
//      WireMock.configureFor("127.0.0.1", 10000)
//
//      wireMockServer.shutdown();
//
//      BetterFile(file.getPath).md5 should be("This is Step 1")
//    }
//
//  }

  "Mock Object" should {
    "abc" in {
      trait Reporter {
        def report : String
        def ack : Unit
      }

      val mockReporter = mock[Reporter]

      //(mockReporter.report _).when().returns("ABCD")

      (mockReporter.report _).expects().returning("ABCD").once()
      (mockReporter.ack _).expects().once()


      1 times mockReporter.report

      1 times mockReporter.ack

//      (mockReporter.report _).verify()
//      (mockReporter.ack _).verify()

      val o1 = List(Some(1),Some(2),None)

      val r = for {
        o <- o1
        x <- o
      } yield x + 1

      r shouldBe List(2,3)

      val r2 = o1.flatMap((x) => x.map((y) => y + 1))

      r2 shouldBe List(2,3)


    }
  }

  case class Person(`@id`: String, personInfo: PersonInfo)
  case class PersonInfo(displayName: String, tags: Seq[Tag])
  case class Tag(label: String)

  case class Book(title: String, price: Int)

  "json4s" should {
    "bbb" in {
      implicit val format = DefaultFormats;

      val str =
        """
          |{
          |  "@id" : "Jack",
          |  "personInfo" : {
          |    "displayName" : "Jack Lee",
          |    "age" : 99,
          |    "tags" : [ { "label" : "GOLD" } ]
          |  }
          |}
          |""".stripMargin

      val p = parse(str).extract[Person]

      val j = parse(str)
      val j1 = j \ "@id"

      p.`@id` shouldBe "Jack"
      p.personInfo.displayName shouldBe "Jack Lee"
      p.personInfo.tags.size shouldBe 1

      import org.json4s.JsonDSL._

      compact(decompose(Book("Green", 99))) shouldBe "{\"title\":\"Green\",\"price\":99}"

      val xp =
        <Person><Name>Jack</Name></Person>

      val strWriter = new StringWriter()
      scala.xml.XML.write(strWriter, xp, "", false, null)

      strWriter.toString.replace("\r","").replace("\n","") shouldBe "<Person><Name>Jack</Name></Person>"
  }
  }

  "Computer" should {
    "add" in {
      Computer.add(1,2) shouldBe 3
    }
    "diff a smaller than b" in {
      Computer.diff(1, 2) shouldBe 1
    }
    "diff a larger than b" in {
      Computer.diff(2, 1) shouldBe 1
    }
  }
}
