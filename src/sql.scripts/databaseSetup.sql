CREATE TABLE trail (
    trail_id integer not null PRIMARY KEY,
    trail_name varchar2(20) not null,
    trail_difficulty integer,
    trail_distance real,
    trail_elevation_gain real
);

INSERT INTO trail VALUES (1, "Windermere", 3, 8, 150);
INSERT INTO trail VALUES (2, "Lighthouse", 7, 1, 300);
INSERT INTO trail VALUES (3, "Bear", 4, 5, 500);


CREATE TABLE lake (
    lake_name varchar2(20) not null PRIMARY KEY,
    swimmable int,
);

INSERT INTO lake VALUES ("Great Bear Lake", 1);
INSERT INTO lake VALUES ("Williams Lake", 1);
INSERT INTO lake VALUES ("Ontario", 0);

CREATE TABLE connects_to (
      trail_id integer not null,
      lake_name varchar2(20) not null,
      PRIMARY KEY (trail_id, lake_name),
      FOREIGN KEY (lake_name) references lake,
      FOREIGN KEY (trail_id) references trail
);

INSERT INTO connects_to VALUES (1, "Great Bear Lake");
INSERT INTO connects_to VALUES (2, "Williams Lake");
INSERT INTO connects_to VALUES (3, "Ontario");
INSERT INTO connects_to VALUES (1, "Ontario");