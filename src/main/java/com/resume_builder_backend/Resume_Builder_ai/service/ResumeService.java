package com.resume_builder_backend.Resume_Builder_ai.service;

import java.io.IOException;
import java.util.Map;

public interface ResumeService {
	
	Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException;
	
}
