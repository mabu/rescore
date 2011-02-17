INSERT INTO Modeliai(Pavadinimas, Koeficientas) SELECT * FROM CSVREAD('modeliai.tsv', NULL, 'UTF-8', chr(9));
INSERT INTO Regatos (Pavadinimas, Regionas) VALUES ('Pilypo regata', 'Vakarų Lietuvos');
--INSERT INTO Etapai (Regata, Metai, Numeris) VALUES (1, 2010, 1), (1, 2010, 2), (1, 2010, 3);
INSERT INTO Etapai (Regata, Metai, Numeris) VALUES (1, 2010, 1);
INSERT INTO Etapai (Regata, Metai, Numeris) VALUES (1, 2010, 2);
INSERT INTO Etapai (Regata, Metai, Numeris) VALUES (1, 2010, 3);
--INSERT INTO Kapitonai (Vardas) VALUES ('Kapitonas Kapitonauskas'), ('Kapitonė Kapitonienė');
INSERT INTO Kapitonai (Vardas) VALUES ('Kapitonas Kapitonauskas');
INSERT INTO Kapitonai (Vardas) VALUES ('Kapitonė Kapitonienė');
--INSERT INTO Savininkai (Vardas) VALUES ('Savininkas Savininkauskas'), ('Savininkė Savininkienė');
INSERT INTO Savininkai (Vardas) VALUES ('Savininkas Savininkauskas');
INSERT INTO Savininkai (Vardas) VALUES ('Savininkė Savininkienė');
--INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, Kapitonas, Savininkas) VALUES ('LTU2018', 314, 'Gafsan', 1, 2), ('LTU1547', 271, 'Piranija', 2, 1);
INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, Kapitonas, Savininkas) VALUES ('LTU2018', 314, 'Gafsan', 1, 2);
INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, Kapitonas, Savininkas) VALUES ('LTU1547', 271, 'Piranija', 2, 1);
INSERT INTO Plaukimai (Jachta, Etapas, StartoLaikas, FinišoLaikas, Kapitonas, Savininkas) VALUES
  ('LTU2018', 1, '2010-12-06 8:30:00', '2010-12-06 9:50:00', 1, 2);
INSERT INTO Plaukimai (Jachta, Etapas, StartoLaikas, FinišoLaikas, Kapitonas, Savininkas) VALUES
  ('LTU1547', 1, '2010-12-06 8:40:00', '2010-12-06 9:20:00', 2, 1);
