/**
 * Jachta.
 * Objektai saugomi duombazės lentelėje Jachtos.
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

public class Yacht {
  private static Logger logger = Logger.getLogger(Yacht.class.getName());
  private static PreparedStatement selectYacht, selectYachtBySailNumber, selectAllYachts, selectAllYachtIds, insertYacht, deleteYacht, updateSailNumber, updateYachtClass, updateName, updateYear, updateCaptain, updateOwner, updateSponsors, lastInsertId;
  private static TreeMap<Integer, WeakReference<Yacht> > yachts = new TreeMap<Integer, WeakReference<Yacht> >();
  private static TreeMap<String, WeakReference<Yacht> > yachtsBySailNumbers = new TreeMap<String, WeakReference<Yacht> >();
  private static int yachtsCount = -1; // kiek jachtų yra duomenų bazėje
  private static final double YACHT_LIST_RATIO = 0.5;
  // jeigu yachts turi mažesnę nei YACHT_LIST_RATIO dalį visų įrašų arba visų
  // įrašų skaičius nežinomas, tada duombazės užklausia visų įrašų; priešingu
  // atveju – visų burių numerių, pagal kuriuos gauna trūkstamas jachtas po vieną
  private String sailNumber;
  private YachtClass yachtClass;
  private String name;
  private int id, year, yachtClassId, captainId, ownerId;
  private Captain captain;
  private Owner owner;
  private String sponsors;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti jachtos objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private Yacht (int id, String sailNumber, int yachtClassId, String name, int year, int captainId, int ownerId, String sponsors) {
    this.id = id;
    this.sailNumber = sailNumber;
    this.yachtClassId = yachtClassId;
    this.name = name;
    this.year = year;
    this.captainId = captainId;
    this.ownerId = ownerId;
    this.sponsors = sponsors;
  }

/**
 * Grąžina jachtą pagal jos ID.
 *
 * @param id jachtos ID
 * @return jachtos objektas su duotu ID, arba null, jei tokios jachtos nėra
 */
  public static Yacht get(int id) {
    WeakReference<Yacht> weakReference = yachts.get(id);
    Yacht yacht = null;
    if (weakReference != null)
      yacht = weakReference.get();
    if (yacht == null) {
      try {
        selectYacht.setInt(1, id);
        ResultSet resultSet = selectYacht.executeQuery();
        if (resultSet.next()) {
          yacht = new Yacht(id, resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getString(7));
          yachts.put(yacht.getId(), new WeakReference<Yacht>(yacht));
          yachtsBySailNumbers.put(yacht.getSailNumber(), new WeakReference<Yacht>(yacht));
        }
      } catch (SQLException exception) {
        logger.error("get SQL error: " + exception.getMessage());
      }
    }
    return yacht;
  }

