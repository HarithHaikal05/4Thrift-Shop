@echo off
:: 1. Define paths
set SRC_BASE=src/main/java/com/thrift
:: Note: If you haven't moved the DAO folder yet, change the line below to src/main/java/com/dao
set DAO_SRC=src/main/java/com/thrift/dao
set BIN=webapp/WEB-INF/classes
set LIB=webapp/WEB-INF/lib/*
set TOMCAT_LIB=C:/xampp/tomcat/lib/*

:: 2. Create output folder
if not exist "%BIN%" mkdir "%BIN%"

echo Compiling all Java files...

:: 3. Compile everything in one go
javac -d %BIN% -cp "%LIB%;%TOMCAT_LIB%" ^
    %SRC_BASE%/controller/*.java ^
    %SRC_BASE%/model/*.java ^
    %SRC_BASE%/utils/*.java ^
    %DAO_SRC%/*.java

echo Build Complete!
pause