-- Outputs the share of a language in a country
register json-simple-1.1.1.jar;
register elephant-bird-hadoop-compat.jar;
register elephant-bird-pig.jar;

json_data = load '/user/s0165263/data/' using com.twitter.elephantbird.pig.load.JsonLoader('-nestedLoad');
data = foreach json_data generate $0#'place'#'country_code' AS country, $0#'lang' AS language, $0#'user'#'id_str' AS userId, $0#'id_str' AS tweetId;
data = FILTER data BY ((country is not null) AND (language != 'und')); -- Remove undetermined languages and unknown (empty) country codes
groups = GROUP data BY (country, language);
counted = FOREACH groups GENERATE FLATTEN(group), COUNT(data.tweetId) AS occurrences;
group2 = GROUP counted BY country;
sum = FOREACH group2 GENERATE FLATTEN(counted), SUM(counted.occurrences) AS total; --Needed to create a percentage next
sums = FOREACH sum GENERATE counted::group::country, counted::group::language, counted::occurrences, total, ((double)counted::occurrences/(double)total) AS percentage;
STORE sums INTO 'simplePercentagePerCountry';
-- Output format: <country code>	<language>	<#tweetsInLanguage>	<#tweetsInCountry>	<percentageOfTweetsInLanguageForCountry>