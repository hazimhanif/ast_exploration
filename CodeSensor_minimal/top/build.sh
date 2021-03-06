#!/bin/bash


BUILD_DIR='build'
ANTLR_JAR='antlr-3.4-complete-no-antlrv2.jar'

rm -rf ${BUILD_DIR}
mkdir ${BUILD_DIR}

# Copy source files to build dir

cp ./*.g ${BUILD_DIR}
cp ./*.java ${BUILD_DIR}
cp ./manifest.mf ${BUILD_DIR}
cp $ANTLR_JAR ${BUILD_DIR}

cd ${BUILD_DIR}

# Generate Lexer and Parser from Grammar

java -jar ../${ANTLR_JAR} CPPGrammar.g

# Compile java-files
javac -target 8 -cp ./${ANTLR_JAR} ./*.java -Xlint:unchecked

# unpack ANTLR-jar since we need some of the class files

jar xf ./${ANTLR_JAR}

cp *.java ../../src/mycs/
cd ../../src/mycs/
sed  -i '1i package mycs;' CodeTree.java
sed  -i '1i package mycs;' MyCS.java
sed  -i '1i package mycs;' CPPGrammarLexer.java
sed  -i '1i package mycs;' CPPGrammarParser.java