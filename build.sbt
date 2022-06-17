name := "scalala"

scalaVersion := "2.13.8"

val catsRetryVersion = "3.1.0"

libraryDependencies ++= Seq(
  "org.scalatest"                 %% "scalatest"     % "3.2.9" % Test,
  "org.typelevel"                 %% "cats-core"     % "2.7.0",
  "net.debasishg"                 %% "redisclient"   % "3.42",
  "com.github.takezoe"            %% "scala-retry"   % "0.0.6",
  "com.outr"                      %% "hasher"        % "1.2.2",
  "joda-time"                      % "joda-time"     % "2.10.14",
  "com.softwaremill.sttp.client3" %% "core"          % "3.6.2",
  "com.github.cb372"              %% "cats-retry"    % catsRetryVersion,
  "com.github.tomakehurst"         % "wiremock-jre8" % "2.33.2" % Test,
)


