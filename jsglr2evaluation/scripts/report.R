options(warn=1)

args = commandArgs(trailingOnly=TRUE)

if (length(args) != 2) {
  dir        <- "~/jsglr2evaluation"
  reportsDir <- "~/jsglr2evaluation/reports"
} else {
  dir        <- args[1]
  reportsDir <- args[2]
}

setwd(dir)

colors <- c("#8c510a", "#d8b365", "#f6e8c3", "#f5f5f5", "#c7eae5", "#5ab4ac", "#01665e") # Color per parser variant, colorblind safe: http://colorbrewer2.org/#type=diverging&scheme=BrBG&n=6
symbols <- c(0,2,5) # Color per language

savePlot <- function(plot, filename) {
  png(file=paste(filename, ".png", sep=""))
  plot()
  dev.off()

  pdf(file=paste(filename, ".pdf", sep=""))
  plot()
  dev.off()
}

batchBenchmarksPlot <- function(inputFile, outputFile, dimension, unit, getLows, getHighs) {
  data     <- read.csv(file=inputFile, header=TRUE, sep=",")
  variants <- unique(data$variant)
  
  scores <- tapply(data$score,      list(data$variant, data$language), function(x) c(x = x))
  lows   <- tapply(getLows(data),   list(data$variant, data$language), function(x) c(x = x))
  highs  <- tapply(getHighs(data),  list(data$variant, data$language), function(x) c(x = x))
  
  # order by original variant order
  scores <- scores[variants,]
  lows   <-   lows[variants,]
  highs  <-  highs[variants,]
  
  dir.create(reportsDir, showWarnings = FALSE)
  
  savePlot(function() {
    # https://datascienceplus.com/building-barplots-with-error-bars/
    barCenters <- barplot(height=scores,
                          main=paste("Batch parsing", dimension),
                          xlab="Language",
                          ylab=unit,
                          ylim=c(0, 1.01 * max(getHighs(data))),
                          col=colors[1:length(variants)],
                          legend=variants,
                          beside=TRUE)
    
    segments(barCenters, lows, barCenters, highs, lwd = 1)
    arrows(barCenters, lows, barCenters, highs, lwd = 1, angle = 90, code = 3, length = 0.05)
  }, file=paste(reportsDir, outputFile, sep=""))
}

batchTimeBenchmarksPlot <- function(inputFile, outputFile, dimension, unit) {
  batchBenchmarksPlot(inputFile, outputFile, "time", "ms", function(data) data$score - data$error, function(data) data$score + data$error)
}

batchThroughputBenchmarksPlot <- function(inputFile, outputFile, dimension, unit) {
  batchBenchmarksPlot(inputFile, outputFile, "throughput", "1000 chars/s", function(data) data$low, function(data) data$high)
}

perFileBenchmarksPlot <- function(inputFile, outputFile, dimension, unit) {
  data <- read.csv(file=inputFile, header=TRUE, sep=",")
  data <- data[data$variant == "standard",]
  languages <- unique(data$language)
  
  dir.create(reportsDir, showWarnings = FALSE)

  savePlot(function() {
    plot(data$size / 1000,
        data$score,
        main=paste("Batch parsing", dimension, "vs. file size"),
        xlab="File size (1000 characters)",
        ylab=unit,
        pch=symbols[data$language])
    
    legend("top", inset=0.05, legend=languages, pch=symbols[languages])
  }, file=paste(reportsDir, outputFile, sep=""))
}

batchTimeBenchmarksPlot("results/benchmarks-batch-time.csv",             "/benchmarks-batch-time")
batchThroughputBenchmarksPlot("results/benchmarks-batch-throughput.csv", "/benchmarks-batch-throughput")

perFileBenchmarksPlot("results/benchmarks-perFile-time.csv",       "/benchmarks-perFile-time",       "time",       "ms")
perFileBenchmarksPlot("results/benchmarks-perFile-throughput.csv", "/benchmarks-perFile-throughput", "throughput", "1000 chars/s")
