TextMining-GATE-MovieRating
===========================

Movie Rating using twitter based Sentiment Analysis (GATE)

The processing pipeling(two) are developed in GATE (General Architecture For Text Engineering)

First pipeline will fetch all the tweets of the user selected movie and generate corpus dynamically.

The corpus generated by the first pipeline will be forwarded to second pipeline for sentiment analysis. Second pipeline contain few JAPE rules for annotating the tweets from corpus and finding out the negative or positive sentiment from perticulat tweet. In last, multinomial bayesian classifier will be applied on tweets to classify to give them a rating.

I have uploaded the slides of the project to understand the it in detail.

In the folder named "Social Scoring", I have put all OWL files, code for two pipelines described above and gazetteer list.

These two pipelines has been integrated into two clients for showing demo named "Media Wiki" and "Open office"
