package beans;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;

/**
 * Servlet implementation class PaperUpLoad
 * 
 * @MultipartConfig 上传的注解
 */
@WebServlet(description = "文件上传处理", urlPatterns = { "/PaperUpLoad" })
@MultipartConfig
public class PaperUpLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PaperUpLoad() {
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
		response.setContentType("application/json");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String header, fileName = null, fileNameEnd = null;
		String[] aa;
		int rd;
		rd = (int) ((Math.random() * 9 + 1) * 10000);

		// request.getSession().getServletContext().getRealPath("/WEB-INF/uploadFile")
		String savePath = "G:\\MyJava\\yttweb\\WebRoot\\WEB-INF\\uploadFile";
		// String savePath = "F:\\uploadFile";
		// 获取上传的文件集合
		Collection<Part> parts = request.getParts();
		// 上传单个文件
		if (parts.size() == 1) {
			// Servlet3.0将multipart/form-data的POST请求封装成Part，通过Part对上传的文件进行操作。
			// Part part = parts[0];//从上传的文件集合中获取Part对象
			Part part = request.getPart("file");// 通过表单file控件(<input type="file"
												// name="file">)的名字直接获取Part对象
			// Servlet3没有提供直接获取文件名的方法,需要从请求头中解析出来
			// 获取请求头，请求头的格式：form-data; name="file"; filename="snmp4j--api.zip"
			header = part.getHeader("content-disposition");
			// 获取文件名
			fileName = getFileName(header);
			aa = fileName.split("\\.");
			fileNameEnd = aa[0] + rd + '.' + aa[1]; // 重写文件名
			// 把文件写到指定路径
			// System.out.println();
			part.write(savePath + File.separator + fileNameEnd);
			// System.out.println(savePath + File.separator + fileName);
		} else {
			// 一次性上传多个文件
			for (Part part : parts) {// 循环处理上传的文件
				// 获取请求头，请求头的格式：form-data; name="file";
				// filename="snmp4j--api.zip"
				header = part.getHeader("content-disposition");
				// 获取文件名
				fileName = getFileName(header);
				// 把文件写到指定路径
				part.write(savePath + File.separator + fileName);

			}
		}
		FileRes res = new FileRes();
		String json = "";
		Gson gson = new Gson();

		res.setCode(0);
		res.setMsg("");
		res.setSrc(fileNameEnd);
		json = gson.toJson(res);
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * 根据请求头解析出文件名 请求头的格式：火狐和google浏览器下：form-data; name="file";
	 * filename="snmp4j--api.zip" IE浏览器下：form-data; name="file";
	 * filename="E:\snmp4j--api.zip"
	 * 
	 * @param header
	 *            请求头
	 * @return 文件名
	 */
	public String getFileName(String header) {
		/**
		 * String[] tempArr1 =
		 * header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
		 * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
		 * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
		 */
		String[] tempArr1 = header.split(";");
		/**
		 * 火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
		 * IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
		 */
		String[] tempArr2 = tempArr1[2].split("=");
		// 获取文件名，兼容各种浏览器的写法
		String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
		return fileName;
	}

}
