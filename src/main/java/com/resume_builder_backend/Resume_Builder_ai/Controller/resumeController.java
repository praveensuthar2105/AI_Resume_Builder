package com.resume_builder_backend.Resume_Builder_ai.Controller;

import com.resume_builder_backend.Resume_Builder_ai.ResumeRequest;
import com.resume_builder_backend.Resume_Builder_ai.service.ResumeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class resumeController {
	@Autowired
	private ResumeService resumeService;
	
	@PostMapping("/generate")
	public ResponseEntity<Map<String , Object>> getResumeData(@RequestBody ResumeRequest resumeRequest) throws IOException {
	 
		Map<String, Object> jsonObject = resumeService.generateResumeResponse(resumeRequest.userResumeDescription());
		return new ResponseEntity<>( jsonObject , HttpStatus.OK);
	}
}
