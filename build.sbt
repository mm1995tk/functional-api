lazy val Http4sVersion = "0.21.15"
lazy val CirceVersion = "0.13.0"
lazy val MunitVersion = "0.7.20"
lazy val SkunkVersion = "0.0.20"
lazy val ConfigVersion = "1.4.1"
lazy val LogbackVersion = "1.2.3"
lazy val MunitCatsEffectVersion = "0.12.0"
lazy val doobieVersion = "0.9.0"


lazy val root = (project in file("."))
    .settings(
      organization := "com.example",
      name := "functional-api",
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.13.4",
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
        "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
        "org.http4s" %% "http4s-circe" % Http4sVersion,
        "org.http4s" %% "http4s-dsl" % Http4sVersion,
        "io.circe" %% "circe-generic" % CirceVersion,
        "org.scalameta" %% "munit" % MunitVersion % Test,
        "org.typelevel" %% "munit-cats-effect-2" % MunitCatsEffectVersion % Test,
        "org.tpolecat" %% "skunk-core" % SkunkVersion,
        "com.typesafe" % "config" % ConfigVersion,
        "ch.qos.logback" % "logback-classic" % LogbackVersion,
        "org.scalameta" %% "svm-subs" % "20.2.0",
        "org.tpolecat" %% "doobie-core"     % doobieVersion,
        "org.tpolecat" %% "doobie-postgres" % doobieVersion,
        "org.tpolecat" %% "doobie-specs2"   % doobieVersion
      ),
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
      testFrameworks += new TestFramework("munit.Framework")
    )
