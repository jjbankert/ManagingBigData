-- Outputs the number of languages a user from a country can tweet in
register json-simple-1.1.1.jar;
register elephant-bird-hadoop-compat.jar;
register elephant-bird-pig.jar;

json_data = load '/user/s0165263/data/' using com.twitter.elephantbird.pig.load.JsonLoader('-nestedLoad');
data = foreach json_data generate $0#'place'#'country_code' AS country, $0#'lang' AS language, $0#'user'#'id_str' AS userId, $0#'id_str' AS tweetId;
data = FILTER data BY ((country is not null) AND (language != 'und')); -- Remove undetermined languages and unknown (empty) country codes
groups = GROUP data BY (country, userId, language);
allLangs = FOREACH groups GENERATE FLATTEN(group);
allLangs = DISTINCT allLangs; -- Removing all but one tweet per user&country&language
groups2 = GROUP allLangs BY (country, userId);
spokenLangs = FOREACH groups2 GENERATE FLATTEN(group), COUNT(allLangs.language) AS numLanguages, group.userId AS foo; -- foo used to circumvent a nasty group reference 2 lines down
-- spokenLangs contains the amount of languages a certain user from a certain country can tweet in.
groups3 = GROUP spokenLangs BY (country, numLanguages);
numLangsCountry = FOREACH groups3 GENERATE FLATTEN(group), COUNT(spokenLangs.foo); -- Combine the information and give a statistical analysis.
STORE numLangsCountry INTO 'multilingualPerson';
-- Output format: <country>	<#languagesTweetedIn>	<#users>