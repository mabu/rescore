/**
 * Jachtų duomenų tvarkymas per tekstinę sąsają.
 * Reikėjo DBVS laboratoriniui, gal kažkiek gali būti naudinga testavimui.
 */
package rescore;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;
import rescore.views.YachtListView;

public class YachtManager {
  private static Logger logger = Logger.getLogger(YachtManager.class.getName());
  private Scanner scanner;
  private PrintStream printStream;
  private final static int LIST_ITEMS_AT_ONCE = 20; // kiek daugiausiai
                          // pasirinkimų vienu metu spausdins choose...()

  public YachtManager(InputStream inputStream, PrintStream printStream) {
    this.scanner = new Scanner(inputStream);
    this.printStream = printStream;
  }

  public void start() {
	  
    printStream.println("Jachtų duomenų tvarkymas");
    printStream.println("Meniu pasirinkimus atlikite įvesdami skaičių.");
    int command = -1;
    while (command != 3) {
      printStream.println("1. Įtraukti naują jachtą");
      printStream.println("2. Pasirinkti jachtą iš sąrašo");
      printStream.println("3. Baigti");
      command = scanInt();
      switch (command) {
        case 1:
          scanner.nextLine();
          printStream.println("Burės numeris:");
          String sailNumber = scanner.nextLine();
          if (sailNumber.trim().isEmpty()) {
            printStream.println("Neįvestas burės numeris – atšaukiama");
            break;
          }
          printStream.println("Pavadinimas:");
          String name = scanner.nextLine();
          if (name.isEmpty())
            name = null;
          printStream.println("Pasirinkite jachtos modelį.");
          YachtClass yachtClass = chooseYachtClass(YachtClass.getAll());
          if (yachtClass == null) {
            printStream.println("Nepasirinktas modelis – atšaukiama");
            break;
          }
          printStream.println("Pagaminimo metai (jeigu nežinomi, įveskite 0):");
          int year = scanInt();
          scanner.nextLine();
          printStream.println("Kapitonas:");
          String captain = scanner.nextLine();
          if (captain.trim().isEmpty())
            captain = null;
          printStream.println("Savininkas:");
          String owner = scanner.nextLine();
          if (owner.trim().isEmpty())
            owner = null;
          printStream.println("Rėmėjai:");
          String sponsors = scanner.nextLine();
          if (sponsors.trim().isEmpty())
            sponsors = null;
          if (Yacht.create(sailNumber, yachtClass, name, year, captain, owner, sponsors) != null)
            printStream.println("Jachta įtraukta sėkmingai");
          else
            printStream.println("Jachta neįtraukta");
          break;
        case 2:
          Yacht yacht = chooseYacht(Yacht.getAll());
          if (yacht != null)
            processYacht(yacht);
          break;
      }
    }
  }
  
/**
 * Skaito scanner, kol randa int reikšmę.
 *
 * @return rastas skaičius
 */
  private int scanInt() {
    while (scanner.hasNext()) {
      if (scanner.hasNextInt()) {
        return scanner.nextInt();
      }
      else
        scanner.next();
    }
    logger.error("Reached end of stdin in scanInt");
    return -1;
  }

  private Yacht chooseYacht(List<Yacht> list) {
    int traversed = 0, id = 0, size = list.size();
    if (list.isEmpty()) {
      printStream.println("Jachtų sąrašas tuščias");
      return null;
    }
    for (Yacht yacht : list) {
      printStream.println(yacht.getId() + ". " + yacht.getSailNumber() + (yacht.getName() == null ? "" : " (" + yacht.getName() + ")"));
      traversed++;
      if (traversed % LIST_ITEMS_AT_ONCE == 0 && traversed != list.size()) {
        printStream.println("Rodomi pirmi " + traversed + " iš " + size + " pasirinkimų. Įveskite prie pasirinktos jachtos esantį numerį, arba 0, jei norite matyti sąrašo tęsinį. Norėdami nieko nepasirinkti, įveskite neigiamą skaičių.");
        id = scanInt();
        if (id != 0)
          break;
      }
    }
    if (id == 0) {
      printStream.println("Įveskite prie pasirinktos jachtos esantį numerį. Norėdami nieko nepasirinkti, įveskite neteigiamą skaičių.");
      id = scanInt();
    }
    if (id > 0)
      return Yacht.get(id);
    else
      return null;
    }

  private YachtClass chooseYachtClass(List<YachtClass> list) {
    int traversed = 0, id = 0, size = list.size();
    if (list.isEmpty())
      printStream.println("Sąrašas tuščias");
    for (YachtClass yachtclass : list) {
      printStream.println(yachtclass.getId() + ". " + yachtclass.getName());
      traversed++;
      if (traversed % LIST_ITEMS_AT_ONCE == 0 && traversed != list.size()) {
        printStream.println("Rodomi pirmi " + traversed + " iš " + size + " pasirinkimų. Įveskite pasirinkimo ID, arba 0, jei norite matyti sąrašo tęsinį. Norėdami nieko nepasirinkti, įveskite neigiamą skaičių.");
        id = scanInt();
        if (id != 0)
          break;
      }
    }
    if (id == 0) {
      printStream.println("Įveskite pasirinkimo ID. Norėdami nieko nepasirinkti, įveskite neteigiamą skaičių.");
      id = scanInt();
    }
    if (id > 0)
      return YachtClass.get(id);
    else
      return null;
    }

