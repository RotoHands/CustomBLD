const { parentPort, workerData } = require('worker_threads');
const cstimer = require('cstimer_module');

// Generate scrambles
const scrambles = [];
for (let i = 0; i < workerData.count; i++) {
  const scrStr = cstimer.getScramble(workerData.type);
  scrambles.push(scrStr);
}

// Send the result back to the main thread
parentPort.postMessage(scrambles);
