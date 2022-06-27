package Projekt_Wasserball;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Ort 
{
	public static void dropTable(Connection c)
	{
        try 
        {
            Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS ort;";
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
			Statement st= c.createStatement();
			String sql = "create table if not exists " + tableName + "(ortname varchar(255), kapazitaet int, primary key(ortname, kapazitaet))";
			st.executeUpdate(sql);
			st.close();
			System.out.println();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	static public void insertOrt(Connection c, String ortname,int kapazitaet)
	{
		try 
		{
			String sql ="insert ignore into ort(ortname, kapazitaet) values (?,?);";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, ortname);
			st.setInt(2, kapazitaet);
			st.executeUpdate();
			st.close();
			System.out.println(ConsoleColors.YELLOW_ITALIC + ortname + " wurde in das Sytem aufgenommen." + ConsoleColors.RESET);
		} 
		catch (SQLException e) 
		{
			System.out.println(ConsoleColors.RED_ITALIC + "Eingaben waren ungültig" + ConsoleColors.RESET);
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
			String[] insert = new String[1];
			
			while (sc.hasNextLine()) 
			{
				daten = sc.nextLine();
				daten.trim();
				insert = daten.split(";");
				String sel = "select * from ort where ortname='" + insert[0] + "' and kapazitaet =" + insert[1] + ";";
				
				String sql = "insert ignore into ort(ortname,kapazitaet) values (?, ?);";
				PreparedStatement st;
				try 
				{	
					st = c.prepareStatement(sql);
					st.setString(1, insert[0]);
					int kap = Integer.parseInt(insert[1]);
					st.setInt(2, kap);
					st.executeUpdate();
					st.close();
					System.out.println(ConsoleColors.YELLOW_ITALIC + "" + insert[0] + " wurde aus der CSV-Datei ausgelsen." + ConsoleColors.RESET);
					
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
			System.out.println(ConsoleColors.RED_ITALIC + "Der angegebene Pfad ist nicht auffindbar" + ConsoleColors.RESET);
		}
	}
}
