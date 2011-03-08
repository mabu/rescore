/**
 * Esybės, turinčios ID ir pavadinimus.
 */
package rescore;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.HashMap;
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
  private static HashMap<Class<? extends NamedEntity>, Integer> tableSizeEstimate = new HashMap<Class<? extends NamedEntity>, Integer>(); // kiek paskutinį kartą buvo įrašų atitinkamos esybės duombazės lentelėje
  private static HashMap<Class<? extends NamedEntity>, TreeMap<Integer, WeakReference<NamedEntity> > > objectMaps = new HashMap<Class<? extends NamedEntity>, TreeMap<Integer, WeakReference<NamedEntity> > >(); // iš duombazės užkrautos esybės
  protected static double LIST_RATIO = 0.5;
  // jeigu atitinkamas objectMaps elementas turi mažesnę nei LIST_RATIO dalį
  // numanomo lentelės įrašų skaičiaus (pagal atitinkamą tableSizeEstimate
  // elementą) arba numanomo skaičiaus nėra, tada duombazės užklausia visų įrašų;
  // priešingu atveju – visų ID, pagal kuriuos gauna trūkstamas esybes po vieną
  protected int id;
  protected String name, notes;

  // objectMaps inicializacija
  static {
    Class[] subClasses = {Yacht.class, YachtClass.class, Regatta.class}; // palaikomi poklasiai
    for (Class<? extends NamedEntity> subClass : subClasses)
      objectMaps.put(subClass, new TreeMap<Integer, WeakReference<NamedEntity> >());
    }

/**
 * Šį konstruktorių kviečia paveldinčios klasės.
 *
 * @param id esybės id
 * @param name esybės vardas/pavadinimas
 * @param notes pastabos
 */
  protected NamedEntity(int id, String name, String notes) {
    this.id = id;
    this.name = name;
    this.notes = notes;
    objectMaps.get(this.getClass()).put(id, new WeakReference<NamedEntity>(this));
  }

/**
 * Grąžina esybę pagal jos ID.
 *
 * @param id        esybės ID
 * @param select    paruošta užklausa esybės iš duomazės pagal ID gavimui
 * @param subClass  konkrečios esybės klasė, turinti konstruktorių, kuriam
 *                  perduodamas ID ir ResultSet
 * @return jeigu yra anksčiau gauta esybė su duotu ID – grąžina ją;
 *         jeigu ją paėmė iš duombazės – grąžina rezultatą (ResultSet);
 *         jeigu esybės su duotu ID nėra duombazėje, grąžina null
 */
  protected static NamedEntity get(int id, PreparedStatement select, Class<? extends NamedEntity> subClass) {
    TreeMap<Integer, WeakReference<NamedEntity> > objectMap = objectMaps.get(subClass);
    WeakReference<NamedEntity> weakReference = objectMap.get(id);
    NamedEntity namedEntity = null;
    if (weakReference != null)
      namedEntity = weakReference.get();
    if (namedEntity == null) {
      try {
        select.setInt(1, id);
        ResultSet resultSet = select.executeQuery();
        if (resultSet.next()) {
          namedEntity = subClass.getConstructor(int.class, ResultSet.class).newInstance(id, resultSet);
          objectMap.put(namedEntity.getId(), new WeakReference<NamedEntity>(namedEntity));
        } else {
          logger.warn("Entity not found in the database");
        }
      } catch (SQLException exception) {
        logger.error("get SQL error: " + exception.getMessage());
      } catch (Exception exception) {
        logger.error("get non-SQL error: " + exception.getMessage());
      }
    }
    return namedEntity;
  }

/**
 * Grąžina visų esybių sąrašą jų ID didėjimo tvarka.
 *
 * @param selectAll    paruošta užklausa visų subClass esybės objektų visų
 *                     duomenų gavimui iš duobmazės
 * @param selectAllIds paruošta užklausa visų subClass esybės objektų ID
 *                     gavimui iš duobmazės
 * @param subClass     konkrečios esybės klasė, turinti konstruktorius, kuriems
 *                     perduodamas ResultSet bei ID ir ResultSet
 * @return visų esybių sąrašas jų ID didėjimo tvarka arba null, jei įvyko klaida
 */
