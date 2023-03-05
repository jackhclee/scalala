import com.redis.RedisClient

object Database {

  val db = new RedisClient("localhost", 6379)

  def get(key : String) : Option[String] = {
    db.get(key)
  }

  def set(key : String, value: String) : Boolean = {
    db.set(key, value)
  }

}
