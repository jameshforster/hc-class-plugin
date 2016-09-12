name := "MinecraftClassesProject"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "spigot-repo" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots"
resolvers += "bungeecord-repo" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "org.bukkit" % "bukkit" % "1.10.2-R0.1-SNAPSHOT"
libraryDependencies += "net.md-5" % "bungeecord-api" % "1.10-SNAPSHOT"
libraryDependencies += "org.spigotmc" % "spigot-api" % "1.10.2-R0.1-SNAPSHOT"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"

coverageMinimum := 70
coverageFailOnMinimum := true
coverageExcludedPackages := "controllers.PluginController;controllers.Main"
