import sbtrelease.ReleaseStateTransformations._

ThisBuild / scalaVersion := "2.12.18"

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
  "org.typelevel" %% "cats-core"               % "2.7.0",
  "org.json4s"    %% "json4s-jackson"          % "4.1.0-M2",
  "org.json4s"    %% "json4s-ext"              % "4.1.0-M2",
  "org.scalatest" %% "scalatest"               % "3.2.17"    % Test,
  "org.mockito"   %% "mockito-scala-scalatest" % "1.17.30"   % Test
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

