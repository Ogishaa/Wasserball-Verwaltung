package Projekt_Wasserball;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;

public class Training 
{
	public static void dropTable(Connection c)
	{
        try 
        {
            Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS training;";
            stmt.executeUpdate(sql);
            stmt.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
	}
	
	static void createTable(Connection c, String tableName) 
	{
		try 
		{
			Statement stmt = c.createStatement();
			String sql = "create table if not exists " + tableName +
			" (teamname varchar(255),liga varchar(255),gruendungsjahr int,ortname varchar(255), kapazitaet int, datum DATE, primary key(teamname, liga, gruendungsjahr, ortname, kapazitaet, Datum), foreign key (teamname, liga, gruendungsjahr) references Team (teamname, liga, gruendungsjahr), foreign key (ortname, kapazitaet) references ort (ortname, kapazitaet));";
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void Traininganlegen(Connection c, String teamname, String ortname, String termin) 
	{
		
		String select1="select liga, gruendungsjahr from team where teamname='" + teamname + "';";
		String select2="select kapazitaet from ort where ortname='" + ortname + "';";
		String select3="select teamname, ortname, datum from training where ortname='" + ortname + "' and datum='" + termin + "';";
		String insert="insert into training(teamname, liga, gruendungsjahr, ortname, kapazitaet, datum) values (?,?,?,?,?,?);";	
		
		String verTeamname="";
		String verOrtname="";
		Date verDate = Date.valueOf("1900-01-01");
		String liga="";
		int gruendungsjahr=0;
		int kapazitaet=0;
		
		try 
		{
			Statement stmt = c.createStatement();
			
			ResultSet rs= stmt.executeQuery(select1);
			while(rs.next()) 
			{
				liga = rs.getString("liga");
				gruendungsjahr =rs.getInt("gruendungsjahr");
			}
			rs.close();
			
			ResultSet rs1 = stmt.executeQuery(select2);
			while(rs1.next()) 
			{
				kapazitaet=rs1.getInt("kapazitaet");
			}
			rs1.close();
			 
			ResultSet rs2 = stmt.executeQuery(select3);
			while(rs2.next()) 
			{
				verTeamname=rs2.getString("teamname");
				verOrtname=rs2.getString("ortname");
				verDate=rs2.getDate("datum");
			}
			rs2.close();
			stmt.close();

			if(verDate.equals(Date.valueOf(termin)) && verOrtname.equals(ortname)) 
			{  
				System.out.printf(ConsoleColors.CYAN_ITALIC + "" + verTeamname + " hat diesen Ort bereits am " + termin + " gemietet\n" + ConsoleColors.RESET);
			}
			else {
			PreparedStatement pstmt = c.prepareStatement(insert);
			pstmt.setString(1, teamname);
			pstmt.setString(2, liga);
			pstmt.setInt(3, gruendungsjahr);
			pstmt.setString(4, ortname);
			pstmt.setInt(5,kapazitaet);
			Date date1;
			date1 = Date.valueOf(termin);
			pstmt.setDate(6,date1);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println(ConsoleColors.GREEN_ITALIC + ortname + " wurde für den " + termin + " reserviert." + ConsoleColors.RESET);
			}
		}
		catch (SQLException e) 
		{
			System.out.println(ConsoleColors.RED_ITALIC + "Eingaben waren inkorrekt" + ConsoleColors.RESET);
		}
	}

	static void TrainingJsonWrite(Connection c,String path)
	{
		try 
		{
			FileWriter fw = new FileWriter(path);
		 
			JSONObject json = new JSONObject();
			String s = "";
			
			Statement stmt = c.createStatement();
			String sql = "select teamname, ortname, datum from training;";
			ResultSet rs = stmt.executeQuery(sql);
	
			while (rs.next()) 
			{
				String teamname = rs.getString("teamname");
				String ortname = rs.getString("ortname");
				Date datum = rs.getDate("datum");
				
				json.put("Teamname", teamname);
				json.put("Ortname", ortname);
				json.put("Datum", datum);	
				s = s + json;
			
			}
			fw.write(s);
			fw.flush();
			fw.close();
			rs.close();
			stmt.close();
			System.out.println(ConsoleColors.GREEN_ITALIC + "Datei wurde geschrieben" + ConsoleColors.RESET);
		}
		catch (SQLException e) 
		{
			System.out.println(ConsoleColors.RED_ITALIC + "Eingaben waren ungültig" + ConsoleColors.RESET);
		}
		catch (IOException e1)
		{
			System.out.println(ConsoleColors.RED_ITALIC + "Etwas funktioniert nicht" + ConsoleColors.RESET);
		}
		
	}
}
