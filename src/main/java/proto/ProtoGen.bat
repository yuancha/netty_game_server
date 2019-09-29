@echo off
del /s /q message
for %%i in (*.proto) do call:next "%%i"
pause
goto:eof

:next
echo %1
protoc.exe --java_out=../ %1
goto:eof