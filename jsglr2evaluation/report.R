args = commandArgs(trailingOnly=TRUE)

if (length(args) != 2) {
  dir       <- "~/jsglr2evaluation"
  outputDir <- "~/jsglr2evaluation/plots"
} else {
  dir       <- args[1]
  outputDir <- args[2]
}

setwd(dir)

data                       <- read.csv(file="results/benchmarks.csv", header=TRUE, sep=",")
variants                   <- unique(data$variant)
scorePerLanguageAndVariant <- as.table(xtabs(score~variant+language, data))

scorePerLanguageAndVariant <- scorePerLanguageAndVariant[variants,] # order by original variant order

dir.create(outputDir, showWarnings = FALSE)

pdf(file=paste(outputDir, "/benchmarks.pdf",sep=""))

barplot(scorePerLanguageAndVariant,
        main="Benchmarks results per language and variant (ms)",
        xlab="Language",
        col=rainbow(length(variants)),
        legend=rownames(scorePerLanguageAndVariant),
        beside=TRUE)

dev.off()