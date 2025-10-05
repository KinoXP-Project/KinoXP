-- Movies (ID autogenereres)
INSERT INTO movie (title, category, age_limit, duration_min, release_year, actors, description, language)
VALUES ('Inception', 'Sci-Fi', 13, 148, 2010, 'Leonardo DiCaprio, Joseph Gordon-Levitt',
        'A thief who enters dreams to steal secrets.', 'English');

INSERT INTO movie (title, category, age_limit, duration_min, release_year, actors, description, language)
VALUES ('The Dark Knight', 'Action', 12, 152, 2008, 'Christian Bale, Heath Ledger',
        'Batman faces the Joker in Gotham City.', 'English');

INSERT INTO movie (title, category, age_limit, duration_min, release_year, actors, description, language)
VALUES ('Toy Story', 'Animation', 7, 81, 1995, 'Tom Hanks, Tim Allen',
        'Toys come to life when their owner isn’t looking.', 'English');

INSERT INTO movie (title, category, age_limit, duration_min, release_year, actors, description, language)
VALUES ('Get Out', 'Horror', 15, 104, 2017, 'Daniel Kaluuya, Allison Williams',
        'A young man uncovers disturbing secrets when meeting his girlfriend’s family.', 'English');

-- Theaters (ID autogenereres)
INSERT INTO theater (name, row_count, seat_count) VALUES ('Sal 1', 10, 100);
INSERT INTO theater (name, row_count, seat_count) VALUES ('Sal 2', 12, 120);

-- Showings – slå FK’er op via SELECT, så vi ikke gætter på id-værdier
INSERT INTO showing (movie_id, theater_id, start_at)
VALUES (
           (SELECT movie_id FROM movie   WHERE title = 'Inception'),
           (SELECT theater_id FROM theater WHERE name  = 'Sal 1'),
           TIMESTAMP '2025-10-05 19:00:00'
       );

INSERT INTO showing (movie_id, theater_id, start_at)
VALUES (
           (SELECT movie_id FROM movie   WHERE title = 'The Dark Knight'),
           (SELECT theater_id FROM theater WHERE name  = 'Sal 2'),
           TIMESTAMP '2025-10-06 20:30:00'
       );

INSERT INTO showing (movie_id, theater_id, start_at)
VALUES (
         (SELECT movie_id FROM movie WHERE title = 'Toy Story'),
         (SELECT theater_id FROM theater WHERE name = 'Sal 1'),
         TIMESTAMP '2025-10-07 18:00:00'
       );

INSERT INTO showing (movie_id, theater_id, start_at)
VALUES (
         (SELECT movie_id FROM movie WHERE title = 'Get Out'),
         (SELECT theater_id FROM theater WHERE name = 'Sal 2'),
         TIMESTAMP '2025-10-07 22:00:00'
        );