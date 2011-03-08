/**
 * Grupė.
 * Objektai saugomi duombazės lentelėje Grupės.
 */
package rescore;

import java.util.List;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Group extends NamedEntity {
  private static Logger logger = Logger.getLogger(Group.class.getName());
  private static PreparedStatement selectGroup, selectAllGroups, selectAllGroupIds, deleteGroup, updateName, updateNotes, insertGroup;

  private Regatta regatta;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti grupės objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private Group(int id, Regatta regatta, String name, String notes) {
    super(id, name, notes);
    this.regatta = regatta;
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectGroup
 */
  public Group(int id, ResultSet resultSet) throws SQLException {
    this(id, Regatta.get(resultSet.getInt(1)), resultSet.getString(2), resultSet.getString(3));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectAllGroups
 */
  public Group(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), Regatta.get(resultSet.getInt(2)), resultSet.getString(3), resultSet.getString(4));
  }

/**
 * Grąžina objektą grupės su nurodytu id.
 *
 * @param id grupės id
 * @return grupė su nurodytu id
 */
  public static Group get(int id) {
    return (Group)NamedEntity.get(id, selectGroup, Group.class);
  }

/**
 * Grąžina visų grupių sąrašą jų id didėjimo tvarka.
 *
 * @return visų grupių sąrašas id didėjimo tvarka
 */
  public static List<Group> getAll() {
    return (List<Group>)NamedEntity.getAll(selectAllGroups, selectAllGroupIds, Group.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectGroup = connection.prepareStatement("SELECT Regata, Pavadinimas, Pastabos FROM Grupės WHERE Id = ?");
      selectAllGroups = connection.prepareStatement("SELECT Id, Regata, Pavadinimas, Pastabos FROM Grupės ORDER BY Id");
      selectAllGroupIds = connection.prepareStatement("SELECT Id FROM Grupės ORDER BY Id");
      insertGroup = connection.prepareStatement("INSERT INTO Grupės (Regata, Pavadinimas, Pastabos) VALUES(?, ?, ?)");
      updateName = connection.prepareStatement("UPDATE Grupės SET Pavadinimas = ? WHERE Id = ?");
      updateNotes = connection.prepareStatement("UPDATE Grupės SET Pastabos = ? WHERE Id = ?");
      deleteGroup = connection.prepareStatement("DELETE FROM Grupės WHERE Id = ?");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

/**
 * Sukuria naują grupę.
 * Įrašo į duomenų bazę.
 *
 * @param regatta regata, kurioje grupė kuriama
 * @param name pavadinimas
 * @param notes pastabos (gali būti null)
 * @return grupė, jeigu sukūrimas pavyko, arba null, jei įvyko klaida
 */
  public static Group create(Regatta regatta, String name, String notes) {
    try {
      insertGroup.setInt(1, regatta.getId());
      insertGroup.setString(2, name);
      if (notes == null)
        insertGroup.setNull(3, java.sql.Types.VARCHAR);
      else
        insertGroup.setString(3, notes);
      if (insertGroup.executeUpdate() == 1) {
        return new Group(getLastInsertId(), regatta, name, notes);
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

  public Regatta getRegatta() {
    return regatta;
  }

  public boolean remove() {
    return remove(deleteGroup);
  }

}
