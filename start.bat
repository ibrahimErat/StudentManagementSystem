@echo off
cd src
javac -cp "../sqlite-jdbc-3.42.0.0.jar" *.java
java -cp ".;../sqlite-jdbc-3.42.0.0.jar" Main
pause 