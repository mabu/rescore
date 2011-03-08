/**
 * Etapas.
 * Objektai saugomi duombazės lentelėje Etapai.
 */
package rescore;

import java.util.List;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Leg extends NamedEntity {
  private static Logger logger = Logger.getLogger(Leg.class.getName());
  private static PreparedStatement selectLeg, selectAllLegs, selectAllLegIds, deleteLeg, updateName, updateNotes, insertLeg;

  private Group group;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti etapo objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private Leg(int id, Group group, String name, String notes) {
    super(id, name, notes);
    this.group = group;
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectLeg
 */
  public Leg(int id, ResultSet resultSet) throws SQLException {
    this(id, Group.get(resultSet.getInt(1)), resultSet.getString(2), resultSet.getString(3));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectAllLegs
 */
  public Leg(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), Group.get(resultSet.getInt(2)), resultSet.getString(3), resultSet.getString(4));
  }

/**
 * Grąžina objektą etapo su nurodytu id.
 *
 * @param id etapo id
 * @return etapas su nurodytu id
 */
  public static Leg get(int id) {
    return (Leg)NamedEntity.get(id, selectLeg, Leg.class);
  }

/**
 * Grąžina visų etapų sąrašą jų id didėjimo tvarka.
 *
 * @return visų etapų sąrašas id didėjimo tvarka
 */
  public static List<Leg> getAll() {
    return (List<Leg>)NamedEntity.getAll(selectAllLegs, selectAllLegIds, Leg.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectLeg = connection.prepareStatement("SELECT Grupė, Pavadinimas, Pastabos FROM Etapai WHERE Id = ?");
      selectAllLegs = connection.prepareStatement("SELECT Id, Grupė, Pavadinimas, Pastabos FROM Etapai ORDER BY Id");
      selectAllLegIds = connection.prepareStatement("SELECT Id FROM Etapai ORDER BY Id");
      insertLeg = connection.prepareStatement("INSERT INTO Etapai (Grupė, Pavadinimas, Pastabos) VALUES(?, ?, ?)");
      updateName = connection.prepareStatement("UPDATE Etapai SET Pavadinimas = ? WHERE Id = ?");
      updateNotes = connection.prepareStatement("UPDATE Etapai SET Pastabos = ? WHERE Id = ?");
      deleteLeg = connection.prepareStatement("DELETE FROM Etapai WHERE Id = ?");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

/**
 * Sukuria naują etapą.
 * Įrašo į duomenų bazę.
 *
 * @param group grupė, kurioje etapas kuriamas
 * @param name pavadinimas
 * @param notes pastabos (gali būti null)
 * @return etapas, jeigu sukūrimas pavyko, arba null, jei įvyko klaida
 */
  public static Leg create(Group group, String name, String notes) {
    try {
      insertLeg.setInt(1, group.getId());
      insertLeg.setString(2, name);
      if (notes == null)
        insertLeg.setNull(3, java.sql.Types.VARCHAR);
      else
        insertLeg.setString(3, notes);
      if (insertLeg.executeUpdate() == 1) {
        return new Leg(getLastInsertId(), group, name, notes);
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

  public Group getGroup() {
    return group;
  }

  public boolean remove() {
    return remove(deleteLeg);
  }

}
