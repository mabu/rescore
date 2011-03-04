/**
 * Jachtos savininkas.
 * Objektai saugomi duombazės lentelėje Savininkai.
 */
package rescore;

import java.util.List;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Owner extends NamedEntity {
  private static Logger logger = Logger.getLogger(Owner.class.getName());
  private static PreparedStatement selectOwner, selectAllOwners, selectAllOwnerIds, deleteOwner, updateName;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti savininko objektą iš kitur,
 * naudoti get() arba getAll().
 */
  private Owner(int id, String name) {
    super(id);
    this.name = name;
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectOwner
 */
  public Owner(int id, ResultSet resultSet) throws SQLException {
    this(id, resultSet.getString(1));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laikaus, kuriuos grąžina selectAllOwners
 */
  public Owner(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), resultSet.getString(2));
  }

/**
 * Grąžina objektą savininko su nurodytu id.
 *
 * @param id savininko id
 * @return savininkas su nurodytu id
 */
  public static Owner get(int id) {
    return (Owner)NamedEntity.get(id, selectOwner, Owner.class);
  }

/**
 * Grąžina visų savininkų sąrašą jų id didėjimo tvarka.
 *
 * @return visų savininkų sąrašas id didėjimo tvarka
 */
  public static List<Owner> getAll() {
    return (List<Owner>)NamedEntity.getAll(selectAllOwners, selectAllOwnerIds, Owner.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectOwner = connection.prepareStatement("SELECT Vardas FROM Savininkai WHERE Id = ?");
      selectAllOwners = connection.prepareStatement("SELECT Id, Vardas FROM Savininkai ORDER BY Id");
      selectAllOwnerIds = connection.prepareStatement("SELECT Id FROM Savininkai ORDER BY Id");
      updateName = connection.prepareStatement("UPDATE Savininkai SET Vardas = ? WHERE Id = ?");
      deleteOwner = connection.prepareStatement("DELETE FROM Savininkai WHERE Id = ?");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  public boolean setName(String name) {
    return setName(name, updateName);
  }

  public String getName(){
	  return name;
  }
  
  public boolean remove() {
    return remove(deleteOwner);
  }

}
