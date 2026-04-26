@echo off
setlocal

cd /d "%~dp0"
echo [0/2] Building admin dist...
call build-admin.bat
if errorlevel 1 (
  echo Admin build failed. Startup aborted.
  pause
  exit /b 1
)

set "PORT_PID="
for /f "tokens=5" %%p in ('netstat -ano ^| findstr /R /C:":8080 .*LISTENING"') do (
  set "PORT_PID=%%p"
  goto :port_found
)
:port_found
if defined PORT_PID (
  echo Port 8080 is already in use by PID %PORT_PID%.
  echo If this is an old project process, run:
  echo taskkill /PID %PORT_PID% /F
  echo Then double-click start-local.bat again.
  pause
  exit /b 1
)

echo Starting Spring Boot at: http://localhost:8080/springboot32e20828n/admin/dist/index.html
call mvnw.cmd spring-boot:run
set RC=%errorlevel%
if not "%RC%"=="0" (
  echo Spring Boot exited with code %RC%.
  pause
)
exit /b %RC%
