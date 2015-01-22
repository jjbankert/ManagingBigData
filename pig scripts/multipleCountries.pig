-- Outputs the amount of users tweeting from different countries
register json-simple-1.1.1.jar;
register elephant-bird-hadoop-compat.jar;
register elephant-bird-pig.jar;

json_data = load '/user/s0165263/data/' using com.twitter.elephantbird.pig.load.JsonLoader('-nestedLoad');
data = foreach json_data generate $0#'place'#'country_code' AS country, $0#'lang' AS language, $0#'user'#'id_str' AS userId, $0#'id_str' AS tweetId;
data = FILTER data BY ((country is not null) AND (language != 'und')); -- Remove undetermined languages and unknown (empty) country codes
groups = GROUP data BY (country, userId);
allLocs = FOREACH groups GENERATE FLATTEN(group); -- Could also use a DISTINCT here, as this should be faster
groups2 = GROUP allLocs BY userId;
usedLocations = FOREACH groups2 GENERATE FLATTEN(group), COUNT(allLocs.country) AS numCountries; --Contains the amount of countries a user tweets from
groups3 = GROUP usedLocations BY numCountries;
numCountriesPerUser = FOREACH groups3 GENERATE FLATTEN(group), COUNT(usedLocations.group);
STORE numCountriesPerUser INTO 'countriesPerUser';
-- Output format: <#countries>	<#users>