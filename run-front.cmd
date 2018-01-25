@echo off
setlocal enabledelayedexpansion
start "Frontend Server" cmd /C "cd front & ng %1 %2 %3"
echo done