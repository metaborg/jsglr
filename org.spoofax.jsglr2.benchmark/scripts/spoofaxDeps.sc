import coursierapi.MavenRepository

interp.repositories.update(
  interp.repositories() ::: List(MavenRepository.of(
    "file://" + java.lang.System.getProperties.get("user.home") + "/.m2/repository/"
  ))
)