/**
 * Jachtų modeliai.
 * Objektai saugomi duombazės lentelėje Modeliai.
 */
package rescore;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class YachtClass extends NamedEntity {
  private static Logger logger = Logger.getLogger(YachtClass.class.getName());
  private static PreparedStatement selectYachtClass, selectAllYachtClasses, selectAllYachtClassIds, updateName, updateNotes, deleteYachtClass;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti modelio objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private YachtClass(int id, String name, String notes) {
    super(id, name, notes);
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectYachtClass
 */
  public YachtClass(int id, ResultSet resultSet) throws SQLException {
    this(id, resultSet.getString(1), resultSet.getString(2));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectAllYachtClasses
 */
  public YachtClass(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
  }

/**
 * Grąžina objektą modelio su nurodytu id.
 *
 * @param id modelio id
 * @return modelis su nurodytu id
 */
  public static YachtClass get(int id) {
    return (YachtClass)NamedEntity.get(id, selectYachtClass, YachtClass.class);
  }

/**
 * Grąžina visų modelių sąrašą jų id didėjimo tvarka.
 *
 * @return visų modelių sąrašas id didėjimo tvarka
 */
  public static List<YachtClass> getAll() {
    return (List<YachtClass>)NamedEntity.getAll(selectAllYachtClasses, selectAllYachtClassIds, YachtClass.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectYachtClass = connection.prepareStatement("SELECT Pavadinimas, Pastabos FROM Modeliai WHERE Id = ?");
      selectAllYachtClasses = connection.prepareStatement("SELECT Id, Pavadinimas, Pastabos FROM Modeliai ORDER BY Id");
      selectAllYachtClassIds = connection.prepareStatement("SELECT Id FROM Modeliai ORDER BY Id");
      updateName = connection.prepareStatement("UPDATE Modeliai SET Pavadinimas = ? WHERE Id = ?");
      updateNotes = connection.prepareStatement("UPDATE Modeliai SET Pastabos = ? WHERE Id = ?");
      deleteYachtClass = connection.prepareStatement("DELETE FROM Modeliai WHERE Id = ?");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  public boolean setName(String name) {
    return setName(name, updateName);
  }

  public boolean setNotes(String notes) {
    return setNotes(notes, updateNotes);
  }

  public boolean remove() {
    return remove(deleteYachtClass);
  }
}
