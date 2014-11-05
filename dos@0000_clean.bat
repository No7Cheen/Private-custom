@echo off

echo project clean...

del /q .\bin\*.jar

ant clean

pause