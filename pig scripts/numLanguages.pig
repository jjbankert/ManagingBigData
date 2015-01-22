-- Outputs the amount of languages in the data set
register json-simple-1.1.1.jar;
register elephant-bird-hadoop-compat.jar;
register elephant-bird-pig.jar;

json_data = load '/user/s0165263/data/' using com.twitter.elephantbird.pig.load.JsonLoader('-nestedLoad');
data = foreach json_data generate $0#'place'#'country_code' AS country, $0#'lang' AS language, $0#'user'#'id_str' AS userId, $0#'id_str' AS tweetId;
data = FILTER data BY ((country is not null) AND (language != 'und')); -- Remove undetermined languages and unknown (empty) country codes
groups = FOREACH data GENERATE language;
dist = DISTINCT groups;
numLangs = FOREACH (GROUP dist ALL) GENERATE COUNT(dist);
STORE numLangs INTO 'numLangs';
