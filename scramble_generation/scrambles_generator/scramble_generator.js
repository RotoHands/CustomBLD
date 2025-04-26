const { Worker } = require('worker_threads');
const fs = require('fs');
const os = require('os');

const workerCount = os.cpus().length; // Use the number of CPU cores

function generateScramblesInWorker(count, type, buffers = {}) {
  return new Promise((resolve, reject) => {
    const worker = new Worker('scrambles_generator/scramble_worker.js', { 
      workerData: { 
        count, 
        type,
        buffers 
      } 
    });
    worker.on('message', resolve); // Receive result from the worker
    worker.on('error', reject);   // Handle errors
    worker.on('exit', code => {
      if (code !== 0) reject(new Error(`Worker stopped with exit code ${code}`));
    });
  });
}

async function generateScrambles(numScrambles, scrambleType, buffers = {}) {
  const tasks = [];
  const scramblesPerWorker = Math.ceil(numScrambles / workerCount); // Divide tasks evenly across workers

  // Spawn workers
  for (let i = 0; i < workerCount; i++) {
    tasks.push(generateScramblesInWorker(scramblesPerWorker, scrambleType, buffers));
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
  
  // Parse buffer arguments from command line
  const buffers = {};
  for (let i = 4; i < process.argv.length; i += 2) {
    if (process.argv[i].startsWith('--') && i + 1 < process.argv.length) {
      const bufferType = process.argv[i].substring(2); // Remove '--' prefix
      buffers[bufferType] = process.argv[i + 1];
    }
  }
  
  if (isNaN(numScrambles) || !scrambleType) {
    console.error("Missing required arguments: numScrambles, scrambleType");
    process.exit(1);
  }

  generateScrambles(numScrambles, scrambleType, buffers).catch(err => console.error(err));
}

// Example usage
if (require.main === module) {
  main();
}
