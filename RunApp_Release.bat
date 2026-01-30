@echo off
chcp 65001 > nul
title CineMe Manager
echo Starting CineMe Manager...
echo Mode: Standalone Release

rem This script runs the pre-compiled application distributed in the Release folder.
rem No compilation is needed required on the target machine.

rem 1. Check Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH!
    echo Please install JDK 17 or higher.
    pause
    exit /b
)

rem 2. Run Application
rem Classpath points to local 'build/classes' and 'lib' folder
echo Launching...
java -cp "build/classes;lib/*" com.cinema.gui.LoginFrame

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Application crashed or failed to start.
    pause
) else (
    echo Application Closed.
    pause
)
