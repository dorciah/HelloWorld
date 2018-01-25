@echo off
setlocal enabledelayedexpansion
start "Backend Server" cmd /C "mvn spring-boot:run -pl back"
echo done