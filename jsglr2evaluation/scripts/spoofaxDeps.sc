import coursierapi.MavenRepository

interp.repositories.update(List(
    MavenRepository.of("file:/jsglr2evaluation/data/m2-repo"),
    MavenRepository.of("https://repo1.maven.org/maven2")
))