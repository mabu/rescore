/**
 * Regata.
 * Objektai saugomi duombazės lentelėje Regatos.
 */
package rescore;

import java.util.List;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Regatta extends NamedEntity {
  private static Logger logger = Logger.getLogger(Regatta.class.getName());
  private static PreparedStatement selectRegatta, selectAllRegattas, selectAllRegattaIds, deleteRegatta, updateName, updateNotes, updateRegion, updateBeginning, updateEnd, insertRegatta;

  String region;
  Date beginning, end;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti regatos objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private Regatta(int id, String name, String region, Date beginning, Date end, String notes) {
    super(id, name, notes);
    this.region = region;
    this.beginning = beginning;
    this.end = end;
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectRegatta
 */
  public Regatta(int id, ResultSet resultSet) throws SQLException {
    this(id, resultSet.getString(1), resultSet.getString(2), resultSet.getDate(3), resultSet.getDate(4), resultSet.getString(5));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectAllRegattas
 */
  public Regatta(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDate(4), resultSet.getDate(5), resultSet.getString(6));
  }

/**
 * Grąžina objektą regatos su nurodytu id.
 *
 * @param id regatos id
 * @return regata su nurodytu id
 */
  public static Regatta get(int id) {
    return (Regatta)NamedEntity.get(id, selectRegatta, Regatta.class);
  }

/**
 * Grąžina visų regatų sąrašą jų id didėjimo tvarka.
 *
 * @return visų regatų sąrašas id didėjimo tvarka
 */
  public static List<Regatta> getAll() {
    return (List<Regatta>)NamedEntity.getAll(selectAllRegattas, selectAllRegattaIds, Regatta.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectRegatta = connection.prepareStatement("SELECT Pavadinimas, Regionas, Pradžia, Pabaiga, Pastabos FROM Regatos WHERE Id = ?");
      selectAllRegattas = connection.prepareStatement("SELECT Id, Pavadinimas, Regionas, Pradžia, Pabaiga, Pastabos FROM Regatos ORDER BY Id");
      selectAllRegattaIds = connection.prepareStatement("SELECT Id FROM Regatos ORDER BY Id");
      insertRegatta = connection.prepareStatement("INSERT INTO Regatos (Pavadinimas, Regionas, Pradžia, Pabaiga, Pastabos) VALUES(?, ?, ?, ?, ?)");
      updateName = connection.prepareStatement("UPDATE Regatos SET Pavadinimas = ? WHERE Id = ?");
      updateNotes = connection.prepareStatement("UPDATE Regatos SET Pastabos = ? WHERE Id = ?");
      updateRegion = connection.prepareStatement("UPDATE Regatos SET Regionas = ? WHERE Id = ?");
      updateBeginning = connection.prepareStatement("UPDATE Regatos SET Pradžia = ? WHERE Id = ?");
      updateEnd = connection.prepareStatement("UPDATE Regatos SET Pabaiga = ? WHERE Id = ?");
      deleteRegatta = connection.prepareStatement("DELETE FROM Regatos WHERE Id = ?");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

/**
 * Sukuria naują regatą.
 * Įrašo į duomenų bazę.
 *
 * @param name pavadinimas
 * @param region regionas (gali būti null)
 * @param beginning pradžios data (gali būti null)
 * @param end pabaigos data (gali būti null)
 * @param notes pastabos (gali būti null)
 * @return regata, jeigu sukūrimas pavyko, arba null, jei įvyko klaida
 */
  public static Regatta create(String name, String region, Date beginning, Date end, String notes) {
    try {
      insertRegatta.setString(1, name);
      if (region == null)
        insertRegatta.setNull(2, java.sql.Types.VARCHAR);
      else
        insertRegatta.setString(2, region);
      if (beginning == null)
        insertRegatta.setNull(3, java.sql.Types.DATE);
      else
        insertRegatta.setDate(3, new java.sql.Date(beginning.getTime()));
      if (end == null)
        insertRegatta.setNull(4, java.sql.Types.DATE);
      else
        insertRegatta.setDate(4, new java.sql.Date(end.getTime()));
      if (notes == null)
        insertRegatta.setNull(5, java.sql.Types.VARCHAR);
      else
        insertRegatta.setString(5, notes);
      if (insertRegatta.executeUpdate() == 1) {
        return new Regatta(getLastInsertId(), name, region, beginning, end, notes);
      } else {
        // TODO
      }
    } catch (SQLException exception) {
      logger.error("create SQL error: " + exception.getMessage());
    }
    return null;
  }

  public boolean setName(String name) {
    return setName(name, updateName);
  }

  public boolean setNotes(String notes) {
    return setNotes(notes, updateNotes);
  }

  public String getRegion() {
    return region;
  }

  public boolean setRegion(String region) {
    if (region == null) {
      if (this.region == null)
        return false;
    } else if (region.equals(this.region))
      return false;
    if (updateString(updateRegion, region)) {
      this.region = region;
      return true;
    }
    return false;
  }

  public Date getBeginning() {
    return beginning;
  }
  
  public boolean setBeginning(Date beginning) {
    if (beginning == null) {
      if (this.beginning == null)
        return false;
    } else if (beginning.equals(this.beginning))
      return false;
    if (updateDate(updateBeginning, beginning)) {
      this.beginning = beginning;
      return true;
    }
    return false;
  }

  public Date getEnd() {
    return end;
  }
  
  public boolean setEnd(Date end) {
    if (end == null) {
      if (this.end == null)
        return false;
    } else if (end.equals(this.end))
      return false;
    if (updateDate(updateEnd, end)) {
      this.end = end;
      return true;
    }
    return false;
  }

  private boolean updateDate(PreparedStatement update, Date value) {
    try {
      if (value == null)
        update.setNull(1, java.sql.Types.DATE);
      else
        update.setDate(1, new java.sql.Date(value.getTime()));
      update.setInt(2, id);
      int rowsAffected = update.executeUpdate();
      if (rowsAffected == 1) {
        return true;
      } else {
        logger.warn("Strange updateDate updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("updateDate SQL error: " + exception.getMessage());
    }
    return false;
  }

  public boolean remove() {
    return remove(deleteRegatta);
  }

}
