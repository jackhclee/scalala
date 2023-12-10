import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.StdIn


class AkkaHttpTest extends AnyWordSpec with Matchers {

  "ABC" should {
    "jj" in {
     implicit val system = ActorSystem(Behaviors.empty, "HTTP_SYS")
     implicit val executionContext = system.executionContext
     var counter = 0
      val route =
        path("hello") {
          get {
            counter = counter + 1
            if (counter <= 3) {
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Say hello to akka-http</h1> $counter"))
            } else {
              complete(HttpResponse(InternalServerError, entity = "Bad numbers, bad result!!!"))
            }
          }
        }

      val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

      println(s"Server now online. Please navigate to http://localhost:8080/hello\nPress RETURN to stop...")
      StdIn.readLine() // let it run until user presses return
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done

    }
  }


}
