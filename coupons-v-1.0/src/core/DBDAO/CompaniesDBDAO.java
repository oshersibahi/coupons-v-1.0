package core.DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import core.DAO.CompaniesDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.javaBeans.Company;

public class CompaniesDBDAO implements CompaniesDAO {

	@Override
	public boolean isCompanyExists(String email, String password) throws CouponSystemException {
		String sql = "select id from companies where email=? and password=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			int id = 0;

			if (rs.next()) {
				id = rs.getInt("id");
			}
			if (id == 0) {
				System.out.println(">>> company does not exist - by email, password");
				return false;
			}
			System.out.println(">>> company " + id + " exist - by email, password");
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is company exists - by email, password ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public boolean isCompanyExists(int id, String name) throws CouponSystemException {
		String sql = "select id from companies where id=? and name=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			ResultSet rs = pstmt.executeQuery();
			int currId = 0;

			if (rs.next()) {
				currId = rs.getInt("id");
			}

			if (currId == 0) {
				System.out.println(">>> company does not exist - by id, name");
				return false;
			}
			System.out.println(">>> company " + currId + " exist - by id, name");
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is company exists - by id, name ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public boolean isCompanyExists(int id) throws CouponSystemException {
		String sql = "select id from companies where id=?";
		
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			int currId = 0;
			
			if (rs.next()) {
				currId = rs.getInt("id");
			}
			
			if (currId == 0) {
				System.out.println(">>> company does not exist - by id");
				return false;
			}
			System.out.println(">>> company " + currId + " exist - by id");
			return true;
			
		} catch (SQLException e) {
			throw new CouponSystemException("is company exists - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
		
	}

	@Override
	public boolean isCompanyExists(String name) throws CouponSystemException {
		String sql = "select id from companies where name=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			int id = 0;

			if (rs.next()) {
				id = rs.getInt("id");
			}

			if (id == 0) {
				System.out.println(">>> company does not exist - by name");
				return false;
			}
			System.out.println(">>> company " + id + " exist - by name");
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is company exists - by name ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public boolean isCompanyExistsByEmail(String email) throws CouponSystemException {
		String sql = "select id from companies where email=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("id");
			}

			if (id == 0) {
				System.out.println(">>> company does not exist - by email");
				return false;
			}

			System.out.println(">>> company " + id + " exist - by email");
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is company exists - by email ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public void addCompany(Company company) throws CouponSystemException {
		String sql = "insert into companies(name , email , password) " + "values (? , ? , ?)";

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getEmail());
			pstmt.setString(3, company.getPassword());

			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				company.setId(id);				
			}
			
			System.out.println(">>> " + company + " added");
		} catch (SQLException e) {
			throw new CouponSystemException("add company failed - " + company + " has not been added (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}


	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		String sql = "update companies " + "set email=? , name=?, password=? " + "where id=?";

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, company.getEmail());
			pstmt.setString(2, company.getName());
			pstmt.setString(3, company.getPassword());
			pstmt.setInt(4, company.getId());

			int row = pstmt.executeUpdate();

			if (row == 0) {
				throw new CouponSystemException("update company failed - " + company + " has not been updated ; company id '" + company.getId() + "' has not found in database");
			}
			System.out.println(">>> " + company + " updated in " + row + " row");

		} catch (SQLException e) {
			throw new CouponSystemException("update company failed - " + company + " has not been updated (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public void deleteCompany(int companyID) throws CouponSystemException {
		String sql = "delete from companies where id=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, companyID);

			int row = pstmt.executeUpdate();
			if (row == 0) {
				throw new CouponSystemException("delete company - by id ; failed - company '" + companyID + "' has not been deleted ; company id '" + companyID + "' has not found in database");
			}
			System.out.println(">>> company " + companyID + " deleted from " + row + " row");
		} catch (SQLException e) {
			throw new CouponSystemException("delete company - by id ; failed - company '" + companyID + "' has not been deleted (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public ArrayList<Company> getAllCompanies() throws CouponSystemException {
		String sql = "select * from companies";
		ArrayList<Company> companies = new ArrayList<>();

		Connection con = ConnectionPool.getInstance().getConnection();

		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Company company = new Company();

				company.setId(rs.getInt("id"));
				company.setName(rs.getString("name"));
				company.setEmail(rs.getString("email"));
				company.setPassword(rs.getString("password"));

				companies.add(company);
			}

			return companies;

		} catch (SQLException e) {
			throw new CouponSystemException("get all companies failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public Company getOneCompany(int companyID) throws CouponSystemException {
		String sql = "select * from companies where id=?";

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, companyID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Company company = new Company();
				company.setId(rs.getInt("id"));
				company.setName(rs.getString("name"));
				company.setEmail(rs.getString("email"));
				company.setPassword(rs.getString("password"));
				
				return company;
			}
			throw new CouponSystemException("get one company - by id ; failed - company '" + companyID + "' has not found");
		} catch (SQLException e) {
			throw new CouponSystemException("get one company - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public Company getOneCompany(String email, String password) throws CouponSystemException {
		String sql = "select * from companies where email=? and password=?";

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Company company = new Company();
				company.setId(rs.getInt("id"));
				company.setName(rs.getString("name"));
				company.setEmail(rs.getString("email"));
				company.setPassword(rs.getString("password"));
				
				return company;				
			}
			throw new CouponSystemException("get one company - by email, password ; failed - company (" + email + ", " + password + ") has not found");
		} catch (SQLException e) {
			throw new CouponSystemException("get one company - by email, password ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	/**getCompanyIdByEmail(String email) : int - return company's <b>id</b> or (-1), if company's email has not found.
	 *@param email
	 *@return id
	 *@exception CouponSystemException
	 */
	@Override
	public int getCompanyIdByEmail(String email) throws CouponSystemException {
		String sql = "select id from companies where email=?";
		Connection con = ConnectionPool.getInstance().getConnection();
		int id = -1;
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				id = rs.getInt("id");	
			}
			return id;							
		} catch (SQLException e) {
			throw new CouponSystemException("get company id - by email ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

}
