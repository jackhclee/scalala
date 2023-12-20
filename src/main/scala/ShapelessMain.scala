import shapeless.{HNil, Generic, LabelledGeneric, Poly1}
import shapeless.record._

case class Secret(str: String)
case class Config(name: String, age: Int, password: Secret, password2: Secret)


object PolyStr extends Poly1 {
  implicit val intCase = at[Int](i => i)
  implicit val stringCase = at[String](d => d)
  implicit val secretCase = at[Secret](s => Secret("******"))
}
object ShapelessMain {

  def getMaskedConfig() = {
    val config = Config("Jack", 99, Secret("secret"), Secret("secret2"))
    val g = Generic[Config].to(config)
    Generic[Config].from(g.map(PolyStr))
  }

}