  private void processYacht(Yacht yacht) {
    int command = 0;
    final int RETURN_COMMAND_NUMBER = 9;
    while (command != RETURN_COMMAND_NUMBER) {
      printStream.println("Burės numeris: " + yacht.getSailNumber());
      if (yacht.getName() == null)
        printStream.println("Pavadinimo nėra");
      else
        printStream.println("Pavadinimas: " + yacht.getName());
      printStream.println("Modelis: " + yacht.getYachtClass().getName());
      if (yacht.getYear() <= 0)
        printStream.println("Pagaminimo metai nežinomi");
      else
        printStream.println("Pagaminimo metai: " + yacht.getYear());
      if (yacht.getCaptain() == null)
        printStream.println("Kapitono nėra");
      else
        printStream.println("Kapitonas: " + yacht.getCaptain());
      if (yacht.getOwner() == null)
        printStream.println("Savininkas nežinomas");
      else
        printStream.println("Savininkas: " + yacht.getOwner());
      if (yacht.getSponsors() == null)
        printStream.println("Rėmėjų nėra");
      else
        printStream.println("Rėmėjai: " + yacht.getSponsors());
      printStream.println();
      printStream.println("Pasirinkite veiksmą:");
      printStream.println("1. Pakeisti jahctos burės numerį");
      printStream.println("2. Pakeisti jachtos pavadinimą");
      printStream.println("3. Pakeisti jachtos modelį");
      printStream.println("4. Pakeisti jachtos pagaminimo metus");
      printStream.println("5. Pakeisti jachtos kapitoną");
      printStream.println("6. Pakeisti jachtos savininką");
      printStream.println("7. Pakeisti jachtos rėmėjus");
      printStream.println("8. Pašalinti jachtą");
      printStream.println("9. Grįžti");
      command = scanInt();
      switch (command) {
        case 1:
          scanner.nextLine();
          printStream.println("Įveskite naują jachtos burės numerį (buvo " + yacht.getSailNumber() + "): ");
          String sailNumber = scanner.nextLine();
          if (sailNumber.trim().isEmpty() || !yacht.setSailNumber(sailNumber)) {
            printStream.println("Burės numeris nepakeistas");
          }
          break;
        case 2:
          scanner.nextLine();
          printStream.println("Įveskite naują jachtos pavadinimą" + (yacht.getName() == null ? "" : " (buvo " + yacht.getName() + ")") + ":");
          String name = scanner.nextLine();
          if (name.isEmpty())
            name = null;
          if (!yacht.setName(name)) {
            printStream.println("Jachtos pavadinimas nepakeistas");
          }
          break;
        case 3:
          printStream.println("Ankstesnis jachtos modelis buvo " + yacht.getYachtClass().getName() + ". Pasirinkite naują modelį.");
          YachtClass yachtClass = chooseYachtClass(YachtClass.getAll());
          if (yachtClass == null || !yacht.setYachtClass(yachtClass))
            printStream.println("Jachtos modelis nepakeistas");
          break;
        case 4:
          printStream.println("Įveskite naujus jachtos pagaminimo metus arba 0, jei metai nežinomi" + (yacht.getYear() <= 0 ? "" : " (buvo " + yacht.getYear() + ")") + ":");
          int year = scanInt();
          if (year < 0 || !yacht.setYear(year))
            printStream.println("Jachtos pagaminimo metai nepakeisti");
          break;
        case 5:
          scanner.nextLine();
          printStream.println("Įveskite naują jachtos kapitoną" + (yacht.getCaptain() == null ? "" : " (buvo " + yacht.getCaptain() + ")") + ":");
          String captain = scanner.nextLine();
          if (captain.isEmpty())
            captain = null;
          if (!yacht.setCaptain(captain)) {
            printStream.println("Jachtos kapitonas nepakeistas");
          }
          break;
        case 6:
          scanner.nextLine();
          printStream.println("Įveskite naują jachtos savininką" + (yacht.getOwner() == null ? "" : " (buvo " + yacht.getOwner() + ")") + ":");
          String owner = scanner.nextLine();
          if (owner.isEmpty())
            owner = null;
          if (!yacht.setOwner(owner)) {
            printStream.println("Jachtos savininkas nepakeistas");
          }
          break;
        case 7:
          scanner.nextLine();
          printStream.println("Įveskite naujus jachtos rėmėjus" + (yacht.getSponsors() == null ? "" : " (buvo " + yacht.getSponsors() + ")") + ":");
          String sponsors = scanner.nextLine();
          if (sponsors.isEmpty())
            sponsors = null;
          if (!yacht.setSponsors(sponsors)) {
            printStream.println("Jachtos rėmėjai nepakeisti");
          }
          break;
        case 8:
          scanner.nextLine();
          printStream.println("Dėmesio! Jei tikrai norite pašalinti jachtą, įveskite „Taip“");
          if (scanner.nextLine().equals("Taip") && yacht.remove()) {
            printStream.println("Jachta pašalinta sėkmingai");
            command = RETURN_COMMAND_NUMBER;
          }
          else
            printStream.println("Jachta nepašalinta");
          break;
      }
    }
  }
}
