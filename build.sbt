//scalaVersion := "2.13.9"

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
  "org.typelevel" %% "cats-core"      % "2.7.0",
  "org.json4s"    %% "json4s-jackson" % "4.1.0-M2",
  "org.json4s"    %% "json4s-ext"     % "4.1.0-M2"
)

releaseIgnoreUntrackedFiles := true

publish / skip := true

