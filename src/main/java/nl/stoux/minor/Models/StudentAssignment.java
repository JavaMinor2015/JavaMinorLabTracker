package nl.stoux.minor.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Leon Stam on 3-9-2015.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@DatabaseTable(tableName = "students_assignments")
public class StudentAssignment {

    @DatabaseField(foreign = true, columnName = "student_id", foreignAutoRefresh = true)
    private Student student;

    @DatabaseField(foreign = true, columnName = "assignment_id", foreignAutoRefresh = true)
    private Assignment assignment;

    @DatabaseField()
    @Setter
    private boolean status;

}
