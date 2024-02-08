package com.jsp.studentmanagementsystem1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jsp.studentmanagementsystem1.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer>{
	
	public Student findByStudentEmail (String email);
	
	public Student findByStudentPhNo (long phno);
	
	@Query("select s.studentEmail from Student s where studentGrade=?1")
	public List<String> getAllEmailsByGrade (String grade);
}
//custom respository in jpa ----- write findBy(variableName) which you want....