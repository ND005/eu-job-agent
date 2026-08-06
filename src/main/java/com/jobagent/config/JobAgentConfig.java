package com.jobagent.config;

import com.jobagent.drafter.ApplicationDrafterAgent;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobAgentConfig {

    @Bean
    public ApplicationDrafterAgent applicationDrafterAgent(ChatLanguageModel chatLanguageModel) {
        return AiServices.builder(ApplicationDrafterAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .build();
    }
}