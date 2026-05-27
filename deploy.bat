@echo off

:: ── Stop running instance ──────────────────────────────────────────────────
echo Stopping any running instance...
if exist app.pid (
    set /p PID=<app.pid
    taskkill /PID %PID% /F >nul 2>&1
    del app.pid
    echo Stopped PID %PID%.
) else (
    :: Fallback: kill whatever is on port 8080
    for /f "tokens=5" %%p in ('netstat -ano ^| findstr ":8080 " ^| findstr "LISTENING" 2^>nul') do (
        taskkill /PID %%p /F >nul 2>&1
        echo Stopped PID %%p.
    )
)

:: Give the process a moment to release the port
timeout /t 2 /nobreak >nul

:: ── Build ──────────────────────────────────────────────────────────────────
echo Building project (skipping tests)...
call mvnw.cmd package -DskipTests
if %errorlevel% neq 0 (
    echo Build failed. Check the output above for errors.
    exit /b 1
)
echo Build successful.

:: ── Start ──────────────────────────────────────────────────────────────────
echo Starting pak-it-app...
echo Logs will be written to output.log

start "pak-it-app" /min java -Xmx400m -Xms200m -jar target\pak-it-app-0.0.1-SNAPSHOT.jar > output.log 2>&1

:: Wait for the app to bind to port 8080 (up to 60 seconds)
set /a attempts=0
:wait_loop
timeout /t 2 /nobreak >nul
set /a attempts+=1
netstat -ano | findstr ":8080 " | findstr "LISTENING" >nul 2>&1
if %errorlevel% == 0 goto :started
if %attempts% lss 30 goto :wait_loop

echo App is taking longer than expected. Check output.log for details.
goto :done

:started
for /f "tokens=5" %%p in ('netstat -ano ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    echo %%p > app.pid
    echo Started with PID %%p.
    goto :done
)

:done
echo Done. Access the app at: http://localhost:8080
