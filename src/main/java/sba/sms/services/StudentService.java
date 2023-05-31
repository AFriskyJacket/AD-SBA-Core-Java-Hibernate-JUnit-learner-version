package sba.sms.services;

import jakarta.persistence.criteria.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.List;

public class StudentService implements StudentI {

    @Override
    public List<Student> getAllStudents() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            return queryAllStudents(session, transaction);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void createStudent(Student student) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (queryAllStudents(session, transaction).stream().noneMatch(s -> s.getEmail().equals(student.getEmail()))) {
                session.persist(student);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            return queryStudentByEmail(session, transaction, email);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Student student = queryStudentByEmail(session, transaction, email);
            if (student.getPassword().equals(password)) return true;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        CourseService cs = new CourseService();
        Course course = cs.getCourseById(courseId);
        try {
            transaction = session.beginTransaction();
            Student student = queryStudentByEmail(session, transaction, email);

            if (student.getCourses().stream().noneMatch(course1 -> course1.equals(course)))
            {

                student.getCourses().add(course);
                session.merge(student);
            }
                transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Student student = queryStudentByEmail(session,transaction, email);
            if (student != null) return student.getCourses();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
    private Student queryStudentByEmail(Session sesh, Transaction transaction, String email) throws HibernateException{
        if(transaction.isActive()) {
            CriteriaBuilder cb = sesh.getCriteriaBuilder();
            CriteriaQuery<Student> studentCriteriaQuery = cb.createQuery(Student.class);
            Root<Student> studentRoot = studentCriteriaQuery.from(Student.class);

            studentRoot.fetch("courses", JoinType.LEFT);
            studentCriteriaQuery.select(studentRoot);
            studentCriteriaQuery.where(cb.equal(studentRoot.get("email"), email));

            Query<Student> query = sesh.createQuery(studentCriteriaQuery);
            return query.getResultList().stream().filter(student -> student.getEmail().equals(email)).findAny().orElse(null);
        }
        return null;
    }

    /*transactionless query to prevent code duplication*/
    private List<Student> queryAllStudents(Session sesh, Transaction transaction) throws HibernateException {
        if (transaction.isActive()) {
            CriteriaBuilder cb = sesh.getCriteriaBuilder();
            CriteriaQuery<Student> studentCriteriaQuery = cb.createQuery(Student.class);
            Root<Student> studentRoot = studentCriteriaQuery.from(Student.class);

            studentRoot.fetch("courses", JoinType.LEFT);
            studentCriteriaQuery.select(studentRoot);

            Query<Student> query = sesh.createQuery(studentCriteriaQuery);
            return query.getResultList();
        }
        return null;
    }
}
