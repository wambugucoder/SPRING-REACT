package com.server.pollingapp.configs;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
/**
 * Stanfords NLP open source library - used for basic sentiment analysis
 */
@Configuration
public class PollSentimentAnalyzer {

    @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment, pos, lemma");
       return new StanfordCoreNLP( props );
    }
}
