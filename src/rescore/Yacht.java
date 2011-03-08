/**
 * Jachta.
 * Objektai saugomi duombazės lentelėje Jachtos.
 */
package rescore;

import java.util.List;
import java.util.Vector;
import java.util.TreeMap;
import java.lang.ref.WeakReference;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Yacht extends NamedEntity {
  private static Logger logger = Logger.getLogger(Yacht.class.getName());
  private static PreparedStatement selectYacht, selectAllYachts, selectAllYachtIds, insertYacht, deleteYacht, updateSailNumber, updateYachtClass, updateName, updateNotes, updateYear, updateCaptain, updateOwner, updateSponsors, selectCaptains, selectOwners, insertParticipant, deleteParticipant, selectParticipant, updateParticipantCaptain, updateParticipantOwner, updateParticipantSponsors;
  private String sailNumber;
  private YachtClass yachtClass;
  private int year, yachtClassId;
  private String sponsors, captain, owner;
  private TreeMap<Integer, WeakReference<Participant> > participantMap = new TreeMap<Integer, WeakReference<Participant> >(); // iš duombazės užkrauta informacija apie regatų dalyvius (pagal grupių Id)

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
      insertParticipant = connection.prepareStatement("INSERT INTO Dalyviai (Jachta, Grupė, Kapitonas, Savininkas, Rėmėjai) VALUES (?, ?, ?, ?, ?)");
      deleteParticipant = connection.prepareStatement("DELETE FROM Dalyviai WHERE Jachta = ? AND Grupė = ?");
      selectParticipant = connection.prepareStatement("SELECT Kapitonas, Savininkas, Rėmėjai FROM Dalyviai WHERE Jachta = ? AND Grupė = ?");
      updateParticipantCaptain = connection.prepareStatement("UPDATE Dalyviai SET Kapitonas = ? WHERE Jachta = ? AND Grupė = ?");
      updateParticipantOwner = connection.prepareStatement("UPDATE Dalyviai SET Savininkas = ? WHERE Jachta = ? AND Grupė = ?");
      updateParticipantSponsors = connection.prepareStatement("UPDATE Dalyviai SET Rėmėjai = ? WHERE Jachta = ? AND Grupė = ?");
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
    try {
      updateYachtClass.setInt(1, yachtClass.getId());
      updateYachtClass.setInt(2, id);
      int rowsAffected = updateYachtClass.executeUpdate();
      if (rowsAffected == 1) {
        this.yachtClass = yachtClass;
        return true;
      } else {
        logger.warn("Strange setYachtClass updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("setYachtClass SQL error: " + exception.getMessage());
    }
    return false;
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
    if (captain == null) {
      if (this.captain == null)
        return false;
    } else if (captain.equals(this.captain))
      return false;
    if (updateString(updateCaptain, captain)) {
      this.captain = captain;
      return true;
    }
    return false;
  }

  public boolean setOwner(String owner) {
    if (owner == null) {
      if (this.owner == null)
        return false;
    } else if (owner.equals(this.owner))
      return false;
    if (updateString(updateOwner, owner)) {
      this.owner = owner;
      return true;
    }
    return false;
  }

  public boolean setSponsors(String sponsors) {
    if (sponsors == null) {
      if (this.sponsors == null)
        return false;
    } else if (sponsors.equals(this.sponsors))
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

  /**
   * Dalyvio užregistravimas regatoje (konkrečioje jos grupėje).
   *
   * @param group regatos grupė
   * @return true, jei dalyvis pridėtas; false – jei įvyko klaida
   *         (galbūt jachta šioje regatoje jau dalyvauja)
   */
  public boolean participate(Group group) {
    try {
      insertParticipant.setInt(1, id);
      insertParticipant.setInt(2, group.getId());
      if (captain == null)
        insertParticipant.setNull(3, java.sql.Types.VARCHAR);
      else
        insertParticipant.setString(3, captain);
      if (owner == null)
        insertParticipant.setNull(4, java.sql.Types.VARCHAR);
      else
        insertParticipant.setString(4, owner);
      if (sponsors == null)
        insertParticipant.setNull(5, java.sql.Types.VARCHAR);
      else
        insertParticipant.setString(5, sponsors);
      if (insertParticipant.executeUpdate() == 1) {
        participantMap.put(group.getId(), new WeakReference<Participant>(new Participant(captain, owner, sponsors)));
        return true;
      } else {
        // TODO
      }
    } catch (SQLException exception) {
      logger.error("participate SQL error: " + exception.getMessage());
    }
    return false;
  }

  /**
   * Pašalina jachtą iš regatos dalyvių.
   * Visi šios jachtos atitinkamos regatos rezultatai ištrinami.
   *
   * @param group grupė, iš kurios ištrinama
   * @return true, jei pašalinimas pavyko; false, jei įvyko klaida
   *         (galbūt jachta šioje regatoje ir nedalyvavo)
   */
  public boolean cancelParticipation(Group group) {
    try {
      deleteParticipant.setInt(1, id);
      deleteParticipant.setInt(2, group.getId());
      if (deleteParticipant.executeUpdate() == 1) {
        participantMap.remove(group.getId());
        return true;
      } else {
        // TODO
      }
    } catch (SQLException exception) {
      logger.error("cancelParticipation SQL error: " + exception.getMessage());
    }
    return false;
  }

  /**
   * Grąžina jachtos kapitoną konkrečioje regatoje.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return jachtos kapitonas konkrečioje regotje
   */
  public String getCaptain(Group group) {
    return getParticipant(group).captain;
  }

  /**
   * Grąžina jachtos savininką konkrečioje regatoje.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return jachtos savininkas konkrečioje regatoje
   */
  public String getOwner(Group group) {
    return getParticipant(group).owner;
  }

  /**
   * Grąžina jachtos rėmėjus konkrečioje regatoje.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return jachtos rėmėjai konkrečioje regatoje
   */
  public String getSponsors(Group group) {
    return getParticipant(group).sponsors;
  }

  /**
   * Nustato jachtos kapitoną konkrečioje regatoje.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return true, jei kapitonas pakeistas; false, jei kapitonas nepakeistas
   *        (buvo toks pats, arba įvyko klaida)
   */
  public boolean setCaptain(Group group, String captain) {
    Participant participant = getParticipant(group);
    if (participant.captain == null) {
      if (captain == null)
        return false;
    } else if (participant.captain.equals(captain))
      return false;
    if (participant.updateString(group, updateParticipantCaptain, captain)) {
      participant.captain = captain;
      return true;
    }
    return false;
  }

  /**
   * Nustato jachtos savininką konkrečioje regatoje.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return true, jei savininkas pakeistas; false, jei savininkas nepakeistas
   *        (buvo toks pats, arba įvyko klaida)
   */
  public boolean setOwner(Group group, String owner) {
    Participant participant = getParticipant(group);
    if (participant.owner == null) {
      if (owner == null)
        return false;
    } else if (participant.owner.equals(owner))
      return false;
    if (participant.updateString(group, updateParticipantOwner, owner)) {
      participant.owner = owner;
      return true;
    }
    return false;
  }

  /**
   * Nustato jachtos rėmėjus konkrečioje regatoje.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return true, jei rėmėjai pakeisti; false, jei rėmėjai nepakeisti
   *        (buvo tokie patys, arba įvyko klaida)
   */
  public boolean setSponsors(Group group, String sponsors) {
    Participant participant = getParticipant(group);
    if (participant.sponsors == null) {
      if (sponsors == null)
        return false;
    } else if (participant.sponsors.equals(sponsors))
      return false;
    if (participant.updateString(group, updateParticipantSponsors, sponsors)) {
      participant.sponsors = sponsors;
      return true;
    }
    return false;
  }

  /**
   * Grąžina informaciją apie jachtą, dalyvavimo konkrečioje regatoje metu.
   *
   * @param group regatos grupė, kurioje dalyvauja jachta
   * @return informacija apie jachtą, dalyvavimo konkrečioje regatoje metu,
   *         arba null, jei jachta duotoje grupėje nedalyvauja arba įvyko kita
   *         klaida
   */
  private Participant getParticipant(Group group) {
    WeakReference<Participant> weakReference = participantMap.get(group.getId());
    Participant participant = null;
    if (weakReference != null)
      participant = weakReference.get();
    if (participant == null) {
      try {
        selectParticipant.setInt(1, id);
        selectParticipant.setInt(2, group.getId());
        ResultSet resultSet = selectParticipant.executeQuery();
        if (resultSet.next()) {
          participant = new Participant(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
          participantMap.put(group.getId(), new WeakReference<Participant>(participant));
        } else {
          logger.warn("Participant not found in the database");
        }
      } catch (SQLException exception) {
        logger.error("get SQL error: " + exception.getMessage());
      }
    }
    return participant;
  }

  /**
   * Struktūra kurioje nors regatoje dalyvaujančios jachtos informacijai saugoti.
   */
  private class Participant {
    public String captain, owner, sponsors;

    public Participant(String captain, String owner, String sponsors) {
      this.captain = captain;
      this.owner = owner;
      this.sponsors = sponsors;
    }

  /**
   * Įvykdo užklausą dalyvio informacijos VARCHAR lauko atnaujinimui.
   *
   * @param group grupė, kurioje esančios jachtos duomenis keisime
   * @param update paruoštas update sakinys, kuriam duodamas String,
   *        jachtos bei grupės Id
   * @param value nauja reikšmė
   * @return true, jei užklausa įvykdyta ir pakito 1 eilutė;
   *         false priešingu atveju
   */
    public boolean updateString(Group group, PreparedStatement update, String value) {
      try {
        if (value == null)
          update.setNull(1, java.sql.Types.VARCHAR);
        else
          update.setString(1, value);
        update.setInt(2, id);
        update.setInt(3, group.getId());
        int rowsAffected = update.executeUpdate();
        if (rowsAffected == 1) {
          return true;
        } else {
          logger.warn("Strange updateString updated database rows count: " + rowsAffected);
        }
      } catch (SQLException exception) {
        logger.error("updateString SQL error: " + exception.getMessage());
      }
      return false;
    }

  }
}
