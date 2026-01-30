@echo off
title Cinema DB - Auto Cloud Sync
echo ================================================
echo   Cinema Database Auto Sync Tool
echo   (Local to Cloud)
echo ================================================
echo.

rem Configuration
SET MYSQLDUMP_PATH="C:\xampp\mysql\bin\mysqldump.exe"
SET LOCAL_USER=root
SET LOCAL_DB=cinema_db
SET DUMP_FILE=migration_dump.sql

rem 1. Check if mysqldump exists
if not exist %MYSQLDUMP_PATH% (
    echo [ERROR] mysqldump.exe not found at %MYSQLDUMP_PATH%
    echo Please check your XAMPP installation path.
    pause
    exit /b
)

rem 2. Export Local DB
echo [Step 1/3] Exporting LOCAL database...
%MYSQLDUMP_PATH% -u %LOCAL_USER% %LOCAL_DB% > %DUMP_FILE%

if %errorlevel% neq 0 (
    echo [ERROR] Export failed!
    pause
    exit /b
)
echo [OK] Exported to %DUMP_FILE%
echo.

rem 3. Compile Tools (Just in case)
echo [Step 2/3] Preparing tools...
if not exist "build" mkdir "build"
javac -d build -cp "lib/*;src" -encoding UTF-8 src/com/cinema/util/CloudMigrator.java src/com/cinema/util/TableCaseFixer.java

rem 4. Run CloudMigrator (Imports dump to Cloud)
echo [Step 3/3] Uploading to CLOUD (This may take a minute)...
java -cp "build;lib/*" com.cinema.util.CloudMigrator

rem 5. Run TableCaseFixer (Ensures correct casing)
echo [Bonus] Fixing Table Case Sensitivity...
java -cp "build;lib/*" com.cinema.util.TableCaseFixer

echo.
echo ================================================
echo   Sync Complete!
echo ================================================
echo.
pause
