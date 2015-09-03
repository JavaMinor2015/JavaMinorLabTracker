package nl.stoux.minor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import nl.stoux.minor.Models.Assignment;
import nl.stoux.minor.Models.Json.AssignmentData;
import nl.stoux.minor.Models.Student;
import nl.stoux.minor.Models.StudentAssignment;
import nl.stoux.minor.Transformers.JsonTransformer;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by Leon Stam on 3-9-2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //Setup ORM connection
        JdbcConnectionSource conn = new JdbcConnectionSource("jdbc:mysql://localhost/labtracker", "labtracker", "system");
        final Dao<Assignment, Integer> assignmentDAO = DaoManager.createDao(conn, Assignment.class);

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("header", "Home");

            //Build the HTML
            String html = "<div id=\"assignment-data\" class=\"row text-center\">Selecteer een opdracht</div><hr/>";
            try {
                html += "<div class=\"row text-center\"><div class=\"col-lg-12\"><ul class=\"pagination\">";
                for (Assignment assignment : assignmentDAO.queryForAll()) {
                    html += "<li><a class=\"assignment-button btn btn-primary\" data-id=\"" + assignment.getId() + "\" href=\"#" + assignment.getId() + "\">" + assignment.getName() + "</a></li>";
                }
                html += "</ul></div><br /><a class=\"btn btn-primary\"  href=\"/add-assignment/\">Voeg opdracht toe</a> <a class=\"btn btn-primary\" href=\"/add-student/\">Voeg student toe</a></div>";
            } catch (Exception e) {
                e.printStackTrace();
                halt(500, "Woops");
            }


            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");
        }, new FreeMarkerEngine());

        get("/add-student/", (request1, response1) -> {
            String html = "<div id=\"assignment-data\" class=\"row\"><div class=\"col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3\"><form method=\"POST\">" +
                    "<div class=\"form-group\">" +
                    "    <label for=\"name\">Naam</label>" +
                    "    <input type=\"text\" class=\"form-control\" name=\"name\" placeholder=\"Henk\">" +
                    "</div>" +
                    "<div class=\"form-group\">" +
                    "    <label for=\"email\">Email</label>" +
                    "    <input type=\"email\" class=\"form-control\" name=\"email\" placeholder=\"Email\">" +
                    "</div>" +
                    "<div class=\"form-group\">" +
                    "    <label for=\"telephone\">Telefoon</label>" +
                    "    <input type=\"text\" class=\"form-control\" name=\"telephone\" placeholder=\"+31600000000\">" +
                    "</div>" +
                    "<button class=\"btn btn-primary\"  type=\"submit\">Voeg toe</button>" +
                    "</form></div></div>";

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("header", "Student toevoegen");

            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");
        }, new FreeMarkerEngine());

        post("/add-student/", (request2, response2) -> {
            Map<String, Object> attributes = new HashMap<>();
            String html = "";
            try {
                String name = request2.queryMap("name").value();
                String email = request2.queryMap("email").value();
                String telephone = request2.queryMap("telephone").value();

                Dao<Student, Integer> dao = DaoManager.createDao(conn, Student.class);
                dao.create(new Student(name, email, telephone));
                html = "Student " + name + " toegevoegd!<br/><a class=\"btn btn-primary\" href=\"/\">Terug </a>";

            } catch (Exception e) {
                e.printStackTrace();
                halt(500, "Nope");
            }

            attributes.put("header", "Student toegevoegd");
            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");

        }, new FreeMarkerEngine());

        get("/assign-student/", (request1, response1) -> {
            String select = "", html = "";
            try {
                Integer assignmentID = request1.queryMap("id").integerValue();

                ForeignCollection<StudentAssignment> assignments = assignmentDAO.queryForId(assignmentID).getStudents();
                Dao<Student, Integer> sDao = DaoManager.createDao(conn, Student.class);

                for (Student student : sDao.queryForAll()) {
                    boolean skip = false;
                    for (StudentAssignment assignment : assignments) {
                        if (student.getId().equals(assignment.getStudent().getId())) {
                            skip = true;
                            break;
                        }
                    }
                    if (!skip) {
                        select += "<option value=\"" + student.getId() + "\">" + student.getName() + "</option>";
                    }
                }

                html += "<div class=\"row text-center\"><form method=\"POST\">";
                html += "<p>Opdracht: " + assignmentDAO.queryForId(assignmentID).getName() + "</p>";
                html += "<p> Student: <select name=\"student_id\">" + select + "</select></p>";
                html += "<p><input class=\"form-control\" name=\"assignment_id\" type=\"hidden\" value=\"" + assignmentID + "\" /></p>";
                html += "<button class=\"btn btn-primary\" type=\"submit\">Ken toe</button>";
                html += "</form></div>";


            } catch (Exception e) {
                halt(500);
            }

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("header", "Assign student");

            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");
        }, new FreeMarkerEngine());

        post("/assign-student/", (request1, response1) -> {
            Map<String, Object> attributes = new HashMap<>();
            String html = "";
            try {
                int studentId = request1.queryMap("student_id").integerValue();
                int assId = request1.queryMap("assignment_id").integerValue();

                Dao<Student, Integer> sDao = DaoManager.createDao(conn, Student.class);
                Student s = sDao.queryForId(studentId);
                Assignment a = assignmentDAO.queryForId(assId);

                Dao<StudentAssignment, Integer> saDao = DaoManager.createDao(conn, StudentAssignment.class);
                saDao.create(new StudentAssignment(s, a, false));

                html = "Student is toegewezen! <a class=\"btn btn-primary\" href=\"/\">Terug </a>";

            } catch (Exception e) {
                e.printStackTrace();
                halt(500, "Nope");
            }

            attributes.put("header", "Student toegevoegd");
            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");
        }, new FreeMarkerEngine());

        get("/add-assignment/", (request1, response1) -> {
            String html = "<div id=\"assignment-data\" class=\"row\"><div class=\"col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3\"><form method=\"POST\">" +
                    "<div class=\"form-group\">" +
                    "    <label for=\"name\">Naam</label>" +
                    "    <input type=\"text\" class=\"form-control\" name=\"name\" placeholder=\"1A\">" +
                    "</div>" +
                    "<button class=\"btn btn-primary\"  type=\"submit\">Voeg toe</button>" +
                    "</form></div></div>";

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("header", "Opdracht toevoegen");

            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");
        }, new FreeMarkerEngine());

        post("/add-assignment/", (request1, response1) -> {
            Map<String, Object> attributes = new HashMap<>();
            String html = "";
            try {
                String name = request1.queryMap("name").value();
                assignmentDAO.create(new Assignment(name));
                html = "Opdracht '" + name + "' is toegevoegd!<br/><a class=\"btn btn-primary\" href=\"/\">Terug </a>";

            } catch (Exception e) {
                e.printStackTrace();
                halt(500, "Nope");
            }

            attributes.put("header", "Opdracht toevoegen");
            attributes.put("html", html);
            return new ModelAndView(attributes, "template.ftl");
        }, new FreeMarkerEngine());

        get("/assignment.json", (request, response) -> {
            int assignmentID = 0;
            try {
                assignmentID = request.queryMap("id").integerValue();
            } catch (NullPointerException e) {
                halt(400, "Invalid request");
            }
            Assignment assignment = assignmentDAO.queryForId(assignmentID);
            if (assignment == null) {
                halt(400, "Invalid request!");
            }

            AssignmentData data = new AssignmentData(assignmentID, assignment.getName());
            for (StudentAssignment studentAssignment : assignment.getStudents()) {
                data.addStudent(studentAssignment);
            }

            return data;
        }, new JsonTransformer());

        post("/assignment-edit", (request, response) -> {
            try {
                int studentID = request.queryMap("student").integerValue();
                int assignmentID = request.queryMap("assignment").integerValue();
                boolean status = request.queryMap("status").booleanValue();

                Dao<StudentAssignment, Integer> saDao = DaoManager.createDao(conn, StudentAssignment.class);
                saDao.updateRaw("UPDATE `students_assignments` SET `status` = ? WHERE `student_id` = ? AND `assignment_id` = ?",
                        (status ? "0" : "1"), String.valueOf(studentID), String.valueOf(assignmentID)
                );

                HashMap<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("status", !status);
                return jsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
                halt(400, "Nope");
            }


            return null;
        }, new JsonTransformer());

    }

}
