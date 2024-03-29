package com.jsp.studentmanagementsystem1.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.jsp.studentmanagementsystem1.dto.MessageData;
import com.jsp.studentmanagementsystem1.dto.StudentRequest;
import com.jsp.studentmanagementsystem1.dto.StudentResponse;
import com.jsp.studentmanagementsystem1.entity.Student;
import com.jsp.studentmanagementsystem1.service.StudentService;
import com.jsp.studentmanagementsystem1.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.persistence.GenerationType;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	
//	@PostMapping()
	@RequestMapping(method = RequestMethod.POST, value = "student")
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest)
	{
		return studentService.saveStudent(studentRequest);
	}
	
//	@PutMapping
	@CrossOrigin
	@RequestMapping(method = RequestMethod.PUT, value = "student", params = "studentId")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@RequestBody @Valid StudentRequest studentRequest, @RequestParam int studentId)
	{
		return studentService.updateStudent(studentRequest, studentId);
	}
	
	
//	@DeleteMapping
	@RequestMapping(method = RequestMethod.DELETE, value = "student")
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(@RequestParam int studentId)
	{
		return studentService.deleteStudent(studentId);
	}
	
	
//	@GetMapping
//	public ResponseEntity<Student> findbyId(@RequestParam int studentId)
//	{
//		return studentService.findStudentById(studentId);
//	}
	
	@GetMapping(value = "stds/s" ,params = "studentId")
//	@RequestMapping(method = RequestMethod.GET, value = "id/i")
	public ResponseEntity<ResponseStructure<StudentResponse>> findbyId(@RequestParam int studentId)
	{
		return studentService.findStudentById(studentId);
	}
	
	@CrossOrigin
	@GetMapping(value = "students")
//	@RequestMapping(method = RequestMethod.GET, value = "students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAll()
	{
		return studentService.findAllStudents();
	}
	
//	@GetMapping(params = "studentEmail")
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "student", params = "studentEmail")
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail (String studentEmail)
	{
		return studentService.findByEmail(studentEmail);
	}
	
//	@GetMapping(params = "studentPhNo")
	@RequestMapping(method = RequestMethod.GET, value = "student", params = "studentPhNo")
	public ResponseEntity<ResponseStructure<StudentResponse>> findByPhNo (long studentPhNo)
	{
		return studentService.findByPhNo(studentPhNo);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "student", params = "studentGrade")
	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade (@RequestParam String studentGrade)
	{
		return studentService.getAllEmailsByGrade(studentGrade);
	}
	
	/*
	@RequestMapping(method = RequestMethod.POST, value = "extract")
	public ResponseEntity<String> extractDataFromExcel (@RequestParam MultipartFile file) throws IOException
	{
		return studentService.extractDataFromExcel(file);
	}
	*/
	
	@RequestMapping(method = RequestMethod.POST, value = "extract")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel (@RequestParam MultipartFile file) throws IOException
	{
		return studentService.extractDataFromExcel(file);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "write/excel")
	public ResponseEntity<String> writeToExcel (@RequestParam String filePath) throws IOException
	{
		return studentService.writeToExcel(filePath);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "extract/csvfile/csv")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDatafromCsv (@RequestParam MultipartFile file) throws IOException
	{
		return studentService.extractDatafromCsv(file);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "write/csv")
	public ResponseEntity<String> writeToCsv (@RequestParam String filePath) throws IOException
	{
		return studentService.writeToCsv(filePath);
	}
	
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "mail")
	public ResponseEntity<String> sendMail (@RequestBody MessageData messageData)
	{
		return studentService.sendMail(messageData);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "mime-message")
	public ResponseEntity<String> sendMyMailMessage (@RequestBody MessageData messageData) throws MessagingException
	{
		return studentService.sendMyMailMessage(messageData);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "attherate/csvfile")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDatafromCsvAtTheRate (@RequestParam MultipartFile file) throws IOException
	{
		return studentService.extractDatafromCsvAtTheRate(file);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "attherate/write/csv")
	public ResponseEntity<String> writeToCsvAtTheRate (@RequestParam String filePath) throws IOException 
	{
		return studentService.writeToCsvAtTheRate(filePath);
	}
	
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "checkemail")
	public ResponseEntity<String> checkEmail (String studentEmail, String studentPassword)
	{
		return studentService.checkEmail(studentEmail, studentPassword);
	}
}
