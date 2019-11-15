args = commandArgs(trailingOnly=TRUE)

if (length(args) != 2) {
  dir       <- "~/jsglr2evaluation"
  reportDir <- "~/jsglr2evaluation/reports"
} else {
  dir       <- args[1]
  reportDir <- args[2]
}

setwd(dir)

data                       <- read.csv(file="results/benchmarks.csv", header=TRUE, sep=",")
variants                   <- unique(data$variant)
scorePerLanguageAndVariant <- as.table(xtabs(score~variant+language, data))

scorePerLanguageAndVariant <- scorePerLanguageAndVariant[variants,] # order by original variant order

dir.create(reportDir, showWarnings = FALSE)

pdf(file=paste(reportDir, "/benchmarks.pdf",sep=""))

barplot(scorePerLanguageAndVariant,
        main="Benchmarks results per language and variant (ms)",
        xlab="Language",
        col=rainbow(length(variants)),
        legend=rownames(scorePerLanguageAndVariant),
        beside=TRUE)

dev.off()