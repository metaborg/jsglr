import $file.args, args._

case class Config(languages: Seq[Language])

case class Language(id: String, extension: String, repo: String, path: String, sources: Seq[Source]) {
    def repoDir(implicit args: Args) = Args.languagesDir / id
    def dir(implicit args: Args) = repoDir / path
    def sourcesDir(implicit args: Args) = Args.sourcesDir / id
    def parseTablePath(implicit args: Args) = dir / "target" / "metaborg" / "sdf.tbl"
    def measurementsDir(implicit args: Args) = Args.measurementsDir / id
    def benchmarksPath(implicit args: Args) = Args.benchmarksDir / (id + ".csv")
}

case class Source(id: String, repo: String)