package com.server.pollingapp.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class SentimentAnalysisService {

    final StanfordCoreNLP stanfordCoreNLP;

    @Autowired
    public SentimentAnalysisService(@Lazy StanfordCoreNLP stanfordCoreNLP) {
        this.stanfordCoreNLP = stanfordCoreNLP;
    }

    public int GetSentimentScoreOfPoll(String text){
        int sentimentScore=0;
        Annotation annotation= stanfordCoreNLP.process(text);
        for (CoreMap sentence: annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            //CREATE A SENTIMENT TREE
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
           sentimentScore= RNNCoreAnnotations.getPredictedClass(tree);
        }
        System.out.println(sentimentScore);
        return sentimentScore;
    }
}
