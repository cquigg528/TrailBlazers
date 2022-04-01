-- All CREATE TABLE statements:

CREATE TABLE trail (
    trail_id integer not null PRIMARY KEY,
    trail_name varchar2(20) not null,
    trail_difficulty integer,
    trail_distance real,
    trail_elevation_gain real
);

CREATE TABLE lake (
    lake_name varchar2(20) not null PRIMARY KEY,
    swimmable int,
);

CREATE TABLE connects_to (
    trail_id integer not null,
    lake_name varchar2(20) not null,
    PRIMARY KEY (trail_id, lake_name),
    CONSTRAINT FK_lake_name FOREIGN KEY (lake_name) REFERENCES lake(lake_name) ON DELETE CASCADE,
    CONSTRAINT FK_trail_id FOREIGN KEY (trail_id) REFERENCES trail(trail_id) ON DELETE CASCADE
);

-- All insertions used to populate the database on startup:

INSERT INTO trail VALUES (1, "Eagle Trail", 3, 12.5, 1200);
INSERT INTO trail VALUES (2, "Lighthouse Trail", 6, 4.3, 5500);
INSERT INTO trail VALUES (3, "Bear Trail", 2, 9.5, 500);
INSERT INTO trail VALUES (4, "Rattlesnake Trail", 1, 4.3, 500);
INSERT INTO trail VALUES (5, "Rattle Trail", 2, 4.3, 1500);
INSERT INTO trail VALUES (6, "Rat Trail", 2, 4.3, 500);

INSERT INTO lake VALUES ("Great Bear Lake", 1);
INSERT INTO lake VALUES ("Williams Lake", 1);
INSERT INTO lake VALUES ("Ontario", 0);

INSERT INTO connects_to VALUES (1, "Great Bear Lake");
INSERT INTO connects_to VALUES (2, "Williams Lake");
INSERT INTO connects_to VALUES (3, "Ontario");
