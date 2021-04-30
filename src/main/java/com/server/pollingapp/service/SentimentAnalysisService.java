package com.server.pollingapp.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class SentimentAnalysisService {

    final StanfordCoreNLP stanfordCoreNLP;

    public SentimentAnalysisService(StanfordCoreNLP stanfordCoreNLP) {
        this.stanfordCoreNLP = stanfordCoreNLP;
    }

    public AtomicReference<Integer> GetSentimentScoreOfPoll(String text){
        AtomicReference<Integer> sentimentScore= new AtomicReference<>(0);
        Annotation annotation= stanfordCoreNLP.process(text);
        annotation.get(CoreAnnotations.SentencesAnnotation.class).stream()
                .map(sentence->{
                    //CREATE A SENTIMENT TREE
                    Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                    //USE RECURSIVE NEURAL NETWORK TO PREDICT CLASS(0,1,2,3,4)
                    sentimentScore.set(RNNCoreAnnotations.getPredictedClass(tree));

                    return null;
                });
        return sentimentScore;
    }
}
