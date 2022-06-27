package Projekt_Wasserball;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Mannschaft 
{
	public static void dropTable(Connection c)
	{
        try 
        {
            Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS team;";
            stmt.executeUpdate(sql);
            stmt.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
	}
	
	static public void createTable(Connection c, String tableName)
	{
		try 
		{
			Statement st = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(teamname varchar(255), liga varchar(255), gruendungsjahr int, primary key(teamname, liga, gruendungsjahr))";
			st.executeUpdate(sql);
			st.close();
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static public void insertTeam(Connection c, String teamname, String liga, int gruendungsjahr)
	{
		try 
		{
			String sql ="INSERT IGNORE INTO team(teamname, liga, gruendungsjahr) values (?,?,?);";
			PreparedStatement st = c.prepareStatement(sql);
			
			st.setString(1, teamname);
			st.setString(2,liga);
			st.setInt(3, gruendungsjahr);
			st.executeUpdate();
			st.close();
			System.out.println(ConsoleColors.YELLOW_ITALIC + teamname + " wurde in das Sytem eingetragen." + ConsoleColors.RESET);
		} 
		catch (SQLException e) 
		{
			System.out.println(ConsoleColors.RED_ITALIC + "Einagaben sind falsch" + ConsoleColors.RESET);
			e.printStackTrace();
		}
		
	}

	static void CSVInsert(Connection c, String path) 
	{
		File file = new File(path);
		String daten = "";
		
		try 
		{
			Scanner sc = new Scanner(file);
			String firstLine = sc.nextLine();
			String[] insert = new String[2];
			
			while (sc.hasNextLine()) 
			{
				daten = sc.nextLine();
				daten.trim();
				insert = daten.split(";");
				
				String sql = "INSERT IGNORE INTO team(teamname, liga, gruendungsjahr) values (?, ?, ?);";
				PreparedStatement st;
				try 
				{	
					st = c.prepareStatement(sql);
					st.setString(1, insert[0]);
					st.setString(2, insert[1]);
					
					int age = Integer.parseInt(insert[2]);
					st.setInt(3, age);
					st.executeUpdate();
					st.close();
					System.out.println(ConsoleColors.YELLOW_ITALIC + "Team " + insert[0] + " wurde aus der CSV-Datei ausgelsen." + ConsoleColors.RESET);
				} 
				catch (SQLException e) 
				{
					System.out.println(ConsoleColors.RED_ITALIC + "Fehler in der CSV-Datei" + ConsoleColors.RESET);
					e.printStackTrace();
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(ConsoleColors.RED_ITALIC + "Pfad ist nicht auffindbar" + ConsoleColors.RESET);
		}
	}
}
