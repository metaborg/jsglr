args = commandArgs(trailingOnly=TRUE)

if (length(args) != 2) {
  dir       <- "~/jsglr2evaluation"
  reportDir <- "~/jsglr2evaluation/reports"
} else {
  dir       <- args[1]
  reportDir <- args[2]
}

setwd(dir)

benchmarksPlot <- function(inputFile, outputFile, unit) {
  data                       <- read.csv(file=inputFile, header=TRUE, sep=",")
  variants                   <- unique(data$variant)
  scorePerLanguageAndVariant <- as.table(xtabs(score~variant+language, data))

  scorePerLanguageAndVariant <- scorePerLanguageAndVariant[variants,] # order by original variant order

  dir.create(reportDir, showWarnings = FALSE)

  pdf(file=paste(reportDir, outputFile, sep=""))

  barplot(scorePerLanguageAndVariant,
          main="Benchmarks results per language and variant",
          xlab="Language",
          ylab=unit,
          col=rainbow(length(variants)),
          legend=rownames(scorePerLanguageAndVariant),
          beside=TRUE)

  dev.off()
}

benchmarksPlot("results/benchmarks.csv",            "/benchmarks.pdf",            "parse time in ms")
benchmarksPlot("results/benchmarks-normalized.csv", "/benchmarks-normalized.pdf", "throughput in chars/s")