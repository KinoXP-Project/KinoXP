-- ==== NULSTIL (kør i korrekt rækkefølge pga. FK) ===========================
DELETE FROM showing;
DELETE FROM movie;
DELETE FROM theater;

-- Nulstil identity/auto-increment
ALTER TABLE theater ALTER COLUMN theater_id RESTART WITH 1;
ALTER TABLE movie   ALTER COLUMN movie_id   RESTART WITH 1;
ALTER TABLE showing ALTER COLUMN show_id    RESTART WITH 1;

-- ==== KRAV TIL SHOWING (Issue #47) =========================================
-- Tilføj status-kolonne hvis den ikke findes, sæt start_at som NOT NULL,
-- og lav et index til hurtig overlap-søgning.
ALTER TABLE showing ADD COLUMN IF NOT EXISTS status VARCHAR(16) DEFAULT 'ACTIVE';
UPDATE showing SET status = 'ACTIVE' WHERE status IS NULL;

-- ==== THEATERS ==============================================================
INSERT INTO theater (name, row_count, seat_count) VALUES
 ('Sal 1', 10, 100),
 ('Sal 2', 12, 120);

-- ==== MOVIES ================================================================
INSERT INTO movie (title, category, age_limit, duration_min, release_year, actors, description, language) VALUES
('Inception', 'Sci-Fi', 13, 148, 2010, 'Leonardo DiCaprio, Joseph Gordon-Levitt',
 'A thief who enters people’s dreams to steal secrets.', 'English'),

('The Dark Knight', 'Action', 12, 152, 2008, 'Christian Bale, Heath Ledger',
'Batman faces the Joker in Gotham City.', 'English'),

 ('Interstellar', 'Sci-Fi', 10, 169, 2014, 'Matthew McConaughey, Anne Hathaway',
'Explorers travel through a wormhole to find a new home for humanity.', 'English'),

('Parasite', 'Thriller', 15, 132, 2019, 'Song Kang-ho, Lee Sun-kyun',
'A poor family infiltrates a wealthy household with unexpected consequences.', 'Korean'),

('Toy Story', 'Animation', 7, 81, 1995, 'Tom Hanks, Tim Allen',
 'Toys come to life when their owner isn’t looking.', 'English'),

('Inception 2', 'Sci-Fi', 13, 150, 2026, 'Leonardo DiCaprio, Elliot Page',
 'Dream heists return with higher stakes.', 'English');

-- ==== SHOWINGS (brug JOIN i INSERT … SELECT) ================================
-- VIGTIGT: Vælg fremtidige tidspunkter, ellers filtreres de fra i /upcoming.

-- Inception i Sal 1
INSERT INTO showing (movie_id, theater_id, start_at)
SELECT m.movie_id, t.theater_id, TIMESTAMP '2025-10-12 19:00:00'
FROM movie m JOIN theater t ON t.name = 'Sal 1'
WHERE LOWER(m.title) = LOWER('Inception');

-- The Dark Knight i Sal 2
INSERT INTO showing (movie_id, theater_id, start_at)
SELECT m.movie_id, t.theater_id, TIMESTAMP '2025-10-12 21:00:00'
FROM movie m JOIN theater t ON t.name = 'Sal 2'
WHERE LOWER(m.title) = LOWER('The Dark Knight');

-- Interstellar i Sal 1
INSERT INTO showing (movie_id, theater_id, start_at)
SELECT m.movie_id, t.theater_id, TIMESTAMP '2025-10-13 18:30:00'
FROM movie m JOIN theater t ON t.name = 'Sal 1'
WHERE LOWER(m.title) = LOWER('Interstellar');

-- Parasite i Sal 2
INSERT INTO showing (movie_id, theater_id, start_at)
SELECT m.movie_id, t.theater_id, TIMESTAMP '2025-10-13 20:45:00'
FROM movie m JOIN theater t ON t.name = 'Sal 2'
WHERE LOWER(m.title) = LOWER('Parasite');

-- Toy Story i Sal 1
INSERT INTO showing (movie_id, theater_id, start_at)
SELECT m.movie_id, t.theater_id, TIMESTAMP '2025-10-14 16:00:00'
FROM movie m JOIN theater t ON t.name = 'Sal 1'
WHERE LOWER(m.title) = LOWER('Toy Story');

-- Din nye film 'Inception 2' i Sal 1
INSERT INTO showing (movie_id, theater_id, start_at)
SELECT m.movie_id, t.theater_id, TIMESTAMP '2025-10-15 19:30:00'
FROM movie m JOIN theater t ON t.name = 'Sal 1'
WHERE LOWER(m.title) = LOWER('Inception 2');

