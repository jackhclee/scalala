name := "scalala"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "org.scalatest"            %% "scalatest"    % "3.2.9" % Test,
  "org.typelevel"            %% "cats-core"    % "2.7.0",
  "net.debasishg"            %% "redisclient"  % "3.42",
  "com.github.takezoe"       %% "scala-retry"  % "0.0.6",
  "joda-time" % "joda-time"   % "2.10.14"
)


