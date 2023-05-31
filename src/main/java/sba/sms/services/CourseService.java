package sba.sms.services;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.List;

public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (queryAllCourses(session, transaction).stream().noneMatch(s -> s.equals(course))) {
                session.persist(course);
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
    public Course getCourseById(int courseId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            return queryCourseById(session, transaction, courseId);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            return queryAllCourses(session, transaction);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    /*transactionless query to prevent code duplication*/
    private List<Course> queryAllCourses(Session sesh, Transaction transaction) throws HibernateException {
        if (transaction.isActive()) {
            CriteriaBuilder cb = sesh.getCriteriaBuilder();
            CriteriaQuery<Course> courseCriteriaQuery = cb.createQuery(Course.class);
            Root<Course> courseRoot = courseCriteriaQuery.from(Course.class);

            courseRoot.fetch("students", JoinType.LEFT);
            courseCriteriaQuery.select(courseRoot);

            Query<Course> query = sesh.createQuery(courseCriteriaQuery);
            return query.getResultList();
        }
        return null;
    }
    private Course queryCourseById(Session sesh, Transaction transaction, int id) {
        if (transaction.isActive()) {
            CriteriaBuilder cb = sesh.getCriteriaBuilder();
            CriteriaQuery<Course> courseCriteriaQuery = cb.createQuery(Course.class);
            Root<Course> courseRoot = courseCriteriaQuery.from(Course.class);

            courseRoot.fetch("students", JoinType.LEFT);
            courseCriteriaQuery.select(courseRoot);
            courseCriteriaQuery.where(cb.equal(courseRoot.get("id"),id));

            Query<Course> query = sesh.createQuery(courseCriteriaQuery);
            return query.getResultList().stream().filter(course -> Integer.valueOf(course.getId()).equals(id)).findAny().orElse(null);
        }
        return null;
    }
}
