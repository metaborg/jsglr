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

    ul.append("<li><a href=\"./" + id + "/index.html\">" + id + "</a></li>")

    write.over(indexFile, index.toString)

    mkdir! dir

    cp(args.dir / "archive.tar.gz", dir / "archive.tar.gz")

    val config = removeCommentedLines(read! args.configPath)

    write(
        dir / "index.html",
        s"""<html>
           |<body>
           |<h1>$id</h1>
           |<p><a href="./archive.tar.gz">Archive</a></p>
           |<pre>$config</pre>
           |</body>
           |</html>""".stripMargin
    )
}

def removeCommentedLines(text: String) = text.replaceAll("[ \t\r]*\n[ \t]*#[^\n]+", "")

@main
def ini(args: String*) = withArgs(args :_ *)(addToWebsite(_))