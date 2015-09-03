package nl.stoux.minor.Models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Leon Stam on 3-9-2015.
 */
@DatabaseTable(tableName = "students")
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Student {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField()
    private String name;

    @DatabaseField()
    private String email;

    @DatabaseField()
    private String telephone;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<StudentAssignment> assignments;


    public Student(String name, String email, String telephone) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

}
