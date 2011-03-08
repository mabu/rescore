/**
 * Teisėjavimo jachtų regatoms įrankiai.
 */
package rescore;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
  private static Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String args[]) {
    if (args.length != 4) {
      System.out.println("Paleidimo parametrai: JDBC_URL prisijungimoVardas slaptažodis log4jKonfigūracijosFailas");
      return;
    }

    PropertyConfigurator.configure(args[3]);

    Connection connection;
    try {
      Class.forName("org.h2.Driver");
      connection = DriverManager.getConnection(args[0], args[1], args[2]);
    } catch (Exception exception) {
      logger.error("Unable to connect to the database: " + exception.getMessage());
      return;
    }

    Yacht.prepareStatements(connection);
    YachtClass.prepareStatements(connection);
    NamedEntity.prepareStatements(connection);
    Regatta.prepareStatements(connection);
    Group.prepareStatements(connection);
    Leg.prepareStatements(connection);

    (new YachtManager(System.in, System.out)).start();
  }
}
