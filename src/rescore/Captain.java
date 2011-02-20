/**
 * Jachtos kapitonas.
 * Objektai saugomi duombazės lentelėje Kapitonai.
 */
package rescore;

import java.util.List;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Captain extends NamedEntity {
  private static Logger logger = Logger.getLogger(Captain.class.getName());
  private static PreparedStatement selectCaptain, selectAllCaptains, selectAllCaptainIds, deleteCaptain, updateName;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti kapitono objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private Captain(int id, String name) {
    super(id);
    this.name = name;
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectCaptain
 */
  public Captain(int id, ResultSet resultSet) throws SQLException {
    this(id, resultSet.getString(1));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laikaus, kuriuos grąžina selectAllCaptains
 */
  public Captain(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), resultSet.getString(2));
  }

/**
 * Grąžina objektą kapitono su nurodytu id.
 *
 * @param id kapitono id
 * @return kapitonas su nurodytu id
 */
  public static Captain get(int id) {
    return (Captain)NamedEntity.get(id, selectCaptain, Captain.class);
  }

/**
 * Grąžina visų kapitonų sąrašą jų id didėjimo tvarka.
 *
 * @return visų kapitonų sąrašas id didėjimo tvarka
 */
  public static List<Captain> getAll() {
    return (List<Captain>)NamedEntity.getAll(selectAllCaptains, selectAllCaptainIds, Captain.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectCaptain = connection.prepareStatement("SELECT Vardas FROM Kapitonai WHERE Id = ?");
      selectAllCaptains = connection.prepareStatement("SELECT Id, Vardas FROM Kapitonai ORDER BY Id");
      selectAllCaptainIds = connection.prepareStatement("SELECT Id FROM Kapitonai ORDER BY Id");
      updateName = connection.prepareStatement("UPDATE Kapitonai SET Vardas = ? WHERE Id = ?");
      deleteCaptain = connection.prepareStatement("DELETE FROM Kapitonai WHERE Id = ?");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  public boolean setName(String name) {
    return setName(name, updateName);
  }

  public boolean remove() {
    return remove(deleteCaptain);
  }

}
