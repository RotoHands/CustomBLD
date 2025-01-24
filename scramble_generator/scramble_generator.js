const { Worker } = require('worker_threads');
const fs = require('fs');
const os = require('os');

const workerCount = os.cpus().length; // Use the number of CPU cores

function generateScramblesInWorker(count, type) {
  return new Promise((resolve, reject) => {
    const worker = new Worker('./scramble_generator/scramble_worker.js', { workerData: { count, type } });
    worker.on('message', resolve); // Receive result from the worker
    worker.on('error', reject);   // Handle errors
    worker.on('exit', code => {
      if (code !== 0) reject(new Error(`Worker stopped with exit code ${code}`));
    });
  });
}

async function generateScrambles(numScrambles, scrambleType) {

  const tasks = [];
  const scramblesPerWorker = Math.ceil(numScrambles / workerCount); // Divide tasks evenly across workers

  // Spawn workers
  for (let i = 0; i < workerCount; i++) {
    tasks.push(generateScramblesInWorker(scramblesPerWorker, scrambleType));
  }

  // Wait for all workers to finish
  const results = await Promise.all(tasks);


  // Combine results from all workers
  const allScrambles = results.flat();

  // Write allScrambles to a text file
  const fileName = `txt_files\\${scrambleType}_${generateRandomFileName("txt")}`;
  fs.writeFile(fileName, allScrambles.join('\n'), (err) => {
    if (err) throw err;
    console.log(`Scrambles have been saved to ${fileName}`);
  });

}

function generateRandomFileName(extension = "txt") {
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  let randomString = "";
  for (let i = 0; i < 10; i++) { // Generate a 10-character random string
    randomString += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return `file_${randomString}.${extension}`;
}

function main() {
  const numScrambles = parseInt(process.argv[2], 10);
  const scrambleType = process.argv[3];

  if (isNaN(numScrambles) || !scrambleType) {
    process.exit(1);
  }

  generateScrambles(numScrambles, scrambleType).catch(err => console.error(err));
}

// Example usage
if (require.main === module) {
  main();
}
