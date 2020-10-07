FROM ubuntu:20.04

WORKDIR /jsglr2evaluation
RUN mkdir /jsglr2evaluation/bin
RUN mkdir /jsglr2evaluation/data

RUN apt-get update

# Run apt-get without interactive dialogue
ENV DEBIAN_FRONTEND=noninteractive

# Install dependencies
RUN apt-get install -y openjdk-8-jdk wget curl git make python3 python3-pip

# Install Maven 3
RUN cd bin &&\
    wget http://apache.mirror.anlx.net/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz &&\
    tar -zxf apache-maven-3.5.4-bin.tar.gz &&\
    cp -R apache-maven-3.5.4 /usr/local &&\
    ln -s /usr/local/apache-maven-3.5.4/bin/mvn /usr/bin/mvn &&\
    ln -s /usr/local/apache-maven-3.5.4/bin/mvnDebug /usr/bin/mvnDebug

# Local Maven settings (http://metaborg.org/dev/maven/#local-settings-file)
RUN mkdir /root/.m2
COPY docker/settings.xml /root/.m2/settings.xml

# Install Ammonite (Scala scripting)
RUN sh -c '(echo "#!/usr/bin/env sh" && curl -L https://github.com/lihaoyi/Ammonite/releases/download/2.2.0/2.13-2.2.0) > /usr/local/bin/amm && chmod +x /usr/local/bin/amm'

# Upgrade PIP
RUN python3 -m pip install --upgrade pip

# Add scripts
COPY build build
COPY docker/Makefile .

ENV target=all
ENV evaluation_target=all

ENTRYPOINT make EVALUATION_TARGET=$evaluation_target $target