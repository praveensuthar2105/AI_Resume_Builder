package com.resume_builder_backend.Resume_Builder_ai;

import com.resume_builder_backend.Resume_Builder_ai.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ResumeBuilderAiApplicationTests {
	@Autowired
	private ResumeService resumeService;
	@Test
	void contextLoads() throws IOException {
		resumeService.generateResumeResponse("I an Praveen Suthar fresher graduate from IIT Jodhpur, I am looking for a job in the field of software development. I have a strong foundation in programming languages such as Java, Python, and C++. I am also proficient in web development technologies like HTML, CSS, and JavaScript. My academic projects have given me hands-on experience in developing applications and solving complex problems. I am eager to apply my skills and knowledge in a professional setting and contribute to innovative software solutions.");
	}

}
