
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Quiz1 extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/ajpmicro";
            Connection con = DriverManager.getConnection(url, "postgres", "");

            String query = "SELECT id, questions, a_option, b_option, c_option, d_option FROM quizquestions ORDER BY RANDOM() LIMIT 3";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            pw.println("<html><body>");
            pw.println("<form action='submitQuiz' method='post'>");

            int questionNumber = 1;
            while (rs.next()) {
                int questionId = rs.getInt(1);
                String question = rs.getString(2);
                String a = rs.getString(3);
                String b = rs.getString(4);
                String c = rs.getString(5);
                String d = rs.getString(6);

                pw.println("<p>" + question + "</p>");
                pw.println("<input type='radio' name='question" + questionNumber + "' value='" + a + "'> " + a + "<br>");
                pw.println("<input type='radio' name='question" + questionNumber + "' value='" + b + "'> " + b + "<br>");
                pw.println("<input type='radio' name='question" + questionNumber + "' value='" + c + "'> " + c + "<br>");
                pw.println("<input type='radio' name='question" + questionNumber + "' value='" + d + "'> " + d + "<br>");

                pw.println("<input type='hidden' name='questionId" + questionNumber + "' value='" + questionId + "'>");
                questionNumber++;
            }

            pw.println("<input type='submit' value='Submit'>");
            pw.println("</form>");
            pw.println("</body></html>");

            con.close();
        } catch (Exception e) {
            pw.println("<p>SQL Error: " + e.getMessage() + "</p>");
        }

        pw.close();
    }
}
