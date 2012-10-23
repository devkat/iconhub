import sbt._, Keys._

object BuildSettings {
  val buildScalaVersion = "2.9.1"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    scalacOptions += "-deprecation",
    crossScalaVersions := Seq("2.8.1", "2.9.0", "2.9.0-1", "2.9.1", "2.9.2"),
    resolvers ++= Seq(
      ScalaToolsReleases,
      "Shiro Releases" at "https://repository.apache.org/content/repositories/releases/",
      "Shiro Snapshots" at "https://repository.apache.org/content/repositories/snapshots/",
      "sonatype.repo" at "https://oss.sonatype.org/content/repositories/public/"
    ),
/*
    publishTo <<= version { (v: String) => 
      val nexus = "https://oss.sonatype.org/" 
        if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots") 
        else Some("releases" at nexus + "service/local/staging/deploy/maven2") 
    },
*/
    publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository"))),

    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { repo => false },
    pomExtra := (
      <url>https://github.com/devkat/iconhub</url>
      <licenses>
        <license>
          <name>Apache 2.0 License</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:devkat/iconhub.git</url>
        <connection>scm:git:git@github.com:devkat/iconhub.git</connection>
      </scm>
      <developers>
        <developer>
          <id>devkat</id>
          <name>Andreas Hartmann</name>
          <url>http://www.devkat.net</url>
        </developer>
      </developers>),

    libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

  )
}

object IconhubBuild extends Build {

  val liftVersion = "2.4"

  lazy val root = Project("iconhub-root", file("."),
    settings = BuildSettings.buildSettings ++ Seq(
      // the root is just an aggregator so dont publish a JAR
      publishArtifact in (Compile, packageBin) := false,
      publishArtifact in (Test, packageBin) := false,
      publishArtifact in (Compile, packageDoc) := false,
      publishArtifact in (Compile, packageSrc) := false
    )
  ) aggregate(persistence, web)

  lazy val persistence: Project = Project("iconhub-persistence", file("persistence"),
    settings = BuildSettings.buildSettings ++ (
      libraryDependencies ++= Seq(
        "org.squeryl" %% "squeryl" % "0.9.5-2",
        "org.apache.derby" % "derby" % "10.9.1.0"
      )
    )
    ++ net.virtualvoid.sbt.graph.Plugin.graphSettings
  )
  
  lazy val web: Project = Project("iconhub-web", file("web"), 
    settings = BuildSettings.buildSettings ++ (
      libraryDependencies ++= Seq(
        "net.liftweb" %% "lift-webkit" % liftVersion,
//        "net.liftweb" %% "lift-openid" % "2.4",
        "net.liftweb" %% "lift-squeryl-record" % liftVersion,
        "eu.getintheloop" %% "lift-shiro" % "0.0.5",
        "commons-collections" % "commons-collections" % "3.2.1",
        //"commons-beanutils" % "commons-beanutils" % "20030211.134440",
        "org.mortbay.jetty" % "jetty" % "6.1.25" % "test,container",
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "commons-logging" % "commons-logging" % "1.1.1",
        "ch.qos.logback" % "logback-classic" % "1.0.6",
        "junit" % "junit" % "4.10" % "test",
        "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"
      )
    )
    ++ com.github.siasia.WebPlugin.webSettings
    ++ net.virtualvoid.sbt.graph.Plugin.graphSettings
  ) dependsOn(persistence)
  
}
