-- Drop user first if they exist
DROP USER if exists 'movieratings'@'%' ;

-- Now create user with prop privileges
CREATE USER 'movieratings'@'%' IDENTIFIED BY 'movieratings';

GRANT ALL PRIVILEGES ON * . * TO 'movieratings'@'%';