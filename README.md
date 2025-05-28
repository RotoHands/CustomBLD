## CustomBLD
CustomBLD is a tool for generating custom BLD scrambles.
It is a database of ~20 million scrambles and solutions for 3x3, 4x4 and 5x5 BLD events, that you can choose and customize the specific scrambles types you want to practice.


### Features
- Letter pairs Solution for each solve with stats, for example:
D' L2 B2 U' F2 U' L2 F2 U' B2 L' D' R B2 U2 F' U L F U2 Rw' 

Rotations: x
Edges: TB UW AV JH EM Flips: L
Length: 10
Cycle breaks: 1
Solved edges: 1
Corners: XF QV WGTwist Ccw: A

Length: 6

- Customize the letter scheme to your own language and letter positions
- Choose your own buffers
- Customize the scrambles in each piece (Corners, Edges, Wings, Tcenters, Xcenters) accrding to:
    - number of algs in the solutions
    - with/without parity
    - number of edge flips
    - number of corner twists
    - numnber of solved pieces
    - number of cycle breaks
- Generate up to 1000 scrambles


### why is it good for?
- if you want to learn 3 style 

### if you want to use it yourself
you are more than welcome to use the code to your projects. 
I tried to make it the most convinient in order to get it up and running the easist way possible.


you can very easily do:
1. Run a local instance of the website
2. Generate a scrambles db for you needs (specific buffers combinations, larger quantity of a specific event)

for both of this  you have first run 
git clone https://github.com/RotoHands/CustomBLD.git

Running the website locally
1. install docker (put a link to docker wecsite)
2. inside this path CustomBLD/CustomBLD/ run :
docker-compose up --build
3. the website will be available in http://localhost:8080/ in your browser

some key notes:
* the db of the solves is all_solves_db.backup, it has to be with that name
* if you generate a new db and you replace the file, the docker doest overrideit, you need to run "docker-compose down -v" 
and than oit will restore the new db. if this still doesnt work, than manually  delete the volume from the docker
* if you put a new db, delete the contents of stats_cache.json so it will know it needs to make a new query 



# DBs
you can download the db from here :
1 sqlite db
2. postgres db 
insert the restore script of the postgres db

insert a breif explanation about the db cols



## Thanks
CS timer
scrambo
the code for analyzing the solcves


## background story

