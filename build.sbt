name := "scalala"

scalaVersion := "2.13.10"

val catsRetryVersion = "3.1.0"
val akkaVersion = "2.6.20"



publish / skip := true

libraryDependencies ++= Seq(
  "org.typelevel"                 %% "cats-core"         % "2.7.0",
  "net.debasishg"                 %% "redisclient"       % "3.42",
  "com.github.takezoe"            %% "scala-retry"       % "0.0.6",
  "com.outr"                      %% "hasher"            % "1.2.2",
  "joda-time"                      % "joda-time"         % "2.10.14",
  "com.softwaremill.sttp.client3" %% "core"              % "3.6.2",
  "com.github.pathikrit"          %% "better-files"      % "3.9.1",
  "com.github.cb372"              %% "cats-retry"        % catsRetryVersion,
  "org.scalactic"                 %% "scalactic"         % "3.2.14",
  "com.typesafe.akka"             %% "akka-stream"       % akkaVersion,
  "com.typesafe.akka"             %% "akka-stream-kafka" % "3.0.1",
  "com.typesafe"                   % "config"            % "1.4.2",
  "org.slf4j"                      % "slf4j-api"         % "2.0.3",
  "org.json4s"                    %% "json4s-jackson"    % "4.1.0-M2",
  "ch.qos.logback"                 % "logback-classic"   % "1.4.4",
  "com.typesafe.scala-logging"    %% "scala-logging"     % "3.9.4",
  "org.scalatest"                 %% "scalatest"         % "3.2.9"            % Test,
  "com.github.tomakehurst"         % "wiremock-jre8"     % "2.33.2"           % Test,
  "org.scalamock"                 %% "scalamock"         % "5.2.0"            % Test
)


