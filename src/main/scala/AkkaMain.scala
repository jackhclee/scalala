import akka.NotUsed
import akka.actor.ActorSystem
import akka.dispatch.ExecutionContexts
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream._
import akka.stream.javadsl.GraphDSL.builder
import akka.stream.scaladsl._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import java.util.Calendar
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object AkkaMain {
  def main(argv: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("QuickStart")
    implicit val ec = ExecutionContexts.global()
    println(Calendar.getInstance().getTime)
    val rx = Source(List("1", "2", "3", "4", "5", "6"))
      //      .throttle(1, 1.seconds)
      .map(x => s"$x!")
      .to(Sink.seq)

    val topic = "test"
    val conf: Config  = system.settings.config.getConfig("our-kafka-consumer")
    val cs = ConsumerSettings(conf,keyDeserializer = new StringDeserializer, valueDeserializer = new StringDeserializer)

    val prdCfg = system.settings.config.getConfig("akka.kafka.producer")
    val producerSettings =
      ProducerSettings(prdCfg, new StringSerializer, new StringSerializer)

//  val outa = Flow[Int].toMat(Sink.seq)(Keep.right)


    val outa = Flow[Int]
      .wireTap(x => println(s"Wiretapped $x"))
      .map(x => new ProducerRecord[String, String]("test-result",s"RESULT ${x.toString}")).to(Producer.plainSink(producerSettings))
   // val outa = Flow[ConsumerRecord[String, String]].toMat(Sink.seq)(DrainingControl.apply)
    val g = GraphDSL.create(outa) { implicit builder =>
      out =>
        import GraphDSL.Implicits._

//        val in = Source(1 to 5)

        val in = Consumer.atMostOnceSource(cs, Subscriptions.assignmentWithOffset(new TopicPartition("test", 0),0))
          .map(cs => {
//          println(s"read ${cs.value()}")
          Integer.parseInt(cs.value())})


        val f1, f4 = Flow[Int].map(_ + 10)
        val fAdd100 = Flow[Int].map(_ + 100)
        val fAdd1000 = Flow[Int].map( x => {
//          println(x)
          x + 1000
        })
        //      val bcast = builder.add(Broadcast[Int](2))
        //      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
        //      bcast ~> f4 ~> merge
        val evenModResult = 0
        val oddModResult = 1

        val bcast = builder.add(Partition[Int](2, x => x % 2))
        val merge = builder.add(Merge[Int](2))
        in ~> f1 ~> bcast
                    bcast.out(evenModResult) ~> fAdd100  ~> merge ~> f4 ~> out
                    bcast.out(oddModResult)  ~> fAdd1000 ~> merge
        ClosedShape
    }
    val rg = RunnableGraph.fromGraph(g)
    val future = rg.run()
//    for {
//      f <- future
//      e <- f
//    } {
//      println(s"${Calendar.getInstance().getTime} $e")
//    }

  }
}
