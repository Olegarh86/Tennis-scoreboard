INSERT INTO Players (Name) VALUES ('Roger Federer');    -- ID: 1
INSERT INTO Players (Name) VALUES ('Rafael Nadal');     -- ID: 2
INSERT INTO Players (Name) VALUES ('Novak Djokovic');   -- ID: 3
INSERT INTO Players (Name) VALUES ('Andy Murray');      -- ID: 4
INSERT INTO Players (Name) VALUES ('Serena Williams');  -- ID: 5
INSERT INTO Players (Name) VALUES ('Venus Williams');   -- ID: 6
INSERT INTO Players (Name) VALUES ('Maria Sharapova');  -- ID: 7
INSERT INTO Players (Name) VALUES ('Andre Agassi');     -- ID: 8
INSERT INTO Players (Name) VALUES ('Pete Sampras');     -- ID: 9
INSERT INTO Players (Name) VALUES ('Carlos Alcaraz');   -- ID: 10
INSERT INTO Players (Name) VALUES ('Jannik Sinner');    -- ID: 11
INSERT INTO Players (Name) VALUES ('Daniil Medvedev');  -- ID: 12
INSERT INTO Players (Name) VALUES ('Alexander Zverev'); -- ID: 13
INSERT INTO Players (Name) VALUES ('Stefanos Tsitsipas');-- ID: 14
INSERT INTO Players (Name) VALUES ('Iga Swiatek');      -- ID: 15
INSERT INTO Players (Name) VALUES ('Aryna Sabalenka');  -- ID: 16
INSERT INTO Players (Name) VALUES ('Coco Gauff');       -- ID: 17
INSERT INTO Players (Name) VALUES ('Naomi Osaka');      -- ID: 18
INSERT INTO Players (Name) VALUES ('John McEnroe');     -- ID: 19
INSERT INTO Players (Name) VALUES ('Bjorn Borg');       -- ID: 20

INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (1, 2, 2);   -- Federer vs Nadal, Winner: Nadal
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (3, 4, 3);   -- Djokovic vs Murray, Winner: Djokovic
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (5, 6, 5);   -- S. Williams vs V. Williams, Winner: S. Williams
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (10, 11, 10); -- Alcaraz vs Sinner, Winner: Alcaraz
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (12, 13, 13); -- Medvedev vs Zverev, Winner: Zverev
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (1, 3, 1);   -- Federer vs Djokovic, Winner: Federer
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (2, 4, 2);   -- Nadal vs Murray, Winner: Nadal
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (8, 9, 8);   -- Agassi vs Sampras, Winner: Agassi
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (15, 16, 15); -- Swiatek vs Sabalenka, Winner: Swiatek
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (17, 18, 17); -- Gauff vs Osaka, Winner: Gauff
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (19, 20, 19); -- McEnroe vs Borg, Winner: McEnroe
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (1, 10, 1);  -- Federer vs Alcaraz, Winner: Federer
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (2, 11, 2);  -- Nadal vs Sinner, Winner: Nadal
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (3, 12, 3);  -- Djokovic vs Medvedev, Winner: Djokovic
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (5, 7, 5);   -- S. Williams vs Sharapova, Winner: S. Williams
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (14, 4, 14);  -- Tsitsipas vs Murray, Winner: Tsitsipas
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (1, 4, 1);   -- Federer vs Murray, Winner: Federer
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (2, 3, 3);   -- Nadal vs Djokovic, Winner: Djokovic
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (10, 12, 12); -- Alcaraz vs Medvedev, Winner: Medvedev
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (11, 13, 11); -- Sinner vs Zverev, Winner: Sinner
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (6, 7, 7);   -- V. Williams vs Sharapova, Winner: Sharapova
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (15, 17, 15); -- Swiatek vs Gauff, Winner: Swiatek
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (16, 18, 18); -- Sabalenka vs Osaka, Winner: Osaka
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (8, 20, 20);  -- Agassi vs Borg, Winner: Borg
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (9, 19, 19);  -- Sampras vs McEnroe, Winner: McEnroe
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (1, 9, 1);   -- Federer vs Sampras, Winner: Federer
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (2, 8, 2);   -- Nadal vs Agassi, Winner: Nadal
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (3, 10, 3);  -- Djokovic vs Alcaraz, Winner: Djokovic
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (13, 14, 13); -- Zverev vs Tsitsipas, Winner: Zverev
INSERT INTO tennis_matches (Player1_id, Player2_id, Winner_id) VALUES (5, 15, 15);  -- S. Williams vs Swiatek, Winner: Swiatek

