@echo off
:: Number of parallel instances
set INSTANCE_COUNT=5
set NODE_SCRIPT=scramble_generator.js
for /L %%i in (1, 1, %INSTANCE_COUNT%) do (
    start "Node Instance %%i" cmd /c "node %NODE_SCRIPT% "
)

echo All instances have been started.