/**
 * Grąžina jachtą pagal jos burės numerį.
 *
 * @param sailNumber burės numeris
 * @return jachtos objektas su duotu numeriu, arba null, jei tokios jachtos nėra
 */
  public static Yacht getBySailNumber(String sailNumber) {
    WeakReference<Yacht> weakReference = yachtsBySailNumbers.get(sailNumber);
    Yacht yacht = null;
    if (weakReference != null)
      yacht = weakReference.get();
    if (yacht == null) {
      try {
        selectYachtBySailNumber.setString(1, sailNumber);
        ResultSet resultSet = selectYachtBySailNumber.executeQuery();
        if (resultSet.next()) {
          yacht = new Yacht(resultSet.getInt(1), sailNumber, resultSet.getInt(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getString(7));
          yachts.put(yacht.getId(), new WeakReference<Yacht>(yacht));
          yachtsBySailNumbers.put(sailNumber, new WeakReference<Yacht>(yacht));
        }
      } catch (SQLException exception) {
        logger.error("get SQL error: " + exception.getMessage());
      }
    }
    return yacht;
  }

/**
 * Grąžina visų jachtų sąrašą ID didėjimo tvarka.
 *
 * @return visų jachtų sąrašas ID didėjimo tvarka
 */
  public static List<Yacht> getAll() {
    Vector<Yacht> yachtList = new Vector<Yacht>();
    Yacht yacht;
    WeakReference<Yacht> weakReference;
    try {
      if (yachtsCount == -1 || yachts.size() / yachtsCount > YACHT_LIST_RATIO) {
        ResultSet resultSet = selectAllYachts.executeQuery();
        for (yachtsCount = 0; resultSet.next(); yachtsCount++) {
          yacht = null;
          weakReference = yachts.get(resultSet.getInt(1));
          if (weakReference != null)
            yacht = weakReference.get();
          if (yacht == null) {
            yacht = new Yacht(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getInt(7), resultSet.getString(8));
            yachts.put(resultSet.getInt(1), new WeakReference<Yacht>(yacht));
            yachtsBySailNumbers.put(resultSet.getString(2), new WeakReference<Yacht>(yacht));
          }
          yachtList.add(yacht);
        }
      } else {
        ResultSet resultSet = selectAllYachtIds.executeQuery();
        for (yachtsCount = 0; resultSet.next(); yachtsCount++) {
          yachtList.add(get(resultSet.getInt(1)));
        }
      }
    } catch (SQLException exception) {
      logger.error("getAll SQL error: " + exception.getMessage());
      yachtList = null;
    }
    return yachtList;
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectYacht = connection.prepareStatement("SELECT BurėsNumeris, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai FROM Jachtos WHERE Id = ?");
      selectYachtBySailNumber = connection.prepareStatement("SELECT Id, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai FROM Jachtos WHERE BurėsNumeris = ?");
      selectAllYachts = connection.prepareStatement("SELECT Id, BurėsNumeris, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai FROM Jachtos ORDER BY Id");
      selectAllYachtIds = connection.prepareStatement("SELECT Id FROM Jachtos ORDER BY Id");
      insertYacht = connection.prepareStatement("INSERT INTO Jachtos (BurėsNumeris, Modelis, Pavadinimas, PagaminimoMetai, Kapitonas, Savininkas, Rėmėjai) VALUES(?, ?, ?, ?, ?, ?, ?)");
      deleteYacht = connection.prepareStatement("DELETE FROM Jachtos WHERE Id = ?");
      updateSailNumber = connection.prepareStatement("UPDATE Jachtos SET BurėsNumeris = ? WHERE Id = ?");
      updateYachtClass = connection.prepareStatement("UPDATE Jachtos SET Modelis = ? WHERE Id = ?");
      updateName = connection.prepareStatement("UPDATE Jachtos SET Pavadinimas = ? WHERE Id = ?");
      updateYear = connection.prepareStatement("UPDATE Jachtos SET PagaminimoMetai = ? WHERE Id = ?");
      updateCaptain = connection.prepareStatement("UPDATE Jachtos SET Kapitonas = ? WHERE Id = ?");
      updateOwner = connection.prepareStatement("UPDATE Jachtos SET Savininkas = ? WHERE Id = ?");
      updateSponsors = connection.prepareStatement("UPDATE Jachtos SET Rėmėjai = ? WHERE Id = ?");
      lastInsertId = connection.prepareStatement("SELECT LAST_INSERT_ID()");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  static int getLastInsertId() {
    try {
      ResultSet resultSet = lastInsertId.executeQuery();
      return resultSet.getInt(1);
    } catch (SQLException exception) {
      logger.error("getLastInsertId SQL error: " + exception.getMessage());
    }
    return 0;
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
 * @return jachta, jeigu sukūrimas pavyko, arba null, jei jachta su tokiu burės
 *         numeriu jau egzistavo arba įvyko kita klaida
 */
  public static Yacht create(String sailNumber, YachtClass yachtClass, String name, int year, Captain captain, Owner owner, String sponsors) {
    Yacht yacht = null;
    try {
      int captainId, ownerId;
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
      if (captain == null) {
        insertYacht.setNull(5, java.sql.Types.INTEGER);
        captainId = 0;
      } else {
        captainId = captain.getId();
        insertYacht.setInt(5, captainId);
      }
      if (owner == null) {
        insertYacht.setNull(6, java.sql.Types.INTEGER);
        ownerId = 0;
      } else {
        ownerId = owner.getId();
        insertYacht.setInt(6, ownerId);
      }
      if (name == null)
        insertYacht.setNull(7, java.sql.Types.VARCHAR);
      else
        insertYacht.setString(7, sponsors);
      if (insertYacht.executeUpdate() == 1) {
        yacht = new Yacht(getLastInsertId(), sailNumber, yachtClass.getId(), name, year, captainId, ownerId, sponsors);
        yachts.put(yacht.getId(), new WeakReference<Yacht>(yacht));
        yachtsBySailNumbers.put(sailNumber, new WeakReference<Yacht>(yacht));
      } else {
        // TODO
      }
    } catch (SQLException exception) {
      logger.error("create SQL error: " + exception.getMessage());
    }
    return yacht;
  }

  /**
   * Panaikina jachtą iš duomenų bazės.
   * Toliau šis objektas nebeturėtų būti naudojamas.
   *
   * @return true, jei objektas panaikintas, false – jei įvyko klaida arba
   *         objektas buvo panaikintas anksčiau
   */
  public boolean remove() {
    if (sailNumber != null) {
      try {
        deleteYacht.setInt(1, id);
        int rowsDeleted = deleteYacht.executeUpdate();
        if (rowsDeleted == 1) {
          yachts.remove(id);
          yachtsBySailNumbers.remove(sailNumber);
          id = 0;
          sailNumber = null;
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

  protected void finalize () {
    yachts.remove(id);
    yachtsBySailNumbers.remove(sailNumber);
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

  public boolean setYear(int year) {
    boolean ret = false;
    try {
      if (year == 0)
        updateYear.setNull(1, java.sql.Types.INTEGER);
      else
        updateYear.setInt(1, year);
      updateYear.setInt(2, id);
      int rowsAffected = updateYear.executeUpdate();
      if (rowsAffected == 1) {
        this.year = year;
        ret = true;
      } else {
        logger.warn("Strange setYear updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setYear SQL error: " + exception.getMessage());
    }
    return ret;
  }

  public boolean setCaptain(Captain captain) {
    boolean ret = false;
    try {
      if (captain == null)
        updateCaptain.setNull(1, java.sql.Types.INTEGER);
      else
        updateCaptain.setInt(1, captain.getId());
      updateCaptain.setInt(2, id);
      int rowsAffected = updateCaptain.executeUpdate();
      if (rowsAffected == 1) {
        this.captain = captain;
        if (captain == null)
          this.captainId = 0;
        else
          this.captainId = captain.getId();
        ret = true;
      } else {
        logger.warn("Strange setCaptain updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setCaptain SQL error: " + exception.getMessage());
    }
    return ret;
  }

  public boolean setOwner(Owner owner) {
    boolean ret = false;
    try {
      if (owner == null)
        updateOwner.setNull(1, java.sql.Types.INTEGER);
      else
        updateOwner.setInt(1, owner.getId());
      updateOwner.setInt(2, id);
      int rowsAffected = updateOwner.executeUpdate();
      if (rowsAffected == 1) {
        this.owner = owner;
        if (owner == null)
          this.ownerId = 0;
        else
          this.ownerId = owner.getId();
        ret = true;
      } else {
        logger.warn("Strange setOwner updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setOwner SQL error: " + exception.getMessage());
    }
    return ret;
  }

  public boolean setSponsors(String sponsors) {
    boolean ret = false;
    try {
      if (sponsors == null)
        updateSponsors.setNull(1, java.sql.Types.VARCHAR);
      else
        updateSponsors.setString(1, sponsors);
      updateSponsors.setInt(2, id);
      int rowsAffected = updateSponsors.executeUpdate();
      if (rowsAffected == 1) {
        this.sponsors = sponsors;
        ret = true;
      } else {
        logger.warn("Strange setSponsors updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setSponsors SQL error: " + exception.getMessage());
    }
    return ret;
  }

  public int getId() {
    return id;
  }

  public String getSailNumber() {
    return sailNumber;
  }

  public YachtClass getYachtClass() {
    if (yachtClass == null)
      yachtClass = YachtClass.get(yachtClassId);
    return yachtClass;
  }

  public String getName() {
    return name;
  }

  /**
   * Jachtos pagaminimo metai.
   *
   * @return jachtos pagaminimo metai, arba 0, jei nežinomi
   */
  public int getYear() {
    return year;
  }

  public Captain getCaptain() {
    if (captain == null && captainId != 0)
      captain = Captain.get(captainId);
    return captain;
  }

  public Owner getOwner() {
    if (owner == null && ownerId != 0)
      owner = Owner.get(ownerId);
    return owner;
  }

  public String getSponsors() {
    return sponsors;
  }

}
