package Projekt_Wasserball;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Verwaltung 
{
		static String url = "jdbc:mysql://localhost:3306/waterpolo";
		static String user = "root"; 
		static String pass = "";  
		
		static boolean main = true;
		static boolean team = true;
		static boolean ort = true;
		static boolean training = true;
		static boolean ausgabe = true;
		static String auswahlMain = "0";
		static String auswahl = "0";
		static String pathJson;
		static String pathCSV;
		static String termin;
		static String teamname = "";
		static String liga = "";
		static int gruendungsjahr = 0;
		static String ortname = "";
		static int kabinen = 0;
	    
		static Connection getConnection() throws ClassNotFoundException 
		{
			try 
			{
				return DriverManager.getConnection(url, user, pass);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return null;
		}

		public static void main(String[] args) throws ClassNotFoundException 
		{
			Connection c = getConnection();
			Scanner sc = new Scanner(System.in);

			while(main) 
			{
			System.out.println(ConsoleColors.BLUE_ITALIC + "[1]Team eintragen");
			System.out.println("[2]Ort eintragen");
			System.out.println("[3]Ort für ein Training buchen");
			System.out.println("[4]Trainingausgabe");
			System.out.println("[5]Beenden" + ConsoleColors.RESET);
			auswahlMain = sc.nextLine().trim();
				if(auswahlMain.equals("1")) 
				{
				  while(team) 
				  {
					  System.out.println(ConsoleColors.GREEN_ITALIC + "[1]Ein Team manuell eingeben");
					  System.out.println(ConsoleColors.GREEN_ITALIC + "[2]Falls mehrere Teams->CSV-Datei einlesen" + ConsoleColors.RESET);
					  auswahl = sc.nextLine().trim();
					  
					  if(auswahl.equals("1")) 
					  {
						  System.out.println(ConsoleColors.PURPLE_ITALIC + "Bitte den Namen der Mannschaft eingeben");
						  teamname=sc.nextLine().trim();
						  System.out.println("Bitte aktuelle Liga eingeben");
						  liga=sc.nextLine().trim();
						  System.out.println("Bitte das Gründungsjahr eingeben");
						  gruendungsjahr = sc.nextInt();
						  System.out.println(teamname);
						  System.out.println(liga);
						  System.out.println(gruendungsjahr);
						  Mannschaft.createTable(c, "team");
						  Mannschaft.insertTeam(c, teamname, liga, gruendungsjahr);
						  team = false;
					  }
					  else if(auswahl.equals("2")) 
					  {
						  System.out.println("Den Pfad bitte eingeben");
						  pathCSV = sc.nextLine();
						  Mannschaft.CSVInsert(c, pathCSV);
						  team = false;
					  }
					  if(!auswahl.equals("1") && !auswahl.equals("2"))
					  {
						  System.out.println(ConsoleColors.RED_ITALIC + "Ungültige Einagbe, nur nie Nummern in der Klammer verwenden" + ConsoleColors.RESET);
					  }
				  }
				  team = true;
				}
				if(auswahlMain.equals("2")) 
				{
					while(ort) 
					{
						System.out.println(ConsoleColors.GREEN_ITALIC + "[1]Ort eingeben");
						System.out.println("[2]Falls mehrere Orte->CSV-Datei einlesen" + ConsoleColors.RESET);
						auswahl=sc.nextLine();
						if(auswahl.equals("1")) 
						{
							System.out.println(ConsoleColors.PURPLE_ITALIC + "Bitte den Namen vom Ort eingeben");
							ortname = sc.nextLine();
							System.out.println("Bitte die Anzahl der Kabinen angeben");
							kabinen = sc.nextInt();
							System.out.println(ortname);
							System.out.println(kabinen);
							Ort.createTable(c, "ort");
							Ort.insertOrt(c, ortname, kabinen);
							ort = false;
						}
						else if(auswahl.equals("2")) 
						{
							System.out.println("Den Pfad bitte eingeben\n");
							pathCSV=sc.nextLine();
							Ort.CSVInsert(c, pathCSV);
							ort = false;
						}
						if(!auswahl.equals("1") && !auswahl.equals("2"))
						{
							System.out.println("Ungültige Einagbe, nur nie Nummern in der Klammer verwenden");
						}
					}
				}
				if(auswahlMain.equals("3")) 
				{
					System.out.println(ConsoleColors.PURPLE_ITALIC + "Den Teamnamen bitte eingeben");
					teamname = sc.nextLine();
					System.out.println("Ort auswählen");
					ortname = sc.nextLine();
					System.out.println("Bitte den Termin eingeben");
					termin = sc.nextLine();
					Training.createTable(c, "training");
					Training.Traininganlegen(c, teamname, ortname, termin);
				}
				if(auswahlMain.equals("4")) 
				{
					System.out.println(ConsoleColors.PURPLE_ITALIC + "Speicherort angeben" + ConsoleColors.RESET);
					pathJson = sc.nextLine();
					Training.TrainingJsonWrite(c, pathJson);
				}
				if(auswahlMain.equals("5")) 
				{
					System.out.println(ConsoleColors.GREEN_ITALIC + "[1]Wollen Sie alles löschen?" + ConsoleColors.RESET);
					System.out.println(ConsoleColors.GREEN_ITALIC + "[2]Nur beenden" + ConsoleColors.RESET);
					auswahl = sc.nextLine().trim();
					
					if(auswahl.equals("1")) {
						System.out.println(ConsoleColors.YELLOW_ITALIC + "Alle Tabellen werden gelöscht!");
						Training.dropTable(c);
						Ort.dropTable(c);
						Mannschaft.dropTable(c);
					}
					if(auswahl.equals("2")) {
					System.out.println("Programm wird beendet" + ConsoleColors.RESET);
					main = false;
					}
				}
				if(!auswahlMain.equals("1") && !auswahlMain.equals("2") && !auswahlMain.equals("3") && !auswahlMain.equals("4") && !auswahlMain.equals("5"))
				{
					System.out.println(ConsoleColors.RED_ITALIC + "Keine gültige Eingabe, bitte die Nummern in den Klammern verwnden\n" + ConsoleColors.RESET);
				}
			}
			sc.close();
	}
}
