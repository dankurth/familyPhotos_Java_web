package view;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import dto.Picture;
import persistence.ConnectionPool;
import persistence.PoolManager;

public class GetImageAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    private HttpServletResponse response;
    
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	protected ConnectionPool pool;
	protected Connection con;  

	public GetImageAction() {
		initPool();
	} 

	protected void initPool() {
		try {
			PoolManager pm = PoolManager.getInstance();
			pool = pm.getConnectionPool();
		}
		catch (Exception e) {
			e.printStackTrace();
		}     
	}

	public String execute() {

		//if (isCancelled(request)) return (mapping.findForward("cancel"));

		HttpSession session = request.getSession();
		String size = request.getParameter("size"); // may be "1024x768" or "thumbnails"
		int index = Integer.parseInt(request.getParameter("index"));
		if (size == null) size = "original";

		ArrayList<?> pictures = (ArrayList<?>)session.getAttribute("pictures");
		Picture picture = (Picture)pictures.get(index);
		String md5 = picture.getMD5();

		String query = "SELECT smallblob, LENGTH(smallblob) FROM pictures WHERE md5 = ?";
		String column = "smallblob";
		if ("1024x768".equals(size)) {
			query = "SELECT mediumblob, LENGTH(mediumblob) FROM pictures WHERE md5 = ?";
			column = "mediumblob";
		}

		int len = 0;
		byte[] buf = null;
		do {
			try {
				Connection con = pool.getConnection();
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1, md5);
				ResultSet rs = pst.executeQuery();
				rs.next();
				len = rs.getInt(2);
				buf = rs.getBytes(column);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (buf == null);
		pool.releaseConnection(con);

		try {
			response.setContentLength(len);
			response.setContentType("image/jpeg");
			OutputStream os = response.getOutputStream();
			os.write(buf, 0, len);
			os.flush();
			os.close();
		} catch (Exception ex) {
			Logger lgr = Logger.getLogger(this.getClass().getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}

		return null;
	}

}
