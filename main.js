const { Worker } = require('worker_threads');
const fs = require('fs');

function generateScramblesInWorker(count) {
  return new Promise((resolve, reject) => {
    const worker = new Worker('./worker.js', { workerData: count });
    worker.on('message', resolve); // Receive result from the worker
    worker.on('error', reject);   // Handle errors
    worker.on('exit', code => {
      if (code !== 0) reject(new Error(`Worker stopped with exit code ${code}`));
    });
  });
}

async function main() {
  console.time("MultithreadedScrambleGeneration");

  const tasks = [];
  const workerCount = 8; // Number of threads
  const scramblesPerWorker = 10000; // Divide tasks evenly across workers

  // Spawn workers
  for (let i = 0; i < workerCount; i++) {
    tasks.push(generateScramblesInWorker(scramblesPerWorker));
  }

  // Wait for all workers to finish
  const results = await Promise.all(tasks);

  console.timeEnd("MultithreadedScrambleGeneration");

  // Combine results from all workers
  const allScrambles = results.flat();

  // Write allScrambles to a text file
  fs.writeFile('scrambles.txt', allScrambles.join('\n'), (err) => {
    if (err) throw err;
    console.log('Scrambles have been saved to scrambles.txt');
  });

  console.log("finished");
}

main().catch(err => console.error(err));
