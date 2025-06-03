# CustomBLD

CustomBLD is a tool for using custom BLD scrambles.
It is a database of ~20 million scrambles and solutions for 3x3, 4x4, and 5x5 BLD events.
It allows you to choose and customize specific scrambles types for what you want to practice.

Feel free to contact me if you have ideas or need help ([contact@custombld.net](contact@custombld.net), [discord](https://discord.com/invite/uTbBjardtn))
Short tutorial video - [here](https://youtu.be/o7PhXQPeOrs)

## 🎯 Why is it good for?

- Training hard scrambles (scrambles with 3twists, 4flips, many cycle breaks)
- Training easy scrambles - get used to easy sceambles so you don't panic when you one in a competition
- Training excecution only (with the solution provided)
- Training 3-style subsets with real solves (you can choose which letter pairs to practice)
- etc...

## ✨ Features

- Letter pair solutions for each solve with detailed stats. For example:

```rubik
D' L2 B2 U' F2 U' L2 F2 U' B2 L' D' R B2 U2 F' U L F U2 Rw'

Rotations: x
Edges: TB UW AV JH EM Flips: L
Length: 10
Cycle breaks: 1
Solved edges: 1

Corners: XF QV WG Twist Ccw: A
Length: 6
```

- Customize your letter scheme to match your preferred language and letter positions
- Choose your own buffer pieces
- Customize scrambles for each piece type (Corners, Edges, Wings, T-centers, X-centers) based on:
  - Number of algorithms in the solutions
  - Presence of parity
  - Number of edge flips
  - Number of corner twists
  - Number of solved pieces
  - Number of cycle breaks
- Generate up to 1000 scrambles
- Copy the scrambles easily and paste them in your favorite timer
- CSV export

## 🚀 If you want to generate scrambles yourself

You are welcome to use this code for your projects.
I've designed it to be as convenient and easy to set up as possible.

You can easily:

1. Run a local instance of the website
2. Generate a scrambles database for your specific needs (particular buffer combinations, larger quantities of specific events)

First, clone the repository:

```bash
git clone https://github.com/RotoHands/CustomBLD.git
```

### 🖥️ Running the Website Locally

1. Install [Docker](https://www.docker.com/products/docker-desktop/)
2. Navigate to `CustomBLD/CustomBLD/` and run:

```bash
docker-compose up --build
```

3. Access the website at [http://localhost:8080](http://localhost:8080) in your browser

### 📝 Important Notes

- The database file must be named `all_solves_db.backup`
- When replacing the database file, run `docker-compose down -v` to ensure Docker loads the new database
- After updating the database, clear `stats_cache.json` to force a new stats query (occurs every 24 hours)

## 💾 Databases

Download the database in your preferred format:

1. SQLite database - [link](https://drive.google.com/file/d/1qhtHivpLuTCUB2yj18XttZWo91rlom-k/view?usp=sharing)
2. PostgreSQL database - [link](https://drive.google.com/file/d/1vUnHhm1A_SxuDkCcpskecx7QHRzK3pYX/view?usp=sharing) (this file you can put in CusomBLD\CustomBLD\custombld_db and it will be loaded to the website)

note that the website only work with the postgres version of the db

### 🔄 Restoring the Database Locally

I will recommand to use put the db file in scramble_generation\dbsolves or in CusomBLD\CustomBLD\custombld_db and when you will run the docker-compose up --build and will automativally restore it.

if you want to restore the db locally on your machine you can use this script (this is the script that is running in the dockers)

<details>
<summary>Click to view database restoration script</summary>

For Linux/Mac (bash script):

```bash
#!/bin/bash
set -e

# Configuration
DB_NAME="all_solves_db"
DB_USER="postgres"
PGPASSWORD="postgres"
BACKUP_FILE="all_solves_db.backup"

# Export password for non-interactive use
export PGPASSWORD

# Check if backup file exists
if [ ! -f "$BACKUP_FILE" ]; then
    echo "Error: Backup file '$BACKUP_FILE' not found!"
    exit 1
fi

echo "Starting phased database restore for better performance..."

# Phase 0: Create database if it doesn't exist
echo "Phase 0: Checking for database '$DB_NAME'..."
if ! psql -U "$DB_USER" -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME';" | grep -q 1; then
    echo "Database not found. Creating '$DB_NAME'..."
    createdb -U "$DB_USER" "$DB_NAME"
else
    echo "Database '$DB_NAME' already exists."
fi

# Phase 0b: Drop and recreate public schema
echo "Phase 0b: Dropping and recreating 'public' schema to avoid conflicts..."
psql -U "$DB_USER" -d "$DB_NAME" -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

# Phase 1: Restore schema only
echo "Phase 1: Restoring schema only..."
pg_restore -v --no-owner --no-privileges --section=pre-data -U "$DB_USER" -d "$DB_NAME" "$BACKUP_FILE"

# Phase 2: Restore data only (without indexes)
echo "Phase 2: Restoring data only (without indexes)..."
pg_restore -v --no-owner --no-privileges --section=data --jobs=4 -U "$DB_USER" -d "$DB_NAME" "$BACKUP_FILE"

# Phase 3: Restore indexes and constraints
echo "Phase 3: Creating indexes after data is loaded..."
pg_restore -v --no-owner --no-privileges --section=post-data -U "$DB_USER" -d "$DB_NAME" "$BACKUP_FILE"

# Phase 4: Analyze database
echo "Phase 4: Analyzing database tables for optimized index usage..."
psql -U "$DB_USER" -d "$DB_NAME" -c "ANALYZE scrambles;"

# Phase 5: Verify data integrity
echo "Phase 5: Verifying data integrity..."
ROWS=$(psql -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM scrambles;" | tr -d '[:space:]')

echo "Database contains $ROWS rows"
if [ "$ROWS" -gt 0 ]; then
    echo "Database restore completed successfully"
else
    echo "WARNING: Database restore may have failed - no rows found"
fi
```

For Windows (save as `restore.bat`):

```batch
@echo off
setlocal EnableDelayedExpansion

:: Configuration
set DB_NAME=all_solves_db
set DB_USER=postgres
set PGPASSWORD=postgres
set BACKUP_FILE=all_solves_db.backup

:: Check if backup file exists
if not exist "%BACKUP_FILE%" (
    echo Error: Backup file %BACKUP_FILE% not found!
    exit /b 1
)

echo Starting phased database restore for better performance...

:: Phase 0: Create database if it doesn't exist
echo Phase 0: Checking for database "%DB_NAME%"...
psql -U "%DB_USER%" -tc "SELECT 1 FROM pg_database WHERE datname = '%DB_NAME%';" | findstr 1 >nul
if errorlevel 1 (
    echo Database not found. Creating "%DB_NAME%"...
    psql -U "%DB_USER%" -c "CREATE DATABASE \"%DB_NAME%\";"
    if errorlevel 1 (
        echo Error creating database
        exit /b 1
    )
) else (
    echo Database "%DB_NAME%" already exists.
)

:: Phase 0b: Drop and recreate public schema
echo Phase 0b: Dropping and recreating 'public' schema to avoid conflicts...
psql -U "%DB_USER%" -d "%DB_NAME%" -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
if errorlevel 1 (
    echo Error while dropping or recreating schema
    exit /b 1
)

:: Phase 1: Restore schema only (without data and indexes)
echo Phase 1: Restoring schema only...
pg_restore -v --no-owner --no-privileges --section=pre-data -U "%DB_USER%" -d "%DB_NAME%" "%BACKUP_FILE%"
if errorlevel 1 (
    echo Error during schema restore
    exit /b 1
)

:: Phase 2: Restore data only (fast, without index maintenance)
echo Phase 2: Restoring data only (without indexes)...
pg_restore -v --no-owner --no-privileges --section=data --jobs=4 -U "%DB_USER%" -d "%DB_NAME%" "%BACKUP_FILE%"
if errorlevel 1 (
    echo Error during data restore
    exit /b 1
)

:: Phase 3: Create indexes after data is loaded
echo Phase 3: Creating indexes after data is loaded...
pg_restore -v --no-owner --no-privileges --section=post-data -U "%DB_USER%" -d "%DB_NAME%" "%BACKUP_FILE%"
if errorlevel 1 (
    echo Error during index creation
    exit /b 1
)

:: Phase 4: Analyze database for optimal query planning
echo Phase 4: Analyzing database tables for optimized index usage...
psql -U "%DB_USER%" -d "%DB_NAME%" -c "ANALYZE scrambles;"
if errorlevel 1 (
    echo Error during database analysis
    exit /b 1
)

:: Phase 5: Verify data integrity
echo Phase 5: Verifying data integrity...
for /f "tokens=*" %%i in ('psql -U "%DB_USER%" -d "%DB_NAME%" -t -c "SELECT COUNT(*) FROM scrambles;" 2^>nul') do (
    set ROWS=%%i
)
set ROWS=!ROWS: =!
echo Database contains !ROWS! rows
if "!ROWS!" gtr "0" (
    echo Database restore completed successfully
) else (
    echo WARNING: Database restore may have failed - no rows found
)

```

</details>

### Generating Your Own Scrambles

Inside the `scramble_generation` folder, you'll find a `config_scramble_generation.yaml` file where you can configure:

- Number of scrambles to generate
- Buffer pieces
- Event types
- And more...

#### 📋 Configuration Tips

- Use the specific combination section to generate solutions only for your preferred buffer combinations
- If this section is empty, it will generate all combinations from your chosen buffers
- Specify the number of scrambles you want to generate

#### 💾 Backup Options

Configure your export format in the backup section:

```yaml
backup:
  enabled: true # Whether to create backups at all
  sqlite_backup: true # Whether to create SQLite backup
```

#### ⚡ Running the Generation

After configuring your settings, run:

```bash
docker-compose up --build
```

#### ⏱️ Generation Time

- The process may take several hours (approximately 4 hours for 10 million scrambles)

#### 🔄 Multiple Generations

- The db will be saved at scramble_generation\db_solves
- New scrambles will be added to the existing database
- You can generate multiple times to accumulate more scrambles
- To start with a fresh database it is not enough to delete the db files, you need to run:

```bash
docker-compose down -v
docker-compose up --build
```

### 📊 Database Structure

<details>
<summary>Click to view database columns and their descriptions</summary>

| Column Name              | Description                                                                                                 |
| ------------------------ | ----------------------------------------------------------------------------------------------------------- |
| `id`                     | Unique identifier for each scramble                                                                         |
| `scramble_type`          | Type of scramble (e.g., "333ni" for 3x3 BLD)                                                                |
| `scramble`               | The actual scramble string                                                                                  |
| `rotations_to_apply`     | Required cube rotations before solving                                                                      |
| `random_key`             | Unique key for sorting efficiently                                                                          |
| `edge_buffer`            | Edge_buffer                                                                                                 |
| `edges`                  | Edges letter pairs solution                                                                                 |
| `edge_length`            | Number of algs in edge solution                                                                             |
| `edges_cycle_breaks`     | Number of cycle breaks in edge solution                                                                     |
| `edges_flipped`          | Number of flipped edges                                                                                     |
| `edges_solved`           | Number of already solved edges                                                                              |
| `flips`                  | List of flipped edge pieces                                                                                 |
| `first_edges`            | String of the first letter of each letter pair, in order to be able to train only specific letter pair sets |
| `corner_buffer`          | Corner_buffer                                                                                               |
| `corners`                | Corners letter pairs solution                                                                               |
| `corner_length`          | Number of algs in corner solution                                                                           |
| `corners_cycle_breaks`   | Number of cycle breaks in corner solution                                                                   |
| `twist_clockwise`        | Number of clockwise corner twists                                                                           |
| `twist_counterclockwise` | Number of counterclockwise corner twists                                                                    |
| `corners_twisted`        | Total number of twisted corners                                                                             |
| `corners_solved`         | Number of already solved corners                                                                            |
| `corner_parity`          | Whether corner parity is present                                                                            |
| `first_corners`          | String of the first letter of each letter pair, in order to be able to train only specific letter pair sets |
| `wing_buffer`            | Wing_buffer                                                                                                 |
| `wings`                  | Wings letter pairs solution                                                                                 |
| `wings_length`           | Number of algs in wing solution                                                                             |
| `wings_cycle_breaks`     | Number of cycle breaks in wing solution                                                                     |
| `wings_solved`           | Number of already solved wings                                                                              |
| `wing_parity`            | Whether wing parity is present                                                                              |
| `first_wings`            | String of the first letter of each letter pair, in order to be able to train only specific letter pair sets |
| `xcenter_buffer`         | X-center_buffer                                                                                             |
| `xcenters`               | X-centers letter pairs solution                                                                             |
| `xcenter_length`         | Number of algs in X-center solution                                                                         |
| `xcenters_cycle_breaks`  | Number of cycle breaks in X-center solution                                                                 |
| `xcenters_solved`        | Number of already solved X-centers                                                                          |
| `xcenter_parity`         | Whether X-center parity is present                                                                          |
| `first_xcenters`         | String of the first letter of each letter pair, in order to be able to train only specific letter pair sets |
| `tcenter_buffer`         | T-center_buffer                                                                                             |
| `tcenters`               | T-centers letter pairs solution                                                                             |
| `tcenter_length`         | Number of algs in T-center solution                                                                         |
| `tcenters_cycle_breaks`  | Number of cycle breaks in T-center solution                                                                 |
| `tcenters_solved`        | Number of already solved T-centers                                                                          |
| `tcenter_parity`         | Whether T-center parity is present                                                                          |
| `first_tcenters`         | String of the first letter of each letter pair, in order to be able to train only specific letter pair sets |

</details>

### 📝 Example Queries

<details>
<summary>Click to view example SQL queries</summary>

#### Regular 15 scrambles from 3x3 BLD

```sql
SELECT * FROM scrambles
WHERE 1=1
AND scramble_type = '333ni'
AND edge_buffer = 'C'
AND corner_buffer = 'C'
AND random_key >= 0.6734778799876431
ORDER BY random_key ASC
LIMIT 15
```

#### Custom edge length and cycle breaks

```sql
SELECT * FROM scrambles
WHERE 1=1
AND scramble_type = '333ni'
AND edge_buffer = 'U'
AND edge_length BETWEEN 4 AND 6
AND edges_cycle_breaks BETWEEN 3 AND 6
AND corner_buffer = 'A'
AND corners_solved BETWEEN 3 AND 3
AND random_key >= 0.3346416817239617
ORDER BY random_key ASC
LIMIT 15
```

#### Practice A-J algs in Edges

```sql
SELECT * FROM scrambles
WHERE 1=1
AND scramble_type = '333ni'
AND (first_edges LIKE '%A%' OR first_edges LIKE '%C%' OR first_edges LIKE '%B%'
     OR first_edges LIKE '%D%' OR first_edges LIKE '%E%' OR first_edges LIKE '%F%'
     OR first_edges LIKE '%G%' OR first_edges LIKE '%H%' OR first_edges LIKE '%I%'
     OR first_edges LIKE '%J%')
AND NOT EXISTS (
    SELECT 1 FROM (
        SELECT substr(first_edges, i, 1) as char
        FROM (SELECT first_edges),
             (SELECT 1 as i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
              UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8
              UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12)
        WHERE i <= length(first_edges)
    )
    WHERE char NOT IN ('A', 'C', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
)
AND edge_buffer = 'C'
AND corner_buffer = 'C'
AND random_key >= 0.7550832230858098
ORDER BY random_key ASC
LIMIT 15
```

</details>

## 🙏 Acknowledgments

- [csTimer](https://github.com/cs0x7f/cstimer) - I used the library to generate the scrambles
- [Scrambo](https://github.com/NickColley/scrambo) - I used it for generating 4BLD scrambles efficiently
- Java code for analyzing solves - I found this code online a decade ago and used it to develop a BLD android app. When I started this projectm I didn't manage to find the code online  again and had been lucky enough to have a local copy on my computer. If you know the original developer, please let me know so I can properly credit them.

- Roman Strakhov - I had this idea for a couple of years now, but I didn't know how to approach it because I had no clear method for generating custom scrambles. Initially, I considered mathematically constructing scrambles based on specific requirements. However, when I met Roman at Worlds 2023, he shared his implementation of a similar idea that he used for training the Got Talent show he participated in. He explained that he took a "brute force" approach, generating millions of scrambles and then choosing the scrambles that answered his requirements. This gave me a completely new perspective and made the problem feel much more solvable.

## 💭 Final Thoughts

I hope this tool (and [trainbld.com](https://trainbld.com/)) will make BLD training more efficient and expand the ways people approach training. I believe that practicing and coaching in cubing is an undeveloped area in our community, and I hope this tool can help advance the BLD event.

for example, a common challenge in competitions is handling easy scrambles, where the pressure to perform well can be overwhelming. This tool allows you to simulate these situations, helping you prepare for real competition scenarios. Instead of randomly waiting for easy scrambles, you can now deliberately practice on them.
