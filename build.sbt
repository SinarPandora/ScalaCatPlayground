ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "CatPlayground",
    scalacOptions ++= Seq(
      "-Wunused:all",
      "-Wvalue-discard"
    ),
    semanticdbEnabled := true,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.15",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "circe" % "3.8.15",
    libraryDependencies += "io.circe" %% "circe-core" % "0.14.5",
    libraryDependencies += "io.circe" %% "circe-generic" % "0.14.5",
    libraryDependencies += "io.circe" %% "circe-parser" % "0.14.5",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.16",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % "test",
    // Cat Effects
    libraryDependencies += "co.fs2" %% "fs2-core" % "3.7.0",
    libraryDependencies += "org.typelevel" %% "cats-effect-cps" % "0.4.0",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "fs2" % "3.8.15",
    libraryDependencies += "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test,
    // ZIO
    libraryDependencies += "dev.zio" %% "zio" % "2.0.15",
    libraryDependencies += "dev.zio" %% "zio-direct" % "1.0.0-RC7",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "zio" % "3.8.15"
  )
