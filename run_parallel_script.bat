@echo off
:: Number of parallel instances
set INSTANCE_COUNT=10

:: Path to the Node.js script
set NODE_SCRIPT=main.js

:: Log file prefix
set LOG_PREFIX=node_instance_

:: Loop to start instances
for /L %%i in (1, 1, %INSTANCE_COUNT%) do (
    start "Node Instance %%i" cmd /c "node %NODE_SCRIPT% > %LOG_PREFIX%%%i.log 2>&1"
    echo Started instance %%i, logging to %LOG_PREFIX%%%i.log
)

echo All instances have been started.
