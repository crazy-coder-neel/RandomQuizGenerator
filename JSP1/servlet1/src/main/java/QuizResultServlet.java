
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class QuizResultServlet extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        int score = 0;

        try {
            String url = "jdbc:postgresql://localhost:5432/ajpmicro";
            Connection con = DriverManager.getConnection(url, "postgres", "");

            for (int i = 1; i <= 3; i++) {
                String selectedAnswer = req.getParameter("question" + i);
                int questionId = Integer.parseInt(req.getParameter("questionId" + i));

                String query = "SELECT correct_option FROM quizquestions WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, questionId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String correctAnswer = rs.getString(1);

                    if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
                        score++;
                    }
                }
            }

            pw.println("<html><body>");
            pw.println("<p>Your Score: " + score + "/3</p>");
            pw.println("</body></html>");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            pw.println("<p>SQL Error: " + e.getMessage() + "</p>");
        }

        pw.close();
    }
}
