const { parentPort, workerData } = require('worker_threads');
const cstimer = require('cstimer_module');
const Scrambo = require('scrambo');

// Extract buffer information
const buffers = workerData.buffers || {};
const cornerBuffer = buffers.corner_buffer || 'C';
const edgeBuffer = buffers.edge_buffer || 'C';
const wingBuffer = buffers.wing_buffer || 'C';
const xcenterBuffer = buffers.xcenter_buffer || 'C';
const tcenterBuffer = buffers.tcenter_buffer || 'C';

// Log buffer information (for debugging)
console.log(`Generating ${workerData.type} scrambles with buffers:`, buffers);

// Initialize scrambo for 4x4x4
const scrambo444 = new Scrambo().type('444');

// Generate scrambles
const scrambles = [];
if (["333ni", "corners", "edges"].includes(workerData.type)) {
  for (let i = 0; i < workerData.count; i++) {
    const scrStr = cstimer.getScramble(workerData.type);
    scrambles.push(scrStr);
  }
}
else if (["555bld", "5edge"].includes(workerData.type)) {
  for (let i = 0; i < workerData.count; i++) {
    const scrStr = cstimer.getScramble(workerData.type, 60);
    scrambles.push(scrStr);
  }
}
else if (["444bld", "444cto", "444edo"].includes(workerData.type)) {
  for (let i = 0; i < workerData.count; i++) {
    const scrStr = scrambo444.length(45).get();
    scrambles.push(scrStr);
  }
}

// Send the result back to the main thread
parentPort.postMessage(scrambles);
