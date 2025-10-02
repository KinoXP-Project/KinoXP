-- Indsæt film
INSERT INTO movie (movie_id, title, category, age_limit, duration_min, release_year, actors, description, language)
VALUES (1, 'Inception', 'Sci-Fi', 13, 148, 2010, 'Leonardo DiCaprio, Joseph Gordon-Levitt', 'A thief who enters dreams to steal secrets.', 'English');

INSERT INTO movie (movie_id, title, category, age_limit, duration_min, release_year, actors, description, language)
VALUES (2, 'The Dark Knight', 'Action', 12, 152, 2008, 'Christian Bale, Heath Ledger', 'Batman faces the Joker in Gotham City.', 'English');

-- Indsæt teater
INSERT INTO theater (theater_id, name, row_count, seat_count)
VALUES (1, 'Sal 1', 10, 100);

INSERT INTO theater (theater_id, name, row_count, seat_count)
VALUES (2, 'Sal 2', 12, 120);

-- Indsæt shows (forestilling)
INSERT INTO showing (show_id, movie_id, theater_id, start_at)
VALUES (1, 1, 1, TIMESTAMP '2025-10-05 19:00:00');

INSERT INTO showing (show_id, movie_id, theater_id, start_at)
VALUES (2, 2, 2, TIMESTAMP '2025-10-06 20:30:00');
