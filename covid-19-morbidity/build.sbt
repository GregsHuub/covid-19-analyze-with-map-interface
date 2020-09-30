name := "SparkProj"

version := "0.1"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.5",
  "org.apache.spark" %% "spark-sql" % "2.4.5",
  "org.jsoup" % "jsoup" % "1.13.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3"
  //  "com.typesafe" % "config" % "1.4.0" - hocon
)


