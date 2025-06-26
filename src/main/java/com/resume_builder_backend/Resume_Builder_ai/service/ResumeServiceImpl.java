package com.resume_builder_backend.Resume_Builder_ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
//import org.json.JSONObject;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeServiceImpl  implements ResumeService {
	
	private ChatClient chatClient;
	
	public ResumeServiceImpl(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	@Override
	public String generateResumeResponse(String userResumeDescription) throws IOException {
		
		String promptString = this.loadPromptFromFile("resume_prompt.txt");
		String promptContent = this.putValueToTemplate(promptString, Map.of("userResumeDescription", userResumeDescription));
		Prompt prompt = new Prompt(promptContent);
		String response = chatClient.prompt(prompt).call().content();
		
		JSONObject jsonObject = parseMultipleResponses(response);
		return response;
		
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
	
	public static JSONObject parseMultipleResponses(String response) {
		JSONObject jsonResponse = new JSONObject();
		
		int thinkStart = response.indexOf("<think>") + 7;
		int thinkEnd = response.indexOf("</think>");
		if (thinkStart != -1 && thinkEnd != -1) {
			String thinkContent = response.substring(thinkStart, thinkEnd).trim();
			jsonResponse.put("think", thinkContent);
		} else {
			jsonResponse.put("think", JSONObject.NULL);
		}
		
		int jsonStart = response.indexOf("```json") + 7;
		int jsonEnd = response.indexOf("```");
		if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
			String jsonContent = response.substring(jsonStart, jsonEnd).trim();
			
			try {
				JSONObject jsonObject = new JSONObject(jsonContent);
				jsonResponse.put("data", jsonObject);
			} catch (Exception e) {
				System.err.println("Invaild Json format" + e.getMessage());
			}
		} else {
			jsonResponse.put("data", JSONObject.NULL);
		}
		return jsonResponse;
	}
}
