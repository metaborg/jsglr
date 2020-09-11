import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import coursierapi.MavenRepository


interp.repositories.update(
  interp.repositories() ::: List(MavenRepository.of(
    "file://" + %%("mvn", "help:evaluate", "-Dexpression=settings.localRepository", "-q", "-DforceStdout")(pwd).out.string
  ))
)
