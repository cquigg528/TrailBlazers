CREATE TABLE trail (
    trail_id integer not null PRIMARY KEY,
    trail_name varchar2(20) not null,
    trail_difficulty integer,
    trail_distance real,
    trail_elevation_gain real
);

INSERT INTO trail VALUES (1, "Windermere", 3, 8, 150);
INSERT INTO trail VALUES (2, "Lighthouse", 7, 1, 300);

CREATE TABLE campsite (
    cid integer not null PRIMARY KEY,
    num_campsites integer,
    nightly_rate real not null,
);

INSERT INTO campsite VALUES (1, 8, 150.99);
INSERT INTO campsite VALUES (2, 20, 19.99);