protected static List getAll(PreparedStatement selectAll, PreparedStatement selectAllIds, Class<? extends NamedEntity> subClass) {
  TreeMap<Integer, WeakReference<NamedEntity> > objectMap = objectMaps.get(subClass);
  Vector list = new Vector();
  NamedEntity namedEntity;
  WeakReference<NamedEntity> weakReference;
  int tableSize = 0;
  try {
    Integer count = tableSizeEstimate.get(subClass);
    if (count == null ||  objectMap.size() / count.doubleValue() < LIST_RATIO) {
      ResultSet resultSet = selectAll.executeQuery();
      for (tableSize = 0; resultSet.next(); tableSize++) {
        namedEntity = null;
        weakReference = objectMap.get(resultSet.getInt(1));
        if (weakReference != null)
          namedEntity = weakReference.get();
        if (namedEntity == null) {
          namedEntity = subClass.getConstructor(ResultSet.class).newInstance(resultSet);
          objectMap.put(namedEntity.getId(), new WeakReference<NamedEntity>(namedEntity));
        }
        list.add(namedEntity);
      }
    } else {
      ResultSet resultSet = selectAllIds.executeQuery();
      for (tableSize = 0; resultSet.next(); tableSize++) {
        list.add((NamedEntity)subClass.getMethod("get", int.class).invoke(null, resultSet.getInt(1)));
      }
    }
  } catch (SQLException exception) {
    logger.error("getAll SQL error: " + exception.getMessage());
    list = null;
  } catch (Exception exception) {
    logger.error("getAll non-SQL error: " + exception.getMessage());
    list = null;
  }
  tableSizeEstimate.put(subClass, tableSize);
  return list;
}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getNotes() {
    return notes;
  }

  protected boolean setName(String name, PreparedStatement updateName) {
    if (name == null) {
      if (this.name == null)
        return false;
    } else if (name.equals(this.name))
      return false;
    if (updateString(updateName, name)) {
      this.name = name;
      return true;
    }
    return false;
  }

  abstract public boolean setName(String name);

  protected boolean setNotes(String notes, PreparedStatement updateNotes) {
    if (notes == null) {
      if (this.notes == null)
        return false;
    } else if (notes.equals(this.notes))
      return false;
    if (updateString(updateNotes, notes)) {
      this.notes = notes;
      return true;
    }
    return false;
  }

  abstract public boolean setNotes(String notes);

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
          objectMaps.get(this.getClass()).remove(id);
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

/**
 * Įvykdo užklausą VARCHAR lauko atnaujinimui.
 *
 * @param update paruoštas update sakinys, kuriam duodamas String ir Id
 * @param value nauja reikšmė
 * @return true, jei užklausa įvykdyta ir pakito 1 eilutė, false priešingu atveju
 */
  protected boolean updateString(PreparedStatement update, String value) {
    try {
      if (value == null)
        update.setNull(1, java.sql.Types.VARCHAR);
      else
        update.setString(1, value);
      update.setInt(2, id);
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

/**
 * Įvykdo užklausą INTEGER lauko atnaujinimui.
 *
 * @param update paruoštas update sakinys, kuriam duodamas int ir Id
 * @param value nauja reikšmė; 0 paverčiamas į NULL
 * @return true, jei užklausa įvykdyta ir pakito 1 eilutė, false priešingu atveju
 */
  protected boolean updateInt(PreparedStatement update, int value) {
    try {
      if (value == 0)
        update.setNull(1, java.sql.Types.INTEGER);
      else
        update.setInt(1, value);
      update.setInt(2, id);
      int rowsAffected = update.executeUpdate();
      if (rowsAffected == 1) {
        return true;
      } else {
        logger.warn("Strange updateInt updated database rows count: " + rowsAffected);
      }
    } catch (SQLException exception) {
      logger.error("updateInt SQL error: " + exception.getMessage());
    }
    return false;
  }

  protected void finalize () {
    objectMaps.get(this.getClass()).remove(id);
  }
}
