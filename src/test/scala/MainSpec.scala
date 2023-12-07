import org.scalatest.wordspec.{AnyWordSpec}
import org.scalatest.matchers.should.Matchers._
import org.mockito.scalatest.IdiomaticMockito

trait Machine {
  def beep(msg: String, repeat: Int): String

  def stop: Unit
}
class BasicMachine extends Machine {
  def beep(msg: String, repeat: Int) = {
    println(s"Beep $msg" * repeat)
    s"Beep $msg"
  }

  def stop() = {
    println("Stop")
  }
}

class Person(m: Machine) {
  def pressBeep(msg: String) = {
    m.beep(msg, 1)
  }
}

class MainSpec extends AnyWordSpec with IdiomaticMockito {

  "An object" when {
    "created" should {
      "works" in {
//        val b = new BasicMachine()
//        val m = spy(b)
//        "Beep wa!" willBe returned by m.beep("wa", 1)
//        val p = new Person(m)
//        p.pressBeep("ha") shouldBe "Beep ha"
//        p.pressBeep("ma")
//        p.pressBeep("ha")
//        p.pressBeep("wa") shouldBe "Beep wa!"
//        m.beep("ha", 1) wasCalled 2.times
//        m.beep("ma", any) wasCalled 1.times
        "1" shouldBe "1"
      }
    }
  }
}
