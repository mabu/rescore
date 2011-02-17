-- Galbūt kapitonams ir savininkams ir neverta atskirų lentelių daryt – kaip rėmėjams...
-- TODO: išversti į anglų kalbą
-- TODO: reikia trigerio, užtikrinančio, kad etape plaukianti jachta yra atitinkamos grupės dalyvė
-- TODO: reikai trigerio arba išorinio rakto, užtikrinančio, kad jachta regatoje dalyvauja tik vienoje grupėje

CREATE TABLE Kapitonai (
  Id INTEGER IDENTITY,
  Vardas VARCHAR(64)
  );
CREATE TABLE Savininkai (
  Id INTEGER IDENTITY,
  Vardas VARCHAR(64)
  );
CREATE TABLE Modeliai (
  Id INTEGER IDENTITY,
  Pavadinimas VARCHAR(32) NOT NULL CHECK (length(trim(both from Pavadinimas)) > 0),
  Koeficientas FLOAT,
  ProjektavimoMetai INTEGER CHECK (ProjektavimoMetai BETWEEN 1500 AND EXTRACT(YEAR FROM CURRENT_DATE) + 1),
  Ilgis INTEGER CONSTRAINT IlgioRibojimas CHECK (Ilgis BETWEEN 100 AND 10000), -- centimetrais
  Plotis INTEGER CONSTRAINT PločioRibojimas CHECK (Plotis BETWEEN 50 AND 5000), -- centimetrais
  Vandentalpa INTEGER CHECK (Vandentalpa BETWEEN 10 AND 100000), -- kilogramais
  VaterlinijosIlgis INTEGER CHECK (VaterlinijosIlgis BETWEEN 100 AND 10000), -- centimetrais
  BuriųPlotasPlaukiantPavėjui INTEGER CHECK (BuriųPlotasPlaukiantPavėjui < 10000), -- kvadratiniais metrais
  BuriųPlotasPlaukiantPriešVėją INTEGER CHECK (BuriųPlotasPlaukiantPriešVėją < 10000), -- kvadratiniais metrais
  Pastabos VARCHAR(64)
  );
CREATE TABLE Jachtos (
  Id INTEGER IDENTITY,
  BurėsNumeris VARCHAR(32) NOT NULL UNIQUE CHECK (length(trim(both from BurėsNumeris)) > 0),
  Modelis INTEGER NOT NULL REFERENCES Modeliai,
  PagaminimoMetai INTEGER CHECK (PagaminimoMetai BETWEEN 1500 AND EXTRACT(YEAR FROM CURRENT_DATE) + 1),
  Kapitonas INTEGER REFERENCES Kapitonai,
  Savininkas INTEGER REFERENCES Savininkai,
  Pavadinimas VARCHAR(64) NOT NULL UNIQUE CHECK (length(trim(both from Pavadinimas)) > 0), -- FIXME: ar tinka UNIQUE?
  Rėmėjai VARCHAR(64),
  Pastabos VARCHAR(64)
  );
CREATE TABLE Regatos (
  Id INTEGER IDENTITY,
  Pavadinimas VARCHAR(64) NOT NULL,
  Regionas VARCHAR(64),
  Pradžia DATE,
  Pabaiga DATE,
  Sistema TINYINT, -- FIXME: enum?
  Pastabos VARCHAR(64)
  );
CREATE TABLE Grupės (
  Id INTEGER IDENTITY,
  Regata INTEGER NOT NULL REFERENCES Regatos,
  Pavadinimas VARCHAR(32),
  Pastabos VARCHAR(64)
  );
CREATE TABLE Etapai (
  Id INTEGER IDENTITY,
  Grupė INTEGER NOT NULL REFERENCES Grupės ON DELETE CASCADE,
  Pavadinimas VARCHAR(32) NOT NULL CHECK (length(trim(both from Pavadinimas)) > 0),
  Sistema TINYINT, -- FIXME: enum?
  Pastabos VARCHAR(64),
  );
CREATE TABLE Dalyviai (
  Jachta INTEGER NOT NULL REFERENCES Jachtos,
  Grupė INTEGER NOT NULL REFERENCES Grupės ON DELETE CASCADE,
  Kapitonas INTEGER REFERENCES Kapitonai,
  Savininkas INTEGER REFERENCES Savininkai,
  Rėmėjai VARCHAR(64),
  PRIMARY KEY(Jachta, Grupė) -- FIXME: neleisti dalyvauti toje pačioje regatoje keliose grupėse (pridėti išorinį raktą arba trigerį)
  );
CREATE TABLE Plaukimai (
  Jachta INTEGER NOT NULL REFERENCES Jachtos,
  Etapas INTEGER NOT NULL REFERENCES Etapai ON DELETE CASCADE,
  StartoLaikas TIMESTAMP,
  FinišoLaikas TIMESTAMP,
  Kodas INTEGER, -- FIXME: enum?
  Pastabos VARCHAR(64),
  PRIMARY KEY(Jachta, Etapas) -- jachta etape plaukia tik kartą
  );

CREATE INDEX Modelis ON Jachtos (Modelis);

-- TODO: perrašyti trigerius iš plpgsql į Java
-- Jachta negali būti pagaminta anksčiau, negu suprojektuotas jos modelis
CREATE FUNCTION JachtosMetai()
  RETURNS "trigger" AS
  '
    BEGIN
      IF (SELECT COALESCE(ProjektavimoMetai, 0) FROM Modeliai WHERE Id = NEW.Modelis) > COALESCE(NEW.PagaminimoMetai, 0)
        THEN
	  RAISE EXCEPTION ''Jachta negali būti pagaminta anksčiau, negu buvo suprojektuota.'';
	END IF;
	RETURN NEW;
    END;
  '
  LANGUAGE 'plpgsql';
CREATE TRIGGER JachtosMetai
  AFTER INSERT OR UPDATE ON Jachtos
  FOR EACH ROW EXECUTE PROCEDURE JachtosMetai();

-- Modelis negali būti suprojektuotas vėliau, negu yra pagaminta kokia nors to modelio jachta
CREATE FUNCTION ModelioMetai()
  RETURNS "trigger" AS
  '
    BEGIN
      IF (SELECT COUNT(*) FROM Jachtos WHERE Modelis = NEW.Id AND PagaminimoMetai > NEW.ProjektavimoMetai) > 0
        THEN
	  RAISE EXCEPTION ''Modelis negali būti suprojektuotas anksčiau, negu yra pagaminta kokia nors to modelio jachta.'';
	END IF;
	RETURN NEW;
    END;
  '
  LANGUAGE 'plpgsql';
CREATE TRIGGER ModelioMetai
  AFTER UPDATE ON Modeliai
  FOR EACH ROW EXECUTE PROCEDURE ModelioMetai();
