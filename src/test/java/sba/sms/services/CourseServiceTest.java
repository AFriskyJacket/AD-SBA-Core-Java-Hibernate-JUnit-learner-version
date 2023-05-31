package sba.sms.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sba.sms.models.Course;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseServiceTest {
    static CourseService courseService;
    private static String instructorPhillip = "Phillip Witkin";
    private static List<Course> expectedCourses = new ArrayList<>(Arrays.asList(
            new Course(1, "Java", instructorPhillip),
            new Course(2, "Frontend", "Kasper Kain"),
            new Course(3, "JPA", "Jafer Alhaboubi"),
            new Course(4, "Spring Framework", instructorPhillip),
            new Course(5, "SQL", instructorPhillip)
    ));

    @BeforeAll
    static void beforeAll() {
        courseService = new CourseService();
        CommandLine.addData();
    }

    @Test
    void getAllCourses() {
        assertThat(courseService.getAllCourses()).hasSameElementsAs(expectedCourses);
        System.out.println(courseService.getAllCourses().toString());
    }

    @Test
    void getCourseById() {
        assertThat(courseService.getCourseById(1)).isEqualTo(expectedCourses.get(0));
        System.out.println(courseService.getCourseById(1));
    }
}