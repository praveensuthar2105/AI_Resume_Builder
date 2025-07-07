package com.resume_builder_backend.Resume_Builder_ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
//import org.json.JSONObject;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeServiceImpl  implements ResumeService {
	
	private final ChatClient chatClient;
	
	public ResumeServiceImpl(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	@Override
	public Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException {
		
		String promptString = this.loadPromptFromFile("resume_prompt.txt");
		String promptContent = this.putValueToTemplate(promptString, Map.of("userResumeDescription", userResumeDescription));
		Prompt prompt = new Prompt(promptContent);
		String response = chatClient.prompt(prompt).call().content();
		
		  Map<String , Object> stringMap =  parseMultipleResponses(response);
		return stringMap;
		
	}
	
	String loadPromptFromFile(String fileName) throws IOException {
		Path path = new ClassPathResource(fileName).getFile().toPath();
		return Files.readString(path);
	}
	
	String putValueToTemplate(String template, Map<String, String> values) {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
		}
		return template;
	}
	
	public static Map<String, Object> parseMultipleResponses(String response) {
		Map<String, Object> result = new HashMap<>();
		int thinkStart = response.indexOf("<think>") + 7;
		int thinkEnd = response.indexOf("</think>");
		if (thinkStart != -1 && thinkEnd != -1) {
			String thinkContent = response.substring(thinkStart, thinkEnd).trim();
			result.put("think", thinkContent);
		} else {
			result.put("think", null);
		}
		
		int jsonStart = response.indexOf("```json") + 7;
		int jsonEnd = response.indexOf("```", jsonStart);
		if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
			String jsonContent = response.substring(jsonStart, jsonEnd).trim();
			try {
				ObjectMapper mapper = new ObjectMapper();
				Map data = mapper.readValue(jsonContent, Map.class);
				result.put("data", data);
			} catch (Exception e) {
				System.err.println("Invalid Json format: " + e.getMessage());
				result.put("data", null);
			}
		} else {
			result.put("data", null);
		}
		return result;
	}
}
