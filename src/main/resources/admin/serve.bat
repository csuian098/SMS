@echo off
setlocal

set "NODE_HOME=F:\v12.2.0"
set "PROJECT_DIR=%~dp0"

if not exist "%NODE_HOME%\node.exe" (
  echo [ERROR] Node.exe not found: %NODE_HOME%\node.exe
  pause
  exit /b 1
)

if not exist "%NODE_HOME%\npm.cmd" (
  echo [ERROR] Npm.cmd not found: %NODE_HOME%\npm.cmd
  pause
  exit /b 1
)

cd /d "%PROJECT_DIR%"
set "PATH=%NODE_HOME%;%PATH%"
set "npm_config_scripts_prepend_node_path=true"

echo Using Node:
"%NODE_HOME%\node.exe" -v
echo Using NPM:
call "%NODE_HOME%\npm.cmd" -v
echo.
echo Installing dependencies...
call "%NODE_HOME%\npm.cmd" install
echo.
echo Starting dev server...

call "%NODE_HOME%\npm.cmd" run serve --scripts-prepend-node-path=true
exit /b %errorlevel%

endlocal
