LOAD DATABASE
    FROM postgresql://postgres:postgres@db/all_solves_db
    INTO sqlite:///db_solves/sqlite_custombld.db

WITH batch rows=1000, batch size=50MB, prefetch rows=10000

SET work_mem to '50MB', maintenance_work_mem to '50MB'; 