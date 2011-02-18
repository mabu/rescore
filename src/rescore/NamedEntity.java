/**
 * Esybės, turinčios ID ir pavadinimus.
 */
package rescore;

import java.lang.ref.WeakReference;
import java.util.TreeMap;
import java.util.List;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public abstract class NamedEntity {
  private static Logger logger = Logger.getLogger(NamedEntity.class.getName());
  private static PreparedStatement lastInsertId;
  protected static double LIST_RATIO = 0.5;
  // jeigu objectMap turi mažesnę nei LIST_RATIO dalį visų įrašų arba visų
  // įrašų skaičius nežinomas, tada duombazės užklausia visų įrašų; priešingu
  // atveju – visų ID, pagal kuriuos gauna trūkstamas esybes po vieną
  protected int id;
  protected String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  protected boolean setName(String name, PreparedStatement updateName) {
    boolean ret = false;
    try {
      if (name == null)
        updateName.setNull(1, java.sql.Types.VARCHAR);
      else
        updateName.setString(1, name);
      updateName.setInt(2, id);
      int rowsAffected = updateName.executeUpdate();
      if (rowsAffected == 1) {
        this.name = name;
        ret = true;
      } else {
        logger.warn("Strange setName updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setName SQL error: " + exception.getMessage());
    }
    return ret;
  }

  abstract public boolean setName(String name);

  /**
   * Panaikina esybę iš duomenų bazės.
   * Toliau šis objektas nebeturėtų būti naudojamas.
   *
   * @param erase užklausa atitinkamos lentelės eilutės šalinimui
   * @return true, jei objektas panaikintas, false – jei įvyko klaida arba
   *         objektas buvo panaikintas anksčiau
   */
  protected boolean remove(PreparedStatement erase) {
    if (id != 0) {
      try {
        erase.setInt(1, id);
        int rowsDeleted = erase.executeUpdate();
        if (rowsDeleted == 1) {
          getObjectMap().remove(id);
          id = 0;
          return true;
        } else {
          logger.warn("Strange remove deleted database rows count: " + rowsDeleted);
        }
      } catch (SQLException exception) {
        logger.error("remove SQL error: " + exception.getMessage());
      }
    }
    return false;
  }

  abstract public boolean remove();

  static protected int getLastInsertId() {
    try {
      ResultSet resultSet = lastInsertId.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      } else {
        logger.error("lastInsertId returned empty set");
      }
    } catch (SQLException exception) {
      logger.error("getLastInsertId SQL error: " + exception.getMessage());
    }
    return 0;
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      lastInsertId = connection.prepareStatement("SELECT LAST_INSERT_ID()");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  abstract protected TreeMap<Integer, WeakReference <? extends NamedEntity> > getObjectMap();

  protected void finalize () {
    getObjectMap().remove(id);
  }
}
