package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("username") == null) {
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            pw.println("<html><head><style>");
            pw.println("body { background: linear-gradient(135deg, navy, skyblue); color: white; font-family: Arial, sans-serif; }");
            pw.println(".card { background: white; color: black; padding: 20px; margin: 15px; border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2); max-width: 400px; }");
            pw.println(".center { display: flex; flex-direction: column; align-items: center; }");
            pw.println(".submit-btn { background-color: navy; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; }");
            pw.println(".submit-btn:hover { background-color: darkblue; }");
            pw.println("</style></head><body class='center'>");
            pw.println("<div class='card'><h1 style='text-align:center'>Login</h1><br>");
            pw.println("<form method='POST' action='http://localhost:8080/servlet1/hello'>");
            pw.println("Username: <input type='text' name='username' required><br><br>");
            pw.println("Password: <input type='password' name='password' required><br><br>");
            pw.println("Select number of questions: ");
            pw.println("<select name='numQuestions'>");
            for (int i = 5; i <= 40; i += 5) {
                pw.println("<option value='" + i + "'>" + i + "</option>");
            }
            pw.println("</select><br><br>");
            pw.println("<input type='submit' style='display: block; margin: 0 auto; padding: 10px 20px; background-color: purple; color: white; border: none; border-radius: 4px; cursor: pointer;' value='Start Quiz'>");
            pw.println("</form></div>");
            pw.println("</body></html>");
        } else {
            doGetWithQuiz(request, response);
        }
    }

    public void doGetWithQuiz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/ajpmicro";
            Connection con = DriverManager.getConnection(url, "postgres", "");
            int numQuestions = Integer.parseInt(request.getParameter("numQuestions"));
            request.getSession().setAttribute("numQuestions", numQuestions);
            Vector<Integer> questionIds = new Vector<>();
            request.getSession().setAttribute("questionIds", questionIds);
            String query = "SELECT id, questions, a_option, b_option, c_option, d_option FROM quizquestions1 ORDER BY RANDOM() LIMIT " + numQuestions;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            pw.println("<html><head><style>");
            pw.println("body { background: linear-gradient(135deg, navy, skyblue); color: white; font-family: Arial, sans-serif; }");
            pw.println(".card { background: white; color: black; padding: 20px; margin: 15px; border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2); }");
            pw.println(".question-container { display: flex; flex-direction: column; align-items: center; }");
            pw.println(".submit-btn { background-color: navy; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; }");
            pw.println(".submit-btn:hover { background-color: darkblue; }");
            pw.println("</style></head><body><div class='question-container'>");
            pw.println("<form method='POST' action='http://localhost:8080/servlet1/hello?submitQuiz=true'>");
            pw.println("<input type='hidden' name='numQuestions' value='" + numQuestions + "'>");
            int questionNumber = 1;
            while (rs.next()) {
                int questionId = rs.getInt("id");
                questionIds.add(questionId);
                String question = rs.getString("questions");
                String a = rs.getString("a_option");
                String b = rs.getString("b_option");
                String c = rs.getString("c_option");
                String d = rs.getString("d_option");
                pw.println("<div class='card'><p><strong>" + questionNumber + ". " + question + "</strong></p>");
                pw.println("<label><input type='radio' name='question" + questionNumber + "' value='A'> " + a + "</label><br>");
                pw.println("<label><input type='radio' name='question" + questionNumber + "' value='B'> " + b + "</label><br>");
                pw.println("<label><input type='radio' name='question" + questionNumber + "' value='C'> " + c + "</label><br>");
                pw.println("<label><input type='radio' name='question" + questionNumber + "' value='D'> " + d + "</label><br>");
                pw.println("</div>");
                questionNumber++;
            }
            pw.println("<input type='submit' style='display: block; margin: 0 auto; padding: 10px 20px; background-color: blue; color: white; border: none; border-radius: 4px; cursor: pointer;' value='Submit Quiz'>");
            pw.println("</form></div></body></html>");
            con.close();
        } catch (Exception e) {
            pw.println(e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String submitQuiz = request.getParameter("submitQuiz");
        if (submitQuiz != null) {
            calculate(request, response);
        } else {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            boolean isValidLogin = validateLogin(username, password, pw);
            if (isValidLogin) {
                request.getSession().setAttribute("username", username);
                doGetWithQuiz(request, response);
            } else {
                response.setContentType("text/html");
                pw.println("<html><body>");
                pw.println("<h1>Login Failed</h1>");
                pw.println("<p>Invalid username or password.</p>");
                pw.println("<a href='/'>Try again</a>");
                pw.println("</body></html>");
            }
        }
    }

    public boolean validateLogin(String username, String password, PrintWriter pw) {
        boolean flag = false;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/ajpmicro";
            Connection con = DriverManager.getConnection(url, "postgres", "");
            String query1 = "SELECT username, password FROM users";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                String un = rs.getString(1);
                String pass = rs.getString(2);
                if (un.equals(username) && pass.equals(password)) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            pw.println("<p>Error: " + e.getMessage() + "</p>");
        }
        return flag;
    }

    public void calculate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        int marks = 0;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/ajpmicro";
            Connection con = DriverManager.getConnection(url, "postgres", "");
            int totalQuestions = (int) request.getSession().getAttribute("numQuestions");
            Vector<Integer> questionIds = (Vector<Integer>) request.getSession().getAttribute("questionIds");
            for (int i = 1; i <= totalQuestions; i++) {
                String userAnswer = request.getParameter("question" + i);
                int questionId = questionIds.get(i - 1);
                String getCorrectOption = "SELECT correct_option FROM quizquestions1 WHERE id=?";
                PreparedStatement stmt = con.prepareStatement(getCorrectOption);
                stmt.setInt(1, questionId);
                ResultSet rsCorrect = stmt.executeQuery();
                if (rsCorrect.next()) {
                    String correctAnswer = rsCorrect.getString("correct_option");
                    if (correctAnswer.equalsIgnoreCase(userAnswer)) {
                        marks++;
                    }
                } else {
                    pw.println("<p>Error: Correct answer not found for question ID " + questionId + "</p>");
                }
                rsCorrect.close();
                stmt.close();
            }
            pw.println("<html><body><center><h2>Your Total Score: " + marks + " out of " + totalQuestions + "</h2></center></body></html>");
            con.close();
        } catch (Exception e) {
            pw.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
