import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ShapelessMainSpec extends AnyWordSpec with Matchers {

  "Shape" when {
    "created" should {
      "work" in {
         ShapelessMain.getMaskedConfig().password shouldBe Secret("******")
      }
    }
  }

}
