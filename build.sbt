import sbtrelease.ReleaseStateTransformations._

ThisBuild / scalaVersion := "2.12.18"

val akkaVersion = "2.8.5"
val akkaHttpVersion = "10.5.3"

lazy val domain = project.in(file("modules/domain"))
  .settings(
    name:= "domain"
  )

lazy val root = project.in(file("."))
  .settings(
    name:= "scalala"
  ).dependsOn(domain)
  .aggregate(domain)

libraryDependencies ++= Seq(
  "org.typelevel"                 %% "cats-core"         % "2.7.0",
  "net.debasishg"                 %% "redisclient"       % "3.42",
  "com.github.takezoe"            %% "scala-retry"       % "0.0.6",
  "com.outr"                      %% "hasher"            % "1.2.2",
  "joda-time"                      % "joda-time"         % "2.10.14",
  "com.softwaremill.sttp.client3" %% "core"              % "3.6.2",
  "com.github.pathikrit"          %% "better-files"      % "3.9.1",
 // "com.github.cb372"              %% "cats-retry"        % catsRetryVersion,
  "org.scalactic"                 %% "scalactic"         % "3.2.14",
  "com.typesafe.akka"             %% "akka-stream"       % akkaVersion,
  "com.typesafe.akka"             %% "akka-stream-kafka" % "4.0.2",
  "com.typesafe"                   % "config"            % "1.4.2",
  "org.slf4j"                      % "slf4j-api"         % "2.0.3",
  "org.json4s"                    %% "json4s-jackson"    % "4.1.0-M2",
  "org.json4s"                    %% "json4s-ext"        % "4.1.0-M2",
  "ch.qos.logback"                 % "logback-classic"   % "1.4.4",
  "com.typesafe.scala-logging"    %% "scala-logging"     % "3.9.4",
  "com.typesafe.akka"             %% "akka-actor-typed"  % akkaVersion,
  "com.typesafe.akka"             %% "akka-stream"       % akkaVersion,
  "com.typesafe.akka"             %% "akka-http"         % akkaHttpVersion,
  "com.chuusai"                   %% "shapeless"         % "2.3.10",
  "org.scalatest"                 %% "scalatest"         % "3.2.9"            % Test,
  "com.github.tomakehurst"         % "wiremock-jre8"     % "2.33.2"           % Test,
  "org.scalamock"                 %% "scalamock"         % "5.2.0"            % Test,
  "org.mockito"                   %% "mockito-scala-scalatest"  % "1.17.30"   % Test
)

releaseIgnoreUntrackedFiles := true

publish / skip := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runClean,                               // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
//  publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)

