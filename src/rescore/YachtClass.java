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
  private static PreparedStatement selectYachtClass, selectAllYachtClasses, selectAllYachtClassIds, insertYachtClass, updateName, updateNotes, deleteYachtClass;
  private String name;
  private float coefficient;
  private int projectYear, length, width;
  /** vandentalpa */
  private int displacement;
  /** waterlinijos ilgis */
  private int waterlineLength;
  /** burių plotas plaukiant pavėjui */
  private int sailAreaDownwind;
  /** burių plotas plaukiant pries vėją */
  private int sailAreaUpwind;



/**
 * Konstruktorius.
 * Naudojamas tik šioje klasėje. Norint gauti modelio objektą iš kitur, naudoti
 * get() arba getAll().
 */
  private YachtClass(int id, String name, float coefficient, int projectYear, int length, int width, int displacement, int waterlineLength, int sailAreaDownwind, int sailAreaUpwind, String notes) {
    super(id, name, notes);
    this.coefficient = coefficient;
    this.projectYear = projectYear;
    this.length = length;
    this.width = width;
    this.displacement = displacement;
    this.waterlineLength = waterlineLength;
    this.sailAreaDownwind = sailAreaDownwind;
    this.sailAreaUpwind = sailAreaUpwind;
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
    this(id, resultSet.getString(1), resultSet.getFloat(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getInt(7), resultSet.getInt(8), resultSet.getInt(9), resultSet.getString(10));
  }

/**
 * Šį konstruktorių kviečia NamedEntity.getAll().
 *
 * @param resultSet skaitymui paruošta duombazės eilutė su visais objekto
 *                  kūrimui reikalingais laukais, kuriuos grąžina selectAllYachtClasses
 */
  public YachtClass(ResultSet resultSet) throws SQLException {
    this(resultSet.getInt(1), resultSet.getString(2), resultSet.getFloat(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getInt(7), resultSet.getInt(8), resultSet.getInt(9), resultSet.getInt(10), resultSet.getString(11));
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
      insertYachtClass = connection.prepareStatement("INSERT INTO Modeliai (Pavadinimas, Modelis, Koeficientas, ProjektavimoMetai, Ilgis, Plotis, Vandentalpa, VaterlinijosIlgis, BuriųPlotasPlaukiantPavėjui, BuriųPlotasPlaukiantPriešVėją, Pastabos) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      updateName = connection.prepareStatement("UPDATE Modeliai SET Pavadinimas = ? WHERE Id = ?");
      updateNotes = connection.prepareStatement("UPDATE Modeliai SET Pastabos = ? WHERE Id = ?");
      deleteYachtClass = connection.prepareStatement("DELETE FROM Modeliai WHERE Id = ?");
    } catch (SQLException exception) {
    	logger.error("prepareStatements SQL error: " + exception.getMessage());
    }
  }

  public static YachtClass create(String name, float coefficient, int projectYear, int length, int width, int displacement, int waterlineLength, int sailAreaDownwind, int sailAreaUpwind, String notes) {
	  YachtClass yachtClass = null;
	    try {
	      insertYachtClass.setString(1, name);
	      if (coefficient == 0)
	    	  insertYachtClass.setNull(2, java.sql.Types.FLOAT);
	      else
	    	  insertYachtClass.setFloat(2, coefficient) ;
	      if (projectYear == 0)
	        insertYachtClass.setNull(3, java.sql.Types.INTEGER);
	      else
	        insertYachtClass.setInt(3, projectYear);
	      if (length == 0)
	        insertYachtClass.setNull(4, java.sql.Types.INTEGER);
	      else
	        insertYachtClass.setInt(4, length);
	      if (width == 0)
	        insertYachtClass.setNull(5, java.sql.Types.INTEGER);
	      else
	        insertYachtClass.setInt(5, width);
	      if (displacement == 0)
	        insertYachtClass.setNull(6, java.sql.Types.INTEGER);
	      else
	        insertYachtClass.setInt(6, displacement);
	      if (waterlineLength == 0)
	        insertYachtClass.setNull(7, java.sql.Types.INTEGER);
	      else
	        insertYachtClass.setInt(7, waterlineLength);
	      if (sailAreaDownwind == 0)
	        insertYachtClass.setNull(8, java.sql.Types.INTEGER);
	      else
	        insertYachtClass.setInt(8, sailAreaUpwind);
	      if (sailAreaDownwind == 0)
		    insertYachtClass.setNull(9, java.sql.Types.INTEGER);
		  else
		    insertYachtClass.setInt(9, sailAreaUpwind);
	      if (notes == null)
			 insertYachtClass.setNull(10, java.sql.Types.VARCHAR);
	      else
			 insertYachtClass.setString(10, notes);
	      if (insertYachtClass.executeUpdate() == 1) {
	        yachtClass = new YachtClass(getLastInsertId(), name, coefficient, projectYear, length, width, displacement, waterlineLength, sailAreaDownwind, sailAreaUpwind, notes);
	      } else {
	        // TODO
	      }
	    } catch (SQLException exception) {
	      logger.error("create SQL error: " + exception.getMessage());
	    }
	  return yachtClass;
  }
  
  
  public static YachtClass create(String name, float coefficient, int projectYear, int length, int width, int displacement, int waterlineLength, int sailAreaDownwind, int sailAreaUpwind) {
	    return create( name, coefficient, projectYear, length, width, displacement, waterlineLength, sailAreaDownwind, sailAreaUpwind, null);
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
  
  public float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	public int getProjectYear() {
		return projectYear;
	}

	public void setProjectYear(int projectYear) {
		this.projectYear = projectYear;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getDisplacement() {
		return displacement;
	}

	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}

	public int getWaterlineLength() {
		return waterlineLength;
	}

	public void setWaterlineLength(int waterlineLength) {
		this.waterlineLength = waterlineLength;
	}

	public int getSailAreaDownwind() {
		return sailAreaDownwind;
	}

	public void setSailAreaDownwind(int sailAreaDownwind) {
		this.sailAreaDownwind = sailAreaDownwind;
	}

	public int getSailAreaUpwind() {
		return sailAreaUpwind;
	}

	public void setSailAreaUpwind(int sailAreaUpwind) {
		this.sailAreaUpwind = sailAreaUpwind;
	}

	public String getName() {
		return name;
	}
}
