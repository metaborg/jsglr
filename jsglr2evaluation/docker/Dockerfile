FROM ubuntu:20.04

# Setup directories
WORKDIR /jsglr2evaluation
RUN mkdir /jsglr2evaluation/bin
RUN mkdir /jsglr2evaluation/data

RUN apt-get update

# Run apt-get without interactive dialogue
ENV DEBIAN_FRONTEND=noninteractive

# Install dependencies
RUN apt-get install -y software-properties-common openjdk-8-jdk wget curl git make python3-venv

# Install Maven 3
RUN cd bin &&\
    wget http://apache.mirror.anlx.net/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz &&\
    tar -zxf apache-maven-3.5.4-bin.tar.gz &&\
    cp -R apache-maven-3.5.4 /usr/local &&\
    ln -s /usr/local/apache-maven-3.5.4/bin/mvn /usr/bin/mvn &&\
    ln -s /usr/local/apache-maven-3.5.4/bin/mvnDebug /usr/bin/mvnDebug

# Local Maven settings (http://metaborg.org/dev/maven/#local-settings-file)
RUN mkdir /root/.m2
COPY docker/maven-settings.xml /root/.m2/settings.xml

# Install Ammonite (Scala scripting)
RUN sh -c '(echo "#!/usr/bin/env sh" && curl -L https://github.com/lihaoyi/Ammonite/releases/download/2.2.0/2.13-2.2.0) > /usr/local/bin/amm && chmod +x /usr/local/bin/amm'
ENV COURSIER_CACHE /jsglr2evaluation/data/coursier-cache

# Install R
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys E298A3A825C0D65DFD57CBB651716619E084DAB9
RUN add-apt-repository 'deb https://cloud.r-project.org/bin/linux/ubuntu focal-cran40/'
RUN apt-get update
RUN apt-get -y install r-base

# Git config
RUN git config --global user.name "JSGLR2 Evaluation"
RUN git config --global user.email "j.denkers@tudelft.nl"
RUN echo ".DS_Store" >> ~/.gitignore_global
RUN git config --global core.excludesfile ~/.gitignore_global

# Add scripts
COPY scripts scripts
COPY parsetables parsetables
COPY docker/Makefile .

# Install required Python packages for scripts into a virtual environment
RUN ./scripts/setup-venv.sh

ENV SPOOFAX_VERSION=master
ENV JSGLR_VERSION=develop/jsglr2

ENV TARGET=all
ENV EVALUATION_TARGET=all

ENTRYPOINT make $TARGET
