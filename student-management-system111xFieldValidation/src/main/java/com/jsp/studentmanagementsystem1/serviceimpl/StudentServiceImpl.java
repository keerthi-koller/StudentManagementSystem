package com.jsp.studentmanagementsystem1.serviceimpl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jsp.studentmanagementsystem1.dto.MessageData;
import com.jsp.studentmanagementsystem1.dto.StudentRequest;
import com.jsp.studentmanagementsystem1.dto.StudentResponse;
import com.jsp.studentmanagementsystem1.entity.Student;
import com.jsp.studentmanagementsystem1.exception.StudentEmailsNotFoundBasedOnGradeException;
import com.jsp.studentmanagementsystem1.exception.StudentNotFoundByEmailException;
import com.jsp.studentmanagementsystem1.exception.StudentNotFoundByIdException;
import com.jsp.studentmanagementsystem1.exception.StudentNotFoundByPhNoException;
import com.jsp.studentmanagementsystem1.repository.StudentRepo;
import com.jsp.studentmanagementsystem1.service.StudentService;
import com.jsp.studentmanagementsystem1.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.websocket.Decoder.Text;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;
	

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) {

		Student student = new Student();
		
		student.setStudentName(studentRequest.getStudentName());
		student.setStudentPhNo(studentRequest.getStudentPhNo());
		student.setStudentEmail(studentRequest.getStudentEmail());
		student.setStudentGrade(studentRequest.getStudentGrade());
		student.setStudentPassword(studentRequest.getStudentPassword());
		
		Student save = studentRepo.save(student);
		
		StudentResponse studentResponse = new StudentResponse();
		studentResponse.setStudentId(save.getStudentId());
		studentResponse.setStudentName(save.getStudentName());
		studentResponse.setStudentGrade(save.getStudentGrade());
		
		ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Student data saved successfully");
		structure.setData(studentResponse);
		
		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest, int studentId) {

//		student---contains new data don't know which data is new whole data is new
		Optional<Student> optional = studentRepo.findById(studentId);	//having old data
		
		Student student11 = new Student();
		
		if (optional.isPresent())
		{
			Student student2 = optional.get();
			student11.setStudentId(student2.getStudentId());
			student11.setStudentName(studentRequest.getStudentName());
			student11.setStudentPhNo(studentRequest.getStudentPhNo());
			student11.setStudentEmail(studentRequest.getStudentEmail());
			student11.setStudentGrade(studentRequest.getStudentGrade());
			student11.setStudentPassword(studentRequest.getStudentPassword());
			
//			studentRepo.save(student);
//			return new ResponseEntity<Student>(student, HttpStatus.OK);
			Student save = studentRepo.save(student11);
			
			StudentResponse studentResponse = new StudentResponse();
			studentResponse.setStudentId(save.getStudentId());
			studentResponse.setStudentName(save.getStudentName());
			studentResponse.setStudentGrade(save.getStudentGrade());
			
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student data updated successfully");
			structure.setData(studentResponse);
			
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);
		}
		else
		{
			throw new StudentNotFoundByIdException("Student of this id not found!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(int studentId) {
		Optional<Student> optional = studentRepo.findById(studentId);
				
		if (optional.isPresent())
		{
			Student student = optional.get();

			studentRepo.delete(student);
			
			StudentResponse studentResponse = new StudentResponse();
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student data deleted successfully");
			structure.setData(studentResponse);
			
			return new ResponseEntity<ResponseStructure<StudentResponse>> (structure, HttpStatus.OK);
		}
		else
		{
			throw new StudentNotFoundByIdException("Failed to delete the Student!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentById(int studentId) {
		Optional<Student> optional = studentRepo.findById(studentId);
		
		if (optional.isPresent())
		{
			Student student = optional.get();
			
			StudentResponse studentResponse = new StudentResponse();
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student data found successfully");
			structure.setData(studentResponse);
			
			return new ResponseEntity<ResponseStructure<StudentResponse>> (structure, HttpStatus.FOUND);
		}
		else
		{
			throw new StudentNotFoundByIdException("Student of this id not found!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents() {
		List<Student> list = studentRepo.findAll();
		
		List<StudentResponse> studentResponses = new ArrayList<StudentResponse>();
		
		for (int i=0; i<=list.size()-1; i++)
		{
			StudentResponse studentResponse = new StudentResponse();
			
			studentResponse.setStudentId(list.get(i).getStudentId());
			studentResponse.setStudentName(list.get(i).getStudentName());
			studentResponse.setStudentGrade(list.get(i).getStudentGrade());
			
			studentResponses.add(studentResponse);
		}
		
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		structure.setMessage("All the Student data fetched successfully !!");
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setData(studentResponses);
		
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail (String studentEmail) {

		Student student = studentRepo.findByStudentEmail(studentEmail);
		if (student != null)
		{
			StudentResponse studentResponse = new StudentResponse();
			
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			
			structure.setMessage("Student found based on Email");
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setData(studentResponse);
			
			return new ResponseEntity<ResponseStructure<StudentResponse>> (structure, HttpStatus.FOUND);
			
		}
		else
		{
			throw new StudentNotFoundByEmailException ("Failed to find the Student !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findByPhNo(long studentPhNo) {

		Student student = studentRepo.findByStudentPhNo(studentPhNo);
		if (student != null)
		{
			StudentResponse studentResponse = new StudentResponse();
			
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			
			structure.setMessage("Student found based on PhNo");
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setData(studentResponse);
			
			return new ResponseEntity<ResponseStructure<StudentResponse>> (structure, HttpStatus.FOUND);
		}
		else
		{
			throw new StudentNotFoundByPhNoException ("Failed to find the Student !!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade(String grade) {
		List<String> allEmailsByGrade = studentRepo.getAllEmailsByGrade(grade);
		
		if (!allEmailsByGrade.isEmpty())
		{
			
			ResponseStructure<List<String>> structure = new ResponseStructure<List<String>>();
			
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student Emails found based on Grade");
			structure.setData(allEmailsByGrade);
			
			return new ResponseEntity<ResponseStructure<List<String>>> (structure, HttpStatus.FOUND);
			
		}
		else
		{
			throw new StudentEmailsNotFoundBasedOnGradeException ("No Emails are present based on grade");
		}
	}


	/* 
	 --------------------------ONLY RETURNING STRING VALUES--------------------------
	@Override
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException {
		
//		import org.apache.poi.ss.usermodel.Row;
//		import org.apache.poi.ss.usermodel.Sheet;
//		import org.apache.poi.xssf.usermodel.XSSFWorkbook;

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		
		for (Sheet sheet : workbook)
		{
			for (Row row :sheet)
			{			
				Student student = new Student();
				
				if (row.getRowNum() > 0)
				{
					if (row != null)
					{
						String name = row.getCell(0).getStringCellValue();
						String email = row.getCell(1).getStringCellValue();
						long phoneNumber = (long) row.getCell(2).getNumericCellValue();
						String grade = row.getCell(3).getStringCellValue();
						String password = row.getCell(4).getStringCellValue();
						
						student.setStudentName(name);
						student.setStudentEmail(email);
						student.setStudentPhNo(phoneNumber);
						student.setStudentGrade(grade);
						student.setStudentPassword(password);
						
						studentRepo.save(student);
						
					}
				}
			}
		}
		return new ResponseEntity<String> (HttpStatus.CREATED);
	}
	 */
	

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel(MultipartFile file) throws IOException {
		
		List<StudentResponse> list = new ArrayList<StudentResponse>();

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
				
		for (Sheet sheet : workbook)
		{
			for (Row row : sheet)
			{
				if (row.getRowNum() > 0)
				{
					if (row != null)
					{
						String name = row.getCell(0).getStringCellValue();
						String email = row.getCell(1).getStringCellValue();
						long phoneNumber = (long) row.getCell(2).getNumericCellValue();
						String grade = row.getCell(3).getStringCellValue();
						String password = row.getCell(4).getStringCellValue();
						
						Student student = new Student();
						
						student.setStudentName(name);
						student.setStudentEmail(email);
						student.setStudentPhNo(phoneNumber);
						student.setStudentGrade(grade);
						student.setStudentPassword(password);
						
						student = studentRepo.save(student);
						
						StudentResponse studentResponse = new StudentResponse();
						
						studentResponse.setStudentId(student.getStudentId());
						studentResponse.setStudentName(student.getStudentName());
						studentResponse.setStudentGrade(student.getStudentGrade());
						
						list.add(studentResponse);
					}					
				}
			}
		}
		
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Data saved successfully !!");
		structure.setData(list);
		
		workbook.close();
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>> (structure, HttpStatus.CREATED);		
	}
	
	
	@Override
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException {
		
		List<Student> students = studentRepo.findAll();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet();
		
		XSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("studentId");
		header.createCell(1).setCellValue("studentName");
		header.createCell(2).setCellValue("studentEmail");
		header.createCell(3).setCellValue("studentPhNo");
		header.createCell(4).setCellValue("studentGrade");
		header.createCell(5).setCellValue("studentPassword");
		
		int rowNum = 1;
		for (Student std : students)
		{
			Row row = sheet.createRow(rowNum++);
			
			row.createCell(0).setCellValue(std.getStudentId());
			row.createCell(1).setCellValue(std.getStudentName());
			row.createCell(2).setCellValue(std.getStudentEmail());
			row.createCell(3).setCellValue(std.getStudentPhNo());
			row.createCell(4).setCellValue(std.getStudentGrade());
			row.createCell(5).setCellValue(std.getStudentPassword());
		}
		FileOutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
		
		workbook.close();
		return new ResponseEntity<String> ("Data transfered to excel sheet !!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDatafromCsv(MultipartFile file) throws IOException {
		
		BufferedReader filReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
		CSVParser csvParser = new CSVParser(filReader, 
				CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

		
				
		Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		
		List<StudentResponse> studentResponses = new ArrayList<StudentResponse>();
		
		for (CSVRecord csvRecord : csvRecords)
		{
			String name = csvRecord.get("studentName");
			long phoneNumber = Long.parseLong(csvRecord.get("studentPhNo"));
			String password = csvRecord.get("studentPassword");
			String email = csvRecord.get("studentEmail");
			String grade = csvRecord.get("studentGrade");
			
			System.out.println(name+", "+phoneNumber+", "+password+", "+email+", "+grade);
			
			Student student = new Student();
			
			student.setStudentName(name);
			student.setStudentPhNo(phoneNumber);
			student.setStudentPassword(password);
			student.setStudentEmail(email);			
			student.setStudentGrade(grade);
			
			student = studentRepo.save(student);
			
			StudentResponse studentResponse = new StudentResponse();
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			
			studentResponses.add(studentResponse);
		}
		
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Data saved successfully !!");
		structure.setData(studentResponses);
		
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>> (structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> writeToCsv (String filePath) throws IOException {
				
		List<Student> students = studentRepo.findAll();
				
		CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT);
		csvPrinter.printRecord("studentId", "studentName", "studentPhNo", "studentPassword", "studentEmail", "studentGrade");
		csvPrinter.println();
		
		for (Student student : students)
		{
			csvPrinter.printRecord(student.getStudentId(), student.getStudentName(), student.getStudentPhNo(),
					student.getStudentPassword(), student.getStudentEmail(), student.getStudentGrade());
		}
		
		csvPrinter.flush();
		return new ResponseEntity<String> ("Data transfered to CSV File !!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMail (MessageData messageData) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText()
				+"\n\nThanks & Regards"
				+"\n"+messageData.getSenderName()
				+"\n"+messageData.getSenderAddress());
		message.setSentDate(new Date());
		
		javaMailSender.send(message);
		
		return new ResponseEntity<String> ("Mail sent seccessfully !!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMyMailMessage (MessageData messageData) throws MessagingException {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);//true--multipart file or not--media
		
		messageHelper.setTo(messageData.getTo());
		messageHelper.setSubject(messageData.getSubject());
		String emailBody = "<div style='background-color:yellow'>" + messageData.getText() + 
							"<br><br><h4>Thanks & Regards</h4>" +
							"<h3 style='color:red'>" + messageData.getSenderName() + "<br>" +
							messageData.getSenderAddress() + "</h3></div>" + 
							"<img src='https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png' width='250' height='100'>";
		messageHelper.setText(emailBody, true);//true--html file or not
		messageHelper.setSentDate(new Date());
		
		javaMailSender.send(mimeMessage);
		
		return new ResponseEntity<String> ("Mail sent successfully !!", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDatafromCsvAtTheRate (MultipartFile file) throws IOException
	{
		BufferedReader filReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
		CSVParser csvParser = new CSVParser(filReader, 
								CSVFormat.newFormat('@').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
		
		Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		
		List<StudentResponse> studentResponses = new ArrayList<StudentResponse>();
		
		for (CSVRecord csvRecord : csvRecords)
		{
			String name = csvRecord.get("studentName");
			long phoneNumber = Long.parseLong(csvRecord.get("studentPhNo"));
			String password = csvRecord.get("studentPassword");
			String email = csvRecord.get("studentEmail");
			String grade = csvRecord.get("studentGrade");
			
			Student student = new Student();
			
			student.setStudentName(name);
			student.setStudentEmail(email);
			student.setStudentPhNo(phoneNumber);
			student.setStudentPassword(password);
			student.setStudentGrade(grade);
			
			student = studentRepo.save(student);
			
			StudentResponse studentResponse = new StudentResponse();
			
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			
			studentResponses.add(studentResponse);
		}
		
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("data saved successfully");
		structure.setData(studentResponses);
		
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>> (structure, HttpStatus.CREATED);		
	}
	
	@Override
	public ResponseEntity<String> writeToCsvAtTheRate (String filePath) throws IOException {
				
		List<Student> students = studentRepo.findAll();
				
		CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.newFormat('|'));
		csvPrinter.printRecord("studentId", "studentName", "studentPhNo", "studentPassword", "studentEmail", "studentGrade");
		csvPrinter.println();
		
		for (Student student : students)
		{
			csvPrinter.printRecord(student.getStudentId(), student.getStudentName(), student.getStudentPhNo(),
					student.getStudentPassword(), student.getStudentEmail(), student.getStudentGrade());
		}
		
		csvPrinter.flush();
		return new ResponseEntity<String> ("Data transfered to CSV File !!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> checkEmail(String studentEmail, String studentPassword) {
		
		Student findByStudentEmail = studentRepo.findByStudentEmail(studentEmail);
		
		if (findByStudentEmail != null)
		{
			if (findByStudentEmail.getStudentPassword().equals(studentPassword))
			{
				return new ResponseEntity<String> ("email & password is correct", HttpStatus.FOUND);
			}
			else
			{
//				throw new PasswordIncorrectException ("Password did not matched !!");
				return new ResponseEntity<String> ("email is correct & password is incoorect", HttpStatus.NOT_FOUND);
			}
			
//			return new ResponseEntity<String> ("email & password is correct", HttpStatus.FOUND);
		}
		else
		{
			throw new StudentNotFoundByEmailException("Not Found !!");
		}
		
	}

}