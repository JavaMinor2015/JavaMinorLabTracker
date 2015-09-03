package nl.stoux.minor.Models.Json;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nl.stoux.minor.Models.Student;
import nl.stoux.minor.Models.StudentAssignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon Stam on 3-9-2015.
 */

public class AssignmentData {

    private int id;
    private String name;

    private List<StudentData> students;

    public AssignmentData(int id, String name) {
        this.id = id;
        this.name = name;
        students = new ArrayList<>();
    }

    public void addStudent(StudentAssignment assignment) {
        Student student = assignment.getStudent();
        students.add(new StudentData(student.getId(), student.getName(), student.getEmail(), student.getTelephone(), assignment.isStatus()));
    }


    @NoArgsConstructor
    @AllArgsConstructor
    private class StudentData {
        private int id;
        private String name;
        private String email;
        private String telephone;
        private boolean status;
    }

}
