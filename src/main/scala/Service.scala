import com.github.takezoe.retry._
import scala.concurrent.duration._

object Service {

  implicit val policy = RetryPolicy(
    maxAttempts = 3,
    retryDuration = 10.second,
    backOff = ExponentialBackOff, // default is FixedBackOff
    jitter = 1.second // default is no jitter
  )

  def get(key: String) : Option[String] = {
    retry {
      println(s"trying to read DB key:$key")
      Database.get(key)
    }
  }

  def set(key: String, value: String) : Boolean = {
    retry {
      println(s"trying to write DB key:$key value: $value")
      Database.set(key, value)
    }
  }

}
