//scalaVersion := "2.13.9"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"      % "2.7.0",
  "org.json4s"    %% "json4s-jackson" % "4.1.0-M2",
  "org.json4s"    %% "json4s-ext"     % "4.1.0-M2"
)

releaseIgnoreUntrackedFiles := true

publish / skip := true

