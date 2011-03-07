/**
 * Jachta.
 * Objektai saugomi duombazės lentelėje Jachtos.
 */
package rescore;

import java.util.List;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Yacht extends NamedEntity {
  private static Logger logger = Logger.getLogger(Yacht.class.getName());
  private static PreparedStatement selectYacht, selectAllYachts, selectAllYachtIds, insertYacht, deleteYacht, updateSailNumber, updateYachtClass, updateName, updateNotes, updateYear, updateCaptain, updateOwner, updateSponsors, selectCaptains, selectOwners;
  private String sailNumber;
  private YachtClass yachtClass;
  private int year, yachtClassId;
  private String sponsors, captain, owner;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti jachtos objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private Yacht (int id, String sailNumber, int yachtClassId, String name, int year, String captain, String owner, String sponsors, String notes) {
    super(id, name, notes);
    this.sailNumber = sailNumber;
    this.yachtClassId = yachtClassId;
    this.year = year;
    this.captain = captain;
    this.owner = owner;
    this.sponsors = sponsors;
  }

/**
 * Šį konstruktorių kviečia statiniai NamedEntity klasės metodai,
 * imantys objektą iš duomenų bazės.
 *
 * @param id objekto ID
 * @param resultSet skaitymui paruošta duombazės eilutė su kitais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectYacht
 */
  public Yacht(int id, ResultSet resultSet) throws SQLException {
    this(id, resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectAllYachts
 */
  public Yacht(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9));
  }

/**
 * Grąžina jachtą pagal jos ID.
 *
 * @param id jachtos ID
 * @return jachtos objektas su duotu ID, arba null, jei tokios jachtos nėra
 */
  public static Yacht get(int id) {
    return (Yacht)NamedEntity.get(id, selectYacht, Yacht.class);
  }

/**
 * Grąžina visus dabartinius jachtų kapitonus.
 */
  public static List<String> getAllCaptains() {
    Vector<String> captains = new Vector();
    try {
      ResultSet resultSet = selectCaptains.executeQuery();
      while (resultSet.next()) {
        captains.add(resultSet.getString(1));
      }
    } catch (SQLException exception) {
      logger.error("getAllCaptains SQL error: " + exception.getMessage());
    }
    return captains;
  }

/**
 * Grąžina visus dabartinius jachtų savininkus.
 */
  public static List<String> getAllOwners() {
    Vector<String> owners = new Vector();
    try {
      ResultSet resultSet = selectOwners.executeQuery();
      while (resultSet.next()) {
        owners.add(resultSet.getString(1));
      }
    } catch (SQLException exception) {
      logger.error("getAllCaptains SQL error: " + exception.getMessage());
    }
    return owners;
  }

/**
 * Grąžina visų jachtų sąrašą ID didėjimo tvarka.
 *
 * @return visų jachtų sąrašas ID didėjimo tvarka
 */
  public static List<Yacht> getAll() {
    return (List<Yacht>)NamedEntity.getAll(selectAllYachts, selectAllYachtIds, Yacht.class);
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectYacht = connection.prepareStatement("SELECT BurėsNumeris, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai, Pastabos FROM Jachtos WHERE Id = ?");
      selectAllYachts = connection.prepareStatement("SELECT Id, BurėsNumeris, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai, Pastabos FROM Jachtos ORDER BY Id");
      selectAllYachtIds = connection.prepareStatement("SELECT Id FROM Jachtos ORDER BY Id");
      insertYacht = connection.prepareStatement("INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai, Pastabos) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
      deleteYacht = connection.prepareStatement("DELETE FROM Jachtos WHERE Id = ?");
      updateSailNumber = connection.prepareStatement("UPDATE Jachtos SET BurėsNumeris = ? WHERE Id = ?");
      updateYachtClass = connection.prepareStatement("UPDATE Jachtos SET Modelis = ? WHERE Id = ?");
      updateName = connection.prepareStatement("UPDATE Jachtos SET Pavadinimas = ? WHERE Id = ?");
      updateNotes = connection.prepareStatement("UPDATE Jachtos SET Pastabos = ? WHERE Id = ?");
      updateYear = connection.prepareStatement("UPDATE Jachtos SET PagaminimoMetai = ? WHERE Id = ?");
      updateCaptain = connection.prepareStatement("UPDATE Jachtos SET Kapitonas = ? WHERE Id = ?");
      updateOwner = connection.prepareStatement("UPDATE Jachtos SET Savininkas = ? WHERE Id = ?");
      updateSponsors = connection.prepareStatement("UPDATE Jachtos SET Rėmėjai = ? WHERE Id = ?");
      selectCaptains = connection.prepareStatement("SELECT DISTINCT Kapitonas FROM Jachtos");
      selectOwners = connection.prepareStatement("SELECT DISTINCT Savininkas FROM Jachtos");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

/**
 * Sukuria naują jachtą.
 * Įrašo į duomenų bazę.
 *
 * @param sailNumber burės numeris
 * @param yachtClass modelis
 * @param name pavadinimas
 * @param year pagaminimo metai (0, jei nežinomi)
 * @param captain kapitonas (gali būti null)
 * @param owner savininkas (gali būti null)
 * @param sponsors rėmėjai
 * @param notes pastabos
 * @return jachta, jeigu sukūrimas pavyko, arba null, jei jachta su tokiu burės
 *         numeriu jau egzistavo arba įvyko kita klaida
 */
  public static Yacht create(String sailNumber, YachtClass yachtClass, String name, int year, String captain, String owner, String sponsors, String notes) {
    Yacht yacht = null;
    try {
      insertYacht.setString(1, sailNumber);
      insertYacht.setInt(2, yachtClass.getId());
      if (name == null)
        insertYacht.setNull(3, java.sql.Types.VARCHAR);
      else
        insertYacht.setString(3, name);
      if (year == 0)
        insertYacht.setNull(4, java.sql.Types.INTEGER);
      else
        insertYacht.setInt(4, year);
      if (captain == null)
        insertYacht.setNull(5, java.sql.Types.VARCHAR);
      else
        insertYacht.setString(5, captain);
      if (owner == null)
        insertYacht.setNull(6, java.sql.Types.VARCHAR);
      else
        insertYacht.setString(6, owner);
      if (sponsors == null)
        insertYacht.setNull(7, java.sql.Types.VARCHAR);
      else
        insertYacht.setString(7, sponsors);
      if (notes == null)
        insertYacht.setNull(8, java.sql.Types.VARCHAR);
      else
        insertYacht.setString(8, notes);
      if (insertYacht.executeUpdate() == 1) {
        yacht = new Yacht(getLastInsertId(), sailNumber, yachtClass.getId(), name, year, captain, owner, sponsors, notes);
      } else {
        // TODO
      }
    } catch (SQLException exception) {
      logger.error("create SQL error: " + exception.getMessage());
    }
    return yacht;
  }

/**
 * Tas pats jachtos kūrimas, tik be pastabų.
 * Suderinamumui su pirminėmis sąsajomis.
 */
  public static Yacht create(String sailNumber, YachtClass yachtClass, String name, int year, String captain, String owner, String sponsors) {
    return create(sailNumber, yachtClass, name, year, captain, owner, sponsors, null);
  }

  /**
   * Panaikina jachtą iš duomenų bazės.
   * Toliau šis objektas nebeturėtų būti naudojamas.
   *
   * @return true, jei objektas panaikintas, false – jei įvyko klaida arba
   *         objektas buvo panaikintas anksčiau
   */
  public boolean remove() {
    return remove(deleteYacht);
  }

  protected void finalize () {
    super.finalize();
  }

  public boolean setSailNumber(String sailNumber) {
    if (sailNumber == null || sailNumber.equals(this.sailNumber))
      return false;
    boolean ret = false;
    try {
      updateSailNumber.setString(1, sailNumber);
      updateSailNumber.setInt(2, id);
      int rowsAffected = updateSailNumber.executeUpdate();
      if (rowsAffected == 1) {
        this.name = name;
        ret = true;
      } else {
        logger.warn("Strange setSailNumber updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setSailNumber SQL error: " + exception.getMessage());
    }
    return ret;
  }

  public boolean setYachtClass(YachtClass yachtClass) {
    boolean ret = false;
    try {
      updateYachtClass.setInt(1, yachtClass.getId());
      updateYachtClass.setInt(2, id);
      int rowsAffected = updateYachtClass.executeUpdate();
      if (rowsAffected == 1) {
        this.yachtClass = yachtClass;
        ret = true;
      } else {
        logger.warn("Strange setYachtClass updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setYachtClass SQL error: " + exception.getMessage());
    }
    return ret;
  }

  public boolean setName(String name) {
    return setName(name, updateName);
  }

  public boolean setNotes(String notes) {
    return setNotes(notes, updateNotes);
  }

  public boolean setYear(int year) {
    if (year == this.year)
      return false;
    if (updateInt(updateYear, year)) {
      this.year = year;
      return true;
    }
    return false;
  }

  public boolean setCaptain(String captain) {
    if (captain.equals(this.captain))
      return false;
    if (updateString(updateCaptain, captain)) {
      this.captain = captain;
      return true;
    }
    return false;
  }

  public boolean setOwner(String owner) {
    if (owner.equals(this.owner))
      return false;
    if (updateString(updateOwner, owner)) {
      this.owner = owner;
      return true;
    }
    return false;
  }

  public boolean setSponsors(String sponsors) {
    if (sponsors.equals(this.sponsors))
      return false;
    if (updateString(updateSponsors, sponsors)) {
      this.sponsors = sponsors;
      return true;
    }
    return false;
  }

  public String getSailNumber() {
    return sailNumber;
  }

  public YachtClass getYachtClass() {
    if (yachtClass == null)
      yachtClass = YachtClass.get(yachtClassId);
    return yachtClass;
  }

  /**
   * Jachtos pagaminimo metai.
   *
   * @return jachtos pagaminimo metai, arba 0, jei nežinomi
   */
  public int getYear() {
    return year;
  }

  public String getCaptain() {
    return captain;
  }

  public String getOwner() {
    return owner;
  }

  public String getSponsors() {
    return sponsors;
  }

}
