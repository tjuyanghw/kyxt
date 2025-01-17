package paper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.ResJson;
import db.Db;

/**
 * Servlet implementation class PaperQueryT
 */
@WebServlet(description = "教师查询论文的处理", urlPatterns = { "/PaperQueryT" })
public class PaperQueryT extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PaperQueryT() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodname = request.getParameter("methodname");
		if (methodname != null) {
			switch (methodname) {
			case "deletesingle":
				deleteSingle(request, response);
				break;
			default:
				queryList(request, response);
			}

		} else {
			queryList(request, response);
		}
	}

	private void queryList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String json = "";
		int numbers = 0;
		Db db = new Db();
		int tid = (int) request.getSession().getAttribute("id");
		String title = request.getParameter("title");
		String pubtime = request.getParameter("pubtime");
		String firstauthor = request.getParameter("firstauthor");
		String subtypeid = request.getParameter("subtypeid");
		String firstsubid = request.getParameter("firstsubid");
		String pubtypeid = request.getParameter("pubtypeid");
		String prosourceid = request.getParameter("prosourceid");
		String pubarea = request.getParameter("pubarea");
		String journalid = request.getParameter("journalid");
		String layout = request.getParameter("layout");
		String auditflag = request.getParameter("auditflag");

		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		int offset = limit * (page - 1);
		String sql = ""; // 查询总数据的最终sql拼接语句
		String sqlf = "select * from TeacherQueryPaper where teacherid = " + tid; // sql头部
		// 分页查询
		String sqle = " limit " + offset + "," + limit + ""; // sql尾部
		String sql1 = "select count(*) numbers from TeacherQueryPaper where teacherid = " + tid; // 得到count值，总结果值
		String str = "";
		if (title == "" || title == null) {

		} else {
			str = str + " and title like '%" + title + "%'";
		}

		if (firstauthor == "" || firstauthor == null) {

		} else {
			str = str + " and firstauthor like '%" + firstauthor + "%'";
		}

		if (subtypeid == "" || subtypeid == null) {

		} else {
			str = str + " and subtypeid like '%" + subtypeid + "%'";
		}

		if (firstsubid == "" || firstsubid == null) {

		} else {
			str = str + " and firstsubid like '%" + firstsubid + "%'";
		}
		if (pubtypeid == "" || pubtypeid == null) {

		} else {
			str = str + " and pubtypeid like '%" + pubtypeid + "%'";
		}
		if (prosourceid == "" || prosourceid == null) {

		} else {
			str = str + " and prosourceid like '%" + prosourceid + "%'";
		}
		if (pubarea == "" || pubarea == null) {

		} else {
			str = str + " and pubarea like '%" + pubarea + "%'";
		}
		if (pubarea == "" || pubarea == null) {

		} else {
			str = str + " and pubarea like '%" + pubarea + "%'";
		}
		if (journalid == "" || journalid == null) {

		} else {
			str = str + " and journalid like '%" + journalid + "%'";
		}
		if (layout == "" || layout == null) {

		} else {
			str = str + " and layout like '%" + layout + "%'";
		}
		if (auditflag == "" || auditflag == null) {

		} else {
			str = str + " and auditflag like '%" + auditflag + "%'";
		}

		if (pubtime == "" || pubtime == null) {

		} else {
			String[] aa;
			aa = pubtime.split(" - ");
			// System.out.println(aa[0] + aa[1]);
			str = str + " and (pubtime > '" + aa[0] + "' and pubtime < '" + aa[1] + " ')";
		}

		sql1 = sql1 + str;
		sql = sqlf + str + sqle;
		// System.out.println(sql);
		try {
			ResultSet rs;
			PreparedStatement ps;
			ps = db.getPs(sql1);
			rs = ps.executeQuery();
			if (rs.next()) {
				numbers = rs.getInt("numbers");
			} else {
				out.print("<script>alert('未知错误');window.history.go(-1);</script>");
				return;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<QueryPaper> paperList = new ArrayList<QueryPaper>();
		ResJson resjson = new ResJson();
		resjson.setCode(0);
		resjson.setCount(numbers);
		resjson.setMsg("");

		try {
			ResultSet rs;
			PreparedStatement ps;
			ps = db.getPs(sql);
			ps = db.getPs(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				QueryPaper paper = new QueryPaper();
				paper.setPaperid(rs.getInt(1));
				paper.setTitle(rs.getString(2));
				paper.setFirstauthor(rs.getString(3));
				paper.setPubtime(rs.getDate(4));
				paper.setPubarea(rs.getString(5));
				paper.setIstrans(rs.getString(6));

				paper.setAuditflag(rs.getString(7));
				paper.setSourcename(rs.getString(8));
				paper.setPubpartname(rs.getString(9));
				paper.setSubpartname(rs.getString(10));
				paper.setJournalname(rs.getString(11));
				paper.setFirstsubname(rs.getString(12));
				paper.setTeacherid(rs.getInt(13));
				paper.setProsourceid(rs.getInt(14));
				paper.setFirstsubid(rs.getInt(15));
				paper.setSubtypeid(rs.getInt(16));
				paper.setJournalid(rs.getInt(17));
				paper.setPubtypeid(rs.getInt(18));
				paper.setMajorname(rs.getString(19));
				paper.setMentorflag(rs.getString(20));
				paper.setLayout(rs.getString(21));
				paper.setFileurl(rs.getString(22));
				paperList.add(paper);
			}
			resjson.setData(paperList);
			json = gson.toJson(resjson);
			// System.out.println(json);
			rs.close();
			ps.close();
			db.getConnect().close();
			out.print(json);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	private void deleteSingle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Db db = new Db();
		String sql = "delete from paper where id = ?";
		int paperid = Integer.parseInt(request.getParameter("paperid"));
		int row = 0;
		PreparedStatement ps = db.getPs(sql);
		try {
			ps.setInt(1, paperid);
			row = ps.executeUpdate();
			if (row != 0) {
				out.print("1");
			} else {
				out.print("0"); // 直接返回只能返回数字，否则要构建json数据
			}
			ps.close();
			db.getConnect().close();
			out.flush();
			out.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
