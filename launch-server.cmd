@echo off
setlocal
pushd "%~dp0"
if not defined JAVA_HOME if exist "C:\Program Files\Java\jdk-21.0.12.1\bin\java.exe" set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12.1"
call gradlew.bat :NeoForge:runServer --console=plain
set "launchExitCode=%ERRORLEVEL%"
popd
if not "%launchExitCode%"=="0" pause
exit /b %launchExitCode%
