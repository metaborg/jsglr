import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._
import $ivy.`org.jsoup:jsoup:1.7.2`, org.jsoup._
import $file.common, common._, Args._
import collection.JavaConverters._
import java.io.File

def addToWebsite(implicit args: Args) = {
    val id = java.time.LocalDateTime.now.toString
    
    val dir = websiteDir / id
    val indexFile = websiteDir / "index.html"
    
    val index = Jsoup.parse(new File("" + indexFile), "UTF-8")
    val ul = index.select("#runs").first

    ul.prepend("<li><a href=\"./" + id + "/index.html\">" + id + "</a></li>")

    write.over(indexFile, index.toString)

    mkdir! dir

    cp(args.dir / "archive.tar.gz", dir / "archive.tar.gz")
    cp.into(reportsDir, dir)

    val config = removeCommentedLines(read! args.configPath)

    write(
        dir / "index.html",
        s"""<!doctype html>
           |<html lang="en">
           |<head>
           |  <meta charset="utf-8">
           |  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
           |  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
           |  <title>$id</title>
           |</head>
           |<body>
           |<div class="container">
           |  <div class="row">
           |    <div class="col">
           |      <p><a href="../index.html">&larr; Back to index</a></p>
           |      <h1>$id</h1>
           |      <p><a href="./archive.tar.gz" class="btn btn-primary">Download Archive</a></p>
           |      <pre>$config</pre>
           |      <p><img src="./reports/benchmarks-batch-throughput.png" /></p>
           |      <p><img src="./reports/benchmarks-perFile-throughput.png" /></p>
           |    </div>
           |  </div>
           |</div>
           |</body>
           |</html>""".stripMargin
    )
}

def removeCommentedLines(text: String) = text.replaceAll("[ \t\r]*\n[ \t]*#[^\n]+", "")

@main
def ini(args: String*) = withArgs(args :_ *)(addToWebsite(_))