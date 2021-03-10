package core.delete_restore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import core.javaBeans.Category;

public class CreateCouponSystemDB {

		public static void main(String[] args) {
			
			String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=master;user=eldar1;password=pass1";
			String databaseNameCoupons = "CouponSystemDb";
			String sql = "create database " + databaseNameCoupons;
			
			try(
				
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
					
				){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println(sql);
				System.out.println("database created");
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}
			
			connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + databaseNameCoupons + ";user=eldar1;password=pass1";
			sql = "create table companies(id int identity(1,1) constraint pk_companies_id primary key,";
			sql += "name varchar(50),";
			sql += "email varchar(50),";
			sql += "password varchar(50))";
			
			try(
					
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
				
					
				){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println(sql);
				System.out.println("table companies created");				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}

			sql = "create table customers(id int identity(1,1) constraint pk_customers_id primary key,";
			sql += "first_name varchar(50),";
			sql += "last_name varchar(50),";
			sql += "email varchar(50),";
			sql += "password varchar(50))";
			
			try(
					
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
					
					
					){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println(sql);
				System.out.println("table customers created");				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}
			
			sql = "create table categories (id int identity(1,1) constraint pk_categories_id primary key, ";
			sql += "category varchar(25))";
			
			
			try(
					
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
					
					
					){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println(sql);
				System.out.println("table categories created");
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}
			
			sql = "insert into categories (category) values(?)";
			
			try(
					
					Connection con = DriverManager.getConnection(connectionUrl);
					
					PreparedStatement pstmt = con.prepareStatement(sql);
					
					
					){
				
				System.out.println("connected");
				pstmt.setString(1, Category.NULL.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.FOOD.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.ELECTRICITY.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.RESTURANT.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.VACATION.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.SPORT.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.CLOTHES.toString());
				pstmt.executeUpdate();
				pstmt.setString(1, Category.ALL.toString());
				pstmt.executeUpdate();
				System.out.println("table categories created");
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}
			
			
			sql = "create table coupons(id int identity(1,1) constraint pk_coupons_id primary key,";
			sql += "company_id int constraint fk_coupons_companiesId references companies(id),";
			sql += "category_id int constraint fk_coupons_categoriesId references categories(id),";
			sql += "title varchar(100),";
			sql += "description varchar(max),";
			sql += "start_date date,";
			sql += "end_date date,";
			sql += "amount int,";
			sql += "price money,";
			sql += "image varchar(50))";
			
			try(
					
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
					
					
					){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println(sql);
				System.out.println("table coupons created");				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}
			
			sql = "create table customers_vs_coupons(customer_id int,";
			sql += "coupon_id int,";
			sql += "constraint pk_c_vs_c_customer_coupon_id primary key(customer_id,coupon_id),";
			sql += "constraint fk_c_vs_c_customer_id foreign key(customer_id) references customers(id),";
			sql += "constraint fk_c_vs_c_coupon_id foreign key(coupon_id) references coupons(id))";
			
			
			try(
					
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
					
					
					){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println(sql);
				System.out.println("table customers_vs_coupons created");				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(sql);
			}
			
		
			

		}
}
