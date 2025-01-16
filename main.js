const { Worker } = require('worker_threads');
const fs = require('fs');

function generateScramblesInWorker(count, type) {
  return new Promise((resolve, reject) => {
    const worker = new Worker('./worker.js', { workerData: { count, type } });
    worker.on('message', resolve); // Receive result from the worker
    worker.on('error', reject);   // Handle errors
    worker.on('exit', code => {
      if (code !== 0) reject(new Error(`Worker stopped with exit code ${code}`));
    });
  });
}

async function generateScrambles(numScrambles, scrambleType) {
//   console.time("MultithreadedScrambleGeneration");

  const tasks = [];
  const workerCount = 8; // Number of threads
  const scramblesPerWorker = Math.ceil(numScrambles / workerCount); // Divide tasks evenly across workers

  // Spawn workers
  for (let i = 0; i < workerCount; i++) {
    tasks.push(generateScramblesInWorker(scramblesPerWorker, scrambleType));
  }

  // Wait for all workers to finish
  const results = await Promise.all(tasks);

//   console.timeEnd("MultithreadedScrambleGeneration");

  // Combine results from all workers
  const allScrambles = results.flat();

  // Write allScrambles to a text file
  const fileName = `${scrambleType}_scrambles.txt`;
  fs.writeFile(fileName, allScrambles.join('\n'), (err) => {
    if (err) throw err;
    console.log(`Scrambles have been saved to ${fileName}`);
  });

  console.log("finished");
}

// Example usage
generateScrambles(100, '444bld').catch(err => console.error(err));
// generateScrambles(10000, 'edges').catch(err => console.error(err));
// generateScrambles(10000, 'corners').catch(err => console.error(err));
// generateScrambles(10000, '333').catch(err => console.error(err));
// 444edo - only scramlbe wings
// 444cto - scramble only centers / can be used for 555 center with couple of mis-slices
// 5edge - only scramble midges + wings
