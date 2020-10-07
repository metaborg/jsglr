import coursier.MavenRepository

interp.repositories() ++= Seq(MavenRepository(
  "file://jsglr2evaluation/data/m2-repo"
))