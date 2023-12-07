import org.mockito.MockitoSugar.doReturn
import org.mockito.Strictness
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.matchers.should.Matchers._
import org.mockito.scalatest.AsyncIdiomaticMockito

import scala.concurrent.Future

class AsyncMainSpec extends AsyncWordSpec with AsyncIdiomaticMockito {

  def getFutureAnswer = Future({println("going to print 1"); 1})

  "An object" when {
    "created" should {
      "works" in {
        implicit val strictness: Strictness = Strictness.Lenient
        val b = new BasicMachine()
        val m = spy(b, lenient = true)
        "Special Beep wa!" willBe returned by m.beep("wa", 1)
        val p = new Person(m)
        p.pressBeep("ha") shouldBe "Beep ha"
        p.pressBeep("ma")
        p.pressBeep("ha")
        p.pressBeep("wa") shouldBe "Special Beep wa!"
        m.beep("ha", 1) wasCalled 2.times
        m.beep("ma", any) wasCalled 1.times
      }
      "successfully eventually" in {
        getFutureAnswer.map(answer => assert(answer == 1))
      }
    }
  }
}
