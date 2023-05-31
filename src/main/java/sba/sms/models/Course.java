package sba.sms.models;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

/*Course (@Table(name = "course")):
Field	Datatype	Description	Database attributes @Column()
id	int	Course unique identifier	Primary key
name	String	Course name	50 character limit, not null
instructor	String	Instructor name	50 character limit not null
students	List<Student>	Course learners list	fetch type eager, cascade all except remove, mappedBy courses*/
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "instructor", length = 50, nullable = false)
    private String instructor;
    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Student> students;

    public Course() {
    }

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

    public Course(int id, String name, String instructor) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
    }

    public Course(int id, String name, String instructor, List<Student> students) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getId() == course.getId() && Objects.equals(getName(), course.getName()) && Objects.equals(getInstructor(), course.getInstructor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getInstructor());
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instructor='" + instructor + '\'' +
                ", students=" + getStudentsNames() +
                '}';
    }

    private String getStudentsNames() {
        String names = "[";
        if (Hibernate.isInitialized(this.getStudents()) && this.getStudents().size()>0) {
            for (Student c : this.getStudents()) {
                names = names.concat(c.getName() + ", ");
            }
        }
        names = names.concat("]");
        return names;
    }
}
