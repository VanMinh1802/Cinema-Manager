@echo off
chcp 65001 > nul
title CineMe Manager
echo Starting CineMe Manager...

rem Clean previous build to ensure we run latest code
if exist "build" rmdir /s /q "build"
mkdir "build\classes"

echo Compiling source code (Clean Build)...
dir /s /B src\*.java > sources.txt
javac -d build/classes -encoding UTF-8 -cp "lib/*" @sources.txt
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation Failed! 
    echo Please check the error messages above.
    pause
    exit /b
)

echo Starting Application...
java -cp "build/classes;lib/*" com.cinema.gui.LoginFrame

echo.
echo Application Closed.
pause
