@echo off
setlocal

cd /d "%~dp0src\main\resources\admin"
echo [1/2] Building admin dist...
call npm run build
if errorlevel 1 (
  echo Frontend build failed.
  exit /b 1
)

cd /d "%~dp0"
echo [2/2] Syncing dist to target\classes\admin\dist...
if not exist "target\classes\admin\dist" mkdir "target\classes\admin\dist"
robocopy "src\main\resources\admin\dist" "target\classes\admin\dist" /MIR /NFL /NDL /NJH /NJS /NP >nul
if errorlevel 8 (
  echo Dist sync failed.
  exit /b 1
)

echo Build and sync completed.
exit /b 0
