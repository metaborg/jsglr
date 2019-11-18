args = commandArgs(trailingOnly=TRUE)

if (length(args) != 2) {
  dir       <- "~/jsglr2evaluation"
  reportDir <- "~/jsglr2evaluation/reports"
} else {
  dir       <- args[1]
  reportDir <- args[2]
}

setwd(dir)

batchBenchmarksPlot <- function(inputFile, outputFile, unit) {
  data                       <- read.csv(file=inputFile, header=TRUE, sep=",")
  variants                   <- unique(data$variant)
  scorePerLanguageAndVariant <- as.table(xtabs(score~variant+language, data))

  scorePerLanguageAndVariant <- scorePerLanguageAndVariant[variants,] # order by original variant order

  dir.create(reportDir, showWarnings = FALSE)

  pdf(file=paste(reportDir, outputFile, sep=""))

  barplot(scorePerLanguageAndVariant,
          main="Parsing speed",
          xlab="Language",
          ylab=unit,
          col=rainbow(length(variants)),
          legend=rownames(scorePerLanguageAndVariant),
          beside=TRUE)

  dev.off()
}

perFileBenchmarksPlot <- function(inputFile, outputFile, unit) {
  data <- read.csv(file=inputFile, header=TRUE, sep=",")
  data <- data[data$variant == "standard",]
  
  dir.create(reportDir, showWarnings = FALSE)
  
  pdf(file=paste(reportDir, outputFile, sep=""))
  
  plot(data$score,
       data$size,
       main="Parsing speed vs. file size",
       xlab="File size (# characters)",
       ylab=unit)
  
  dev.off()
}

batchBenchmarksPlot("results/benchmarks-batch.csv",            "/benchmarks-batch.pdf",            "parse time in ms")
batchBenchmarksPlot("results/benchmarks-batch-normalized.csv", "/benchmarks-batch-normalized.pdf", "throughput in k chars/s")

perFileBenchmarksPlot("results/benchmarks-perFile.csv",            "/benchmarks-perFile.pdf",            "parse time in ms")
perFileBenchmarksPlot("results/benchmarks-perFile-normalized.csv", "/benchmarks-perFile-normalized.pdf", "throughput in k chars/s")
