@echo off
rem Windows version of run.sh. Usage: run.bat build ^| demo ^| test ^| bench ^| plots
setlocal
cd /d "%~dp0"
if "%1"=="" goto usage

if exist out rmdir /s /q out
if exist out-test rmdir /s /q out-test
dir /s /b src\*.java > sources.txt
javac --release 17 -d out @sources.txt || goto :eof
dir /s /b test\*.java > test-sources.txt
javac --release 17 -cp out -d out-test @test-sources.txt || goto :eof
del sources.txt test-sources.txt

if "%1"=="build" goto :eof
if "%1"=="demo"  java -cp out app.SmartCityDemo
if "%1"=="test"  java -cp out;out-test tests.AllTests
if "%1"=="bench" java -Xms1g -Xmx2g -cp out experiments.BenchmarkRunner results\tables
if "%1"=="plots" python scripts\plot_results.py
goto :eof

:usage
echo usage: run.bat build ^| demo ^| test ^| bench ^| plots
