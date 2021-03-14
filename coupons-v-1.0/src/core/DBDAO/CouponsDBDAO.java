package core.DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import core.DAO.CouponsDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.javaBeans.Category;
import core.javaBeans.Coupon;

public class CouponsDBDAO implements CouponsDAO {

	@Override
	public boolean isCouponExists(int couponID, int companyID) throws CouponSystemException {
		String sql = "select id from coupons where id=? and company_id=?";
		int id = 0;

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, couponID);
			pstmt.setInt(2, companyID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			if (id == 0) {
				return false;
			}
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is coupon exists - by id, company id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}
	
	@Override
	public boolean isCouponTitleExists(String title, int companyID) throws CouponSystemException {
		String sql = "select id from coupons where title=? and company_id=?";
		int id = 0;
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, title);
			pstmt.setInt(2, companyID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			if (id == 0) {
				return false;
			}
			return true;
			
		} catch (SQLException e) {
			throw new CouponSystemException("is coupon exists - by title, company id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}

	@Override
	public void addCoupon(Coupon coupon) throws CouponSystemException {
		String sqlCoupons = "insert into coupons(company_id , category_id , title , description , start_date , end_date , amount , price , image) "
				+ "values (?,?,?,?,?,?,?,?,?)";
		String sqlCategory = "select id from categories where category=?";
		int categoryId;

		Connection con = ConnectionPool.getInstance().getConnection();

		try (

				PreparedStatement pstmtCategories = con.prepareStatement(sqlCategory);

				PreparedStatement pstmtCoupons = con.prepareStatement(sqlCoupons,
						PreparedStatement.RETURN_GENERATED_KEYS);

		) {

			pstmtCategories.setString(1, coupon.getCategory().toString());
			ResultSet rs = pstmtCategories.executeQuery();
			rs.next();
			categoryId = rs.getInt(1);

			pstmtCoupons.setInt(1, coupon.getCompanyID());
			pstmtCoupons.setInt(2, categoryId);
			pstmtCoupons.setString(3, coupon.getTitle());
			pstmtCoupons.setString(4, coupon.getDescription());
			pstmtCoupons.setDate(5, Date.valueOf(coupon.getStartDate()));
			pstmtCoupons.setDate(6, Date.valueOf(coupon.getEndDate()));
			pstmtCoupons.setInt(7, coupon.getAmount());
			pstmtCoupons.setDouble(8, coupon.getPrice());
			pstmtCoupons.setString(9, coupon.getImage());

			pstmtCoupons.executeUpdate();

			rs = pstmtCoupons.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);

			coupon.setId(id);

			System.out.println(">>> " + coupon + " added");

		} catch (SQLException e) {
			throw new CouponSystemException("add coupon failed - " + coupon + " has not been added (SQLException)", e);

		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		String sql = "update coupons "
				+ "set category_id=? , company_id=? , title=? , description=? , start_date=? , end_date=? , amount=? , price=? , image=? "
				+ "where id=?";
		String sqlCategory = "select id from categories where category=?";
		int categoryId;

		Connection con = ConnectionPool.getInstance().getConnection();

		try (

				PreparedStatement pstmtCoupons = con.prepareStatement(sql);

				PreparedStatement pstmtCategories = con.prepareStatement(sqlCategory);

		) {

			pstmtCategories.setString(1, coupon.getCategory().toString());
			ResultSet rs = pstmtCategories.executeQuery();
			rs.next();
			categoryId = rs.getInt("id");

			pstmtCoupons.setInt(1, categoryId);
			pstmtCoupons.setInt(2, coupon.getCompanyID());
			pstmtCoupons.setString(3, coupon.getTitle());
			pstmtCoupons.setString(4, coupon.getDescription());
			pstmtCoupons.setDate(5, Date.valueOf(coupon.getStartDate()));
			pstmtCoupons.setDate(6, Date.valueOf(coupon.getEndDate()));
			pstmtCoupons.setInt(7, coupon.getAmount());
			pstmtCoupons.setDouble(8, coupon.getPrice());
			pstmtCoupons.setString(9, coupon.getImage());

			pstmtCoupons.setInt(10, coupon.getId());

			int row = pstmtCoupons.executeUpdate();
			if(row == 0) {
				throw new CouponSystemException("update coupon failed - " + coupon + " has not been updated ; coupon id '" + coupon.getId() + "' has not found in database");
			}

			System.out.println(">>> " + coupon + " updated in " + row + " row");

		} catch (SQLException e) {
			throw new CouponSystemException("update coupon failed - " + coupon + " has not been updated (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public void deleteCoupon(int couponID) throws CouponSystemException {
		String sql = "delete from coupons where id=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, couponID);

			int row = pstmt.executeUpdate();
			if(row == 0) {
				throw new CouponSystemException("delete coupon by id ; failed - coupon " + couponID + " has not been deleted ; coupon id '" + couponID + "' has not found in database");
			}

			System.out.println(">>> coupon " + couponID + " deleted from " + row + " row(s)");

		} catch (SQLException e) {
			throw new CouponSystemException("delete coupon by id ; failed - coupon " + couponID + " has not been deleted (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public void deleteExpiredCoupons() throws CouponSystemException {
		String sql = "delete from coupons where CONVERT(date, GETDATE()) > end_date";
		
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			int row = pstmt.executeUpdate();
			
			System.out.println(">>> expired coupons deleted from " + row + " row(s)");
			
		} catch (SQLException e) {
			throw new CouponSystemException("delete expired coupons failed - coupons has not been deleted (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public ArrayList<Coupon> getAllCoupons() throws CouponSystemException {
		String sql = "select * from coupons";
		String sqlCategory = "select category from categories where id =? ";
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = ConnectionPool.getInstance().getConnection();

		try (Statement stmt = con.createStatement();

				PreparedStatement pstmt = con.prepareStatement(sqlCategory);

		) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				int categoryId;
				Coupon coupon = new Coupon();

				coupon.setId(rs.getInt("id"));
				coupon.setCompanyID(rs.getInt("company_id"));
				categoryId = rs.getInt("category_id");
				coupon.setTitle(rs.getString("title"));
				coupon.setDescription(rs.getString("description"));
				coupon.setStartDate(rs.getDate("start_date").toLocalDate());
				coupon.setEndDate(rs.getDate("end_date").toLocalDate());
				coupon.setAmount(rs.getInt("amount"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));

				pstmt.setInt(1, categoryId);
				ResultSet categoryNameRs = pstmt.executeQuery();
				categoryNameRs.next();
				coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));

				coupons.add(coupon);
			}

			return coupons;

		} catch (SQLException e) {
			throw new CouponSystemException("get all coupons failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}
	
	@Override
	public ArrayList<Integer> getAllExpiredCouponsId() throws CouponSystemException {
		String sql = "select id from coupons where CONVERT(date, GETDATE()) > end_date";
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (Statement stmt = con.createStatement()) {
			
			ArrayList<Integer> ids = new ArrayList<>();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
			
			return ids;
			
		} catch (SQLException e) {
			throw new CouponSystemException("get all expired coupons failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}

	@Override
	public Coupon getOneCoupon(int couponID) throws CouponSystemException {
		String sql = "select * from coupons where id = " + couponID;

		Connection con = ConnectionPool.getInstance().getConnection();

		try (Statement stmt = con.createStatement();) {

			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				int categoryId;
				Coupon coupon = new Coupon();
				
				coupon.setId(rs.getInt("id"));
				coupon.setCompanyID(rs.getInt("company_id"));
				categoryId = rs.getInt("category_id");
				coupon.setTitle(rs.getString("title"));
				coupon.setDescription(rs.getString("description"));
				coupon.setStartDate(rs.getDate("start_date").toLocalDate());
				coupon.setEndDate(rs.getDate("end_date").toLocalDate());
				coupon.setAmount(rs.getInt("amount"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
				
				sql = "select category from categories where id = " + categoryId;
				ResultSet categoryNameRs = stmt.executeQuery(sql);
				categoryNameRs.next();
				coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));
				
				return coupon;				
			}
			throw new CouponSystemException("get one coupon - by id ; failed - coupon " + couponID + " has not found");

		} catch (SQLException e) {
			throw new CouponSystemException("get one coupon - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) throws CouponSystemException {
		String sql = "insert into customers_vs_coupons(customer_id, coupon_id) values(?,?)";

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, customerID);
			pstmt.setInt(2, couponID);

			int row = pstmt.executeUpdate();
			if(row == 0) {
				throw new CouponSystemException("add coupon purchase failed - coupon purchase (customer '" + customerID +"', coupon'" + couponID +"') has not been added");
			}

			System.out.println(">>> add coupon purchase succeeded in " + row + " row");

		} catch (SQLException e) {
			throw new CouponSystemException("add coupon purchase failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws CouponSystemException {
		String sql = "delete from customers_vs_coupons where customer_id=? and coupon_id=?";

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, customerID);
			pstmt.setInt(2, couponID);

			int row = pstmt.executeUpdate();
			if(row == 0) {
				System.out.println(">>> coupon has no purchases - v");
				return;
			}
			System.out.println(">>> delete coupon purchase succeeded in " + row + " row");

		} catch (SQLException e) {
			throw new CouponSystemException("delete coupon purchase failed - coupon purchase (customer '" + customerID + "', coupon '" + couponID + "') has not been deleted (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public void deleteCouponPurchase(int couponID) throws CouponSystemException {
		String sql = "delete from customers_vs_coupons where coupon_id=" + couponID;

		Connection con = ConnectionPool.getInstance().getConnection();

		try (Statement stmt = con.createStatement();) {

			int row = stmt.executeUpdate(sql);
			if(row == 0) {
				System.out.println(">>> coupon has no purchases - v");
				return;
			}
			System.out.println(">>> delete coupon purchase - by coupon id ; succeeded in " + row + " row");

		} catch (SQLException e) {
			throw new CouponSystemException("delete coupon purchase - by coupon id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override

	public ArrayList<Coupon> getCompanyPurchasedCoupons(int companyID) throws CouponSystemException {
		String sql = "select * from coupons where company_id = " + companyID;
		String sqlCategory = "select category from categories where id =?";
		String sqlPurchasedCoupon = "select * from customers_vs_coupons where coupon_id=?";
		Connection con = ConnectionPool.getInstance().getConnection();

		try (
				
				Statement stmt = con.createStatement();
				
				PreparedStatement pstmt1 = con.prepareStatement(sqlCategory);
				
				PreparedStatement pstmt2 = con.prepareStatement(sqlPurchasedCoupon);
				
			) {
			ArrayList<Coupon> coupons = new ArrayList<>();
			ArrayList<Coupon> purchasedCoupons = new ArrayList<>();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				int categoryId;
				Coupon coupon = new Coupon();

				coupon.setId(rs.getInt("id"));
				coupon.setCompanyID(rs.getInt("company_id"));
				categoryId = rs.getInt("category_id");
				coupon.setTitle(rs.getString("title"));
				coupon.setDescription(rs.getString("description"));
				coupon.setStartDate(rs.getDate("start_date").toLocalDate());
				coupon.setEndDate(rs.getDate("end_date").toLocalDate());
				coupon.setAmount(rs.getInt("amount"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));

				pstmt1.setInt(1,categoryId);
				ResultSet categoryNameRs = pstmt1.executeQuery();
				categoryNameRs.next();
				coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));

				coupons.add(coupon);
			}
			
			
			for (Coupon coupon : coupons) {
				pstmt2.setInt(1, coupon.getId());
				ResultSet purchasedRs = pstmt2.executeQuery();
				while(purchasedRs.next()) {
					purchasedCoupons.add(coupon);
				}
			}
		
			return purchasedCoupons;

		} catch (SQLException e) {
			throw new CouponSystemException("get company purchased coupons - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}
	
	@Override
	public ArrayList<Coupon> getCompanyCoupons(int companyID) throws CouponSystemException{
		String sql = "select * from coupons where company_id = " + companyID;
		String sqlCategory = "select category from categories where id =?";
		Connection con = ConnectionPool.getInstance().getConnection();

		try (
				
				Statement stmt = con.createStatement();
				
				PreparedStatement pstmt1 = con.prepareStatement(sqlCategory);
				
			) {
			ArrayList<Coupon> coupons = new ArrayList<>();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				int categoryId;
				Coupon coupon = new Coupon();

				coupon.setId(rs.getInt("id"));
				coupon.setCompanyID(rs.getInt("company_id"));
				categoryId = rs.getInt("category_id");
				coupon.setTitle(rs.getString("title"));
				coupon.setDescription(rs.getString("description"));
				coupon.setStartDate(rs.getDate("start_date").toLocalDate());
				coupon.setEndDate(rs.getDate("end_date").toLocalDate());
				coupon.setAmount(rs.getInt("amount"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));

				pstmt1.setInt(1,categoryId);
				ResultSet categoryNameRs = pstmt1.executeQuery();
				categoryNameRs.next();
				coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));

				coupons.add(coupon);
			}
			
			return coupons;

		} catch (SQLException e) {
			throw new CouponSystemException("get company coupons - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}
	
	@Override
	public ArrayList<Coupon> getCompanyCouponsByCategory(int companyID, Category category) throws CouponSystemException{
		String sqlCategory = "select id from categories where category =?";
		String sql = "select * from coupons where company_id = ? and category_id = ?";
		int categoryId;
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (
				
				PreparedStatement pstmt1 = con.prepareStatement(sqlCategory);
				
				PreparedStatement pstmt2 = con.prepareStatement(sql);
								
				) {
			pstmt1.setString(1,category.toString());
			ResultSet categoryIdRs = pstmt1.executeQuery();
			if(categoryIdRs.next()) {
				categoryId = categoryIdRs.getInt("id");				
			}else {
				throw new CouponSystemException("get company coupons by category failed ; category '" + category + "' has not found in database");
			}
		
			ArrayList<Coupon> coupons = new ArrayList<>();
			
			pstmt2.setInt(1, companyID);
			pstmt2.setInt(2, categoryId);
			ResultSet rs = pstmt2.executeQuery();
			while (rs.next()) {
				
				Coupon coupon = new Coupon();
				
				coupon.setId(rs.getInt("id"));
				coupon.setCompanyID(rs.getInt("company_id"));
				coupon.setCategory(category);
				coupon.setTitle(rs.getString("title"));
				coupon.setDescription(rs.getString("description"));
				coupon.setStartDate(rs.getDate("start_date").toLocalDate());
				coupon.setEndDate(rs.getDate("end_date").toLocalDate());
				coupon.setAmount(rs.getInt("amount"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
				
				
				coupons.add(coupon);
			}
			
			return coupons;
			
		} catch (SQLException e) {
			throw new CouponSystemException("get company coupons by category failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}
	
	@Override
	public ArrayList<Coupon> getCompanyCouponsByMaxPrice(int companyID, double maxPrice) throws CouponSystemException{
		String sql = "select * from coupons where company_id = " + companyID + " and price <= " + maxPrice;
		String sqlCategory = "select category from categories where id =?";
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (
				
				Statement stmt = con.createStatement();
				
				PreparedStatement pstmt1 = con.prepareStatement(sqlCategory);
				
				) {
			ArrayList<Coupon> coupons = new ArrayList<>();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
				int categoryId;
				Coupon coupon = new Coupon();
				
				coupon.setId(rs.getInt("id"));
				coupon.setCompanyID(rs.getInt("company_id"));
				categoryId = rs.getInt("category_id");
				coupon.setTitle(rs.getString("title"));
				coupon.setDescription(rs.getString("description"));
				coupon.setStartDate(rs.getDate("start_date").toLocalDate());
				coupon.setEndDate(rs.getDate("end_date").toLocalDate());
				coupon.setAmount(rs.getInt("amount"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
				
				pstmt1.setInt(1,categoryId);
				ResultSet categoryNameRs = pstmt1.executeQuery();
				categoryNameRs.next();
				coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));
				
				coupons.add(coupon);
			}
			
			return coupons;
			
		} catch (SQLException e) {
			throw new CouponSystemException("get company coupons by max price failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}
	

	@Override
	public ArrayList<Coupon> getCustomerCoupons(int customerID) throws CouponSystemException {
		String sql = "select coupon_id from customers_vs_coupons where customer_id=" + customerID;
		String sqlCoupon = "select * from coupons where id=?";
		String sqlCategory = "select category from categories where id =?";
		Connection con = ConnectionPool.getInstance().getConnection();

		try (
				Statement stmt = con.createStatement(); 
				
				PreparedStatement pstmt1 = con.prepareStatement(sqlCoupon);
				
				PreparedStatement pstmt2 = con.prepareStatement(sqlCategory)
				
			) {
			
			ArrayList<Coupon> customerCoupons = new ArrayList<>();

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
				pstmt1.setInt(1, rs.getInt("coupon_id"));
				ResultSet couponsRs = pstmt1.executeQuery();

				while(couponsRs.next()) {
					
					int categoryId;
					Coupon coupon = new Coupon();
					
					coupon.setId(couponsRs.getInt(1));
					coupon.setCompanyID(couponsRs.getInt(2));
					categoryId = couponsRs.getInt(3);
					coupon.setTitle(couponsRs.getString(4));
					coupon.setDescription(couponsRs.getString(5));
					coupon.setStartDate(couponsRs.getDate(6).toLocalDate());
					coupon.setEndDate(couponsRs.getDate(7).toLocalDate());
					coupon.setAmount(couponsRs.getInt(8));
					coupon.setPrice(couponsRs.getDouble(9));
					coupon.setImage(couponsRs.getString(10));
					
					pstmt2.setInt(1, categoryId);
					ResultSet categoryNameRs = pstmt2.executeQuery();
					categoryNameRs.next();
					coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));
					
					customerCoupons.add(coupon);
				}

			}

			return customerCoupons;

		} catch (SQLException e) {
			throw new CouponSystemException("get customer coupons - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}
	
	@Override
	public ArrayList<Coupon> getCustomerCouponsByCategory(int customerID, Category category) throws CouponSystemException {
		String sqlCategory = "select id from categories where category =?";
		String sql = "select coupon_id from customers_vs_coupons where customer_id=" + customerID;
		String sqlCoupon = "select * from coupons where id=? and category_id = ?";
		
		int categoryId;
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (
				PreparedStatement pstmt1 = con.prepareStatement(sqlCategory);

				Statement stmt = con.createStatement(); 
				
				PreparedStatement pstmt2 = con.prepareStatement(sqlCoupon);
						
				) {

			pstmt1.setString(1, category.toString());
			ResultSet categoryIdRs = pstmt1.executeQuery();
			if(categoryIdRs.next()) {
				categoryId = categoryIdRs.getInt("id");						
			}else {
				throw new CouponSystemException("get customer coupons by category failed ;category '" + category + "' has not found in database");
			}
			
			ArrayList<Coupon> customerCoupons = new ArrayList<>();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
				pstmt2.setInt(1, rs.getInt("coupon_id"));
				pstmt2.setInt(2, categoryId);
				ResultSet couponsRs = pstmt2.executeQuery();
				
				while(couponsRs.next()) {
					
					
					Coupon coupon = new Coupon();
					
					coupon.setId(couponsRs.getInt(1));
					coupon.setCompanyID(couponsRs.getInt(2));
					coupon.setCategory(category);
					coupon.setTitle(couponsRs.getString(4));
					coupon.setDescription(couponsRs.getString(5));
					coupon.setStartDate(couponsRs.getDate(6).toLocalDate());
					coupon.setEndDate(couponsRs.getDate(7).toLocalDate());
					coupon.setAmount(couponsRs.getInt(8));
					coupon.setPrice(couponsRs.getDouble(9));
					coupon.setImage(couponsRs.getString(10));
					
					
					customerCoupons.add(coupon);
				}
				
			}
			
			return customerCoupons;
			
		} catch (SQLException e) {
			throw new CouponSystemException("get customer coupons by category ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}
	
	@Override
	public ArrayList<Coupon> getCustomerCouponsByMaxPrice(int customerID, double maxPrice) throws CouponSystemException {
		String sql = "select coupon_id from customers_vs_coupons where customer_id=" + customerID;
		String sqlCoupon = "select * from coupons where id=? and price <= ?";
		String sqlCategory = "select category from categories where id =?";
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (
				Statement stmt = con.createStatement(); 
				
				PreparedStatement pstmt1 = con.prepareStatement(sqlCoupon);
				
				PreparedStatement pstmt2 = con.prepareStatement(sqlCategory)
						
				) {
			
			ArrayList<Coupon> customerCoupons = new ArrayList<>();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
				pstmt1.setInt(1, rs.getInt("coupon_id"));
				pstmt1.setDouble(2, maxPrice);
				ResultSet couponsRs = pstmt1.executeQuery();
				
				while(couponsRs.next()) {
					
					int categoryId;
					Coupon coupon = new Coupon();
					
					coupon.setId(couponsRs.getInt(1));
					coupon.setCompanyID(couponsRs.getInt(2));
					categoryId = couponsRs.getInt(3);
					coupon.setTitle(couponsRs.getString(4));
					coupon.setDescription(couponsRs.getString(5));
					coupon.setStartDate(couponsRs.getDate(6).toLocalDate());
					coupon.setEndDate(couponsRs.getDate(7).toLocalDate());
					coupon.setAmount(couponsRs.getInt(8));
					coupon.setPrice(couponsRs.getDouble(9));
					coupon.setImage(couponsRs.getString(10));
					
					pstmt2.setInt(1, categoryId);
					ResultSet categoryNameRs = pstmt2.executeQuery();
					categoryNameRs.next();
					coupon.setCategory(Category.valueOf(categoryNameRs.getString("category")));
					
					customerCoupons.add(coupon);
				}
				
			}
			
			return customerCoupons;
			
		} catch (SQLException e) {
			throw new CouponSystemException("get customer coupons - by max price ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}
	
	/**getCouponIdByTitleAndCompanyId(String title, int companyID) : int - return coupon's <b>id</b> or (-1), if coupon's title has not found for the company's id.
	 *@param title, companyID
	 *@return id
	 *@exception CouponSystemException
	 */
	@Override
	public int getCouponIdByTitleAndCompanyId(String title, int companyID) throws CouponSystemException {
		String sql = "select id from coupons where title=? and company_id = ?";
		int id = -1;
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			pstmt.setString(1, title);
			pstmt.setInt(2, companyID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				id = rs.getInt("id");	
			}
			return id;				
		} catch (SQLException e) {
			throw new CouponSystemException("get company id by title and company id failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}


}
