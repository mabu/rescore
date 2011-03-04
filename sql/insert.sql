INSERT INTO Modeliai(Pavadinimas, Koeficientas) SELECT * FROM CSVREAD('modeliai.tsv', NULL, 'UTF-8', chr(9));
INSERT INTO Regatos (Pavadinimas, Regionas) VALUES ('Pilypo regata', 'Vakarų Lietuvos');
INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, Kapitonas, Savininkas) VALUES ('LTU2018', 314, 'Gafsan', 'Kapitonas Kapitonauskas', 'Savininkė Savininkienė');
INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, Kapitonas, Savininkas) VALUES ('LTU1547', 271, 'Piranija', 'Kapitonė Kapitonienė', 'Savininkas Savininkauskas');
