package com.resume_builder_backend.Resume_Builder_ai.service;

import java.io.IOException;
import java.util.Map;

public interface ResumeService {
	
	String generateResumeResponse(String userResumeDescription) throws IOException;
	
}
