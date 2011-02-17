/**
 * Jachtų modeliai.
 * Objektai saugomi duombazės lentelėje Modeliai.
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

public class YachtClass {
  private static Logger logger = Logger.getLogger(YachtClass.class.getName());
  private static PreparedStatement selectYachtClass, selectAllYachtClasss, selectAllYachtClassIds;
  private static TreeMap<Integer, WeakReference<YachtClass> > yachtClasss = new TreeMap<Integer, WeakReference<YachtClass> >();
  private static int yachtClasssCount = -1; // kiek modelių yra duomenų bazėje
  private static double YACHTCLASS_LIST_RATIO = 0.5;
  private int id;
  private String name;

/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti modelio objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private YachtClass(int id, String name) {
    this.id = id;
    this.name = name;
  }

/**
 * Grąžina objektą modelio su nurodytu id.
 *
 * @param id modelio id
 * @return modelis su nurodytu id
 */
  public static YachtClass get(int id) {
    WeakReference<YachtClass> weakReference = yachtClasss.get(id);
    YachtClass yachtClass = null;
    if (weakReference != null)
      yachtClass = weakReference.get();
    if (yachtClass == null) {
      try {
        selectYachtClass.setInt(1, id);
        ResultSet resultSet = selectYachtClass.executeQuery();
        if (resultSet.next()) {
          yachtClass = new YachtClass(id, resultSet.getString(1));
          yachtClasss.put(id, new WeakReference<YachtClass>(yachtClass));
        }
      } catch (SQLException exception) {
        logger.error("get SQL error: " + exception.getMessage());
      }
    }
    return yachtClass;
  }

/**
 * Grąžina visų modelių sąrašą jų id didėjimo tvarka.
 *
 * @return visų modelių sąrašas id didėjimo tvarka
 */
  public static List<YachtClass> getAll() {
    Vector<YachtClass> yachtClassList = new Vector<YachtClass>();
    YachtClass yachtClass;
    WeakReference<YachtClass> weakReference;
    try {
      if (yachtClasssCount == -1 || yachtClasss.size() / yachtClasssCount > YACHTCLASS_LIST_RATIO) {
        ResultSet resultSet = selectAllYachtClasss.executeQuery();
        for (yachtClasssCount = 0; resultSet.next(); yachtClasssCount++) {
          yachtClass = null;
          weakReference = yachtClasss.get(resultSet.getInt(1));
          if (weakReference != null)
            yachtClass = weakReference.get();
          if (yachtClass == null) {
            yachtClass = new YachtClass(resultSet.getInt(1), resultSet.getString(2));
            yachtClasss.put(resultSet.getInt(1), new WeakReference<YachtClass>(yachtClass));
          }
          yachtClassList.add(yachtClass);
        }
      } else {
        ResultSet resultSet = selectAllYachtClassIds.executeQuery();
        for (yachtClasssCount = 0; resultSet.next(); yachtClasssCount++) {
          yachtClassList.add(get(resultSet.getInt(1)));
        }
      }
    } catch (SQLException exception) {
      logger.error("getAll SQL error: " + exception.getMessage());
      yachtClassList = null;
    }
    return yachtClassList;
  }

/**
 * Paruošia statinius PreparedStatement objektus.
 *
 * @param connection jungtis su duombaze
 */
  static void prepareStatements(Connection connection) {
    try {
      selectYachtClass = connection.prepareStatement("SELECT Pavadinimas FROM Modeliai WHERE Id = ?");
      selectAllYachtClasss = connection.prepareStatement("SELECT Id, Pavadinimas FROM Modeliai ORDER BY Id");
      selectAllYachtClassIds = connection.prepareStatement("SELECT Id FROM Modeliai ORDER BY Id");
    } catch (SQLException exception) {
      logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
