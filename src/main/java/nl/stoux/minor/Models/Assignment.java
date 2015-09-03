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
@DatabaseTable(tableName = "assignments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Assignment {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = true, maxEagerLevel = 2)
    private ForeignCollection<StudentAssignment> students;

    public Assignment(String name) {
        this.name = name;
    }

}
