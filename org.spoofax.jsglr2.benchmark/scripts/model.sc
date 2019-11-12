case class Config(languages: Seq[Language])
case class Language(id: String, extension: String, repo: String, path: String, sources: Seq[Source])
case class Source(id: String, repo: String)