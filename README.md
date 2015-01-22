# Contents
* java: The Java files needed for gathering the data. The java project including maven pom.xml can be found in the 'Java code' folder.
* pig scripts: All processing scripts written in Pig Latin. All pig scripts can be found in the 'pig scripts' folder.
* visualisation: The visualisation of the results can be found in the 'googleRegionMap' folder.

# How to use
## Java files
The Java files are meant to be run whitebox; modifying code is likely necessary, as paths to storage are hardcoded for example. 

### RawTwitterStore
This program collects all tweets that contain location data. It buffers 10k tweets which are then stored in a single file. It is necessary to add your own twitter API keys and tokens to run this program. They can be easily obtained at https://apps.twitter.com/app/new.

### FileMerger
This program merges contents of 10 files into one bigger file. This was done to reduce file handling overhead. The files containing 10k tweets are combined into 100k tweet files (around 280MB).

### CountryCounter
This program was used to perform proof-of-concept analysis on the twitter data. It outputs multilingualism statistics, but much slower than the pig scripts on the cluster.

### PigOutputAnalyser
This program post processes the output from the simplePercentagePerCountry.pig script. It sorts the results and outputs top-4 + 'other' percentages. The output from the pig script is small enough that this post processing is performed within 1 second. This program also creates a pie chart image for each country with the top-4 + 'other' percentages.


## Pig scripts
The pig scripts all require 3 jar files to run, which are included for easy compiling. All scripts run from the same directory using the following commands:
```
pig <yourScript>.pig
```
Our files point to data on our big data cluster, you should use your own datapath if you want to use different data of course.

## Visualisation
1. Open map.html in your favorite browser.
2. Mouse over countries to get more information. The pie charts show the division of tweets over different languages. We also give the absolute number of tweets of a country and the 10-log of that value between brackets.
3. Feast you eyes.
