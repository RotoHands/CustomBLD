const { parentPort, workerData } = require('worker_threads');
const cstimer = require('cstimer_module');

// Generate scrambles
const scrambles = [];
if (["333ni", "corners", "edges"].includes(workerData.type)){
}
if (["333ni", "corners", "edges"].includes(workerData.type)){
  for (let i = 0; i < workerData.count; i++) {
    const scrStr = cstimer.getScramble(workerData.type);
    scrambles.push(scrStr);
  }
}
else {
  if(["555bld", "5edge"].includes(workerData.type)){
    for (let i = 0; i < workerData.count; i++) {
      const scrStr = cstimer.getScramble(workerData.type, 60);
      scrambles.push(scrStr);
    }
  }
  else {
    if(["444bld", "444cto", "444edo"].includes(workerData.type)){

    for (let i = 0; i < workerData.count; i++) {
      const scrStr = cstimer.getScramble(workerData.type, 45);
      scrambles.push(scrStr);
    }
  }
}
}


// Send the result back to the main thread
parentPort.postMessage(scrambles);
