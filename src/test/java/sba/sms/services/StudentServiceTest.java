package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
class StudentServiceTest {

    static StudentService studentService;
    static CourseService courseService;
    private static List<Student> expected;

    @BeforeAll
    static void beforeAll() {
        studentService = new StudentService();
        CommandLine.addData();
        expected = new ArrayList<>(Arrays.asList(
                new Student("reema@gmail.com", "reema brown", "password"),
                new Student("annette@gmail.com", "annette allen", "password"),
                new Student("anthony@gmail.com", "anthony gallegos", "password"),
                new Student("ariadna@gmail.com", "ariadna ramirez", "password"),
                new Student("bolaji@gmail.com", "bolaji saibu", "password")
        ));
    }

    @Test
    void getAllStudents() {
        assertThat(studentService.getAllStudents()).hasSameElementsAs(expected);
        System.out.println(studentService.getAllStudents().toString());
    }

    @Test
    void getStudentCourses() {
        courseService = new CourseService();
        studentService.registerStudentToCourse(expected.get(0).getEmail(), 1);
        System.out.println(studentService.getStudentByEmail(expected.get(0).getEmail()));
        System.out.println(courseService.getCourseById(1));
        assertThat(studentService.getStudentCourses(expected.get(0).getEmail())).hasSameElementsAs(Collections.singleton(courseService.getCourseById(1)));
        System.out.println(studentService.getStudentByEmail(expected.get(0).getEmail()));
    }

    @Test
    void getStudentByEmail() {
        assertThat(studentService.getStudentByEmail(expected.get(0).getEmail()).getEmail()).isEqualTo(expected.get(0).getEmail());
        System.out.println(studentService.getStudentByEmail(expected.get(0).getEmail()).toString());
    }
}