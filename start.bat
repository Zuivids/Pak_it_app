@echo off

:: Check if already running on port 8080
netstat -ano | findstr ":8080 " | findstr "LISTENING" >nul 2>&1
if %errorlevel% == 0 (
    echo App is already running on port 8080.
    exit /b 1
)

echo Starting pak-it-app...
echo Logs will be written to output.log

start "pak-it-app" /min java -Xmx400m -Xms200m -jar target\pak-it-app-0.0.1-SNAPSHOT.jar > output.log 2>&1

:: Wait for the app to bind to port 8080 (up to 30 seconds)
set /a attempts=0
:wait_loop
timeout /t 2 /nobreak >nul
set /a attempts+=1
netstat -ano | findstr ":8080 " | findstr "LISTENING" >nul 2>&1
if %errorlevel% == 0 goto :started
if %attempts% lss 15 goto :wait_loop

echo App is taking longer than expected. Check output.log for details.
goto :done

:started
for /f "tokens=5" %%p in ('netstat -ano ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    echo %%p > app.pid
    echo Started with PID %%p
    goto :done
)

:done
echo Access the app at: http://localhost:8080
