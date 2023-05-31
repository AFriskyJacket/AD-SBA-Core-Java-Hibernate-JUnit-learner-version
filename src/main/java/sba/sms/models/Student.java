package sba.sms.models;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

/*Student (@Table(name = "student")):
Field	Datatype	Description	Database attributes @Column()
email	String	Student’s unique identifier	Primary key, 50 character limit, name email
name	String	Student’s name	50 character limit, not null, name name
password	String	Student’s password	50 character limit not null, name password
courses	List<Course>	Student courses list	Join table strategy name student_courses , name of student primary key column student_email and inverse primary key (courses) column courses_id , fetch type eager, cascade all except remove*/
@Entity
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "password", length = 50, nullable = false)
    private String password;
    @ManyToMany(targetEntity = Course.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "STUDENT_COURSES",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private List<Course> courses;

    public Student() {
    }

    public Student(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Student(String email, String name, String password, List<Course> courses) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.courses = courses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(getEmail(), student.getEmail()) && Objects.equals(getName(), student.getName()) && Objects.equals(getPassword(), student.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getName(), getPassword());
    }

    @Override
    public String toString() {
        return "Student{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", courses=" + getCoursesNames() +
                '}';
    }

    private String getCoursesNames() {
        String names = "[";
        if (Hibernate.isInitialized(this.getCourses()) && this.getCourses().size()>0) {
            for (Course c : this.getCourses()) {
                names = names.concat(c.getName() + ", " );
            }
        }
        names = names.concat("]");
        return names;
    }
}
